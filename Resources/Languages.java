package Resources;

public enum Languages{//TODO complete
	COMMON,DRACONIC,DWARVISH,ELVISH,//Common languages
	GIANT,GOBLIN,ORCISH,UNDERCOMMON,//Uncommon languages
	CELESTIAL,DEEPSPEECH,DRUIDIC,SYLVAN,THIEVESCANT,PRIMORDIAL,//Rare languages
	ABYSSAL,INFERNAL,//Fiendish languages
	AURAN,AQUAN,IGNAN,TERRAN,//Elemental languages
	ALL,TELEPATHY,//Universal languages
	
	BULLYWUG,GITH,GNOLL,GNOMISH,GRELL,GRUNG,//Other languages
	HALFLING,HOOKHORROR,IXITXACHITL,KRAUL,MERFOLK,MODRON,OTYUGH,SAHUAGIN,
	SLAAD,SPHINX,THRIKREEN,TLINCALLI,TROGLODYTE,
	UMBERHULK,VEGEPYGMY,YIKARIA,YETI,
	BOTHII,NETHERESE,OLMAN,THAYAN,
	BLINKDOG,GIANTEAGLE,GIANTELK,KRUTHIK,WINTERWOLF,WORG,
	DEEPCROW,ICETOAD;
	public String toNiceString(){return name().toUpperCase().substring(0, 1)
			+ name().toLowerCase().substring(1);}
}