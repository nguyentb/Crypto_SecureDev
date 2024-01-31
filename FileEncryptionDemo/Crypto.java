package filesEncryptionApp;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.zip.*;

/*================*/
public class Crypto
/*================*/
	{	
		/****************/
		/*  Encryption  */
		/****************/

/*---------------------------------------------------*/
public	static	byte[]	encrypt(String key, byte[] in)
/*---------------------------------------------------*/
	{
	return do_crypt(Cipher.ENCRYPT_MODE, key, in);
	}

/*---------------------------------------------------*/
public	static	byte[]	decrypt(String key, byte[] in)
/*---------------------------------------------------*/
	{
	return do_crypt(Cipher.DECRYPT_MODE, key, in);
	}

/*--------------------------------------------------------------*/
private	static	byte[]	do_crypt(int mode, String key, byte[] in)
/*--------------------------------------------------------------*/
	{
		// pad key to 16 characters
	int	klen = key.length();
	for (int i = klen; i < 16; i++)
		key += 'z';
	
		// initialise cipher
	Key	k = new SecretKeySpec(key.getBytes(), "AES");
	
	Cipher	c = null;
	try
		{
		c = Cipher.getInstance("AES");
		}
	catch (NoSuchPaddingException e)
		{
		System.err.println("No Such Padding Exception");
		System.exit(0);
		}
	catch (NoSuchAlgorithmException e)
		{
		System.err.println("No Such Algorithm Exception");
		System.exit(0);	
		}
	
	try
		{
		c.init(mode, k);
		}
	catch (InvalidKeyException e)
		{
		System.err.println("Invalid Key Exception");
		System.exit(0);	
		}
	
		// encrypt or decrypt
	try
		{
		return c.doFinal(in);
		}
	catch (BadPaddingException e)
		{
		System.err.println("Bad Padding Exception");
		System.exit(0);	
		}
	catch (IllegalBlockSizeException e)
		{
		System.err.println("Bad Padding Exception");
		System.exit(0);	
		}
	return null;
	}

	/*****************/
	/*  Compression  */
	/*****************/

/*---------------------------------------*/
public	static	byte[]	compress(byte[] b)
/*---------------------------------------*/
	{
	Deflater	d = new Deflater();
	d.setInput(b);
	d.finish();
	byte[]	tmp = new byte[b.length];	// big enough, assume gets smaller
	int	n = d.deflate(tmp);
	byte[]	out = new byte[n+4];	// put length in first 4 bytes
	
		// add length to first 4 bytes
	int	len = b.length;
	for (int i = 0; i < 4; i++)
		{
		out[i] = (byte) (len & 0x7f);
		len >>= 7;
		}
	
		// copy data bytes
	for (int i = 0; i < n; i++)
		out[i+4] = tmp[i];
	return out;
	}

/*-----------------------------------------*/
public	static	byte[]	uncompress(byte[] b)
/*-----------------------------------------*/
	{
		// extract the length from the first 4 bytes
	int	a0 = b[0];
	int	a1 = b[1];
	int	a2 = b[2];
	int	a3 = b[3];
	int	len = a0 | (a1 << 7) | (a2 << 14) | (a3 << 21);
	
	Inflater	u = new Inflater();
	u.setInput(b, 4, b.length-4);
	byte[]	out = new byte[len];
	try
		{
		u.inflate(out);
		}
	catch(DataFormatException x)
		{
		System.err.println("Inflate Error");
		System.exit(0);
		}
	u.end();
	return out;
	}
	}