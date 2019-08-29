package app.user;

import app.database.DatabaseController;
import javax.crypto.Mac;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


public class UserController {

   public static DatabaseController userData = new DatabaseController();

    public static String encode(String password, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        String algorithm = "HmacSHA256";
        StringBuilder hashCode = new StringBuilder();
        Charset asciiCs = Charset.forName("US-ASCII");
        Mac MessageAuthenticationCode = Mac.getInstance(algorithm);
        MessageAuthenticationCode.init(new javax.crypto.spec.SecretKeySpec(asciiCs.encode(key).array(), algorithm));
        byte[] MessageAuthenticationCodeData = MessageAuthenticationCode.doFinal(asciiCs.encode(password).array());
        for (final byte element : MessageAuthenticationCodeData) {
            hashCode.append(Integer.toString((element & 0xff) + 0x100, 16).substring(1));
        }
        return hashCode.toString();
    }


    public static boolean loginAuthenticate(String username, String password) throws NoSuchAlgorithmException, InvalidKeyException, SQLException {
        if (!username.isEmpty() && !password.isEmpty()  && userData.checkUserPass(encode(password,username)) ) {
            return true;
        }

        return false;
    }

    public static boolean registerAuthenticate(String firstName, String lastName, String DOB, String email, String password) throws NoSuchAlgorithmException, InvalidKeyException, SQLException {
        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || DOB.isEmpty()) {
            return false;
        }
        if (!userData.checkUserEmail(email)) {
            return true;
        }

        return false;
    }
}
