package es.ucm.fdi.tp.view;

import java.awt.Color;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;

public abstract class PlayersInfoViewer<S extends GameState<S,A>, A extends GameAction<S,A>> extends GUIView<S,A> {
	protected PlayersInfoViewer<S,A> playersInfoViewer;
	
	public void setPlayersInfoViewer(PlayersInfoViewer<S,A> playersInfoViewer){
		this.playersInfoViewer = playersInfoViewer;
	}
	
	abstract public void setNumberOfPlayer();
	abstract public Color getPlayerColor(int player);
	
	public interface PlayersInfoObserver{
		public void ColorChanged(int p, Color color); //los observadores deben implementarla
	}
	
	protected List<PlayersInfoObserver> observers;
	
	public void addObserver(PlayersInfoObserver o){observers.add(o);}
	
	protected void notifyObservers(int p, Color c){
		for(PlayersInfoObserver o: observers){
			o.ColorChanged(p, c);
		}
	}
}
