package es.ucm.fdi.tp.was;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameState;


@SuppressWarnings("serial")
public class WolfAndSheepState extends GameState<WolfAndSheepState, WolfAndSheepAction>{
	
	private  ArrayList<WolfAndSheepAction> validActions;
	 
	private final int turn;
    private final boolean finished;
    private final int[][] board;
    private final int winner;
    
    private final int lastRow;
    private final int lastCol;

    private final int dim;

    final static int EMPTY = -1;
    
    /**
     * Constructora que inicializa las referencias turn, finished, board, winner cada vez que se llama a un objeto de tipo
     * WolfAndSheepState.
     * Los for anidados recorren el tablero y lo inicializan con las 4 ovejas al principio del tablero y con el lobo al final de el.
     * El resto de casillas estan vacias (EMPTY = -1).
     * @param dim: Dimension del tablero
     */
	public WolfAndSheepState(int dim) {
		super(2);
		
		if (dim != 8) {
            throw new IllegalArgumentException("Expected dim to be 8");
        }

        this.dim = dim;
        board = new int[dim][];
        for (int i=0; i<dim; i++) {
            board[i] = new int[dim];
            for (int j=0; j<dim; j++){
            	if((i == 0 && j == 1) ||(i == 0 && j == 3) || (i == 0 && j == 5) || (i == 0 && j == 7)) board[i][j] = 1;
            	else if(i == 7 && j == 0) board[i][j] = 0;
            	else board[i][j] = EMPTY;
            }
        }
       
        this.turn = 0;
        this.winner = -1;
        this.finished = false;
        
        this.lastRow = -1;
        this.lastCol = -1;
	}
	
	
    /**   
     * Constructora que inicializa la dim del proximo estado, el tablero, el siguiente turno,
     * el estado del turno (finished -> true || false), el ganador. Además le pasa a la superclase GameState el numero 
     * de jugadores.
     * @param prev
     * @param board
     * @param finished
     * @param winner
     */
    public WolfAndSheepState(WolfAndSheepState prev, int[][] board, boolean finished, int winner) {
    	super(2);
    	this.dim = prev.dim;
        this.board = board;
        this.turn = (prev.turn + 1) %2;
        this.finished = finished;
        this.winner = winner; 
        
       this.lastRow = prev.lastRow();
       this.lastCol = prev.lastCol();
    }  

    public int lastRow(){
    	return lastRow;
    }
    
    public int lastCol(){
    	return lastCol;
    }
	/**
	 * Metodo que crea un nuevo objeto, que en este caso es una matriz de enteros y se inicializa
	 * con el tamaño del tablero. Despues se recorre todas las filas del tablero y clona lo que hay dentro de cada
	 * casilla en el tablero copia.
     * @return Una copia del tablero.
     */
    public int[][] getBoard() {
        int[][] copy = new int[board.length][];
        for (int i=0; i<board.length; i++) copy[i] = board[i].clone();
        return copy;
    }
    
    
    
    
    /**
     * Metodo estatico que comprueba si el lobo ha ganado. Es decir, recorre el tablero segun las coordenadas que le
     * hemos dado y si se cumple que el jugador es el lobo, comprueba si en la casilla esta el lobo, si no lo esta
     *  won es false, entonces sale del for y devuelve won = false.
     * Si el jugador es la oveja inicializamos won a false y comprobamos con las coordenadas dadas, si en esa casilla se 
     * encuentra la oveja, si devuelve true, won = true. Por lo tanto devolveria que la oveja a ganado.
     * Si no esta la oveja en esas coordenadas, no ha ganado y por lo tanto won = false;
     * 
     * @param board: Tablero del juego.
     * @param playerNumber: Numero asignado a cada jugador.
     * @param x0: Indice de x. Corresponderia a nuestra coordenada j del tablero.
     * @param y0: Indice de y. Corresponderia a nuestra coordenada i del tablero.
     * @param dx: Controlan la siguiente posicion a comprobar. (j)
     * @param dy: Controlan la siguiente posicion a comprobar. (i)
     * @return: Devuelve true, si el lobo a llegado a la fila inicial donde se encontraban las ovejas.
     * Devuelve false si las ovejas han acorralado al lobo.
     */
    private static boolean isWinner(int[][] board, int playerNumber, 
    		int x0, int y0, int dx, int dy) {
        boolean won = false;
        
        
        if(playerNumber == 0){
         for(int i=0, x=x0, y=y0; !won && i<board.length; i++) if (board[y][x] == playerNumber) won = true;
        }
        
        if(playerNumber == 1){
        	if(dy < 0 || dy >= board.length || dx < 0 || dx >= board.length) won = true;
        	else
        		if(board[dx][dy] == playerNumber) won = true;
        }
        
        return won;
    }
  

