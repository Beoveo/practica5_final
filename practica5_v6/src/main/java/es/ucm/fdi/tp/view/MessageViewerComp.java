package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

/**
 * Mostrara los mensajes en una barra orientada a la derecha de la ventana. 
 *
 */
public class MessageViewerComp<S extends GameState<S,A>, A extends GameAction<S,A>> extends MessageViewer<S,A> {
	private JTextArea msgArea;
	private GameController<S,A> gameCtrl;
	private String msgTextArea = "";
	private S state;
	
	public MessageViewerComp(GUIView<S, A> gameView){
		initGUI(gameView);
	}
	
	/**
	 * Metodo que crea el area de texto y lo coloca en el JPanel
	 */
	private void initGUI(GUIView<S, A> gameView) {
		JPanel txtPanel = new JPanel();
		msgArea = new JTextArea(15,20); 
		msgArea.append(msgTextArea);
		msgArea.setPreferredSize(new Dimension(200, 300));
		msgArea.setEditable(false);
		JScrollPane txtScroll = new JScrollPane(msgArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
		txtPanel.add(txtScroll);
		gameView.getWindow().getContentPane().add(txtPanel, BorderLayout.PAGE_END);
	}
		@Override
	public void addContent(String msg) {
		msgTextArea = msg;
		msgArea.append(msg);
		msgArea.repaint();
	}

	@Override
	public void setContent(String msg) {
		msgTextArea = msg;
		msgArea.repaint();
	}

	//para esta clase enable, disable y update no hacen nada.
	@Override
	public void enable() {/*Always Enable, no use*/}

	@Override
	public void disable() {/*Always Enable, no use*/}

	@Override
	public void update(S state) {
		this.state = state;
		if(gameCtrl.getPlayerId() == state.getWinner()) msgArea.append("You win !");
		if((gameCtrl.getPlayerId() != state.getWinner()) && state.getWinner() != -1) msgArea.append("You have lost!");
		
	}

	@Override
	public void setPlayersInfoViewer(PlayersInfoViewer<S, A> playersInfoViewer) {
		//There is no Player Info Viewer here
	}

	@Override
	public void setGameController(GameController<S, A> gameCntrl) {
		this.gameCtrl = gameCntrl;
	}

}
