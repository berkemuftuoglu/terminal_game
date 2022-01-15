
/**
 * This class represents all the sizes avilable in the game
 * I have used it for items but later it can be used on players as well
 *
 * @author Berke Müftüoğlu
 * @version 2020.11.22
 */
public enum Size
{
    SMALL("small"), BIG("big");
    
    // The size string.
    private String sizeString;
    
    /**
     * Initialise with the corresponding size string.
     * @param sizeString The size string.
     */
    Size(String sizeString)
    {
        this.sizeString = sizeString;
    }
    
    /**
     * @return The size word as a string.
     */
    public String toString()
    {
        return sizeString;
    }
}
