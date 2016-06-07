package gui.eval;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.*;

public class ShowGroup extends JPanel {
	JToggleButton th ;
	JToggleButton all;
	JToggleButton step;
	JLabel lCnt;
	//ShowGroup(ShowSteps owner) {
	ShowGroup() {
		//���������� �������� gui-��������
		JLabel lWhat = new JLabel("�� ����������� ?");
		th = new JToggleButton("�������",true);
		th.setMaximumSize(new Dimension(200,20));
		all = new JToggleButton("�� �������");
		all.setMaximumSize(new Dimension(200,20));
		step = new JToggleButton("�� ������� (���������)");
		step.setMaximumSize(new Dimension(200,20));
		lCnt = new JLabel("0");
		ButtonGroup bg = new ButtonGroup();
		bg.add(th);
		bg.add(all);
		bg.add(step);
	
		
		//=================================
		// ������� ���������
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		//setBorder(new EtchedBorder());
		add(lWhat);
		add(Box.createHorizontalStrut(12));
		add(th);
		add(Box.createHorizontalStrut(2));
		add(all);
		add(Box.createHorizontalStrut(2));
		add(step);
		add(Box.createHorizontalStrut(12));
		add(lCnt);
	}	
	
}
