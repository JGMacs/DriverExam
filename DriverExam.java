
package lab2;

import javax.swing.JOptionPane;
import java.io.*;

/*
 * Author: Joshua McMahan
 * Class: CITC 1311 P01
 * Program Name: Driver Exam (DriverExam)
 * Program Description:
 *          This is part of the Driver Exam software. It runs the actual exam
 *          grading aspect, and has direct access to the FileManager for saving
 *          purposes, and exam info access.
 */

public class DriverExam extends FileManager {
    
    private String exam;
    private String name;
    private int pinNum;
    
    static ExamMain main = new ExamMain();
    
    
    // Default Constructor
    public DriverExam(){}
    
    // Constructor to assign student's name/age
    public DriverExam(String name, int age)
            {this.name=name; this.pinNum=age;}
    
    // Arrays for exam answers, user input, and missed questions
    private final String[] ANS_KEY = super.getAnswers();
    private String[][] ansInput = new String[ANS_KEY.length][ANS_KEY.length];
    private String[] questionsMissed = new String[ANS_KEY.length];
    
    // Valid answer possibilities
    private final static char[] LEGAL_INPUT = {'A', 'B', 'C', 'D'};
    
    // Total Correct and Incorrect answers, and counter for question number
    private int totalCorrect=0;
    private int totalIncorrect=0;
    private int counter=0;
    
    // Booleans to determine if answer input is valid, and if student passed
    private boolean inputValid;
    private boolean passed;
    
    // Method to run the exam
    public void newExam()
    {
        // Asks each question and collects user's answers
        for (int num=0; num<ANS_KEY.length; num++)
        {
            ansInput[0][num] = "" + (num+1);
            ansInput[1][num] = examQuestion();
            
            if (ansInput[1][num].equals(ANS_KEY[num]))
                {totalCorrect++;}
            else
                {totalIncorrect++;
                 questionsMissed[num] = "" + ansInput[1][num]; }
        }
        
        passed = totalCorrect>=15;
        
        // Displays final tally
        JOptionPane.showMessageDialog(null, 
            name + "'s Driving Exam Results:\n" +
            "    Answers Correct: " + totalCorrect + "\n" +
            "    Answers Incorrect: " + totalIncorrect + "\n" +
            ((totalIncorrect==0)?"":
                    "    Answers Missed: " + "\n" + stringGen()) + "\n" +
            "Result: " + ((passed)?"Pass":"Fail")
        );
        
        saveScreen();
    }
    
    // Method to collect the answers to the exam
    public String examQuestion()
    {
        String answer="";
        String ansTemp;
        
        counter++;
        
        do {
            
        inputValid=false;    
            
            do {
                ansTemp = JOptionPane.showInputDialog(null, 
                    "Please enter the answer for question " + counter);
        
                // If no answer entered, ask if user wants to go to main menu
                if (ansTemp == null || ansTemp.equals(""))
                    {int leave = JOptionPane.showConfirmDialog(null, 
                            "Return to Main Menu? Changes will not be saved.", 
                            "Driver Exam", 
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (leave==0)
                        menu();
                }
            } while (ansTemp == null || ansTemp.equals(""));
        
        answer = ansTemp.toUpperCase();
        char ansValidator = answer.charAt(0);
        
        for (int i=0; i<LEGAL_INPUT.length; i++)
            {
                if (ansValidator==LEGAL_INPUT[i])
                    {inputValid=true;}
            }
        
        if (!inputValid)
            {JOptionPane.showMessageDialog(null, 
                    "Error: Invalid Input, please reenter answer", 
                    "Error!", 
                    JOptionPane.ERROR_MESSAGE);}
        
        } while (!inputValid);
        
        return answer;
    }
    
    // Method to generate the list of questions missed by testee
    public String stringGen()
    {
        String output = "        ";
        
        int count=0;
        int answerNum=1;
        
        for (String element : questionsMissed)
        {
            if (count==5)
                {output += "\n        ";
                count=0;}
            
            if (element != null)
                {output += answerNum + ". " + element.charAt(0) + ";  ";}
            
            count++;
            answerNum++;
        }
        
        return output;
    }
    
    public void saveScreen()
    {
        String[] options = {"Yes", "Retry", "No"};
        
        int choose = JOptionPane.showOptionDialog(null, 
                "Would you like to save this score?", 
                "Driver Exam", 
                JOptionPane.YES_NO_CANCEL_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);
        
        if (choose != 0 && choose != 1 && choose != 2)
                    nextStep(); // Go to next dialog if "X" button clicked
        
        switch (choose)
            {case 0: // Save file
                try {
                    saveExam(totalCorrect, totalIncorrect, questionsMissed, passed);
                } catch (FileNotFoundException a){};
                nextStep();
                break;
            case 1: // Reset variables and restart exam
                totalCorrect=0;
                totalIncorrect=0;
                counter=0;
                newExam();
                break;
            case 2: // Go to next dialog box
                nextStep();
                break;
            default:
                JOptionPane.showMessageDialog(null, 
                        "Switch Case Error: newExam Method (line 95)", 
                        "ERROR", 
                        JOptionPane.ERROR_MESSAGE);
                break;            
            }
    }
    
    // Method to determine if returning to menu, or exiting program
    public void nextStep()
    {
        String[] options = {"Return to Main Menu", "Exit Program"};
        
        int choose = JOptionPane.showOptionDialog(null, 
                "What would you like to do?", 
                "Driver Exam", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                options, 
                options[0]);
        
        if (choose==0)
            {main.menu();}
        else
            {System.exit(0);}
    }
}
