package es.ucm.fdi.tp.mainpr4;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.exception.NotValidArgumentException;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class MainLauncher {
	
	
	/**
	 * Metodo que parsea el nombre del juego al que se quiere jugar. 
	 * - Si el nombre del juego es Ttt, se devuelve un nuevo estado del juego TicTacToe con su dimension del
	 * tablero, 3x3.
	 * 
	 * - Si el nombre del juego es Was, se devuelve un nuevo estado del juego WolfAndSheep con su dimension del
	 * tablero, 8x8.
	 * 
	 * - Si no es ninguna de las dos opciones disponibles, salta una excepcion.
	 * @param gameName: Nombre del juego.
	 * @return Nuevo objeto del estado de ese juego.
	 */
	public static GameState<?,?> createInitialState(String gameName){
		if(gameName.equalsIgnoreCase("TTT")) return new TttState(3);
		else if(gameName.equalsIgnoreCase("WAS")) return new WolfAndSheepState(8);
		else throw new NotValidArgumentException("Error: El juego introducido es incorrecto o no existe.");
	}
	
	
	
	/**
	 * Metodo que parsea el tipo de jugador, comprueba si el string playerType coincide con alguno de los tres tipos posibles,
	 * (Console, Rand, Smart). 
	 * -Si coincide devuelve un nuevo objeto de ese tipo de jugador, con el nombre del jugador.
	 * -Si no coincide con ninguno devuelve un mensaje de error.
	 * @param playerType: Tipo de jugador.
	 * @param playerName: Nombre del jugador.
	 * @return Nuevo objeto del tipo de jugador seleccionado.
	 */
	@SuppressWarnings("resource")
	public static GamePlayer createPlayer(String gameName, String playerType, String playerName){
		Scanner s = new Scanner(System.in);
		if(playerType.equalsIgnoreCase("CONSOLE")) return new ConsolePlayer(playerName, s);
		else if(playerType.equalsIgnoreCase("RAND")) return new RandomPlayer(playerName);
		else if(playerType.equalsIgnoreCase("SMART")) return new SmartPlayer(playerName, 5); //5, es el numero de niveles del juego.
		else throw new NotValidArgumentException("Error: El tipo de jugador introducido es incorrecto o no existe.");
	} 
	
	
	/**
	 * Metodo que inicializa el contador de jugadores. Se recorre la lista de jugadores y los incluye en el juego.
	 * -Mientras el estado actual no haya acabado, se pide un movimiento para el jugador del turno actual.
	 * Cuando se obtiene "action", esta se aplica al estado actual y se muestra el tablero despues de hacer el movimiento.
	 * Despues, se comprueba si tras realizar el movimiento indicado la partida a finalizado. Si la partida no ha terminado, se siguen aplicando movimientos
	 * hasta que acabe el juego, es decir, hasta que un jugador gane e isFinished() se vuelva true. Entonces se devuelve el estado
	 * del ganador, winner a 1 (Si gana).
	 *- Si la partida ha acabado, se muestra un mensaje de que el juego ha terminado y tambien se muestra el ganador.
	 * @param initialState: Tipo de juego.
	 * @param players: Lista de jugadores.
	 * @return El ganador al final del juego.
	 */
	public static <S extends GameState<S, A>, A extends GameAction<S, A>> int playGame(GameState<S, A> initialState,
			List<GamePlayer> players) {
		int playerCount = 0;
		for (GamePlayer p : players) {
			p.join(playerCount++); // welcome each player, and assign
			// playerNumber
		}
		@SuppressWarnings("unchecked")
		S currentState = (S) initialState;

		while (!currentState.isFinished()) {
			// request move
			A action = players.get(currentState.getTurn()).requestAction(currentState);
			// apply move
			currentState = action.applyTo(currentState);
			System.out.println("After action:\n" + currentState);

			if (currentState.isFinished()) {
				// game over
				String endText = "The game ended: ";
				int winner = currentState.getWinner();
				if (winner == -1) {
					endText += "draw!";
				} else {
					endText += "player " + (currentState.getTurn() + 1) % 2 + " (" + players.get(winner).getName() + ") won!";
				}
				System.out.println(endText);
			}
		}
		return currentState.getWinner();
	}

	
	/**
	 * Metodo que repite el juego las veces que se hayan indicado en times.
	 * Primero se crea una Lista del tipo GamePlayer y se incluyen los jugadores.
	 * Se llama a playGame (times == n veces) pasandole como referencia el estado inicial y los jugadores. Este devuelve si el ganador ha sido el jugador 0
	 * o el jugador 1. Si ha sido el 0 o el 1, dicho contador aumenta, y se imprime por pantalla el resultado de la partida.
	 * 
	 * @param initialState: Nuevo objeto del tipo GameState, del juego seleccionado.
	 * @param players: Lista de los jugadores.
	 * @param times: Numero de veces que se quiere jugar.
	 */
	public static void match(GameState<?, ?> initialState, GamePlayer player0, GamePlayer player1, int times) {
		int va = 0, vb = 0;
		
		List<GamePlayer> players = new ArrayList<GamePlayer>(); 
		players.add(player0); 
		players.add(player1); 

		for (int i = 0; i < times; i++) {
			switch (playGame(initialState, players)) {
			case 0:
				va++;
				break;
			case 1:
				vb++;
				break;
			}
		}
		System.out.println("Result: " + va + " for " + player0.getName() + " vs " + vb + " for " + player1.getName());
	}
	
	
	/**
	 * Metodo main que pone en marcha el juego. Lee por teclado todos los datos necesarios:
	 * Primero pide el juego y despues el tipo de jugadores y sus nombres.
	 * Cuando se pide elegir juego, se pasa el nombre del juego como referencia a createInitialState, el cual
	 * parsea el nombre del juego y devuelve un nuevo objeto del juego del tipo GameState.
	 * 
	 * Al pedir el tipo y nombre del jugador, se llama a createPlayer pasandole como referencia el nombre del juego,
	 * el tipo de jugador y el nombre de este, el metodo devuelve un nuevo objeto del tipo GamePlayer. Esto lo hace con los dos jugadores.
	 * 
	 * Si todos los datos introducidos son validos, se llama al metodo match, al cual se le pasa como referencia
	 * el objeto del juego, los dos jugadores y el numero de veces que queremos jugar. En este caso hemos establecido como
	 * estandar que se juegue una partida. El metodo match pone en marcha el juegollamando a playGame.
	 *  
	 * @param args
	 */
	public static void main(String... args){
		GameState<?, ?> iniState = null;
		GamePlayer player0 = null, player1 = null;
		/* args[0] juego
		 * args[1] tipo jug 1
		 * args[2] tipo jug 2
		 * args[3] nombre jug1
		 * args[4] nombre jug2
		 */
		
		if (args.length < 3)throw new NotValidArgumentException("Error: Falta de argumentos.");
		try{
			System.out.println("El juego seleccionado ha sido : " + args[0]);  
				iniState = createInitialState(args[0]);
		
				player0 = createPlayer (args[0], args[1], args[3]);
				
			System.out.println("Nombre del jugador 1: " + args[3]);   
		
				player1 = createPlayer (args[0], args[2], args[4]);
				
			System.out.println("Nombre del jugador 2: " + args[4]);
			System.out.println("Inicio de partida: ");
		
			match(iniState, player0, player1, 5); //Aqui se llama a playGame y comienza el juego.
			
		} catch (GameError e){
			System.out.println(e);
		}
	}
}