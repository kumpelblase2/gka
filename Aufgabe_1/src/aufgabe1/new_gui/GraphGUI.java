package aufgabe1.new_gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import aufgabe1.*;
import aufgabe1.gui.GKAFileFilter;
import com.intellij.uiDesigner.core.*;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;

public class GraphGUI
{
	private JFrame m_frame;
	private JPanel m_panel1;
	private mxGraphComponent m_graphComponent;
	private JGraphXAdapter<String, WeightedNamedEdge> m_adapter;
	private JComboBox<SearchAlgorithm> m_searchSelection;
	private JButton m_setStartButton;
	private JButton m_setEndButton;
	private JButton m_searchButton;
	private JLabel m_graphAccessesLabel;

	private String m_start;
	private String m_end;
	private Graph<String, WeightedNamedEdge> m_currentGraph;
	private SearchAlgorithm m_searchAlgorithm;

	public GraphGUI(JFrame inFrame)
	{
		this.m_frame = inFrame;
		$$$setupUI$$$();
		m_setStartButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if(m_adapter.getSelectionCell() != null)
				{
					mxCell cell = (mxCell)m_adapter.getSelectionCell();
					setStart((String)cell.getValue());
				}
			}
		});

		m_setEndButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if(m_adapter.getSelectionCell() != null)
				{
					mxCell cell = (mxCell)m_adapter.getSelectionCell();
					setEnd((String)cell.getValue());
				}
			}
		});

		m_searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				doSearch();
			}
		});

		BFSSearcher bfs = new BFSSearcher();
		this.m_searchAlgorithm = bfs;
		this.m_searchSelection.addItem(bfs);
		this.m_searchSelection.addItem(new DijkstraSearcher());
		this.m_searchSelection.addItem(new FWSearcher());
		this.m_searchSelection.addItem(new FordFulkerson());
		this.m_searchSelection.addItem(new EdmondsKarp());

		this.m_searchSelection.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(final ItemEvent e)
			{
				if(e.getStateChange() == ItemEvent.SELECTED)
				{
					m_searchAlgorithm = (SearchAlgorithm)e.getItem();
				}
			}
		});
	}

	private void doSearch()
	{
		this.resetColors();
		for(WeightedNamedEdge edge : this.m_currentGraph.edgeSet())
		{
			edge.setCurrentFlow(0);
		}

		Path pathResult = this.m_searchAlgorithm.search(this.m_currentGraph, this.m_start, this.m_end);
		this.m_graphComponent.getGraph().refresh();
		this.m_graphComponent.refresh();
		if(!pathResult.hasMore())
		{
			JOptionPane.showMessageDialog(this.m_frame, "Es wurde kein Pfad gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}

		List<String> path = pathResult.next();
		for(int i = 0; i < path.size(); i++)
		{
			String start = path.get(i);
			colorVertex(start);
			if(i + 1 < path.size())
			{
				String end = path.get(i + 1);
				colorEdge(start, end);
			}
		}

		this.m_graphAccessesLabel.setText(pathResult.getSteps() + "");
		JOptionPane.showMessageDialog(this.m_frame, "Path found with " + (path.size() - 1) + " edge(s).", "Path found", JOptionPane.INFORMATION_MESSAGE);
	}

	private void colorEdge(String inStart, String inEnd)
	{
		this.colorCell(this.m_adapter.getEdgeToCellMap().get(this.m_currentGraph.getEdge(inStart, inEnd)));
	}

	private void colorVertex(String inVertex)
	{
		this.colorCell(this.m_adapter.getVertexToCellMap().get(inVertex));
	}

	private void colorCell(Object inCell)
	{
		this.m_adapter.setCellStyles(mxConstants.STYLE_STROKECOLOR, "#FF0000", new Object[] {inCell});
	}

	private void resetColors()
	{
		this.m_graphComponent.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "#6495ED", this.m_graphComponent.getGraph().getChildEdges(this.m_graphComponent.getGraph().getDefaultParent()));
		this.m_graphComponent.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "#6495ED", this.m_graphComponent.getGraph().getChildVertices(this.m_graphComponent.getGraph().getDefaultParent()));
		this.m_graphComponent.getGraph().refresh();
	}

	private void createUIComponents()
	{
		this.m_graphComponent = new mxGraphComponent(new JGraphXAdapter<>(new DefaultGraph()));
		this.loadGraph(new DefaultGraph());
		this.resetColors();
		this.createMenu();
	}

	private void createMenu()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open...");
		openItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser(new File("."));
				fileChooser.setFileFilter(new GKAFileFilter());
				int result = fileChooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					Graph<String, WeightedNamedEdge> parsed = GraphParser.parse(fileChooser.getSelectedFile());
					loadGraph(parsed);
				}
			}
		});
		fileMenu.add(openItem);
		JMenuItem randomItem = new JMenuItem("Open random");
		final GraphGUI self = this;
		randomItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GenerateRandomDialog dialog = new GenerateRandomDialog(self);
				dialog.pack();
				dialog.setVisible(true);
			}
		});
		fileMenu.add(randomItem);
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				JFileChooser fileChooser = new JFileChooser(new File("."));
				fileChooser.setFileFilter(new GKAFileFilter());
				int result = fileChooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION)
				{
					GraphParser.parse(m_currentGraph, fileChooser.getSelectedFile());
				}
			}
		});
		fileMenu.add(saveItem);

		menuBar.add(fileMenu);
		this.m_frame.setJMenuBar(menuBar);
	}

	private void setStart(String inStart)
	{
		this.m_start = inStart;
	}

	public void setEnd(final String inEnd)
	{
		m_end = inEnd;
	}

	public void loadGraph(Graph<String, WeightedNamedEdge> inGraph)
	{
		this.m_currentGraph = inGraph;
		this.m_graphComponent.clearCellOverlays();
		if(this.m_adapter != null)
			this.m_adapter.removeCells();

		this.m_adapter = new JGraphXAdapter<>(inGraph);
		this.m_graphComponent.setGraph(this.m_adapter);
		mxCircleLayout layout = new mxCircleLayout(this.m_adapter);
		layout.execute(this.m_adapter.getDefaultParent());
		if(!(this.m_currentGraph instanceof DirectedGraph))
			this.m_graphComponent.getGraph().setCellStyles(mxConstants.STYLE_ENDARROW, mxConstants.NONE, this.m_graphComponent.getGraph().getChildEdges(this.m_graphComponent.getGraph().getDefaultParent()));
	}

	public static void main(final String[] args)
	{
		JFrame frame = new JFrame("GraphGUI");
		frame.setContentPane(new GraphGUI(frame).m_panel1);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.pack();
		frame.setSize(600, 300);
		frame.setVisible(true);
	}

	/**
	 * Method generated by IntelliJ IDEA GUI Designer
	 * >>> IMPORTANT!! <<<
	 * DO NOT edit this method OR call it in your code!
	 */
	private void $$$setupUI$$$()
	{
		createUIComponents();
		m_panel1 = new JPanel();
		m_panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
		final JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
		m_panel1.add(panel1, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		m_graphComponent.setHorizontalScrollBarPolicy(32);
		m_graphComponent.setVerticalScrollBarPolicy(22);
		panel1.add(m_graphComponent, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
		final JPanel panel2 = new JPanel();
		panel2.setLayout(new GridLayoutManager(7, 2, new Insets(0, 0, 0, 0), -1, -1));
		m_panel1.add(panel2, new GridConstraints(0, 1, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		m_searchSelection = new JComboBox();
		panel2.add(m_searchSelection, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final Spacer spacer1 = new Spacer();
		panel2.add(spacer1, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
		m_setStartButton = new JButton();
		m_setStartButton.setText("Set start");
		panel2.add(m_setStartButton, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		m_setEndButton = new JButton();
		m_setEndButton.setText("Set end");
		panel2.add(m_setEndButton, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		m_searchButton = new JButton();
		m_searchButton.setText("Run");
		panel2.add(m_searchButton, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label1 = new JLabel();
		label1.setText("Search Algorithm");
		panel2.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		final JLabel label2 = new JLabel();
		label2.setText("Graph accesses:");
		panel2.add(label2, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
		m_graphAccessesLabel = new JLabel();
		m_graphAccessesLabel.setText("");
		panel2.add(m_graphAccessesLabel, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
	}

	/** @noinspection ALL */
	public JComponent $$$getRootComponent$$$()
	{
		return m_panel1;
	}
}
