package com.yosoy.wotperformancecomparison;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    //private static SecretKeySpec secretKey;
    private static byte[] key;
    public static String ciphersuite = "AES/CFB/PKCS5Padding"; //cbc
    public static IvParameterSpec ivspec = null;
    public static SecretKeySpec secretKey = null;

    public static void generateKeyAES() {
        MessageDigest sha = null;
        try {
            String secretKeyStr = "boooooooooom!!!!";
            key = secretKeyStr.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void generateKeyAES128() {
        try {
            String secretKeyStr = "boooooooooom!!!!";
            String salt = "ssshhhhhhhhhhh!!!!";
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(secretKeyStr.toCharArray(), salt.getBytes(), 65536, 128);
            SecretKey tmp = factory.generateSecret(spec);
            secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    public static void generateKeyAES256() {
        try {
            String secretKeyStr = "boooooooooom!!!!";
            String salt = "ssshhhhhhhhhhh!!!!";
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKeyStr.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt256(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance(ciphersuite);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt256(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance(ciphersuite);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt128(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance(ciphersuite);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt128(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance(ciphersuite);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance(ciphersuite);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance(ciphersuite);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}