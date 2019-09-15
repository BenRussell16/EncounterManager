package Resources;

public enum Stats{
	STR("Strength"),DEX("Deterity"),CON("Constitution"),
	INT("Intellegence"),WIS("Wisdom"),CHA("Charisma");
	private String niceFormat;
	private Stats(String niceFormat){this.niceFormat = niceFormat;}
	public String toNiceString(){return niceFormat;}
}