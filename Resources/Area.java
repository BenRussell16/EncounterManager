package Resources;

public enum Area{
	SELF,SINGLE,CONE,LINE,
	CYLINDER,SPHERE,CUBE;
	public String toNiceString(){return name().toUpperCase().substring(0, 1)
			+ name().toLowerCase().substring(1);}
}