    /**
     * Metodo que realiza una llamada recursiva a isWinner().
     * Primero comprueba si las ovejas tienen movimientos, si no pueden moverse mas porque han llegado al final del
     * tablero, el lobo gana.
     * Despues, comprueba si el lobo esta en la fila final contraria a la que el empezo,
     * es decir, si se encuentra en la fila [0], columna [1|3|5|7], solo comprueba los movimientos en diagonal,
     * por lo que j aumenta de 2 en 2. Si se encuentra en alguna de esas posiciones, el metodo devuelve true.
     * 
     * Si el lobo se encuentra acorralado, es decir, si no puede mover ni arriba/abajo a la izquierda/derecha en diagonal,
     * el metodo devuelve false. 
     * @param board: Tablero del juego.
     * @param playerNumber: Numero del jugador.
     * @return: Este metodo solo devuelve si el lobo gana (true) o pierde (false).
     */
    public static boolean isWinner(int[][] board, int playerNumber) {
        boolean won = false;
        int cont;
        for (int i=0; !won && i<board.length; i++) {
        	if(playerNumber == 1){
        		for(int j=0; !won && j<board.length; j++){
        			if(board[i][j] == 0){
        				cont = 0;
        				if(isWinner(board, playerNumber, i, j, i - 1, j + 1)) cont++;
        				if(isWinner(board, playerNumber, i, j, i - 1, j - 1)) cont++;
        				if(isWinner(board, playerNumber, i, j, i + 1, j - 1)) cont++;
        				if(isWinner(board, playerNumber, i, j, i + 1, j + 1)) cont++;
        				if(cont == 4) won = true;
        			}
        		}
        	}else{
	        	if (isWinner(board, playerNumber, i, 0, 0, 0)) won = true; //comprueba si lobo esta en la fila i = 0
        	}
        }
        return won;
        }
    
    
	/**
	 * Metodo que comprueba si ha acabado el turno del jugador. Si ha acabado entonces no hay movimiento valido.
	 * Si no ha acabado, se devuelve la celda vacia o lo que es lo mismo, un movimiento valido.
	 * @param action: Una accion cualquiera
	 * @return Celda vacia == Celda valida.
	 */
	public boolean isValid(WolfAndSheepAction action) {
		boolean encontrado = false;
	       int i = 0;
	       int actionsSize = validActions.size();
	       
	       while(!encontrado && (i < actionsSize)){
	    	   if(action.toString().equalsIgnoreCase(validActions.get(i).toString()))
	    		   encontrado=true;
	    	   i++;
	       }
	        return encontrado;
    }
	/**
	 * Metodo que devuelve una lista de acciones validas para un jugador.
	 * Crea un nuevo objeto de ArrayList (valid), comprueba si se ha acabado la partida, en caso de que finished sea true,
	 * devuelve valid vacio.
	 * Si no se ha acabado la partida recorre el tablero. Si en la casilla esta la ficha del jugador actual (si es el turno de ese jugador),
	 * se comprueba en el caso de que sea el lobo (jugador 0): 
	 * 	-Si la posicion de la casilla de arriba a la izquierda esta dentro del rango del tablero y si esta vacia,
	 * 	 se incluye la casilla como un movimiento valido.
	 * 	 Lo mismo ocurre cuando se comprueban las casillas de arriba a la derecha, abajo izquierda/derecha.
	 * Si el turno es de las ovejas (jugador 1), estas solo podran moverse hacia abajo, no pueden subir por el tablero.
	 * - Si la posicion de la casilla de abajo izquierda/ derecha esta dentro del rango del tablero y esta vacia,
	 * 	 se incluye la casilla como un movimiento valido. 
	 * 
	 * @return Devuelve el objeto valid con todos los movimientos validos para el jugador del que sea el turno.
	 */
	@Override
	public List<WolfAndSheepAction> validActions(int playerNumber) {
		ArrayList<WolfAndSheepAction> valid = new ArrayList<>();
		this.validActions = valid;
		
        if (finished) return validActions;
        
        if(playerNumber == 1){
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                	if ((at (i, j) == 1) && dentroDelRango(i + 1, j - 1) && at(i + 1, j - 1) == -1) validActions.add(new WolfAndSheepAction(playerNumber , i + 1, j - 1, i, j)); //ABAJO IZQ
                	if((at (i, j) == 1) && dentroDelRango(i + 1, j + 1) && at(i + 1, j + 1) == -1)  validActions.add(new WolfAndSheepAction(playerNumber, i + 1, j + 1, i, j)); //ABAJO DER
                }
            }
         }
    
         if(playerNumber == 0){
            for (int i = 0; i < dim; i++) {
                for (int j = 0; j < dim; j++) {
                    if ((at (i, j) == 0) && dentroDelRango(i - 1, j - 1) && at(i - 1, j - 1) == -1 ) validActions.add(new WolfAndSheepAction(playerNumber, i - 1, j - 1, i, j)); //ARRIBA IZQ
                    if ((at (i, j) == 0) && dentroDelRango(i + 1, j - 1) && at(i + 1, j - 1) == -1 ) validActions.add(new WolfAndSheepAction(playerNumber, i + 1, j - 1, i, j)); //ABAJO IZQ
                    if ((at (i, j) == 0) && dentroDelRango(i - 1, j + 1) && at(i - 1, j + 1) == -1 ) validActions.add(new WolfAndSheepAction(playerNumber, i - 1, j + 1, i, j)); //ARRIBA DER
                    if((at (i, j) == 0) && dentroDelRango(i + 1, j + 1) && at(i + 1, j + 1) == -1 ) validActions.add(new WolfAndSheepAction(playerNumber, i + 1, j + 1, i, j)); //ABAJO DER
                }
            }
         }
        
        return validActions;
	}
	
	/**
	 * Metodo que comprueba que la fila y columna seleccionadas no se salen del array. (ArrayOutIndexException)
	 * @param row: Fila
	 * @param col: Columna
	 * @return: True, si esta dentro de rango.
	 * False, si se sale del array.
	 */
	public boolean dentroDelRango(int row, int col){
		if(row > -1 && col > -1 && row < 8 && col < 8 )return true;
		else return false;
	}
	
	/**
     * Devuelve el turno del jugador.
     * 0 -> Turno del lobo.
     * 1 -> Turno de las ovejas.
     */
	@Override
	public int getTurn() {
		return turn;
	}
	
	
	
	/**
	 * 
	 * @param row: Fila del tablero
	 * @param col: Columna del Tablero
	 * @return	Devuelve el contenido de la posicion indicada por row y col (int).
	 */
	public int at(int row, int col) {
        return board[row][col];
    }
	
	/**
	 * Devolvemos el argumento de finalizacion de partida.
	 * Si true -> acabada la partida.
	 * Si false -> continua la partida.
	 */
	@Override
	public boolean isFinished() {
		return finished;
	}

	
	/**
	 * Devuelve el ganador de la partida.
	 * 0 -> Ganador lobo.
	 * 1 -> Ganador ovejas.
	 */
	@Override
	public int getWinner() {
		return winner;
	}

	 public String toString() {
	        StringBuilder sb = new StringBuilder();
	        for (int i=0; i<board.length; i++) {
	            sb.append("|");
	            for (int j=0; j<board.length; j++) {
	                sb.append(board[i][j] == EMPTY ? "   |" : board[i][j] == 0 ? " O |" : " X |");
	            }
	            sb.append("\n");
	        }
	        return sb.toString();
	    }
	
	
}
