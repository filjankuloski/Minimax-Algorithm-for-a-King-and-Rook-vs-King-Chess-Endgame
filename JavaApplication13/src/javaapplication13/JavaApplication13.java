package javaapplication13;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class JavaApplication13 
{
    public static LinkedList<Coords> wRookProtected = new LinkedList<Coords>();
    public static LinkedList<Coords> wKingProtected = new LinkedList<Coords>();
    
    public static void main(String[] args) 
    {
        try 
        {
            File file = new File("legalpositions.txt");
            if (file.createNewFile()) 
            {
                System.out.println("File created: " + file.getName());
            } 
            else 
            {
                System.out.println("File already exists.");
            }
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    
        Integer bx;
        Integer by;
        Integer rx;
        Integer ry;
        Integer wx;
        Integer wy;
        
        Integer legalCounter = 0;
        
        try 
        {
            FileWriter myWriter = new FileWriter("legalpositions.txt");
            
            for(int i=0;i<262144;i++)
            {
                Integer coords[] = new Integer[6];
                coords = reverseEnum(i);

                bx = coords[0];
                by = coords[1];
                rx = coords[2];
                ry = coords[3];
                wx = coords[4];
                wy = coords[5];

                boolean legal = isLegal(bx, by, rx, ry, wx, wy);

                if(legal)
                {
                    myWriter.write(""+i);
                    myWriter.write("\n");
                    legalCounter++;
                }
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } 
        catch (IOException e) 
        {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        
        
        System.out.println("legal counter: "+legalCounter);
    }
    
    static boolean isLegal(Integer bx, Integer by, Integer rx, Integer ry, Integer wx, Integer wy)
    {
        wKingReplace(wx, wy);
        wRookReplace(rx, ry, wx, wy);
            
        if(Objects.equals(bx, rx)&&Objects.equals(by, ry))
        {
            return false;
        }
        
        if(Objects.equals(bx, wx)&&Objects.equals(by, wy))
        {
            return false;
        }
        
        if(Objects.equals(wx, rx)&&Objects.equals(wy, ry))
        {
            return false;
        }
        
        for(int i=0;i<wKingProtected.size();i++)
        {
            if(Objects.equals(bx, wKingProtected.get(i).getXCoord())&&Objects.equals(by, wKingProtected.get(i).getYCoord()))
            {
                return false;
            }
        }
        
        for(int i=0;i<wRookProtected.size();i++)
        {
            if(Objects.equals(bx, wRookProtected.get(i).getXCoord())&&Objects.equals(by, wRookProtected.get(i).getYCoord()))
            {
                return false;
            }
        }
        
        return true;
    }
    
    static Integer[] reverseEnum(Integer enumeration)
    {
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
        
        Integer coords[] = new Integer[6];
        
        coords[0] = bx;
        coords[1] = by;
        coords[2] = rx;
        coords[3] = ry;
        coords[4] = wx;
        coords[5] = wy;
        
        return coords;
    }
    
    static void wKingReplace(Integer x, Integer y) // O(1)
    {
        wKingProtected.clear();
        if(x==0&&y==0) 
        {
            wKingProtected.add(new Coords(x+1, y));
            wKingProtected.add(new Coords(x+1, y+1));
            wKingProtected.add(new Coords(x, y+1));
        }
        else if(x==7&&y==0)
        {
            wKingProtected.add(new Coords(x-1, y));
            wKingProtected.add(new Coords(x-1, y+1));
            wKingProtected.add(new Coords(x, y+1));
        }
        else if(x==0&&y==7)
        {
            wKingProtected.add(new Coords(x, y-1));
            wKingProtected.add(new Coords(x+1, y-1));
            wKingProtected.add(new Coords(x+1, y));
        }
        else if(x==7&&y==7) 
        {
            wKingProtected.add(new Coords(x, y-1));
            wKingProtected.add(new Coords(x-1, y-1));
            wKingProtected.add(new Coords(x-1, y));
        }
        else if(x==0&&(y==1||y==2||y==3||y==4||y==5||y==6)) 
        {
            wKingProtected.add(new Coords(x, y-1));
            wKingProtected.add(new Coords(x+1, y-1));
            wKingProtected.add(new Coords(x+1, y));
            wKingProtected.add(new Coords(x+1, y+1));
            wKingProtected.add(new Coords(x, y+1));
        }
        else if(x==7&&(y==1||y==2||y==3||y==4||y==5||y==6))  
        {
            wKingProtected.add(new Coords(x, y-1));
            wKingProtected.add(new Coords(x-1, y-1));
            wKingProtected.add(new Coords(x-1, y));
            wKingProtected.add(new Coords(x-1, y+1));
            wKingProtected.add(new Coords(x, y+1));
        }
        else if((x==1||x==2||x==3||x==4||x==5||x==6)&&y==0) 
        {
            wKingProtected.add(new Coords(x-1, y));
            wKingProtected.add(new Coords(x-1, y+1));
            wKingProtected.add(new Coords(x, y+1));
            wKingProtected.add(new Coords(x+1, y+1));
            wKingProtected.add(new Coords(x+1, y));
        }
        else if((x==1||x==2||x==3||x==4||x==5||x==6)&&y==7) 
        {
            wKingProtected.add(new Coords(x-1, y));
            wKingProtected.add(new Coords(x-1, y-1));
            wKingProtected.add(new Coords(x, y-1));
            wKingProtected.add(new Coords(x+1, y-1));
            wKingProtected.add(new Coords(x+1, y));
        }
        else 
        {
            wKingProtected.add(new Coords(x, y-1));
            wKingProtected.add(new Coords(x+1, y-1));
            wKingProtected.add(new Coords(x+1, y));
            wKingProtected.add(new Coords(x+1, y+1));
            wKingProtected.add(new Coords(x, y+1));
            wKingProtected.add(new Coords(x-1, y+1));
            wKingProtected.add(new Coords(x-1, y));
            wKingProtected.add(new Coords(x-1, y-1));
        }
    }
    
    static void wRookReplace(Integer rx, Integer ry, Integer wx, Integer wy) // O(N) 
    {
        wRookProtected.clear();
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
}