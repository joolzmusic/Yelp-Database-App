import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReviewBusiness {
    private Connection connection;
    private String loggedUserID;

    public ReviewBusiness(Connection connection, String loggedUserId) {
        this.connection = connection;
        this.loggedUserID = loggedUserId;
    }

    public void reviewBusiness() {
        Scanner scanner = new Scanner(System.in);

        // Step 1: User Input - Business ID
        String businessId = getValidBusinessId(scanner);

        // Step 2: User Input - Stars
        int stars = getValidStars(scanner);

        // Step 3: Record the Review
        if (recordReview(businessId, stars)) {
            System.out.println("Review recorded successfully!");
        } else {
            System.out.println("Failed to record the review. Please try again.");
        }
    }

    private String getValidBusinessId(Scanner scanner) {
        String businessId;
        while (true) {
            System.out.print("Enter the business ID you want to review: ");
            businessId = scanner.nextLine().trim();

            // Check if the business ID is valid
            if (isValidBusinessId(businessId)) {
                break;
            } else {
                System.out.println("Invalid business ID. Please try again.");
            }
        }
        return businessId;
    }

    private boolean isValidBusinessId(String businessId) {
        try {
            // Fetch all business IDs from the database
            String query = "SELECT business_id FROM copy_business";
            List<String> businessIds = new ArrayList<>();

            try (Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    businessIds.add(resultSet.getString("business_id"));
                }
            }
            // Check if the input business ID exists (case-sensitive comparison)
            return businessIds.contains(businessId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getValidStars(Scanner scanner) {
        int stars;
        while (true) {
            System.out.print("Enter the number of stars (between 1 and 5): ");
            try {
                stars = Integer.parseInt(scanner.nextLine().trim());

                // Check if stars is within the valid range
                if (stars == 1 || stars == 2 || stars == 3 || stars == 4 || stars == 5) {
                    break;
                } else {
                    System.out.println("Invalid stars. Please enter a whole number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for stars.");
            }
        }
        return stars;
    }

    private boolean recordReview(String businessId, int stars) {
        try {
            // Step 3: Record the Review
            String reviewId = generateReviewId();
            String insertQuery = "INSERT INTO copy_review (review_id, user_id, business_id, stars, useful, funny, cool, date) " +
                    "VALUES (?, ?, ?, ?, 0, 0, 0, CURRENT_TIMESTAMP)";

            try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                pstmt.setString(1, reviewId);
                pstmt.setString(2, loggedUserID);
                pstmt.setString(3, businessId);
                pstmt.setInt(4, stars);

                pstmt.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateReviewId() {
        // Combine the user's ID and the current date to create a unique review ID
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedDate = now.format(formatter);

        // Ensure that the user ID is no more than 8 characters
        String userIdPart = loggedUserID.substring(0, Math.min(8, loggedUserID.length()));

        // Concatenate the user ID and the formatted date to create a 22-character review ID
        String reviewId = userIdPart + formattedDate;

        return reviewId;
    }

}
