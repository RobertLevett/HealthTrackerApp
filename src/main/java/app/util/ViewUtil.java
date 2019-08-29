package app.util;

import app.login.LoginController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jetty.http.HttpStatus;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static app.util.RequestUtil.*;

public class ViewUtil {
    // Renders a template given a model and a request

    public static String render(Request request, Map<String, Object> model, String templatePath) throws SQLException {
        model.put("msg", new MessageBundle(getSessionLocale(request)));
        model.put("currentUser", getSessionCurrentUser(request));
        model.put("WebPath", Path.Web.class); // Access application URLs from templates
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }

    public static String renderAccount(Request request, Map<String, Object> model, String templatePath) throws SQLException {
        ArrayList<String> userInfo = new ArrayList<String>();
        String name = LoginController.user;
        userInfo = controller.getOneUserInfo(name);
        model.put("msg", new MessageBundle(getSessionLocale(request)));
        model.put("currentUser", getSessionCurrentUser(request));
        model.put("firstName",userInfo.get(1));
        model.put("lastName",userInfo.get(2));
        model.put("DOB",userInfo.get(3));
        model.put("weight",userInfo.get(4));
        model.put("height",userInfo.get(5));
        model.put("pedometer",userInfo.get(6));
        model.put("distance",userInfo.get(7));
        model.put("totalDistance",userInfo.get(9));
        model.put("totalDistanceInM",userInfo.get(10));
        model.put("totalCalories",userInfo.get(10));
        model.put("WebPath", Path.Web.class); // Access application URLs from templates
        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }


    public static Route notFound = (Request request, Response response) -> {
        response.status(HttpStatus.NOT_FOUND_404);
        return render(request, new HashMap<>(), Path.Template.NOT_FOUND);
    };

    private static VelocityTemplateEngine strictVelocityEngine() {
        VelocityEngine configuredEngine = new VelocityEngine();
        configuredEngine.setProperty("runtime.references.strict", true);
        configuredEngine.setProperty("resource.loader", "class");
        configuredEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        return new VelocityTemplateEngine(configuredEngine);
    }
    public static String renderNutrition(Request request, Map<String, Object> model, String templatePath) throws SQLException {
        String name = LoginController.user;
        ArrayList<String> allFood = new ArrayList<String>();
        ArrayList<String> allDrink = new ArrayList<String>();
        //ArrayList<String> curFood = new ArrayList<String>();

        allFood = controller.getFoodItem();

        model.put("msg", new MessageBundle(getSessionLocale(request)));
        model.put("currentUser", getSessionCurrentUser(request));
        model.put("foodName",allFood.get(0));
        model.put("protein",allFood.get(1));
        model.put("fat",allFood.get(2));
        model.put("carbs",allFood.get(3));
        model.put("calories",allFood.get(4));
        model.put("energy",allFood.get(5));

        List<String> foodItems = new ArrayList<String>();
        List<String> drinkItems = new ArrayList<String>();

        for (int i = 0; i < allFood.size(); i+=6) {
            foodItems.add(allFood.get(i));
        }

        ObjectMapper mapper = new ObjectMapper();

        String json = "";
        try{
            json = mapper.writeValueAsString(foodItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < allDrink.size(); i+=8) {
            drinkItems.add(allDrink.get(i));
        }

        ObjectMapper mapper1 = new ObjectMapper();

        String json1 = "";
        try{
            json1 = mapper1.writeValueAsString(foodItems);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }




        model.put("list", json);
        model.put("drink", json1);

        model.put("WebPath", Path.Web.class); // Access application URLs from templates



        return strictVelocityEngine().render(new ModelAndView(model, templatePath));
    }
}