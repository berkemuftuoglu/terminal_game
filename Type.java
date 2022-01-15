import java.util.*;
/**
 * Enumeration class Presedence - write a description of the enum class here
 *
 * @author Berke Müftüoğlu
 * @version (version number or date here)
 */
public enum Type
{
    // A value for each presedence word along with its
    // corresponding user interface string.
    MAGICIAN("magician"), GOLLUM("gollum"), DWARF("dwarf");
    
    // The presedence string.
    private String presedenceString;
    private ArrayList<Type> presedence = new ArrayList<Type>();
    private int called = 0; //sotres a the amount times a mehotd is called.
    
    /**
     * Initialise with the corresponding presedence string.
     * @param presedenceString The presedence string.
     */
    Type(String presedenceString)
    {
        this.presedenceString = presedenceString;
    }
    
    /**
     * @return The presedence word as a string.
     */
    public String toString()
    {
        return presedenceString;
    }
    
    /**
     * Fills the presence order
     */
    private void setPresedenceOrder(){
        //Only calls the method once.
        //Since there is a enum class, I couldn't make anything else work.
        
        if(called == 0){
          /*
          * Here is the sturture of the presedence concept:
          * Since the items are added to the end they have a higher index
          * I have used that index to indicate their presedence so in this implementation this is the order of predence:
          * 
          * Magician > Dwarf > Gollum
          * 
          * Since thier indicies are 2,1 and 0 respectively
          * 
          */
          
          presedence.add(GOLLUM);
          presedence.add(DWARF);
          presedence.add(MAGICIAN);
          called++;
        }
        
        
    }
    
    /**
     * Gets the presedence order as a number
     */
    public int getPresedenceOrder(Type type){
       
       setPresedenceOrder();
       return presedence.indexOf(type);
    }

}
