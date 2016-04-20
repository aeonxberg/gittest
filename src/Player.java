import java.util.ArrayList;

public class Player {
	private ArrayList<Item> backpack = new ArrayList<Item>();
	private String playerLocation;
	private Room playerRoom;
	
	public Player() {
		// TODO Auto-generated constructor stub
		System.out.println("Player has joined succesfully");
		//giveItems();
	}

	public void addToBackpack(Item gameItem) {
		// TODO Auto-generated method stub
		backpack.add(gameItem);
	}
	
	public Room getPlayerRoom(){
		return playerRoom;
	}
	
	public void setPlayerRoom(Room goTo){
		this.playerRoom = goTo;
		this.playerLocation = goTo.getName();
		System.out.println("Succesfully traveled to "+goTo.getName());
		System.out.println(goTo.getDescription());
	}
	
	public void teleportToRoom(Room roomName){
		this.playerRoom= roomName;
		this.playerLocation = roomName.getName();
	}
	
	public String getPlayerLocation(){
		return playerLocation;
	}
	
	public ArrayList<Item> getBackpack(){
		return backpack;
	}

	public void setStartingRoom(Room startingRoom) {
		// TODO Auto-generated method stub
		playerRoom=startingRoom;
		playerLocation = playerRoom.getName();
	}		
}
