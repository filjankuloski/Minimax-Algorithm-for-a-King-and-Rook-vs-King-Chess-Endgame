package chessgui.pieces;
import chessgui.Coords;
import chessgui.Node;
import java.util.LinkedList;

public interface positions 
{
    public static LinkedList<Coords> wRookProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> bKingProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> wKingProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> tempRookProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> tempBKingProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> tempWKingProtected = new LinkedList<Coords>();
    public static Integer[] piecePositions = new Integer[3];
    public static LinkedList<Coords> coords = new LinkedList<Coords>();
    public static LinkedList<Node> whiteNodes = new LinkedList<Node>();
    public static LinkedList<Node> blackNodes = new LinkedList<Node>();
    public static LinkedList<Node> maxBlack = new LinkedList<Node>();
    public static LinkedList<Node> tempBlack = new LinkedList<Node>();
    public static LinkedList<Node> minWhite = new LinkedList<Node>();
    public static LinkedList<Node> tempWhite = new LinkedList<Node>();
    public static LinkedList<Integer> depth = new LinkedList<Integer>();
    public static LinkedList<Node> vNodes = new LinkedList<Node>();
    public static LinkedList<Integer> vLevelWidth = new LinkedList<Integer>();
    public static LinkedList<Integer> potentMoveSize = new LinkedList<Integer>();
    public static LinkedList<Coords> pathNodes = new LinkedList<Coords>();
    public static LinkedList<Coords> pathLines = new LinkedList<Coords>();
    public static LinkedList<Integer> originCount = new LinkedList<Integer>();
    public static LinkedList<Coords> movingNode = new LinkedList<Coords>();
    public static LinkedList<Coords> fsCoords = new LinkedList<Coords>();
    public static LinkedList<Coords> wkCoords = new LinkedList<Coords>();
    
}
