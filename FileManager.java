
package lab2;

import java.io.*;
import java.nio.file.*;
import java.text.*;
import javax.swing.JOptionPane;
import java.util.*;

/*
 * Author: Joshua McMahan
 * Class: CITC 1311 P01
 * Program Name: Driver Exam (FileManager)
 * Program Description:
 *          This is part of the Driver Exam software. This class is responsible
 *          for reading/writing all info meant to be accessed by this program.
 *          It can create new files and file directories, read files from
 *          said directories, and list files found in these directories for the
 *          user to choose a file from.
 */

public class FileManager extends ExamMain {

    // Two bufferedreaders, just in case
    static BufferedReader br;
    static BufferedReader br2;

    // Variable to store the chosen file number for later reading
    static private int fileNum=0;

    // Arrays/Arraylist for student info, answers, and file info
    private ArrayList<String> student = new ArrayList<>();
    private ArrayList<String> answers = new ArrayList<>();
    static String[] examNames;
    private File[] foundFiles;
    private File folder;
    
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static Date date = new Date();

    // Default Constructor
    public FileManager()
        {
            // Pre-load files
            loadFiles(1);

            boolean valid=false;
            
            // Ask user to select the file they want to use
            do {
            String choose=null;
            
            choose = JOptionPane.showInputDialog(null,
                    "Please type the number of the answer key you wish to use: \n" +
                    lister(),
                    "Input",
                    JOptionPane.QUESTION_MESSAGE);
            
            if (choose == null || choose.equals(""))
                {menu();}

            choose=choose.trim();

            // Convert input and determine if valid
            for (int i=1; i<foundFiles.length+1; i++)
            {
                String iString = "" + i;
                if (choose.equals(iString))
                    {
                        valid=true;
                        fileNum=Integer.parseInt(choose);
                            if (fileNum>0)
                                {fileNum--;}
                    }
            }

            if (!valid)
            {
                JOptionPane.showMessageDialog(null,
                    "Invalid Entry: Please type only the number of the answer key you wish to use",
                    "ERROR",
                    JOptionPane.ERROR_MESSAGE);
            }

            } while (!valid);

            // Read/Prepare answers for test
            try {
                loadAnswers();
            } catch (FileNotFoundException a){} catch (IOException b){}
        }
    
    // Constructor for Student Records access
    public FileManager(int load) 
        {
            // Pre-load files
            loadFiles(2);
            
            fileNum=0;
            
            boolean valid=false;

            // Ask user to select the files they want to access
            if (load==2) 
            {
                do {
                    String choose = JOptionPane.showInputDialog(null,
                        "Please type the number of the exam record you wish to view: \n" +
                        lister(),
                        "Input",
                        JOptionPane.QUESTION_MESSAGE);
                    
                    if (choose == null || choose.equals(""))
                        {menu();}

                    choose=choose.trim();

                    // Convert input and determine if valid
                    for (int i=1; i<foundFiles.length+1; i++)
                        {
                            String iString = "" + i;
                            if (choose.equals(iString))
                                {
                                    valid=true;
                                    fileNum=Integer.parseInt(choose);
                                        if (fileNum>0)
                                            {fileNum--;}
                                }
                        }

                    if (!valid)
                        {
                            JOptionPane.showMessageDialog(null,
                                "Invalid Entry: Please type only the number of the exam record you wish to view",
                                "ERROR",
                                JOptionPane.ERROR_MESSAGE);
                        }

                    } while (!valid);
            }
        
        // Read/Prepare student info
        try {
                findStudent();
            } catch (FileNotFoundException a){} catch (IOException b){}
        }

    // Method to load the exam answer key files
    private void loadFiles(int choose)
    {
        switch (choose)
        {
            case 1: // Load answer files
        
                folder = new File("E:\\SpringSemester2018\\DriverExams\\ExamFiles");
                foundFiles = folder.listFiles();
                break;
            case 2: // load student files
        
                folder = new File("E:\\SpringSemester2018\\DriverExams\\Students\\" + pinNum);
                foundFiles = folder.listFiles();
                
                break;
        }
        
    }

