package chessgui;
import static chessgui.BoardFrame.bf;
import static chessgui.BoardFrame.mb;
import static chessgui.BoardFrame.info;
import static chessgui.BoardFrame.about;
import static chessgui.BoardFrame.startGame;
import static chessgui.BoardFrame.resetGame;
import static chessgui.BoardFrame.newGame;
import static chessgui.BoardFrame.nextMove;
import static chessgui.BoardFrame.treeVisualization;
import chessgui.pieces.*;
import static chessgui.pieces.positions.bKingProtected;
import static chessgui.pieces.positions.piecePositions;
import static chessgui.pieces.positions.wRookProtected;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;
//usb
@SuppressWarnings("serial")
public class Board extends JPanel implements positions, ActionListener, Runnable
{
    public int turnCounter = 0; // Value which specifies whether it is black's or white's turn
    private static Image NULL_IMAGE = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB); // Something important related to initialization of chess board
    
    public static boolean checkmateFlag = false; 
    public static boolean stalemateFlag = false;
    public static boolean repetitionFlag = false;
    public static boolean rookCaptureFlag = false;
    public static boolean fiftyMoveFlag = false;
    
    public static boolean isBlocked;
    public static boolean afterblock;

    public static Integer steps;

    public static Integer globalCounter = 0;
    public static String initEnum;
    public static Integer bestMoveID;
    public static Integer highlightCounter = -1;
    public static Integer idCounter = 0;
    public static Integer levelCounter = 0;
    public static Integer blackMoveCounter = 0;
    
    public static JButton next = new JButton("Next Highlighted Node");
    public static JButton prev = new JButton("Previous Highlighted Node");
    
    public static Diagram d;
    
    public static Integer spWidth;
    public static Integer spHeight;
    
    public static int initialWx; // The "initial" variables are used to determine the initial coordinates of chess pieces. This changes every time "new game" button is clicked
    public static int initialWy;
    public static int initialRx;
    public static int initialRy;
    public static int initialBx;
    public static int initialBy;
    
    public static int[] moveCounter = new int[262144];
    
    public static Integer enumer = 0; // Value which represents the enumeration of the state of the chess board
    public static Integer posValue = 0; // Value which represents the positional value (calculated through a formula) of all chess pieces on the board

    private final int Square_Width = 65; // Something important related to initialization of chess board
    public ArrayList<Piece> White_Pieces; // Something important related to initialization of chess board
    public ArrayList<Piece> Black_Pieces; // Something important related to initialization of chess board
    
    public ArrayList<DrawingShape> Static_Shapes; // Something important related to initialization of chess board
    public ArrayList<DrawingShape> Piece_Graphics; // Something important related to initialization of chess board

    public Piece Active_Piece; // Something important related to initialization of chess board

    private final int rows = 8; // Something important related to initialization of chess board
    private final int cols = 8; // Something important related to initialization of chess board
    private Integer[][] BoardGrid; // Something important related to initialization of chess board
    private String board_file_path = "images" + File.separator + "board.png"; // Something important related to initialization of chess board
    private String active_square_file_path = "images" + File.separator + "active_square.png"; // Something important related to initialization of chess board

    public void initGrid() // O(N)
    {
        for (int i = 0; i < rows; i++) // O(1)
        {
            for (int j = 0; j < cols; j++)
            {
                BoardGrid[i][j] = 0;
            }
        }
        
        Integer wx, wy, bx, by, rx, ry; // O(1)
        boolean isLegal = false; // O(1)
        
        bx = getRandomInteger(7, 0); // O(1)
        by = getRandomInteger(7, 0); // O(1)
        bKingReplace(bx, by); // O(1)
        
        wx = 0; // O(1)
        wy = 0; // O(1)
        rx = 0; // O(1)
        ry = 0; // O(1)
        
        while(isLegal==false) // O(N)
        {
            wx = getRandomInteger(7, 0); // O(1)
            wy = getRandomInteger(7, 0); // O(1)
            
            isLegal = randomKingTest(bx, by, wx, wy); // O(N)
        }
        
        wKingReplace(wx, wy); // O(1)
        isLegal = false; // O(1)
        
        while(isLegal==false) // O(N)
        {
            rx = getRandomInteger(7, 0); // O(1)
            ry = getRandomInteger(7, 0); // O(1)
            
            isLegal = randomRookTest(bx, by, wx, wy, rx, ry); // O(N)
        }
        
        wRookReplace(rx, ry, wx, wy);

        White_Pieces.add(new King(wx,wy,true,"wk.png",this)); // O(N)
        White_Pieces.add(new Rook(rx,ry,true,"wr.png",this)); // O(N)
        Black_Pieces.add(new King(bx,by,false,"bk.png",this)); // O(N)
        
        initialWx = wx; // O(1)
        initialWy = wy; // O(1)
        initialRx = rx; // O(1)
        initialRy = ry; // O(1)
        initialBx = bx; // O(1)
        initialBy = by; // O(1)
        
        isBlocked = isBlocked(wx, wy, rx, ry, bx, by); // O(N)
    }

    public Board()  // O(J) + O(N)
    {
        BoardGrid = new Integer[rows][cols]; // O(1)
        Static_Shapes = new ArrayList(); // O(1)
        Piece_Graphics = new ArrayList(); // O(1)
        White_Pieces = new ArrayList(); // O(1)
        Black_Pieces = new ArrayList(); // O(1)

        newGame = new JButton("New");
        startGame = new JButton("Start");
        resetGame = new JButton("Reset");
        info = new JButton("Read Me!"); // O(1)
        about = new JButton("About"); // O(1)
        nextMove = new JButton("Next");
        treeVisualization = new JButton("Tree");
        
        mb.add(info);
        mb.add(newGame);
        mb.add(startGame);
        mb.add(resetGame);
        mb.add(nextMove); // O(J)
        mb.add(about); // O(J) 
        mb.add(treeVisualization);
        
        newGame.addActionListener(this);
        startGame.addActionListener(this);
        resetGame.addActionListener(this);
        info.addActionListener(this); // O(J)
        about.addActionListener(this); // O(J)
        nextMove.addActionListener(this);
        treeVisualization.addActionListener(this);
        
        resetGame.setEnabled(false);
        treeVisualization.setEnabled(false);
        nextMove.setEnabled(false);

        initGrid(); // O(N)
        
        this.setBackground(new Color(37,13,84)); // O(J)
        this.setPreferredSize(new Dimension(520, 520)); // O(J)
        this.setMinimumSize(new Dimension(100, 100)); // O(J)
        this.setMaximumSize(new Dimension(1000, 1000)); // O(J)

        this.setVisible(true); // O(J)
        this.requestFocus(); // O(J)
        drawBoard(); // O(J) + O(N)
    }

    private void drawBoard() // O(J) + O(N)
    {
        Piece_Graphics.clear(); // O(N)
        Static_Shapes.clear(); // O(N)
        
        int whiteSize = White_Pieces.size(); // O(1)
        int blackSize = Black_Pieces.size(); // O(1)

        Image board = loadImage(board_file_path); // O(J)
        Static_Shapes.add(new DrawingImage(board, new Rectangle2D.Double(0, 0, board.getWidth(null), board.getHeight(null)))); // O(1)
        
        for (int i=0;i<whiteSize;i++) // O(J)
        {
            int COL = White_Pieces.get(i).getX(); // O(1)
            int ROW = White_Pieces.get(i).getY(); // O(1)
            Image piece = loadImage("images" + File.separator + "white_pieces" + File.separator + White_Pieces.get(i).getFilePath());  // O(J) 
            Piece_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL+10.75,Square_Width*ROW+10.75, piece.getWidth(null), piece.getHeight(null)))); // O(J)
        }
        for (int i=0;i<blackSize;i++) // O(J)
        {
            int COL = Black_Pieces.get(i).getX();
            int ROW = Black_Pieces.get(i).getY();
            Image piece = loadImage("images" + File.separator + "black_pieces" + File.separator + Black_Pieces.get(i).getFilePath());  
            Piece_Graphics.add(new DrawingImage(piece, new Rectangle2D.Double(Square_Width*COL+10.75,Square_Width*ROW+10.75, piece.getWidth(null), piece.getHeight(null))));
        }
        this.repaint(); // O(J)
    }

    
    public Piece getPiece(int x, int y) // O(1)
    {
        for (Piece p : White_Pieces)
        {
            if (p.getX() == x && p.getY() == y)
            {
                return p;
            }
        }
        for (Piece p : Black_Pieces)
        {
            if (p.getX() == x && p.getY() == y)
            {
                return p;
            }
        }
        return null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==info) // O(J)
        {
            JOptionPane.showMessageDialog(bf, " Click \"New\" to generate a new random position on the chess board and choose the number of steps for Minimax for White and Maximin for Black. \n Click \"Start\" to have White make its first move.\n Click \"Reset\" to reset the game to its initial position. \n Click \"Next\" to execute the next move by the side having the turn to move. White moves first.\n Click \"About\" to find out more about the creators of this program. \n Click \"Tree\" to view the tree of Minimax for White or Maximin for Black depending on the side which has just made the move. \n The goal of White is to deliver checkmate and the goal of Black is to achieve a draw.\n Standard chess rules apply. \n Black can achieve draw by 3 move repetition, stalemate, the 50 move draw rule, or capturing the White rook. \n", "Information", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(e.getSource()==about) // O(J)
        {
            JOptionPane.showMessageDialog(bf, " Made by Filemon Jankuloski and Adrijan Bozinovski.\n Tested by Toni Jankuloski. \n University American College Skopje.\n School of Computer Science and Information Technology. \n 2023", "About", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(e.getSource()==newGame) // O(J) + O(N)
        {
            for(int i=0;i<262144;i++)
            {
                moveCounter[i] = 0;
            }
            turnCounter = 0;
            bKingProtected.clear();
            wKingProtected.clear();
            wRookProtected.clear();
            Integer wx, wy, bx, by, rx, ry; // O(1)
            /*boolean isLegal = false; // O(1) COMMENT STARTS HERE 

            bx = getRandomInteger(7, 0); // O(1)
            by = getRandomInteger(7, 0); // O(1)
            bKingReplace(bx, by); // O(1)

            wx = 0; // O(1)
            wy = 0; // O(1)
            rx = 0; // O(1)
            ry = 0; // O(1)

            while(isLegal==false) // O(N)
            {
                wx = getRandomInteger(7, 0); // O(1)
                wy = getRandomInteger(7, 0); // O(1)
                isLegal = randomKingTest(bx, by, wx, wy); // O(N)
            }

            wKingReplace(wx, wy); // O(1)
            isLegal = false; // O(1)

            while(isLegal==false) // O(N)
            {
                rx = getRandomInteger(7, 0); // O(1)
                ry = getRandomInteger(7, 0); // O(1)

                isLegal = randomRookTest(bx, by, wx, wy, rx, ry); // O(N)
            }

            wRookReplace(rx, ry, wx, wy);
            
            White_Pieces.get(0).setX(wx);
            White_Pieces.get(0).setY(wy);
            White_Pieces.get(1).setX(rx);
            White_Pieces.get(1).setY(ry);
            Black_Pieces.get(0).setX(bx);
            Black_Pieces.get(0).setY(by);*/
            
            White_Pieces.get(0).setX(0); // WHITE KING.... COMMENT FROM HERE
            White_Pieces.get(0).setY(6);
            White_Pieces.get(1).setX(0); // WHITE ROOK
            White_Pieces.get(1).setY(1);
            Black_Pieces.get(0).setX(5); // BLACK KING
            Black_Pieces.get(0).setY(6);
            
            wx = White_Pieces.get(0).getX();
            wy = White_Pieces.get(0).getY();
            rx = White_Pieces.get(1).getX();
            ry = White_Pieces.get(1).getY();
            bx = Black_Pieces.get(0).getX();
            by = Black_Pieces.get(0).getY();
            
            bKingReplace(bx, by);
            wKingReplace(wx, wy);
            wRookReplace(rx, ry, wx, wy);// TO HERE
                    
            initialWx = wx; // O(1)
            initialWy = wy; // O(1)
            initialRx = rx; // O(1)
            initialRy = ry; // O(1)
            initialBx = bx; // O(1)
            initialBy = by; // O(1)*/ 

            isBlocked = isBlocked(wx, wy, rx, ry, bx, by); // O(N)
            checkmateFlag = false;
            resetGame.setEnabled(false); // O(J)
            startGame.setEnabled(true);
            nextMove.setEnabled(false);
            treeVisualization.setEnabled(false);
            drawBoard();
        }
        else if(e.getSource()==startGame)
        {
            Object[] stepNumbers = {"One", "Two"};
            steps = JOptionPane.showOptionDialog(null, "Please select number of steps for White and Black.", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, stepNumbers, stepNumbers[0]);
            piecePositions[0] = Black_Pieces.get(0).getX() * 8 + Black_Pieces.get(0).getY();
            piecePositions[1] = White_Pieces.get(1).getX() * 8 + White_Pieces.get(1).getY();
            piecePositions[2] = White_Pieces.get(0).getX() * 8 + White_Pieces.get(0).getY();
            
            Integer initValue = positionEvaluator(White_Pieces.get(0).getX(), White_Pieces.get(0).getY(), White_Pieces.get(1).getX(), White_Pieces.get(1).getY(), Black_Pieces.get(0).getX(), Black_Pieces.get(0).getY());

            initEnum = initValue+"k"+Black_Pieces.get(0).getX()+Black_Pieces.get(0).getY()+"R"+White_Pieces.get(1).getX()+White_Pieces.get(1).getY()+"K"+White_Pieces.get(0).getX()+White_Pieces.get(0).getY()+";";
            
            checkmateFlag = false; 
            stalemateFlag = false;
            repetitionFlag = false;
            rookCaptureFlag = false;
            fiftyMoveFlag = false;
    
            Integer enumeration = positionEnum(piecePositions[0], piecePositions[1], piecePositions[2]);
        
            if(turnCounter==0)
            {
                whiteMinimax(enumeration);
            }
            
            resetGame.setEnabled(true);
            startGame.setEnabled(false);
            treeVisualization.setEnabled(true);
            nextMove.setEnabled(true);
        }
        else if(e.getSource()==nextMove)
        {
            piecePositions[0] = Black_Pieces.get(0).getX() * 8 + Black_Pieces.get(0).getY();
            piecePositions[1] = White_Pieces.get(1).getX() * 8 + White_Pieces.get(1).getY();
            piecePositions[2] = White_Pieces.get(0).getX() * 8 + White_Pieces.get(0).getY();
            
            Integer initValue = positionEvaluator(White_Pieces.get(0).getX(), White_Pieces.get(0).getY(), White_Pieces.get(1).getX(), White_Pieces.get(1).getY(), Black_Pieces.get(0).getX(), Black_Pieces.get(0).getY());

            initEnum = initValue+"k"+Black_Pieces.get(0).getX()+Black_Pieces.get(0).getY()+"R"+White_Pieces.get(1).getX()+White_Pieces.get(1).getY()+"K"+White_Pieces.get(0).getX()+White_Pieces.get(0).getY()+";";
            
            checkmateFlag = false; 
            stalemateFlag = false;
            repetitionFlag = false;
            rookCaptureFlag = false;
            fiftyMoveFlag = false;
            
            Integer enumeration = positionEnum(piecePositions[0], piecePositions[1], piecePositions[2]);  
            
            if(turnCounter%2==0)
            {
                whiteMinimax(enumeration);
            }
            else
            {
                blackMaximin(enumeration);
            }
        }
        else if(e.getSource()==resetGame)
        {
            for(int i=0;i<262144;i++)
            {
                moveCounter[i] = 0;
            }
            turnCounter = 0;
            
            White_Pieces.get(0).setX(initialWx);
            White_Pieces.get(0).setY(initialWy);
            White_Pieces.get(1).setX(initialRx);
            White_Pieces.get(1).setY(initialRy);
            Black_Pieces.get(0).setX(initialBx);
            Black_Pieces.get(0).setY(initialBy);

            drawBoard();
            
            wRookProtected.clear();
            wKingProtected.clear();
            bKingProtected.clear();
            
            bKingReplace(initialBx, initialBy);
            wKingReplace(initialWx, initialWy); // O(1)
            wRookReplace(initialRx, initialRy, initialWx, initialWy);
            
            checkmateFlag = false;
            resetGame.setEnabled(false);
            startGame.setEnabled(true);
        }
        if(e.getSource()==treeVisualization)
        {
            if(steps == 0)
            {
                spWidth = 19500;
                spHeight = 800;
            }
            else if(steps == 1)
            {
                spWidth = 2030000;
                spHeight = 200000;
            }
            
            run();
        }
    }

    @Override
    public void run() 
    {
        EventQueue.invokeLater(() -> 
        {
            JFrame f = new JFrame("Tree Diagram");
            if(turnCounter%2==1)
            {
                f.setTitle("Tree Diagram (White's Turn)");
            }
            else
            {
                f.setTitle("Tree Diagram (Black's Turn)");
            }
            
            JMenuBar dmb = new JMenuBar();

            dmb.add(next);
            dmb.add(prev);

            f.setJMenuBar(dmb);
            
            f.setDefaultCloseOperation(f.DISPOSE_ON_CLOSE);
            
            f.getContentPane().add(makeDiagram());
            next.addActionListener(d);
            prev.addActionListener(d);
            f.setSize(1600, 800);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
    
    public JComponent makeDiagram()
    {
        d = new Diagram(new ViewModel(new DataModel()));

        MouseAdapter mouseAdapter = new CustomKeyListener();
        d.addMouseListener(mouseAdapter);

        return new JScrollPane(d);
    }
    
    public void whiteMinimax(Integer enumeration)
    {
        vNodes.clear();
        vLevelWidth.clear();
        originCount.clear();
        depth.clear();
        potentMoveSize.clear();
        
        for(int k=0;k<=steps;k++)
        {
            levelCounter++;
            
            if(blackNodes.isEmpty())
            {
                allWhiteMoves(enumeration, -1);
                originCount.add(globalCounter);
            }
            else
            {
                whiteNodes.clear();
                for(int i=0;i<blackNodes.size();i++)
                {
                    if(blackNodes.get(i).getNodeType()=='0')
                    {
                        allWhiteMoves(blackNodes.get(i).getEnum(), blackNodes.get(i).getOrigin());
                        originCount.add(globalCounter);
                    }
                    else
                    {
                        whiteNodes.add(new Node(-1, blackNodes.get(i).getValue(), blackNodes.get(i).getOrigin(), '6'));
                        
                        potentMoveSize.add(1);
                        originCount.add(1);
                        depth.add(1);
                    }
                }
            }
            
            vNodes.addAll(whiteNodes);
            
            depth.add(-1);

            blackNodes.clear();
            levelCounter++;
            
            for(int i=0;i<whiteNodes.size();i++)
            {
                if(k==0)
                {
                    if(whiteNodes.get(i).getNodeType()=='0')
                    {
                        allBlackMoves(whiteNodes.get(i).getEnum(), whiteNodes.get(i).getEnum());
                        originCount.add(globalCounter);
                    }
                    else
                    {
                        blackNodes.add(new Node(-1, whiteNodes.get(i).getValue(), whiteNodes.get(i).getEnum(), '6'));
                        
                        potentMoveSize.add(1);
                        originCount.add(1);
                        depth.add(1);
                    }
                }
                else
                {
                    if(whiteNodes.get(i).getNodeType()=='0')
                    {
                        allBlackMoves(whiteNodes.get(i).getEnum(), whiteNodes.get(i).getOrigin());
                        originCount.add(globalCounter);
                    }
                    else
                    {
                        blackNodes.add(new Node(-1, whiteNodes.get(i).getValue(), whiteNodes.get(i).getOrigin(), '6'));
                        
                        potentMoveSize.add(1);
                        originCount.add(1);
                        depth.add(1);
                    }
                }    
            }
            
            vNodes.addAll(blackNodes);
            
            if(k!=steps)
            {
                depth.add(-1);
            }
            
        }

        Integer totalWidth = 0;
        
        for(int k=0;k<depth.size();k++)
        {
            Integer partitionSize = depth.get(k);

            if(partitionSize==-1)
            {
                vLevelWidth.add(totalWidth);
                totalWidth = 0;
            }
            else if(k==depth.size()-1)
            {
                totalWidth += partitionSize;
                vLevelWidth.add(totalWidth);
            }
            else
            {
                totalWidth += partitionSize;
            }
        }
        
        Integer removeCounter = 0;
        Integer allButLast = vLevelWidth.getLast();

        Integer nodeCount = vNodes.size() - allButLast + 1;
       
        for(int i=0;i<blackNodes.size();i++)
        {
            maxBlack.add(new Node(nodeCount, blackNodes.get(i).getEnum(), blackNodes.get(i).getValue(), blackNodes.get(i).getOrigin()));
            nodeCount++;
        }

        for(int i=steps+steps+1;i>=0;i--)
        {
            if(i%2==1)
            {
                for(int k=depth.size()-1;k>=0;k--)
                {
                    Integer partitionSize = depth.get(k);

                    if(partitionSize==-1)
                    {
                        removeCounter++;
                        break;
                    }

                    for(int j=0;j<partitionSize;j++)
                    {
                        tempBlack.add(maxBlack.removeLast());
                    }

                    Collections.sort(tempBlack, new Node());
                    minWhite.addFirst(tempBlack.getLast());
                    tempBlack.clear();

                    removeCounter++;
                }

                for (int j=0;j<removeCounter;j++) 
                {
                    depth.removeLast();
                }
            }
            else
            {
                for(int k=depth.size()-1;k>=0;k--)
                {
                    Integer partitionSize = depth.get(k);

                    if(partitionSize==-1)
                    {
                        removeCounter++;
                        break;
                    }
                    
                    for(int j=0;j<partitionSize;j++)
                    {
                        tempWhite.add(minWhite.removeLast());
                    }
                    
                    Collections.sort(tempWhite, new Node()); 
                    if(i==0&&tempWhite.size()>=2&&Objects.equals(tempWhite.getFirst().getValue(), tempWhite.get(1).getValue()))
                    {
                        Integer randCount = 0;
                        for(int j=0;j<=tempWhite.size()-2;j++)
                        {
                            if(Objects.equals(tempWhite.get(j).getValue(), tempWhite.get(j+1).getValue()))
                            {
                                randCount++;
                            }
                            else
                            {
                                break;
                            }
                        }
                        
                        maxBlack.add(tempWhite.get(getRandomInteger(0, randCount-1)));
                        tempWhite.clear();
                    }
                    else
                    {
                        maxBlack.addFirst(tempWhite.getFirst());
                        tempWhite.clear();
                    }
                    
                    removeCounter++;
                }
                
                for(int j=0;j<removeCounter;j++)
                {
                    depth.removeLast();
                }
                
            } 
            removeCounter = 0;
        }

        Integer bestMoveEnumeration;
        bestMoveEnumeration = maxBlack.get(0).getOrigin();
        bestMoveID = maxBlack.get(0).getID()+1;

        reverseEnum(bestMoveEnumeration);
        
        Integer bx = coords.get(0).getXCoord();
        Integer by = coords.get(0).getYCoord();
        Integer rx = coords.get(1).getXCoord();
        Integer ry = coords.get(1).getYCoord();
        Integer wx = coords.get(2).getXCoord();
        Integer wy = coords.get(2).getYCoord();
        
        Black_Pieces.get(0).setX(bx);
        Black_Pieces.get(0).setY(by);
        White_Pieces.get(1).setX(rx);
        White_Pieces.get(1).setY(ry);
        White_Pieces.get(0).setX(wx);
        White_Pieces.get(0).setY(wy);
        
        piecePositions[0] = bx * 8 + by;
        piecePositions[1] = rx * 8 + ry;
        piecePositions[2] = wx * 8 + wy;
        
        enumer = positionEnum(piecePositions[0], piecePositions[1], piecePositions[2]);
        moveCounter[enumer]++;
        bf.setTitle("Chess (White's Turn)        Position: "+enumer); 
        drawBoard();
        
        whiteNodes.clear();
        blackNodes.clear();
        maxBlack.clear();
        minWhite.clear();
        tempBlack.clear();
        tempWhite.clear();

        turnCounter++;
        
        boolean checkmate = isCheckmate(wx, wy, rx, ry, bx, by);
        boolean stalemate = isStalemate(wx, wy, rx, ry, bx, by);

        if(checkmate) // O(J) + O(N)
        {
            drawBoard();
            JOptionPane.showMessageDialog(bf, "Checkmate", "", JOptionPane.INFORMATION_MESSAGE); // O(J)
            nextMove.setEnabled(false);
        }
        if(stalemate)
        {
            drawBoard();
            JOptionPane.showMessageDialog(bf, "Stalemate", "", JOptionPane.INFORMATION_MESSAGE);
            nextMove.setEnabled(false);
        }
        if(moveCounter[enumer]==3)
        {
            JOptionPane.showMessageDialog(null, "Threefold Repetition Draw", "", JOptionPane.INFORMATION_MESSAGE);
            nextMove.setEnabled(false);
        }
        if(!checkmate&&isCheck(wx, wy, rx, ry, bx, by)||((!checkmate)&&(Objects.equals(rx, bx)||Objects.equals(ry, by))&&!isBlocked(wx, wy, rx, ry, bx, by)))
        {
            drawBoard();
            JOptionPane.showMessageDialog(null, "Check", "", JOptionPane.INFORMATION_MESSAGE);
        } 
        else if(afterblock)
        {
            JOptionPane.showMessageDialog(null, "Discovered Check", "", JOptionPane.INFORMATION_MESSAGE);
            afterblock = false;
        }  
        

        levelCounter = 0;
    }
    
    public void blackMaximin(Integer enumeration)
    {
        vNodes.clear();
        vLevelWidth.clear();
        originCount.clear();
        depth.clear();
        potentMoveSize.clear();
        
        for(int k=0;k<=steps;k++)
        {
            levelCounter++;
            
            if(whiteNodes.isEmpty())
            {
                allBlackMoves(enumeration, -1);
                originCount.add(globalCounter);
            }
            else
            {
                blackNodes.clear();
                for(int i=0;i<whiteNodes.size();i++)
                {
                    if(whiteNodes.get(i).getNodeType()=='0')
                    {
                        allBlackMoves(whiteNodes.get(i).getEnum(), whiteNodes.get(i).getOrigin());
                        originCount.add(globalCounter);
                    }
                    else
                    {
                        blackNodes.add(new Node(-1, -1, whiteNodes.get(i).getOrigin(), '6'));
                        
                        potentMoveSize.add(1);
                        originCount.add(1);
                        depth.add(1);
                    }
                }
            }
            
            vNodes.addAll(blackNodes);
            
            depth.add(-1);

            whiteNodes.clear();
            levelCounter++;
            
            for(int i=0;i<blackNodes.size();i++)
            {
                if(k==0)
                {
                    if(blackNodes.get(i).getNodeType()=='0')
                    {
                        allWhiteMoves(blackNodes.get(i).getEnum(), blackNodes.get(i).getEnum());
                        originCount.add(globalCounter);
                    }
                    else
                    {
                        whiteNodes.add(new Node(-1, -1, blackNodes.get(i).getEnum(), '6'));
                        
                        potentMoveSize.add(1);
                        originCount.add(1);
                        depth.add(1);
                    }
                }
                else
                {
                    if(blackNodes.get(i).getNodeType()=='0')
                    {
                        allWhiteMoves(blackNodes.get(i).getEnum(), blackNodes.get(i).getOrigin());
                        originCount.add(globalCounter);
                    }
                    else
                    {
                        whiteNodes.add(new Node(-1, -1, blackNodes.get(i).getOrigin(), '6'));
                        
                        potentMoveSize.add(1);
                        originCount.add(1);
                        depth.add(1);
                    }
                }    
            }
            
            vNodes.addAll(whiteNodes);
            
            if(k!=steps)
            {
                depth.add(-1);
            }
            
        }
        
        Integer totalWidth = 0;
        
        for(int k=0;k<depth.size();k++)
        {
            Integer partitionSize = depth.get(k);

            if(partitionSize==-1)
            {
                vLevelWidth.add(totalWidth);
                totalWidth = 0;
            }
            else if(k==depth.size()-1)
            {
                totalWidth += partitionSize;
                vLevelWidth.add(totalWidth);
            }
            else
            {
                totalWidth += partitionSize;
            }
        }
        
        Integer removeCounter = 0;
        Integer allButLast = vLevelWidth.getLast();
        Integer nodeCount = vNodes.size() - allButLast + 1;
       
        for(int i=0;i<whiteNodes.size();i++)
        {
            minWhite.add(new Node(nodeCount, whiteNodes.get(i).getEnum(), whiteNodes.get(i).getValue(), whiteNodes.get(i).getOrigin()));
            nodeCount++;
        }
        
        for(int i=steps+steps+1;i>=0;i--)
        {
            if(i%2==0)
            {
                for(int k=depth.size()-1;k>=0;k--)
                {
                    Integer partitionSize = depth.get(k);

                    if(partitionSize==-1)
                    {
                        removeCounter++;
                        break;
                    }

                    for(int j=0;j<partitionSize;j++)
                    {
                        tempBlack.add(maxBlack.removeLast());
                    }

                    Collections.sort(tempBlack, new Node());
                    if(i==0&&tempBlack.size()>=2&&Objects.equals(tempBlack.getLast().getValue(), tempBlack.get(tempBlack.size()-2).getValue()))
                    {
                        Integer randCount = 0;
                        for(int j=tempBlack.size()-1;j>=1;j--)
                        {
                            if(Objects.equals(tempBlack.get(j).getValue(), tempBlack.get(j-1).getValue()))
                            {
                                randCount++;
                            }
                            else
                            {
                                break;
                            }
                        }
                        
                        minWhite.add(tempBlack.get(getRandomInteger(0, randCount-1)));
                        tempBlack.clear();
                    }
                    else
                    {
                        minWhite.addFirst(tempBlack.getLast());
                        tempBlack.clear();
                    }

                    removeCounter++;
                }

                for (int j=0;j<removeCounter;j++) 
                {
                    depth.removeLast();
                }
            }
            else
            {
                for(int k=depth.size()-1;k>=0;k--)
                {
                    Integer partitionSize = depth.get(k);

                    if(partitionSize==-1)
                    {
                        removeCounter++;
                        break;
                    }
                    
                    for(int j=0;j<partitionSize;j++)
                    {
                        tempWhite.add(minWhite.removeLast());
                    }
                    
                    Collections.sort(tempWhite, new Node());                   
                    maxBlack.addFirst(tempWhite.getFirst());
                    tempWhite.clear();

                    removeCounter++;
                }
                
                for(int j=0;j<removeCounter;j++)
                {
                    depth.removeLast();
                }
                
            } 
            removeCounter = 0;
        }

        Integer bestMoveEnumeration;
        bestMoveEnumeration = minWhite.get(0).getOrigin();
        bestMoveID = minWhite.get(0).getID()+1;

        reverseEnum(bestMoveEnumeration);
        
        Integer bx = coords.get(0).getXCoord();
        Integer by = coords.get(0).getYCoord();
        Integer rx = coords.get(1).getXCoord();
        Integer ry = coords.get(1).getYCoord();
        Integer wx = coords.get(2).getXCoord();
        Integer wy = coords.get(2).getYCoord();

        Black_Pieces.get(0).setX(bx);
        Black_Pieces.get(0).setY(by);
        White_Pieces.get(1).setX(rx);
        White_Pieces.get(1).setY(ry);
        White_Pieces.get(0).setX(wx);
        White_Pieces.get(0).setY(wy);
        
        piecePositions[0] = bx * 8 + by;
        piecePositions[1] = rx * 8 + ry;
        piecePositions[2] = wx * 8 + wy;
        
        enumer = positionEnum(piecePositions[0], piecePositions[1], piecePositions[2]);
        moveCounter[enumer]++;
        bf.setTitle("Chess (White's Turn)        Position: "+enumer); 
        drawBoard();
        
        whiteNodes.clear();
        blackNodes.clear();
        maxBlack.clear();
        minWhite.clear();
        tempBlack.clear();
        tempWhite.clear();

        turnCounter++;
        
        boolean rookCapture = rookCaptureFunction(rx, ry, bx, by);
  
        if(turnCounter==100)
        {
            JOptionPane.showMessageDialog(null, "50-Move-Rule Draw", "", JOptionPane.INFORMATION_MESSAGE);
            nextMove.setEnabled(false);
        }
        if(moveCounter[enumer]==3)
        {
            JOptionPane.showMessageDialog(null, "Threefold Repetition Draw", "", JOptionPane.INFORMATION_MESSAGE);
            nextMove.setEnabled(false);
        }
        if(rookCapture)
        {
            JOptionPane.showMessageDialog(null, "Rook Capture Draw", "", JOptionPane.INFORMATION_MESSAGE);
            nextMove.setEnabled(false);
        }

        levelCounter = 0;
    }
    
    public boolean randomKingTest(Integer bx, Integer by, Integer wx, Integer wy) // O(N)
    {
        int kingProtectedSize = bKingProtected.size(); // O(N)
        for(int i=0;i<kingProtectedSize;i++) // 
        {
            if(Objects.equals(bKingProtected.get(i).getXCoord(), wx)&&Objects.equals(bKingProtected.get(i).getYCoord(), wy)) // O(N)
            {  
                return false;
            }
        }
        
        if(Objects.equals(wx, bx)&&Objects.equals(wy, by)) // O(N)
        {
            return false;
        }
        
        return true;
    }
    
    public boolean randomRookTest(Integer bx, Integer by, Integer wx, Integer wy, Integer rx, Integer ry) // O(N)
    {  
        if(Objects.equals(rx, bx)&&Objects.equals(ry, by)) // O(N)
        {
            return false;
        }
        
        if(Objects.equals(rx, wx)&&Objects.equals(ry, wy)) // O(N)
        {
            return false;
        }
        
        if(Objects.equals(rx, bx)&&!Objects.equals(rx, wx))
        {
            return false;
        }
        
        if(Objects.equals(ry, by)&&!Objects.equals(ry, wy))
        {
            return false;
        }
        
        if(Objects.equals(rx, bx)&&Objects.equals(rx, wx))
        {
            if(!(bx<wx&&wx<rx))
            {
                return false;
            }
            else if(!(bx>wx&&wx>rx))
            {
                return false;
            }
        }
        
        if(Objects.equals(ry, by)&&Objects.equals(ry, wy))
        {
            if(!(by<wy&&wy<ry))
            {
                return false;
            }
            else if(!(by>wy&&wy>ry))
            {
                return false;
            }
        }
        
        int bKingProtectedSize = bKingProtected.size(); // O(1)
        
        for(int i=0;i<bKingProtectedSize;i++) // O(N)
        {
            if(Objects.equals(bKingProtected.get(i).getXCoord(), rx)&&Objects.equals(bKingProtected.get(i).getYCoord(), ry)) // O(N)
            {  
                return false;
            }
        }
        
        return true;       
    }
    
    public boolean isCheck(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by) // O(J) + O(N)
    {
        if(Objects.equals(rx, bx)) // O(N)
        {
            return true;
        }
        if(Objects.equals(ry, by)) // O(N)
        {
            return true;
        }
        
        return false;
    }
    
    public boolean isCheckmate(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
    {
        if(isCheck(wx, wy, rx, ry, bx, by))
        {
            if(!rookCaptureFunction(rx, ry, bx, by))
            {
                if(blackMoveCounter(wx, wy, rx, ry, bx, by)==0)
                {
                    return true;
                }
            } 
        }
        return false;
    }
    
    public boolean isStalemate(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
    {
        if(!isCheck(wx, wy, rx, ry, bx, by))
        {
            if(blackMoveCounter(wx, wy, rx, ry, bx, by)==0)
            {
                return true;
            }
        }
        return false;
    }
    
    public Integer blackMoveCounter(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
    {
        Integer moveCount = 0;
        
        tempBKingProtected.clear();
        tempBKingReplace(bx, by);
        tempWKingProtected.clear();
        tempWKingReplace(wx, wy);
        tempRookProtected.clear();
        tempRookReplace(rx, ry, wx, wy);

        for(int i=0;i<tempBKingProtected.size();i++)
        {
            boolean bkRookDanger = false;
            boolean bkKingDanger = false;
            boolean rookCapturable = false;
            
            for(int j=0;j<tempWKingProtected.size();j++)
            {
                if(Objects.equals(tempWKingProtected.get(j).getXCoord(), tempBKingProtected.get(i).getXCoord())&&Objects.equals(tempWKingProtected.get(j).getYCoord(), tempBKingProtected.get(i).getYCoord()))
                {
                    bkKingDanger = true;
                }
            }
            
            for(int j=0;j<tempRookProtected.size();j++)
            {
                if(Objects.equals(tempRookProtected.get(j).getXCoord(), tempBKingProtected.get(i).getXCoord())&&Objects.equals(tempRookProtected.get(j).getYCoord(), tempBKingProtected.get(i).getYCoord()))
                {
                    bkRookDanger = true;
                }
            }
            
            if(Objects.equals(tempBKingProtected.get(i).getXCoord(), rx)&&Objects.equals(tempBKingProtected.get(i).getYCoord(), ry))
            {
                rookCapturable = true;
            }
            
            if((!bkKingDanger&&!bkRookDanger)||(!bkKingDanger&&bkRookDanger&&rookCapturable))
            {              
                moveCount++;
            }
        }
        
        return moveCount;
    }

    public static Integer getRandomInteger(Integer maximum, Integer minimum) // O(1)
    { 
        return ((int) (Math.random()*(maximum - minimum))) + minimum; 
    }
    
    public void bKingReplace(Integer x, Integer y) // O(1)
    {
        if(!(y-1<0))
        {
            bKingProtected.add(new Coords(x, y-1));
        }
        
        if(!(x+1>7)&&!(y-1<0))
        {
            bKingProtected.add(new Coords(x+1, y-1));
        }
        
        if(!(x+1>7))
        {
            bKingProtected.add(new Coords(x+1, y));
        }
        
        if(!(x+1>7)&&!(y+1>7))
        {
            bKingProtected.add(new Coords(x+1, y+1));
        }
        
        if(!(y+1>7))
        {
            bKingProtected.add(new Coords(x, y+1));
        }
        
        if(!(x-1<0)&&!(y+1>7))
        {
            bKingProtected.add(new Coords(x-1, y+1));
        }
        
        if(!(x-1<0))
        {
            bKingProtected.add(new Coords(x-1, y));
        }
        
        if(!(x-1<0)&&!(y-1<0))
        {
            bKingProtected.add(new Coords(x-1, y-1));
        }
    }
    
    public void tempBKingReplace(Integer x, Integer y) // O(1)
    {
        if(!(y-1<0))
        {
            tempBKingProtected.add(new Coords(x, y-1));
        }
        
        if(!(x+1>7)&&!(y-1<0))
        {
            tempBKingProtected.add(new Coords(x+1, y-1));
        }
        
        if(!(x+1>7))
        {
            tempBKingProtected.add(new Coords(x+1, y));
        }
        
        if(!(x+1>7)&&!(y+1>7))
        {
            tempBKingProtected.add(new Coords(x+1, y+1));
        }
        
        if(!(y+1>7))
        {
            tempBKingProtected.add(new Coords(x, y+1));
        }
        
        if(!(x-1<0)&&!(y+1>7))
        {
            tempBKingProtected.add(new Coords(x-1, y+1));
        }
        
        if(!(x-1<0))
        {
            tempBKingProtected.add(new Coords(x-1, y));
        }
        
        if(!(x-1<0)&&!(y-1<0))
        {
            tempBKingProtected.add(new Coords(x-1, y-1));
        }
    }
    
    public void wKingReplace(Integer x, Integer y) // O(1)
    {
        if(!(y-1<0))
        {
            wKingProtected.add(new Coords(x, y-1));
        }
        
        if(!(x+1>7)&&!(y-1<0))
        {
            wKingProtected.add(new Coords(x+1, y-1));
        }
        
        if(!(x+1>7))
        {
            wKingProtected.add(new Coords(x+1, y));
        }
        
        if(!(x+1>7)&&!(y+1>7))
        {
            wKingProtected.add(new Coords(x+1, y+1));
        }
        
        if(!(y+1>7))
        {
            wKingProtected.add(new Coords(x, y+1));
        }
        
        if(!(x-1<0)&&!(y+1>7))
        {
            wKingProtected.add(new Coords(x-1, y+1));
        }
        
        if(!(x-1<0))
        {
            wKingProtected.add(new Coords(x-1, y));
        }
        
        if(!(x-1<0)&&!(y-1<0))
        {
            wKingProtected.add(new Coords(x-1, y-1));
        }
    }
    
    public void tempWKingReplace(Integer x, Integer y) // O(1)
    {
        if(!(y-1<0))
        {
            tempWKingProtected.add(new Coords(x, y-1));
        }
        
        if(!(x+1>7)&&!(y-1<0))
        {
            tempWKingProtected.add(new Coords(x+1, y-1));
        }
        
        if(!(x+1>7))
        {
            tempWKingProtected.add(new Coords(x+1, y));
        }
        
        if(!(x+1>7)&&!(y+1>7))
        {
            tempWKingProtected.add(new Coords(x+1, y+1));
        }
        
        if(!(y+1>7))
        {
            tempWKingProtected.add(new Coords(x, y+1));
        }
        
        if(!(x-1<0)&&!(y+1>7))
        {
            tempWKingProtected.add(new Coords(x-1, y+1));
        }
        
        if(!(x-1<0))
        {
            tempWKingProtected.add(new Coords(x-1, y));
        }
        
        if(!(x-1<0)&&!(y-1<0))
        {
            tempWKingProtected.add(new Coords(x-1, y-1));
        }
    }
    
    public void wRookReplace(Integer rx, Integer ry, Integer wx, Integer wy) // O(N) 
    {
        for(int i=rx+1;i<=7;i++) // O(N)
        {
            wRookProtected.add(new Coords(i, ry)); // O(1)
            if(Objects.equals(i, wx)&&Objects.equals(ry, wy)) // O(N)
            {
                break;
            }
        }
        
        for(int i=rx-1;i>=0;i--) 
        {
            wRookProtected.add(new Coords(i, ry));
            if(Objects.equals(i, wx)&&Objects.equals(ry, wy))
            {
                break;
            }
        }
        
        for(int i=ry+1;i<=7;i++) 
        {
            wRookProtected.add(new Coords(rx, i));
            if(Objects.equals(i, wy)&&Objects.equals(rx, wx))
            {
                break;
            } 
        }
        
        for(int i=ry-1;i>=0;i--) 
        {
            wRookProtected.add(new Coords(rx, i));
            if(Objects.equals(i, wy)&&Objects.equals(rx, wx))
            {
                break;
            }
        }
        
        for(int i=0;i<wRookProtected.size();i++)
        {
            if(Objects.equals(wRookProtected.get(i).getXCoord(), rx)&&Objects.equals(wRookProtected.get(i).getYCoord(), ry))
            {
                wRookProtected.remove(i);
                break;
            }
        }
    }
    
    public void tempRookReplace(Integer rx, Integer ry, Integer wx, Integer wy) // O(N) 
    {
        for(int i=rx+1;i<=7;i++) // O(N)
        {
            tempRookProtected.add(new Coords(i, ry)); // O(1)
            if(Objects.equals(i, wx)&&Objects.equals(ry, wy)) // O(N)
            {
                break;
            }
        }
        
        for(int i=rx-1;i>=0;i--) 
        {
            tempRookProtected.add(new Coords(i, ry));
            if(Objects.equals(i, wx)&&Objects.equals(ry, wy))
            {
                break;
            }
        }
        
        for(int i=ry+1;i<=7;i++) 
        {
            tempRookProtected.add(new Coords(rx, i));
            if(Objects.equals(i, wy)&&Objects.equals(rx, wx))
            {
                break;
            } 
        }
        
        for(int i=ry-1;i>=0;i--) 
        {
            tempRookProtected.add(new Coords(rx, i));
            if(Objects.equals(i, wy)&&Objects.equals(rx, wx))
            {
                break;
            }
        }
        
        for(int i=0;i<tempRookProtected.size();i++)
        {
            if(Objects.equals(tempRookProtected.get(i).getXCoord(), rx)&&Objects.equals(tempRookProtected.get(i).getYCoord(), ry))
            {
                tempRookProtected.remove(i);
                break;
            }
        }
    }
     
    public boolean isBlocked(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by) // O(N)
    {
        if(bx>wx&&wx>rx&&(Objects.equals(wy, ry))) // O(N)
        {
            return true;
        }
        if(rx>wx&&wx>bx&&(Objects.equals(wy, ry))) // O(N)
        {
            return true;
        }
        if(by>wy&&wy>ry&&((Objects.equals(wx, rx)))) // O(N)
        {
            return true;
        }
        if(ry>wy&&wy>by&&((Objects.equals(wx, rx)))) // O(N)
        {
            return true;
        }
        return false;
    }
    
    public boolean isOpposition(Integer bx, Integer by, Integer wx, Integer wy, Integer rx, Integer ry) // O(N)
    {
        if(Objects.equals(bx, wx)&&(by-wy==2||wy-by==2)&&!(Math.abs(ry-wy)==1&&Math.abs(ry-by)==1&&Objects.equals(bx, rx))) // O(N)
        {
            return true;
        }
        else if(Objects.equals(by, wy)&&(bx-wx==2||wx-bx==2)&&!(Math.abs(rx-wx)==1&&Math.abs(rx-bx)==1&&Objects.equals(by, ry))) // O(N)
        {
            return true;
        }
        return false;
    }
    
    public boolean passage(Integer bx, Integer by, Integer wx, Integer wy, Integer rx, Integer ry)
    {
        if(Objects.equals(wx, rx)||Objects.equals(wy, ry))
        {
            if(Objects.equals(wx, rx))
            {
                if(ry>wy&&wy>by)
                {
                    if(wy>=2)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else if(ry<wy&&wy<by)
                {
                    if(7-wy>=2)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
            
           
            if(Objects.equals(wy, ry))
            {
                if(rx>wx&&wx>bx)
                {
                    if(wx>=2)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else if(rx<wx&&wx<bx)
                {
                    if(7-wx>=2)
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return false;
                }
            }
        }
        
        return false;
    }
    
    public void whiteKingSquares(Integer x, Integer y)
    {
        wkCoords.clear();
        wkCoords.add(new Coords(x, y));
        
        if(!(y-1<0))
        {
            wkCoords.add(new Coords(x, y-1));
        }
        
        if(!(x+1>7)&&!(y-1<0))
        {
            wkCoords.add(new Coords(x+1, y-1));
        }
        
        if(!(x+1>7))
        {
            wkCoords.add(new Coords(x+1, y));
        }
        
        if(!(x+1>7)&&!(y+1>7))
        {
            wkCoords.add(new Coords(x+1, y+1));
        }
        
        if(!(y+1>7))
        {
            wkCoords.add(new Coords(x, y+1));
        }
        
        if(!(x-1<0)&&!(y+1>7))
        {
            wkCoords.add(new Coords(x-1, y+1));
        }
        
        if(!(x-1<0))
        {
            wkCoords.add(new Coords(x-1, y));
        }
        
        if(!(x-1<0)&&!(y-1<0))
        {
            wkCoords.add(new Coords(x-1, y-1));
        }
    }
    
    public Integer freeSquaresFunction(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
    {
        fsCoords.clear();
        String primaryQuadrant = "";
        String secondaryQuadrant = "";
        
        if((Objects.equals(rx, bx)||Objects.equals(ry, by))&&!isBlocked(wx, wy, rx, ry, bx, by))                        // FREE SQUARES WHEN THE BOARD IS IN STATE OF CHECK
        {
            if(!isOpposition(bx, by, wx, wy, rx, ry)) // CHECK BUT WITHOUT OPPOSITION
            {
                if(Objects.equals(rx, bx))
                {
                    if(by>ry)
                    {
                        primaryQuadrant = "q3";
                        secondaryQuadrant = "q4";
                    }
                    else if(by<ry) // THIS IS WHERE IT DETERMINES FREE SQUARES FOR THE PRINT SCREEN
                    {
                        primaryQuadrant = "q1";
                        secondaryQuadrant = "q2";
                    }
                }
                else if(Objects.equals(ry, by))
                {
                    if(bx>rx)
                    {
                        primaryQuadrant = "q1";
                        secondaryQuadrant = "q4";
                    }
                    else if(bx<rx)
                    {
                        primaryQuadrant = "q2";
                        secondaryQuadrant = "q3";
                    }
                }
            }
            else if(isOpposition(bx, by, wx, wy, rx, ry)) // CHECK BUT WITH OPPOSITION
            {
                if(Objects.equals(ry, by)&&Objects.equals(bx, wx))
                {
                    if(rx>bx&&wy>by) // quadrant 2
                    {
                        primaryQuadrant = "q2";
                    }
                    else if(rx<bx&&wy>by) // quadrant 1 
                    {
                        primaryQuadrant = "q1";
                    }
                    else if(rx<bx&&wy<by) // quadrant 3
                    {
                        primaryQuadrant = "q3";
                    }
                    else if(rx>bx&&wy<by) // quadrant 4
                    {
                        primaryQuadrant = "q4";
                    }
                }
                else if(Objects.equals(rx, bx)&&Objects.equals(by, wy))
                {
                    if(ry<by&&wx<bx) // quadrant 4
                    {
                        primaryQuadrant = "q4";
                    }
                    else if(ry<by&&wx>bx) //  quadrant 3 
                    {
                        primaryQuadrant = "q3";
                    }
                    else if(ry>by&&wx>bx) //  quadrant 2
                    {
                        primaryQuadrant = "q2";
                    }
                    else if(ry>by&&wx<bx) //  quadrant 1
                    {
                        primaryQuadrant = "q1";
                    }
                }
            }
        }
        else if(!isBlocked(wx, wy, rx, ry, bx, by))                                                           // FREE SQUARES WHEN ROOK IS NOT BLOCKED BY KING
        {
            if(rx<bx&&ry>by)
            {
                primaryQuadrant = "q1";
            }
            else if(rx>bx&&ry>by)
            {
                primaryQuadrant = "q2";
            }
            else if(rx>bx&&ry<by)
            {
                primaryQuadrant = "q3";
            }
            else if(rx<bx&&ry<by)
            {
                primaryQuadrant = "q4";
            } 
        }
        else if(isBlocked(wx, wy, rx, ry, bx, by))                                                                  // FREE SQUARES WHEN THE KING BLOCKS THE ROOK
        {
            if(passage(bx, by, wx, wy, rx, ry)) // THE WHITE ROOK IS BLOCKED BY THE WHITE KING AND THERE IS A PASSAGE
            {
                if(Objects.equals(wx, rx))
                {
                    if(ry>wy&&wy>by)
                    {
                        primaryQuadrant = "q1";
                        secondaryQuadrant = "q2"; 
                        for(int i=wy-2;i>-1;i--)
                        {
                            fsCoords.add(new Coords(wx, i));
                        }
                    }
                    else if(ry<wy&&wy<by)
                    {
                        primaryQuadrant = "q3";
                        secondaryQuadrant = "q4";
                        for(int i=wy+2;i<8;i++) // passage squares
                        {
                            fsCoords.add(new Coords(wx, i));
                        }
                        
                    }
                }
                else if(Objects.equals(wy, ry))
                {
                    if(rx>wx&&wx>bx)
                    {
                        primaryQuadrant = "q2";
                        secondaryQuadrant = "q3";
                        for(int i=wx+2;i<8;i++) // passage squares
                        {
                            fsCoords.add(new Coords(i, wy));
                        }
                    }
                    else if(rx<wx&&wx<bx)
                    {
                        primaryQuadrant = "q1";
                        secondaryQuadrant = "q4"; 
                        for(int i=wx-2;i>-1;i--)
                        {
                            fsCoords.add(new Coords(i, wy));
                        }
                    }
                }
            }
            else if(!passage(bx, by, wx, wy, rx, ry)) // THE WHITE ROOK IS BLOCKED BY THE WHITE KING AND THERE IS NO PASSAGE
            {
                if(rx<bx&&ry>by)
                {
                    primaryQuadrant = "q1";
                }
                else if(rx>bx&&ry>by)
                {
                    primaryQuadrant = "q2";
                }
                else if(rx>bx&&ry<by)
                {
                    primaryQuadrant = "q3";
                }
                else if(rx<bx&&ry<by)
                {
                    primaryQuadrant = "q4";
                }
            } 
        }
        
        switch(primaryQuadrant)
        {
            case "q1":
                for(int i=rx+1;i<8;i++)
                {
                    for(int j=0;j<ry;j++)
                    {
                        fsCoords.add(new Coords(i, j));
                    }
                }
                break;
            case "q2":
                for(int i=0;i<rx;i++)
                {
                    for(int j=0;j<ry;j++)
                    {
                        fsCoords.add(new Coords(i, j));
                    }
                }
                break;
            case "q3":
                for(int i=0;i<rx;i++)
                {
                    for(int j=ry+1;j<8;j++)
                    {
                        fsCoords.add(new Coords(i, j));
                    }
                }
                break;
            case "q4":
                for(int i=rx+1;i<8;i++)
                {
                    for(int j=ry+1;j<8;j++)
                    {
                        fsCoords.add(new Coords(i, j));
                    }
                }
                break;
        }
        
        switch(secondaryQuadrant)
        {
            case "q1":
                for(int i=rx+1;i<8;i++)
                {
                    for(int j=0;j<ry;j++)
                    {
                        fsCoords.add(new Coords(i, j));
                    }
                }
                break;
            case "q2":
                for(int i=0;i<rx;i++)
                {
                    for(int j=0;j<ry;j++)
                    {
                        fsCoords.add(new Coords(i, j));
                    }
                }
                break;
            case "q3":
                for(int i=0;i<rx;i++)
                {
                    for(int j=ry+1;j<8;j++)
                    {
                        fsCoords.add(new Coords(i, j));
                    }
                }
                break;
            case "q4":
                for(int i=rx+1;i<8;i++)
                {
                    for(int j=ry+1;j<8;j++)
                    {
                        fsCoords.add(new Coords(i, j));
                    }
                }
                break;
        }
        
        Integer squaresCovered = 0;
        
        whiteKingSquares(wx, wy);
        
        for (Coords wkCoord : wkCoords) 
        {
            for (Coords fsCoord : fsCoords) 
            {
                if(Objects.equals(fsCoord.getXCoord(), wkCoord.getXCoord())&&Objects.equals(fsCoord.getYCoord(), wkCoord.getYCoord()))
                {
                    squaresCovered++;
                    break;
                }
            }
        }
        
        return fsCoords.size() - squaresCovered;
    }
    
    public Integer positionEnumerationFunction(Integer bx, Integer by, Integer rx, Integer ry, Integer wx, Integer wy)
    {
        Integer bKing = bx * 8 + by;
        Integer wRook = rx * 8 + ry;
        Integer wKing = wx * 8 + wy;

        Integer enumeration = positionEnum(bKing, wRook, wKing);
        
        return enumeration;
    }
    
    public Integer positionEnum(Integer a, Integer b, Integer c)
    {
        return (a*64*64)+(b*64)+c;
    }
    
    static public void reverseEnum(Integer enumeration)
    {
        coords.clear();
        
        Integer bKingPos;
        Integer wRookPos;
        Integer wKingPos;
        
        Integer bx;
        Integer by;
        Integer rx;
        Integer ry;
        Integer wx;
        Integer wy;
        
        bKingPos = enumeration / (64*64);
        
        enumeration %= 64*64;
        
        wRookPos = enumeration / 64;
        
        enumeration %= 64;
        
        wKingPos = enumeration;
        
        bx = bKingPos / 8;
        by = bKingPos % 8;
        rx = wRookPos / 8;
        ry = wRookPos % 8;
        wx = wKingPos / 8;
        wy = wKingPos % 8;
        
        coords.add(new Coords(bx, by));
        coords.add(new Coords(rx, ry));
        coords.add(new Coords(wx, wy));
    }

    public boolean rookCaptureFunction(Integer rx, Integer ry, Integer bx, Integer by)
    {
        boolean rookCapture = false;
        
        if(Objects.equals(rx, bx)&&Objects.equals(ry, by))
        {
            rookCapture = true;
        }
        
        return rookCapture;
    }

    public Integer positionEvaluator(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
    {
        Integer freeSquares = 0;
        Integer endGameValue = 0;
        Integer enumeration = positionEnumerationFunction(bx, by, rx, ry, wx, wy);
        
        if(isCheckmate(wx, wy, rx, ry, bx, by))
        {
            endGameValue = -1000;
            checkmateFlag = true;
            
            if(levelCounter==3) // checkmate 3 moves ahead in minimax, lesser value so it has lesser priority since it occurs later
            {
                endGameValue = -900;
            }
        }
        else if(isStalemate(wx, wy, rx, ry, bx, by))
        {
            endGameValue = 1000;
            stalemateFlag = true;
        }
        else if(moveCounter[enumeration]+1==3)
        {
            endGameValue = 1000;
            repetitionFlag = true;
        }
        else if(rookCaptureFunction(rx, ry, bx, by))
        {
            endGameValue = 1000;
            rookCaptureFlag = true;
        }
        else if(turnCounter+levelCounter==100)
        {
            fiftyMoveFlag = true;
        }
        
        if(!checkmateFlag&&!stalemateFlag)
        {
            freeSquares = freeSquaresFunction(wx, wy, rx, ry, bx, by);
        }
        
        return freeSquares + endGameValue;
        
    }
    
    public void allWhiteMoves(Integer enumeration, Integer origin)
    {
        Integer moveCount = 0;
        
        reverseEnum(enumeration);
        
        Integer bx = coords.get(0).getXCoord();
        Integer by = coords.get(0).getYCoord();
        Integer rx = coords.get(1).getXCoord();
        Integer ry = coords.get(1).getYCoord();
        Integer wx = coords.get(2).getXCoord();
        Integer wy = coords.get(2).getYCoord();
        
        bKingProtected.clear();
        bKingReplace(bx, by);
        wKingProtected.clear();
        wKingReplace(wx, wy);
        wRookProtected.clear();
        wRookReplace(rx, ry, wx, wy);

        for(int i=0;i<wRookProtected.size();i++)
        {
            boolean sameCoords = false;
            
            if(Objects.equals(wRookProtected.get(i).getXCoord(), wx)&&Objects.equals(wRookProtected.get(i).getYCoord(), wy))
            {
                sameCoords = true;
            } 
            
            if(!sameCoords)
            {
                Integer enumerationTemp = positionEnumerationFunction(bx, by, wRookProtected.get(i).getXCoord(), wRookProtected.get(i).getYCoord(), wx, wy);
                Integer eval = positionEvaluator(wx, wy, wRookProtected.get(i).getXCoord(), wRookProtected.get(i).getYCoord(), bx, by);
                
                if(checkmateFlag)
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '1'));
                }
                else if(stalemateFlag)
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '2'));
                }
                else if(repetitionFlag)
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '3'));
                }
                else if(fiftyMoveFlag)
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '5'));
                }
                else
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '0'));
                }
                
                checkmateFlag = false;
                stalemateFlag = false;
                repetitionFlag = false;
                rookCaptureFlag = false;
                
                moveCount++;
            }
        }
        
        for(int i=0;i<wKingProtected.size();i++)
        {
            boolean wkDanger = false;
            boolean sameCoords = false;
            
            for(int j=0;j<bKingProtected.size();j++)
            {
                if(Objects.equals(bKingProtected.get(j).getXCoord(), wKingProtected.get(i).getXCoord())&&Objects.equals(bKingProtected.get(j).getYCoord(), wKingProtected.get(i).getYCoord()))
                {
                    wkDanger = true;
                }
            }
            
            if(Objects.equals(wKingProtected.get(i).getXCoord(), rx)&&Objects.equals(wKingProtected.get(i).getYCoord(), ry))
            {
                sameCoords = true;
            }
            
            if(!wkDanger&&!sameCoords)
            {
                Integer enumerationTemp = positionEnumerationFunction(bx, by, rx, ry, wKingProtected.get(i).getXCoord(), wKingProtected.get(i).getYCoord());
                Integer eval = positionEvaluator(wKingProtected.get(i).getXCoord(), wKingProtected.get(i).getYCoord(), rx, ry, bx, by);
                
                if(checkmateFlag)
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '1'));
                }
                else if(stalemateFlag)
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '2'));
                }
                else if(repetitionFlag)
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '3'));
                }
                else if(fiftyMoveFlag)
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '5'));
                }
                else
                {
                    whiteNodes.add(new Node(enumerationTemp, eval, origin, '0'));
                }
                
                checkmateFlag = false;
                stalemateFlag = false;
                repetitionFlag = false;
                rookCaptureFlag = false;
                
                moveCount++;
            }
        }    
        
        if(moveCount!=0)
        {
            depth.add(moveCount);
            potentMoveSize.add(moveCount);
        }
        
        globalCounter = moveCount;
    }  
    
    public void allBlackMoves(Integer enumeration, Integer origin)
    {
        Integer moveCount = 0;
        
        reverseEnum(enumeration);
        
        Integer bx = coords.get(0).getXCoord();
        Integer by = coords.get(0).getYCoord();
        Integer rx = coords.get(1).getXCoord();
        Integer ry = coords.get(1).getYCoord();
        Integer wx = coords.get(2).getXCoord();
        Integer wy = coords.get(2).getYCoord();
        
        bKingProtected.clear();
        bKingReplace(bx, by);
        wKingProtected.clear();
        wKingReplace(wx, wy);
        wRookProtected.clear();
        wRookReplace(rx, ry, wx, wy);

        for(int i=0;i<bKingProtected.size();i++)
        {
            boolean bkRookDanger = false;
            boolean bkKingDanger = false;
            boolean rookCapturable = false;
            
            for(int j=0;j<wKingProtected.size();j++)
            {
                if(Objects.equals(wKingProtected.get(j).getXCoord(), bKingProtected.get(i).getXCoord())&&Objects.equals(wKingProtected.get(j).getYCoord(), bKingProtected.get(i).getYCoord()))
                {
                    bkKingDanger = true;
                }
            }
            
            for(int j=0;j<wRookProtected.size();j++)
            {
                if(Objects.equals(wRookProtected.get(j).getXCoord(), bKingProtected.get(i).getXCoord())&&Objects.equals(wRookProtected.get(j).getYCoord(), bKingProtected.get(i).getYCoord()))
                {
                    bkRookDanger = true;
                }
            }
            
            if(Objects.equals(bKingProtected.get(i).getXCoord(), rx)&&Objects.equals(bKingProtected.get(i).getYCoord(), ry))
            {
                rookCapturable = true;
            }
            
            if((!bkKingDanger&&!bkRookDanger)||(!bkKingDanger&&bkRookDanger&&rookCapturable))
            {
                Integer enumerationTemp = positionEnumerationFunction(bKingProtected.get(i).getXCoord(), bKingProtected.get(i).getYCoord(), rx, ry, wx, wy);
                Integer eval = positionEvaluator(wx, wy, rx, ry, bKingProtected.get(i).getXCoord(), bKingProtected.get(i).getYCoord());
                
                if(repetitionFlag)
                {
                    blackNodes.add(new Node(enumerationTemp, eval, origin, '3'));
                }
                else if(rookCaptureFlag)
                {
                    blackNodes.add(new Node(enumerationTemp, eval, origin, '4'));
                }
                else if(fiftyMoveFlag)
                {
                    blackNodes.add(new Node(enumerationTemp, eval, origin, '5'));
                }
                else
                {
                    blackNodes.add(new Node(enumerationTemp, eval, origin, '0'));
                }
                
                checkmateFlag = false;
                stalemateFlag = false;
                repetitionFlag = false;
                rookCaptureFlag = false;
                
                moveCount++;
            }
        }
        
        if(moveCount!=0)
        {
            depth.add(moveCount);
            potentMoveSize.add(moveCount);
        }
        
        globalCounter = moveCount;
    }

    private Image loadImage(String imageFile) // O(J)
    {
        try 
        {
                return ImageIO.read(new File(imageFile));
        }
        catch (IOException e) 
        {
                return NULL_IMAGE;
        }
    }

    @Override
    protected void paintComponent(Graphics g) // O(J)
    {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        drawBackground(g2);
        drawShapes(g2);
        
        g.setFont(new Font("default", Font.BOLD, 13));
        g.setColor(Color.BLACK);
        
        g.drawString("7", 510, 80);
        g.drawString("5", 510, 210);
        g.drawString("3", 510, 340);
        g.drawString("1", 510, 470);
        g.drawString("b", 70, 513);
        g.drawString("d", 200, 513);
        g.drawString("f", 330, 513);
        g.drawString("h", 460, 513);
        
        g.setColor(Color.WHITE);
        
        g.drawString("8", 510, 15); // along y - axis
        g.drawString("6", 510, 145);
        g.drawString("4", 510, 275);
        g.drawString("2", 510, 405);
        g.drawString("a", 5, 513); // along x - axis
        g.drawString("c", 135, 513);
        g.drawString("e", 265, 513);
        g.drawString("g", 395, 513);  
    }

    private void drawBackground(Graphics2D g2) // O(J)
    {
        g2.setColor(getBackground());
        g2.fillRect(10,  10, getWidth(), getHeight());
    }
       

    private void drawShapes(Graphics2D g2) // O(J)
    {
        for (DrawingShape shape : Static_Shapes) 
        {
            shape.draw(g2);
        }	
        
        for (DrawingShape shape : Piece_Graphics) 
        {
            shape.draw(g2);
        }
    }
}

interface DrawingShape 
{
    boolean contains(Graphics2D g2, double x, double y);
    void adjustPosition(double dx, double dy);
    void draw(Graphics2D g2);
}

class DrawingImage implements DrawingShape 
{
    public Image image;
    public Rectangle2D rect;

    public DrawingImage(Image image, Rectangle2D rect) // O(1)
    {
            this.image = image;
            this.rect = rect;
    }

    @Override
    public boolean contains(Graphics2D g2, double x, double y) // O(J)
    {
            return rect.contains(x, y);
    }

    @Override
    public void adjustPosition(double dx, double dy)  // O(J)
    {
            rect.setRect(rect.getX() + dx, rect.getY() + dy, rect.getWidth(), rect.getHeight());	
    }

    @Override
    public void draw(Graphics2D g2)  // O(J)
    {
            Rectangle2D bounds = rect.getBounds2D();
            g2.drawImage(image, (int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getMaxX(), (int)bounds.getMaxY(), 0, 0, image.getWidth(null), image.getHeight(null), null);
    }	
}