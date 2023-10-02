package chessgui;
import static chessgui.Board.bestMoveID;
import static chessgui.Board.initEnum;
import static chessgui.Board.reverseEnum;
import static chessgui.Board.steps;
import static chessgui.pieces.positions.coords;
import static chessgui.pieces.positions.movingNode;
import static chessgui.pieces.positions.pathNodes;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Objects;

public class ViewModel 
{
    private LinkedList<Coords> nodeCoords = new LinkedList<>();
    private LinkedList<Coords> originNodeCoords = new LinkedList<>();
    private LinkedList<Connection> connections = new LinkedList<>();
    Integer x, y;

    public ViewModel(DataModel model) 
    {
        LinkedList<Integer> potentMoveSize = new LinkedList<>(model.getPotentMoveSize());
        LinkedList<Integer> vLevelWidth = new LinkedList<>(model.getVLevelWidth());
        LinkedList<Node> vNodes = new LinkedList<>(model.getVNodes());
        LinkedList<Integer> originCount = new LinkedList<>(model.getOriginCount());

        Integer height = steps + steps + 2;
        
        if(steps+steps==0)
        {
            x = 19500;
            y = 750;
        }
        else
        {
            x = 2030000;
            y = 200000;
        } 
        
        nodeCoords.add(new Coords(1, initEnum, x / 2, 10, '0'));
        
        Integer idCount = 2;
        
        Integer heightIntervals = (y-10) / (height); // height

        for (int i = 0; i < height; i++) 
        {
            for (int j = 0; j < vLevelWidth.size(); j++) 
            {
                Integer fullWidth = vLevelWidth.get(j);
                Integer nodeCount = 0;

                Integer widthInterval = (x - 150) / fullWidth; // width

                for (int k = 0; k < vNodes.size(); k++) 
                {
                    Integer enumeration = vNodes.get(k).getEnum();
                    reverseEnum(enumeration);
        
                    Integer bx = coords.get(0).getXCoord();
                    Integer by = coords.get(0).getYCoord();
                    Integer rx = coords.get(1).getXCoord();
                    Integer ry = coords.get(1).getYCoord();
                    Integer wx = coords.get(2).getXCoord();
                    Integer wy = coords.get(2).getYCoord();
                    
                    String coordinates = vNodes.get(k).getValue()+"k"+bx+by+"R"+rx+ry+"K"+wx+wy+";";
                    nodeCoords.add(new Coords(idCount, coordinates, widthInterval * (k + 1), heightIntervals * (i + 1), vNodes.get(k).getNodeType()));
                    nodeCount++;
                    idCount++;

                    if (Objects.equals(nodeCount, fullWidth)) 
                    {
                        break;
                    }
                }

                for (int k = 0; k < nodeCount; k++) 
                {
                    vNodes.removeFirst();
                }

                if (Objects.equals(nodeCount, fullWidth)) 
                {
                    vLevelWidth.removeFirst();
                    break;
                }
            }
        }

        LinkedList<Point> froms = new LinkedList<>();
        LinkedList<Point> tos = new LinkedList<>();
        char[] fromType = new char[nodeCoords.size() - (model.getVLevelWidth().getLast())];
        char[] toType = new char[nodeCoords.size()-1];

        for (int i = 0; i < nodeCoords.size() - (model.getVLevelWidth().getLast()); i++) 
        {
            froms.add(new Point(nodeCoords.get(i).getXCoord(), nodeCoords.get(i).getYCoord()));
            fromType[i] = nodeCoords.get(i).getNodeType();
        }
        
        for (int i = 1; i < nodeCoords.size(); i++) 
        {
            tos.add(new Point(nodeCoords.get(i).getXCoord(), nodeCoords.get(i).getYCoord()));
            toType[i-1] = nodeCoords.get(i).getNodeType();
        }

        Integer connectedCount;
        Integer toCount = 0;

        for (int i = 0; i < froms.size(); i++) 
        {
            connectedCount = 0;
            for (int j = 0; j < tos.size(); j++) 
            {
                connections.add(new Connection(froms.get(i), tos.get(j), fromType[i], toType[toCount]));
                
                connectedCount++;

                if (j + 1 == potentMoveSize.get(0)) 
                {
                    potentMoveSize.removeFirst();
                    for (int k = 0; k < connectedCount; k++) 
                    {
                        tos.removeFirst();
                        toCount++;
                    }

                    break;
                }
            }
        }
        
        originNodeCoords.add(new Coords(nodeCoords.get(0).getID(), nodeCoords.get(0).getInfo(), nodeCoords.get(0).getXCoord(), nodeCoords.get(0).getYCoord(), null));
        movingNode.add(new Coords(nodeCoords.get(0).getID(), nodeCoords.get(0).getInfo(), nodeCoords.get(0).getXCoord(), nodeCoords.get(0).getYCoord(), null, '1'));
        
        Integer nodeCount = 2;
        Integer originNodeCount = 1;

        boolean empty = false;
        
        while(!empty)
        {
            if(originCount.isEmpty())
            {
                break;
            }

            for(int i=0;i<originCount.size();i++)
            {
                Integer oCount = originCount.get(i);
                
                for(int j=0;j<oCount;j++)
                {
                    originNodeCoords.add(new Coords(nodeCount, nodeCoords.get(nodeCount-1).getInfo(), nodeCoords.get(nodeCount-1).getXCoord(), nodeCoords.get(nodeCount-1).getYCoord(), originNodeCount));
                    movingNode.add(new Coords(nodeCount, nodeCoords.get(nodeCount-1).getInfo(), nodeCoords.get(nodeCount-1).getXCoord(), nodeCoords.get(nodeCount-1).getYCoord(), originNodeCount, nodeCoords.get(nodeCount-1).getNodeType()));
                    nodeCount++;
                }
                
                originNodeCount++;
                originCount.removeFirst();
                break;
            }
        } 
        
        for(int i=0;i<nodeCoords.size();i++)
        {
            if(Objects.equals(bestMoveID, nodeCoords.get(i).getID()))
            {
                pathNodes.add(new Coords(bestMoveID, nodeCoords.get(i).getInfo(), nodeCoords.get(i).getXCoord(), nodeCoords.get(i).getYCoord(), nodeCoords.get(i).getNodeType()));
                break;
            }
        }

        for(int i=0;i<steps+steps+2;i++)
        {
            Integer pathID = pathNodes.get(i).getID();
            Integer originID = 0;

            for(int j=0;j<originNodeCoords.size();j++)
            {
                if(Objects.equals(pathID, originNodeCoords.get(j).getID()))
                {
                    originID = originNodeCoords.get(j).getOrigin();
                    break;
                }
            }

            for(Coords coord : nodeCoords)
            {
                if(Objects.equals(originID, coord.getID()))
                {
                   pathNodes.add(new Coords(coord.getID(), coord.getInfo(), coord.getXCoord(), coord.getYCoord(), coord.getNodeType()));
                   break;
                }
            }
        } 
        
        for(int i=0;i<pathNodes.size();i++)
        {
            if(pathNodes.get(i).getNodeType()=='6')
            {
                pathNodes.remove(i);
            }
        }
    }

    public LinkedList<Coords> getNodeCoords() 
    {
        return nodeCoords;
    }

    public LinkedList<Connection> getConnections() 
    {
        return connections;
    }
    
    public LinkedList<Coords> getPathNodes()
    {
        return pathNodes;
    }
}
