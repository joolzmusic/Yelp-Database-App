// /*

// For testing SQL Server connection in CSIL through JDBC connection.
 
//  Microsoft JDBC Driver for SQL Server
//   https://docs.microsoft.com/en-us/sql/connect/jdbc/microsoft-jdbc-driver-for-sql-server
//   (URL valid as of 2023.03.30)
  
//  https://docs.microsoft.com/en-us/sql/connect/jdbc/using-the-jdbc-driver
//   - configure CLASSPATH
 
//  Please make sure the specific jar file of Microsoft JDBC Driver for SQL Server is under JDK & JRE folders:
//   on a CSIL system, these files are:
//    C:\Program Files\Java\jdk\jre\lib\ext\mssql-jdbc-12.2.0.jre8.jar
//    C:\Program Files\Java\jre\lib\ext\mssql-jdbc-12.2.0.jre11.jar
//    (as of 2023.03.30)
//    C:\Program Files\Java\jdk\jre\lib\ext\mssql-jdbc-8.2.2.jre8.jar
//    C:\Program Files\Java\jre\lib\ext\mssql-jdbc-8.2.2.jre8.jar
//    (as of 2020.06.02)
//     (the folder "C:\Program Files\Java\jdk\jre\lib\ext" holds contents of "Extension classes")
// 	(https://docs.oracle.com/javase/7/docs/technotes/tools/findingclasses.html)
//     (https://docs.oracle.com/javase/7/docs/technotes/tools/windows/classpath.html)
	
//  observation: the version 11.2 supports JRE 17 &18, but not version 12.2
//  so, what we could do?
//  A: The jars in the 12.2 package are named according to Java version compatibility.
//     For example, the mssql-jdbc-12.2.0.jre11.jar file from the 12.2 package should be used with Java 11 (or higher).


// set CLASSPATH=.;c:\progra~1\Java\jdbc4sqlserver\mssql-jdbc-12.2.0.jre11.jar
//   ^^^ tested @2023.11.17
  
  
// Author: Johnny Zhang

// You should run this program on a CSIL system.

// Last modified @ 2023.11.17, 2023.03.30 2020.06.02, 2008.12.20, 2018.03.29

// */

// /*
// Please modify this program before using.

// alternation includes: username and passphrase for CSIL SQL Server standard login

// note: the JDBC jar file must be on the variable CLASSPATH

// */
import java.sql.*;

class Main {
	public static void main(String[] args) {

		// get the connection
        Connection connection = DatabaseConnection.getConnection();
		
		//initialize the login
		Login login = new Login(connection);
		
		// initialize business search option
		BusinessSearch business = new BusinessSearch(connection);

		//initialize user search option
		UserSearch userSearch = new UserSearch(connection);

		//initialize make friend option
		MakeFriend makeFriend = new MakeFriend(connection);

		// First confirm if user is logged in (first input)
		boolean validUser = login.userIsLoggedIn();

		// get user's input for Yelp user ID
		String userYelpID = login.getUserID();

		// initialize review business option
		ReviewBusiness reviewBusiness = new ReviewBusiness(connection, userYelpID);

		// once user is logged in, start the program
		while (validUser) {

			// get user's first choice
			int firstChoice = InitialUserInput.getUserChoice();

			switch (firstChoice) {
				case 1:
					System.out.println("User chose search business. \n");
					business.searchBusinesses();
					break;
				case 2:
					System.out.println("User chose search Users. \n");
					userSearch.searchUser();
					break;
				case 3:
					System.out.println("User chose to add friend. \n");
					makeFriend.makeFriend(userYelpID);
					break;
				case 4:
					System.out.println("User chose to make a business review. \n");
					reviewBusiness.reviewBusiness();
					break;
				case 5:
					System.out.println("User chose to logout\n");
					validUser = false;
					break;

				default: 
					System.out.println("Invalid choice. Please choose again.\n");
			}
		}

		System.out.println("Thank you for using this program. Have a nice day. ");

        // Close the connection when done 
        DatabaseConnection.closeConnection();
    }
}

