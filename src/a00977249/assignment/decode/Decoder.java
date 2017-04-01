/**
 * Project: COMP3613_A00977249Assignment02
 * File: Database.java
 * Date: Oct 14, 2016
 * Time: 12:49:39 PM
 */
package a00977249.assignment.decode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * @author Siamak Pourian
 *
 * Decodes an encrypted file
 * 
 * Decipher Class
 */
public class Decoder {
	
	// salt for password-based encryption-decryption algorithm
    private static final byte[] salt = { (byte) 0xf5, (byte) 0x33, (byte) 0x01, (byte) 0x2a, 
			                             (byte) 0xb2, (byte) 0xcc, (byte) 0xe4, (byte) 0x7f };
	// iteration count
	private  static int iterationCount = 100;

	/**
	 * Decodes any file given a valid password and filename
	 * 
	 * @param fileName name of the file in the source path
	 * @param password key to decipher the file contents
	 * @return Byte Array containing the decoded file contents
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static String getDecipherCode(String filePath, String password) throws Exception {
		
		// create secret key
		Cipher cipher = null;

		try {
			// create password based encryption key object
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());

			// obtain instance for secret key factory
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

			// generate secret key for encryption
			SecretKey secretKey = keyFactory.generateSecret(keySpec);

			// specifies parameters used with password based encryption
			PBEParameterSpec parameterSpec = new PBEParameterSpec(salt, iterationCount);

			// obtain cipher instance reference.
			cipher = Cipher.getInstance("PBEWithMD5AndDES");

			// initialize cipher in decrypt mode
			cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
		}

		// handle NoSuchAlgorithmException
		catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();
			System.exit(1);
		}

		// handle InvalidKeySpecException
		catch (InvalidKeySpecException exception) {
			exception.printStackTrace();
			System.exit(1);
		}

		// handle InvalidKeyException
		catch (InvalidKeyException exception) {
			exception.printStackTrace();
			System.exit(1);
		}

		// handle NoSuchPaddingException
		catch (NoSuchPaddingException exception) {
			exception.printStackTrace();
			System.exit(1);
		}

		// handle InvalidAlgorithmParameterException
		catch (InvalidAlgorithmParameterException exception) {
			exception.printStackTrace();
			System.exit(1);
		}

		// read and decrypt contents from file
		FileInputStream fileInputStream = null;
		try {
			 File file = new File(filePath);
			 fileInputStream = new FileInputStream(file);
		     CipherInputStream in = new CipherInputStream(fileInputStream, cipher);
		
			 ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			 
			 int contents;
			 while ((contents = in.read()) != -1) {
			     buffer.write((byte) (contents));
			 }
			 in.close();
		     return new String(buffer.toByteArray());
		} catch (Exception exception) {
			throw new Exception("File not found.");
		} finally {
	       if(fileInputStream != null) {
	    	   fileInputStream.close();  
	       }
		}
	}
}
