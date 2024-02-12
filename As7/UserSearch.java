import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserSearch {
    private Connection connection;
    private List<String> searchResultsUserIds;

    public UserSearch(Connection connection) {
        this.connection = connection;
        this.searchResultsUserIds = new ArrayList<>();
    }

    public void searchUser() {
        Scanner scanner = new Scanner(System.in);

        // Get user name
        String chosenName = getValidNameInput(scanner);

        // Get min review count
        int minReviewCount = getValidMinRevCount(scanner);

        // Get min average stars
        double minAvgStars = getValidMinAvgStars(scanner);

        performSearchAndDisplayResults(chosenName, minReviewCount, minAvgStars);
    }

     private String getValidNameInput(Scanner scanner) {
        while (true) {
            System.out.print("Enter a User Yelp name: ");
            String name = scanner.nextLine().trim();

            if (!name.isEmpty() && !name.matches(".*\\d.*")) {
                return name;
            } else {
                System.out.println("Invalid input. Please enter a non-empty city name without numbers.");
            }
        }
    }

    private int getValidMinRevCount(Scanner scanner) {
        while (true) {
            System.out.print("Enter a non-negative integer review count: ");
            try {
                int rc = Integer.parseInt(scanner.nextLine().trim());

                if (rc >= 0) {
                    return rc;
                } else {
                    System.out.println("Invalid input. Please enter a value equal or greater than zero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid non-negative integer number.");
            }
        }
    }

    private double getValidMinAvgStars(Scanner scanner) {
        while (true) {
            System.out.print("Enter minimum number of average stars (between 1 and 5): ");
            try {
                double stars = Double.parseDouble(scanner.nextLine().trim());

                if (stars >= 1 && stars <= 5) {
                    return stars;
                } else {
                    System.out.println("Invalid input. Please enter a value between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void performSearchAndDisplayResults(String userName, int revCount, double avgStars) {
        try {
            // Construct the SQL query baseNSzEvk_wH485zHnlPCoAOwd on user input
            String query = "SELECT user_id, name, review_count, useful, funny, cool, average_stars, yelping_since FROM copy_user_yelp " +
                           "WHERE LOWER(name) LIKE ? AND review_count >= ? AND average_stars >= ? ORDER BY name";
    
            // Prepare and execute the SQL statement
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, "%" + userName.toLowerCase() + "%");
            pstmt.setInt(2,revCount);
            pstmt.setDouble(3, avgStars);
    
            ResultSet resultSet = pstmt.executeQuery();

            // Display the search results
            storeAndDisplaySearchResults(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // for displaying the business requests
    private void storeAndDisplaySearchResults(ResultSet resultSet) throws SQLException {

        System.out.println("Search Results:");
    
        //clear the existing search results
        searchResultsUserIds.clear();

        // if result set is empty, print this
        if (!resultSet.isBeforeFirst()) {
            System.out.println("No users found matching the criteria.");
            return;
        }

        System.out.printf("%-30s%-20s%-18s%-10s%-10s%-10s%-15s%-15s%n",
                "User ID", "Name", "Review Count", "Useful", "Funny", "Cool", "Avg Stars", "Date Registered");
    
        while (resultSet.next()) {
            // store user id's in the string array
            searchResultsUserIds.add(resultSet.getString("user_id"));

            System.out.printf("%-30s%-20s%-18s%-10s%-10s%-10s%-15s%-15s%n",
                    resultSet.getString("user_id"),
                    resultSet.getString("name"),
                    resultSet.getString("review_count"),
                    resultSet.getString("useful"),
                    resultSet.getDouble("funny"),
                    resultSet.getString("cool"),
                    resultSet.getString("average_stars"),
                    resultSet.getString("yelping_since"));
        }
    }

    // Expose a method to retrieve the search results
    public List<String> getSearchResultsUserIds() {
        return new ArrayList<>(searchResultsUserIds);
    }

    // for testing purposes
    public void displaySearchResults(List<String> result) {
        System.out.println("Search Results:");

        // if result set is empty, print this
        if (result.isEmpty()) {
            System.out.println("No users found matching the criteria.");
            return;
        }

        // Print the stored user IDs
        System.out.println("User IDs:");
        for (String userId : result) {
            System.out.println(userId);
        }
    }

}
