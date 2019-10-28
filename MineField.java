// Name:Tianchen Zhang
// USC NetID:tzhang03
// CS 455 PA3
// Fall 2019



/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
public class MineField {
   
   // <put instance variables here>
   private boolean mineData[][];
   private int numMines;
   private boolean flag; //A flag determines which ofthe constructors is called;  True iff MineField(boolean[][] mineData) vise versa.
   
   /**
      Create a minefield with same dimensions as the given array, and populate it with the mines in the array
      such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
      this minefield will corresponds to the number of 'true' values in mineData.
    * @param mineData  the data for the mines; must have at least one row and one col.
    */
   public MineField(boolean[][] mineData) {
       flag = true;
       //Make a copy of the 2D Array(mineData) and passed into the mineData in this class;
       this.mineData = new boolean[mineData.length][];
       for(int i=0; i<mineData.length;i++)
       {
           this.mineData[i] = new boolean[mineData[i].length];
           for(int j=0; j<mineData[i].length;j++)
           {
               this.mineData[i][j] = mineData[i][j];
           }
       }
     //  this.mineData= mineData;

       //Count numMines in the mineData;
       for(int i=0; i<mineData.length;i++)
       {
           for(int j=0; j<mineData[i].length;j++)
           {
               if(mineData[i][j]==true)
               {
                   numMines++;
               }
           }
       }


   }

   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
       flag = false;
       this.mineData = new boolean[numRows][numCols];
       this.numMines = numMines;


   }
   

   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col)
    */
   public void populateMineField(int row, int col) {
       // Removes any current mines on the minefield
       if(flag) return;
       for(int i=0; i<mineData.length;i++)
       {
           for(int j=0; j<mineData[i].length;j++)
           {
               mineData[i][j] = false;
           }
       }

       //puts numMines() mines in random locations on the minefield
       int count = numMines;
       Random rand = new Random();
       while(count>0&&mineData[row][col]==false)
       {

           int row_num = rand.nextInt(mineData.length); //Pick a random row from the mineData set
           int col_num = rand.nextInt(mineData[row_num].length);// pick a random column from the mineData set
           while(row_num==row&&col_num==col) //Re-generate random row and col if prompt to populate mine at first click location
           {
               row_num = rand.nextInt(mineData.length);
               col_num = rand.nextInt(mineData[row_num].length);
           }
               mineData[row_num][col_num] = true;
               count--;

       }

   }
   
   
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state the minefield is in at the beginning of a game.
    */
   public void resetEmpty() {
       for(int i=0; i<mineData.length;i++)
       {
           for(int j=0; j<mineData[i].length;j++)
           {
               mineData[i][j] = false;
           }
       }
   }

   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
   public int numAdjacentMines(int row, int col) {
      int count = 0;
          if(inRange(row-1,col) && hasMine(row-1,col)) count++; //up
          if(inRange(row+1,col) &&hasMine(row+1,col)) count++; //down
          if(inRange(row,col-1) && hasMine(row,col-1)) count++; //left
          if(inRange(row,col+1) &&hasMine(row,col+1)) count++; //right
          if(inRange(row+1,col+1)&& hasMine(row+1,col+1)) count++; //down-right
          if(inRange(row+1,col-1) &&hasMine(row+1,col-1)) count++; //down-left
          if(inRange(row-1,col+1)&& hasMine(row-1,col+1)) count++; //up-right
          if(inRange(row-1,col-1)&& hasMine(row-1,col-1)) count++; //up-left
       return count;
   }
   
   
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
      if(row>=0 && row<mineData.length && col>=0 && col<mineData[0].length)   //Minedata parameter checking
      {
          return true;
      }else return false;

   }
   
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
      return mineData.length;
   }
   
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
      return mineData[0].length;  //A Square thus return any row size;
   }
   
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      if(mineData[row][col]==true) return true;
      else return false;
   }
   
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
      return numMines;       // DUMMY CODE so skeleton compiles
   }

   
         
}

