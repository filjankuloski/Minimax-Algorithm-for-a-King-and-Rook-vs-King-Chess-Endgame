package javaapplication11;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JavaApplication11
{ 
    public static Integer checkmateCount1 = 0;
    public static Integer checkmateCount2 = 0;
    public static Integer rookCaptureCount1 = 0;
    public static Integer rookCaptureCount2 = 0;
    public static Integer fiftyMoveCount1 = 0;
    public static Integer fiftyMoveCount2 = 0;
    public static Integer stalemateCount1 = 0;
    public static Integer stalemateCount2 = 0;
    public static Integer threefoldRepCount1 = 0;
    public static Integer threefoldRepCount2 = 0;
    public static Integer totalMoves1 = 0;
    public static Integer totalMoves2 = 0;
    
    public static void main(String[] args) 
    {
        BufferedReader reader;
        BufferedReader reader2;
        Integer counter = 0;
        
        try 
        {
            reader = new BufferedReader(new FileReader("legalpositions.txt"));
            
            while (counter<175168)
            {
		String line = reader.readLine();
                File f = new File("C:\\Users\\Filemon HP\\Documents\\NetBeansProjects\\JavaApplication11\\JavaApplication11\\LegalPositionTests\\"+line+".txt");
                try
                {
                    reader2 = new BufferedReader(new FileReader(f));
                    String line2 = reader2.readLine();
                    if (!f.createNewFile()) 
                    {
                        boolean done = false;
                        Integer steps = 0;
                        
                        while(!done)
                        {
                            line2 = reader2.readLine();
                            if(line2!=null)
                            {
                                if(line2.length()>=22&&!line2.isEmpty()&&"1-step Minimax/Maximin".equals(line2.substring(0,22)))
                                {
                                    steps = 1;
                                }
                                else if(line2.length()>=22&&!line2.isEmpty()&&"2-step Minimax/Maximin".equals(line2.substring(0,22)))
                                {
                                    steps = 2;
                                }
                                
                                if(line2.length()>=11&&(!line2.isEmpty())&&"Moves taken".equals(line2.substring(0,11)))
                                {
                                    String numOfMoves = "";
                                    char[] stringChars = line2.toCharArray();
                                    
                                    for (char c : stringChars) 
                                    {
                                        if(Character.isDigit(c)) 
                                        {
                                            numOfMoves+=c;
                                        }
                                    }
                                    
                                    if(steps==1)
                                    {
                                        totalMoves1 += Integer.parseInt(numOfMoves);
                                    }
                                    else
                                    {
                                        totalMoves2 += Integer.parseInt(numOfMoves);
                                    }
                                }

                                if(line2.length()>=7&&(!line2.isEmpty())&&"Outcome".equals(line2.substring(0,7)))
                                {
                                    if("Checkmate".equals(line2.substring(9, line2.length())))
                                    {
                                        if(steps==1)
                                        {
                                            checkmateCount1++;
                                        }
                                        else
                                        {
                                            checkmateCount2++;
                                        }
                                    }
                                    else if("Stalemate".equals(line2.substring(9, line2.length())))
                                    {
                                        if(steps==1)
                                        {
                                            stalemateCount1++;
                                        }
                                        else
                                        {
                                            stalemateCount2++;
                                        }
                                    }
                                    else if("Threefold Repetition Draw".equals(line2.substring(9, line2.length())))
                                    {
                                        if(steps==1)
                                        {
                                            threefoldRepCount1++;
                                        }
                                        else
                                        {
                                            threefoldRepCount2++;
                                        }
                                    }
                                    else if("50-Move-Rule Draw".equals(line2.substring(9, line2.length())))
                                    {
                                        if(steps==1)
                                        {
                                            fiftyMoveCount1++;
                                        }
                                        else
                                        {
                                            fiftyMoveCount2++;
                                        }
                                    }
                                    else if("Rook Capture Draw".equals(line2.substring(9, line2.length())))
                                    {
                                        if(steps==1)
                                        {
                                            rookCaptureCount1++;
                                        }
                                        else
                                        {
                                            rookCaptureCount2++;
                                        }
                                    }
                                }
                            }  
                            else
                            {
                                break;
                            } 
                        }
                    } 

                    counter++;
                    reader2.close();
                }
                catch (IOException e) 
                {
                    e.printStackTrace();
                }  
            }
            
            reader.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        System.out.println("1 step Checkmate: "+checkmateCount1);
        System.out.println("2 step Checkmate: "+checkmateCount2);
        System.out.println("1 step Stalemate: "+stalemateCount1);
        System.out.println("2 step Stalemate: "+stalemateCount2);
        System.out.println("1 step 50 move rule: "+fiftyMoveCount1);
        System.out.println("2 step 50 move rule: "+fiftyMoveCount2);
        System.out.println("1 step 3fold rep: "+threefoldRepCount1);
        System.out.println("2 step 3fold rep: "+threefoldRepCount2);
        System.out.println("1 step rook capture: "+rookCaptureCount1);
        System.out.println("2 step rook capture: "+rookCaptureCount2);
        System.out.println("1 step total moves: "+totalMoves1);
        System.out.println("2 step total moves: "+totalMoves2);
    }
}