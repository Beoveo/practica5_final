package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;


public class ConsoleView<S extends GameState<S,A>, A extends GameAction<S,A>> implements GameObserver<S,A>{

	private GameEvent<S,A> event;
	
	public ConsoleView(GameObservable<S, A> gameTable) {
		gameTable.addObserver(this);
	}
	
	public GameEvent<S,A> getEvent(){
		return event;
	}
	
	public void notifyType(EventType type){
		
		switch(type){
		case Start:
			System.out.println("Comienza el juego !");
			System.out.println(event.getState().toString());
			break;
		case Stop:
			
			boolean partidaAcabada = event.getState().isFinished();
			if(partidaAcabada) notifyWinner();
			System.out.println("Fin del juego !");
			break;
			
		case Change:
			System.out.println("El estado del juego ha cambiado!" + System.getProperty("line.separator") + 
			 "Es el turno del jugador " + event.getAction().getPlayerNumber() + " ." + System.getProperty("line.separator") +
			"Jugador "+ event.getAction().getPlayerNumber() + " : " + event.getAction().toString());
			System.out.println(event.getState().toString());
			break;
		case Error:
			System.out.println("Se ha producido un error durante el juego!");
			System.out.println(event.getError().getMessage());
			break;
		case Info:
			System.out.println("Informacion sobre el juego.");
			break;
		default:
			break;
		}
	}
	
	public void notifyWinner(){
		String endText = " ";
		int winner = event.getState().getWinner(); 
		
		if (winner == -1) 
			endText += "Empate!";
	    else 
			endText += "Jugador " + winner + " :" + " ha ganado la partida!";
		
		System.out.println(endText);
	}

	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		this.event = e;
		notifyType(event.getType());
	}

}


