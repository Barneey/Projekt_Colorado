package serverUtil;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class ServerResultFrame extends JFrame{
	
	JScrollPane scrollPane;
	
	public ServerResultFrame(){
		super("Query Result");
		initialize();
	}
	
	public ServerResultFrame(String message){
		super("Query Result");
		initialize();
		setMessage(message);
	}
	
	public ServerResultFrame(ResultSet message){
		super("Query Result");
		initialize();
		setMessage(message);
	}
	
	public void setMessage(String message){		
		JTextArea jtaResult = new JTextArea(message, 1, message.length());
		jtaResult.setEditable(false);
		scrollPane.setViewportView(jtaResult);
		pack();
		setLocationRelativeTo(null);
	}
	
	public void setMessage(ResultSet message){
		try {
			JTable jtbResult = new JTable(resultSetToTableModel(null, message));
			scrollPane.setViewportView(jtbResult);
			pack();
			setLocationRelativeTo(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private DefaultTableModel resultSetToTableModel(DefaultTableModel model, ResultSet result) throws SQLException {
		ResultSetMetaData rsMetaData = result.getMetaData();
		if(model == null){
			model = new DefaultTableModel();
		}
		String columnLabels[] = new String[rsMetaData.getColumnCount()];
		
		for(int i = 0;i < columnLabels.length; i++) {
			columnLabels[i] = rsMetaData.getColumnLabel(i+1);
		}

	    model.setColumnIdentifiers(columnLabels);

	    while(result.next()) {
	        Object data[] = new Object[columnLabels.length];
	        for(int i = 0; i < data.length; ++i) {
	             data[i] = result.getObject(i+1);
	        }
	        model.addRow(data);
	    }
	    return model;
	}
	
	private void initialize(){
		scrollPane = new JScrollPane(); 
		add(scrollPane);
		
		
		
		pack();
//		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
