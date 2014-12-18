package aufgabe1.gui;

import aufgabe1.WeightedNamedEdge;

import java.awt.Dimension;

import javax.swing.JDialog;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;

public class ShowMST extends JDialog{

	
	    //Variable for the Input Graph
		private Graph<String, WeightedNamedEdge> m_graph;
		//Variable holds the Input Graph as a JGraphXAdapter-Class later
		private JGraphXAdapter<String, WeightedNamedEdge> m_adapter;
		//Component of the mxGraphComponent-Class (Input is the JGraphXAdapter)
		private mxGraphComponent m_component;
		
		public ShowMST(Graph<String, WeightedNamedEdge> inGraph){
			m_graph = inGraph;
			
			setTitle("Minimal Spanning Tree - Euler");
			//To set Layout to null, free positioning of Buttons etc.
			setLayout(null);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setSize(640, 480);
			
			//Method for initializing
			this.init();
			
			//center JDialog 
			this.setLocationRelativeTo(null);
			
			setVisible(true);
		}
		
		public void init(){
			//Graph<String, WeightedNamedEdge> --> JGraphXAdapter(Graph<V,E> graph)
			m_adapter = new JGraphXAdapter<>(m_graph);
			
			//JGraphXAdapter(Graph<V,E> graph) --> mxGraphComponent-Class
			m_component = new mxGraphComponent(m_adapter);
			
			//Set Layout for the Adapter
			mxCircleLayout layout = new mxCircleLayout(m_adapter);
			layout.setRadius(200);
			
			//Add component to the MainGUI-Frame
			getContentPane().add(m_component); //getContentPane() is optional to write
			//Set Position and Frame-Size of the component
			m_component.setBounds(10, 10, 800, 800);
			
			layout.execute(m_adapter.getDefaultParent());
			m_component.getGraph().setCellsEditable(false);
			if(!(m_graph instanceof DirectedGraph)){
				m_component.getGraph().setCellStyles(mxConstants.STYLE_ENDARROW, mxConstants.NONE, m_component.getGraph().getChildEdges(m_component.getGraph().getDefaultParent()));
			}
				
			
			
		}
}
