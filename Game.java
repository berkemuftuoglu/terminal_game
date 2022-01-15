import java.util.*;
/**
 *  This class is the main class of the "Game of Rings" application. 
 *  "Game of Rings" is a very simple, text based adventure game.  Users 
 *  are after a great mission of saving the world!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @author  Berke Müftüoğlu
 * @version 2020.11.22
 */
public class Game 
{
    private Parser parser;
    private Room endRoom, escapeRoom, transportRoom; //these are just room fields to accses throughout the class
    private Stack<Room> previousRooms; //holds the data for previous rooms
    private HashSet<Item> items; //holds the items in the game
    private HashSet<Player> players; //holds the players in the game
    private HashSet<Room> rooms;//holds the rooms in the game
    private Player currentPlayer, antagonist, masterPlayer; //these are just player fields to accses throughout the class
    private Room currentRoom;
    private Item winner; //item that is required to win
    private boolean win; //wining condition
    private Random random; //a random object
    private Scanner scan; //used for sub commands
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        
        //initilazing the currentRoom here and currentPlayer here because they won't be changed thourgouht the code.
        Type dwarf = Type.DWARF;
        Player bilbo = new Player("Baggings", 7, dwarf, currentRoom ,null);
        players.add(bilbo);
        currentPlayer = bilbo;
        currentRoom = currentPlayer.getLocation();
    }

    /**
     * Create all the rooms, players and items and links them together
     */
    private void createGame()
    {
        
        Room chamber, bigCave, stuckRoom, hills, mountDoom, dolGuldur, outside;
        
        // create the rooms
        outside = new Room("outside",  "You see the chamber of Gandalf.Maybe he is there?",false);
        chamber = new Room("chamber", "This is where Gandlaf is",false);
        bigCave = new Room("big cave", "You are now in a huge,endless cave",true);
        stuckRoom = new Room("mysterious room", "There is nowhere to go!",false);
        hills = new Room("hills", "This where gollum lives",false);
        mountDoom = new Room("Mount Doom", "It spreads lava everywhere be quick get out of there!",false);
        dolGuldur = new Room("Dol Guldur", "This is the mountain where gandlaf lives",false);

        // initialise room exits
        outside.setExit("straight",chamber);
        stuckRoom.setExit("straight",chamber);

        chamber.setExit("right", bigCave);

        bigCave.setExit("straight", hills);
        bigCave.setExit("right", stuckRoom);

        hills.setExit("straight", dolGuldur);
        hills.setExit("left", mountDoom);

        mountDoom.setExit("straight", dolGuldur);


        rooms.add(outside);
        rooms.add(chamber);
        rooms.add(bigCave);
        rooms.add(stuckRoom);
        rooms.add(hills);
        rooms.add(mountDoom);
        rooms.add(dolGuldur);

        

        //create the players


        Player gandalf, gollum;
        
        Size small = Size.SMALL;
        
        Type dwarf = Type.DWARF;
        Type magician = Type.MAGICIAN;
        Type tgollum = Type.GOLLUM;
        
        
        gandalf =  new Player("Gandalf", 1000000, magician,chamber,null);
        gollum = new Player("Gollum",2,tgollum, hills , small);

        antagonist = gollum;
        masterPlayer = gandalf;

        players.add(gandalf);
        players.add(gollum);
        
        //create items

        Item gun, knife, torch, baton, ring;

        gun = new Item("gun",2,dwarf,bigCave,null);
        knife = new Item("knife",1,dwarf,bigCave,null);
        torch = new Item("torch",3,dwarf,chamber,null);
        baton = new Item("baton",3, magician ,chamber, gandalf);
        ring = new Item("precious", 1, tgollum, hills, gollum);

        winner = ring;

        items.add(gun);
        items.add(knife);
        items.add(torch);
        items.add(baton);
        items.add(ring);

        // start game outside
        
        
         
        currentRoom = outside;
        escapeRoom = chamber;
        endRoom = dolGuldur;
        transportRoom = stuckRoom;
       
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }

        //if master player dies you lose
        if(!masterPlayer.getAlive()){
            System.out.println("You have killed Gandalf! You lost");

        }
        
        //if the currentPlyaer is dead 
        if(!currentPlayer.getAlive()){
            System.out.println(currentPlayer.getName() + " is dead" +  "You lost");

        } 
        
        //if win condition is true than you win
        if(win){
            System.out.println("Congrats!! You have won. You have saved the world.");
        }

        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the Game of Rings");
        System.out.println("You are Mr. Bagings and your mission is to reclaim");
        System.out.println("the ring before it falls in the hands of bad people");
        System.out.println("Type 'help' if you need help.");
        System.out.println("Type 'info' to learn how to play the game.");
        System.out.println();
        System.out.println("Written by Berke Müftüoğlu");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
                
            case GET:
                getItem(command);
                break;
                
            case LOOK:
                look(command);
                break;
                
            case BACK:
                goBack();
                break;
                
            case GIVE:
                give(command);
                break;
            
            case USE:
                use(command);
                break;
                
            case DROP:
                drop(command);
                break;
        }
        
        if(doesPlayerHave(masterPlayer, winner)){
            win = true;
            wantToQuit = true;
        }

        if(!masterPlayer.getAlive()){
            wantToQuit = true;
        }
        
        if(!currentPlayer.getAlive()){
            wantToQuit = true;
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("Are you lost? Okay calm.");
        System.out.println();
        System.out.println("You may proceed with these commands:");
        parser.showCommands();
    }

    /**
     * Introduces the game to the user
     */
    private void printInfo(){
        System.out.println("So this game is based on the film/book series called Lord of the Rings");
        System.out.println("When you type help you will see the comments (you cannot use kill)");
        System.out.println("Use comment word first and then the action next");
        System.out.println("enjoy!");
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

            
        //if it is null say there is no door else execute next room
        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRooms.push(currentRoom);
            currentPlayer.changeLocation(nextRoom);
        }

        //when the player arrives at transportRoom it get teleoprted to a random room
        transportRoomMethod();
        
        if(!(previousRooms.empty()) && previousRooms.peek() == escapeRoom){
            System.out.println();
            System.out.println("There was a secret door that just shut. You are trapped!");
            System.out.println("Only was is to follow the path that leads you");
            System.out.println();
            masterPlayer.changeLocation(endRoom);
        }
        
        changeRoom();
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }

    //the commands that are added by Berke Müftüoğlu
    /**
     * Get the total weight of the items that a player carries.
     * @param the player that is desired
     * @return the total weight
     */
    private int getTotalWeight(Player player){
        int result = 0;

        for(Item item : items){
            if(item.getpLocation() == player){
                result = result + item.getWeight();
            }
        }

        return result;
    }

    /**
     * The method for current play to get an item
     * @param a command
     */
    private void getItem(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("What does he get?");
            return;
        }

        Item result = null;
        String itemname = command.getSecondWord();

        result = findItem(itemname);

        if(result == null){
            System.out.println("That is not in this room. Try looking into the room");
        }
        
        if(!isItem(result.getName())){
            System.out.println("This is not an item");
        }
        
        if(!(result.getrLocation() == currentPlayer.getLocation())){
            System.out.println("This item is not even in this room");
        }
        else if(!(result.getpLocation() == null)){

            if(result.getpLocation() == currentPlayer){
                System.out.println("You already have this item.");
            }

            System.out.println("This item belongs to someone else. You cannot get it.");
        }
        else if(!currentPlayer.canPickUp(result)){
            System.out.println("Sorry but " + currentPlayer.getName() + " can't pick it up because of it status");

        } else if(!currentPlayer.canCarry(getTotalWeight(currentPlayer))){
            System.out.println("Sorry but " + currentPlayer.getName() + "can't pick it up because it is too heavy");

        } else {
            System.out.println("You got it!");
            result.updatepLocation(currentPlayer);
            result.updaterLocation(currentPlayer.getLocation());
        }

    } 
    /**
     * The method for using an item for the currentPlayer
     * @param command
     */
    private void use(Command command){

        if(!command.hasSecondWord()) {
            System.out.println("Use what?");
            return;
        }

        System.out.println("");

        String itemname = command.getSecondWord();

        if(itemname.equals("gun")){

            if(!doesPlayerHave(currentPlayer,findItem(itemname))){
                System.out.println("Sorry this player doesn't have this item");
                return;
            }

            System.out.println("Who do you want to kill?");
            System.out.println();
            System.out.println("you may kill:");
            printPlayersInRoom(currentRoom);
            System.out.println();
            System.out.print("> ");

            String answer = scan.nextLine();
            killgun(answer);

        } else if(itemname.equals("knife")) {

            if(!doesPlayerHave(currentPlayer,findItem(itemname))){
                System.out.println("Sorry this player doesn't have this item");
                return;
            }

            System.out.println("Who do you want to kill?");
            System.out.println();
            System.out.println("you may kill:");
            printPlayersInRoom(currentRoom);
            System.out.println();
            System.out.print("> ");

            String answer = scan.nextLine();
            killknife(answer);

        } else if(itemname.equals("torch"))  {

            if(!doesPlayerHave(currentPlayer,findItem(itemname))){
                System.out.println("Sorry this player doesn't have this item");
                return;
            }

            System.out.println("This lights up the room. Do you wish to continue?");
            System.out.println("Please type yes or no to continue");
            System.out.print(">");
            String answer = scan.nextLine();

            if(answer.equals("yes")){
                if(currentRoom.isDark()){
                    System.out.println("You made the right choice. Now you can see");
                    currentRoom.changeDark();
                }
                else {
                    System.out.println("This place is not dark! Why did you waste it");
                }

            }
        }
    }
    
    /**
     * The method for using a gun
     * @param the target
     */
    private void killgun(String input){
        
        //finds the target that neeed to be killed
        Player target = findPlayer(input);
        
        if(target == null){
            System.out.println("That player doesn't exsist");
            return;
        }

        if(!target.getAlive()) {
            System.out.println("That player is already dead!"); 
            return;
        }

        if(currentPlayer.getLocation() == target.getLocation()){
            target.dead();
            dropAllTimes(target);
            
            //if the antongonist is dead print out a special sentance
            killAntagonist(target);
        }

    }

    /**
     * The method for using a knife. Knife is not accurate
     * @param the target
     */
    private void killknife(String input){
        Player target = findPlayer(input);
        if(target == null) {
            System.out.println("That player doesn't exsist"); 
            return;
        }
        
        if(!target.getAlive()) {
            System.out.println("That player is already dead!"); 
            return;
        }
        
         //knife is pretty accurate chane of hitting is only 1/10
        if(currentPlayer.getLocation() == target.getLocation()){
            if(target.size().toString().equals("small")){
                
                //hitchance creates a random number between 0 and 9. If the number is between 0-8, it misses; if it is 9, it registers. 
                int hitchance = random.nextInt(10);
                switch(hitchance){
                    case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7: case 8:

                    System.out.println("He is too small! You have missed");
                    System.out.println(antagonist.getName() + " is getting away stop before he escapes");
                    return;

                    case 9:

                    target.dead();
                    dropAllTimes(target);
                }   
            }
            
            killAntagonist(target);
        }

    }

    /**
     * Looks into a room and prints the items
     * @param command
     */
    private void look(Command command){
        String roomword = command.getSecondWord(); 
        
        if(command.hasThirdWord()) {
            roomword +=(" " + command.getThirdWord()); 
        }
        
        if(!isRoom(roomword)){
            System.out.println("That room doesn't exsist");
            return;
        }
       
        if(!command.hasSecondWord()) {
            System.out.println("Which room?");
            return;
        }
        

        if(!roomword.equals(currentRoom.getName())){
            System.out.println("Your cannot look into that room. You are not there.");
            return;
        }


        if(currentRoom.isDark()){
            System.out.println("This room is dark. You cannot see maybe you need a torch?");
            return;
        }

        System.out.println("This room has these items:");
        System.out.println();

        for (Item item : items){
            if(item.getrLocation() == currentRoom){
                System.out.println(item.getName() + " which weighs " + item.getWeight() +" krons");
            }

        }
        System.out.println();
        System.out.println("You can carry " + (currentPlayer.getMaxWeight()-getTotalWeight(currentPlayer) + " more krons of items"));
        System.out.println();
        printPlayersInRoom(currentRoom);
    }

    /**
     * The method which enables player to drop items
     * @param command
     */
    private void drop(Command command){
        String second = command.getSecondWord();
        String third = command.getThirdWord();

        if(isPlayer(second) && isItem(third)){

            if(doesPlayerHave(findPlayer(second),findItem(third))){
                
                //if the player has the item , change the item room location to the location of the player 
                //and change the player location to null
                findItem(third).updaterLocation(findPlayer(second).getLocation());
                findItem(third).updatepLocation(null);
                System.out.println(findItem(third).getName() + " is dropped to " + findPlayer(second).getLocation().getName());

            } else {

                System.out.println(findPlayer(second).getName() + " doesn't have that item");

            }

        }else if(isItem(second)){
            
            // if the user types drop (itemname) then it understood as the currentPlayer shıuld be doing it
            if(doesPlayerHave(currentPlayer,findItem(second))){
                
                //if currentPlayer has that item change the room location to the location of the currentPlayer
                //and make the room location null
                findItem(second).updaterLocation(currentRoom);
                findItem(second).updatepLocation(null);
                System.out.println(findItem(second).getName() + " is dropped to " + currentRoom.getName());

            } else{

                System.out.println(currentPlayer.getName() + " doesn't have that item");

            }
        }

        
    }

    /**
     * Three word method which enables players to give other players items 
     * @param command
     */
    private void give(Command command){
        
        String second = command.getSecondWord();
        String third = command.getThirdWord();

        Player reciever = findPlayer(second);
        Item object = findItem(third);

        if(!sameppRoom(currentPlayer, reciever)){
            System.out.println(currentPlayer.getName() + " is not even in the same room as " + reciever.getName()+ "!"); 
        }

        if(!doesPlayerHave(currentPlayer,object)){
            System.out.println(currentPlayer.getName() + "doesn' have the " + object.getName()); 
        }

        if(!reciever.canPickUp(object)){
            System.out.println(reciever.getName()+ " can't pick up the object because of his status"); 
        }

        if(!reciever.canCarry(object.getWeight())){
            System.out.println(reciever.getName()+ " can't pick up the object because it is too heavy"); 
        }

        if(doesPlayerHave(currentPlayer,object) && reciever.canPickUp(object) && reciever.canCarry(object.getWeight()) && sameppRoom(currentPlayer, reciever)){
            
            //Change the player location of the item to reciever 
            //and room location to the room location of the reciever
            object.updatepLocation(reciever);
            object.updaterLocation(reciever.getLocation());
            System.out.println(currentPlayer.getName() + " has given " + reciever.getName()+ " the " + object.getName() );
        }

    }

    /**
     * Go back to the previous room if you can't prints an error messege
     * @param command
     */
    private void goBack(){
        if(!(previousRooms.empty())){

            if(previousRooms.peek() == escapeRoom){
                
                //when the previous room is the field escapeRoom you may not go back according to the story.
                System.out.println("Sorry you cannot go back from this room");
                return;
            }

            if(isRoom(previousRooms.peek().getName())){
                //The way stacks work is perfect for storing previous rooms. 
                //Since it is LIFO, when a previous rooms is retrieved it
                currentPlayer.changeLocation(previousRooms.pop());
                transportRoomMethod();
                changeRoom();
    
            } 

        } else{
            System.out.println("Sorry you cannot go back");
        }

    }

    
    
    
    //Supplementary methods used in command methods
    
    /**
     * Checks wheter some string is a item or not
     * @param name of the item
     * @return true or false
     */
    private boolean isItem(String input){

        for(Item item : items){
            if(item.getName().equals(input)){
                return true;
            }
        }

        return false;

    }

    /**
     * Finds the item in the game
     * @param name of the item
     * @return item
     */
    private Item findItem(String input){

        for(Item item : items){
            if(item.getName().equals(input)){
                return item;
            }
        }

        return null;
    }

    /**
     * Checks wheter some string is a room or not
     * @param name of the room
     * @return true or false
     */
    private boolean isRoom(String input){

        for(Room room : rooms){
            if(room.getName().equals(input)){
                return true;
            }
        }

        return false;
    }

    /**
     * Finds the room in the game
     * @param name of the room
     * @return room
     */
    private Room findRoom(String input){

        for(Room room : rooms){
            if(room.getName().equals(input)){
                return room;
            }
        }

        return null;
    }

    /**
     * Checks wheter some string is a player or not
     * @param name of the player
     * @return true or false
     */
    private boolean isPlayer(String input){

        for(Player player : players){
            if(player.getName().equals(input)){
                return true;
            }
        }

        return false;

    }

    /**
     * Finds the player in the game
     * @param name of the player
     * @return player
     */
    private Player findPlayer(String input){

        for(Player player : players){
            if(player.getName().equals(input)){
                return player;
            }
        }

        return null;
    }

    /**
     * Checks wheter a player has a item
     * @param the player and the item
     * @return true or false
     */
    private boolean doesPlayerHave(Player player, Item item){

        if(item.getpLocation() == player){
            return true;
        }
        return false;
    }

    /**
     * Checks wheter two people are in the same room
     * @param the two players
     * @return true or false
     */
    private boolean sameppRoom(Player player1,Player player2){
        if(player1.getLocation() == player2.getLocation()){
            return true;
        }
        
        return false; 
    }

    /**
     * Print all players in the room
     * @param input room
     */ 
    private void printPlayersInRoom(Room room){
        for(Player player : players){

            if(player.getLocation() == room){

                if(!player.getAlive()){
                    continue;
                }

                if(player == currentPlayer){
                    continue;
                }

                System.out.println(player.getName());
            }

                
        }
    }

    /**
     * Drops all of the items of a player to his current
     * Usually used when a player is dead
     * @param input player
     */ 
    private void dropAllTimes(Player player){
        for(Item item : items){
            if(item.getpLocation() == player){
                item.updatepLocation(null);
                item.updaterLocation(player.getLocation());
            }
        }
    }
    
        
    /**
     * Invoked when or if the antagonist is killed, prevents code duplication
     */
    private void killAntagonist(Player target)
    {
        if(target == antagonist && !(target.getAlive())){
                System.out.println("Great you have killed the " + antagonist.getName());
                System.out.println("Now you must give the" + winner.getName() + " to " + masterPlayer.getName());
                return;

            } else if (target == antagonist && target.getAlive()){
                System.out.println(antagonist.getName() + " is still alive! Don't let him get away");
            }
    }
    
        
    /** 
     * Updates the location of the currentRoom
     */
    private void changeRoom(){
        //currentPlayer.changeLocation(currentRoom);
        System.out.println(currentRoom.getLongDescription());
        System.out.println();
        System.out.println("This room has these characters:");
        printPlayersInRoom(currentRoom);
    
    }
    
    /**
     * Invoked when the player eaaches the transport room
     */
    private void transportRoomMethod(){
        if(currentRoom == transportRoom){
            /*
             * The algorithm behind this is that I generate a random number from0 to the size of  rooms list.
             * Lets say it generated the number number x| 0  ≤ x < rooms.size()
             * the for loop starts itering thorugh the rooms where i initliazed varibale i to keep track of the index.
             * When i (the index) is equal to num (the randomly generated index) we store that room and name to be used for teleporting
             */
            String name = null;
            Room newroom = null;
            
            int num = random.nextInt(rooms.size());
            int i = 0;
            
            for(Room room : rooms){
                if(i == num){
                    newroom = room;
                    name = room.getName();
                }
                i++;
            }
            System.out.println("You have been teleported to a random room!");
            System.out.println();
            
            previousRooms.push(currentRoom);
            currentPlayer.changeLocation(newroom);
        }
    
    }
}
