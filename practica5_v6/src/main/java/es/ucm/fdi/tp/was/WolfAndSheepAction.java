package es.ucm.fdi.tp.was;


import es.ucm.fdi.tp.base.model.GameAction;


public class WolfAndSheepAction implements GameAction<WolfAndSheepState, WolfAndSheepAction>{
	
	private int fila;
	private int columna;
	private int numJugador;
	private int lastRow;
	private int lastCol;
	
	public WolfAndSheepAction(int jugador, int row, int col, int lastRow, int lastCol){
		this.numJugador = jugador;
		this.fila = row;
		this.columna = col;
		this.lastRow = lastRow;
		this.lastCol = lastCol;
	}
	
	/**
	 * Metodo que devuelve si el jugador es el numero 0 u el numero 1.
	 * Es decir nos devuelve si es el lobo(0) o la oveja(1).
	 */
	@Override
	public int getPlayerNumber() {
		return numJugador;
	}
	
	/**
	 * Metodo que aplica el movimiento al estado actual. Primero comprueba que es el turno del jugador, sino lo es lanza una excepcion.
	 * Se hace una llamada al metodo getBoard(), el cual hace una copia del tablero. Despues, elimina la ficha del jugador y la establece en
	 * la casilla indicada.
	 * Se calculan las acciones validas del siguiente jugador y se le asigna a accionesValidas.
	 * Por ultimo se comprueba si el jugador actual es el ganador, si lo es devuelve un nuevo objeto WolfAndSheepState
	 * con el estado actual, el tablero, con winner = true y el turno del jugador actual.
	 * En caso contrario, se devuelve un nuevo objeto WolfAndSheepState con winner = false y turno = -1.
	 * 
	 * @return Devuelve next, que es el nuevo objeto de WolfAndSheepState.
	 */
	@Override
	public WolfAndSheepState applyTo(WolfAndSheepState state) {
		 if (numJugador != state.getTurn()) throw new IllegalArgumentException("Not the turn of this player");
	        
	        int[][] board = state.getBoard(); //copia el tablero
	        board[lastRow][lastCol] = -1; //borra ficha
	        //meter un null.
	        board[fila][columna] = numJugador; //mueve la ficha
	        
	        
	        // actualiza estado
	        WolfAndSheepState next;
	        
	        if (WolfAndSheepState.isWinner(board, state.getTurn())) next = new WolfAndSheepState(state, board, true, state.getTurn());
	        else next = new WolfAndSheepState(state, board, false, -1);
	   
	        return next;
	}
	
	/**
	 * Metodo que devuelve la fila a la que se va a mover el jugador.
	 * @return entero fila.
	 */
	public int getRow(){
		return fila;
	}

	/**
	 * Metodo que devuelve la columna a la que se va a mover el jugador.
	 * @return entero columna.
	 */
	public int getCol(){
		return columna;
	}
	
	
	
	/**
	 * Metodo toString que muestra por pantalla el jugador y la accion que va a realizar este.
	 * Es decir, indica a que posicion del tablero se va a mover el jugador.
	 */
	public String toString() {
        return "place " + numJugador + " at (" + fila + ", " + columna + ")";
    }
}
