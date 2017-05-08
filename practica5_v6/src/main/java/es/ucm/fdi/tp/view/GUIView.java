package es.ucm.fdi.tp.view;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public abstract class GUIView<S extends GameState<S,A>, A extends GameAction<S,A>> extends JPanel{
	protected JFrame window;
	protected JPanel mainPanel;
	private boolean isEnabled = false;
	
	public abstract void enable();
	public abstract void disable();
	public abstract void update(S state);
	public abstract void setMessageViewer(MessageViewer<S,A> infoViewer);
	public abstract void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer);
	public abstract void setGameController(GameController<S,A> gameCntrl);
	
	
	public GUIView (){}
	
	
	//asi habilitamos o desabilitamos la ventana que queramos usar.
	public void enableWindowMode(){
		if(!isEnabled){
			window.setContentPane(this);
			window.setVisible(true);
			isEnabled = true;
		}
	}
	
	public void disableWindowMode(){
		window.dispose();
		window = null;
	}
	
	public JFrame getWindow(){
		return window;
	}
	
	public void setTitle(String title){
		if(window != null){
			window.setTitle(title);
		}else{
			this.setBorder(BorderFactory.createTitledBorder(title));
		}
	}


}
