package Resources;

public enum Area{
	SELF,SINGLE,SPHERE,CONE,LINE,CUBE,CYLINDER;
	public String toNiceString(){return name().toUpperCase().substring(0, 1)
			+ name().toLowerCase().substring(1);}
}