package gui;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.tree.*;

public class ShowTree extends JPanel {
	
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("Testing");
	ShowTree(DefaultMutableTreeNode root) {
		
		JTree tree = new JTree(root,true);
		JScrollPane p = new JScrollPane(tree);
						//p.setSize(600,600);
						//p.setMaximumSize(new Dimension(200,100));
		p.setPreferredSize(new Dimension(650, 110));
		add(p);
							//setSize(200,200);
	}
	
	public void setTree(DefaultMutableTreeNode root1){
		root = root1;
	}
	
	public DefaultMutableTreeNode getRoot() {return root;}
}
