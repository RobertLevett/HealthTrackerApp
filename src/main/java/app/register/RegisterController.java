
package app.register;

import app.database.DatabaseController;
import app.user.UserController;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.crypto.Mac;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static app.util.RequestUtil.*;
import static app.util.RequestUtil.getQueryEmail;

public class RegisterController {

    public static DatabaseController controller = new DatabaseController();

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
    public static Route serveRegisterPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("registerRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.render(request, model, Path.Template.REGISTER);
    };

    public static Route handleRegisterPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        if (!UserController.registerAuthenticate(getQueryFirstName(request),getQueryLastName(request), getQueryDOB(request),getQueryEmail(request), getQueryPassword(request))) {
            model.put("registrationFailed", true);

            return ViewUtil.render(request, model, Path.Template.REGISTER);
        }

        model.put("registrationSucceeded", true);
        controller.addUser(getQueryFirstName(request),getQueryLastName(request), getQueryDOB(request),getQueryEmail(request), RegisterController.encode(getQueryPassword(request),getQueryEmail(request)));
        return ViewUtil.render(request, model, Path.Template.INDEX);
    };
}