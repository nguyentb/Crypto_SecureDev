package filesEncryptionApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/*=======================================================*/
public class Jer extends JFrame implements ActionListener
/*=======================================================*/
	{
/*-----------------------------------*/
public static void main(String[] args)
/*-----------------------------------*/
	{
	new Jer();
	}

/*----------*/
public	Jer()
/*----------*/
	{
	minwin = new MinWin();
	
	setTitle("Files Encryption App");
	setSize(600, 130);
	setLocation(600, 500);
	
		// action buttons
	pbuttons = new JPanel(new GridLayout(1, 3));
	bnew  = new JButton("New");
	bopen = new JButton("Open");
	bquit = new JButton("Quit");
	bnew .addActionListener(this);
	bopen.addActionListener(this);
	bquit.addActionListener(this);
	pbuttons.add(bnew);
	pbuttons.add(bopen);
	pbuttons.add(bquit);
	add(pbuttons, "South");
	
	// text fields
	ptfs = new JPanel(new GridLayout(2, 2));
	tf_name = new JTextField(20);
	tf_pwd  = new JPasswordField(20);
	ptfs.add(new JLabel("Index File Name"));
	ptfs.add(tf_name);
	ptfs.add(new JLabel("Index Password"));
	ptfs.add(tf_pwd);
	add(ptfs, "Center");

	setVisible(true);
	tf_name.requestFocus();
	}

/*-------------------------------------------*/
public	void	actionPerformed(ActionEvent e)
/*-------------------------------------------*/
	{
	Object	b = e.getSource();
	if (b == bnew)
		do_index(true);
	else if (b == bopen)
		do_index(false);
	else if (b == bquit)
		System.exit(0);
	}

/*-------------------------------------*/
private	void	do_index(boolean is_new)
/*-------------------------------------*/
	{
	String	iname = tf_name.getText();
	String	ipwd  = tf_pwd .getText();
	
	if (iname.length() == 0)
		{
		JOptionPane.showMessageDialog(null,
				"Enter Index File Name", "Index Error",
				JOptionPane.ERROR_MESSAGE);
		return;
		}
	
	if (ipwd.length() == 0)
		{
		JOptionPane.showMessageDialog(null,
			"Enter Index Password", "Index Error",
			JOptionPane.ERROR_MESSAGE);
		return;
		}
	
	File	f = new File (iname);
	if (is_new)
		{
		if (!f.exists())
			new Index(this, minwin, iname, ipwd, null);
		else
			{
			JOptionPane.showMessageDialog(null, f.getName() + " exists",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
			}
		}
	else
		{
		if (f.exists() && f.canRead() && f.canWrite())
			new Index(this, minwin, iname, ipwd, f);
		else
			{
			JOptionPane.showMessageDialog(null, "Cant Open Index " + f.getName(),
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
			}
		}
	}


private	MinWin	minwin;
private	JPanel	pbuttons, ptfs;
private	JButton	bnew, bopen, bquit;
private	JTextField	tf_name, tf_pwd;
	}