package gui;

import java.awt.*;
import javax.swing.*;

public class ShowModelButtons extends JPanel {
	
	ShowModelButtons(){
		//���������� �������� gui-�������� 
		JButton add = new JButton("�����");
		JButton addBase = new JButton("����� �� �����");
		JButton report = new JButton("����");
		JButton delete = new JButton("��������");
		JButton file = new JButton("���� ...");
		JTextField nmFile = new JTextField(70);
		nmFile.setMaximumSize(new Dimension(300,100));
		nmFile.setMinimumSize(new Dimension(50,100));
		JButton work = new JButton("�o���� � ����������");
		JButton output = new JButton("������� �������� � ����");
		JButton input = new JButton("������ �������� � �����");
		JButton quit = new JButton("�����");
	
				//=================================
				Box box1 = Box.createHorizontalBox();
				box1.add(add);
				box1.add(Box.createHorizontalStrut(5));
				box1.add(addBase);
				box1.add(Box.createHorizontalStrut(5));
				box1.add(report);
				box1.add(Box.createHorizontalStrut(5));
				box1.add(delete);
				box1.add(Box.createHorizontalStrut(5));
				box1.add(work);
				box1.add(Box.createHorizontalStrut(15));				
				box1.add(quit);
			
				Box box2 = Box.createHorizontalBox();
				//box2.add(Box.createHorizontalStrut(5));
				box2.add(file);
			//	box2.add(nmFile);
				box2.add(Box.createHorizontalStrut(5));
				box2.add(output);
				box2.add(Box.createHorizontalStrut(5));
				box2.add(input);
				//box2.add(Box.createHorizontalStrut(5));
				
				Box mainBox = Box.createVerticalBox();
				mainBox.add(box1);
				mainBox.add(box2);
			//	buttons.add(mainBox);
		// ���������� �������� !!!			
			//	add.addActionListener(new Add());
			//	addBase.addActionListener(new AddAs());
			//	report.addActionListener(new ModelReport());
			//	delete.addActionListener(new ModelDelete());
			//	file.addActionListener(new SetNameFile());
			//	work.addActionListener(new ModelWork());
			//	output.addActionListener(new ModelOutput());
			//	input.addActionListener(new ModelInput());
			//	quit.addActionListener(new Quit());
	}
	//===============================
	

}
