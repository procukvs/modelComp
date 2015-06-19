package gui;

import javax.swing.*;
import javax.swing.tree.*;

public class ShowTree extends JPanel {
	
	DefaultMutableTreeNode root;
	ShowTree() {
		JTree tree = new JTree(root,true);
		add(new JScrollPane(tree));
	}
	
	public void setTree(DefaultMutableTreeNode root1){
		root = root1;
	}
}
