package rms;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.*;
import java.sql.*;
import java.awt.event.*;
import java.util.*;
public class BookFrame {
	JFrame frame=new JFrame("Book record management");
	PreparedStatement ps;
	JTabbedPane tabbedpane=new JTabbedPane();
	JLabel l1,l2,l3,l4,l5;
	JTextField t1,t2,t3,t4,t5;
	JPanel insertPanel,viewPanel;
	JButton saveButton, updateButton, deleteButton;
	JTable table;
	JScrollPane scrollPane;
	DefaultTableModel dm;
	String[] colNames= {"Book ID","Title","Price","Author","Publisher"};
	Connection con;
	
	public BookFrame() {
		
		getConnection();
		initcomponent();
		
		
	}
	void getConnection() {
		//to get connection
		try{
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/prakhar","root","Prakhar@1");
			System.out.println("Connection established");
		}
		catch(SQLException e) {
			System.out.println("Exception occured"+ e.getMessage());
		}
		
	}
	void initcomponent() {
		//component for insert form
		l1=new JLabel("Book ID =>");
		l2=new JLabel("Title =>"); 
		l3=new JLabel("Price =>");
		l4=new JLabel("Author =>");
		l5=new JLabel("Publisher =>");
		t1=new JTextField();
		t2=new JTextField();
		t3=new JTextField();
		t4=new JTextField();
		t5=new JTextField();
		saveButton = new JButton("Save");
		l1.setBounds(100, 100, 100, 20);
		l2.setBounds(100, 150, 100, 20);
		l3.setBounds(100, 200, 100, 20);
		l4.setBounds(100, 250, 100, 20);
		l5.setBounds(100, 300, 100, 20);
		t1.setBounds(250, 100, 100, 20);
		t2.setBounds(250, 150, 100, 20);
		t3.setBounds(250, 200, 100, 20);
		t4.setBounds(250, 250, 100, 20);
		t5.setBounds(250, 300, 100, 20);
		saveButton.setBounds(100, 350, 100, 30);
		saveButton.addActionListener(new InsertBookRecord());
		insertPanel=new JPanel();
		insertPanel.setLayout(null);
		insertPanel.add(l1);
		insertPanel.add(l2);
		insertPanel.add(l3);
		insertPanel.add(l4);
		insertPanel.add(l5);
		insertPanel.add(t1);
		insertPanel.add(t2);
		insertPanel.add(t3);
		insertPanel.add(t4);
		insertPanel.add(t5); 
		insertPanel.add(saveButton);
		
		ArrayList<Book> booklist=fetchBookrecords();
		setDataOntable(booklist);
		updateButton=new JButton("Update Book");
		updateButton.addActionListener(new UpdateBookRecord());
		deleteButton=new JButton("Delete Book");
		deleteButton.addActionListener(new DeleteBookrecord());
		viewPanel=new JPanel();
		viewPanel.add(deleteButton);
		viewPanel.add(updateButton);
		scrollPane=new JScrollPane(table);
		viewPanel.add(scrollPane);
		
		
		tabbedpane.add(insertPanel);
		tabbedpane.add(viewPanel);
		frame.add(tabbedpane);
		
		
		tabbedpane.addChangeListener(new TabChangehandler());
		
		
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
	}
		@SuppressWarnings("finally")
		ArrayList<Book> fetchBookrecords() {
			ArrayList<Book> booklist=new ArrayList<Book>();
			String q="select * from Book";
			try {
				ps=con.prepareStatement(q);
				ResultSet rs=ps.executeQuery();
				while(rs.next()) {
					Book b=new Book();
					b.setBookid(rs.getInt(1));
					b.setTitle(rs.getString(2));
					b.setPrice(rs.getDouble(3));
					b.setAuthor(rs.getString(4));
					b.setPublisher(rs.getString(5));
					booklist.add(b);
				}
			}
			catch(SQLException a) {
				System.out.println(a.getMessage());
			}
			finally {
				return booklist;
			}
		}
		
