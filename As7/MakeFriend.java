import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MakeFriend {
    private Connection connection;

    public MakeFriend(Connection connection) {
        this.connection = connection;
    }

    public void makeFriend(String userYelpID) {
        Scanner scanner = new Scanner(System.in);

        // Continue asking for a friend's ID until a valid one is provided
        String friendID;
        do {
            // Ask the user to enter a friend's ID
            System.out.print("Enter the user ID of the friend you want to befriend: ");
            friendID = scanner.nextLine().trim();

            // Check if the entered friend ID exists in the copy_user_yelp table
            if (isValidUser(friendID)) {
                // Insert a new tuple into the copy_friendship table
                insertFriendship(userYelpID, friendID);
                break; // Exit the loop if the friend ID is valid
            } else {
                System.out.println("Invalid friend ID. Please try again.");
            }
        } while (true);
    }

    private boolean isValidUser(String userId) {
        try {
            String query = "SELECT user_id FROM copy_user_yelp WHERE user_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, userId);

            ResultSet resultSet = pstmt.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertFriendship(String userYelpID, String selectedUserID) {
        try {
            String query = "INSERT INTO copy_friendship (user_id, friend) VALUES (?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, userYelpID);
            pstmt.setString(2, selectedUserID);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Friendship added successfully!");
            } else {
                System.out.println("Failed to add friendship. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

