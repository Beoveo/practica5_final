package es.ucm.fdi.tp.was;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.SmartPlayer;

public class WolfAndSheepStateTest {
	private WolfAndSheepAction action;
	List<WolfAndSheepAction> accionesValidas;
	
	
	@Test
	public void loboRodeado(){
		WolfAndSheepState state = new WolfAndSheepState(8);
		int[][] board = new int[8][8];
		for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++){
            	if((i == 4 && j == 4)) board[i][j] = 0;
            	else if((i == 3 && j == 3) || (i == 3 && j == 5) || (i== 5 && j == 3) || (i == 5 && j == 5)) board[i][j] = 1;
            	else board[i][j] = -1;
            }
        }
		WolfAndSheepState state2 = new WolfAndSheepState(state, board, false, -1);
		assertEquals(true,WolfAndSheepState.isWinner(board, 1));
		
	}
	
	@Test 
	public void loboEnLaPrimeraFila(){
		WolfAndSheepState state = new WolfAndSheepState(8);
		int[][] board = new int[8][8];
		for (int i=0; i<8; i++) {
            for (int j=0; j<8; j++){
            	if((i == 0 && j == 0)) board[i][j] = 0;
            	else if((i == 3 && j == 3) || (i == 3 && j == 5) || (i== 5 && j == 3) || (i == 5 && j == 5)) board[i][j] = 1;
            	else board[i][j] = -1;
            }
        }
		WolfAndSheepState state2 = new WolfAndSheepState(state, board, false, -1);
		assertEquals(true,WolfAndSheepState.isWinner(board, 0));
	}
	
	@Test
	public void testPrimerMovimientoLoboSoloTieneUnaOpcion() {
		WolfAndSheepState state = new WolfAndSheepState(8);
		assertEquals(1, state.validActions(0).size());
		
	}
	
	@Test
	public void testSegundoMovimientoLoboTieneCuatroOpciones(){
		WolfAndSheepState state = new WolfAndSheepState(8);
		state = estadoDosLobo(state);
		assertEquals(4, state.validActions(0).size());
	}
	
	public static <S extends GameState<S, A>, A extends GameAction<S, A>> S estadoDosLobo(S state){
		S nextState = state;
		List<GamePlayer> players = new ArrayList<GamePlayer>(); 
		players.add( new SmartPlayer("Lobo", 5)); 
		players.add( new SmartPlayer("Oveja", 5));
		for(int i = 0; i < 2; i++){
			A action = players.get(state.getTurn()).requestAction(state);
			nextState = action.applyTo(state);
		}
		return nextState;
	}
	
	
}