package gui.command;

import javax.swing.*;
import javax.swing.tree.*;

import gui.PnTree;
import gui.command.DgEdCommand.LsCancel;
import gui.command.DgEdCommand.LsStructure;
import gui.command.DgEdCommand.LsTest;
import gui.command.DgEdCommand.LsYes;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import main.*;

public class DgEdCommand extends JDialog {
	private JLabel lWhat;
	private PnCommand pCommand;
	private JButton test;
	private JButton structure;
	private JButton yes;
	private JButton cancel;
	private Command command = null;
	private String type = "Algorithm";
	private Model model = null;
	private Box mainBox ;
	private PnTree showTree;
	
	public DgEdCommand(Frame owner){
		super(owner, "Command");
		setModal(true);
		
		//сформувати необхідні gui-елементи
		lWhat = new JLabel("Edit");
		lWhat.setHorizontalAlignment(lWhat.CENTER);
		lWhat.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,16));

		//showRule = new ShowRule(this);  //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		pCommand = new PnCommand();        //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		pCommand.setEnv(this);         //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		yes = new JButton("Зберегти");
		test = new JButton("Тестувати");
		structure = new JButton("Структура");
		cancel = new JButton("Не зберігати");
		
		// формуємо розміщення
		setLayout(new BorderLayout());
		//-----------------------------
		mainBox = Box.createVerticalBox();
		
		mainBox.add(pCommand);
		//---------------------------		
		Box buttonBox = Box.createHorizontalBox();
		buttonBox.add(Box.createGlue());
		buttonBox.add(test);
		buttonBox.add(Box.createHorizontalStrut(15));
		buttonBox.add(yes);
		buttonBox.add(Box.createHorizontalStrut(15));
		buttonBox.add(cancel);
		buttonBox.add(Box.createHorizontalStrut(15));
		buttonBox.add(structure);
		buttonBox.add(Box.createGlue());
		//---------------------
		add(lWhat, BorderLayout.NORTH);
		add(mainBox, BorderLayout.CENTER);
		add(buttonBox, BorderLayout.SOUTH);
		pack();
		// встановити слухачів !!!	
		test.addActionListener(new LsTest());
		yes.addActionListener(new LsYes());
		cancel.addActionListener(new LsCancel());
		structure.addActionListener(new LsStructure());
	}
	
	//описуємо класи - слухачі !!!!!!
	class LsYes implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			command = pCommand.getCommand();
				ArrayList <String> mes = pCommand.testAllCommand();
				if (mes.size() == 0) {
					command = pCommand.getCommand();
					hide();
				} else JOptionPane.showMessageDialog(DgEdCommand.this,StringWork.transferToArray(mes)); 
		}	
	}
	class LsCancel implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			command = null;
			hide();
		}	
	}
	
	class LsTest implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			if(type.equals("Recursive")){
				Recursive r = (Recursive)model;
				Function f = (Function)pCommand.getCommand();
				pCommand.setLTesting(r.fullAnalys(f.getName(), f.gettxBody()));
			}
			else{
				//System.out.println("Analys " + type);
				Calculus calc = (Calculus)model;
				LambdaDecl ld = (LambdaDecl)pCommand.getCommand();
				pCommand.setLTesting(calc.fullAnalys(ld.getNum(),ld.gettxBody()));
			}
		}
	}
	
	class LsStructure implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			Recursive r = (Recursive)model;
			Function f = (Function)pCommand.getCommand();
			RecBody rb = r.analysRecBody(f.gettxBody());
			if(rb != null){
				String sRoot = f.getName() + "...";
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(sRoot,true);
				rb.formTree(root);
				if (showTree != null) mainBox.remove(showTree);
				showTree = new PnTree(root);
				mainBox.add(showTree);
				pack();
			}
			
		}
	}	
	
	public void setCommand(String what, String type, Model model,  int id){
		command = null;
		this.type = type; this.model = model;
		//System.out.println("ShowCommand:setCommand " + what + " " + type + " " + id);
		if (what == "Add") lWhat.setText(Model.title(type, 9)); else lWhat.setText("Редагувати");
		if (showTree != null) mainBox.remove(showTree);
		test.setVisible(type.equals("Recursive")||type.equals("Calculus"));
		structure.setVisible(type.equals("Recursive"));
		pCommand.setRule(type, model, id, what);
		pack();
	}
	public Command getCommand() { return command;}
	
	public void setTreeVisible(boolean visible){ if (showTree != null) showTree.setVisible(visible);}
}
