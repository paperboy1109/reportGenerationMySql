
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
		
		int pageCount = 0;
		
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
			myRs = myStmt.executeQuery(
			"SELECT * " +
			"FROM coursenotes RIGHT JOIN " +
				"( SELECT enrolledin.CRN AS underEnrolledCRN, " +  
					"offeringof.subject, " +
					"offeringof.number, " +
					"scheduledcourses.section,  " +
					"offeringof.title,  " +
					"COUNT(enrolledin.wNumber) AS NumberOfStudents " +
				"FROM coscuw.enrolledin " +  
					"JOIN coscuw.scheduledcourses ON enrolledin.CRN = scheduledcourses.crn " + 
					"JOIN coscuw.offeringof ON scheduledcourses.crn = offeringof.CRN " + 
				"GROUP BY underEnrolledCRN " + 
				"HAVING COUNT(*)<=5 AND scheduledcourses.section=3 AND underEnrolledCRN<11000 " +
				"ORDER BY NumberOfStudents ASC, enrolledin.CRN ASC " +
				"LIMIT 50) AS lowEnrollment " +
			"ON coursenotes.CRN=lowEnrollment.underEnrolledCRN ; "
			);
			
			//displayUnderEnrolledCourses(myRs);
			// writeToFileUnderEnrolledCourses(myRs);
			// printTest();
			
			//printHeader();
			//displayAll(myRs);
			displayCourseInfo(myRs);


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

	
	
	private static void displayAll(ResultSet myRs) throws SQLException {
		while (myRs.next()) {
			
			int courseCRN = myRs.getInt("underEnrolledCRN");
			int noteNumber = myRs.getInt("notenumber");
			String studentWNum = myRs.getString("note");
			
			String courseSubject = myRs.getString("subject");
			int courseNumber = myRs.getInt("number");
			int courseSection = myRs.getInt("section");
			String courseTitle = myRs.getString("title");
			int studentCount = myRs.getInt("NumberOfStudents");
			
			System.out.printf("CRN: %d, Number of students enrolled: %d\n", courseCRN, studentCount);
		}
	}
	
	
	private static void displayCourseInfo(ResultSet myRs) throws SQLException {
		
		int currentCRN = 0;
		
		while (myRs.next()) {
			
			int courseCRN = myRs.getInt("underEnrolledCRN");
			int noteNumber = myRs.getInt("notenumber");
			String courseNote = myRs.getString("note");
			
			String courseSubject = myRs.getString("subject");
			int courseNumber = myRs.getInt("number");
			int courseSection = myRs.getInt("section");
			String courseTitle = myRs.getString("title");
			int studentCount = myRs.getInt("NumberOfStudents");
			
			if (courseCRN!=currentCRN) {
				System.out.printf("  CRN: %d, Number of students enrolled: %d\n", courseCRN, studentCount);
				System.out.printf("\t Notes: ");
				currentCRN = courseCRN;
			}

			//System.out.printf("\t Note: %d, %s \n", noteNumber, courseNote);
			if (noteNumber != 1) {
				System.out.printf("\t");
			}
			System.out.printf(" %s \n", courseNote);
		}
	}
	
	
	private static void printHeader() throws SQLException {
		
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
	
	
	
	
}



