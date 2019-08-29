package app.util;

import app.database.DatabaseController;
import spark.Request;

public class RequestUtil {


    static DatabaseController controller = new DatabaseController();

    public static String getQueryLocale(Request request) {
        return request.queryParams("locale");
    }

    public static String getQueryFirstName(Request request){
        return request.queryParams("firstName");
    }
    public static String getQueryLastName(Request request){ return request.queryParams("lastName"); }
    public static String getQueryDOB(Request request){
        return request.queryParams("DOB");
    }
    public static String getQueryEmail(Request request) { return request.queryParams("email"); }
    public static String getQueryPassword(Request request) { return request.queryParams("password"); }
    public static String getQueryLoginRedirect(Request request) {
        return request.queryParams("loginRedirect");
    }
    public static String getQueryWeight(Request request){return request.queryParams("weight");}
    public static String getQueryHeight(Request request){return request.queryParams("height");}
    public static String getQueryDistance(Request request){return request.queryParams("distance");}
    public static String getQueryPedometer(Request request){return request.queryParams("pedometer");}
    public static String getSessionLocale(Request request) {
        return request.session().attribute("locale");
    }
    public static String getSessionCurrentUser(Request request) {
        return request.session().attribute("currentUser");
    }
    public static String getQueryFood(Request request) { return request.queryParams("myFood"); }
    public static String getQueryDeleteAccount(Request request){return request.queryParams("deleteAccount");}


    public static boolean removeSessionAttrLoggedOut(Request request) {
        Object loggedOut = request.session().attribute("loggedOut");
        request.session().removeAttribute("loggedOut");
        return loggedOut != null;
    }

    public static String removeSessionAttrLoginRedirect(Request request) {
        String loginRedirect = request.session().attribute("loginRedirect");
        request.session().removeAttribute("loginRedirect");
        return loginRedirect;
    }

    public static boolean clientAcceptsHtml(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("text/html");
    }

    public static boolean clientAcceptsJson(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("application/json");
    }
}