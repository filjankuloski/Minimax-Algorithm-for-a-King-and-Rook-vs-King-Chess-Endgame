package chessgui;
import java.awt.Point;

public class Connection 
    {
        private Point from, to;
        char fromNodeType, toNodeType;

        public Connection(Point from, Point to, char fromNodeType, char toNodeType) 
        {
            this.from = from;
            this.to = to;
            this.fromNodeType = fromNodeType;
            this.toNodeType = toNodeType;
        }

        public Point getFrom() 
        {
            return from;
        }

        public Point getTo() 
        {
            return to;
        }
        
        public char getFromNodeType()
        {
            return fromNodeType;
        }
        
        public char getToNodeType()
        {
            return toNodeType;
        }
    }