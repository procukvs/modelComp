package gui.eval;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import main.*;
import java.awt.event.*;




public class PnSelFunction extends JPanel {
	private DgEval dEval;
	private JLabel lName;
	//private JTextField tName;
	private JComboBox cbName;
	private JLabel lRank;
	private JTextField tRank;
	private JLabel lConst;
	private JCheckBox cConst;
	
	private JLabel lBody;
	private JTextField tBody;
	
	private JLabel lCom;
	private JTextField tCom;
	//private String[] aa = {"uu"}; 
	private JLabel lParam;
	JTextField tParam[];
	JLabel lNodef;
	JTextField tNodef;
	JTextField tResult;
	JLabel lStep;
	JTextField tStep;
	
	private Recursive rec = null;
	private Function f = null;
	private int rank = 0;
	
	//ShowFunction(ShowWork owner) {  // !!!!!!ref!!!!!!!!!!!!!!!!!!
	PnSelFunction() {                // !!!!!!ref!!!!!!!!!!!!!!!!!!            
		lName = new JLabel("Функція (ХХ)");
		cbName = new JComboBox();
		//tName = new JTextField(10);
		//tName.setMaximumSize(new Dimension(15,20));
		//tName.enable(false);
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
		
		lParam = new JLabel("Аргументи");
		tParam = new JTextField[10];//JTextField(30);
		String si;
		for (int i = 0; i < 10; i++){
			tParam[i] = new JTextField(2);
			//tParam[i].setColumns(5);
			tParam[i].setMaximumSize(new Dimension(10,20));
			si = ((Integer)i).toString();
			tParam[i].setActionCommand(si);
			//tParam[i].addActionListener(new LtParam());
		}
		lNodef = new JLabel("Невизначено");
		tNodef = new JTextField(5);
		tNodef.setMaximumSize(new Dimension(40,20));
		
		JLabel lResult = new JLabel("Результат");
		tResult = new JTextField(30);
		tResult.setMaximumSize(new Dimension(100,20));
		tResult.setEditable(false);
		lStep = new JLabel("Виконано кроків");
		tStep = new JTextField(5);
		tStep.setMaximumSize(new Dimension(40,20));
		tStep.setEditable(false);
		
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());
		//----------------------
		Box bxHead = Box.createHorizontalBox();
		bxHead.add(Box.createGlue());
		bxHead.add(lName);
		bxHead.add(Box.createHorizontalStrut(2));
		bxHead.add(cbName);
		//bxHead.add(tName);
		bxHead.add(Box.createGlue());
		bxHead.add(Box.createHorizontalStrut(30));
		bxHead.add(lRank);
		bxHead.add(Box.createHorizontalStrut(2));
		bxHead.add(tRank);
		bxHead.add(Box.createHorizontalStrut(22));
		bxHead.add(lConst);
		bxHead.add(Box.createHorizontalStrut(2));
		bxHead.add(cConst);
		bxHead.add(Box.createGlue());
		//----------------------
		Box bxBody = Box.createHorizontalBox();
		bxBody.add(Box.createGlue());
		bxBody.add(lBody);
		bxBody.add(Box.createHorizontalStrut(2));
		bxBody.add(tBody);
		bxBody.add(Box.createGlue());
		//----------------------
		Box bxCom = Box.createHorizontalBox();
		bxCom.add(Box.createGlue());
		bxCom.add(lCom);
		bxCom.add(Box.createHorizontalStrut(2));
		bxCom.add(tCom);
		bxCom.add(Box.createGlue());
		//---------------------------
		Box bxArg = Box.createHorizontalBox();
		bxArg.add(Box.createGlue());
		bxArg.add(lParam);
		for(int i = 0; i < 10; i++)	bxArg.add(tParam[i]);
	    bxArg.add(Box.createGlue());
		bxArg.add(lNodef);
		bxArg.add(tNodef);
		bxArg.add(Box.createGlue());
		Box bxResult = Box.createHorizontalBox();
		bxResult.add(Box.createGlue());
		bxResult.add(lResult);
		bxResult.add(tResult);
		bxResult.add(Box.createGlue());
		bxResult.add(lStep);
		bxResult.add(tStep);
		bxResult.add(Box.createGlue());
		//------------------------------------		
		//----------------------------
		add(Box.createGlue());
		add(Box.createVerticalStrut(5));
		add(bxHead);
		add(Box.createVerticalStrut(3));
		add(bxBody);	
		add(Box.createVerticalStrut(3));
		add(bxCom);	
		add(Box.createVerticalStrut(10));
		add(bxArg);	
		add(Box.createVerticalStrut(3));
		add(bxResult);	
		add(Box.createVerticalStrut(5));
		add(Box.createGlue());
		//--------------------------
			
