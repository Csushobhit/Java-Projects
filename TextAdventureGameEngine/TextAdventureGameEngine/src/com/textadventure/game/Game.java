
package com.textadventure.game;

import com.textadventure.model.Item;
import com.textadventure.model.Player;
import com.textadventure.model.Room;
import com.textadventure.utils.SaveState;
import com.textadventure.engine.GameLoader;
import com.textadventure.engine.GameLoader.GameDataException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.textadventure.model.ExitData;
import com.textadventure.model.Conditions;
import com.textadventure.model.ConditionalDescription;
import com.textadventure.model.ExitModification;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Game {

    private Map<String, Room> rooms;
    private Player player;
    private final GameLoader gameLoader;
    private static final String SAVE_FILE_NAME = "savegame.json";
    private Map<String, Item> allGameItems;
    

    public Game() {
        this.gameLoader = new GameLoader();
        System.out.println("Game object created. GameLoader is ready.");
    }

    public void initialize(String dataFilePath) throws IOException, JsonSyntaxException, GameDataException, IllegalArgumentException {

        System.out.println("----------------------------------------");
        System.out.println("Initializing game from data file: " + dataFilePath + "...");
        System.out.println("----------------------------------------");

        gameLoader.loadGameData(dataFilePath);

        this.rooms = gameLoader.getLoadedRooms();
        this.allGameItems = gameLoader.getLoadedItems();
        if (this.allGameItems == null || this.allGameItems.isEmpty()) {
            throw new GameDataException("Initialization failed: No items were loaded.");
        }

        if (this.rooms == null || this.rooms.isEmpty()) {
            throw new GameDataException("Initialization failed: No rooms were loaded.");
        }

        String startRoomName = gameLoader.getPlayerStartRoomName();

        if (startRoomName == null || !this.rooms.containsKey(startRoomName)) {
            throw new GameDataException("Invalid starting room: " + startRoomName);
        }

        this.player = new Player(startRoomName);
        this.player.setCurrentRoomName(startRoomName);

        System.out.println("Player created. Starting in room: " + startRoomName);
        System.out.println("Game initialization complete.");
        System.out.println("Player is ready at location: " + this.player.getCurrentRoomName());
    }
    private boolean checkConditions(Conditions conditions, Player player)
    {
        if (conditions == null) return true;

        Object requiredItemsObj = conditions.getRequiresItem();

        if (requiredItemsObj == null) return true;

        if (requiredItemsObj instanceof String)
        {
            String requiredItem = (String) requiredItemsObj;
            return player.findItemInInventory(requiredItem).isPresent();
        }
        else if (requiredItemsObj instanceof List)
        {
            @SuppressWarnings("unchecked")
            List<String> requiredItems = (List<String>) requiredItemsObj;

            for (String item : requiredItems)
            {
                if (player.findItemInInventory(item).isEmpty())
                {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
    public Room getRoom(String roomName) {
        if (this.rooms == null) {
            return null;
        }
        return this.rooms.get(roomName);
    }

    public Room getCurrentRoom() {
        if (this.player == null) {
            return null;
        }
        String roomName = this.player.getCurrentRoomName();
        if (roomName == null) {
            return null;
        }
        return getRoom(roomName);
    }
    public void processCommand(String[] commandParts) {
        if(commandParts == null || commandParts.length == 0)
        {
        	System.out.println("Huh? Please enter a command.");
        	return;
        }
        
        String commandVerb = commandParts[0];
        System.out.println( "Processing verb: '" + commandVerb + "'");
        
        switch(commandVerb)
        {
        case "go":
            if (commandParts.length < 2)
            {
                System.out.println("Go where?");
                break;
            }

            String direction = String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length)).trim().toLowerCase();

            Room currentRoom = getCurrentRoom();
            if (currentRoom == null)
            {
                System.err.println("CRITICAL ERROR: Current room is null.");
                break;
            }

            Map<String, ExitData> exits = currentRoom.getExits();
            if (exits == null)
            {
                System.out.println("There are no exits from here.");
                break;
            }
            ExitData exitData = exits.get(direction);

            if (exitData == null)
            {
                System.out.println("You can't go that way.");
                break;
            }

            String targetRoomName = exitData.getTargetRoom();
            if (targetRoomName == null || targetRoomName.isBlank())
            {
                System.err.println("[ERROR] Missing targetRoom for exit " + direction);
                break;
            }

           
            Conditions conditions = exitData.getConditions();
            boolean conditionsMet = checkConditions(conditions, player);

            if (!conditionsMet)
            {
                String failMessage = (conditions != null) ? conditions.getFailMessage() : null;

                if (failMessage != null && !failMessage.isBlank())
                {
                    System.out.println(failMessage);
                }
                else
                {
                    System.out.println("Something prevents you from going " + direction + ".");
                }
                break;
            }

            // ✅ MOVE PLAYER
            if (!rooms.containsKey(targetRoomName))
            {
                System.err.println("[ERROR] Target room does not exist: " + targetRoomName);
                break;
            }

            player.setCurrentRoomName(targetRoomName);
            System.out.println("You move " + direction + ".");

            Room newRoom = getCurrentRoom();
            if (newRoom != null)
            {
            	System.out.println("\n" + newRoom.getName());

            	boolean shown = false;
            	List<ConditionalDescription> conditionals = newRoom.getConditionalDescriptions();

            	if (conditionals != null && !conditionals.isEmpty()) {
            	    for (ConditionalDescription cd : conditionals) {
            	        if (cd.getConditions() != null && cd.getDescription() != null) {
            	            if (checkConditions(cd.getConditions(), player)) {
            	                System.out.println(cd.getDescription());
            	                shown = true;
            	                break;
            	            }
            	        }
            	    }
            	}

            	if (!shown) {
            	    System.out.println(newRoom.getDescription());
            	}

                if (!newRoom.getItems().isEmpty())
                {
                    System.out.println("\nYou see:");
                    for (Item item : newRoom.getItems())
                    {
                        System.out.println("- " + item.getName());
                    }
                }

                if (!newRoom.getExits().isEmpty())
                {
                    System.out.println("\nExits: " + String.join(", ", newRoom.getExits().keySet()));
                }
            }

            break;
        	
        case "look":
            Room room = getCurrentRoom();
            if (room != null)
            {
            	System.out.println("\n" + room.getName());

            	boolean shown = false;
            	List<ConditionalDescription> conditionals = room.getConditionalDescriptions();

            	if (conditionals != null && !conditionals.isEmpty()) {
            	    for (ConditionalDescription cd : conditionals) {
            	        if (cd.getConditions() != null && cd.getDescription() != null) {
            	            if (checkConditions(cd.getConditions(), player)) {
            	                System.out.println(cd.getDescription());
            	                shown = true;
            	                break;
            	            }
            	        }
            	    }
            	}

            	if (!shown) {
            	    System.out.println(room.getDescription());
            	}
                if (!room.getItems().isEmpty())
                {
                    System.out.println("\nYou see:");
                    for (Item item : room.getItems())
                    {
                        System.out.println("- " + item.getName());
                    }
                }

                if (!room.getExits().isEmpty())
                {
                    System.out.println("\nExits: " + String.join(", ", room.getExits().keySet()));
                }
            }
            break;
        case "inventory":
        case "inv":
        	List<Item> inventory = player.getInventory();
        	if(inventory == null || inventory.isEmpty())
        	{
        		System.out.println("Your inventory is empty");
        	}
        	else {
        		System.out.println("You are carrying:");
                for (Item item : inventory) {
                    System.out.println("- " + item.getName());
                }
        	}
        	break;
        case "take":
        	if (commandParts.length < 2) {
                System.out.println("Take what? Please specify an item (e.g., 'take key').");
                break;
        	}
        	String targetItemName = String.join(" ", Arrays.copyOfRange(commandParts, 1, commandParts.length));
            System.out.println("Trying to take item: '" + targetItemName + "'");
        	
            Room currentRoom1 = getCurrentRoom();
            if (currentRoom1 == null) {
                System.err.println("[Game.processCommand] CRITICAL ERROR: Cannot determine current location to take item.");
                break;
            }
            
            List<Item> itemsInRoom = currentRoom1.getItems();
            if (itemsInRoom == null) {
                System.err.println("[Game.processCommand] ERROR: Room '" + currentRoom1.getName() + "' has a null items list!");
                System.out.println("There appears to be nothing to take here.");
                break; // Exit 'take' case
           }
            
            Item itemToTake = null;
            boolean itemFound = false;
            
            Iterator<Item> iterator = itemsInRoom.iterator();
            while (iterator.hasNext()) {
            	Item roomItem = iterator.next();
            	if(roomItem.getName().equalsIgnoreCase(targetItemName))
            	{
            		itemToTake = roomItem;
            		itemFound = true;
            		
            		 iterator.remove();
            		 System.out.println("[Debug] Removed '" + itemToTake.getName() + "' from room '" + currentRoom1.getName() + "'.");
            		 break;
            	}
            }
            
            if(itemFound && itemToTake != null)
            {
            	player.takeItem(itemToTake);
            	System.out.println("[Debug] Added '" + itemToTake.getName() + "' to player inventory.");

                
                System.out.println("You take the " + itemToTake.getName() + ".");

            } else {
         
                System.out.println("There is no '" + targetItemName + "' here to take.");
            }
            break;
        case "examine":
        case "x":
        	break;
        case "save":
        	System.out.println("Attempting to save game state...");
        	String playerLocationName = player.getCurrentRoomName();
        	if(playerLocationName == null)
        	{
        		System.err.println("[Game.processCommand] ERROR: Cannot save game. Player location is unknown.");
                break;
        	}
        	System.out.println("[Debug] Saving player location: " + playerLocationName);
        	
        	List<String> inventoryItemNames = new ArrayList<>();
        	List<Item> playerInventory = player.getInventory();
        	
        	if (playerInventory != null)
        	{
        		 inventoryItemNames = playerInventory.stream()
                         .map(Item::getName) // Method reference Item::getName is equivalent to item -> item.getName()
                         .collect(Collectors.toList());
        	}
        	System.out.println("[Debug] Saving player inventory: " + inventoryItemNames);
        	
        	Map<String, List<String>> currentRoomItems = new HashMap<>();
        	for(Map.Entry<String, Room> entry: rooms.entrySet())
        	{
        		String roomName = entry.getKey();
                Room room1 = entry.getValue();
                List<String> itemNamesInRoom = new ArrayList<>();
                if (room1 != null && room1.getItems() != null) { // Ensure room and its item list are valid
                    // Use stream to map each Item in the room to its name String
                    itemNamesInRoom = room1.getItems().stream()
                                          .map(Item::getName)
                                          .collect(Collectors.toList());
                } else if (room1 == null) {
                     System.err.println("[Game.processCommand] WARNING: Skipping null room object during save for key: " + roomName);
                     continue; // Skip this entry if the room object is somehow null
                }
                
                currentRoomItems.put(roomName, itemNamesInRoom);
        	}
        	SaveState saveState = new SaveState(playerLocationName, inventoryItemNames, currentRoomItems);
        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        	
        	try (FileWriter writer = new FileWriter(SAVE_FILE_NAME)) {
        		gson.toJson(saveState, writer);
        		 System.out.println("Game saved successfully to " + SAVE_FILE_NAME + ".");
        	} catch(IOException e)
        	{
        		System.err.println("ERROR: Could not save game state to file '" + SAVE_FILE_NAME + "'.");
                e.printStackTrace();
                System.out.println("Failed to save game. Please check permissions or disk space.");
        	}
        	break;
        case "use":
            if (commandParts.length < 4) {
                System.out.println("How do you want to use that? Try 'use <item> on <target>'.");
                break;
            }

            int onIndex = -1;
            for (int i = 1; i < commandParts.length; i++) {
                if (commandParts[i].equalsIgnoreCase("on")) {
                    onIndex = i;
                    break;
                }
            }

            if (onIndex == -1 || onIndex == 1 || onIndex == commandParts.length - 1) {
                System.out.println("How do you want to use that? Try 'use <item> on <target>'.");
                break;
            }

            String itemName = String.join(" ", Arrays.copyOfRange(commandParts, 1, onIndex));
            String targetName = String.join(" ", Arrays.copyOfRange(commandParts, onIndex + 1, commandParts.length));

            System.out.println("[Debug] Trying to use '" + itemName + "' on '" + targetName + "'");

            // 1. Check if player has item
            Item itemToUse = null;
            for (Item item : player.getInventory()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    itemToUse = item;
                    break;
                }
            }

            if (itemToUse == null) {
                System.out.println("You don't have a '" + itemName + "'.");
                break;
            }

            // 2. Check usability
            Item.Usability usability = itemToUse.getUsability();
            if (usability == null) {
                System.out.println("You can't use the " + itemToUse.getName() + " like that.");
                break;
            }

            // 3. Check target match
            String requiredTarget = usability.getTarget();
            if (usability.getTarget() == null || !requiredTarget.equalsIgnoreCase(targetName)) {
                System.out.println("You can't use the " + itemToUse.getName() + " on " + targetName + ".");
                break;
            }

            // 4. Check target presence
            boolean targetFound = false;

            String requiredTarget1 = usability.getTarget();

            if (requiredTarget1.equalsIgnoreCase("self")) {
                targetFound = true;
            } else {
                Room currentRoom11 = getCurrentRoom();
                if (currentRoom11 != null && currentRoom11.getItems() != null) {
                    for (Item item : currentRoom11.getItems()) {
                        if (item.getName().equalsIgnoreCase(requiredTarget1)) {
                            targetFound = true;
                            break;
                        }
                    }
                }
            }
            if (!targetFound) {
                System.out.println("You don't see a '" + targetName + "' here.");
                break;
            }

            // 5. Apply effect
            System.out.println(usability.getEffectDescription());
            
            Room currentRoom11 = getCurrentRoom();

         // REMOVE TARGET
         String removeTarget = usability.getRemovesTarget();
         if(removeTarget != null && currentRoom11 != null)
         {
             Iterator<Item> it = currentRoom11.getItems().iterator();
             while(it.hasNext())
             {
                 Item item = it.next();
                 if(item.getName().equalsIgnoreCase(removeTarget))
                 {
                     it.remove();
                     System.out.println("The " + item.getName() + " disappears.");
                     break;
                 }
             }
         }

         // ADD TARGET (new item in room)
         String addTarget = usability.getAddsTarget();
         if(addTarget != null && currentRoom11 != null)
         {
        	 Item newItem = allGameItems.get(addTarget.toLowerCase());
             if(newItem != null)
             {
                 currentRoom11.addItem(newItem);
                 System.out.println("A " + newItem.getName() + " appears.");
             }
         }

         // ADD ITEM TO INVENTORY
         String addToInventory = usability.getAddsItemToInventory();
         if(addToInventory != null)
         {
        	 Item invItem = allGameItems.get(addToInventory.toLowerCase());
             if(invItem != null)
             {
                 player.takeItem(invItem);
                 System.out.println("You received " + invItem.getName() + ".");
             }
         }

         // CHANGE ROOM DESCRIPTION
         String newDesc = usability.getChangesRoomDescriptionTo();
         if(newDesc != null && currentRoom11 != null)
         {
             currentRoom11.setDescription(newDesc);
         }
      // -------- MODIFY EXIT (STEP 15.4) --------
         ExitModification exitMod = usability.getModifiesExit();

         if (exitMod != null && currentRoom11 != null)
         {
             String directionToModify = exitMod.getDirection();

             if (directionToModify != null && !directionToModify.isBlank())
             {
                 ExitData exitToModify = currentRoom11.getExits().get(directionToModify.toLowerCase());

                 if (exitToModify != null)
                 {
                     Conditions exitConditions = exitToModify.getConditions();

                     if (exitConditions != null)
                     {
                         
                         if (exitMod.isClearRequiresItem())
                         {
                             if (exitConditions.getRequiresItem() != null)
                             {
                                 exitConditions.setRequiresItem(null);
                                 System.out.println("You hear something unlock towards " + directionToModify + ".");
                             }
                         }

                         
                         String newFail = exitMod.getSetFailMessage();
                         if (newFail != null)
                         {
                             exitConditions.setFailMessage(newFail);
                         }
                     }
                     else
                     {
                         System.out.println("[Debug] No conditions found to modify on exit.");
                     }
                 }
                 else
                 {
                     System.out.println("[Debug] No such exit: " + directionToModify);
                 }
             }
         }

         // CONSUME ITEM
         if(usability.isConsumesItem())
         {
             Iterator<Item> it = player.getInventory().iterator();
             while(it.hasNext())
             {
                 Item item = it.next();
                 if(item.getName().equalsIgnoreCase(itemToUse.getName()))
                 {
                     it.remove();
                     System.out.println("The " + item.getName() + " is used up.");
                     break;
                 }
             }
         }          

            break;
        case "load":
            System.out.println("Attempting to load game state from " + SAVE_FILE_NAME + "...");

            SaveState loadedState = null;
            Gson gson1 = new Gson();

            try (FileReader reader = new FileReader(SAVE_FILE_NAME)) {
                loadedState = gson1.fromJson(reader, SaveState.class);
                System.out.println("[Debug] Save file read and parsed successfully.");
            } catch (FileNotFoundException e) {
                System.out.println("No save file found ('" + SAVE_FILE_NAME + "').");
                break;
            } catch (IOException e) {
                System.err.println("ERROR: Could not read save file '" + SAVE_FILE_NAME + "'.");
                e.printStackTrace();
                System.out.println("Failed to load game due to a file reading error.");
                break;
            } catch (JsonSyntaxException e) {
                System.err.println("ERROR: Save file '" + SAVE_FILE_NAME + "' is corrupted or has invalid format.");
                e.printStackTrace();
                System.out.println("Failed to load game. The save file might be damaged.");
                break;
            } catch (Exception e) {
                System.err.println("ERROR: An unexpected error occurred while loading the game.");
                e.printStackTrace();
                System.out.println("Failed to load game.");
                break;
            }

            if (loadedState != null) {
                System.out.println("[Debug] Applying loaded game state...");

                if (player == null) {
                    player = new Player(loadedState.getPlayerLocation());
                    System.out.println("[Debug] Player object was null, created a new one for loading.");
                }

                if (rooms == null) {
                    System.err.println("ERROR: Cannot apply loaded state, game rooms are not initialized.");
                    System.out.println("Load failed.");
                    break;
                }

                String loadedLocation = loadedState.getPlayerLocation();
                if (loadedLocation != null && rooms.containsKey(loadedLocation)) {
                    player.setCurrentRoomName(loadedLocation);
                    System.out.println("[Debug] Player location updated to: " + loadedLocation);
                } else {
                    System.err.println("WARNING: Loaded location '" + loadedLocation + "' is invalid.");
                }

                List<String> loadedInvNames = loadedState.getPlayerInventory();
                player.getInventory().clear();
                System.out.println("[Debug] Player inventory cleared.");

                if (loadedInvNames != null) {
                    for (String itemName1 : loadedInvNames) {
                        Item item = allGameItems.get(itemName1);
                        if (item != null) {
                            player.takeItem(item);
                            System.out.println("[Debug] Added '" + itemName1 + "' to player inventory.");
                        } else {
                            System.err.println("WARNING: Unknown item '" + itemName1 + "' in inventory.");
                        }
                    }
                } else {
                    System.err.println("WARNING: Saved inventory missing.");
                }

                Map<String, List<String>> loadedRoomStates = loadedState.getRoomItemStates();

                if (loadedRoomStates != null) {
                    for (Map.Entry<String, Room> entry : rooms.entrySet()) {

                        String roomName = entry.getKey();
                        Room room1 = entry.getValue();

                        if (room1 == null) continue;

                        room1.getItems().clear();
                        System.out.println("[Debug] Cleared items for room: " + roomName);

                        List<String> itemNames = loadedRoomStates.get(roomName);

                        if (itemNames != null) {
                            for (String itemName1 : itemNames) {
                                Item item = allGameItems.get(itemName1);
                                if (item != null) {
                                    room1.addItem(item);
                                    System.out.println("[Debug] Added '" + itemName1 + "' back to room '" + roomName + "'.");
                                } else {
                                    System.err.println("WARNING: Unknown item '" + itemName1 + "' in room '" + roomName + "'.");
                                }
                            }
                        } else {
                            System.out.println("[Debug] No saved state for room '" + roomName + "'.");
                        }
                    }
                } else {
                    System.err.println("WARNING: Room state missing in save file.");
                }

                System.out.println("Game loaded successfully.");

            } else {
                System.out.println("Failed to load game state.");
            }

            break;
        case "quit":
        	System.out.println("Quitting game. Goodbye!");
        	break;
        default:
        	System.out.println("Sorry, I don't know how to '" + commandVerb + "'. Try 'go', 'look', 'take', 'inventory', or 'quit'.");
        	break;
        }
        System.out.println();
    }
}
