import java.io.*;
import java.util.Scanner;
import java.util.Random;

// a class that retreives a random puzzle from a list and tracks the progress of the puzzle
public class Puzzle {
   private String myPuzzle;
   private String myPuzzleCategory;
   private String myPuzzleBlanks;
   private String myPuzzleSolution;
   private String guess;
   private String guessTracker = "";
   private int numLettersRevealed;
   private boolean isGuessCorrect;
   private boolean isPuzzleSolved;
   Keyboard keyboard = new Keyboard();
      
   // method that retrieves a random puzzle from a file and sets the puzzle, category, blanks and solution fields
   public void generatePuzzle(String puzzleFile) throws IOException {
      File listOfPuzzles = new File(puzzleFile);
      while (!listOfPuzzles.exists()) {
         listOfPuzzles = new File(keyboard.getString("A file does not exist at the specified location. Please enter a valid file name"));
      }
      Scanner countLines = new Scanner(listOfPuzzles);
      Scanner readLines = new Scanner(listOfPuzzles);
      Random random = new Random();
      
      // count number of lines in the file and search for header text to determine first line that contains a puzzle
      int totalLinesInFile = 0;
      int firstPuzzleLine = 0;
      String currentLineText = "";
      while (countLines.hasNext()) {
         currentLineText = countLines.nextLine();
         totalLinesInFile++;
         if (currentLineText.equals("CATEGORY|PUZZLE")) {
            firstPuzzleLine = totalLinesInFile + 1;
         }
      }
      countLines.close();
      
      // read a random line from the file, and set my puzzle
      int randomLine = random.nextInt(totalLinesInFile - firstPuzzleLine) + firstPuzzleLine;
      String myLine = "";
      for (int i = 1; i <= randomLine; i++) {
         myLine = readLines.nextLine();
      }
      readLines.close();
      myPuzzle = myLine.substring(myLine.indexOf("|") + 1).toUpperCase(); 
      myPuzzleCategory = myLine.substring(0, myLine.indexOf("|"));
      
      isPuzzleSolved = false;
      myPuzzleBlanks = "";
      myPuzzleSolution = "";
      guessTracker = "";
      boolean lastCharWasSpecial = false;
      
      // convert puzzle to puzzle blanks and solution forms
      for (int i = 0; i < myPuzzle.length(); i++) {
         String currentChar = myPuzzle.substring(i, i + 1);
         String space = " ";
         if (i == 0 || lastCharWasSpecial) {
            space = "";
         }
         switch (currentChar) {
            case "A":
            case "B":
            case "C":
            case "D":
            case "E":
            case "F":
            case "G":
            case "H":
            case "I":
            case "J":
            case "K":
            case "L":
            case "M":
            case "N":
            case "O":
            case "P":
            case "Q":
            case "R":
            case "S":
            case "T":
            case "U":
            case "V":
            case "W":
            case "X":
            case "Y":
            case "Z":
               myPuzzleSolution += space + currentChar;
               currentChar = "_";
               myPuzzleBlanks += space + currentChar;
               lastCharWasSpecial = false;
               break;
            case "-":
               myPuzzleSolution += space + currentChar;
               myPuzzleBlanks += space + currentChar;
               lastCharWasSpecial = false;
               break;
            case " ":
               myPuzzleSolution += "   ";
               myPuzzleBlanks += "   ";
               lastCharWasSpecial = true;
               break;
            default:
               myPuzzleSolution += currentChar;
               myPuzzleBlanks += currentChar;
               lastCharWasSpecial = true;
               break;
         }
      } 
   }

   // method that takes the player's guess and checks it against each character in the solution
   // if it matches, reveal the letter(s) in the puzzleBlanks field
   public void checkGuess() {
      numLettersRevealed = 0;
      boolean attemptedToSolve = false;
      String myPuzzleBlanksUpdated = "";
      
      guess = keyboard.getString("Guess a letter (A-Z) or attempt to solve the puzzle").toUpperCase();
      
      if (guess.length() == 1) {
         // player guesses a single letter
         attemptedToSolve = false;
         for (int i = 0; i < myPuzzleSolution.length(); i++) {
            String currentCharSolution = myPuzzleSolution.substring(i, i + 1);
            String currentCharBlanks = myPuzzleBlanks.substring(i, i + 1);
               
            // if guess matches the current letter in the solution, add that letter to the updated puzzle blanks
            // otherwise, keep the blank character
            if (guess.equals(currentCharSolution)) {
               myPuzzleBlanksUpdated += currentCharSolution;
               numLettersRevealed++;
            } else {
               myPuzzleBlanksUpdated += currentCharBlanks;
            }
         }
      } else {    
         // player attempts to solve (i.e, guesses more than 1 letter)   
         attemptedToSolve = true;
         if (guess.equals(myPuzzle)) {
            myPuzzleBlanksUpdated = myPuzzleSolution;
            numLettersRevealed++;
         } else {
            myPuzzleBlanksUpdated = myPuzzleBlanks;
         }
      }
      myPuzzleBlanks = myPuzzleBlanksUpdated;
      
      if (numLettersRevealed > 0) {
         isGuessCorrect = true;
      } else {
         isGuessCorrect = false;
         guessTracker += " " + guess;
      }
      
      if (myPuzzleBlanks.equals(myPuzzleSolution)) {
         isPuzzleSolved = true;
      } else {
         isPuzzleSolved = false;
      }
   }
   
   public String getMyPuzzle() {
      return myPuzzle;
   }

   public String getMyPuzzleCategory() {
      return myPuzzleCategory;
   }

   public String getMyPuzzleBlanks() {
      return myPuzzleBlanks;
   }

   public String getGuessTracker() {
      return guessTracker;
   }
   
   public int getNumLettersRevealed() {
      return numLettersRevealed;
   }
   
   public boolean getIsGuessCorrect() {
      return isGuessCorrect;
   }
   
   public boolean getIsPuzzleSolved() {
      return isPuzzleSolved;
   }
   
}
