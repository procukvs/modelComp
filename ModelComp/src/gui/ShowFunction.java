package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import main.*;

public class ShowFunction extends JPanel {
	private JLabel lName;
	private JTextField tName;
	private JLabel lRank;
	private JTextField tRank;
	private JLabel lConst;
	private JCheckBox cConst;
	private JLabel lBody;
	private JTextField tBody;
	private JLabel lCom;
	private JTextField tCom;
	
	ShowFunction(ShowWork owner) {
		lName = new JLabel("Функція (ХХ)");
		tName = new JTextField(10);
		tName.setMaximumSize(new Dimension(15,20));
		tName.enable(false);
		lRank = new JLabel("Арність");
		tRank = new JTextField(2);
		tRank.setMaximumSize(new Dimension(10,20));
		tRank.enable(false);
		lConst = new JLabel("Константа ?");
		cConst = new JCheckBox();
		cConst.enable(false);
		lBody = new JLabel("Тіло функції");
		tBody = new JTextField(50);
		tBody.setMaximumSize(new Dimension(100,20));
		tBody.enable(false);
		lCom = new JLabel("Коментар");
		tCom = new JTextField(50);
		tCom.setMaximumSize(new Dimension(100,20));
		tCom.enable(false);
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());
		//----------------------
		Box head = Box.createHorizontalBox();
		head.add(Box.createGlue());
		head.add(lName);
		head.add(Box.createHorizontalStrut(2));
		head.add(tName);
		head.add(Box.createGlue());
		head.add(Box.createHorizontalStrut(30));
		head.add(lRank);
		head.add(Box.createHorizontalStrut(2));
		head.add(tRank);
		head.add(Box.createHorizontalStrut(22));
		head.add(lConst);
		head.add(Box.createHorizontalStrut(2));
		head.add(cConst);
		head.add(Box.createGlue());
		//----------------------
		Box body = Box.createHorizontalBox();
		body.add(Box.createGlue());
		body.add(lBody);
		body.add(Box.createHorizontalStrut(2));
		body.add(tBody);
		body.add(Box.createGlue());
		//----------------------
		Box com = Box.createHorizontalBox();
		com.add(Box.createGlue());
		com.add(lCom);
		com.add(Box.createHorizontalStrut(2));
		com.add(tCom);
		com.add(Box.createGlue());
		//----------------------------
		add(Box.createGlue());
		add(Box.createVerticalStrut(5));
		add(head);
		add(Box.createVerticalStrut(3));
		add(body);	
		add(Box.createVerticalStrut(3));
		add(com);	
		add(Box.createVerticalStrut(5));
		add(Box.createGlue());
		//--------------------------
	}
	
	public void setFunction(Function f){
		lName.setText("Функція (" + f.getId() + ") ");
		tName.setText(f.getName());
		tRank.setText("" + f.getRank()); 
		cConst.setSelected(f.getisConst());
		tBody.setText(f.gettxBody()); 
		tCom.setText(f.gettxComm()); 
	}

}
