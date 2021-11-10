package main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class Main {
	
	public static void main(String[] args) throws InterruptedException {
		
		//Progress Bar 
		JProgressBar bar = new JProgressBar();
		bar.setBounds(100,280,300,30);
        bar.setBorderPainted(true);
        bar.setStringPainted(true);
        bar.setBackground(Color.WHITE);
        bar.setForeground(new Color(55,44,130));
        bar.setValue(0);
        
        //Panel for Loading Page
		JPanel loadingScreen = new JPanel();
		loadingScreen.setForeground(Color.white);
		loadingScreen.setVisible(true);
		loadingScreen.setLayout(null);
		loadingScreen.setBounds(0,0,500,400);
		loadingScreen.add(bar);
		
		String[] date_arr = date_reader();		//Finds today date
		
		//All Labels and components that makes up the GUI
		String month = date_arr[1];
        Integer year = Integer.parseInt(date_arr[2]);
        ImageIcon img = new ImageIcon(Main.class.getResource("/images/icon.png"));
        ImageIcon bg = new ImageIcon(Main.class.getResource("/images/bg.png"));
        JLabel bg_label = new JLabel(bg);
        bg_label.setVisible(true);
        bg_label.setBounds(0,0,500,400);
        loadingScreen.add(bg_label);
		JFrame frame = new JFrame("CK's Weather Calender");
		frame.add(loadingScreen);
		frame.getContentPane().setBackground(new Color(55,44,130));
		frame.setIconImage(img.getImage());
		JLabel month_l = new JLabel("Month : " + month);
		month_l.setForeground(Color.WHITE);
		JLabel year_l = new JLabel("Year : " + year);
		year_l.setForeground(Color.WHITE);
		JLabel location = new JLabel("Location : ");
		location.setForeground(Color.WHITE);
		JLabel high_temp = new JLabel("Select a date to get the forecast");
		high_temp.setForeground(Color.WHITE);
		JLabel low_temp = new JLabel();
		low_temp.setForeground(Color.WHITE);
		high_temp.setBounds(30,270,400,100);
		high_temp.setVisible(true);
		low_temp.setBounds(280,270,400,100);
		low_temp.setVisible(true);
		location.setBounds(30,230,400,100);
		location.setVisible(true);
		month_l.setBounds(30,-20,100,100);
		month_l.setVisible(true);
		year_l.setBounds(380,-20,100,100);
		year_l.setVisible(true);
		frame.setSize(500, 400);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);  
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        month_l.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        year_l.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        location.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        high_temp.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        low_temp.setFont(new Font("TimesRoman", Font.PLAIN, 18));
        JButton more = new JButton("More Details");
        more.setVisible(true);
        more.setBounds(260,270,150,20);
        frame.setVisible(true);
        
        bar.setString("Loading GUI Components");
        bar.setValue(10);
        Thread.sleep(500);
        
        frame.add(month_l);
		frame.add(year_l);
		frame.add(location);
		frame.add(high_temp);
		frame.add(low_temp);      
        
		//Creating an instance of User defined Class 
        Web_Scrapper Obj = new Web_Scrapper(bar);
        location.setText("Location : " + Obj.get_location());
        final ArrayList<String[]> weather_data = Obj.get_weather_data();
        String url = Obj.get_url();
        
        bar.setString("Extracting Forecast Data");
        bar.setValue(60);
        Thread.sleep(500);
        
        //Creating Desktop instance to invoke Browser's action later
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        
        //event handling for the more button
        more.addMouseListener(new java.awt.event.MouseAdapter() {
        	
        	public void mouseClicked(java.awt.event.MouseEvent evt) {
        		
        	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        	        try {
        	        	//Opens the browser with required url
        	            desktop.browse(new URL(url).toURI());
        	        } catch (Exception e) {
        	            e.printStackTrace();
        	        }
        	    }

        	}
        });
        
        //Data for the Calendar
        String column[]={"Sun","Mon","Tue", "Wed", "Thu", "Fri", "Sat"};   
        Integer data[][] = table_generator(weather_data);
        
        bar.setString("Creating the Calendar");
        bar.setValue(70);
        Thread.sleep(500);
        
        //Creating JTable
        final JTable jt = new JTable(data,column) {
			private static final long serialVersionUID = 1577662578070990324L;
			public boolean isCellEditable(int row, int column) {
        		return false;
        	};
        };   
        jt.getTableHeader().setOpaque(false);
        jt.getTableHeader().setBackground(new Color(13,24,82));
        jt.getTableHeader().setForeground(new Color(28,177,152));
        jt.getTableHeader().setFont(new Font("Serif", Font.BOLD, 11));
        jt.setRowSelectionAllowed(false);
        jt.getTableHeader().setReorderingAllowed(false);
        jt.setRowHeight(30);
        jt.setShowGrid(true);
        jt.setFont(new Font("Serif", Font.BOLD, 18));
        
        bar.setString("Feeding the Calendar");
        bar.setValue(80);
        Thread.sleep(500);
        
        //Creating instance of our own defined rendering class
        TableColorCellRenderer renderer = new TableColorCellRenderer();
        jt.setDefaultRenderer(Object.class, renderer);

        JScrollPane sp=new JScrollPane(jt);
        frame.add(sp);
		frame.add(more);
        sp.setBounds(20, 50, 445, 203);
        sp.setVisible(false);
        
        bar.setString("Feeding the Calendar");
        bar.setValue(90);
        Thread.sleep(500);
        
        //Action listener for mouse clicks on the cells of the table
        jt.addMouseListener(new java.awt.event.MouseAdapter()
        		{
        	@Override
        	public void mouseClicked(java.awt.event.MouseEvent evt) {
        		
        		int row = jt.rowAtPoint(evt.getPoint());
                int col = jt.columnAtPoint(evt.getPoint());
                int z = (row * 7) + col;
                high_temp.setText("High Temp : " + weather_data.get(z)[1] + "C");
                low_temp.setText("Low Temp : " + weather_data.get(z)[2] + "C");

        	}
        		});       
       
        bar.setString("Loading Done");
        bar.setValue(100);
        Thread.sleep(500);
        
        //Swithcing to the main frame
        loadingScreen.setVisible(false);
        sp.setVisible(true);

        //Message box with necessary info
        JOptionPane.showMessageDialog(frame,
        		"1.Turn off any VPN OR Proxy since we trace your IP "
        		+ "for location to give you accurate forecast"
        		+ "\n2.Weather reports are provided by "
        		+ "Accuweather.com\n3.Because of Inefficient fund, the author "
        		+ "couldn't afford any Premium API keys,\nSo this app's weather "
        		+ "forecast is limited with forecasted temperatures of "
        		+ "current month\n\n"
        		+ "Regards from The Author,\nCavinKumaran M, CSE, SJIT ",
        		"Thanks For Using This App", JOptionPane.INFORMATION_MESSAGE);       
	}
	
	public static String[] date_reader() {
		
		//Returns Current Date, Month and Year
		LocalDate myObj = LocalDate.now();
		DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String date = myObj.format(dateformat);
        String[] date_arr = date.split("-");
        return(date_arr);
        
	}
	
	public static Integer[][] table_generator(ArrayList<String[]> data) {
		
		//Assigns values to the data cells of the Calendar table
		Integer table_data[][] = new Integer[6][7];
        int k = 0;
        try {
	        for(int i = 0; i < 6; i++) {
	        	for(int j = 0; j < 7; j++) {   
	        		table_data[i][j] = Integer.parseInt(data.get(k)[0]);
	        		k++;
	        	}
	        }
        }
        catch(Exception e) {
        	;
        }
        return table_data;
	}
	
}

//User defined class to make cells to change their color on selection 
class TableColorCellRenderer implements TableCellRenderer{
	
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