//BUSINESS review test
// class sample_java_jdbc_driver_for_sqlserver {
//     public static void main(String[] args) {
//         // Get the database connection
//         Connection connection = DatabaseConnection.getConnection();

//         // User and business IDs to search for
//         String userIdToSearch = "NsdQHIDKD-22B6paUuJRbw";
//         String businessIdToSearch = "XnPlMmc5O68XywXPEOUTkA";

//         // Perform the SQL query to fetch rows from copy_review based on user and business IDs
//         String query = "SELECT * FROM copy_review WHERE user_id = ? AND business_id = ?";

//         try (PreparedStatement pstmt = connection.prepareStatement(query)) {
//             // Set the parameters in the query
//             pstmt.setString(1, userIdToSearch);
//             pstmt.setString(2, businessIdToSearch);

//             // Execute the query
//             try (ResultSet resultSet = pstmt.executeQuery()) {
//                 // Print column headers
//                 System.out.printf("%-32s%-32s%n",
//                         "User ID", "Business ID");

//                 // Print each row
//                 while (resultSet.next()) {
//                     System.out.printf("%-32s%-32s%n",
//                             resultSet.getString("user_id"),
//                             // resultSet.getString("friend"));
//                             resultSet.getString("business_id"));
//                             // resultSet.getString("stars"),
//                             // resultSet.getString("useful"),
//                             // resultSet.getString("funny"),
//                             // resultSet.getString("cool"),
//                             // resultSet.getString("date"));
//                 }
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         } finally {
//             // Close the connection when done
//             DatabaseConnection.closeConnection();
//         }
//     }
// }

// BUSINESS REVIEW DELETE EXAMPLE
// class sample_java_jdbc_driver_for_sqlserver {
// public static void main(String[] args) {
//         Connection connection = DatabaseConnection.getConnection();

//         // Step 3: Use the obtained Connection in your main logic
//         try (Statement statement = connection.createStatement()) {

//             // Step 3.1: Delete the specific row
//             String deleteSQL = "DELETE FROM copy_review " +
//                                "WHERE user_id = 'NsdQHIDKD-22B6paUuJRbw' AND business_id = 'XnPlMmc5O68XywXPEOUTkA'";

//             int rowsAffected = statement.executeUpdate(deleteSQL);

//             if (rowsAffected > 0) {
//                 System.out.println("Row deleted successfully.");
//             } else {
//                 System.out.println("No matching row found to delete.");
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         // Step 4: Close the connection when done (best practice)
//         DatabaseConnection.closeConnection();
//     }
// }


// REVIEW table test
// class sample_java_jdbc_driver_for_sqlserver {
// public static void main(String[] args) {
//         // Get the database connection
//         Connection connection = DatabaseConnection.getConnection();

//         // Perform the SQL query to fetch all rows from copy_review
//         String query = "SELECT * FROM copy_review";

//         try (PreparedStatement pstmt = connection.prepareStatement(query);
//              ResultSet resultSet = pstmt.executeQuery()) {

//             // Print column headers
//             System.out.printf("%-32s%-32s%-32s%-10s%-10s%-10s%-10s%-30s%n",
//                     "Review ID", "User ID", "Business ID", "Stars", "Useful", "Funny", "Cool", "Date");

//             // Print each row

			
//             while (resultSet.next()) {
//                 System.out.printf("%-32s%-32s%-32s%-10s%-10s%-10s%-10s%-30s%n",
//                         resultSet.getString("review_id"),
//                         resultSet.getString("user_id"),
//                         resultSet.getString("business_id"),
//                         resultSet.getString("stars"),
//                         resultSet.getString("useful"),
//                         resultSet.getString("funny"),
//                         resultSet.getString("cool"),
//                         resultSet.getString("date"));
					
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         } finally {
//             // Close the connection when done
//             DatabaseConnection.closeConnection();
//         }
//     }
	
// }


// DELETE FRIEND EXAMPLE
// class sample_java_jdbc_driver_for_sqlserver {
// public static void main(String[] args) {
//         Connection connection = DatabaseConnection.getConnection();

//         // Step 3: Use the obtained Connection in your main logic
//         try (Statement statement = connection.createStatement()) {

//             // Step 3.1: Delete the specific row
//             String deleteSQL = "DELETE FROM copy_friendship " +
//                                "WHERE user_id = 'NsdQHIDKD-22B6paUuJRbw' AND friend = 'nSFkJuiElrGsy5C47s2_vA'";

//             int rowsAffected = statement.executeUpdate(deleteSQL);

//             if (rowsAffected > 0) {
//                 System.out.println("Row deleted successfully.");
//             } else {
//                 System.out.println("No matching row found to delete.");
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         // Step 4: Close the connection when done (best practice)
//         DatabaseConnection.closeConnection();
//     }
	
// }

//FRIEND TEST
// class sample_java_jdbc_driver_for_sqlserver {
// public static void main(String[] args) {
//         // Step 2: Call the static getConnection method
//         Connection connection = DatabaseConnection.getConnection();
// 		String sSQL= "SELECT * from copy_friendship";

//         // Step 3: Use the obtained Connection in your main logic
//         try (Statement statement = connection.createStatement();
//              ResultSet resultSet = statement.executeQuery(sSQL)) {

    
//             while (resultSet.next()) {
//                 System.out.println(resultSet.getString("user_id"));
// 				System.out.println(resultSet.getString("friend"));
//             }
        
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         // Step 4: Close the connection when done (best practice)
//         DatabaseConnection.closeConnection();
//     }
// }

//FIND FRIEND TEST
// class sample_java_jdbc_driver_for_sqlserver {
// public static void main(String[] args) {
//         // Step 2: Call the static getConnection method
//         Connection connection = DatabaseConnection.getConnection();

// 		String sSQL = "SELECT * FROM copy_friendship " +
//                       "WHERE user_id = 'NsdQHIDKD-22B6paUuJRbw' AND friend = 'nSFkJuiElrGsy5C47s2_vA'";

//         // Step 3: Use the obtained Connection in your main logic
//         try (Statement statement = connection.createStatement();
//              ResultSet resultSet = statement.executeQuery(sSQL)) {

    
//             while (resultSet.next()) {
//                 System.out.println(resultSet.getString("user_id"));
// 				System.out.println(resultSet.getString("friend"));
//             }
        
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         // Step 4: Close the connection when done (best practice)
//         DatabaseConnection.closeConnection();
//     }
// }


//USER TEST
// class sample_java_jdbc_driver_for_sqlserver {
// public static void main(String[] args) {
//         // Step 2: Call the static getConnection method
//         Connection connection = DatabaseConnection.getConnection();
// 		String sSQL= "SELECT name, review_count, average_stars FROM copy_user_yelp WHERE name='Angela' AND review_count >=3 AND average_stars >= 4";
// 		String nam="";
// 		String rc="";
// 		String as="";

//         // Step 3: Use the obtained Connection in your main logic
//         try (Statement statement = connection.createStatement();
//              ResultSet resultSet = statement.executeQuery(sSQL)) {

// 			// System.out.printf("%-30s%-40s%-30s%-15s%-5s%n",
//             //     "Business ID", "Name", "Address", "City", "Stars");

// 			int rowCount = 0;
    
//             while (resultSet.next() && rowCount < 10) {
//                 rowCount++;
//                 nam = resultSet.getString("name");
//                 rc = resultSet.getString("review_count");
//                 as = resultSet.getString("average_stars");

//                 System.out.printf("%-30s%-40s%-30s%n", nam, rc, as);
//             }
        
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         // Step 4: Close the connection when done (best practice)
//         DatabaseConnection.closeConnection();
//     }
// }




//BUSINESS TEST
// class sample_java_jdbc_driver_for_sqlserver {
// public static void main(String[] args) {
//         // Step 2: Call the static getConnection method
//         Connection connection = DatabaseConnection.getConnection();
// 		String sSQL= "SELECT business_id, name, address, city, stars FROM copy_business WHERE name='KFC' AND city='Edmonton'";
// 		String bu="";
// 		String nam="";
// 		String ad="";
// 		String ci="";
// 		double st;

//         // Step 3: Use the obtained Connection in your main logic
//         try (Statement statement = connection.createStatement();
//              ResultSet resultSet = statement.executeQuery(sSQL)) {

// 			System.out.printf("%-30s%-40s%-30s%-15s%-5s%n",
//                 "Business ID", "Name", "Address", "City", "Stars");

// 		int rowCount = 0;
    
//             while (resultSet.next() && rowCount < 5) {
//                 rowCount++;
//                 bu = resultSet.getString("business_id");
//                 nam = resultSet.getString("name");
//                 ad = resultSet.getString("address");
//                 ci = resultSet.getString("city");
//                 st = resultSet.getDouble("stars");

//                 System.out.printf("%-30s%-40s%-30s%-15s%-5s%n", bu, nam, ad, ci, st);
//             }
        
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         // Step 4: Close the connection when done (best practice)
//         DatabaseConnection.closeConnection();
//     }
// }


// ORIGINAL
// class sample_java_jdbc_driver_for_sqlserver {
// 	private static Connection con;
// 	private static String space = "                                           ";

// 	public static void main(String[] args)
// 	{
// 		PreparedStatement pstmt = null;
// 		ResultSet rs;
// 		String sSQL= "SELECT * FROM business";	//the table was created by helpdesk
// 		String temp="";
		
// 		String sUsername = "s_jwa423";
// 		String sPassphrase = "22yhATNmjaMtb43T";
// 		// ^^^ modify these 2 lines before compiling this program
// 		// please replace the username with your SFU Computing ID
// 		// please get the passphrase from table 'dbo.helpdesk' of your course database (using SSMS or Azure Data Studio)
		
// 		String sSQLServerString = "jdbc:sqlserver://cypress.csil.sfu.ca;" +
// 			"encrypt=true;trustServerCertificate=true;loginTimeout=90;";
		
		
// 		try
// 		{
// 			con = DriverManager.getConnection ( sSQLServerString, sUsername, sPassphrase );
// 		} catch ( SQLException se )
// 			{
// 				System.out.println ( "\n\nFail to connect to CSIL SQL Server; exit now.\n\n" );
				
// 				se.printStackTrace(System.err);
// 				System.err.println("SQLState: " +
// 					((SQLException)se).getSQLState());

// 				System.err.println("Error Code: " +
// 					((SQLException)se).getErrorCode());

// 				System.err.println("Message: " + se.getMessage());

// 				return;
// 			}
		
// 		try
// 		{
// 			pstmt = con.prepareStatement(sSQL);
// 			rs = pstmt.executeQuery();
			
// 			System.out.println("\nThe table 'business' contains:\n\n");
			
// 			while (rs.next()) {
// 				temp= rs.getString("name");	//the table has a field 'username'
// 				System.out.println(temp);
// 			}
// 			rs.close();
// 			System.out.println("\nSuccessfully connected to CSIL SQL Server!\n\n");
// 		}catch (SQLException se)
// 			{
// 				System.out.println("\nSQL Exception occurred, the state : "+
// 								se.getSQLState()+"\nMessage:\n"+se.getMessage()+"\n");
// 				return;
// 			}
// 	}
// 	//end of main
// }
// //end of class sample_java_jdbc_driver_for_sqlserver


// User Yelp ID's
// NsdQHIDKD-22B6paUuJRbw
// nSFkJuiElrGsy5C47s2_vA
// NsfR1givyGj8IPcZnI-7Kw
// nSJ4jz--8rJQbzkTNvLXqQ
// nsKTCBpD8tEhBbHDFLDwWw


// Business-id: XnPlMmc5O68XywXPEOUTkA




