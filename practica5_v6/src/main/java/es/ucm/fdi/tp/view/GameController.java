package es.ucm.fdi.tp.view;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.model.GamePlayer.PlayerMode;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public interface GameController<S extends GameState<S,A>, A extends GameAction<S,A>> {
	public void makeManualMove(A a); //ejecuta la accion A en el modelo.
	public void makeRandomMove();
	public void makeSmartMove(); //realiza un movimiento si el jugador es rand o smart.
	public void restartGame(); //reanuda el juego
	public void stopGame(); //para el juego
	public void changePlayerMode(PlayerMode p); //cambia el modo de jugador
	public void handleEvent(GameEvent<S,A> e); //
	public PlayerMode getPlayerMode(); //Para saber si es manual, rand, smart.
	public int getPlayerId(); //numero asignado
	public S getState();
	public void startGame();
}
