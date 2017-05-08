package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import es.ucm.fdi.tp.extra.jboard.JBoard;
import es.ucm.fdi.tp.extra.jboard.JBoard.Shape;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.view.PlayersInfoViewer.PlayersInfoObserver;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class WolfAndSheepView extends GUIView<WolfAndSheepState, WolfAndSheepAction>
			implements PlayersInfoObserver{
	
	private JBoard boardComp;
	private int[][] board;
	
	private int numOfRows, numOfCols;
	private Color colorP1, colorP2;
	
	private WolfAndSheepState state;
	
	
	GameController<WolfAndSheepState, WolfAndSheepAction> gameCntrl;
	public int lastCol = 0;
	public int lastRow = 7;

	public WolfAndSheepView() {	
		this.colorP1 = Color.CYAN;
		this.colorP2 = Color.ORANGE;
		this.state = new WolfAndSheepState(8); 
		initGUI();
	}
	

	private void initGUI() {
		super.window = new JFrame();
		super.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.window.setSize(500, 500);
		super.window.setVisible(true);
		createBoardData(this.state);

		boardComp = new JBoard() {

			@Override
			protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
				if(clickCount==1){
					WolfAndSheepView.this.mouseClicked(row, col, clickCount, mouseButton);
					}			
			}

			@Override
			protected void keyTyped(int keyCode) {
				System.out.println("Key " + keyCode + " pressed ..");
			}

			@Override
			protected Shape getShape(int player) {
				Shape shape;
				if(player == 0 || player ==1) shape = Shape.CIRCLE;
				else shape = Shape.RECTANGLE;
				return shape;
			}

			@Override
			protected Integer getPosition(int row, int col) {
				return WolfAndSheepView.this.getPosition(row, col);
			}

			@Override
			protected int getNumRows() {
				return numOfRows;
			}

			@Override
			protected int getNumCols() {
				return numOfCols;
			}

			@Override
			protected Color getColor(int player) {
				return WolfAndSheepView.this.getColor(player);
			}

			@Override
			protected Color getBackground(int row, int col) {
				// use this for 2 chess like board
				 return (row+col) % 2 == 0 ? Color.LIGHT_GRAY : Color.BLACK;
			}

			@Override
			protected int getSepPixels() {
				return 1; // put to 0 if you don't want a separator between
							// cells
			}
		};
		JPanel pBoard= new JPanel();
		pBoard.add(boardComp);
		
		super.window.getContentPane().add(pBoard, BorderLayout.CENTER);
	}


protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		
		if(clickCount==1){
			
			
		if(this.state.at(row, col)==-1){
			
			this.state.validActions(this.gameCntrl.getPlayerId());
			WolfAndSheepAction a = new WolfAndSheepAction(this.gameCntrl.getPlayerId(),row,col,this.lastRow,this.lastCol);
			if(this.state.isValid(a)){
				this.gameCntrl.makeManualMove(a);
			this.state = this.gameCntrl.getState();
			createBoardData(this.state);
			gameCntrl.getPlayerId();
			boardComp.repaint();
			boardComp.revalidate();
			}
		}
	}
		
		System.out.println("Mouse: " + clickCount + "clicks at position (" + row + "," + col + ") with Button "
				+ mouseButton);
		this.lastCol = col;
		this.lastRow = row;
			
}

	private void createBoardData(WolfAndSheepState state) {
		this.numOfRows = 8;
		this.numOfCols = 8;
		
		board = new int[numOfRows][numOfCols];
		
		int[][] boardWas = state.getBoard();
		
		for (int i = 0; i < numOfRows; i++)
			for (int j = 0; j < numOfCols; j++) {
				board[i][j] = boardWas[i][j];
			}	
	}

	protected int getPosition(int row, int col) {
		return board[row][col];
	}

	protected Color getColor(int player) {
		if(player == 0) return colorP1;
		if(player == 1) return colorP2;
		else return null;
	}

	
	@Override
	public void ColorChanged(int p, Color color) {
		if(p == 0) colorP1 = color;
		else colorP2 = color;
	}

	@Override
	public void enable() {
		boardComp.setEnabled(true);
	}

	@Override
	public void disable() {
		boardComp.setEnabled(false);
	}

	@Override
	public void update(WolfAndSheepState state) {
		this.state = state;		
		createBoardData(this.state);
		boardComp.repaint();	
	}

	@Override
	public void setMessageViewer(
			MessageViewer<WolfAndSheepState, WolfAndSheepAction> infoViewer) {
	
	}

	@Override
	public void setPlayersInfoViewer(
			PlayersInfoViewer<WolfAndSheepState, WolfAndSheepAction> playersInfoViewer) {}

	@Override
	public void setGameController(
			GameController<WolfAndSheepState, WolfAndSheepAction> gameCntrl) {
		this.gameCntrl = gameCntrl;	
	}
	
}
