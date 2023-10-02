package chessgui;
import static chessgui.Board.d;
import static chessgui.Board.idCounter;
import static chessgui.Board.steps;
import static chessgui.pieces.positions.movingNode;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;

public class CustomKeyListener extends MouseAdapter
{
    public static Integer selectedWidth = 0;
    public static Integer selectedHeight = 0;

   @Override
   public void mousePressed(MouseEvent e) 
   {
       Component c = e.getComponent();
       Container p = SwingUtilities.getUnwrappedParent(c);
       JViewport vp = (JViewport) p;
       
       KeyListener listener = new KeyListener() 
       {
           @Override
           public void keyPressed(KeyEvent e) 
           {
               Integer height = 0; 
               Integer width = 0;
               
               switch(e.getKeyCode()) 
               { 
                    case KeyEvent.VK_UP:  ////////////////////////// UP KEY
                        if(idCounter!=0)
                        {
                            for(int i=0;i<movingNode.size();i++)
                            {
                                if(Objects.equals(movingNode.get(idCounter-1).getOrigin(), movingNode.get(i).getID()))
                                {
                                    idCounter = movingNode.get(i).getID();
                                    width = movingNode.get(i).getXCoord();
                                    height = movingNode.get(i).getYCoord();
                                }
                            }
                        }
                        break;

                    case KeyEvent.VK_DOWN: ///////////////////////// DOWN KEY
                        if(idCounter==0)
                        {
                            width = movingNode.get(0).getXCoord();
                            height = movingNode.get(0).getYCoord();
                            idCounter = 1;
                        }
                        else
                        {
                            for(int i=0;i<movingNode.size();i++)
                            {
                                if(Objects.equals(movingNode.get(i).getOrigin(), idCounter))
                                {
                                    if(movingNode.get(i).getNodeType()!='6')
                                    {
                                        idCounter = movingNode.get(i).getID();
                                        width = movingNode.get(i).getXCoord();
                                        height = movingNode.get(i).getYCoord();
                                        break;
                                    }
                                    
                                }
                            }
                        }
                        
                        break;

                    case KeyEvent.VK_LEFT: ////////////////////////////// LEFT KEY 
                        if(idCounter!=0&&idCounter!=1)
                        {
                            if(Objects.equals(movingNode.get(idCounter-1).getOrigin(), movingNode.get(idCounter-2).getOrigin()))
                            {
                                idCounter--;
                                width = movingNode.get(idCounter-1).getXCoord();
                                height = movingNode.get(idCounter-1).getYCoord();
                            }
                        }
                        break;

                    case KeyEvent.VK_RIGHT: //////////////////////////////// RIGHT KEY
                        if(idCounter!=0&&idCounter!=1)
                        {
                            if(Objects.equals(movingNode.get(idCounter-1).getOrigin(), movingNode.get(idCounter).getOrigin()))
                            {
                                idCounter++;
                                width = movingNode.get(idCounter-1).getXCoord();
                                height = movingNode.get(idCounter-1).getYCoord();
                            }
                        }
                        
                        break;
                }

                boolean widthLimit = width-800<0;
                boolean heightLimit = height-800<0;

                if(width!=0&&height!=0)
                {
                    if(idCounter==1)
                    {
                        vp.setViewPosition(new Point(width-800, height-80));
                    }
                    else if(!widthLimit&&!heightLimit)
                    {
                        if(steps==0)
                        {
                            vp.setViewPosition(new Point(width-800, height-800));
                        }
                        else if(steps==1)
                        {
                            vp.setViewPosition(new Point(width-800, height-400));
                        }   
                    }
                    else if(!widthLimit&&heightLimit)
                    {
                        vp.setViewPosition(new Point(width-800, height));
                    }
                    else if(widthLimit&&!heightLimit)
                    {
                        if(steps==0)
                        {
                            vp.setViewPosition(new Point(width, height-800));
                        }
                        else if(steps==1)
                        {
                            vp.setViewPosition(new Point(width-20, height-400));
                        }
                    }
                    else
                    {
                        vp.setViewPosition(new Point(width-20, height));
                    }
                } 
            }
            
            @Override
            public void keyReleased(KeyEvent event) {}
            
            @Override
            public void keyTyped(KeyEvent event) {}
        };
        
        d.addKeyListener(listener);
        d.requestFocusInWindow();
        d.setFocusable(true);
   }
}