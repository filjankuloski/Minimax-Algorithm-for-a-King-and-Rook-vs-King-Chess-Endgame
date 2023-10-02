package chessgui;
import static chessgui.Board.highlightCounter;
import static chessgui.Board.idCounter;
import static chessgui.Board.next;
import static chessgui.Board.prev;
import static chessgui.Board.spHeight;
import static chessgui.Board.spWidth;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class Diagram extends JPanel implements ActionListener
{
    private ViewModel viewModel;

    public Diagram(ViewModel viewModel)
    {
        this.viewModel = viewModel;
        Font currentFont = getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 1F);
        setFont(newFont);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        for(Connection connection : viewModel.getConnections())
        {
            Point from = connection.getFrom();
            Point to = connection.getTo();
            char fromType = connection.getFromNodeType();
            char toType = connection.getToNodeType();
            
            if(fromType=='6'||toType=='6')
            {
                g2d.setColor(Color.WHITE);
                g2d.drawLine(from.x, from.y, to.x, to.y);
            }
            else
            {
                g2d.setColor(Color.BLACK);
                g2d.drawLine(from.x, from.y, to.x, to.y);
            }
        }

        for(Coords coord : viewModel.getNodeCoords())
        {
            if(coord.getInfo()!=null)
            {
                g2d.setColor(Color.WHITE);
                g2d.fillRect(coord.getXCoord()-15, coord.getYCoord()-15, 110, 20);
                
                switch(coord.getNodeType())
                {
                    case '0': // STANDARD
                        g2d.setColor(Color.BLACK);
                        break;
                    case '1': // CHECKMATE
                        g2d.setColor(Color.BLUE);
                        break;
                    case '2': // STALEMATE
                        g2d.setColor(Color.GRAY);
                        break;
                    case '3': // THREE WAY REPETITION
                        g2d.setColor(Color.MAGENTA);
                        break;
                    case '4': // ROOK CAPTURE
                        g2d.setColor(Color.PINK);
                        break;
                    case '5': // 50 MOVE DRAW 
                        g2d.setColor(Color.ORANGE);
                        break;
                    case '6': // NONEXISTENTIAL
                        g2d.setColor(Color.WHITE);
                        break;   
                }
                g2d.drawString(coord.getInfo(), coord.getXCoord(), coord.getYCoord());
            }
        }

        Font cf = getFont();
        g2d.setFont(new Font("default", Font.BOLD, 12));
        g2d.setColor(Color.GREEN);

        LinkedList<Coords> pathNodes = viewModel.getPathNodes();

        for(int i=0;i<pathNodes.size()-1;i++)
        {
            g2d.drawLine(pathNodes.get(i).getXCoord(), pathNodes.get(i).getYCoord(), pathNodes.get(i+1).getXCoord(), pathNodes.get(i+1).getYCoord());
        }

        for(int i=0;i<pathNodes.size();i++)
        {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(pathNodes.get(i).getXCoord()-15, pathNodes.get(i).getYCoord()-15, 110, 20);
            g2d.setColor(Color.GREEN);
            g2d.drawString(pathNodes.get(i).getInfo(), pathNodes.get(i).getXCoord(), pathNodes.get(i).getYCoord());
        }

        if(idCounter>0)
        {
            g2d.setColor(Color.WHITE);
            g2d.fillRect(viewModel.getNodeCoords().get(idCounter-1).getXCoord()-15, viewModel.getNodeCoords().get(idCounter-1).getYCoord()-15, 110, 20);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("default", Font.BOLD, 12));
            g2d.drawString(viewModel.getNodeCoords().get(idCounter-1).getInfo(), viewModel.getNodeCoords().get(idCounter-1).getXCoord(), viewModel.getNodeCoords().get(idCounter-1).getYCoord());
        }

        g2d.setFont(cf);
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension(spWidth, spHeight);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource()==prev)
        {
            if(highlightCounter+1>viewModel.getPathNodes().size()-1)
            {
                highlightCounter = 0;
            }
            else
            {
                highlightCounter++;
            }
            
            Container c = SwingUtilities.getUnwrappedParent(this);
            JViewport viewport = (JViewport) c;
            
            boolean widthLimit = viewModel.getPathNodes().get(highlightCounter).getXCoord()-800<0;
            boolean heightLimit = viewModel.getPathNodes().get(highlightCounter).getYCoord()-800<0;

            if(highlightCounter==viewModel.getPathNodes().size()-1)
            {
                viewport.setViewPosition(new Point(viewModel.getPathNodes().get(highlightCounter).getXCoord()-800, viewModel.getPathNodes().get(highlightCounter).getYCoord()-80));
            }
            else if(!widthLimit&&!heightLimit)
            {
                viewport.setViewPosition(new Point(viewModel.getPathNodes().get(highlightCounter).getXCoord()-800, viewModel.getPathNodes().get(highlightCounter).getYCoord()-800));
            }
            else if(!widthLimit&&heightLimit)
            {
                viewport.setViewPosition(new Point(viewModel.getPathNodes().get(highlightCounter).getXCoord()-800, viewModel.getPathNodes().get(highlightCounter).getYCoord()));
            }
            else if(widthLimit&&heightLimit)
            {
                viewport.setViewPosition(new Point(viewModel.getPathNodes().get(highlightCounter).getXCoord(), viewModel.getPathNodes().get(highlightCounter).getYCoord()-800));
            }
        }
        
        if(e.getSource()==next)
        {
            if(highlightCounter-1<0)
            {
                highlightCounter = viewModel.getPathNodes().size()-1;
            }
            else
            {
                highlightCounter--;
            }

            Container c = SwingUtilities.getUnwrappedParent(this);
            JViewport viewport = (JViewport) c;

            boolean widthLimit = viewModel.getPathNodes().get(highlightCounter).getXCoord()-800<0;
            boolean heightLimit = viewModel.getPathNodes().get(highlightCounter).getYCoord()-800<0;

            if(highlightCounter==viewModel.getPathNodes().size()-1)
            {
                viewport.setViewPosition(new Point(viewModel.getPathNodes().get(highlightCounter).getXCoord()-800, viewModel.getPathNodes().get(highlightCounter).getYCoord()-80));
            }
            else if(!widthLimit&&!heightLimit)
            {
                viewport.setViewPosition(new Point(viewModel.getPathNodes().get(highlightCounter).getXCoord()-800, viewModel.getPathNodes().get(highlightCounter).getYCoord()-800));
            }
            else if(!widthLimit&&heightLimit)
            {
                viewport.setViewPosition(new Point(viewModel.getPathNodes().get(highlightCounter).getXCoord()-800, viewModel.getPathNodes().get(highlightCounter).getYCoord()));
            }
            else if(widthLimit&&!heightLimit)
            {
                viewport.setViewPosition(new Point(viewModel.getPathNodes().get(highlightCounter).getXCoord(), viewModel.getPathNodes().get(highlightCounter).getYCoord()-800));
            }
        }
    }
}