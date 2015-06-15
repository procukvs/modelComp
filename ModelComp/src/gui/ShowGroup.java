package gui;

import javax.swing.*;
import javax.swing.border.*;

public class ShowGroup extends JPanel {
	ShowGroup(ShowSteps owner) {
		//сформувати необхідні gui-елементи
		JLabel lWhat = new JLabel("Що переглянути ?");
		JToggleButton th = new JToggleButton("Теореми",true);
		JToggleButton all = new JToggleButton("Всі виводимі");
		JToggleButton step = new JToggleButton("Всі виводимі (покроково)");
		ButtonGroup bg = new ButtonGroup();
		bg.add(th);
		bg.add(all);
		bg.add(step);
		
		//=================================
		// формуємо розміщення
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
