package gui.eval;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EtchedBorder;

import main.AllModels;
import main.Model;
import main.StringWork;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

public class PnEvExpr extends JPanel {
	private AllModels env = null;
	private JLabel lInit;
	JTextField tInit;
	JLabel lNodef;
	JTextField tNodef;
	JTextArea tResult;
	JLabel lStep;
	JTextField tStep;
	//private ShowWork owner;   //     !!!!!!ref!!!!!!!!!!!!!!!!!!
	private Model model =null;
	private String type = "Calculus";
	private JCheckBox chTest;
	
	
	//ShowEvalLambda(ShowWork owner) {   //     !!!!!!ref!!!!!!!!!!!!!!!!!!
	PnEvExpr() {                   //     !!!!!!ref!!!!!!!!!!!!!!!!!!       
		//сформувати необхідні gui-елементи 
		lInit = new JLabel("Вираз");
		tInit = new JTextField(30);
		tInit.setMaximumSize(new Dimension(200, 20));	
		JLabel lResult = new JLabel("Результат");
		tResult = new JTextArea();
		tResult.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tResult.setWrapStyleWord(true);
		tResult.setLineWrap(true);
		tResult.setRows(2);
		tResult.setColumns(50);
		tResult.setMaximumSize(new Dimension(100, 40));
		tResult.setEditable(false);
		
		//this.owner = owner;                    //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());
		//---------------------------
		Box init = Box.createHorizontalBox();
		init.add(Box.createGlue());
		init.add(lInit);
		init.add(tInit);
		init.add(Box.createGlue());
		Box result = Box.createHorizontalBox();
		result.add(Box.createGlue());
		result.add(lResult);
		result.add(tResult);
		result.add(Box.createGlue());
		//------------------------------------
		add(Box.createGlue());
		add(init);	
		add(Box.createVerticalStrut(3));
		
		Box forSteps = Box.createHorizontalBox();
		add(forSteps);
		
		forSteps.add(Box.createGlue());
		
		JLabel lTest = new JLabel("\u0422\u0435\u0441\u0442\u043E\u0432\u0438\u0439 \u0440\u0435\u0436\u0438\u043C");
		forSteps.add(lTest);
		
		chTest = new JCheckBox("");
		forSteps.add(chTest);
		
		Component gl0 = Box.createGlue();
		forSteps.add(gl0);
		lNodef = new JLabel("Невизначено");
		forSteps.add(lNodef);
		
		tNodef = new JTextField(5);
		forSteps.add(tNodef);
		tNodef.setMaximumSize(new Dimension(40,20));
		Component gl1 = Box.createGlue();
		forSteps.add(gl1);
		lStep = new JLabel("Виконано кроків");
		forSteps.add(lStep);
		tStep = new JTextField(5);
		forSteps.add(tStep);
		tStep.setMaximumSize(new Dimension(40,20));
		tStep.setEditable(false);
		Component glue = Box.createGlue();
		forSteps.add(glue);
		Component verticalStrut = Box.createVerticalStrut(5);
		add(verticalStrut);
		add(result);	
		add(Box.createVerticalStrut(5));
		add(Box.createGlue());
	}
	
	
	//public void setModel(String type, Model model) {
	public void show(AllModels env){		
		this.model = env.getModel();
		this.type = env.getType();
		this.env = env;
		tNodef.setText("1000");	
		tResult.setText("");
		tStep.setText("");
	}
	
	public String getLambda() {
		 return tInit.getText();
	}
	public boolean getTest() {
		 return chTest.isSelected();
	}
	public int getNodef() { 
		return new Integer(tNodef.getText());
	}
	
	public void setResult(String text,String step)	{
		tResult.setText(text); 
		tStep.setText(step);
	}
}
