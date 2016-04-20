import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Game {
	// Get all the instance variables.
	private Player myPlayer;
	HashMap<String, Room> roomList = new HashMap<String, Room>();
	String userInput = null;
	boolean riddleSolved = false;
	boolean candleIn55 = false;

	public Game() {
		myPlayer = new Player();
		createGame(); // Makes the Rooms with neighbours, locks, and items. Also
						// makes the player.
		System.out.println("Game Start!");
		System.out.println("Need help? type 'help command'");
		run(); // Plays the game
	}

	private void run() {
		int count=0;
		int maxTries = 3;
		while(true){
		try {
			userInput = userInput();

			while (!userInput.equalsIgnoreCase("quit")) {// As long as the
															// command isn’t to
															// quit:
				handleCommand(userInput);// get the next input line and handle
											// it. (With handleCommand.)
				userInput = userInput();
			}
			quitGame();

		} catch (Exception e) {
			// Bug report
			System.out.println("Error caught: " + e+". You have "+(maxTries-count)+" tries remaining.");
			System.out.println("When typing a command, will crash if its solely the command.");
			if (++count == maxTries) throw e;
		}
		}
	}

	private void quitGame() {
		// TODO Auto-generated method stub
		System.out.println("Game over!");
	}

	private void endGame() {
		for (int i = 5; i > 0; i--) {
			System.out.println(i);
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Boom!");
		System.out.println("You beat the game! use quit to end the game. <N.A> use restart to play again.");
	}

	private String userInput() {
		System.out.println("Insert input");
		Scanner input = new Scanner(System.in);
		String userInput = input.nextLine();
		return userInput;
	}

	/**
	 * @param userInput
	 *            (This is the entire input string from the user.)
	 *
	 *            (Tell others to) Perform the task which belongs to the given
	 *            command.
	 */
	private void handleCommand(String userInput) {
		String command;
		String extra;
		if (userInput.contains(" ")) {
			String[] input = userInput.split(" ", 2);
			command = input[0].toLowerCase();
			extra = input[1].toLowerCase();
			System.out.println("Command: " + command + ", extra: " + extra);
		} else
			throw new IllegalArgumentException("The input " + userInput + " generates an error by itself");
		switch (command) {
		case "go":// Moves to an unlocked room
			// Then go to extra
			if (checkRoomTravel(extra)) {
				travelTo(extra);
			}
			break;
		case "examine": // Can examine backpack, items, rooms. Examine room ->
						// examines playerRoom
			// Give info on extra
			if (extra.equals("room")) {// if the extra command is room
				handleExamineCommand(myPlayer.getPlayerRoom());

			} else if (extra.equals("backpack") && myPlayer.getBackpack().size()>0) {
				handleExamineBackpack();

			} else if (itemInBackpack(extra)) {
				handleExamineCommand(itemNameToItem(extra));
			}
			else{
				System.out.println("Item not available.");
			}
			break;
		case "use":// Can use items from playerRoom and backpack
			// Use the item extra
			// Only if item is in backpack or room
			if (myPlayer.getBackpack().contains(itemNameToItem(extra))
					|| myPlayer.getPlayerRoom().roomItemList.contains(itemNameToItem(extra))) {
				handleUseCommand(extra);
			}
			break;
		case "get":// Can pickup items in the players location(room)
			handleGetCommand(extra);
			break;
		case "drop":// Lets you drop an item; doesn't have much functionality in
					// game.
			System.out.println("Dropping item");
			handleDropCommand(extra);
			break;
		case "teleport": // teleport Cellar (for example)
			if((itemInBackpack("teleporter") || itemInRoom("teleporter")) && extra.equalsIgnoreCase("random")){
				System.out.println("Teleporting to random room.");
				teleportTo(randomRoom());
			}
			else if (itemInBackpack("teleporter") || itemInRoom("teleporter")) {
				teleportTo(roomNameToRoom(extra));
			} else
				System.out.println("You can't teleport without a device.");
			break;
		case "help":
			System.out.println(extra + "(type without the plus):");
			System.out.println(" go + <direction(north, east, south, west)>");
			System.out.println(" examine + room, backpack, or any available item");
			System.out.println(" use + available item");
			System.out.println(" get + available item");
			System.out.println(" drop + available item");
			System.out.println("Do not use a command without extra info, always 2 words.");
			break;

		default: // Anything else is a typo.
			System.out.println("Commands (type without the plus):");
			System.out.println(" go + <direction(north, east, south, west)>");
			System.out.println(" examine + room, backpack, or any available item");
			System.out.println(" use + available item");
			System.out.println(" get + available item");
			System.out.println(" drop + available item");
			System.out.println("Do not use a command without extra info, always 2 words.");
			break;
		}
		// Check if the command is to travel between rooms. If so, handle
		// the room travelling using the method: checkRoomTravel
		// This one is explained later.
		// If there isn’t any room travel, then check all other command
		// command options. (Oh, look: this might be a great place for
		// a switch over the command string.)
		// Depending on the command, you might also need the extra information.
		// e.g. “use stick”, has “use” as command and “stick” as extra
		// information.
		// To make things easy, we created private methods to handle the
		// commands.
		// They are presented below.
	}

	private Item itemNameToItem(String itemName) {// Parameter is an item name,
													// returns the actual item
		// TODO Auto-generated method stub
		Item gameItem = null;
		for (int i = 0; i < myPlayer.getBackpack().size(); i++) {
			if (itemInBackpack(itemName)) {// Checks if the ItemName corresponds
											// to an item in the backpack.
				if (itemName.equalsIgnoreCase(myPlayer.getBackpack().get(i).getItemName())) {// IF
																								// it
																								// does,
																								// get
																								// the
																								// correct
																								// item
					gameItem = myPlayer.getBackpack().get(i);
					return gameItem;
				}
			}
		}
		if (gameItem == null) {
			for (int i = 0; i < myPlayer.getPlayerRoom().roomItemList.size(); i++) {
				if (itemInRoom(itemName)) {// Checks if the ItemName corresponds
					if (itemName.equalsIgnoreCase(myPlayer.getPlayerRoom().roomItemList.get(i).getItemName())) {// IF
																												// it
																												// does,
																												// get
																												// the
																												// correct
																												// item
						gameItem = myPlayer.getPlayerRoom().roomItemList.get(i); // to
																					// an
																					// item
																					// in
																					// the
																					// room.
					}
				}
			}
		}
		return gameItem;
	}

	private boolean itemInBackpack(String itemName) {
		// TODO Auto-generated method stub
		boolean hasItem = false;
		for (int i = 0; i < myPlayer.getBackpack().size(); i++) {
			if (itemName.equals(myPlayer.getBackpack().get(i).getItemName())) {// Checks
																				// if
																				// the
																				// ItemName
																				// corresponds
																				// to
																				// an
																				// item
																				// in
																				// the
																				// backpack.
				hasItem = true;
				return hasItem;// If the item has a match it returns true and
								// stops looking further.
			}
		}
		// System.out.println("Item not in backpack, checking room");
		return hasItem;
	}

	private boolean itemInRoom(String itemName) {
		boolean hasItem = false;
		for (int i = 0; i < myPlayer.getPlayerRoom().roomItemList.size(); i++) {
			if (itemName.equals(myPlayer.getPlayerRoom().roomItemList.get(i).getItemName())) {// Checks
																							// if
																							// the
				// ItemName corresponds
				// to an item in the backpack.
				hasItem = true;
				return hasItem;// If the item has a match it returns true and
								// stops looking further.
			}
		}
		System.out.println("Item not in backpack or in Room, or item Name misspelled");
		return hasItem;
	}

	private void handleUseCommand(String itemName) {
		String errorMessage = "There's a time and place for everything.";

		switch (itemName.toLowerCase()) { // lowerCase it just in case.
		case "the bomb":
			if (myPlayer.getPlayerLocation().equalsIgnoreCase("end room")) {// if
																		// correct
																		// room
				endGame();
			}
			break;
		case "key #1":
			// if in correct room
			// change corresponding lock to true
			if (myPlayer.getPlayerLocation().equalsIgnoreCase("starting room")
					&& myPlayer.getPlayerRoom().roomLocks.get("east") == false) {
				myPlayer.getPlayerRoom().roomLocks.put("east", true);
				System.out.println(itemNameToItem(itemName.toLowerCase()).getUsageText());
			} else {
				System.out.println(errorMessage);
			}
			break;
		case "key #2":
			if (myPlayer.getPlayerLocation().equalsIgnoreCase("starting room")
					&& myPlayer.getPlayerRoom().roomLocks.get("west") == false) {
				myPlayer.getPlayerRoom().roomLocks.put("west", true);
				System.out.println(itemNameToItem(itemName.toLowerCase()).getUsageText());
			} else {
				System.out.println(errorMessage);
			}
			break;
		case "flamethrower":
			if (myPlayer.getPlayerLocation().equalsIgnoreCase("room 32")
					&& myPlayer.getBackpack().contains(itemNameToItem("fuel"))
					&& myPlayer.getPlayerRoom().roomLocks.get("east") == false) {
				// IF im in room 32 and have fuel and the room is frozen shut
				myPlayer.getPlayerRoom().roomLocks.put("east", true);
				System.out.println(itemNameToItem(itemName.toLowerCase()).getUsageText());
			} else if (myPlayer.getPlayerLocation().equalsIgnoreCase("room 32")
					&& !myPlayer.getBackpack().contains(itemNameToItem("fuel"))
					&& myPlayer.getPlayerRoom().roomLocks.get("east") == false) {
				// If im in room 32 and dont have fuel but the door is frozen
				// shut
				System.out.println("Looks like the flamethrower needs fuel");
			} else {
				System.out.println(errorMessage);
			}
			break;
		case "fuel":
			System.out.println(itemNameToItem(itemName.toLowerCase()).getUsageText());
			break;
		case "eerie riddle":
			String answer = null;
			if (!riddleSolved) {// If i use riddle before its solved, i have to
								// type the answer.
				System.out.println("What is your answer?");
				answer = userInput();
				if (answer.equalsIgnoreCase("time") || answer.equalsIgnoreCase("skip")) {// If
																							// the
																							// answer
																							// is
																							// time
																							// or
																							// skip:
																							// riddleSolved
																							// =
																							// true.
					riddleSolved = true;
					roomList.get("supply room").roomLocks.put("south", true); // unlock
																				// T.Room
					System.out.println(itemNameToItem(itemName.toLowerCase()).getUsageText());
				} else {
					System.out.println(errorMessage); // after its solved
														// errorMessage.
				}
			}
			break;
		case "teleporter":
			String location;
			if (riddleSolved) {
				// show list of rooms
				for (Entry<String, Room> room : roomList.entrySet()) {
					String key = room.getKey();
					Room value = room.getValue();
					System.out.println("Room name: " + key);

				}
				System.out.println("Room random: teleport to a random room.");
				//location = userInput().toLowerCase();
				// Commented code here was meant for teleporter easter egg.
				// Teleporter does not work.
				// if (location.equalsIgnoreCase("room 404")){
				// System.out.println(roomList.get(location).getDescription());
				// }
				// if (roomList.containsKey(location)) {
				//	teleportTo(roomList.get(location).getName());
				//	System.out.println("Succesful teleport");
				//}
			}
			// if riddleSolved = true
			// Commence teleport
			break;
		case "end key":
			if (myPlayer.getPlayerLocation().equalsIgnoreCase("room 32")
					&& myPlayer.getPlayerRoom().roomLocks.get("north") == false) {
				myPlayer.getPlayerRoom().roomLocks.put("north", true);
				System.out.println(itemNameToItem(itemName.toLowerCase()).getUsageText());
			} else {
				System.out.println(errorMessage);
			}
			break;
		case "supply key":
			if (myPlayer.getPlayerLocation().equalsIgnoreCase("room 23")
					&& myPlayer.getPlayerRoom().roomLocks.get("west") == false) {
				myPlayer.getPlayerRoom().roomLocks.put("west", true);
				System.out.println(itemNameToItem(itemName.toLowerCase()).getUsageText());
			} else {
				System.out.println(errorMessage);
			}
			break;
		case "a candle":
			if (myPlayer.getPlayerLocation().equalsIgnoreCase("cellar") && candleIn55 == false) {
				candleIn55 = true;
				System.out.println(itemNameToItem(itemName.toLowerCase()).getUsageText());
			} else {
				System.out.println(errorMessage);
			}
			break;
		default:
			System.out.println("Item name may have been misspelled.");
			break;
		}
		// Check if the player has the item in his backpack.
		// If in the backpack: use it.
		// If not in the backpack: check if the item is in the room.
		// If the item is in the room: use it.
		// If no item with that name is present: tell the user he’s
		// trying to use something that isn’t there.
		// System.out.println("Handled use command");
	}

	private void handleExamineCommand(Item gameItem) {
		// Examine item get examineText
		// If the item is in the room or in the backpack then get examineText of
		// the item
		if (itemInBackpack(gameItem.getItemName()) || itemInRoom(gameItem.getItemName())) {
			System.out.println(gameItem.getItemName() + " has been examined.");
			System.out.println("" + gameItem.getExamineText());
		} else {
			System.out.println("Item cannot be examined, you may have misspelled the item.");
		}
	}

	public void handleExamineCommand(Room gameRoom) {
		// Show room name and description
		System.out.println("Room name: " + gameRoom.getName());
		System.out.println("" + gameRoom.getDescription());
		String message; // Passage to de 'direction'
		String lockMessage = " it has no lock";// it appears to be
												// locked/unlocked/lockless
		// Show all neighbourRooms, and if they have a lock.
		if (gameRoom.getName().equalsIgnoreCase("cellar") && candleIn55 == false) {
			System.out.println(gameRoom.getDescription());
		} else {
			for (Entry<String, Room> direction : gameRoom.neighbourRooms.entrySet()) {
				// Show all rooms
				message = "There's passage to the " + direction.getKey();
				if (myPlayer.getPlayerRoom().roomLocks.get(direction.getKey()) == null) {// if
																					// room
																					// doesnt
																					// have
																					// a
																					// lock.
					lockMessage = "it has no lock.";
				} else {
					if (!myPlayer.getPlayerRoom().roomLocks.get(direction.getKey())) {// If
																					// room
																					// is
																					// locked
						lockMessage = "it appears to be locked.";
						// Different lock types
					} else {// //otherwise room is unlocked
						lockMessage = "it appears to be unlocked";
					}
				}
				System.out.println(message + " " + lockMessage);
			}

			// Show all the items
			for (int i = 0; i < gameRoom.roomItemList.size(); i++) {
				System.out.println("Item #" + (1 + i) + ": " + gameRoom.roomItemList.get(i).getItemName());
			}

			if (gameRoom.roomItemList.size() == 0) {
				System.out.println("This room has no items.");
			}
		}
	}

	private void handleExamineBackpack() {
		// get a list with all items in back
		// if backpack has no items return message.
		if (myPlayer.getBackpack().isEmpty()) {
			System.out.println("You got no items hombre.");
		} else {
			System.out.println("I got these in my backpack:");
		}
		for (int i = 0; i < myPlayer.getBackpack().size(); i++) {
			System.out.println("Item #" + (i + 1) + ": " + myPlayer.getBackpack().get(i).getItemName()); // Item
																									// #1:
																									// Key
																									// #1
		}
	}

	private void handleDropCommand(String itemName) {
		Item dropItem = itemNameToItem(itemName);
		if (itemInBackpack(itemName)) {// If item is in the backpack
			myPlayer.getPlayerRoom().roomItemList.add(dropItem);// Add item to room
			myPlayer.getBackpack().remove(dropItem);// remove item from backpack
		} else {
			System.out.println(itemName + " is not a droppable item, or may have been misspelled.");
		}
	}

	private void handleGetCommand(String itemName) {
		Item getItem = itemNameToItem(itemName);
		if (itemInRoom(itemName)) {// If the item is in the room
			myPlayer.getBackpack().add(getItem);// Put item in backpack
			myPlayer.getPlayerRoom().roomItemList.remove(getItem);// Remove item from
																// room
			System.out.println(itemName + " has been added to backpack.");
		} else { // If item is not in the room
			System.out.println(itemName + " does not seem to be in this room.");
		}
	}

	/**
	 * @param command
	 *
	 *            Check if the command can take us to another room. If so: do
	 *            it! Let the caller know if we actually traveled.
	 */
	private boolean checkRoomTravel(String direction) {// check for locks, if
														// door is locked
														// message. If door is
														// unlocked travelTo.
		String lock = null;
		Boolean value = myPlayer.getPlayerRoom().roomLocks.get(direction);
		boolean travelPossible = false;

		System.out.println("Checking " + direction + " door.");
		if (value != null) { // If the direction has a lock
			if (!value) { // If the lock is locked
				lock = "locked";
				System.out.println("Looks like i'll need a key for this room.");
				travelPossible = false;
			} else {
				lock = "unlocked"; // Otherwise its unlocked
				travelPossible = true; // myPlayer.setPlayerRoom(myPlayer.playerRoom.neighbourRooms.get(direction));
			}
		} else if (value == null) { // if its null it means there is no lock or
									// no door
			// Check if there is a door
			if (myPlayer.getPlayerRoom().neighbourRooms.containsKey(direction)) {// there
																			// is
																			// a
																			// door
				lock = "open wide.";
				travelPossible = true;
			} else {// there is no door
				lock = " ... there is no door";
				travelPossible = false;
			}
		}
		System.out.println("The door is " + lock);
		return travelPossible;
		// Get the currentRoom from the player and check if this room
		// has an exit in the direction of command. (East, south, north, west.)
		// If there is an exit in that direction, ask the currentRoom to get
		// that
		// that room.
		// Tell the player to travel to the destination room.
		// If there is no exit in that direction, tell the player.
		// If travel was successful, return true. If not, return false.
	}

	private void travelTo(String roomName) {
		myPlayer.setPlayerRoom(myPlayer.getPlayerRoom().neighbourRooms.get(roomName));
	}
	
	private void teleportTo(Room room){
		System.out.println("Succesful teleport to "+room.getName()+"!");
		myPlayer.setPlayerRoom(room);
	}

	private void mapRoomName(String stringName, Room roomName) {
		roomList.put(stringName.toLowerCase(), roomName);
	}
	
	private Room roomNameToRoom(String roomString){
		Room goToRoom = roomList.get(roomString.toLowerCase());
		return goToRoom;
	}
	
	private Room randomRoom(){
		int randomIndex = new Random().nextInt(roomList.size());
		//Room randomRoom = 
		return null;
	}

	public void createGame() {
		// Randomize rooms?
		// The numbers for room objects represents where they are in a grid,
		// startingRoom is room44 -> 3rd column 3rd row; room54 5th column 4th
		// row
		// Create Rooms, Items in Rooms, Player in Starting Rooms, Close Locks.

		// Rooms:
		Room room13 = new Room("Supply Room",
				"This looks like the supply room. An eerie breeze comes from the south room.");
		Room room14 = new Room("Teleporter room", "This is the teleporter room. Teleport room name");
		Room room22 = new Room("Room 22", "Looks like another standard room.");
		Room room23 = new Room("Room 23", "Looks like another standard room.");
		Room room24 = new Room("Room 24", "Looks like another standard room.");
		Room room31 = new Room("End room", "Finally, the End room. Must plant the bomb here to finish mission.");
		Room room32 = new Room("Room 32", "Looks like another standard room.");
		Room room34 = new Room("Room 34", "Looks like another standard room.");
		Room room42 = new Room("Ice locker", "The floor is all wet from the molten ice.");
		Room room44 = new Room("Starting room", "Navigate the mansion, find the bomb, blow this place to hell!");
		Room room45 = new Room("Room 45",
				"Looks like another standard room. There's a desk, maybe I might find something usefull.");
		Room room54 = new Room("Room 54",
				"Looks like another standard room. There's a Safe, maybe I might find something usefull");
		Room room55 = new Room("Cellar", "The cellar is dark, I'll have to find a light source.");
		// Room room404 = new Room("Room 404", "Room 404 not found");//Meant to
		// be easter egg

		// Add rooms to hashmap of rooms (for teleporter mostly)
		mapRoomName(room13.getName(), room13);
		mapRoomName(room14.getName(), room14);
		mapRoomName(room22.getName(), room22);
		mapRoomName(room23.getName(), room23);
		mapRoomName(room24.getName(), room24);
		mapRoomName(room31.getName(), room31);
		mapRoomName(room32.getName(), room32);
		mapRoomName(room34.getName(), room34);
		mapRoomName(room42.getName(), room42);
		mapRoomName(room44.getName(), room44);
		mapRoomName(room45.getName(), room45);
		mapRoomName(room54.getName(), room54);
		mapRoomName(room55.getName(), room55);
//		mapRoomName()
		// mapRoomName(room404.getName(), room404); //Meant to be easter egg

		// Rooms & Items
		// Room 13 Neighbours & items
		room13.addNeighbour("east", room23);
		room13.addNeighbour("south", room14);// Requires note Use Note > "Time"
		room13.addItems(new Item("The bomb", "Mission accomplished, return to base!", "Beter not waste the bomb."));
		room13.addItems(new Item("A candle", "The candle lights up the room, you can now see what's in here!.",
				"Just a simple candle used to light up dark places."));
		room13.addLocks("south", false);
		// roomList.add(room13);

		// Room 14 Neighbour
		room14.addNeighbour("north", room13);
		room14.addItems(new Item("Teleporter", "ooh a teleporter. 'use teleporter, choose room from list'",
				"Commence teleport"));
				// roomList.add(room14);

		// Room 22 Neighbours
		room22.addNeighbour("east", room32);
		room22.addNeighbour("south", room23);
		room22.addItems(new Item("Fuel", "You need to use the flamethrower.", "Looks like it's for a flamethrower"));
		// roomList.add(room22);

		// Room 23 Neighbour
		room23.addNeighbour("north", room22);
		room23.addNeighbour("south", room24);
		room23.addNeighbour("west", room13);// Requires Supply key
		room23.addLocks("west", false);// direction indicates which room. Adds
										// false = closed, true = open.
		// roomList.add(room23);

		// Room 24 Neighbour
		room24.addNeighbour("north", room23);
		room24.addNeighbour("east", room34);
		// roomList.add(room24);

		// Room 31 Neighbour
		room31.addNeighbour("south", room32);
		// roomList.add(room31);

		// Room 32 Neighbours
		room32.addNeighbour("north", room31);// Requires End Key
		room32.addNeighbour("west", room22);
		room32.addNeighbour("east", room42);// Requires flamethrower
		room32.addLocks("east", false);
		room32.addLocks("north", false);
		// roomList.add(room32);

		// Room 34 Neighbours
		room34.addNeighbour("east", room44);
		room34.addNeighbour("west", room24);
		// roomList.add(room34);

		// Room 42 Neighbour
		room42.addNeighbour("west", room32);
		room42.addItems(
				new Item("Supply Key", "You have unlocked the Supply room", "Looks like a key to a Supply room."));
		room42.addItems(new Item("Eerie Riddle", "You hear something unlock to the far west.",
				"This thing all things devour. Beasts, trees, "
						+ "flowers. Gnaws on iron, bites steel. Grinds hard stones to meal. "
						+ "Slays king, ruins town. And beats high mountain down."));
						// roomList.add(room42);

		// Room 44 Neighbours
		room44.addNeighbour("west", room34);// Requires key 2
		room44.addNeighbour("east", room54);// Requires key 1
		room44.addNeighbour("south", room45);
		room44.addLocks("east", false);
		room44.addLocks("west", false);
		myPlayer.setStartingRoom(room44);

		// Room 45 Neighbour
		room45.addNeighbour("north", room44);
		room45.addItems(new Item("Key #1", "You have unlocked the room to the east.",
				"Looks like a simple key with the number 1 on it."));

		// Room 54 Neighbours
		room54.addNeighbour("south", room55);
		room54.addNeighbour("west", room44);
		room54.addItems(new Item("Flamethrower", "You have molten the frozen door, you can now enter.",
				"Oh baby this is hot."));
		room54.addItems(new Item("Key #2", "You have unlocked the room to the west.",
				"Lookd like a simple key with the number 2 on it."));
				// room54.addLocks("south", false); Room is dark and
				// unexaminable without candle, not locked.

		// Room 55 Neighbour
		room55.addNeighbour("north", room54);
		room55.addItems(new Item("End Key", "You have unlocked the End room. Finish your mission!",
				"This is definitely the fanciest key I have seen."));
	}

}