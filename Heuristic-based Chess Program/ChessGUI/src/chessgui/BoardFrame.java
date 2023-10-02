package chessgui;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class BoardFrame extends JFrame 
{
    public static BoardFrame bf;
    public static Component component;
    public static JMenuBar mb = new JMenuBar();
    public static JButton info = new JButton();
    public static JButton about = new JButton();
    public static JButton startGame = new JButton();
    public static JButton resetGame = new JButton();
    public static JButton newGame = new JButton();
    public static JButton nextMove = new JButton();
    public static JButton treeVisualization = new JButton();
    
    public BoardFrame()
    {
        bf = this;
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setResizable(false);
        component = new Board();
        setJMenuBar(mb);
        this.add(component, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setTitle("Chess (White's Turn)");
        
    }
}
