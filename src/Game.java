import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JFrame implements ActionListener, MouseListener {
// INSTANCE VARIABLES
    Board board;
    boolean started = false;                        // determines if first click (generation) occured

    // Graphics parameters
    JPanel toolbar = new JPanel(), game = new JPanel();
    JButton reset = new JButton("Reset");
    int tileSize = 10;

// CONSTRUCTORS
    public Game(int rows, int cols, int mines){
        board = new Board(rows, cols, mines);
        addMouseListener(this);

        setup();                                // GUI settings
    }
    public Game(){
        this(25, 15, 75);
    }

// GRAPHICS & GUI SETUP
    void setup(){
        // Window setup
        setTitle("Minesweeper");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(toolbar);
        add(game);

        // Panel object setup
        reset.addActionListener(this);
    }

    public void paint(Graphics g){

    }

// MOUSE EVENTS
    public void mouseClicked(MouseEvent e){
        int x = e.getX() / tileSize, y = e.getY() / tileSize;

        if(!started){
            board.generate(x, y);
            started = true;
        }
        if(e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK){                         // left click
            board.click(x, y);
        }
        else if(e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK){                    // right click
            board.flag(x, y);
        }
    }
    public void mousePressed(MouseEvent e){

    }
    public void mouseReleased(MouseEvent e){

    }
    public void mouseEntered(MouseEvent e){

    }
    public void mouseExited(MouseEvent e){

    }

// ACTION EVENTS
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == "Reset"){                                            // resets the game

        }
    }

// MAIN TEST
    public static void main(String[] args){
        Game game = new Game();


    }
}
