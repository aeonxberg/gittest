public class Item {
	String name;
	String usageText;
	String examineText;
	
	public Item(String name, String usageText, String examineText) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.usageText = usageText;
		this.examineText = examineText;
	}
	
	public String getItemName(){
		return name.toLowerCase();
	}
	
	public String getExamineText(){
		return examineText;
	}
	
	public String getUsageText(){
		return usageText;
	}

}
