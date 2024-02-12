import java.util.Scanner;
import java.sql.*;

public class BusinessSearch {

    private Connection connection;

    public BusinessSearch(Connection connection) {
        this.connection = connection;
    }

    public void searchBusinesses() {
        Scanner scanner = new Scanner(System.in);

        // Get minimum number of stars with validation
        double minStars = getValidStarsInput(scanner);

        // Get city with validation
        String city = getValidCityInput(scanner);

        // Get name with validation
        String name = getValidNameInput(scanner);

        int choice = getValidOrderingChoice(scanner);

        performSearchAndDisplayResults(minStars, city, name, choice);
    }

    private double getValidStarsInput(Scanner scanner) {
        while (true) {
            System.out.print("Enter minimum number of stars (between 1 and 5): ");
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

    private String getValidCityInput(Scanner scanner) {
        while (true) {
            System.out.print("Enter the city: ");
            String city = scanner.nextLine().trim();

            if (!city.isEmpty() && !city.matches(".*\\d.*")) {
                return city;
            } else {
                System.out.println("Invalid input. Please enter a non-empty city name without numbers.");
            }
        }
    }

    private String getValidNameInput(Scanner scanner) {
        while (true) {
            System.out.print("Enter the name: ");
            String name = scanner.nextLine().trim();

            if (!name.isEmpty() && !name.matches(".*\\d.*")) {
                return name;
            } else {
                System.out.println("Invalid input. Please enter a non-empty name without numbers.");
            }
        }
    }

    private int getValidOrderingChoice(Scanner scanner) {
        while (true) {
            System.out.println("Choose the ordering of the results:");
            System.out.println("1. By Name");
            System.out.println("2. By City");
            System.out.println("3. By Number of Stars");
            System.out.print("Your Choice: ");
    
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
    
                if (choice == 1 || choice == 2 || choice == 3) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    //SQL calculation to get user requests
    private void performSearchAndDisplayResults(double minStars, String city, String name, int orderingChoice) {
        try {
            // Construct the SQL query based on user input
            String query = "SELECT business_id, name, address, city, stars FROM copy_business " +
                           "WHERE stars >= ? AND LOWER(city) LIKE ? AND LOWER(name) LIKE ? " +
                           "ORDER BY ";
            
            // Append the ORDER BY clause based on the user's choice
            switch (orderingChoice) {
                case 1:
                    query += "name";
                    break;
                case 2:
                    query += "city";
                    break;
                case 3:
                    query += "stars";
                    break;
                default:
                    System.out.println("OrderingChoice error. Should not happen! ");
            }
    
            // Prepare and execute the SQL statement
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setDouble(1, minStars);
            pstmt.setString(2, "%" + city.toLowerCase() + "%");
            pstmt.setString(3, "%" + name.toLowerCase() + "%");
    
            ResultSet resultSet = pstmt.executeQuery();
    
            // Display the search results
            displaySearchResults(resultSet);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // for displaying the business requests
    private void displaySearchResults(ResultSet resultSet) throws SQLException {
        System.out.println("Search Results:");
    
        // if result set is empty, print this
        if (!resultSet.isBeforeFirst()) {
            System.out.println("No businesses found matching the criteria.");
            return;
        }
    
        System.out.printf("%-30s%-40s%-30s%-15s%-5s%n",
                "Business ID", "Name", "Address", "City", "Stars");
    
        while (resultSet.next()) {
            System.out.printf("%-30s%-40s%-30s%-15s%-5s%n",
                    resultSet.getString("business_id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getString("city"),
                    resultSet.getDouble("stars"));
        }
    }
    
}
