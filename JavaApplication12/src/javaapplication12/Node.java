package javaapplication12;
import java.util.Comparator;

public class Node implements Comparator<Node>
{   
    Integer enumeration;
    Integer value;
    Integer origin;
    Integer ID;
    char nodeType;
    
    public Node(){}
    
    public Node(Integer enumeration, Integer value)
    {
        this.enumeration = enumeration;
        this.value = value;
    }
    
    public Node(Integer enumeration, Integer value, Integer origin)
    {
        this.enumeration = enumeration;
        this.value = value;
        this.origin = origin;
    }
    
    public Node(Integer enumeration, Integer value, Integer origin, char nodeType)
    {
        this.enumeration = enumeration;
        this.value = value;
        this.origin = origin;
        this.nodeType = nodeType;
    }
    
    public Node(Integer ID, Integer enumeration, Integer value, Integer origin)
    {
        this.ID = ID;
        this.enumeration = enumeration;
        this.value = value;
        this.origin = origin;
    }
    
    public Integer getEnum()
    {
        return enumeration;
    }
    
    public Integer getValue()
    {
        return value;
    }
    
    public Integer getOrigin()
    {
        return origin;
    }
    
    public Integer getID()
    {
        return ID;
    }
    
    public char getNodeType()
    {
        return nodeType;
    }
    
    @Override
    public int compare(Node c1, Node c2)
    {
        return c1.getValue().compareTo(c2.getValue());
    }
}
