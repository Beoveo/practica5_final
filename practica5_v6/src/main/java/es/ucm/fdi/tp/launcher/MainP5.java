package es.ucm.fdi.tp.launcher;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.SmartPlayer;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.view.ConsoleController;
import es.ucm.fdi.tp.view.ConsoleView;
import es.ucm.fdi.tp.view.GUIController;
import es.ucm.fdi.tp.view.GUIView;
import es.ucm.fdi.tp.view.GameContainer;
import es.ucm.fdi.tp.view.GameController;
import es.ucm.fdi.tp.view.TicTacToeView;
import es.ucm.fdi.tp.view.WolfAndSheepView;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

import javax.swing.SwingUtilities;

public class MainP5 {
	
	/**
	 * Metodo que genera un nuevo objeto GameTable a partir del gType que nos pasan como parametro.
	 * Se comprueba si el gType coincide con el juego Ttt o Was y si es alguno de los dos, se genera un nuevo estado de 
	 * ese juego (estado inicial) y tambien se genera un nuevo GameTable con ese estado inicial.
	 * 
	 * En caso de que el tipo de juego pasado por parametro no coincida con Ttt o Was, se devuelve un objeto GameTable null.
	 * La excepcion saltara en el main.
	 * 
	 * @param gType: Tipo de juego. Ttt o Was.
	 * @return Nuevo objeto GameTable.
	 */
	private static GameTable<?,?> createGame(String gType){
		GameTable<?, ?> table = null;
		
		
		if(gType.equalsIgnoreCase("ttt")){
			TttState iniState = new TttState(3);
			table = new GameTable<TttState, TttAction>(iniState);
		}
		
		if(gType.equalsIgnoreCase("was")){
			WolfAndSheepState iniState = new WolfAndSheepState(8);
			table = new GameTable<WolfAndSheepState, WolfAndSheepAction>(iniState);
		}
		
		return table;
	}
	
	/**
	 * Metodo que pone en marcha el modo de juego por consola.
	 * Incluye a todos los jugadores con su modo de juego en una lista, que posteriormente se pasara como parametro a un nuevo objeto de console
	 * controller.
	 * 
	 * Despues creamos nuevos objetos de ConsoleView y ConsoleController para que se ejecute el modo consola.
	 * 
	 * @param game: Tipo de juego, GameTable.
	 * @param playerModes: Array de strings que contiene el modo de juego de cada jugador.
	 */
	
	private static <S extends GameState<S,A>, A extends GameAction<S, A>> void startConsoleMode(
			GameTable<S,A> game, String playerModes[]){
		
		List<GamePlayer> players = gamePlayers(playerModes);
		
		new ConsoleView<S,A>(game);
		new ConsoleController <S,A>(game,players).run();
	}
	
	/**
	 * Metodo auxiliar que devuelve una lista con los jugadores y sus modos de juego (Smart, Random, Manual).
	 * Primero se comprueba si el jugador es del tipo SMART, RANDOM o MANUAL. Una vez comprobado se genera un nuevo objeto de ese modo
	 * de jugador y se incluye en la lista.
	 * @param playerModes: Modo de juego de los jugadores.
	 * @return Lista con los jugadores.
	 */
	private static List<GamePlayer> gamePlayers(String[] playerModes){
		 List<GamePlayer> players = new ArrayList<GamePlayer>();
		
		 Scanner s = new Scanner(System.in);
		 
		 GamePlayer p1 = null;
		 GamePlayer p2 = null;
		
		 
		 if(playerModes[0].equalsIgnoreCase("smart")) p1 = new SmartPlayer("Jugador 1", 5);
		 else if(playerModes[0].equalsIgnoreCase("random")) p1 = new RandomPlayer("Jugador 1");
		 else if(playerModes[0].equalsIgnoreCase("manual")) p1 = new ConsolePlayer("Jugador 1", s);
		 
		 players.add(p1);
		 p1.join(0);
		 
		 if(playerModes[1].equalsIgnoreCase("smart")) p2 = new SmartPlayer("Jugador 2", 5);
		 else if(playerModes[1].equalsIgnoreCase("random")) p2 = new RandomPlayer("Jugador 2");
		 else if(playerModes[1].equalsIgnoreCase("manual")) p1 = new ConsolePlayer("Jugador 2", s);
		 
		 players.add(p2);
		 p2.join(1);
		 
		 return players;
	}
	
