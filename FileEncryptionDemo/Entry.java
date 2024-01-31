package filesEncryptionApp;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/*===============*/
public class Entry
/*===============*/
	{
public	File	f;	// null indicates entry is closed
public	byte[]	content;
public	JPanel	pan;
public	JButton	bopen, bclose, bup, bdown, bsize, bdel;
public	JTextField	tf_filename, tf_comment;
public	JLabel	lab_size;
	
/*--------------------*/
public	Entry(File fin)
/*--------------------*/
	{
	f = fin;
	content = null;
	startup(fin.getName(), (int) fin.length(), "Add Comment");
	}

/*--------------------------------------------*/
public	Entry(byte[] fn, byte[] co, byte[] con)
/*--------------------------------------------*/
	{
	f = null;
	content = con;
	startup(new String(fn), content.length, new String(co));
	}

/*-------------------------------------------------------------------*/
public	void	startup(String filename, int filesize, String comment)
/*-------------------------------------------------------------------*/
	{
	pan    = new JPanel();
	bopen  = new JButton("Open");
	bclose = new JButton("Close");
	bup    = new JButton("^");
	bdown  = new JButton("v");
	bdel   = new JButton("Delete");
	tf_filename = new JTextField(filename, 30);
	tf_comment  = new JTextField(comment, 90);
	lab_size = new JLabel(String.format("%8d", filesize / 1024));
	lab_size.setPreferredSize(new Dimension(50, 20));
	pan.add(bopen);
	pan.add(bclose);
	pan.add(bup);
	pan.add(bdown);
	pan.add(lab_size);
	pan.add(tf_filename);
	pan.add(bdel);
	pan.add(tf_comment);
	}

/*----------------------*/
public	void	do_open()
/*----------------------*/
	{
		// assume have valid content
		// check if file exists
	f = new File(tf_filename.getText());
	if (f.exists())
		{
		if (JOptionPane.showConfirmDialog(null,
				"File " + f.getName() + " exists. Overwrite?",
				"Open File", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;
		}
	
		// create real file
	FileIO.write(f, content);
	}
/*-----------------------*/
public	void	do_close()
/*-----------------------*/
	{
		// read contents into program and then erase file
	content = FileIO.read(f);
	lab_size.setText(String.format("%8d",  content.length / 1024));
	erase_file();
	}

/*-------------------------*/
public	void	erase_file()
/*-------------------------*/
	{
		// erase content of real file
	Random	rand = new Random();
	byte[]	b = new byte[(int)f.length()];
	rand.nextBytes(b);
	FileIO.write(f,  b);
	
		// delete real file
	f.delete();
	f = null;	
	}

/*-------------------------------------------------------*/
public	void	set_state(File ff, byte[] cont, String fn,
		String sz, String com)
/*-------------------------------------------------------*/
	{
	f = ff;
	content = cont;
	tf_filename.setText(fn);
	lab_size.setText(sz);
	tf_comment.setText(com);
	if (f == null)
		{
		bopen.setEnabled(true);
		bclose.setEnabled(false);
		}
	else
		{
		bopen.setEnabled(false);
		bclose.setEnabled(true);		
		}
	}

	}