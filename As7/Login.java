import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Login {
    private Connection connection;
    private String userID;

    // Constructor to receive the database connection
    public Login(Connection connection) {
        this.connection = connection;
        this.userID = null;

    }

    // Function to perform user login
    public boolean userIsLoggedIn() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter your user_id: ");
            String userId = scanner.nextLine().trim();

            // Validate user_id using SQL query
            if (isValidUser(userId)) {
                System.out.println("Login successful!");
                userID = userId;
                return true;
            } else {
                System.out.println("Invalid user_id. Please try again.");
            }
        }
    }

    // Function to check if the user_id is valid
    private boolean isValidUser(String userId) {
        try {
            // case-sensitive calculation
            String query = "SELECT user_id FROM copy_user_yelp WHERE user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, userId);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String temp = resultSet.getString("user_id");
                if (userId.equals(temp)) {
                    return true;
                }
            }

            // if no matching case sensitive user_id 
            return false;

        // this part should never happen! 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getUserID() {
        return userID;
    }
}
