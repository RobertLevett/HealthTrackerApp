package app;

import app.index.IndexController;
import app.login.LoginController;
import app.register.RegisterController;
import app.user.UserProfileController;
import app.util.Filters;
import app.util.Path;
import app.util.ViewUtil;

import java.security.InvalidKeyException;
import java.sql.SQLException;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;


public class Application {

    public static void main(String[] args) throws SQLException, InvalidKeyException {

        // Configure Spark
        port(4567);
        staticFiles.location("/public");
        staticFiles.expireTime(600L);
        enableDebugScreen();

        // Set up before-filters (called before each get/post)
        before("*",                  Filters.addTrailingSlashes);
        before("*",                  Filters.handleLocaleChange);

        // Set up routes
        get(Path.Web.ACCOUNT,         UserProfileController.serveAccountPage);
        post(Path.Web.ACCOUNT,        UserProfileController.handleUpdatePost);

        get(Path.Web.INDEX,          IndexController.serveIndexPage);

        get(Path.Web.REGISTER,       RegisterController.serveRegisterPage);
        post(Path.Web.REGISTER,       RegisterController.handleRegisterPost);

        get(Path.Web.LOGIN,          LoginController.serveLoginPage);
        post(Path.Web.LOGIN,         LoginController.handleLoginPost);

        post(Path.Web.LOGOUT,        LoginController.handleLogoutPost);

        get(Path.Web.NUTRITION,     UserProfileController.serveNutritionPage);
        post(Path.Web.NUTRITION,     UserProfileController.handleNutritionPost);


        get("*",                     ViewUtil.notFound);

        //Set up after-filters (called after each get/post)
        after("*",                   Filters.addGzipHeader);


    }





}
