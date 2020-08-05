import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * GUI for user to play the game
 */
public class Game extends JPanel implements MouseListener{
// INSTANCE VARIABLES
    static boolean started = false, dead = false;
    Toolbar toolbar;
    int tileSize = 25;


// CONSTRUCTORS
    public Game(int r, int c, int m){
        board = new Board(r, c, m);
        toolbar = new Toolbar();

        addMouseListener(this);
    }

// GRAPHICS
    public void paint(Graphics g){
        // Post-death revealing
        for(int i = 0; i < board.rows; i++){
            for(int j = 0; j < board.cols; j++){
                if(board.visible[i][j] == Board.DEATH){
                    g.setColor(BOMB);
                    g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                }
            }
        }

        // Displaying grid information (numbers & Flags)
        for(int i = 0; i < board.rows; i++){
            for(int j = 0; j < board.cols; j++){
                if(board.flags[i][j]){
                    g.setColor(FLAG);
                    g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                }
                else if(board.visible[i][j] == Board.UNSEEN){
                    g.setColor(BLANK);
                    g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                }
                else if(board.visible[i][j] != 0){
                    g.setColor(Color.BLACK);
                    g.setFont(DEFAULT_TEXT);
                    g.drawString(Integer.toString(board.visible[i][j]), i * tileSize, (j + 1) * tileSize);
                }

                if(dead){
                    if(board.mines[i][j]){
                        g.setColor(REVEALED_BOMB);
                        g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                    }
                    if(board.visible[i][j] == Board.DEATH){
                        g.setColor(BOMB);
                        g.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                    }
                }
            }
        }

        // Drawing gridlines
        g.setColor(GRIDLINE);
        for(int i = 0; i <= board.rows * tileSize; i += tileSize){
            g.drawLine(i, 0, i, board.cols * tileSize);
        }
        for(int i = 0; i <= board.cols * tileSize; i += tileSize){
            g.drawLine(0, i, board.rows * tileSize, i);
        }
    }

// MOUSE EVENTS
    public void mouseClicked(MouseEvent e){

    }
    public void mousePressed(MouseEvent e){
        int r = (e.getX()) / tileSize, c = (e.getY()) / tileSize;

        if(dead) return;                            // ignoring commands after death

        if(!started){                               // generate the board on first click
            board.generate(r, c, Board.DEFAULT);
            board.click(r, c);
            started = true;
        }
        else if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK){                                // left click
            System.out.println("CLICK " + r + " " + c);

            if(!board.click(r, c)){                 // flagging square as death square
                board.visible[r][c] = Board.DEATH;
                dead = true;
                toolbar.minesLeft.setText("Game Over!");
            }
        }
        else if(e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK){                                // right click
            System.out.println("FLAG " + r + " " + c);
            board.flag(r, c);
            toolbar.minesLeft.setText(Integer.toString(board.minesLeft));
        }

        if(board.checkWin()){                       // check for win condition
            toolbar.minesLeft.setText("Congratulations!");
            dead = true;
        } else {
            System.out.println("No win");
        }

        launcher.getContentPane().revalidate();
        launcher.getContentPane().repaint();
    }
    public void mouseReleased(MouseEvent e){

    }
    public void mouseEntered(MouseEvent e){

    }
    public void mouseExited(MouseEvent e){

    }


// MAIN TEST
    public static void main(String[] args){
        Game game = new Game(30, 20, 150);

        // Frame settings
        launcher.setTitle("Minesweeper");
        launcher.setVisible(true);
        launcher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        launcher.setLayout(new BorderLayout());
        launcher.setSize(board.rows * game.tileSize + r_offset, board.cols * game.tileSize + c_offset);
        launcher.setResizable(false);
        launcher.setLocationRelativeTo(null);

        launcher.add(game.toolbar, BorderLayout.NORTH);
        launcher.add(game, BorderLayout.CENTER);

//        while(!board.win){
//            launcher.getContentPane().revalidate();
//            launcher.getContentPane().repaint();
//        }
    }

    /**
     * Header bar showing mine count, timer, reset button
     */
    class Toolbar extends JPanel implements ActionListener{
        // Components
        JButton reset = new JButton("Reset");
        JTextField minesLeft = new JTextField(4);
        JTextField timer = new JTextField(6);

        public Toolbar(){
            reset.addActionListener(this);
            minesLeft.setEditable(false);
            minesLeft.setText(Integer.toString(board.minesLeft));
            timer.setEditable(false);
            timer.setHorizontalAlignment(SwingConstants.RIGHT);
            timer.setText("0:00");

            // Layout management
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(minesLeft);
            add(reset);
            add(timer);
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals(reset.getText())){                       // resets the game
                board = new Board(board.rows, board.cols, board.totalMines);
                minesLeft.setText(Integer.toString(board.minesLeft));
                started = false;
                dead = false;

                launcher.getContentPane().revalidate();
                launcher.getContentPane().repaint();
            }
        }
    }

// GLOBAL VARIABLES & CONSTANTS
    static JFrame launcher = new JFrame();
    static Board board;

    static final int r_offset = 17, c_offset = 66;

    static final Font DEFAULT_TEXT = new Font("Arial", Font.BOLD, 16);
    static final Color GRIDLINE = new Color(175, 175, 175);
    static final Color BLANK = new Color(210, 210, 210);
    static final Color FLAG = new Color(255, 0, 0);
    static final Color BOMB = new Color(0, 0, 0);
    static final Color REVEALED_BOMB = new Color(161, 78, 0);
}
