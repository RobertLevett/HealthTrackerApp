package app.user;

import app.database.DatabaseController;
import app.login.LoginController;
import app.util.Path;
import app.util.ViewUtil;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static app.util.RequestUtil.*;

public class UserProfileController {

     static DatabaseController controller = new DatabaseController();

    public static Route handleUpdatePost = (Request request, Response response) -> {
        String user = LoginController.user;

        String parameterWeight = getQueryWeight(request);
        String parameterHeight = getQueryHeight(request);
        String parameterDistance = getQueryDistance(request);
        String parameterPedometer = getQueryPedometer(request);
        String deleteAccount = getQueryDeleteAccount(request);

        if(deleteAccount == null) {

            if (parameterWeight != null && parameterWeight.matches("[0-9]+")) {
                double value = Double.parseDouble(getQueryWeight(request));
                //more than 0kg less than 500kg
                if (value > 0 && value <= 500) {
                    controller.setUserWeight(value, user);
                }
            } else if (parameterHeight != null && parameterHeight.matches("[0-9]+")) {
                int value = Integer.parseInt(getQueryHeight(request));
                //min 50 cm max 250
                if (value >= 50 && value <= 250) {
                    controller.setUserHeight(value, user);
                }
            } else if (parameterDistance != null && parameterDistance.matches("[0-9]+")) {
                int value = Integer.parseInt(getQueryDistance(request));
                controller.setUserDistance(value, user);
            } else if (parameterPedometer != null && parameterPedometer.matches("[0-9]+")) {
                int value = Integer.parseInt(getQueryPedometer(request));
                controller.setUserPedometer(value, user);
            }
        }else if (deleteAccount.equals(user)){
            request.session().removeAttribute("currentUser");
                controller.deleteAccount(user);
            Map<String, Object> model = new HashMap<>();

            model.put("authenticationSucceeded", true);
            return ViewUtil.render(request,model,Path.Template.INDEX);
        }

        Map<String, Object> model = new HashMap<>();
        model.put("authenticationSucceeded", true);
        return ViewUtil.renderAccount(request,model,Path.Template.ACCOUNT);
    };

    public static Route serveAccountPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        LoginController.ensureUserIsLoggedIn(request,response);
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("registerRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.renderAccount(request, model, Path.Template.ACCOUNT);
    };

    public static Route serveNutritionPage = (Request request, Response response) -> {
        //ModelAndView model = new ModelAndView()
        Map<String, Object> model = new HashMap<>();
        LoginController.ensureUserIsLoggedIn(request,response);
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("registerRedirect", removeSessionAttrLoginRedirect(request));
        return ViewUtil.renderNutrition(request, model, Path.Template.NUTRITION);
    };



    public static Route handleNutritionPost = (Request request, Response response) -> {
        String user = LoginController.user;
        Map<String, Object> model = new HashMap<>();
        String food = getQueryFood(request);
        ArrayList<String> foodInfo = new ArrayList<String>();

        foodInfo = controller.getFoodInformation(food, user);

        if(foodInfo.size() == 0){
            return ViewUtil.renderNutrition(request, model, Path.Template.NUTRITION);
        }

        model.put("currentUser", getSessionCurrentUser(request));
        model.put("foodName",foodInfo.get(0));
        model.put("protein",foodInfo.get(1));
        model.put("fat",foodInfo.get(2));
        model.put("carbs",foodInfo.get(3));
        model.put("calories",foodInfo.get(4));
        model.put("energy",foodInfo.get(5));

        return ViewUtil.renderNutrition(request, model, Path.Template.NUTRITION);
        };

    }