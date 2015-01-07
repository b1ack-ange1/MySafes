package ru.intelinvest.mysafes.Additional;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Stanislav on 07.01.2015.
 */
public class BFEncrypter {
    private final static String ALGORITM = "Blowfish";
    private String password;

    public BFEncrypter() {
    }

    ;

    public byte[] encrypt(String plainText) throws GeneralSecurityException, InvalidKeyException {

        SecretKey secret_key = new SecretKeySpec(password.getBytes(), ALGORITM);

        Cipher cipher = Cipher.getInstance(ALGORITM);
        cipher.init(Cipher.ENCRYPT_MODE, secret_key);
        return cipher.doFinal(plainText.getBytes());
    }

    public String decrypt(byte[] encryptedText) throws GeneralSecurityException {

        SecretKey secret_key = new SecretKeySpec(password.getBytes(), ALGORITM);

        Cipher cipher = Cipher.getInstance(ALGORITM);
        cipher.init(Cipher.DECRYPT_MODE, secret_key);

        byte[] decrypted = cipher.doFinal(encryptedText);

        return new String(decrypted);
    }

    public static String bytesToHex(byte[] data) {

        if (data == null)
            return null;

        String str = "";

        for (int i = 0; i < data.length; i++) {
            if ((data[i] & 0xFF) < 16)
                str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
            else
                str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
        }
        return str;
    }

    public void setPassword(String newPassword){
        this.password=newPassword;
    }
}
