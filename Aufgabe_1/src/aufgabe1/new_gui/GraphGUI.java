package aufgabe1.new_gui;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import aufgabe1.*;
import aufgabe1.gui.GKAFileFilter;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
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

		Path pathResult = this.m_searchAlgorithm.search(this.m_currentGraph, this.m_start, this.m_end);
		java.util.List<String> path = pathResult.getVertexes();
		if(path.size() == 0)
		{
			JOptionPane.showMessageDialog(this.m_frame, "Es wurde kein Pfad gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}

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
		this.m_adapter.setCellStyles(mxConstants.STYLE_STROKECOLOR, "#FF0000", new Object[] { inCell });
	}

	private void resetColors()
	{
		this.m_graphComponent.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "#6495ED", this.m_graphComponent.getGraph().getChildEdges(this.m_graphComponent.getGraph().getDefaultParent()));
		this.m_graphComponent.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "#6495ED", this.m_graphComponent.getGraph().getChildVertices(this.m_graphComponent.getGraph().getDefaultParent()));
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
		this.m_adapter = new JGraphXAdapter<>(inGraph);
		this.m_graphComponent.setGraph(this.m_adapter);
		mxCircleLayout layout = new mxCircleLayout(this.m_adapter);
		layout.execute(this.m_adapter.getDefaultParent());
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
}
