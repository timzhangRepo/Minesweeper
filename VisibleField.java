// Name:Tianchen Zhang
// USC NetID:tzhang03
// CS 455 PA3
// Fall 2019


import java.awt.image.VolatileImage;
import java.sql.SQLOutput;

/**
  VisibleField class
  This is the data that's being displayed at any one point in the game (i.e., visible field, because it's what the
  user can see about the minefield), Client can call getStatus(row, col) for any square.
  It actually has data about the whole current state of the game, including  
  the underlying mine field (getMineField()).  Other accessors related to game status: numMinesLeft(), isGameOver().
  It also has mutators related to actions the player could do (resetGameDisplay(), cycleGuess(), uncover()),
  and changes the game state accordingly.
  
  It, along with the MineField (accessible in mineField instance variable), forms
  the Model for the game application, whereas GameBoardPanel is the View and Controller, in the MVC design pattern.
  It contains the MineField that it's partially displaying.  That MineField can be accessed (or modified) from 
  outside this class via the getMineField accessor.  
 */
public class VisibleField {
   // ----------------------------------------------------------   
   // The following public constants (plus numbers mentioned in comments below) are the possible states of one
   // location (a "square") in the visible field (all are values that can be returned by public method 
   // getStatus(row, col)).
   
   // Covered states (all negative values):
   public static final int COVERED = -1;   // initial value of all squares
   public static final int MINE_GUESS = -2;
   public static final int QUESTION = -3;

   // Uncovered states (all non-negative values):

   
   // values in the range [0,8] corresponds to number of mines adjacent to this square
   
   public static final int MINE = 9;      // this loc is a mine that hasn't been guessed already (end of losing game)
   public static final int INCORRECT_GUESS = 10;  // is displayed a specific way at the end of losing game
   public static final int EXPLODED_MINE = 11;   // the one you uncovered by mistake (that caused you to lose)
   // ----------------------------------------------------------   
  
   // <put instance variables here>
   public static final int UNCOVERED = 0;
   private  int[][] VisibleField;
   private  MineField mineField;
   private boolean GameOver = false; //Variable to determine if a game is over. True when uncover a mine;


   /**
      Create a visible field that has the given underlying mineField.
      The initial state will have all the mines covered up, no mines guessed, and the game
      not over.
      @param mineField  the minefield to use for for this VisibleField
    */
   public VisibleField(MineField mineField) {
         this.mineField = mineField;

         //Update VisibleField with mineField config.
         VisibleField = new int[mineField.numRows()][mineField.numCols()];
         for(int i=0; i<VisibleField.length; i++)
         {
            for(int j=0; j<VisibleField[i].length;j++)
            {
               VisibleField[i][j] = COVERED;
            }
         }

   }
   
   
   /**
      Reset the object to its initial state (see constructor comments), using the same underlying
      MineField. 
   */     
   public void resetGameDisplay() {
      GameOver = false;
      for(int i=0; i<VisibleField.length; i++)
      {
         for(int j=0; j<VisibleField[i].length;j++)
         {
            VisibleField[i][j] = COVERED;
         }
      }
   }

   
   /**
      Returns a reference to the mineField that this VisibleField "covers"
      @return the minefield
    */
   public MineField getMineField() {

      return mineField;       // DUMMY CODE so skeleton compiles
   }
   
   
   /**
      Returns the visible status of the square indicated.
      @param row  row of the square
      @param col  col of the square
      @return the status of the square at location (row, col).  See the public constants at the beginning of the class
      for the possible values that may be returned, and their meanings.
      PRE: getMineField().inRange(row, col)
    */
   public int getStatus(int row, int col) {
      if(VisibleField[row][col]==UNCOVERED)
      {
         return mineField.numAdjacentMines(row, col);
      }
      return VisibleField[row][col];

   }

   
   /**
      Returns the the number of mines left to guess.  This has nothing to do with whether the mines guessed are correct
         or not.  Just gives the user an indication of how many more mines the user might want to guess.  This value can
      be negative, if they have guessed more than the number of mines in the minefield.     
      @return the number of mines left to guess.
    */
   public int numMinesLeft() {
      int numMines = mineField.numMines();
      for(int i=0; i<VisibleField.length; i++)
      {
         for(int j=0; j<VisibleField[i].length;j++)
         {
            if(VisibleField[i][j] == MINE_GUESS)
            {
               numMines--;
            }
         }
      }
      return numMines;
   }
   
