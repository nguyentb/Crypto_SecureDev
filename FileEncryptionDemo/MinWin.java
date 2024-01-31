package filesEncryptionApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*=========================================================*/
public class MinWin extends JFrame implements ActionListener
/*=========================================================*/
	{

/*-----*/
MinWin()
/*-----*/
	{
	setSize(100, 100);
	setLocation(10, 10);
	
	bmax  = new JButton("Max");
	bquit = new JButton("Quit");
	bmax .addActionListener(this);
	bquit.addActionListener(this);
	add(bmax, "West");
	add(bquit, "East");
	}

/*-----------------------*/
void	activate(Index in)
/*-----------------------*/
	{
	index = in;
	index.setVisible(false);
	setVisible(true);
	}

/*-------------------------------------------*/
public	void	actionPerformed(ActionEvent e)
/*-------------------------------------------*/
	{
	Object	b = e.getSource();
	if (b == bmax)
		{
		index.setVisible(true);
		setVisible(false);
		}
	else	// must be quit
		{
		index.write_index();
		System.exit(0);
		}
	}

private	Index	index;
private	JButton	bmax, bquit;
	}