package javaapplication12;
import java.util.LinkedList;

public interface positions 
{
    public static LinkedList<Coords> wRookProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> bKingProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> wKingProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> tempRookProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> tempBKingProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> tempWKingProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> coords = new LinkedList<Coords>();
    public static LinkedList<Coords> fsCoords = new LinkedList<Coords>();
    public static LinkedList<Coords> wkCoords = new LinkedList<Coords>();
    
    public static LinkedList<Node> whiteNodes = new LinkedList<Node>();
    public static LinkedList<Node> blackNodes = new LinkedList<Node>();
    public static LinkedList<Node> maxBlack = new LinkedList<Node>();
    public static LinkedList<Node> tempBlack = new LinkedList<Node>();
    public static LinkedList<Node> minWhite = new LinkedList<Node>();
    public static LinkedList<Node> tempWhite = new LinkedList<Node>();
    
    public static Integer[] piecePositions = new Integer[3];
    public static LinkedList<Integer> subtree = new LinkedList<Integer>();     
}

