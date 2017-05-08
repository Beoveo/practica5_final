package es.ucm.fdi.tp.view;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer.PlayerMode;
import es.ucm.fdi.tp.base.model.GameState;

/**
 * ControlPanel contiene el menu. Con los botones de rand, reestart, exit...
 * Permite seleccionar el modo de jugador. (Manual, Rand, Smart).
 * @param <S>
 * @param <A>
 */
public class ControlPanel <S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S,A> implements ItemListener{
	private JComboBox<String> playerModeCb;
	
	private GameController<S,A> gameCntrl;
	
	public ControlPanel(GUIView<S, A> gameView){
		initGUI(gameView);
	}
	
	private JButton botonesMenu(String nombre){
		JButton button = new JButton();
		ImageIcon imagen = new ImageIcon(nombre);
		Icon icon;
		button.setBounds(10, 10, 50, 50);
		icon = new ImageIcon(imagen.getImage().getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_DEFAULT));
		button.setIcon(icon);
		
		return button;
	}
	
	private void initGUI(GUIView<S, A> gameView) {
		//crear los botones y el combo-box
		
		JMenuBar menuBar = new JMenuBar(); 
		
		JButton randBtn = botonesMenu("dice.png"); //MOV ALEATORIO
		menuBar.add(randBtn);
		JButton smartBtn = botonesMenu("nerd.png"); //MOV INTELIGENTE
		menuBar.add(smartBtn);
		JButton restartBtn = botonesMenu("restart.png"); //REINICIAR PARTIDA
		menuBar.add(restartBtn);
		JButton exitBtn = botonesMenu("exit.png"); //SALIR
		menuBar.add(exitBtn);
		
		JMenu playerMode = new JMenu("Player Mode: "); //Combo box
		menuBar.add(playerMode);
		
		menuBar.setLayout(new FlowLayout());
		playerModeCb = new JComboBox<String>();
		
		playerModeCb.setBounds(10, 10, 80, 20);
		menuBar.add(playerModeCb);
		
		playerModeCb.addItem("Manual");
		playerModeCb.addItem("Random");
		playerModeCb.addItem("Smart");

		gameView.getWindow().setJMenuBar(menuBar);

		playerModeCb.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				String playerMode = (String) playerModeCb.getSelectedItem();
				
				if(playerMode.equals("Manual")) gameCntrl.changePlayerMode(PlayerMode.Manual);
				else if(playerMode.equals("Random")) gameCntrl.changePlayerMode(PlayerMode.Random);
				else gameCntrl.changePlayerMode(PlayerMode.Smart);
			}
		});
		
		
		randBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				randButton();
			}
		});
		
		smartBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				smartButton();
			}
		});
		
		restartBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartButton(); 
			}
		});
		
		exitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});

	}
	
	@Override
	public void enable() {
		setEnabled(true);
	}

	@Override
	public void disable() {
		setEnabled(false);		
	}

	@Override
	public void update(S state) {
		
	}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {}

	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {}

	@Override
	public void setGameController(GameController<S, A> gameCntrl) {
		this.gameCntrl = gameCntrl;
	}

	public void smartButton(){
		this.gameCntrl.makeSmartMove();
	}
	
	public void randButton(){
		this.gameCntrl.makeRandomMove();
	}
	
	public void restartButton(){
		this.gameCntrl.restartGame();
	}
	/**
	 * Metodo que genera una ventana en la que se pregunta al usuario si de verdad quiere salir del juego.
	 * Si selecciona la opcion SI, para el juego y se cierra el JFrame.
	 * Si selecciona la opcion NO, sigue el juego.
	 */
	private void quit() {
		int n = JOptionPane.showOptionDialog(new JFrame(),
				"Seguro que quieres parar el juego?", "Salir del juego",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				null, null);

		if (n == 0) {
			this.gameCntrl.stopGame(); 
			System.exit(0); //Cierra la ventana.
		}
	}

	/**
	 * Metodo que implementa la clase ItemListener. Compara el playerMode para saber cual es el item seleccionado,
	 * en este caso Smart, Manual o Random. Y el seleccionado lo deja fijo en el ComboBox.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == playerModeCb){
			String seleccionado = (String) playerModeCb.getSelectedItem();
			playerModeCb.setName(seleccionado);
		}
	}	

}
