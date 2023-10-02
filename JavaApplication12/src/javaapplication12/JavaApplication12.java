package javaapplication12;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class JavaApplication12 implements positions
{
    public static Integer levelCounter = 0;
    public static Integer enumer = 0;
    public static Integer turnCounter = 0;
    public static Integer moveNumber = 0;
    public static String moveNotation = "";
    public static String outcome = "";
    public static Integer chosenEnumeration = 0;
    
    public static int[] moveCounter = new int[262144];
    public static boolean afterblock = false;
    public static boolean end = false;
    
    public static boolean checkmateFlag = false; 
    public static boolean stalemateFlag = false;
    public static boolean repetitionFlag = false;
    public static boolean rookCaptureFlag = false;
    public static boolean fiftyMoveFlag = false;
    
    public static void main(String[] args) 
    {
        BufferedReader reader;
        Integer counter = 0;
        
        try 
        {
            reader = new BufferedReader(new FileReader("legalpositions.txt"));
            
            while (counter<100) // put total number of enumerations you want to test here
            {
		String line = reader.readLine();
                File f = new File("C:\\Users\\Filemon HP\\Documents\\NetBeansProjects\\JavaApplication12\\LegalPositionTests\\"+line+".txt");
                
                if (f.createNewFile()) 
                {
                    System.out.println(counter);
                } 
                else 
                {
                    System.out.println("File already exists.");
                }
                
                FileWriter writer = new FileWriter("C:\\Users\\Filemon HP\\Documents\\NetBeansProjects\\JavaApplication12\\LegalPositionTests\\"+line+".txt");

                Integer startingEnumeration = Integer.parseInt(line);
                
                writer.write("Starting position: "+startFEN(startingEnumeration));
                writer.write("\n\n1-step Minimax/Maximin\n");
                
                chosenEnumeration = startingEnumeration;
                
                while(!end)
                {
                    moveNumber++;
                    moveNotation+=moveNumber+". ";
                    
                    if(!end)
                    {
                        whiteMinimax(chosenEnumeration, 0);
                    }
                    
                    writer.write(moveNotation);
                    resetParameters();
                    
                    if(!end)
                    {
                        blackMaximin(chosenEnumeration, 0);
                    }
                    
                    writer.write(moveNotation);
                    resetParameters();
                } 
                
                writer.write("\nMoves taken: "+moveNumber+"\n");
                writer.write(outcome);
                writer.write("\n\n2-step Minimax/Maximin\n");
                resetParameters(startingEnumeration);

                while(!end)
                {
                    moveNumber++;
                    moveNotation+=moveNumber+". ";
                    
                    if(!end)
                    {
                        whiteMinimax(chosenEnumeration, 1);
                    }
                    
                    writer.write(moveNotation);
                    resetParameters();
                    
                    if(!end)
                    {
                        blackMaximin(chosenEnumeration, 1);
                    }
                    
                    writer.write(moveNotation);
                    resetParameters();
                }
                
                writer.write("\nMoves taken: "+moveNumber+"\n");
                writer.write(outcome);
                resetParameters(startingEnumeration);

                writer.close();
                counter++;
            }
            
            reader.close();
        
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public static String whitePieceMoved(Integer orx, Integer ory, Integer rx, Integer ry)
    {
        if(!(Objects.equals(orx, rx)&&Objects.equals(ory, ry)))
        {
            return "R";
        }
        else
        {
            return "K";
        }
    }
    
    public static String notationConversion(Integer x, Integer y)
    {
        String notation = "";
        
        switch(x)
        {
            case 0:
                notation+="a";
                break;
            case 1:
                notation+="b";
                break;
            case 2:
                notation+="c";
                break;
            case 3:
                notation+="d";
                break;
            case 4:
                notation+="e";
                break;
            case 5:
                notation+="f";
                break;
            case 6:
                notation+="g";
                break;
            case 7:
                notation+="h";
                break;     
        }
        
        switch(y)
        {
            case 0:
                notation+="8";
                break;
            case 1:
                notation+="7";
                break;
            case 2:
                notation+="6";
                break;
            case 3:
                notation+="5";
                break;
            case 4:
                notation+="4";
                break;
            case 5:
                notation+="3";
                break;
            case 6:
                notation+="2";
                break;
            case 7:
                notation+="1";
                break;     
        }
        
        return notation;
    }
    
    public static String startFEN(Integer enumeration)
    {
        reverseEnum(enumeration);
        
        Integer bx = coords.get(0).getXCoord();
        Integer by = coords.get(0).getYCoord();
        Integer rx = coords.get(1).getXCoord();
        Integer ry = coords.get(1).getYCoord();
        Integer wx = coords.get(2).getXCoord();
        Integer wy = coords.get(2).getYCoord();
        
        Integer emptySquares;
        String FEN = "";
        boolean pieceFound = false;
        
        for(int y=0;y<8;y++)
        {
            emptySquares = 0;
            for(int x=0;x<8;x++)
            {
                if(x==bx&&y==by)
                {
                    if(emptySquares!=0)
                    {
                        FEN+=emptySquares;
                    }
                    FEN+="k";
                    emptySquares = 0;
                    pieceFound = true;
                }
                
                if(x==rx&&y==ry)
                {
                    if(emptySquares!=0)
                    {
                        FEN+=emptySquares;
                    }
                    FEN+="R";
                    emptySquares = 0;
                    pieceFound = true;
                }
                
                if(x==wx&&y==wy)
                {
                    if(emptySquares!=0)
                    {
                        FEN+=emptySquares;
                    }
                    FEN+="K";
                    emptySquares = 0;
                    pieceFound = true;
                }
                
                if(pieceFound)
                {
                    pieceFound = false;
                }
                else
                {
                    emptySquares++;
                }
                
                
                if(x==7&&y!=7)
                {
                    if(emptySquares!=0)
                    {
                        FEN+=emptySquares;
                    }
                    FEN+="/";
                }
                
                if(x==7&&y==7)
                {
                    if(emptySquares!=0)
                    {
                        FEN+=emptySquares;
                    }
                }
            }
        }
        
        return FEN;
    }
    
    public static void resetParameters()
    {
        moveNotation="";
        levelCounter = 0;
        checkmateFlag = false; 
        stalemateFlag = false;
        repetitionFlag = false;
        rookCaptureFlag = false;
        fiftyMoveFlag = false;
        whiteNodes.clear();
        blackNodes.clear();
        maxBlack.clear();
        minWhite.clear();
        tempBlack.clear();
        tempWhite.clear();
        subtree.clear();
    }
    
    public static void resetParameters(Integer enumeration)
    {
        chosenEnumeration = enumeration;
        end = false;
        turnCounter = 0;
        moveNumber = 0;
        outcome = "";
        for(int i=0;i<262144;i++)
        {
            moveCounter[i] = 0;
        }
    }
    
    public static void whiteMinimax(Integer enumeration, Integer steps)
    {
        for(int k=0;k<=steps;k++)
        {
            levelCounter++;
            
            if(blackNodes.isEmpty())
            {
                allWhiteMoves(enumeration, -1);
            }
            else
            {
                whiteNodes.clear();
                for(int i=0;i<blackNodes.size();i++)
                {
                    if(blackNodes.get(i).getNodeType()=='0')
                    {
                        allWhiteMoves(blackNodes.get(i).getEnum(), blackNodes.get(i).getOrigin());
                    }
                    else
                    {
                        whiteNodes.add(new Node(-1, blackNodes.get(i).getValue(), blackNodes.get(i).getOrigin(), '6'));
                        subtree.add(1);
                    }
                }
            }
            
            subtree.add(-1);

            blackNodes.clear();
            levelCounter++;
            
            for(int i=0;i<whiteNodes.size();i++)
            {
                if(k==0)
                {
                    if(whiteNodes.get(i).getNodeType()=='0')
                    {
                        allBlackMoves(whiteNodes.get(i).getEnum(), whiteNodes.get(i).getEnum());
                    }
                    else
                    {
                        blackNodes.add(new Node(-1, whiteNodes.get(i).getValue(), whiteNodes.get(i).getEnum(), '6'));
                        subtree.add(1);
                    }
                }
                else
                {
                    if(whiteNodes.get(i).getNodeType()=='0')
                    {
                        allBlackMoves(whiteNodes.get(i).getEnum(), whiteNodes.get(i).getOrigin());
                    }
                    else
                    {
                        blackNodes.add(new Node(-1, whiteNodes.get(i).getValue(), whiteNodes.get(i).getOrigin(), '6'));
                        subtree.add(1);
                    }
                }    
            }

            if(k!=steps)
            {
                subtree.add(-1);
            }
        }
        
        Integer removeCounter = 0;

        for(int i=steps+steps+1;i>=0;i--)
        {
            if(i%2==1)
            {
                for(int k=subtree.size()-1;k>=0;k--)
                {
                    Integer partitionSize = subtree.get(k);

                    if(partitionSize==-1)
                    {
                        removeCounter++;
                        break;
                    }

                    if(steps+steps+1==i)
                    {
                        for(int j=0;j<partitionSize;j++)
                        {
                            tempBlack.add(blackNodes.removeLast());
                        }
                    }
                    else
                    {
                        for(int j=0;j<partitionSize;j++)
                        {
                            tempBlack.add(maxBlack.removeLast());
                        }
                    }
                    
                    Collections.sort(tempBlack, new Node());
                    minWhite.addFirst(tempBlack.getLast());
                    tempBlack.clear();

                    removeCounter++;
                }

                for (int j=0;j<removeCounter;j++) 
                {
                    subtree.removeLast();
                }
            }
            else
            {
                for(int k=subtree.size()-1;k>=0;k--)
                {
                    Integer partitionSize = subtree.get(k);

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
                    subtree.removeLast();
                } 
            } 
            removeCounter = 0;
        }

        Integer bestMoveEnumeration;
        bestMoveEnumeration = maxBlack.get(0).getOrigin();
        
        reverseEnum(enumeration);
        
        Integer orx = coords.get(1).getXCoord();
        Integer ory = coords.get(1).getYCoord();
        
        reverseEnum(bestMoveEnumeration);
        
        Integer bx = coords.get(0).getXCoord();
        Integer by = coords.get(0).getYCoord();
        Integer rx = coords.get(1).getXCoord();
        Integer ry = coords.get(1).getYCoord();
        Integer wx = coords.get(2).getXCoord();
        Integer wy = coords.get(2).getYCoord();
        
        String pieceMoved = whitePieceMoved(orx, ory, rx, ry);
        String movedFile;
        if(pieceMoved.equalsIgnoreCase("R"))
        {
            movedFile = notationConversion(rx, ry);
        }
        else
        {
            movedFile = notationConversion(wx, wy);
        }
        String whiteMoveNotation = pieceMoved+movedFile;
        
        piecePositions[0] = bx * 8 + by;
        piecePositions[1] = rx * 8 + ry;
        piecePositions[2] = wx * 8 + wy;
        
        enumer = positionEnum(piecePositions[0], piecePositions[1], piecePositions[2]);
        moveCounter[enumer]++;
        turnCounter++;
        
        boolean checkmate = isCheckmate(wx, wy, rx, ry, bx, by);
        boolean stalemate = isStalemate(wx, wy, rx, ry, bx, by);

        if(checkmate) // O(J) + O(N)
        {
            outcome = "Outcome: Checkmate";
            whiteMoveNotation+="#";
            end = true;
        }
        if(stalemate)
        {
            outcome = "Outcome: Stalemate";
            whiteMoveNotation+="$";
            end = true;
        }
        if(moveCounter[enumer]==3)
        {
            outcome = "Outcome: Threefold Repetition Draw";
            whiteMoveNotation+="  ½-½";
            end = true;
        }
        if(!checkmate&&isCheck(wx, wy, rx, ry, bx, by)||((!checkmate)&&(Objects.equals(rx, bx)||Objects.equals(ry, by))&&!isBlocked(wx, wy, rx, ry, bx, by)))
        {
            whiteMoveNotation+="+";
        } 
        else if(afterblock)
        {
            whiteMoveNotation+="+";
        }  
        
        moveNotation+=whiteMoveNotation+" ";
        chosenEnumeration = bestMoveEnumeration;
    }
    
    public static void blackMaximin(Integer enumeration, Integer steps)
    {
        for(int k=0;k<=steps;k++)
        {
            levelCounter++;
            
            if(whiteNodes.isEmpty())
            {
                allBlackMoves(enumeration, -1);
            }
            else
            {
                blackNodes.clear();
                for(int i=0;i<whiteNodes.size();i++)
                {
                    if(whiteNodes.get(i).getNodeType()=='0')
                    {
                        allBlackMoves(whiteNodes.get(i).getEnum(), whiteNodes.get(i).getOrigin());
                    }
                    else
                    {
                        blackNodes.add(new Node(-1, -1, whiteNodes.get(i).getOrigin(), '6'));
                        subtree.add(1);
                    }
                }
            }
            
            subtree.add(-1);

            whiteNodes.clear();
            levelCounter++;
            
            for(int i=0;i<blackNodes.size();i++)
            {
                if(k==0)
                {
                    if(blackNodes.get(i).getNodeType()=='0')
                    {
                        allWhiteMoves(blackNodes.get(i).getEnum(), blackNodes.get(i).getEnum());
                    }
                    else
                    {
                        whiteNodes.add(new Node(-1, -1, blackNodes.get(i).getEnum(), '6'));
                        subtree.add(1);
                    }
                }
                else
                {
                    if(blackNodes.get(i).getNodeType()=='0')
                    {
                        allWhiteMoves(blackNodes.get(i).getEnum(), blackNodes.get(i).getOrigin());
                    }
                    else
                    {
                        whiteNodes.add(new Node(-1, -1, blackNodes.get(i).getOrigin(), '6'));
                        subtree.add(1);
                    }
                }    
            }

            if(k!=steps)
            {
                subtree.add(-1);
            }
            
        }
        
        Integer removeCounter = 0;
        
        /*for(int i=0;i<whiteNodes.size();i++)
        {
            minWhite.add(new Node(whiteNodes.get(i).getEnum(), whiteNodes.get(i).getValue(), whiteNodes.get(i).getOrigin()));
        }*/
        
        for(int i=steps+steps+1;i>=0;i--)
        {
            if(i%2==0)
            {
                for(int k=subtree.size()-1;k>=0;k--)
                {
                    Integer partitionSize = subtree.get(k);

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
                    subtree.removeLast();
                }
            }
            else
            {
                for(int k=subtree.size()-1;k>=0;k--)
                {
                    Integer partitionSize = subtree.get(k);

                    if(partitionSize==-1)
                    {
                        removeCounter++;
                        break;
                    }
                    
                    if(steps+steps+1==i)
                    {
                        for(int j=0;j<partitionSize;j++)
                        {
                            tempWhite.add(whiteNodes.removeLast());
                        }
                    }
                    else
                    {
                        for(int j=0;j<partitionSize;j++)
                        {
                            tempWhite.add(minWhite.removeLast());
                        }
                    }
                    
                    
                    Collections.sort(tempWhite, new Node());                   
                    maxBlack.addFirst(tempWhite.getFirst());
                    tempWhite.clear();

                    removeCounter++;
                }
                
                for(int j=0;j<removeCounter;j++)
                {
                    subtree.removeLast();
                }
                
            } 
            removeCounter = 0;
        }

        Integer bestMoveEnumeration;
        bestMoveEnumeration = minWhite.get(0).getOrigin();
        
        reverseEnum(bestMoveEnumeration);
        
        Integer bx = coords.get(0).getXCoord();
        Integer by = coords.get(0).getYCoord();
        Integer rx = coords.get(1).getXCoord();
        Integer ry = coords.get(1).getYCoord();
        Integer wx = coords.get(2).getXCoord();
        Integer wy = coords.get(2).getYCoord();
        
        String pieceMoved = "K";
        String capture = "";
        String movedFile = notationConversion(bx, by);
        String draw = "";
        
        piecePositions[0] = bx * 8 + by;
        piecePositions[1] = rx * 8 + ry;
        piecePositions[2] = wx * 8 + wy;
        
        enumer = positionEnum(piecePositions[0], piecePositions[1], piecePositions[2]);
        moveCounter[enumer]++;
        turnCounter++;
        
        boolean rookCapture = rookCaptureFunction(rx, ry, bx, by);
  
        if(turnCounter==100)
        {
            outcome = "Outcome: 50-Move-Rule Draw";
            draw = "  ½-½";
            end = true;
        }
        if(moveCounter[enumer]==3)
        {
            outcome = "Outcome: Threefold Repetition Draw";
            draw = "  ½-½";
            end = true;
        }
        if(rookCapture)
        {
            outcome = "Outcome: Rook Capture Draw";
            capture = "x";
            end = true;
        }

        moveNotation+=pieceMoved+capture+movedFile+draw+"   ";
        chosenEnumeration = bestMoveEnumeration;
    }
  
    public static void allWhiteMoves(Integer enumeration, Integer origin)
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
            subtree.add(moveCount);
        }
    }  
    
    public static void allBlackMoves(Integer enumeration, Integer origin)
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
            subtree.add(moveCount);
        }
    }
    
    public static Integer getRandomInteger(Integer maximum, Integer minimum) // O(1)
    { 
        return ((int) (Math.random()*(maximum - minimum))) + minimum; 
    }
    
    public static void reverseEnum(Integer enumeration)
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
    
    public static Integer positionEnumerationFunction(Integer bx, Integer by, Integer rx, Integer ry, Integer wx, Integer wy)
    {
        Integer bKing = bx * 8 + by;
        Integer wRook = rx * 8 + ry;
        Integer wKing = wx * 8 + wy;

        Integer enumeration = positionEnum(bKing, wRook, wKing);
        
        return enumeration;
    }
    
    public static Integer positionEnum(Integer a, Integer b, Integer c)
    {
        return (a*64*64)+(b*64)+c;
    }
    
    public static boolean isCheck(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
    {
        if(Objects.equals(rx, bx))
        {
            return true;
        }
        if(Objects.equals(ry, by))
        {
            return true;
        }
        
        return false;
    }
    
    public static boolean isCheckmate(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
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
    
    public static boolean isStalemate(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
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
    
    public static Integer blackMoveCounter(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
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
    
    public static boolean isBlocked(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by) // O(N)
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
    
    public static boolean rookCaptureFunction(Integer rx, Integer ry, Integer bx, Integer by)
    {
        boolean rookCapture = false;
        
        if(Objects.equals(rx, bx)&&Objects.equals(ry, by))
        {
            rookCapture = true;
        }
        
        return rookCapture;
    }
    
    public static boolean isOpposition(Integer bx, Integer by, Integer wx, Integer wy, Integer rx, Integer ry) // O(N)
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
    
    public static boolean passage(Integer bx, Integer by, Integer wx, Integer wy, Integer rx, Integer ry)
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
    
    public static void bKingReplace(Integer x, Integer y) // O(1)
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
    
    public static void tempBKingReplace(Integer x, Integer y) // O(1)
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
    
    public static void wKingReplace(Integer x, Integer y) // O(1)
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
    
    public static void tempWKingReplace(Integer x, Integer y) // O(1)
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
    
    public static void wRookReplace(Integer rx, Integer ry, Integer wx, Integer wy) // O(N) 
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
    
    public static void tempRookReplace(Integer rx, Integer ry, Integer wx, Integer wy) // O(N) 
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
    
    public static Integer positionEvaluator(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
    {
        Integer freeSquares = 0;
        Integer endGameValue = 0;
        Integer enumeration = positionEnumerationFunction(bx, by, rx, ry, wx, wy);
        
        if(isCheckmate(wx, wy, rx, ry, bx, by))
        {
            endGameValue = -1000;
            checkmateFlag = true;
            
            if(levelCounter==3)
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
    
    public static Integer freeSquaresFunction(Integer wx, Integer wy, Integer rx, Integer ry, Integer bx, Integer by)
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
    
    public static void whiteKingSquares(Integer x, Integer y)
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
}