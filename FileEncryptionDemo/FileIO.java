package filesEncryptionApp;

import java.io.*;

/*================*/
public class FileIO
/*================*/
	{
/*---------------------------------*/
static	public	byte[]	read(File f)
/*---------------------------------*/
	{
	FileInputStream	fin = null;
	try
		{
		fin = new FileInputStream(f);
		}
	catch (FileNotFoundException x)
		{
		System.err.println("File ["+f.getName()+ "] Not Found Exception");
		System.exit(0);
		}
	
	byte[]	b = new byte[(int)f.length()];
	try
		{
		fin.read(b);
		fin.close();
		}
	catch (IOException x)
		{
		System.err.println("File ["+f.getName()+ "] IOException");
		System.exit(0);		
		}
	return b;
	}

/*--------------------------------------------*/
static	public	boolean	write(File f, byte[] b)
/*--------------------------------------------*/
	{
	FileOutputStream	fout = null;
	try
		{
		fout = new FileOutputStream(f);
		}
	catch (FileNotFoundException x)
		{
		System.err.println("File ["+f.getName()+ "] Not Found Exception");
		return false;
		}
	
	try
		{
		fout.write(b);
		fout.close();
		}
	catch (IOException x)
		{
		System.err.println("File ["+f.getName()+ "] IOException");
		return false;		
		}
	
	return true;
	}
	}