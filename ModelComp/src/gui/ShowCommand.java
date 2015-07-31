package gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import main.*;

public class ShowCommand extends JDialog  {
	private JLabel lWhat;
	//================================  showRule --- showRuleM 
	private ShowRule showRule;
	//private ShowRuleM showRuleM;
	//================================  showRule --- showRuleM 
	private JButton test;
	private JButton structure;
	private JButton yes;
	private JButton cancel;
	private Command command = null;
	private String type = "Algorithm";
	private Model model = null;
	private Box mainBox ;
	private ShowTree showTree;
	
	ShowCommand(Frame owner){
		super(owner, "Command");
		setModal(true);
		
		//сформувати необхідні gui-елементи
		//ShowCommandHead showHead = new ShowCommandHead();
		lWhat = new JLabel("Edit");
		lWhat.setHorizontalAlignment(lWhat.CENTER);
		lWhat.setFont(new Font("Courier",Font.BOLD|Font.ITALIC,16));
		//================================  showRule --- showRuleM 
		showRule = new ShowRule(this);
		//showRuleM = new ShowRuleM(this);
		//================================  showRule --- showRuleM 
		yes = new JButton("Зберегти");
		test = new JButton("Тестувати");
		structure = new JButton("Структура");
		cancel = new JButton("Не зберігати");
		
		// формуємо розміщення
		setLayout(new BorderLayout());
		//-----------------------------
		mainBox = Box.createVerticalBox();
		
		mainBox.add(showRule);
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
		//setSize(400,200);
		// встановити слухачів !!!	
		test.addActionListener(new LsTest());
		yes.addActionListener(new LsYes());
		cancel.addActionListener(new LsCancel());
		structure.addActionListener(new LsStructure());
	}
	
	//описуємо класи - слухачі !!!!!!
	class LsYes implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			command = showRule.getCommand();
			if (type.equals("Post")){
				command = showRule.getCommand();
				ArrayList <String> mes = ((Derive)command).iswfCommand((Post)model);
				if (mes.size() == 0) hide();
				else JOptionPane.showMessageDialog(ShowCommand.this,StringWork.transferToArray(mes)); 
				
			} else {
				ArrayList <String> mes = showRule.testAllCommand();
				if (mes.size() == 0) {
					command = showRule.getCommand();
					hide();
				} else JOptionPane.showMessageDialog(ShowCommand.this,StringWork.transferToArray(mes)); 
			}	
		}	
	}
	class LsCancel implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			//	JOptionPane.showMessageDialog(ShowCommand.this,"Cancel !");
			command = null;
			hide();
		}	
	}
	
	class LsTest implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			Recursive r = (Recursive)model;
			Function f = (Function)showRule.getCommand();
			//System.out.println(f.getName() + " ...." + f.gettxBody());
			showRule.lTesting.setText(r.fullAnalys(f.getName(), f.gettxBody()));
		}
	}
	
	class LsStructure implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			Recursive r = (Recursive)model;
			Function f = (Function)showRule.getCommand();
			//System.out.println("LsStructure" + f.getName() + " ...." + f.gettxBody()+ ".."+  f.getiswf() );
			//showRule.lTesting.setText(r.fullAnalys(f.getName(), f.gettxBody()));
			RecBody rb = r.analysRecBody(f.gettxBody());
			if(rb != null){
				//RecBody rb = r.map.get(f.getName());
				String sRoot = f.getName() + "...";
				DefaultMutableTreeNode root = new DefaultMutableTreeNode(sRoot,true);
				rb.formTree(root);
				if (showTree != null) mainBox.remove(showTree);
				showTree = new ShowTree(root);
										//testTree.setMaximumSize(new Dimension(1600,100));
										//testTree.setSize(100,100);
				mainBox.add(showTree);
										//endBox.add(testTree);
									//testTree.setVisible(false);
				pack();
				//testTree.setTree(rb.formTree());
				//testTree.setVisible(true);
			}
			
		}
	}	
	
	public void setCommand(String what, String type, Model model,  int id){
		command = null;
		this.type = type; this.model = model;
		if (what == "Add") lWhat.setText(Model.title(type, 9)); else lWhat.setText("Редагувати");
		if (showTree != null) mainBox.remove(showTree);
		test.setVisible(type.equals("Recursive"));
		structure.setVisible(type.equals("Recursive"));
		showRule.setRule(type, model, id, what);
		pack();
	}
	public Command getCommand() { return command;}
	
	public void setTreeVisible(boolean visible){ if (showTree != null) showTree.setVisible(visible);}
		
}
