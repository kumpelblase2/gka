package aufgabe1.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import aufgabe1.WeightedNamedEdge;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;

public class MainWindow extends JFrame
{
	private final Graph<String, WeightedNamedEdge> m_graph;
	private JGraphXAdapter<String, WeightedNamedEdge> m_adapter;
	private JButton m_startButton;
	private JButton m_endButton;
	private JButton m_runButton;
	private String m_startVertex;
	private String m_endVertex;

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
		this.m_startButton = new JButton("Set start");
		this.m_endButton = new JButton("Set end");
		this.m_runButton = new JButton("Run BFS");
		this.m_adapter = new JGraphXAdapter<String, WeightedNamedEdge>(this.m_graph);
		this.getContentPane().add(new mxGraphComponent(this.m_adapter));
		mxCircleLayout layout = new mxCircleLayout(this.m_adapter);
		this.getContentPane().add(this.m_startButton);
		this.getContentPane().add(this.m_endButton);
		this.getContentPane().add(this.m_runButton);
		this.m_startButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if(m_adapter.getSelectionCell() != null)
				{
					mxCell cell = (mxCell)m_adapter.getSelectionCell();
					m_startVertex = (String)cell.getValue();
				}
			}
		});

		this.m_endButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(final ActionEvent e)
			{
				if(m_adapter.getSelectionCell() != null)
				{
					mxCell cell = (mxCell)m_adapter.getSelectionCell();
					m_endVertex = (String)cell.getValue();
				}
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
	}

	private void doSearch()
	{

	}
}