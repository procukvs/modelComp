package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class ShowTesting extends JPanel {
	JTextField sLeft;
	JTextField sRigth;
	
	ShowTesting() {
		//���������� �������� gui-�������� 
		sLeft = new JTextField(30);
		sLeft.setMaximumSize(new Dimension(40,20));
		sRigth = new JTextField(30);
		sRigth.setMaximumSize(new Dimension(40,20));
		// ������� ���������
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setBorder(new EtchedBorder());
		add(sLeft);
		add(sRigth);
		//pack();
	}
}
