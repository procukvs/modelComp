package gui.eval;

import main.*;
import model.AllModels;
import model.Model;
import model.Post;
import db.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class PnValFunction extends JPanel {
	private AllModels env=null;
	private JLabel lParam;
	private JLabel lFunction;
	JTextField tParam[];
	JTextField tResult;
	private JButton see;
	private DgEval owner;
	private String main = "";
	private int rank = 0;
	private Model model;
	private String type;
	
	
	//ShowEval(ShowWork owner) {         //     !!!!!!ref!!!!!!!!!!!!!!!!!!
	PnValFunction() {      //	     !!!!!!ref!!!!!!!!!!!!!!!!!!
		//сформувати необхідні gui-елементи 
		lParam = new JLabel("Аргументи");
		lFunction = new JLabel("Значення функції");
		tParam = new JTextField[10];//JTextField(30);
		String si;
		for (int i = 0; i < 10; i++){
			tParam[i] = new JTextField(2);
			//tParam[i].setColumns(5);
			tParam[i].setMaximumSize(new Dimension(10,20));
			si = ((Integer)i).toString();
			tParam[i].setActionCommand(si);
		}
		tResult = new JTextField(30);
		tResult.setMaximumSize(new Dimension(100,20));
		tResult.setEditable(false);
		see = new JButton("Показати результат");
		
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());
		//---------------------------
		Box init = Box.createHorizontalBox();
		init.add(Box.createGlue());
		init.add(lParam);
		for(int i = 0; i < 10; i++)	init.add(tParam[i]);
		init.add(Box.createGlue());
		init.add(see);
		init.add(Box.createGlue());
		//---------------------------
		Box result = Box.createHorizontalBox();
		result.add(Box.createGlue());
		JLabel lResult = new JLabel("Результат");
		result.add(lResult);
		result.add(tResult);
		result.add(Box.createGlue());
		//================================
		add(Box.createGlue());
		add(Box.createVerticalStrut(5));
		add(lFunction);
		add(Box.createVerticalStrut(3));
		add(init);	
		add(Box.createVerticalStrut(3));
		add(result);	
		add(Box.createVerticalStrut(5));
		add(Box.createGlue());
		
		// встановити слухачів !!!			
		for (int i = 0; i < 10; i++) tParam[i].addActionListener(new LtParam());
		see.addActionListener(new LSee());
	}
	
	public void setParent(DgEval owner){           //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		this.owner = owner;                        //     !!!!!!ref!!!!!!!!!!!!!!!!!!
	}  
	//     !!!!!!ref!!!!!!!!!!!!!!!!!!
	//public void setModel(String type, Model model) {
	public void show(AllModels env){	
		//  only for Post 
		this.model = env.getModel();
		this.type = env.getType();
		this.env=env;
		//boolean isPost = type.equals("Post");
		if(!type.equals("Recursive")){
			rank = model.getRank();
			main = model.getMain();
			
			for(int i = 0; i < 10; i++)	tParam[i].setVisible(false);
			//lParam.setVisible(model.getIsNumeric());
			for(int i = 0; i < rank; i++){
				tParam[i].setVisible(true);
				tParam[i].setText("");
			}
			tParam[0].requestFocus();
			lParam.setVisible(true);	
		}
		tResult.setText("");
	}

	// перевіряє встановлені аргументи і кількiсть кроків на коректність !!! 
	public String testArguments(){
		String text = "";
		if (!type.equals("Post")){
			if(type.equals("Recursive") || type.equals("Computer") || model.getIsNumeric()){
				for(int i=0; i < rank; i++) 
					if(!StringWork.isNatur(tParam[i].getText())) text = text + tParam[i].getText() + ":"; 
				if(!text.isEmpty()) text = "Аргументи:" + text + " - не натуральні числа";
			}	
	//		else text = testInitial();
			if (text.isEmpty()) ;
	//			if(!StringWork.isNatur(tNodef.getText())) text = "Кількість кроків " + tNodef.getText() + " не натуральне число.";
		}
		return text;
	}
	
	public void setResult(String text,int step)	{
		tResult.setText(text); 
	}
	
	class LtParam implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String sindex = e.getActionCommand();
			Integer index = new Integer(sindex);
			String sParam = tParam[index].getText();
			tResult.setText("");
			if(StringWork.isNatur(sParam)) {
				if(index < rank-1) tParam[index+1].requestFocus();
				else see.requestFocus();
			} else {
				JOptionPane.showMessageDialog(PnValFunction.this, 
						"Значення " + sindex + " аргументу " + sParam + " - не натуральне число !");
			}
		}	
	}
	class LSee implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String text = "";
			String sParam;
			String input = "";
			//JOptionPane.showMessageDialog(ShowEval.this, "See result");
			for (int i = 0; i < rank; i++) {
				sParam = tParam[i].getText();
				if(StringWork.isNatur(sParam)) {
					if (i> 0) input = input + "#";
					input = input + StringWork.toInternal(new Integer(sParam));
				} else text = text + " " + sParam;
			}
			if(!text.isEmpty()) text = "Аргументи:" + text + " - не натуральні числа";
			if(text.isEmpty()) {
				//=================================
				text = ((Post)model).findResult(input); 
				tResult.setText(text);
			} else JOptionPane.showMessageDialog(PnValFunction.this, text);
		}
	}

}
