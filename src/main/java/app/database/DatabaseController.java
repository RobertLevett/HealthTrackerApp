package app.database;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseController {       //Consider static for controller

    Connection connection = null;

    public boolean dbConnection() throws SQLException {
        String x = String.format("jdbc:sqlserver://seq7uea-db.database.windows.net:1433;database=seq7uea;user=seq7uea@seq7uea-db;password=Password1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30");
        connection = DriverManager.getConnection(x);
        String schema = connection.getSchema();
        if (schema != null) {
            return true;
        }
        connection.close();
        return false;
    }

    public boolean checkUserEmail(String email) throws SQLException {
        if (dbConnection()) {
            Statement stm = null;
            stm = connection.createStatement();
            String selectSql = "SELECT email FROM \"USER\" WHERE email = \'" + email + "\';";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {
                if (resultSet.next()) {
                    String no = resultSet.getString(1);
                    connection.close();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        connection.close();
        return false;
    }

    public boolean checkUserPass(String password) throws SQLException {
        if (dbConnection()) {
            Statement stm = null;
            stm = connection.createStatement();
            String selectSql = "SELECT password FROM \"USER\" WHERE password = \'" + password + "\';";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {
                if (resultSet.next()) {
                    connection.close();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        connection.close();
        return false;
    }

    public boolean addUser(String firstName, String lastName, String DOB, String email, String password) throws SQLException {
        if (dbConnection()) {
            try {
                Statement stm = null;
                stm = connection.createStatement();
                String addUser = "INSERT INTO \"USER\" VALUES ('" + firstName + "','" + lastName + "','" + email + "','" + DOB + "','" + password + "');";
                stm.executeUpdate(addUser);
                try (Statement statement = connection.createStatement()) {
                    connection.close();
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        connection.close();
        return false;
    }

    public boolean logInCheck(String email, String password) throws SQLException, InvalidKeyException, NoSuchAlgorithmException {
        if (checkUserEmail(email)) {
            if(checkUserPass(password)) {
                return true;
            }
        }
        return false;
        }

//    public String getUserInfo() throws SQLException {
//        String info = "";
//        if (dbConnection()) {
//            String selectSql = "SELECT * FROM \"ALL_INFO_VIEW\"";
//            try (Statement statement = connection.createStatement();
//                 ResultSet resultSet = statement.executeQuery(selectSql)) {
//                System.out.println("USER INFO: ");
//                while (resultSet.next()) {
//                    String email = resultSet.getString(1);
//                    String firstName = resultSet.getString(2);
//                    String surname = resultSet.getString(3);
//                    String DOB = resultSet.getString(4);
//                    String weight = resultSet.getString(5);
//                    String height = resultSet.getString(6);
//                    String pedometer = resultSet.getString(7);
//                    String distance = resultSet.getString(8);
//                    String image = resultSet.getString(9);
//
//                    System.out.println("Email: " + email + "\nfirstName: " + firstName + "\nSurname: " + surname + "\nDOB: "
//                            + DOB + "\nWeight: " + weight + "\nHeight: " + height + "\nPedometer: " + pedometer + "\nDistance: "
//                            + distance + "\n Image: " + image + "\n\n");
//                }
//                connection.close();
//            }
//        }
//        return info;
//    }

    public void setUserWeight(double weight1, String user) throws SQLException {
            if (dbConnection()) {
                String selectSql = "UPDATE USERINFO SET Weight = "+weight1+" WHERE email =\'"+user+"\'";
                try (Statement stm = connection.createStatement()){
                     stm.executeUpdate(selectSql);
                    }
                    connection.close();
                }
            }
    public void setUserHeight(int height, String user) throws SQLException {
        if (dbConnection()) {
            String selectSql = "UPDATE USERINFO SET Height = "+height+" WHERE email =\'"+user+"\'";
            try (Statement stm = connection.createStatement()){
                stm.executeUpdate(selectSql);
            }
            connection.close();
        }
    }

    public void setUserDistance(int distance, String user) throws SQLException {
        if (dbConnection()) {
            String getTotalDistance = "SELECT TotalDistance FROM USERINFO WHERE email = \'"+user+"\'";
            int totalDist = 0;
            String totalDistance = "";
            String selectSql = "UPDATE USERINFO SET Distance = "+distance+" WHERE email =\'"+user+"\'";
            try (Statement stm = connection.createStatement()){
                ResultSet resultSet = stm.executeQuery(getTotalDistance);
                while(resultSet.next()) {
                    totalDistance = resultSet.getString(1);

                    totalDist = Integer.parseInt(totalDistance);
                }
                totalDist += distance;
                String setTotDist = "UPDATE USERINFO SET TotalDistance = "+totalDist+" WHERE email =\'"+user+"\'";
                stm.executeUpdate(setTotDist);
                stm.executeUpdate(selectSql);

            }
            connection.close();
        }
    }

    public void setUserPedometer(int pedometer, String user) throws SQLException {
        if (dbConnection()) {
            String selectSql = "UPDATE USERINFO SET Pedometer = "+pedometer+" WHERE email =\'"+user+"\'";
            try (Statement stm = connection.createStatement()){
                stm.executeUpdate(selectSql);
            }
            connection.close();
        }
    }


    public ArrayList getOneUserInfo(String email) throws SQLException {
        ArrayList<String> userInfo  = new ArrayList<String>();
        if (dbConnection()) {
            String selectSql = "SELECT * FROM \"ALL_INFO_VIEW\" WHERE email=\'"+email+"\'";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {
                while (resultSet.next()) {

                    for (int i = 1;i<=11;i++){
                        userInfo.add(resultSet.getString(i));
                    }
                    int d = Integer.parseInt(userInfo.get(10));
                    d*=1000;
                    String distInMe = Integer.toString(d);
                    userInfo.add(distInMe);
                }
            }
            String selectSql2 = "SELECT * FROM \"ALL_INFO_VIEW\" WHERE email=\'"+email+"\'";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet2 = statement.executeQuery(selectSql2)) {
                while (resultSet2.next()) {
                        userInfo.add(resultSet2.getString(1));
                }
            }
        }
        connection.close();

        return userInfo;
    }



    public ArrayList getFoodItem() throws SQLException {
        ArrayList<String> allFood  = new ArrayList<String>();
        if (dbConnection()) {
            String selectSql = "SELECT * FROM \"FOOD_AND_DRINK\"";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    for (int i = 1;i<=6;i++){
                        allFood.add(resultSet.getString(i));
                    }
                }
                connection.close();
            }
        }

        return allFood;
    }
    public ArrayList getDrinkItem() throws SQLException {
        ArrayList<String> allDrink  = new ArrayList<String>();
        if (dbConnection()) {
            String selectSql = "SELECT * FROM \"DRINK\"";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    for (int i = 1;i<=8;i++){
                        allDrink.add(resultSet.getString(i));
                    }
                }
                connection.close();
            }
        }

        return allDrink;
    }

    public String getTotalCalories(String user) throws SQLException {
        String totCals = "";
        if(dbConnection()){
            String selectSql3 = "SELECT totalCalories FROM \"ALL_INFO_VIEW\" WHERE email = \'" + user + "\'";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql3)) {
                while (resultSet.next()) {
                        totCals = (resultSet.getString(1));
                }
        }
    }
    return totCals;
    }

    private void setTotalCalories(int totalCals, String user){

        String selectSql2 = "UPDATE USERINFO SET totalCalories = " + totalCals + " WHERE email =\'" + user + "\'";
        try (Statement stm = connection.createStatement()) {
            stm.executeUpdate(selectSql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList getFoodInformation(String food, String user) throws SQLException {
        ArrayList<String> allFood = new ArrayList<String>();
        int totCals = 0;

        if (dbConnection()) {
            String selectSql = "SELECT * FROM \"FOOD_AND_DRINK\" WHERE Name = \'" + food + "\'";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    for (int i = 1; i <= 6; i++) {
                        allFood.add(resultSet.getString(i));
                    }
                }
                if(allFood.size() == 0){
                    return allFood;
                }
                totCals = Integer.parseInt(getTotalCalories(user));

                totCals+=Integer.parseInt(allFood.get(4));

                setTotalCalories(totCals, user);

                    connection.close();
                }
            }
        return allFood;

        }

    public ArrayList getDrinkInformation(String drink, String user) throws SQLException {
        ArrayList<String> allFood = new ArrayList<String>();
        int totCals = 0;

        if (dbConnection()) {

            String selectSql = "SELECT * FROM \"DRINK\" WHERE DrinkName = \'" + drink + "\'";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSql)) {
                while (resultSet.next()) {
                    for (int i = 1; i <= 8; i++) {
                        allFood.add(resultSet.getString(i));
                    }
                }
                totCals = Integer.parseInt(getTotalCalories(user));
                totCals+=Integer.parseInt(allFood.get(4));
                setTotalCalories(totCals, user);
                connection.close();
            }
        }
        return allFood;

    }

    public void deleteAccount(String user) throws SQLException {
        if(dbConnection()){
            String selectSql = "DELETE FROM \"USER\" WHERE email =\'"+user+"\'";
            PreparedStatement st = connection.prepareStatement(selectSql);
            try (Statement stm = connection.createStatement()) {
                stm.executeUpdate(selectSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

}
}