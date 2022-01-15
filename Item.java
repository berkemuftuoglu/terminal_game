import java.util.ArrayList;

/**
 * This class is the main class of the "Game of Rings" application. 
 * "Game of Rings" is a very simple, text based adventure game.  Users 
 * are after a great mission of saving the world!
 * 
 * A "item" represents a item in the game which can be used to in various ways
 * like killing
 *  
 * @author Berke Müftüoğlu
 * @version 2020.11.22
 */
public class Item
{
    private int weight;
    private String name;
    private Type role;
    private Room rlocation;
    private Player plocation;
    /**
     * Constructor for objects of class Item
     */
    public Item(String name, int weight, Type role, Room rlocation, Player plocation)
    {
        this.weight = weight;
        this.name = name;
        this.role = role;
        this.rlocation = rlocation;
        this.plocation = plocation;

    }

    /**
     * @return the weight of the object
     */
    public int getWeight()
    {
        return weight;
    }
    
    /**
     * @return the weight of the item
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @return the player location of an item
     */
    public Player getpLocation()
    {
        return plocation;
    }
    
    /**
     * @return the player location of an item
     */
    public Type getType()
    {
        return role;
    }
    
    /**
     * @return the room location of an item. The "r" refers to room
     */
    public Room getrLocation()
    {
        return rlocation;
    }
    
    /**
     * updates the room location of an item
     */
    public void updaterLocation(Room room){
        rlocation = room;
    }
    
    /**
     * updates the player location of an item. The "p" refers to player
     */
    public void updatepLocation(Player player){
        plocation = player;
    }
        
    /**
     * Gets the presedence order a number
     */
    public int getPresedenceOrder(){
        return role.getPresedenceOrder(getType());
    }
}