		void setDataOntable(ArrayList<Book> booklist) {
			Object[][] obj=new Object[booklist.size()][5];
			for(int i=0;i<booklist.size();i++) {
				obj[i][0]=booklist.get(i).getBookid();
				obj[i][1]=booklist.get(i).getTitle();
				obj[i][2]=booklist.get(i).getPrice();
				obj[i][3]=booklist.get(i).getAuthor();
				obj[i][4]=booklist.get(i).getPublisher();
				
			}
			table=new JTable();
			dm=new DefaultTableModel();
			dm.setColumnCount(5);
			dm.setNumRows(booklist.size());
			dm.setColumnIdentifiers(colNames);
			for(int i=0;i<booklist.size();i++) {
				dm.setValueAt(obj[i][0], i, 0);
				dm.setValueAt(obj[i][1], i, 1);
				dm.setValueAt(obj[i][2], i, 2);
				dm.setValueAt(obj[i][3], i, 3);
				dm.setValueAt(obj[i][4], i, 4);
				
			}
			table.setModel(dm);
		}
		class InsertBookRecord implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				Book b1=readFromData();
				String q="insert into Book (bookid,title,price,author,publisher) values (?,?,?,?,?)";
				try {
					ps=con.prepareStatement(q);
					ps.setInt(1,b1.getBookid());
					ps.setString(2,b1.getTitle());
					ps.setDouble(3, b1.getPrice());
					ps.setString(4, b1.getAuthor());
					ps.setString(5, b1.getPublisher());
					
					ps.execute();
					t1.setText("");
					t2.setText("");
					t3.setText("");
					t4.setText("");
					t5.setText("");
					
					
				}
				catch(SQLException a){
					System.out.println("Exception caught"+ a.getMessage());
				}
			}
			
			Book readFromData() {
				Book b1=new Book();
				b1.setBookid(Integer.parseInt(t1.getText()));
				b1.setTitle(t2.getText());
				b1.setPrice(Double.parseDouble(t3.getText()));
				b1.setAuthor(t4.getText());
				b1.setPublisher(t5.getText());
				
				return b1;
			}
		}
		void updateTable(ArrayList<Book> booklist) {
			Object[][] obj=new Object[booklist.size()][5];
			for(int i=0;i<booklist.size();i++) {
				obj[i][0]=booklist.get(i).getBookid();
				obj[i][1]=booklist.get(i).getTitle();
				obj[i][2]=booklist.get(i).getPrice();
				obj[i][3]=booklist.get(i).getAuthor();
				obj[i][4]=booklist.get(i).getPublisher();
				
			}
			dm.setNumRows(booklist.size());
			for(int i=0;i<booklist.size();i++) {
				dm.setValueAt(obj[i][0], i, 0);
				dm.setValueAt(obj[i][1], i, 1);
				dm.setValueAt(obj[i][2], i, 2);
				dm.setValueAt(obj[i][3], i, 3);
				dm.setValueAt(obj[i][4], i, 4);
				
			}
			table.setModel(dm);
		}
		
		class TabChangehandler implements ChangeListener{

			@Override
			public void stateChanged(ChangeEvent e) {
				int index=tabbedpane.getSelectedIndex();
				if(index==0) {
					System.out.print("Insert");
				}
				if(index==1) {
					ArrayList<Book> booklist=fetchBookrecords();
					updateTable(booklist);
					
				}
						
			}
			
		}
		
		class UpdateBookRecord implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Book> updatedBooklist=readTableData();
				String q="update Book set title=?, price=?,author=?,publisher=? where bookid=?";
				try {
					ps=con.prepareStatement(q);
					for(int i=0;i<updatedBooklist.size();i++) {
						ps.setString(1, updatedBooklist.get(i).getTitle());
						ps.setDouble(2, updatedBooklist.get(i).getPrice());
						ps.setString(3, updatedBooklist.get(i).getAuthor());
						ps.setString(4, updatedBooklist.get(i).getPublisher());
						ps.setInt(5, updatedBooklist.get(i).getBookid());
						ps.executeUpdate();					}
				}
				catch(SQLException b) {
					System.out.println(b.getMessage());
				}
			}
			ArrayList<Book> readTableData(){
				ArrayList<Book> updatedBookList =new ArrayList<Book>();
				for(int i=0;i<table.getRowCount();i++) {
					Book b=new Book();
					b.setBookid(Integer.parseInt(table.getValueAt(i, 0).toString()));
					b.setTitle(table.getValueAt(i, 1).toString());
					b.setPrice(Double.parseDouble(table.getValueAt(i, 2).toString()));
					b.setAuthor(table.getValueAt(i, 3).toString());
					b.setPublisher(table.getValueAt(i, 4).toString());
					updatedBookList.add(b);
				}
				return updatedBookList;
			}
			
		}
		
		class DeleteBookrecord implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				int rowNo=table.getSelectedRow();
				if(rowNo!=-1) {
					int id=(int)table.getValueAt(rowNo, 0);
					String q="Delete from Book where bookid=?";
					try {
						ps=con.prepareStatement(q);
						ps.setInt(1, id);
						ps.execute();
					}
					catch(SQLException c) {
						System.out.println(c.getMessage());
					}
					finally {
						ArrayList<Book> bookList=fetchBookrecords();
						updateTable(bookList);
					}
				}
			}
			
		}

		
	}

