package aufgabe1.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import aufgabe1.*;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;

public class MainWindow extends JFrame
{
	private final Graph<String, WeightedNamedEdge> m_graph;
	private JGraphXAdapter<String, WeightedNamedEdge> m_adapter;
	private mxGraphComponent m_component;
	private JButton m_startButton;
	private JButton m_endButton;
	private JButton m_runButton;
	private String m_startVertex;
	private String m_endVertex;
	private SearchAlgorithm m_search = new BFSSearcher();

	public MainWindow(Graph<String, WeightedNamedEdge> inGraph)
	{
		super("DA GRAPH");
		this.m_graph = inGraph;
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600, 400);
		this.initComponents();
	}

	public void initComponents()
	{
		this.initMenuBar();
		this.m_startButton = new JButton("Set start");
		this.m_endButton = new JButton("Set end");
		this.m_runButton = new JButton("Run BFS");
		this.m_adapter = new JGraphXAdapter<>(this.m_graph);
		this.m_component = new mxGraphComponent(this.m_adapter);
		JScrollPane scroll = new JScrollPane(this.m_component);
		scroll.setSize(100, 100);
		scroll.setMaximumSize(new Dimension(100, 100));
		this.getContentPane().add(scroll);
		mxCircleLayout layout = new mxCircleLayout(this.m_adapter);
		this.getContentPane().add(this.m_startButton);
		this.getContentPane().add(this.m_endButton);
		this.getContentPane().add(this.m_runButton);
		this.m_startButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				setStart();
			}
		});

		this.m_endButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				setEnd();
			}
		});

		this.m_runButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				doSearch();
			}
		});
		this.getContentPane().setLayout(new FlowLayout());
		layout.execute(this.m_adapter.getDefaultParent());
		this.m_adapter.setGridSize(2);
		this.m_component.getGraph().setCellsEditable(false);
		if(!(this.m_graph instanceof DirectedGraph))
			this.m_component.getGraph().setCellStyles(mxConstants.STYLE_ENDARROW, mxConstants.NONE, this.m_component.getGraph().getChildEdges(this.m_component.getGraph().getDefaultParent()));
		
		this.resetColors();
	}

	private void initMenuBar()
	{
		JMenuBar menu = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				saveGraph();
			}
		});

		fileMenu.add(saveItem);
		menu.add(fileMenu);
		this.setJMenuBar(menu);
	}

	private void saveGraph()
	{
		JFileChooser choose = new JFileChooser(new File("."));
		choose.setFileFilter(new GKAFileFilter());
		choose.showSaveDialog(null);
		if(choose.getSelectedFile() != null)
		{
			GraphParser.parse(this.m_graph, choose.getSelectedFile());
		}
	}

	private void setStart()
	{
		if(m_adapter.getSelectionCell() != null)
		{
			mxCell cell = (mxCell)m_adapter.getSelectionCell();
			m_startVertex = (String)cell.getValue();
		}
	}

	private void setEnd()
	{
		if(m_adapter.getSelectionCell() != null)
		{
			mxCell cell = (mxCell)m_adapter.getSelectionCell();
			m_endVertex = (String)cell.getValue();
		}
	}

	private void doSearch()
	{
		this.resetColors();

		Path pathResult = this.m_search.search(this.m_graph, this.m_startVertex, this.m_endVertex);
		if(!pathResult.hasMore())
		{
			JOptionPane.showMessageDialog(this, "Es wurde kein Pfad gefunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
			return;
		}

		java.util.List<String> path = pathResult.next();
		for(int i = 0; i < path.size(); i++)
		{
			String start = path.get(i);
			colorVertex(start);
			if(i + 1 < path.size())
			{
				String end = path.get(i + 1);
				colorEdge(start, end);
				System.out.println("Color " + start + " to " + end);
			}
		}

		JOptionPane.showMessageDialog(this, "Path found with " + (path.size() - 1) + " edge(s).", "Path found", JOptionPane.INFORMATION_MESSAGE);
	}

	private void colorEdge(String inStart, String inEnd)
	{
		this.colorCell(this.m_adapter.getEdgeToCellMap().get(this.m_graph.getEdge(inStart, inEnd)));
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
		this.m_component.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "#6495ED", this.m_component.getGraph().getChildEdges(this.m_component.getGraph().getDefaultParent()));
		this.m_component.getGraph().setCellStyles(mxConstants.STYLE_STROKECOLOR, "#6495ED", this.m_component.getGraph().getChildVertices(this.m_component.getGraph().getDefaultParent()));
	}
}