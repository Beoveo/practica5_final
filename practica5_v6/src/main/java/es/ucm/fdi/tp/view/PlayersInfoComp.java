package es.ucm.fdi.tp.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.extra.jcolor.ColorChooser;
import es.ucm.fdi.tp.extra.jcolor.MyTableModel;

public class PlayersInfoComp<S extends GameState<S,A>, A extends GameAction<S,A>> extends PlayersInfoViewer<S,A> {

	private MyTableModel myModel;
	private Map<Integer, Color> colors; // Line -> Color
	private ColorChooser colorChooser;
	
	public PlayersInfoComp(GUIView<S, A> gameView){
		initGUI(gameView);
	}
	
	private void initGUI(GUIView<S, A> gameView) {
		JPanel playerInfoPanel = new JPanel(new BorderLayout());
		colors = new HashMap<>();
		colorChooser = new ColorChooser(new JFrame(), "Choose Line Color", Color.BLACK);

		// names table
		myModel = new MyTableModel();
		myModel.getRowCount();
		JTable table = new JTable(myModel) {
			private static final long serialVersionUID = 1L;

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if (col == 1)
					comp.setBackground(colors.get(row));
				else
					comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}
		};

		table.setToolTipText("Click on a row to change the color of a player");
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col >= 0) {
					changeColor(row);
				}
			}

		});

		playerInfoPanel.add(new JScrollPane(table), BorderLayout.CENTER);
		JPanel ctrlPabel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		playerInfoPanel.add(ctrlPabel, BorderLayout.PAGE_START);
		playerInfoPanel.setOpaque(true);
		playerInfoPanel.setPreferredSize(new Dimension(200, 250));
		gameView.getWindow().getContentPane().add(playerInfoPanel, BorderLayout.AFTER_LINE_ENDS);
	}

	

	

private void changeColor(int row) {
		colorChooser.setSelectedColorDialog(colors.get(row));
		colorChooser.openDialog();
		if (colorChooser.getColor() != null) {
			colors.put(row, colorChooser.getColor());
			repaint();
		}
}
	@Override
	public void setNumberOfPlayer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getPlayerColor(int player) {
		return null;
	}

	@Override
	public void enable() {}

	@Override
	public void disable() {}

	@Override
	public void update(S state) {}

	@Override
	public void setMessageViewer(MessageViewer<S, A> infoViewer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGameController(GameController<S, A> gameCntrl) {}

}
