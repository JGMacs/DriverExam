
package lab2;

/*
 * Author: Joshua McMahan
 * Class: CITC 1311 P01
 * Program Name: Driver Exam (ExamMain)
 * Program Description:
 *      This program is a fully-functional test score grader/record keeper.
 *      Users may create new profiles for students taking the exam - profiles
 *      that are accessed by PIN numbers - and can then store/recall test
 *      information in those profiles.
 */

import java.io.File;
import javax.swing.JOptionPane;

public class ExamMain {
    
    public static Object[][] students = new Object[3][3];
    
    static String name = null;
    static String exam = "Exam 1";
    static int pinNum;
    static final int PIN_LENGTH=4;
    
    static DriverExam runExam;
    static FileManager findFile;
    
    // Main Method; calls main menu
    public static void main(String[] args)
    {
        menu();
    }
    
    // Method for Main Menu
    public static void menu()
    {
        do {
            String[] options = {"New Student", "Load Student", "Quit"};
        
            int choose=2;
        
            // Main Menu dialog
            try {
                choose = JOptionPane.showOptionDialog(null, 
                    "Welcome, what would you like to do?", 
                    "Driver Exam", 
                    JOptionPane.YES_NO_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE, 
                    null, 
                    options, 
                    options[0]);
                
                if (choose != 0 && choose != 1 && choose != 2)
                    System.exit(0); // Exit program if "X" button clicked
                
            } catch (NullPointerException NPE) {System.exit(0);}
                
            switch (choose)
                {
                case 0: 
                    run(0); // Run Exam for new student
                    break;
                case 1:
                    loadStudent(); // Load student records
                    break;
                case 2:
                    System.exit(0);
                    break;
                }
        } while (PIN_LENGTH==4);
    }
    
    // Method to start the exam
    public static void run(int newStudent)
    {
        String ageTemp=null;
        
        if (newStudent == 0) // If 0, gather info for new student file
            {
            name = JOptionPane.showInputDialog(null, 
                    "Please enter the student's name: ");
                
            if (name == null || name.equals(""))
                {return;} // Return to main menu if null
            
            do {
                
                ageTemp = JOptionPane.showInputDialog(null, 
                        "Please enter the student's 4-digit PIN: ");
                    
                if (ageTemp == null || ageTemp.equals(""))
                    {return;} // Return to main menu if null
            
                pinNum = Integer.parseInt(ageTemp);
                
                if (ageTemp.length()>PIN_LENGTH || ageTemp.length()<PIN_LENGTH)
                    {
                    JOptionPane.showMessageDialog(null, 
                            "Error: PIN must be 4 digits", 
                            "ERROR", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } while (ageTemp.length()>PIN_LENGTH || ageTemp.length()<PIN_LENGTH);
            
            }
                
        runExam = new DriverExam(name, pinNum); // Start Exam
                
        runExam.newExam();
    }
    
    // Method to load student information
    public static void loadStudent()
    {
        String[] options = {"New Exam", "View Records", "Return to Main Menu"};
        
        String[] student;
        
        String pinTemp = null;
        
        pinTemp = JOptionPane.showInputDialog(null, 
                        "Please enter the student's 4-digit PIN: ");
                    
        if (pinTemp == null || pinTemp.equals(""))
            {return;} // Return to main menu if null
            
        pinNum = Integer.parseInt(pinTemp);
        
        File checker = new File("E:\\SpringSemester2018\\DriverExams\\Students\\" + pinNum);
        
        if (!checker.isDirectory()) // Checks if directory for this PIN exists before moving on
            {
                JOptionPane.showMessageDialog(null, 
                            "Error: Student PIN not found",
                            "ERROR",
                            JOptionPane.ERROR_MESSAGE);
                
                loadStudent(); // Have user reenter student PIN
            }
        
        for (int L=1; L>0; L++) // Infinite loop for student record dialog
        {
            int choose=2;
            
            try {
                choose = JOptionPane.showOptionDialog(null, 
                    "Welcome, what would you like to do?", 
                    "Driver Exam", 
                    JOptionPane.YES_NO_CANCEL_OPTION, 
                    JOptionPane.PLAIN_MESSAGE, 
                    null, 
                    options, 
                    options[0]);
            } catch (NullPointerException NPE) {menu();}
            
            switch (choose)
            {
            case 0: // Runs a new exam for this student's records
                findFile = new FileManager(1);
                student = findFile.getStudent();
                name = student[0];
                run(1);
                break;
            case 1: // Accesses current student record files
                findFile = new FileManager(2);
                student = findFile.getStudent();
                printStudent(student);
                break;
            case 2: // returns to main menu
                menu();
                break;
            }
        }
    }
    
    // Method to print student record information
    public static void printStudent(String[] student)
    {
        //Reads each line of file record, and decodes accordingly
        
        name = student[0];
        String totalCorrect = student[2];
        int totalIncorrect = Integer.parseInt(student[3]);
        boolean passed = (student[4].equals("0"));
        String missed = stringGen(student);
        
        JOptionPane.showMessageDialog(null, 
            name + "'s Driving Exam Results:\n" +
            "    Answers Correct: " + totalCorrect + "\n" +
            "    Answers Incorrect: " + totalIncorrect + "\n" +
            ((totalIncorrect==0)?"":
                    "    Answers Missed: " + "\n" + missed + "\n" ) +
            "Result: " + ((passed)?"Pass":"Fail")
        );
    }
    
    // Method to convert student's missed questions into a single string
    public static String stringGen(String[] student)
    {
        String output = "        ";
        
        int counter=0;
        
        for (int i=5; i<student.length; i++)
        {
            if (counter==5)
                {output += "\n        ";
                counter=0;}
            
            output += student[i].charAt(0) + "" + student[i].charAt(1) + ". " + student[i].charAt(2) + ";  ";
            
            counter++;
        }
        
        return output;
    }
    
}
