package app.util;

import lombok.*;

public class Path {

    // The @Getter methods are needed in order to access
    // the variables from Velocity Templates
    public static class Web {
        @Getter public static final String INDEX = "/index/";
        @Getter public static final String LOGIN = "/login/";
        @Getter public static final String LOGOUT = "/logout/";
        @Getter public static final String REGISTER = "/register/";
        @Getter public static final String ACCOUNT = "/userProfile/";
        @Getter public static final String NUTRITION = "/nutritionProfile/";

    }

    public static class Template {
        public final static String INDEX = "/velocity/index/index.vm";
        public final static String LOGIN = "/velocity/login/login.vm";
        public static final String NOT_FOUND = "/velocity/notFound.vm";
        public final static String REGISTER = "/velocity/register/register.vm";
        public final static String ACCOUNT = "/velocity/userProfile/userProfile.vm";
        public final static String NUTRITION = "/velocity/nutritionProfile/nutritionProfile.vm";


    }

}
