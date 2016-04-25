
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
			//myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coscuw", "student" , "student");
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coscuw", "root" , "90dziaDANmySQL~+!");
			
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
			
		
			//displayAll(myRs);
			//displayCourseInfo(myRs);
			
			// printHeader();
			createReportDocument(myRs);


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

	
	

	
	private static void createReportDocument(ResultSet myRs) throws SQLException {
		
		int pageCount = 0;
		int lineCount = 0;
		String lineCount_str = "";
		int currentCRN = 0;
		String currentCRN_str = "";
		String colText = "";
		String outputText = "";
	

		while (myRs.next()) {
			
			System.out.printf("Line count:");
			System.out.printf("%d", lineCount);
			
			int courseCRN = myRs.getInt("underEnrolledCRN");
			int noteNumber = myRs.getInt("notenumber");
			String courseNote = myRs.getString("note");
			
			String courseSubject = myRs.getString("subject");
			int courseNumber = myRs.getInt("number");
			int courseSection = myRs.getInt("section");
			String courseTitle = myRs.getString("title");
			int studentCount = myRs.getInt("NumberOfStudents");
			

			
			// Append the information from the result set to the output file
			String courseCRN_str = String.valueOf(courseCRN);
			String courseSection_str = String.valueOf(courseSection);
			String studentCount_str = String.valueOf(studentCount);
			String courseNumber_str = String.valueOf(courseNumber);
			String courseNote_str = String.valueOf(courseNote);

			
			try {
				
				//System.out.println("Trying to write file ...");
				BufferedWriter writer = new BufferedWriter(new FileWriter(new File("reportDocument.doc"), true));

				//writer.write(courseCRN_str);
				//writer.write("\t");
				//writer.write(courseSection_str);
				//writer.write("\t");
				//writer.write(studentCount_str);
				//writer.newLine();
				//writer.close();
				//System.out.println("End");
				
				if (courseCRN!=currentCRN) {
					
					//System.out.printf("\n  CRN: %d, Number of students enrolled: %d\n", courseCRN, studentCount);
					//System.out.printf("  %s %d %s \t(Section %d)\n", courseSubject, courseNumber, courseTitle, courseSection);
					//System.out.printf("\t Notes: ");
			
					
					//writer.newLine();
					//writer.write("*** Here's the good stuff ***");
					
					
					// Initial format (not organized by column)
					//writer.write("\n  CRN: " + courseCRN_str + ", Number of students enrolled: " + studentCount_str + "\n");
					//writer.write("  " + courseSubject + " ");
					//writer.write(courseNumber_str);
					//writer.write(" " + courseTitle);
					//writer.write("\t (Section " + courseSection_str + ")\n");
					//lineCount=lineCount+3;
					
					
					
					// Print course info column-wise
					//setColWidth()
			        //writer.write(setColWidth("CRN", 5) + "*");
			        //writer.write(setColWidth("Enrolled", 8) + "*");
			        //writer.write(setColWidth("Subject", 5) + "*");
			        //writer.write(setColWidth("Number", 6) + "*");
			        //writer.write(setColWidth("Section", 7) + "*");
			        //writer.write(setColWidth("Title", 31) + "*");
			        //writer.newLine();
			        
			        
			        //writer.write(setColWidth(courseCRN_str, 5) + "*");
			        //writer.write(setColWidth(studentCount_str, 8) + "*");
			        //writer.write(setColWidth(courseSubject, 5) + "*");
			        //writer.write(setColWidth(courseNumber_str, 6) + "*");
			        //writer.write(setColWidth(courseSection_str, 7) + "*");
			        //writer.write(setColWidth(courseTitle, 31) + "*");
			        //writer.newLine();
			        
			        // Is padLeft better?
			        //writer.write(padLeft(courseCRN_str, 5) + "*");
			        //writer.write(padLeft(studentCount_str, 8) + "*");
			        //writer.write(padLeft(courseSubject, 5) + "*");
			        //writer.write(padLeft(courseNumber_str, 6) + "*");
			        //writer.write(padLeft(courseSection_str, 7) + "*");
			        //writer.write(padLeft(courseTitle, 31) + "*");
			        //writer.newLine();
			        
					
			        // Create a single string per line with fixed spacing
					writer.newLine();
			        colText = String.format("%-6s %-8s %-8s %-8s %-8s %-31s", "CRN", "Enrolled", "Subject", "Number", "Section", "Title");
			        writer.write(colText);
			        writer.newLine();
			        lineCount = lineCount + 2;
			        
			        outputText = String.format("%-6s %-8s %-8s %-8s %-8s %-31s", courseCRN_str, studentCount_str, courseSubject, courseNumber_str, courseSection_str, courseTitle);
			        writer.write(outputText);
			        writer.newLine();
			        lineCount ++;

			        
			        // Present notes below the course info
			        if (courseNote != null) {
			        	writer.write("\tNotes: ");
			        	lineCount++ ;
					}
			        
					
					courseCRN_str = currentCRN_str;
				}
				
				
				if (courseNote == null) {
					//System.out.printf("none\n");
					//writer.write("none\n");
					writer.write("");
										
				} else if (noteNumber != 1) {
					//System.out.printf("\t");
					//System.out.printf(" %s \n", courseNote);
					writer.write("\t");			
					writer.write(courseNote + "\n");
					
					lineCount++;
				} else {
					//System.out.printf(" %s \n", courseNote);
					writer.write(courseNote + "\n");
					
					lineCount++;
				}
				
				//writer.write("\n___ end good stuff ___ \n");
				
				//lineCount_str = String.valueOf(lineCount);
				
				//writer.write("Line count: ");
				//writer.write(lineCount);
				
				writer.close();


			} catch (IOException ex) {
				ex.printStackTrace();
			}
			

			
			if (courseCRN!=currentCRN) {
				System.out.printf("\n  CRN: %d, Number of students enrolled: %d\n", courseCRN, studentCount);
				System.out.printf("  %s %d %s \t(Section %d)\n", courseSubject, courseNumber, courseTitle, courseSection);
				System.out.printf("\t Notes: ");
		
				currentCRN = courseCRN;
			}

			//System.out.printf("\t Note: %d, %s \n", noteNumber, courseNote);
			if (courseNote == null) {
				System.out.printf("none\n");
			} else if (noteNumber != 1) {
				System.out.printf("\t");
				System.out.printf(" %s \n", courseNote);
			} else {
			System.out.printf(" %s \n", courseNote);
			}
		} //while
		

	}
	
	
	private static void printHeader() throws SQLException {
		
		// Create a heading for the document
		String greetings = "Hello, Daniel !";
		String description = "This file is so cool";
		
		//
	
		try {
			System.out.println("Trying to write file ...");
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("reportDocument.doc"), true));
			writer.write(greetings);
			writer.newLine();
			
			writer.write("Page 1 information" + (char)12);  
			
	        writer.write("Page 2 after page break char");  
	        // System.out.println(padRight(greetings, 20) + "*");
	        writer.write(padRight(greetings, 20) + "*");
	        writer.newLine();
	        writer.write(padRight("Col1", 20) + "*");
	        writer.write(padRight("Col2", 20) + "*");
	        writer.write(padRight("Col3", 20) + "*");
	        writer.newLine();
	        writer.write(padRight("CRN", 5) + " ");
	        writer.write(padRight("Enrolled", 8) + " ");
	        writer.write(padRight("Subject", 5) + " ");
	        writer.write(padRight("number", 6) + " ");
	        writer.write(padRight("Section", 7) + " ");
	        writer.write(padRight("title", 31) + " ");
	       
			
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
	
	
	public static String padRight(String s, int n) {
	    return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	   return String.format("%1$" + n + "s", s);  
	}
	
	public static String setColWidth(String s, int n) {
	    return String.format("%1$-" + n + "s", s);  
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
				System.out.printf("\n  CRN: %d, Number of students enrolled: %d\n", courseCRN, studentCount);
				System.out.printf("  %s %d %s \t(Section %d)\n", courseSubject, courseNumber, courseTitle, courseSection);
				System.out.printf("\t Notes: ");
				currentCRN = courseCRN;
			}

			//System.out.printf("\t Note: %d, %s \n", noteNumber, courseNote);
			if (courseNote == null) {
				System.out.printf("none\n");
			} else if (noteNumber != 1) {
				System.out.printf("\t");
				System.out.printf(" %s \n", courseNote);
			} else {
			System.out.printf(" %s \n", courseNote);
			}
			
		}
	}
	
	
	
	
	
}