		cbName.addActionListener(new FuncName());
		for (int i = 0; i < 10; i++) tParam[i].addActionListener(new LtParam());
	}
	
	public void setEnv(DgEval dEval){
		this.dEval = dEval;
	}
	public void setModel(Model model) {
		int cnt=0;
		rec = (Recursive)model;
		cnt = rec.program.size();
		for (int i = 0; i < cnt; i++){
			f = (Function)rec.program.get(i);
			cbName.addItem(f.getName());
		}
		cbName.setEnabled(cnt>0);
		if (cnt==0){
			lName.setText("Функція ... ");
			rank = 0;
			tRank.setText("" + rank); 
			cConst.setSelected(false);
			tBody.setText(""); 
			tCom.setText("");
			for(int i = 0; i < 10; i++)tParam[i].setVisible(false);
		} else {
			f = (Function)rec.program.get(cbName.getSelectedIndex());
			setFunction(f); 
		}
	
		tNodef.setText("1000");	
		tResult.setText("");
		tStep.setText("");
	}
	
	private void setFunction(Function f){
		lName.setText("Функція (" + f.getId() + ") ");
		rank = f.getRank();
		tRank.setText("" + rank); 
		cConst.setSelected(f.getisConst());
		tBody.setText(f.gettxBody()); 
		tCom.setText(f.gettxComm());
		for(int i = 0; i < 10; i++){
			tParam[i].setVisible(i<rank);
			tParam[i].setText("");
		}
		tParam[0].requestFocus();
		tResult.setText("");
		tStep.setText("");
	}

	private class FuncName implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int i = cbName.getSelectedIndex();
			f = (Function)rec.program.get(i);
			setFunction(f); 
			dEval.setShow(false);
		}
	}
	
	class LtParam implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String sindex = e.getActionCommand();
			Integer index = new Integer(sindex);
			String sParam = tParam[index].getText();
			tResult.setText("");
			tStep.setText("");
			if(StringWork.isNatur(sParam)) {
				if(index < rank-1) tParam[index+1].requestFocus();
				else tNodef.requestFocus();
			} else {
				JOptionPane.showMessageDialog(PnSelFunction.this, 
						"Значення " + sindex + " аргументу " + sParam + " - не натуральне число !");
			}
		}	
	}	
	
	// перевіряє встановлені аргументи і кількiсть кроків на коректність !!! 
	public String testArguments(){
		String text =  "";
		for(int i=0; i < rank; i++) 
			if(!StringWork.isNatur(tParam[i].getText())) text = text + tParam[i].getText() + ":"; 
		
		if(!text.isEmpty()) text = "Аргументи:" + text + " - не натуральні числа";
		if (text.isEmpty()) 
			if(!StringWork.isNatur(tNodef.getText())) text = "Кількість кроків " + tNodef.getText() + " не натуральне число.";
		return text;
	}
	
	public Object getArguments() {
		int[] arg = new int[rank];
		for(int i=0; i < rank; i++)  arg[i] = new Integer(tParam[i].getText());
		return arg;
	}
	
	public int getNodef() { 
		return new Integer(tNodef.getText());
	}
	
	public Function getFunction() { 
		return f;
	}
	public void setResult(String text,int step)	{
		tResult.setText(text); 
		if (step == -1) tStep.setText(""); else tStep.setText("" + step);
	}	
}

