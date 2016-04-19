
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;


// Goal:
// Print the CRN, Department, Course Number, Section Number, 
// Course Title, Number of Enrolled Students, and Notes (or Comments) 
// for each class that has fewer than 10 students. 


public class generateReport {
	
	public static void main(String[] args) throws SQLException {

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		
		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coscuw", "student" , "student");
			
			// 2. Prepare statement
			myStmt = myConn.prepareStatement("select * from enrolledin where wNumber = ? and CRN > ? ");
			
			// 3. Set the parameters
			myStmt.setString(1, "w87501680");
			myStmt.setDouble(2, 10479);
			
			// 4. Execute SQL query
			myRs = myStmt.executeQuery();
			
			// 5. Display the result set
			display(myRs);
		
			//
			// Reuse the prepared statement:  salary > 25000,  department = HR
			//

			System.out.println("\n\nReuse the prepared statement:");
			
			// 6. Set the parameters
			myStmt.setString(1, "w87501680");
			myStmt.setString(2, "0");
			
			// 7. Execute SQL query
			myRs = myStmt.executeQuery();
			
			// 8. Display the result set
			display(myRs);
			
			// Run a quick-and-easy query
			System.out.println("\n\nA more complicated query:");
			myRs = myStmt.executeQuery("SELECT * FROM enrolledin LIMIT 10");
			display(myRs);
			
			// Run a more complicated query
			System.out.println("\n\nA useful query:");
			myRs = myStmt.executeQuery("SELECT enrolledin.CRN, scheduledcourses.section, COUNT(enrolledin.wNumber) AS NumberOfStudents " +  
			"FROM coscuw.enrolledin " +  
			"INNER JOIN coscuw.scheduledcourses " + 
			"ON enrolledin.CRN = scheduledcourses.crn " + 
			"GROUP BY CRN " + 
			"HAVING COUNT(*)<=3 " +
			"ORDER BY NumberOfStudents ASC, enrolledin.CRN ASC " +
			"LIMIT 10 ");
			
			displayUnderEnrolledCourses(myRs);
			// writeToFileUnderEnrolledCourses(myRs);
			// printTest();


		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		finally {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();
			}
		}
	}

	private static void display(ResultSet myRs) throws SQLException {
		while (myRs.next()) {
			
			String studentWNum = myRs.getString("wNumber");
			double courseCRN = myRs.getDouble("CRN");

			
			System.out.printf("%s, %.0f\n", studentWNum, courseCRN);
		}
	}
	
	
	private static void displayUnderEnrolledCourses(ResultSet myRs) throws SQLException {
		
		// Create a heading for the document
		String greetings = "Hello, Daniel !";
		String description = "This file is so cool";
	
		try {
			System.out.println("Trying to write file ...");
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("reportDocument.doc"), true));
			writer.write(greetings);
			writer.newLine();
			
			writer.write("Page 1 information" + (char)12);  
			
	        writer.write("Page 2 after page break char");  
			
			writer.newLine();
			writer.write(description);
			writer.newLine();
			writer.newLine();
			writer.close();
	
			System.out.println("End");
	
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		
		// Write the result set to file
		while (myRs.next()) {
			
			double courseCRN = myRs.getDouble("CRN");
			double courseSection = myRs.getDouble("section");
			double courseEnrollmentTotal = myRs.getDouble("NumberOfStudents");
			
			//System.out.printf("%.0f, %.0f, %.0f\n", courseCRN, courseSection, courseEnrollmentTotal);
			
			
			// Append the information from the result set to the output file
			String courseCRNs = String.valueOf(courseCRN);
			String courseSections = String.valueOf(courseSection);
			String courseEnrollmentTotals = String.valueOf(courseEnrollmentTotal);
			
			try {
				System.out.println("Trying to write file ...");
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("reportDocument.doc"), true));

				writer.write(courseCRNs);
				writer.write("\t");
				writer.write(courseSections);
				writer.write("\t");
				writer.write(courseEnrollmentTotals);
				writer.newLine();
				writer.close();
				System.out.println("End");



			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	private static void writeToFileUnderEnrolledCourses(ResultSet myRs) throws SQLException {
		while (myRs.next()) {
			
			double courseCRN = myRs.getDouble("CRN");
			double courseSection = myRs.getDouble("section");
			double courseEnrollmentTotal = myRs.getDouble("NumberOfStudents");
			
			System.out.printf("%.0f, %.0f, %.0f\n", courseCRN, courseSection, courseEnrollmentTotal);
			
			String greetings = "Hello, Daniel !";
			String description = "This file is so cool";
			
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("reportDocument.txt")));
				writer.write(greetings);
				writer.newLine();
				writer.write(description);
				writer.close();
				System.out.println("End");



			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	
	public static void printTest() {
		
		String greetings = "Hello, Daniel !";
		String description = "This file is so cool";
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("textfileTest.txt")));
			writer.write(greetings);
			writer.newLine();
			writer.write(description);
			writer.close();
			System.out.println("End");



		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	
}



