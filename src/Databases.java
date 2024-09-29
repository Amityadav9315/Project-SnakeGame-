//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.Statement;
//
//public class Databases {
//    public static void main(String args[]) throws Exception
//    {
//        Class.forName("org.sqlite.JDBC");
//
//        Connection con= DriverManager.getConnection("jdbc:sqlite:C://sqlite//");
//
//        Statement stm=con.createStatement();
//
//        ResultSet rs=stm.executeQuery("Select*from dept");
//
//        int deptNo;
//        String dname;
//
//        while(rs.next())
//        {
//            deptNo=rs.getInt("deptNo");
//            dname=rs.getString("dname");
//
//            System.out.println(deptNo+" "+dname);
//        }
//    }
//}
///////////////////////////////////////
import javax.swing.*;
import java.sql.*;

public class Databases {

    static String JDBC_URL;
    static String DB_USER;
    static String DB_PASSWORD;

    // Prompt user for database connection details
    static {
        JDBC_URL = JOptionPane.showInputDialog(null, "Enter JDBC URL:");
        DB_USER = JOptionPane.showInputDialog(null, "Enter Database User:");
        DB_PASSWORD = JOptionPane.showInputDialog(null, "Enter Database Password:");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    public static int loadHighScore(String playerName) {
        int highScore = 0;
        try (Connection connection = getConnection()) {
            String query = "SELECT Score FROM HighScores WHERE PlayerName = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, playerName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        highScore = resultSet.getInt("Score");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highScore;
    }

    public static void saveHighScore(String playerName, int score) {
        try (Connection connection = getConnection()) {
            String query = "INSERT INTO HighScores (PlayerName, Score) VALUES (?, ?) ON DUPLICATE KEY UPDATE Score = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, playerName);
                preparedStatement.setInt(2, score);
                preparedStatement.setInt(3, score);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

