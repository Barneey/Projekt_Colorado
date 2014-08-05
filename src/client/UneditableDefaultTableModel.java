package client;

import javax.swing.table.DefaultTableModel;

public class UneditableDefaultTableModel extends DefaultTableModel{

	public boolean isCellEditable(int rowIndex, int colIndex){
		return false;
	}
	
}
