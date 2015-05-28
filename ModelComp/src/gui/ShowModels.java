package gui;
import java.awt.*;
import javax.swing.*;
import db.*;


public class ShowModels extends JFrame {
	DbAccess db;
	
	public ShowModels(DbAccess db){
		super("mAlgorithm");
		this.db = db;
		//  ���������� �������� gui-�������� 
		JLabel label = new JLabel("�������� ��������� �������");
		label.setHorizontalAlignment(label.CENTER);
		label.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,20));
		//-------------------------------
		ShowModelOne showModel = new ShowModelOne(db, this);
		//-------------------------------
		JPanel modelButtons = new ShowModelButtons(db);
		//===============================
		// ��������� ��
		getContentPane().add(label, BorderLayout.NORTH);
		getContentPane().add(showModel, BorderLayout.CENTER);
		getContentPane().add(modelButtons, BorderLayout.SOUTH);
		setSize(800,500);	
	}	
	
}
