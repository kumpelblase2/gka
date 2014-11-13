package aufgabe1.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class ShowMatrixT extends JFrame{

	JTable matrixT;
	
	public ShowMatrixT(ArrayList<ArrayList<Integer>> matrix){
		
		setTitle("T-Matrix:");
		//To set Layout to null, free positioning of Buttons etc.
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Filling 
	    int size = matrix.get(0).size();
	    
        Object[][] data = new Object[size][size];
        
        for(int i=0; i<size; i++){
        	for(int j=0; j<size; j++){
        		data[i][j] = matrix.get(i).get(j);
        	}
        }
	    
        Object[] columnNames = new Object[size];
        for(int z=0; z<size; z++){
        	columnNames[z] = matrix.get(0).get(z);
        }
        
	    matrixT= new JTable(data, columnNames);
		
		//Center JFrame
	    Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
	    int screenHeight = screenSize.height;
	    int screenWidth = screenSize.width;
	    setSize(screenWidth / 2, screenHeight / 2);
	    setLocation(screenWidth / 4, screenHeight / 4);
		
	    //Add JTable
	  	int height = this.getHeight()/(matrixT.getRowCount()+1);
	  	matrixT.setRowHeight(height);
	  		
	  	setTableAlignment(matrixT);
	 
	  	matrixT.setBounds(0, 0, this.getWidth(), this.getHeight());
	  	add(matrixT);
	    
		setVisible(true);
	}
	
	public void setTableAlignment(JTable table){
	    // table header alignment
	    JTableHeader header = table.getTableHeader();
	    DefaultTableCellRenderer renderer = (DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer();
	    header.setDefaultRenderer(renderer);
	    renderer.setHorizontalAlignment(JLabel.CENTER);

	   // table content alignment
	   DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	   centerRenderer.setHorizontalAlignment( JLabel.CENTER );
	   int rowNumber = table.getColumnCount();
	   for(int i = 0; i < rowNumber; i++){
	   table.getColumnModel().getColumn(i).setCellRenderer( centerRenderer );
	   }
	}
}
