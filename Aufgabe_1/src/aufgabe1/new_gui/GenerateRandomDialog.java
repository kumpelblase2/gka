package aufgabe1.new_gui;

import javax.swing.*;
import java.awt.event.*;
import aufgabe1.GraphGenerator;
import aufgabe1.GraphGenerator.GeneratorProperties;
import aufgabe1.WeightedNamedEdge;
import org.jgrapht.Graph;

public class GenerateRandomDialog extends JDialog
{
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;
	private JSpinner m_verticeMinAmount;
	private JSpinner m_edgeMinAmount;
	private JSpinner m_verticeMaxAmount;
	private JSpinner m_edgeMaxAmount;
	private final GraphGUI m_gui;

	public GenerateRandomDialog(GraphGUI inGUI)
	{
		this.m_gui = inGUI;
		setContentPane(contentPane);
		setModal(true);
		getRootPane().setDefaultButton(buttonOK);

		buttonOK.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onOK();
			}
		});

		buttonCancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onCancel();
			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				onCancel();
			}
		});

		contentPane.registerKeyboardAction(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				onCancel();
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}

	private void onOK()
	{
		int verticeMinAmount = (int)this.m_verticeMinAmount.getValue();
		int verticeMaxAmount = (int)this.m_verticeMaxAmount.getValue();
		int edgeMinAmount = (int)this.m_edgeMinAmount.getValue();
		int edgeMaxAmount = (int)this.m_edgeMaxAmount.getValue();
		if(verticeMaxAmount < verticeMinAmount)
			verticeMaxAmount = verticeMinAmount;

		if(edgeMaxAmount < edgeMinAmount)
			edgeMaxAmount = edgeMinAmount;

		Graph<String, WeightedNamedEdge> generated = GraphGenerator.generate(new GeneratorProperties(edgeMinAmount, edgeMaxAmount, verticeMinAmount, verticeMaxAmount, true));
		this.m_gui.loadGraph(generated);
		dispose();
	}

	private void onCancel()
	{
		dispose();
	}
}