   /**
      Cycles through covered states for a square, updating number of guesses as necessary.  Call on a COVERED square
      changes its status to MINE_GUESS; call on a MINE_GUESS square changes it to QUESTION;  call on a QUESTION square
      changes it to COVERED again; call on an uncovered square has no effect.  
      @param row  row of the square
      @param col  col of the square
      PRE: getMineField().inRange(row, col)
    */
   public void cycleGuess(int row, int col) {

      if(VisibleField[row][col]==COVERED)
      {
         VisibleField[row][col]=MINE_GUESS;
      }
      else if(VisibleField[row][col]==MINE_GUESS){

         VisibleField[row][col]=QUESTION;
      }
      else if(VisibleField[row][col]==QUESTION) {
         VisibleField[row][col] = COVERED;
      }
   }

   
   /**
      Uncovers this square and returns false iff you uncover a mine here.
      If the square wasn't a mine or adjacent to a mine it also uncovers all the squares in 
      the neighboring area that are also not next to any mines, possibly uncovering a large region.
      Any mine-adjacent squares you reach will also be uncovered, and form 
      (possibly along with parts of the edge of the whole field) the boundary of this region.
      Does not uncover, or keep searching through, squares that have the status MINE_GUESS. 
      Note: this action may cause the game to end: either in a win (opened all the non-mine squares)
      or a loss (opened a mine).
      @param row  of the square
      @param col  of the square
      @return false   iff you uncover a mine at (row, col)
      PRE: getMineField().inRange(row, col)
    */

   public boolean uncover(int row, int col) {
      if(mineField.hasMine(row,col))
      {
         for(int i=0; i<VisibleField.length; i++)
         {
            for(int j=0; j<VisibleField[i].length;j++)
            {
               if(mineField.hasMine(i,j))
               {
                  if(VisibleField[i][j]==COVERED)
                  {
                     VisibleField[i][j] = MINE;
                  }
               }
               else
               {
                  if(VisibleField[i][j]==MINE_GUESS)
                  {
                     VisibleField[i][j]=INCORRECT_GUESS;
                  }
               }

            }
         }
         VisibleField[row][col] = EXPLODED_MINE;
         GameOver = true;
         return false;
      }
      else
      {
         unCoverAdjacent(row,col);
         return true;
      }


   }
 
   
   /**
      Returns whether the game is over.
      (Note: This is not a mutator.)
      @return whether game over
    */
   public boolean isGameOver() {
      int count = 0; //count the number of uncovered sqaure
      for(int i=0; i<VisibleField.length; i++)
      {
         for(int j=0; j<VisibleField[i].length;j++)
         {
            if(VisibleField[i][j]==UNCOVERED) count++;
         }
      }
      int total_Sqaure = VisibleField[0].length*VisibleField.length; //Total number of squares

      if(total_Sqaure-count==mineField.numMines())  //Total_Square is-count to get the total of uncovered squares left in the game.
     {
        for(int i=0; i<VisibleField.length; i++)
        {
           for(int j=0; j<VisibleField[i].length;j++)
           {
              if(mineField.hasMine(i,j))
              {
                 VisibleField[i][j] = MINE_GUESS;
              }
           }
        }
        GameOver = true;
     }
      return GameOver;
   }
   /**
      Returns whether this square has been uncovered.  (i.e., is in any one of the uncovered states, 
      vs. any one of the covered states).
      @param row of the square
      @param col of the square
      @return whether the square is uncovered
      PRE: getMineField().inRange(row, col)
    */
   public boolean isUncovered(int row, int col) {
      if(VisibleField[row][col]==UNCOVERED) return true;
      else return false;
   }
   
 
   // <put private methods here>

   /*
   Recursively find and uncover adjacent location (8 Direction)
   Each recursion represents a different direction, stops until
   discover a mine at adjacent location in any of the 8 directions.
   @param row of the square
   @param col of the square
   Runs iff pre-conditions are met before starting the recursion.
   Else breakout the recursion.
    */
   private void unCoverAdjacent(int row, int col)
   {

      if(notInRange(row,col))return;
      if(VisibleField[row][col]==UNCOVERED || VisibleField[row][col]==MINE_GUESS) return;
      if(VisibleField[row][col]==MINE_GUESS) return;
      if (mineField.hasMine(row,col))return;
      if(isGameOver())return;  //Check if game is over
      if (mineField.numAdjacentMines(row,col)>0)
      {

         VisibleField[row][col] = UNCOVERED;
         getStatus(row,col);
         return;
      }
      VisibleField[row][col] = UNCOVERED;
      unCoverAdjacent(row-1,col); //deep search up
      unCoverAdjacent(row+1,col);//~down
      unCoverAdjacent(row,col-1) ;//~left
      unCoverAdjacent(row,col+1) ; //~right
      unCoverAdjacent(row+1,col+1) ;//~down-right
      unCoverAdjacent(row+1,col-1);//~down-left
      unCoverAdjacent(row-1,col+1); //~up-right
      unCoverAdjacent(row-1,col-1); //~up-left

   }

   /*
   Returns true iff both row and col are NOT in the range
   of the VisibleField 2D Array. Else return true.
   @param row of the square
   @param col of the square
    */
   private boolean notInRange(int row, int col) {
      if(row>=0 && row<VisibleField.length){
         if(col>=0 && col<VisibleField[row].length) return false;
         else return true;
      }else return true;
   }
}
