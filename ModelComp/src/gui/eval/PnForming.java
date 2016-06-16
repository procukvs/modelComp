package gui.eval;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import main.*;

public class PnForming extends JPanel {
	private AllModels env = null;
	private Post post = null;
	//public JTextField tStep;
	private JTextField tStep;
	private JTextField tMessage;
	//public JLabel lMessage;
	private DgEval showWork;
	PnForming() {                        //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		//ShowForm(ShowWork owner) {    //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		//showWork = owner;             //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		//сформувати необхідні gui-елементи
		JLabel lForm = new JLabel("Формування даних.");
		JLabel lStep = new JLabel("Вкажіть кількість кроків");
		tStep = new JTextField(2);
		tStep.setMaximumSize(new Dimension(10,20));
		tStep.setText("3");
		//lMessage = new JLabel("Формування даних..................");
		tMessage = new JTextField(50);
		//tMessage.setMaximumSize(new Dimension(100,20));
		//tMessage.enable(false);
		//=================================
		// формуємо розміщення
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBorder(new EtchedBorder());
		//----------------------
		Box head = Box.createHorizontalBox();
		head.add(lForm);
		//----------------------
		Box step = Box.createHorizontalBox();
		step.add(Box.createGlue());
		step.add(Box.createHorizontalStrut(30));
		step.add(lStep);
		step.add(Box.createHorizontalStrut(2));
		step.add(tStep);
		step.add(Box.createHorizontalStrut(22));
		step.add(tMessage);
		//step.add(lMessage);
		step.add(Box.createHorizontalStrut(30));
		step.add(Box.createGlue());
		//----------------------------
		add(Box.createGlue());
		add(Box.createVerticalStrut(5));
		add(head);
		add(Box.createVerticalStrut(3));
		add(step);	
		add(Box.createVerticalStrut(5));
		add(Box.createGlue());
		//--------------------------
		
		tStep.addActionListener(new LstStep());
	}
	
	public void setParent(DgEval owner){       //     !!!!!!ref!!!!!!!!!!!!!!!!!!
		showWork = owner;                        //     !!!!!!ref!!!!!!!!!!!!!!!!!!
	}                                            //     !!!!!!ref!!!!!!!!!!!!!!!!!!
	//public void setModel(String type,Model model) {
	public void show(AllModels env){
		this.env = env;
		if (env.getType().equals("Post")) post = (Post)env.getModel(); else post = null;
		tMessage.setText("");
	}
	
	public void setMessage(String text){
		tMessage.setText(text);
	}
	
	public String getTStep(){ return tStep.getText();}
	
	public void setFocusIntStep() {tStep.requestFocus();}
	
	class LstStep implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			String step = tStep.getText();
			if(StringWork.isNatur(step)) {
				showWork.resertFromShowStep();
				//showEval.setVisible(false);
				//showSteps.setVisible(false);
				//show.setEnabled(false);
				//pack();
				//eval.requestFocus();
			} else JOptionPane.showMessageDialog(showWork,"Кількість кроків " + step + " - не натуральне число");;
		}
		
	}
	
	public ArrayList formingData(){
		ArrayList sl = null;
		String sParam;
		sParam = tStep.getText();
		if(StringWork.isPosNumber(sParam)) {
			int step = new Integer(sParam);
			String template = "HH:mm:ss";  
			DateFormat formatter = new SimpleDateFormat(template);
			Date cur = new Date();
			String text = "Формування " + formatter.format(cur) + " : ";
			int cnt = post.initialForm();
			for(int i = 0; i < step; i++){
				cur = new Date(); 
				tMessage.setText(text + formatter.format(cur) + " крок " + i + " .. " + cnt +".");
				//showForm.lMessage.setText(text + formatter.format(cur) + " крок " + i + " .. " + cnt +".");
						//showForm.revalidate();
						//showForm.repaint();
						//System.out.println(text + formatter.format(cur) + " крок " + i + " .. " + cnt + ".");
				cnt = post.stepForm(i+1);
			}
			cur = new Date();
			sl = post.finalForm();
			tMessage.setText(text + formatter.format(cur) + " закінчено. За " + step + " кроків створено " + sl.size() + ".");
			//showEval.setVisible(post.getIsNumeric());
			//pack();
			//show.setEnabled(true);
		}
		else {
			tMessage.setText("Кількість кроків - не натуральнe число!");
			tStep.requestFocus();
		}		
		return sl;
	}
	
	/*
	public void setMessage(String text){
		tMessage.setText(text);
		tMessage.repaint();
		revalidate();
		repaint();
		//tMessage.paintCompanent();
		//owner1.paintCompanent();
	}  */
}
