import java.io.*;

// a class that retreives a random puzzle from a list
public class Game {
   private int currentRound = 0;
   private int prizeAward;
   private boolean isGameOver = false;
   private String gameResults = "";
   private String winner = "";
   private boolean playAnotherGame = true;
   private int gameNumber = 1;
   Keyboard keyboard = new Keyboard();

   // method that advances the game through 1 round
   // 1. player spins the wheel
   // 2. player makes a guess (if not bankrupt)
   // 3. check geuss against puzzle, if correct, award prize to player
   // 4. check if game is over
   public void playRound(Puzzle puzzle, Player player, Settings settings, Wheel wheel) {
      printGameStatus(puzzle, player, settings);
      wheel.spinTheWheel();
      currentRound++;
      
      // if bankrupt, skip round; otherwise take player guess and dislay guess results
      int balanceMultiplier = 1;
      if (!wheel.getIsBankrupt()) {
         printGameStatus(puzzle, player, settings);
         puzzle.checkGuess();
         printRoundResults(puzzle, wheel);
      } else {
         balanceMultiplier = 0;
         System.out.println("\nSorry, you lose a turn.");
      }

      // update player balance
      player.updateCurrentBalance(prizeAward, balanceMultiplier);
      System.out.format("Your current balance is: $%,d%n", player.getCurrentBalance());
      setIsGameOver(puzzle, settings);
   }
   
   // method that prints the current status of the game (puzzle status, player balance, rounds remaining, etc.)
   private void printGameStatus(Puzzle puzzle, Player player, Settings settings) {
      System.out.println();
      System.out.println("====================================================================================================");
      System.out.println("Category: " + puzzle.getMyPuzzleCategory());
      System.out.println();
      System.out.println(puzzle.getMyPuzzleBlanks());
      System.out.println();
      System.out.println("Player: " + player.getName());
      System.out.format("Balance: $%,d%n", player.getCurrentBalance());
      System.out.println("Number of spins remaining: " + (settings.getMaxNumRounds() - currentRound));
      System.out.println("Previous incorrect guesses: " + puzzle.getGuessTracker());
   }

   // method that prints the results of the player's guess and calculates the prize to be awarded
   private void printRoundResults(Puzzle puzzle, Wheel wheel) {
      System.out.println();
      System.out.println("====================================================================================================");
      if (puzzle.getIsGuessCorrect()) {
         prizeAward = puzzle.getNumLettersRevealed() * wheel.getPrizeValue();
         System.out.println("Correct! There are " + puzzle.getNumLettersRevealed() + " of those.");
         System.out.format("You just won $%,d%n", prizeAward);
      } else {
         prizeAward = 0;
         System.out.println("Sorry, there aren't any of those.");
         System.out.println("You did not win anything this round.");
      }
   }
   
   // method that checks if game has ended (by a player winning or by the max number of rounds expiring)
   private void setIsGameOver(Puzzle puzzle, Settings settings) {
      int numRoundsRemaining = settings.getMaxNumRounds() - currentRound;
      if (puzzle.getIsPuzzleSolved() || numRoundsRemaining == 0) {
         isGameOver = true;
         currentRound = 0;
      } else {
         isGameOver = false;
      }
   }

   // method that closes out the game, prints the results and saves game date to file
   public void gameOverProcedures(Puzzle puzzle, Player player, Settings settings) throws IOException {
      printGameOverMessage(puzzle, player);
      printGameResults(puzzle, settings);
      saveGameResults();
      gameResults = "";
      gameNumber++;
   }
   
   // method that prints the outcome of the game
   private void printGameOverMessage(Puzzle puzzle, Player player) {
      System.out.println();
      System.out.println("====================================================================================================");

      if (puzzle.getIsPuzzleSolved()) {
         player.setNumGamesWon();
         winner = player.getName();
         System.out.println(winner + ", you win!");
         System.out.format("Your total winnings are: $%,d%n", player.getCurrentBalance());
      } else {
         winner = "n/a";
         System.out.println("You're out of spins! Sorry, better luck next time.");
      }
   }
   
   private void printGameResults(Puzzle puzzle, Settings settings) {
      gameResults += "\nGame " + gameNumber + " Results: ";
      gameResults += "\nCategory: " + puzzle.getMyPuzzleCategory();
      gameResults += "\nSolution: " + puzzle.getMyPuzzle();
      gameResults += "\nWinner: " + winner;
      gameResults += "\n\n";
      for (int i = 0; i < settings.getNumPlayers(); i++) {
         settings.getListOfPlayers()[i].updateRunningBalance();
         gameResults += settings.getListOfPlayers()[i].getName();
         gameResults += String.format(" Cash Won: $%,d", settings.getListOfPlayers()[i].getCurrentBalance());
         gameResults += String.format("; Total Balance: $%,d", settings.getListOfPlayers()[i].getRunningBalance());
         gameResults += "; Games Won: " + settings.getListOfPlayers()[i].getNumGamesWon();
         gameResults += "\n";
         settings.getListOfPlayers()[i].resetCurrentBalance();
      }
      System.out.println(gameResults);
   }

   // method that saves the game's results to a file
   private void saveGameResults() throws IOException {
      FileWriter outputFile = new FileWriter("GameResults.txt", true);
      outputFile.write("\n" + gameResults);
      outputFile.close();
   }

   // method that asks player if they would like to play another game
   public boolean setPlayAnotherGame() {
      playAnotherGame = keyboard.getYesOrNo("Would you like to play another game?");
      if (playAnotherGame) {
         isGameOver = false;
      }
      return playAnotherGame;
   }
   
   public boolean getIsGameOver() {
      return isGameOver;
   }
}