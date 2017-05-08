package es.ucm.fdi.tp.view;


import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;

public class GameContainer<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S,A> implements GameObserver<S,A> {
	private GUIView<S, A> gameView;
	private MessageViewer<S, A> messageViewer;
	private PlayersInfoViewer<S, A> playersInfoViewer;
	private ControlPanel<S, A> controlPanel;
	private GameController<S, A> gameCtrl;
	
	private S state;
	
	public GameContainer(GUIView<S, A> gameView, GameController<S, A> gameCtrl,
			 GameObservable<S, A> game){
		this.gameView = gameView;
		this.gameCtrl = gameCtrl;
		initGUI(this.gameView);
		game.addObserver(this);
	}
	
	private void initGUI(GUIView<S, A> gameView) {
		playersInfoViewer = new PlayersInfoComp<S, A>(gameView);
		messageViewer = new MessageViewerComp<S, A>(gameView); 
		controlPanel = new ControlPanel<S, A>(gameView);
		controlPanel.setGameController(gameCtrl);
	}
	
	@Override
	public void notifyEvent(GameEvent e) {
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() { handleEvent(e); }
		});
	}

	private void handleEvent(GameEvent<S,A> e) {	
		switch ( e.getType() ) {
		case Change:
				messageViewer.addContent("El jugador ha movido!" + System.getProperty("line.separator") + 
					"Turno de jugador " + e.getAction().getPlayerNumber() + " ." + System.getProperty("line.separator") +
						"Jugador "+ e.getAction().getPlayerNumber() + " : " + e.getAction().toString() + System.getProperty("line.separator"));
				this.gameView.update(e.getState()); 
				break;
		case Error:
			messageViewer.addContent("Se ha producido un error durante el juego!");
			messageViewer.addContent(e.getError().getMessage());
				//disable();
			break;
		case Info:
				update(e.getState());
			break;
		case Start:
			update(e.getState()); 
				messageViewer.addContent("Comienza el juego !");
				initGUI(this.gameView);
			break;
		case Stop:
			
			boolean partidaAcabada = e.getState().isFinished();
			if(partidaAcabada) messageViewer.addContent(notifyWinner(e));
			messageViewer.addContent("Fin del juego !");
			 
			quit(e);
			this.gameView.update(e.getState()); 
			break;
		}
		SwingUtilities.invokeLater(new Runnable() { 
			 public void run() { gameCtrl.handleEvent(e); } 
		});
	} 
	
	private void quit(GameEvent<S,A> e){
		int n = JOptionPane.showOptionDialog(new JFrame(),
				notifyWinner(e) + System.getProperty("line.separator"), "Reiniciar el juego?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);

		if (n == 0) {
		this.gameCtrl.startGame(); 
			
		}else if (n == 1)System.exit(0); //Cierra la ventana.
		
		
	}
	
	private String notifyWinner(GameEvent<S,A> event) {
		String endText = " ";
		int winner = event.getState().getWinner();
		if (winner == -1) 
			endText += "Empate!" + System.getProperty("line.separator");
	    else 
			endText += "Jugador " + winner + " :" + " ha ganado la partida!"+ System.getProperty("line.separator");
		messageViewer.addContent(endText);
		return endText;		
	}

	@Override
	public void enable() {
		messageViewer.enable();
		playersInfoViewer.enable();
		controlPanel.enable();
		gameView.disable();
	}
	@Override
	public void disable() {
		messageViewer.disable();
		playersInfoViewer.disable();
		controlPanel.disable();
		gameView.disable();
	}
	@Override
	public void update(S state) {
		this.state = state;
	}
	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
		this.messageViewer = infoViewer;
	}
	
	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {
		this.playersInfoViewer = playersInfoViewer;
	}
	@Override
	public void setGameController(GameController<S, A> gameCntrl) {
		gameView.setGameController(gameCntrl);
		messageViewer.setGameController(gameCntrl);
	}

}
