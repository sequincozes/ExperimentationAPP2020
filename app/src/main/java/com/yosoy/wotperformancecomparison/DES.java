package com.yosoy.wotperformancecomparison;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class DES {

    private static Cipher ecipher;
    private static Cipher dcipher;

    private static SecretKey key;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void generateKey() {

        try {

            // generate secret key using DES algorithm
            key = KeyGenerator.getInstance("DES").generateKey();
            System.out.println("Generated DES key of "+key.getEncoded().length*8+" bits.");
            ecipher = Cipher.getInstance("DES");
            dcipher = Cipher.getInstance("DES");

            // initialize the ciphers with the given key

            ecipher.init(Cipher.ENCRYPT_MODE, key);

            dcipher.init(Cipher.DECRYPT_MODE, key);

//            String encrypted = encrypt("This is a classified message!");
//
//            String decrypted = decrypt(encrypted);
//
//            System.out.println("Decrypted: " + decrypted);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());
            return;
        } catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding:" + e.getMessage());
            return;
        } catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());
            return;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String str) {

        try {
//            byte[] utf8 = str.getBytes("UTF8");
//            byte[] enc = ecipher.doFinal(utf8);

            return Base64.getEncoder().encodeToString(ecipher.doFinal(str.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String str) {

        try {

            // decode with base64 to get bytes

//            byte[] dec = BASE64DecoderStream.decode(str.getBytes());
//            byte[] utf8 = dcipher.doFinal(dec);

// create new string based on the specified charset
            return new String(dcipher.doFinal(Base64.getDecoder().decode(str)));


        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }

}
