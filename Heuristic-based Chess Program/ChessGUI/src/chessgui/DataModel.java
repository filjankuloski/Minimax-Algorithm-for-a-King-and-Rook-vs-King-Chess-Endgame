package chessgui;
import static chessgui.pieces.positions.originCount;
import static chessgui.pieces.positions.potentMoveSize;
import static chessgui.pieces.positions.vLevelWidth;
import static chessgui.pieces.positions.vNodes;
import java.util.LinkedList;

public class DataModel 
    {
        public LinkedList<Integer> getPotentMoveSize() 
        {
            return potentMoveSize;
        }

        public LinkedList<Integer> getVLevelWidth() 
        {
            return vLevelWidth;
        }

        public LinkedList<Node> getVNodes() 
        {
            return vNodes;
        }
        
        public LinkedList<Integer> getOriginCount()
        {
            return originCount;
        }
    }
