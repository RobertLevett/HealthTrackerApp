package app.login;

import app.database.DatabaseController;
import app.register.RegisterController;
import app.user.*;
import app.util.*;
import spark.*;
import java.util.*;
import static app.util.RequestUtil.*;

public class LoginController {
   public static String user;

    public static DatabaseController controller = new DatabaseController();

    public static Route serveLoginPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.render(request, model, Path.Template.LOGIN);
    };

    public static Route handleLoginPost = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        user = getQueryEmail(request);
        if (!UserController.loginAuthenticate(getQueryEmail(request), getQueryPassword(request))) {
            model.put("authenticationFailed", true);
            return ViewUtil.render(request, model, Path.Template.LOGIN);
        }

        String tempPass = RegisterController.encode(getQueryPassword(request),getQueryEmail(request));
        if(controller.logInCheck(getQueryEmail(request), tempPass)){
            model.put("authenticationSucceeded", true);
            request.session().attribute("currentUser", getQueryEmail(request));

        }
        return ViewUtil.renderAccount(request,model,Path.Template.INDEX);
    };


    public static Route handleLogoutPost = (Request request, Response response) -> {
        request.session().removeAttribute("currentUser");
        request.session().attribute("loggedOut", true);
        response.redirect(Path.Web.LOGIN);
        return null;
    };

    public static void ensureUserIsLoggedIn(Request request, Response response) {
        if (request.session().attribute("currentUser") == null) {
            request.session().attribute("loginRedirect", request.pathInfo());
            response.redirect(Path.Web.LOGIN);
        }
    };

}
