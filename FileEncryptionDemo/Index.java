package filesEncryptionApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/*=======================================================*/
public class Index extends JFrame implements ActionListener
/*=======================================================*/
	{
	
/*--------------------------------------------------------------*/
public Index(Jer k, MinWin mw, String in, String ip, File findex)
/*--------------------------------------------------------------*/
	{
	jer = k;
	minwin = mw;
	iname = in;
	ipwd  = ip;
	
	setSize(1800, 1000);
	setLocation(100, 20);
	setLayout(new GridLayout(31, 1));
	
		// action widgets
	pbuttons = new JPanel();
	badd    = new JButton("Add File");
	bclose  = new JButton("Close Index");
	bquit   = new JButton("Quit");
	bminwin = new JButton("Minimise");
	tf_filename = new JTextField("Real File Name", 30);
	tf_index_desc = new JTextField("Add Index Comments", 70);
	
	bminwin.addActionListener(this);
	badd   .addActionListener(this);
	bclose .addActionListener(this);
	bquit  .addActionListener(this);
	
	pbuttons.add(new JLabel("File Name"));
	pbuttons.add(tf_filename);
	pbuttons.add(badd);
	pbuttons.add(bclose);
	pbuttons.add(bquit);
	pbuttons.add(bminwin);
	pbuttons.add(tf_index_desc);
	add(pbuttons);
	
		// entry rows
	nentry = 0;
	maxentry = 30;
	entry = new Entry[maxentry];
	for (int i = 0; i < maxentry; i++)
		entry[i] = null;	// will later reuse entries
	
		// read in data if opening an index
	if (findex != null)
		read_index(findex);
	
	setVisible(true);
	jer.setVisible(false);
	}

/*--------------------------------------------*/
public	void	actionPerformed(ActionEvent ae)
/*--------------------------------------------*/
	{
	Object	b = ae.getSource();
	if (b == bminwin)
		minwin.activate(this);
	else if (b == badd)
		add_file();
	else if (b == bclose)
		{
		jer.setVisible(true);
		setVisible(false);
		write_index();
		}
	else if (b == bquit)
		{
		write_index();
		System.exit(0);
		}
	else for (int i = 0; i < nentry; i++)	// an entry
		{
		Entry	e = entry[i];
		if (b == e.bopen)
			{
			e.do_open();
			e.bopen.setEnabled(false);
			e.bclose.setEnabled(true);
			e.tf_filename.setEnabled(false);
			setVisible(true);
			break;
			}
		else if (b == e.bclose)
			{
			e.do_close();
			e.bopen.setEnabled(true);
			e.bclose.setEnabled(false);
			e.tf_filename.setEnabled(true);
			if (isVisible())
				setVisible(true);
			break;
			}
		else if (b == e.bup)
			do_up(i);
		else if (b == e.bdown)
			do_down(i);
		else if (b == e.bdel)
			do_del(i);
		}
	}

/*-----------------------*/
private	void	add_file()
/*-----------------------*/
	{
		// check if filename already there
	String	filename = tf_filename.getText();
	File	f = new File(filename);
	for (int i = 0; i < nentry; i++)
		if (filename.equals(entry[i].tf_filename.getText()))
			{
			JOptionPane.showMessageDialog(null, "File " + filename
					+ " already in index",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
			}
	
		// check enough space
	if (nentry >= maxentry)
		{
		JOptionPane.showMessageDialog(null, "Maximum Number of Entries Reached",
				"Error", JOptionPane.ERROR_MESSAGE);
		return;
		}
	
		// check can access file
	if (!(f.exists() && f.canRead() && f.canWrite()))
		{
		JOptionPane.showMessageDialog(null, "Cant Open " + f.getName(),
				"Error", JOptionPane.ERROR_MESSAGE);
		return;
		}
	
		// add new entry
	if (entry[nentry] == null)
		{
		Entry	e = new Entry(f);
		entry[nentry++] = e;
		e.bopen .addActionListener(this);
		e.bclose.addActionListener(this);
		e.bup   .addActionListener(this);
		e.bdown .addActionListener(this);
		e.bdel  .addActionListener(this);
		add(e.pan);
		e.bopen.setEnabled(false);
		e.tf_filename.setEnabled(false);
		}
	else	// reuse existing entry
		{
		Entry	e = entry[nentry++];
		e.f = f;
		e.content = null;
		e.bopen.setVisible(true);
		e.bopen.setEnabled(false);
		e.bclose.setVisible(true);
		e.bclose.setEnabled(true);
		e.bup.setVisible(true);
		e.bdown.setVisible(true);
		e.bdel.setVisible(true);
		e.tf_filename.setText(f.getName());
		e.tf_filename.setVisible(true);
		e.tf_filename.setEnabled(false);
		e.tf_comment.setText("Add Comment");
		e.tf_comment.setVisible(true);
		}
	setVisible(true);
	}

/*--------------------------*/
public	void	write_index()
/*--------------------------*/
	{
		// find out how many bytes
	int	nbytes = 38;	// 37 byte initial block + 1 nentries
	nbytes += 4 + tf_index_desc.getText().length();
	
	for (int i = 0; i < nentry; i++)
		{
		Entry	e = entry[i];
		if (e.f != null)
			e.do_close();
		nbytes += 12 + e.content.length + e.tf_filename.getText().length()
				+ e.tf_comment.getText().length();
		}
	
	index_bytes = new byte[nbytes];
	ib = 0;
	
		// initial block
	Random	rand = new Random();
	for (; ib < 37; ib++)	// initial block
		index_bytes[ib] = (byte) ('a' + Math.abs(rand.nextInt()) % 26);
	
	add_bytes(tf_index_desc.getText().getBytes());
	
		// entries
	index_bytes[ib++] = (byte) nentry;
	for (int i = 0; i < nentry; i++)
		{
		Entry	e = entry[i];
		add_bytes(e.tf_filename.getText().getBytes());
		add_bytes(e.tf_comment.getText().getBytes());
		add_bytes(e.content);
		}
	
		// write to the index file
	byte[]	icomp = Crypto.compress(index_bytes);
	byte[]	icrypt = Crypto.encrypt(ipwd, icomp);
	File	f = new File(iname);
	File	f2 = new File(iname+"_bak");
	f.renameTo(f2);
	if (FileIO.write(f,  icrypt))
		f2.delete();
	}

/*-------------------------------*/
private	void	read_index(File f)
/*-------------------------------*/
	{
	byte[]	icrypt = FileIO.read(f);
	byte[]	icomp = Crypto.decrypt(ipwd, icrypt);
	index_bytes = Crypto.uncompress(icomp);
	ib = 0;
		// check initial block
	for (; ib < 37; ib++)
		{
		int	c = index_bytes[ib];
		if (c < 'a' || c > 'z')
			{
			System.err.println("Invalid Initial Block");
			System.exit(0);
			}
		}
	
		// index description
	byte[]	b = get_bytes();
	tf_index_desc.setText(new String(b));
	
		// entries
	nentry = (int) index_bytes[ib++];
	for (int i = 0; i < nentry; i++)
		{
		byte[]	b1 = get_bytes();
		byte[]	b2 = get_bytes();
		byte[]	b3 = get_bytes();
		Entry	e = new Entry(b1, b2, b3);
		entry[i] = e;
		e.bopen .addActionListener(this);
		e.bclose.addActionListener(this);
		e.bup   .addActionListener(this);
		e.bdown .addActionListener(this);
		e.bdel  .addActionListener(this);
		add(e.pan);
		e.bclose.setEnabled(false);
		}
	
	setVisible(true);
	}

/*--------------------------------*/
private	void	add_bytes(byte[] b)
/*--------------------------------*/
	{
	int	len = b.length;
	int	n = len;
	for (int i = 0; i < 4; i++)
		{
		index_bytes[ib++] = (byte) (len & 0x7f);
		len >>= 7;
		}
	for (int i = 0; i < n; i++)
		index_bytes[ib++] = b[i];
	}

/*------------------------*/
private	byte[]	get_bytes()
/*------------------------*/
	{
	int	a0 = index_bytes[ib];
	int	a1 = index_bytes[ib+1];
	int	a2 = index_bytes[ib+2];
	int	a3 = index_bytes[ib+3];
	ib += 4;
	int	len = a0 | (a1 << 7) | (a2 << 14) | (a3 << 21);
	
	byte[]	b = new byte[len];
	for (int i = 0; i < len; i++)
		b[i] = index_bytes[ib++];
	return b;
	}

	/*************/
	/*  Entries  */
	/*************/

/*-------------------------*/
private	void	do_up(int i)
/*-------------------------*/
	{
	if (i == 0)	// already at top
			return;
	
	swap(i-1, i);
	}

/*---------------------------*/
private	void	do_down(int i)
/*---------------------------*/
	{
	if (i == nentry-1)	// already at bottom
		return;
	
	swap(i, i+1);
	}

/*-------------------------------*/
private	void	swap(int i, int j)
/*-------------------------------*/
	{
	Entry	ei = entry[i];
	Entry	ej = entry[j];
	
		// save state for i
	File	tmp_f        = ei.f;
	byte[]	tmp_content  = ei.content;
	String	tmp_filename = ei.tf_filename.getText();
	String	tmp_size     = ei.lab_size.getText();
	String	tmp_comment  = ei.tf_comment.getText();
	
		// copy j to i, then tmp to j
	ei.set_state(ej.f, ej.content, ej.tf_filename.getText(),
			ej.lab_size.getText(), ej.tf_comment.getText());
	ej.set_state(tmp_f, tmp_content, tmp_filename, tmp_size, tmp_comment);
	setVisible(true);
	}

/*--------------------------*/
private	void	do_del(int i)
/*--------------------------*/
	{
		// erase file if open
	if (entry[i].f != null)
		entry[i].erase_file();
	
		// copy them upwards
	for (int j = i+1; j < nentry; j++)
		{
		Entry	e = entry[j];
		entry[j-1].set_state(e.f, e.content, e.tf_filename.getText(),
				e.lab_size.getText(), e.tf_comment.getText());
		}
	
		// hide the last entry
	nentry--;
	Entry	e = entry[nentry];
	e.bopen.setVisible(false);
	e.bclose.setVisible(false);
	e.bup.setVisible(false);
	e.bdown.setVisible(false);
	e.bdel.setVisible(false);
	e.tf_filename.setVisible(false);
	e.tf_comment.setVisible(false);
	setVisible(true);
	}

private	Jer	jer;
private	MinWin	minwin;
private	String	iname, ipwd;
private	JPanel	pbuttons;
private	JButton	badd, bclose, bquit, bminwin;
private	JTextField	tf_filename, tf_index_desc;
private	Entry[]	entry;
private	int	nentry, maxentry;	// next free one
private	byte[]	index_bytes;
private	int	ib;	// running index
	}