	/**
	 * Metodo usado por el modo de juego GUI, el cual crea una nueva vista segun el tipo de juego.
	 * Si el juego es Ttt, se crea una nueva vista de TicTacToe y lo mismo ocurre si es Was.
	 * En caso de no coincidir con los dos tipos de juego posibles, devuelve null.
	 * 
	 * @param gType: Tipo de juego (Ttt o Was).
	 * @return Nueva vista segun el tipo de juego.
	 */
	private static GUIView<?, ?> createGameView(String gType){
		GUIView<?, ?> gView = null;
		if(gType.equalsIgnoreCase("ttt")) gView = new TicTacToeView();
		if(gType.equalsIgnoreCase("was")) gView = new WolfAndSheepView();
		return gView;
	}
	
	
	private static <S extends GameState<S,A>, A extends GameAction<S, A>> void startGUIMode(
			String gType, GameTable<S,A> game, String playerModes[]){
		
		for(int i = 0; i < game.getState().getPlayerCount(); i++){
			final int id = i; //no dejaba pasarle la i porque se modificaba.
			GamePlayer p1 = new RandomPlayer("Jugador 1");
			GamePlayer p2 = new SmartPlayer("Jugador 2", 5);
			p1.join(0);
			p2.join(1);
 
			 try {
				 SwingUtilities.invokeAndWait(new Runnable() { 
					 @SuppressWarnings("unchecked")
					public void run() {
						GUIView<S, A> guiView = (GUIView<S, A>) createGameView(gType);
						GameController<S, A> gameCtrlPlayer = new GUIController<S,A> (id, p1, p2, game);
						guiView.setGameController(gameCtrlPlayer);
						GUIView<S,A> container = new GameContainer<S, A>(guiView, gameCtrlPlayer, game);
						
						guiView.setTitle(gType.toUpperCase() + " View of Jugador " + id);
					 }
				 });
				 
			 } catch (InvocationTargetException | InterruptedException e) {
				 	
				 System.err.println("Some error occurred while creating the view ...");
				 System.exit(1);
			}
		
	  }
		
		SwingUtilities.invokeLater(new Runnable() { 
			public void run() { 
				game.start(); 
			} 
		});
   }
	
	
	/**
	 * Metodo que muestra por pantalla un mensaje de ayuda. Solo se llamara al metodo cuando alguno de los argumentos
	 * introducidos sea incorrecto.
	 */
	private static void usage(){
		System.out.println("********** HELP MENU **********" + System.getProperty("line.separator") +
				"1- Introduzca GAME: ttt o was. " + System.getProperty("line.separator") + 
				"2- Introduzca GAMEMODE: gui o console. " + System.getProperty("line.separator") +
				"3- Introduzca PLAYERMODE: manual, random o smart. ");
	}
	
	public static void main(String[] args){
		if(args.length <2){
			usage();
			System.exit(1);
		}
		
		GameTable<?,?> game = createGame(args[0]);
		
		if(game == null){
			System.err.println("Invalid game");
			usage();
			System.exit(1);
		}
		
		String[] otherArgs = Arrays.copyOfRange(args,2,args.length);
		
		switch(args[1]){
		case "console":
			startConsoleMode(game, otherArgs);
			break;
		case "gui":
			startGUIMode(args[0], game, otherArgs); //porque le pasa args[0]? -Para el createGameView.
			break;
		default: 
			System.err.println("Invalid view mode: "+ args[1]);
			usage();
			System.exit(1);
		}		
	}
	
}
