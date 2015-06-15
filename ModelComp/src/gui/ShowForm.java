package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ShowForm extends JPanel {
	JTextField tStep;
	public JTextField tMessage;
	//ShowWork owner1;
	ShowForm(ShowWork owner) {
		//owner1 = owner;
		//сформувати необхідні gui-елементи
		JLabel lForm = new JLabel("Формування даних.");
		JLabel lStep = new JLabel("Вкажіть кількість кроків");
		tStep = new JTextField(2);
		tStep.setMaximumSize(new Dimension(10,20));
		tStep.setText("3");
		tMessage = new JTextField(50);
		tMessage.setMaximumSize(new Dimension(100,20));
		tMessage.enable(false);
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
	}
	
	public void setMessage(String text){
		tMessage.setText(text);
		tMessage.repaint();
		revalidate();
		repaint();
		//tMessage.paintCompanent();
		//owner1.paintCompanent();
	}
}
