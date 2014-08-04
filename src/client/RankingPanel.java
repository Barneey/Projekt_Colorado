package client;

import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.sun.rowset.CachedRowSetImpl;

public class RankingPanel extends JPanel{
	
	private JScrollPane jscrllRankings;
	private UneditableDefaultTableModel tableModelRankings;
	private JTable tableRankings;
	private DatabaseConnection dbCon;
	
	public RankingPanel(Dimension d){
		super();
		dbCon = DatabaseConnection.getInstance();
		jscrllRankings = new JScrollPane();
		tableModelRankings = new UneditableDefaultTableModel();
		tableRankings = new JTable(tableModelRankings);
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableRankings.getModel());
		tableRankings.setRowSorter(sorter);
		jscrllRankings.setViewportView(tableRankings);
		jscrllRankings.setPreferredSize(d);
		this.add(jscrllRankings);
	}

	public void loadRanking(){
		try {
			resultSetToTableModel(tableModelRankings, dbCon.getRanking());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private DefaultTableModel resultSetToTableModel(DefaultTableModel model, CachedRowSetImpl rowSet) throws SQLException {
		ResultSetMetaData rsMetaData = rowSet.getMetaData();
		if(model == null){
			model = new DefaultTableModel();
		}
		String columnLabels[] = new String[rsMetaData.getColumnCount() + 1];
		columnLabels[0] = "Rank";
		for(int i = 1;i < columnLabels.length; i++) {
			columnLabels[i] = rsMetaData.getColumnLabel(i);
		}

	    model.setColumnIdentifiers(columnLabels);
	    model.setRowCount(0);
	    int rank = 1;
	    while(rowSet.next()) {
	        Object data[] = new Object[columnLabels.length + 1];
	        data[0] = rank;
	        rank++;
	        for(int i = 1; i < data.length - 1; i++) {
	             data[i] = rowSet.getObject(i);
	        }
	        model.addRow(data);
	    }
	    return model;
	}
}