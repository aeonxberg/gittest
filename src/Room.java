import java.util.ArrayList;
import java.util.HashMap;

public class Room {
	private String name;
	private String description;
	HashMap<String, Room> neighbourRooms = new HashMap<String, Room>();
	HashMap<String, Boolean> roomLocks = new HashMap<String, Boolean>();
	ArrayList<Item> roomItemList;
	
	public Room(String name, String description) {
		// TODO Auto-generated constructor stub
		//create the item here?
		//if(!description.equals(anObject))
		this.name = name;
		this.description = description;
		roomItemList = new ArrayList<Item>();
	}

	public void addNeighbour(String direction, Room thisRoom){
		neighbourRooms.put(direction, thisRoom);
	}
	
	public void addLocks(String direction, Boolean unlocked){ //The key unlocks (Sets unlocked to true)
		roomLocks.put(direction, unlocked);//True means doors are unlocked (Testing)
	}
	
	public void addItems(Item gameItem){
		roomItemList.add(gameItem);
		System.out.println();
	}
	
	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}
}
