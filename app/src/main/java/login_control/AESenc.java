package login_control;

import android.util.Base64;
import android.widget.TextView;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Tiziano on 20/11/2014.
 */
public class AESenc {




    private static byte[] key = {
            0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79
    };//"thisIsASecretKey";

    public static String encrypt(String login, String senha, TextView textView)
    {
        try
        {
            LoginFunctions f = new LoginFunctions();
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            Cipher cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeToString(cipher.doFinal(login.getBytes()),Base64.DEFAULT);//login
            System.out.println(encryptedString);
            final SecretKeySpec secretKey2 = new SecretKeySpec(key, "AES");
            cipher2.init(Cipher.ENCRYPT_MODE, secretKey2);
            final String encryptedString2 = Base64.encodeToString(cipher.doFinal(senha.getBytes()),Base64.DEFAULT);//senha
            System.out.println(encryptedString2);

            //f.formatPost(encryptedString,encryptedString2, textView);
            return encryptedString;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;

    }

    public static String decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decode(strToDecrypt, Base64.DEFAULT)));
            return decryptedString;
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        return null;
    }

}
