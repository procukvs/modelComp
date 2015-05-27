package gui;

import java.awt.*;
import javax.swing.*;

public class ShowModelButtons extends JPanel {
	
	ShowModelButtons(){
		//сформувати необхідні gui-елементи 
		JButton add = new JButton("Новий");
		JButton addBase = new JButton("Новий на основі");
		JButton report = new JButton("Звіти");
		JButton delete = new JButton("Вилучити");
		JButton file = new JButton("Файл ...");
		JTextField nmFile = new JTextField(70);
		nmFile.setMaximumSize(new Dimension(300,100));
		nmFile.setMinimumSize(new Dimension(50,100));
		JButton work = new JButton("Рoбота з алгоритмом");
		JButton output = new JButton("Вивести алгоритм в файл");
		JButton input = new JButton("Ввести алгоритм з файлу");
		JButton quit = new JButton("Вийти");
	
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
		// встановити слухачів !!!			
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
