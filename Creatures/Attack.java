package Creatures;

public class Attack {
	private final String name;
	private final int toHit;
	private final int shortRange;
	private final Integer longRange;
	private final String description;

	public Attack(String name, int toHit, int shortRange, Integer longRange, String description) {
		this.name = name;
		this.toHit = toHit;
		this.shortRange = shortRange;
		this.longRange = longRange;
		this.description = description;
	}

	public String getName(){return name;}
	public int getToHit(){return toHit;}
	public int getShortRange(){return shortRange;}
	public Integer getLongRange(){return longRange;}
	public String getDescription(){return description;}
	
	public String toString(){
		String builtString = name+". ";
		if(toHit>=0){builtString+="+";}
		builtString += toHit+" to hit, ";
		builtString += shortRange;
		if(longRange!=null){builtString += "/"+longRange;}
		builtString+=" ft. "+description;
		return builtString;
	}
}
