import java.util.*;

public class Board{
// GLOBAL VARIABLES
    static Random rand = new Random();

// INSTANCE VARIABLES
    public int rows, cols, totalMines;
    boolean[][] mines;                                      // basic parameters
    int[][] count;                                          // generated field with surrounding mine numbers

    int startR, startC, minesLeft;
    public boolean[][] flags;                               // U/I & solver parameters
    public int[][] visible;                                 // squares the user can see

// CONSTRUCTORS
    public Board(){

    }
    public Board(int r, int c, int m){
        rows = r; cols = c; totalMines = m; minesLeft = m;

        mines = new boolean[r][c];
        flags = new boolean[r][c];
        count = new int[r][c];
        visible = new int[r][c];

        for(int i = 0; i < r; i++)
            for(int j = 0 ; j < c; j++)
                visible[r][c] = UNSEEN;
    }

// GENERATOR
    public boolean generate(int r, int c, int mode) throws ArrayIndexOutOfBoundsException{
        int row, col;
        minesLeft = totalMines;

        if(mode == DEFAULT){                                                    // default: initial tile must be 0
            if(totalMines > rows * cols - 9)
                return false;                                                   // check for impossible board (too many mines)

            while (minesLeft > 0){
                row = rand.nextInt(rows);
                col = rand.nextInt(cols);

                if(!mines[row][col] && (row!=r-1||col!=c-1) && (row!=r-1||col!=c) && (row!=r-1||col!=c+1) && (row!=r||col!=c-1) && (row!=r||col!=c) && (row!=r||col!=c+1) && (row!=r+1||col!=c-1) && (row!=r-1||col!=c) && (row!=r+1||col!=c+1)){
                    mines[row][col] = true;
                    minesLeft--;
                }
            }
        }
        if(mode == NON_ZERO_OPEN){                                              // initial tile can be non-zero (but not mine)
            while(minesLeft > 0){
                row = rand.nextInt(rows);
                col = rand.nextInt(cols);

                if (!mines[row][col] && (row != r || col != c)) {
                    mines[row][col] = true;
                    minesLeft--;
                }
            }
        }

        minesLeft = totalMines;
        generateCount();
        return true;
    }
    public boolean generate(int r, int c){
        return generate(r, c, DEFAULT);
    }

    void generateCount() throws ArrayIndexOutOfBoundsException{                              // generate the surrounding mine count
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(mines[i][j])
                    count[i][j] = MINE;
                else{
                    for(int[] k : around)
                        if(i + k[0] >= 0 && i + k[0] < rows && j + k[1] >= 0 && j + k[1] < cols)
                            count[i][j] += mines[i + k[0]][j + k[1]] ? 1 : 0;
                }
            }
        }
    }

// USER INTERACTION COMMANDS
    public boolean click(int r, int c) throws ArrayIndexOutOfBoundsException{               // represents a normal (left) click; "solves" a square
        if(mines[r][c]) return false;                           // death: hit a mine

        visible[r][c] = count[r][c];
        if(checkSolved(r, c)){                                  // auto-clearing a solved square
            clickSurround(r, c);                              // disregards death condition
        }
        return true;
    }

    public void flag(int r, int c) throws ArrayIndexOutOfBoundsException{                   // represents an alternate (right) click
        if(flags[r][c]){
            flags[r][c] = false;
            minesLeft++;
        } else if(minesLeft > 0){
            flags[r][c] = true;
            minesLeft--;
        }
    }

    void clickSurround(int r, int c){                                                   // function to perform a task on all surrounding squares
        for(int[] i : around)
            click(r + i[0], c + i[1]);
    }

    boolean checkSolved(int r, int c) throws ArrayIndexOutOfBoundsException{                // checks if a square can be auto-cleared
        if(count[r][c] == 0)
            return true;

        int flagged = 0;
        for(int[] i : around){
            if(flags[r + i[0]][c + i[1]] && mines[r + i[0]][c + i[1]]){
                flagged++;
            }
        }
        return flagged == count[r][c];
    }

    void updateVisible(){                               // potentially not needed
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){

            }
        }
    }

// I/O TEST
    public void print(){
        for(boolean[] i : mines){                               // printing the mines
            for(boolean j : i){
                if(j)
                    System.out.print("*");
                else
                    System.out.print("O");
            }
            System.out.println();
        }

        System.out.println();
        for(int[] i : count){
            for(int j : i)
                System.out.print(j);
            System.out.println();
        }
    }

// MAIN TEST
    public static void main(String[] args){
        Board test = new Board(20, 50, 350);
        test.generate(19, 49);
        test.print();

    }

// CONSTANTS
    public static final int SEE = 1, CLICK = 2;
    public static final int[][] around = { {-1,-1}, {-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}, {1,1} };          // for surround() method

    // Generation modes
    public static final int DEFAULT = 1;                // default: initial square must be 0
    public static final int NON_ZERO_OPEN = 2;          // initial square can be non-zero (but not mine)

    public static final int MINE = 9;                   // value of mines in count[][] array

    public static final int UNSEEN = -1;                // represents an unseen square in the seen[][] array
}
