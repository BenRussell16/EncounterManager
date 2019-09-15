package Resources;

public enum Alignment{
	LG("Lawful Good"),NG("Neutral Good"),CG("Chaotic Good"),
	LN("Lawful Neutral"),TN("True Neutral"),CN("Chaotic Neutral"),
	LE("Lawful Evil"),NE("Neutral Evil"),CE("Chaotic Evil"),
	UNALIGNED("Unaligned");
	private String niceFormat;
	private Alignment(String niceFormat){this.niceFormat = niceFormat;}
	public String toNiceString(){return niceFormat;}
}