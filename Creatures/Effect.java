package Creatures;

public class Effect {
	private final String name;
	private final String limit;
	private final String description;
	
	public Effect(String name, String limit, String description) {
		this.name = name;
		this.limit = limit;
		this.description = description;
	}

	public String getName(){return name;}
	public String getLimit(){return limit;}
	public String getDescription(){return description;}

	public String toString(){
		String builtString = name;
		if(limit!=null && !limit.equals("null")){builtString += " ("+limit+")";}
		builtString+=". "+description;
		return builtString;
	}
}
