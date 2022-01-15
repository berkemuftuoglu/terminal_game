import java.util.*;

/**
*  This class is the main class of the "Game of Rings" application. 
*  "Game of Rings" is a very simple, text based adventure game.  Users 
*  are after a great mission of saving the world!
*  
*  Players are the part of the game. Every player object represents a single player.
*  A player can 
*  
*  
 * @author Berke Müftüoğlu
 * @version 2020.11.21
 */

public class Player
{
    private String name;
    private int maxWeight;
    private Room location;
    private boolean alive;
    private Size size;
    private Type role;
    
    /**
     * Constructor for objects of class Players
     */
    public Player(String name, int maxWeight, Type role, Room location, Size size)
    {
        this.name = name;
        this.maxWeight = maxWeight;
        this.role = role;
        this.location = location;
        this.size = size;
        alive = true;
 
    }
    
    /**
     * Kills a player by making the alive boolean false
     */
    public void dead(){
        alive = false;
    }
    
    /**
     * @return the name of the player
     */
    public String getName(){
        return name;
    }
    
    /**
     * @return the type of the player
     */
    public Type getType(){
        return role;
    }
    
    /**
     * @return the maximum weight that player can carry
     */
    public int getMaxWeight(){
        return maxWeight;
    }
    
    /**
     * @return the size 
     */
    public Size size(){
        return size;
    }
    
    /**
     * @return the alive boolean
     */
    public boolean getAlive(){
        return alive;
    }
    
    /**
     * Gets the presedence order as a number of a player
     */
    public int getPresedenceOrder(){
       return role.getPresedenceOrder(getType());
    }

    /**
     * Challenges wheter a player can carry its current weight
     * @param weight that the player is carrying
     */
    public boolean canCarry(int currentWeight){
        
        if(currentWeight > maxWeight){
            return false;
        }
        
        return true;
    }
    
    /**
     * Challneges the player wheter he can pick up the item
     * @return true or false
     */
    public boolean canPickUp(Item item){
        
        
        if(getPresedenceOrder() < item.getPresedenceOrder()){
            return false;
        }
        
        return true;
    }
    
    /**
     * @return the room player is in
     */
    public Room getLocation(){
        return location;
    }
    
    /**
     * change the location of the player
     */
    public void changeLocation(Room room){
        location = room;
    }
    
}
