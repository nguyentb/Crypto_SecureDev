/**
 * This is the demonstration for DES for Cryptography and Secure Development course
 * Plaintext/Ciphertext Input: 64-bit data block
 * Key: 64-bit data --> will be trimmed into 56-bit key
 * Any questions: Nguyen.Truong@glasgow.ac.uk  
 */

public class Main {

    public static void main(String[] args)
    {
        DES cipher = new DES();
        String plaintext = "1234567890ABCDEF";
        String key = "AABB09182736CCDD";

        System.out.println("--[Encryption Process]--\n");
        String ciphertext = cipher.encrypt(plaintext, key);
        System.out.println("\nCiphertext: " + ciphertext.toUpperCase() + "\n");

        System.out.println("--[Decryption Process]--\n");
        String decrypted_text = cipher.decrypt(ciphertext, key);
        System.out.println("\nDecrypted text: " + decrypted_text.toUpperCase());

        System.out.println("\n--[Check Decryption Result]--\n");
        System.out.println(plaintext.toUpperCase().equals(decrypted_text.toUpperCase()));
    }
}
