package gui;

import javax.swing.*;
import javax.swing.border.*;

public class ShowGroup extends JPanel {
	ShowGroup(ShowSteps owner) {
		//���������� �������� gui-��������
		JLabel lWhat = new JLabel("�� ����������� ?");
		JToggleButton th = new JToggleButton("�������",true);
		JToggleButton all = new JToggleButton("�� �������");
		JToggleButton step = new JToggleButton("�� ������� (���������)");
		ButtonGroup bg = new ButtonGroup();
		bg.add(th);
		bg.add(all);
		bg.add(step);
		
		//=================================
		// ������� ���������
		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setBorder(new EtchedBorder());
		add(lWhat);
		add(Box.createHorizontalStrut(12));
		add(th);
		add(Box.createHorizontalStrut(2));
		add(all);
		add(Box.createHorizontalStrut(2));
		add(step);
		add(Box.createHorizontalStrut(2));
	}	
	
}
