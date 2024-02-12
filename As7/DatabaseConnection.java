import java.sql.*;

public class DatabaseConnection {
    private static Connection con;

    // static method
    public static Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                // Load the JDBC driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Set up the connection string
                String url = "jdbc:sqlserver://cypress.csil.sfu.ca;encrypt=true;trustServerCertificate=true;loginTimeout=90;";
                String username = "s_jwa423"; 
                String password = "22yhATNmjaMtb43T";

                // Establish the connection
                con = DriverManager.getConnection(url, username, password);

                // Print a test message
                System.out.println("Connected to the database!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            // log exception details
            System.out.println ( "\n\nFail to connect to CSIL SQL Server; exit now.\n\n" );
            e.printStackTrace(System.err);
        }
        return con;
    }

    public static void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
                // Print a test message
                System.out.println("Connection closed!");
            }
        } catch (SQLException e) {
             e.printStackTrace(System.err);
        }
    }
}