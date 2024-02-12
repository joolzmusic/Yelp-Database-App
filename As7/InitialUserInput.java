import java.util.Scanner;

public class InitialUserInput {

    public static int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        int userChoice;

        do {
            System.out.println("Choose: 1 for search business, 2 for search users, 3 to add friend, 4 to make a business review, or 5 to logout");
            System.out.print("Your choice: ");

            String userInput = scanner.nextLine().trim();

            if (isValidInput(userInput)) {
                userChoice = Integer.parseInt(userInput);
                break;
            } else {
                System.out.println("Invalid input. Please enter 1, 2, 3, 4, or 5.");
            }
        } while (true);

        return userChoice;
    }

    private static boolean isValidInput(String userInput) {
        if (userInput.isEmpty() || userInput.contains(" ")) {
            return false; // Empty or contains spaces
        }

        try {
            int choice = Integer.parseInt(userInput);
            return choice == 1 || choice == 2 || choice == 3 || choice == 4 || choice == 5;
        } catch (NumberFormatException e) {
            return false; // Not a valid integer
        }
    }
}