    // Method to load the test's answers into answers array from .txt file
    private void loadAnswers() throws FileNotFoundException, IOException
    {
        // Prepare bufferedreader
        reader();

        // Read information to ArrayList
        try {
            String line;

            int i=0;

            while ((line = br.readLine()) != null)
                {
                    answers.add(line);

                    i++;
                }

            } finally {
                br.close();
                }
    }
    
    // Method to load the test's answers into answers array from .txt file
    private void findStudent() throws FileNotFoundException, IOException
    {
        // Prepare bufferedreader
        reader();

        // Read information to ArrayList
        try {
            String line;

            int i=0;

            while ((line = br.readLine()) != null)
                {
                    student.add(line);

                    i++;
                }

            } finally {
                br.close();
                }
    }
    
    // Getter method for student info array
    public String[] getStudent()
    {
        String[] studentString = student.toArray(new String[student.size()]);
        
        return studentString;
    }


    // Getter method for answers array
    public String[] getAnswers()
    {
        String[] answerString = answers.toArray(new String[answers.size()]);
        
        return answerString;
    }

    // Method to list files available for access
    private String lister()
    {
        String output="";
        
        for (int i=0; i<foundFiles.length; i++)
        {
            output+=(i+1)+". " + (foundFiles[i].getName() ).replace(".txt", "") + "\n";
        }
        
        return output;
    }

    
    // Method to save the student's exam scores to a file directory
    public void saveExam(int correct, int incorrect, String[] missed, boolean pass) throws FileNotFoundException
    {
        int count=1;
        
        // Source for Student Files when submitted
        // O:\\CSIT\\CITC1311P01JavaII\\Handin\\JoshuaMcmahan\\DriverExams\\Students\\
        
        Path newPath = Paths.get("E:\\SpringSemester2018\\DriverExams\\Students\\" + pinNum);
        
        if (Files.exists(newPath)) //If file directory exists, write file
        {
            try( PrintWriter out = new PrintWriter( newPath + "\\" + 
                            "Exam " + (fileNum+1) + "_" + dateFormat.format(date) ) )
            {
                out.println(name); //Line 1 = Student Name
                out.println(pinNum); // Line 2 = Student PIN
                out.println(correct); //Line 3 = Number of correct answers 
                out.println(incorrect); //Line 4 = Number of incorrect answers
                out.println(((pass)? 0 : 1 ) ); //Line 5 = 0 Pass / 1 Fail
                
                for (String element : missed) //Line 6-? = Specific questions missed
                    {
                        if (element!=null)
                            out.println(((count<10)?"0":"") + count + element.charAt(0));
                        
                        count++;
                    } 
                
            } catch (FileNotFoundException e) {System.out.println("Error: Cannot save file");}
        }
        else // If file directory does not exist, create file and write to it
        {
            try {
                Files.createDirectory(newPath);
            } catch (IOException ie) {
                System.out.println("Error making file path"); 
            }
            
            try( PrintWriter out = new PrintWriter( newPath + "\\" + 
                            "Exam " + (fileNum+1) + "_" + dateFormat.format(date) ) )
            {
                out.println(name); //Line 1 = Student Name
                out.println(pinNum); // Line 2 = Student PIN
                out.println(correct); //Line 3 = Number of correct answers 
                out.println(incorrect); //Line 4 = Number of incorrect answers
                out.println(((pass)? 0 : 1 ) ); //Line 5 = 0 Pass / 1 Fail
                
                for (String element : missed) //Line 6-? = Specific questions missed
                    {
                        if (element!=null)
                            out.println(((count<10)?"0":"") + count + element.charAt(0));
                        
                        count++;
                    } 
                    
            } catch (FileNotFoundException e) {System.out.println("Error: Cannot save file");}
        }
        
    }
    
    // Sets the BufferedReader's FileInputStream destination with each call
    private void reader() throws FileNotFoundException, IOException
    {
        br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(foundFiles[fileNum])));

        br2 = new BufferedReader(
                    new InputStreamReader(new FileInputStream(foundFiles[fileNum])));
    }
}
