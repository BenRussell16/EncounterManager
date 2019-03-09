package Creatures;

public class LegendaryAction {
	private final String name;
	private final int cost;
	private final String description;

	public LegendaryAction(String name, int cost, String description) {
		this.name = name;
		this.cost = cost;
		this.description = description;
	}

	public String getName(){return name;}
	public int getCost(){return cost;}
	public String getDescription(){return description;}

	public String toString(){
		String builtString = name;
		if(cost>1){builtString += " (Costs "+cost+" Actions)";}
		builtString+=". "+description;
		return builtString;
	}
}
