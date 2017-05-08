package es.ucm.fdi.tp.view;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameTable;

public class ConsoleController<S extends GameState<S,A>, A extends GameAction<S,A>> implements Runnable {
	private List<GamePlayer> players;
	private GameTable<S, A> game;
	
	public ConsoleController(GameTable<S,A> game, List<GamePlayer> players){
		this.game = game;
		this.players = players;
	}
	
	@Override
	public void run() {
			int playerCount = 0;
			for (GamePlayer p : players) {
				p.join(playerCount++); //asignamos a cada jugador un numero (playerNumber)
			}
		
			S currentState = (S) game.getState(); //estado inicial
			game.start(); //iniciamos el juego
			
			while (!currentState.isFinished()) {
				// request move
				A action = (A) players.get(currentState.getTurn()).requestAction(currentState);
			
				game.execute(action);  // apply move
				currentState = game.getState(); // actualizamos el estado
			}
			
			if (currentState.isFinished()) game.stop(); // game over
			
		}
}

