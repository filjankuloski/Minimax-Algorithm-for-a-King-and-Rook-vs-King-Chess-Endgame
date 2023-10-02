package javaapplication12;

public class Coords
{
    private String info;
    Integer x, y, value, id, origin;
    char nodeType;
    
    public Coords(){}
    
    public Coords(Integer x, Integer y)
    {
        this.x = x;
        this.y = y;
    }
    
    public Coords(Integer x, Integer y, Integer value)
    {
        this.x = x;
        this.y = y;
        this.value = value;
    }
    
    public Coords(Integer x, Integer y, char nodeType)
    {
        this.x = x;
        this.y = y;
        this.nodeType = nodeType;
    }
    
    public Coords(Integer id, String info, Integer x, Integer y, char nodeType)
    {
        this.id = id;
        this.info = info;
        this.x = x;
        this.y = y;
        this.nodeType = nodeType;
    }
    
    public Coords(Integer id, String info, Integer x, Integer y, Integer origin)
    {
        this.id = id;
        this.info = info;
        this.x = x;
        this.y = y;
        this.origin = origin;
    }
    
    public Coords(Integer id, String info, Integer x, Integer y, Integer origin, char nodeType)
    {
        this.id = id;
        this.info = info;
        this.x = x;
        this.y = y;
        this.origin = origin;
        this.nodeType = nodeType;
    }
    
    public String getInfo() 
    {
        return info;
    }
    
    public Integer getXCoord()
    {
        return x;
    }
    
    public Integer getYCoord()
    {
        return y;
    }  
    
    public Integer getValue()
    {
        return value;
    }
    
    public Integer getID()
    {
        return id;
    }
    
    public char getNodeType()
    {
        return nodeType;
    }
    
    public Integer getOrigin()
    {
        return origin;
    }
}