package main;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class TableColorCellRenderer implements TableCellRenderer{
	
	private static final TableCellRenderer RENDERER = new DefaultTableCellRenderer();
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		
		Component c = RENDERER.getTableCellRendererComponent(table, value
				, isSelected, hasFocus, row, column);
	
		Color color, f_color;
		if(row == table.getSelectedRow() && column == table.getSelectedColumn()) {
			color = new Color(255,57,127);
			f_color = Color.WHITE;
		} else {
			color = new Color(19,32,97);
			f_color = new Color(79,94,171);
		}
		c.setForeground(f_color);
		c.setBackground(color);
		
		((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER);
		return c;
	}

}
