/**
 * This is the demonstration for DES for Cryptography and Secure Development course
 * Plaintext/Ciphertext Input: 64-bit data block
 * Key: 64-bit data --> will be trimmed into 56-bit key
 * Any questions: Nguyen.Truong@glasgow.ac.uk  
 */

public class Main {

    public static void main(String[] args)
    {
        Des cipher = new Des();
        String plaintext = "1234567890ABCDEF";
        String key = "AABB09182736CCDD";


        System.out.println("--[Encryption]--\n");
        String ciphertext = cipher.encrypt(plaintext, key);
        System.out.println("\nCipherText: " + ciphertext.toUpperCase() + "\n");


        System.out.println("--[Decryption]--\n");
        String decrypted_text = cipher.decrypt(ciphertext, key);
        System.out.println("\nPlainText: " + decrypted_text.toUpperCase());
    }
}
