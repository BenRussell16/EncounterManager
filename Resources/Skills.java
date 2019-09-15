package Resources;

public enum Skills{
	ACROBATICS("Acrobatics",Stats.DEX),ANIMALHANDLING("Animal Handling",Stats.WIS),ARCANA("Arcana",Stats.INT),ATHLETICS("Athletics",Stats.STR),
	DECEPTION("Deception",Stats.CHA),HISTORY("History",Stats.INT),INSIGHT("Insight",Stats.WIS),INTIMIDATION("Intimidation",Stats.CHA),
	INVESTIGATION("Investigation",Stats.INT),MEDICINE("Medicine",Stats.WIS),NATURE("Nature",Stats.INT),PERCEPTION("Perception",Stats.WIS),
	PERFORMANCE("Performance",Stats.CHA),PERSUASION("Persuasion",Stats.CHA),RELIGION("Religion",Stats.INT),SLIGHTOFHAND("Slight of Hand",Stats.DEX),
	STEALTH("Stealth",Stats.DEX),SURVIVAL("Survival",Stats.WIS);
	private String niceFormat;
	private Stats standardStat;
	private Skills(String niceFormat, Stats standardStat){
		this.niceFormat = niceFormat;
		this.standardStat = standardStat;
	}
	public Stats getStandardStat() {
		return standardStat;
	}
	public String toNiceString(){return niceFormat;}
}