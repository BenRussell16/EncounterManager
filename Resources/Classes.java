package Resources;

public enum Classes{
	BARBARIAN,BARD,CLERIC,DRUID,FIGHTER,MONK,
	PALADIN,RANGER,ROGUE,SORCERER,WARLOCK,WIZARD;
	public String toNiceString(){return name().toUpperCase().substring(0, 1)
			+ name().toLowerCase().substring(1);}

	public Subclass[] getSubclasses(Classes superClass){
		if(superClass == Classes.BARBARIAN){return barbarianSubclass.values();}
		if(superClass == Classes.BARD){return bardSubclass.values();}
		if(superClass == Classes.CLERIC){return clericSubclass.values();}
		if(superClass == Classes.DRUID){return druidSubclass.values();}
		if(superClass == Classes.FIGHTER){return fighterSubclass.values();}
		if(superClass == Classes.MONK){return monkSubclass.values();}
		if(superClass == Classes.PALADIN){return paladinSubclass.values();}
		if(superClass == Classes.RANGER){return rangerSubclass.values();}
		if(superClass == Classes.ROGUE){return rogueSubclass.values();}
		if(superClass == Classes.SORCERER){return sorcererSubclass.values();}
		if(superClass == Classes.WARLOCK){return warlockSubclass.values();}
		if(superClass == Classes.WIZARD){return wizardSubclass.values();}
		return null;
	}		
	
	public interface Subclass{
		public String toNiceString();
	}
	private enum barbarianSubclass implements Subclass{
		BERSERKER("Path of the Berserker"),TOTEMWARRIOR("Path of the Totem Warrior"),//PHB
		BATTLERAGER("Path of the Battlerager"),//SCAG
		ANCESTRALGUARDIAN("Path of the Ancestral Guardian"),STORMHERALD("Path of the Storm Herald"),
			ZEALOT("Path of the Zealot");//XGtE
		private String niceFormat;
		private barbarianSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum bardSubclass implements Subclass{
		LORE,VALOR,//PHB
		GLAMOUR,SWORDS,WHISPERS;//XGtE
		public String toNiceString(){return "College of "+name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
//		LORE("College of Lore"),VALOR("College of Valor"),//PHB
//		GLAMOUR("College of Glamour"),SWORDS("College of Swords"),WHISPERS("College of Whispers");//XGtE
//		private String niceFormat;
//		private bardSubclass(String niceFormat){this.niceFormat = niceFormat;}
//		public String toNiceString(){return niceFormat;}
	}
	private enum clericSubclass implements Subclass{
		KNOWLEDGE,LIFE,LIGHT,NATURE,TEMPEST,TRICKERY,WAR,//PHB
		DEATH,//DMG
		ARCANA,//SCAG
		FORGE,GRAVE,//XGtE
		ORDER;//GGtR
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1)+" Domain";}
	}
	private enum druidSubclass implements Subclass{
		LANDARCTIC("Circle of the Land (Arctic)"),LANDCOAST("Circle of the Land (Coast)"),LANDDESERT("Circle of the Land (Desert)"),
		LANDFOREST("Circle of the Land (Forest)"),LANDGRASSLAND("Circle of the Land (Grassland)"),LANDMOUNTAIN("Circle of the Land (Mountain)"),
		LANDSWAMP("Circle of the Land (Swamp)"),LANDUNDERDARK("Circle of the Land (Underdark)"),
			//Land subsubclasses from PHB
		MOON("Circle of the Moon"),//PHB
		DREAMS("Circle of Dreams"),SHEPHERD("Circle of the Shepherd"),//XGtE
		SPORES("Circle of Spores");//GGtR
		private String niceFormat;
		private druidSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum fighterSubclass implements Subclass{
		CHAMPION("Champion"),BATTLEMASTER("Battle Master"),ELDRITCHKNIGHT("Eldritch Knight"),//PHB
		BANNERET("Bannaret"),//SCAG Purple Dragon Knight
		ARCANEARCHER("Arcane Archer"),CAVALIER("Cavalier"),SAMURAI("Samurai");//XGtE
		private String niceFormat;
		private fighterSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum monkSubclass implements Subclass{
		OPENHAND("Way of the Open Hand"),SHADOW("Way of Shadow"),FOURELEMENTS("Way of the Four Elements"),//PHB
		LONGDEATH("Way of the Long Death"),SUNSOUL("Way of the Sun Soul"),//SCAG
		DRUNKENMASTER("Way of the Drunken Master"),KENSEI("Way of the Kensei");//XGtE, also contains sun soul
		private String niceFormat;
		private monkSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum paladinSubclass implements Subclass{
		DEVOTION("Oath of Devotion"),ANCIENTS("Oath of the Ancients"),VENGEANCE("Oath of Vengeance"),//PHB
		OATHBREAKER("Oathbreaker"),//DMG
		CROWN("Oath of the Crown"),//SCAG
		CONQUEST("Oath of Conquest"),REDEMPTION("Oath of Redemption");//XGtE
		private String niceFormat;
		private paladinSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum rangerSubclass implements Subclass{
		HUNTER("Hunter"),BEASTMASTER("Beast Master"),//PHB
		GLOOMSTALKER("Gloom Stalker"),HORIZONWALKER("Horizon Walker"),MONSTERSLAYER("Monster Slayer");//XGtE
		private String niceFormat;
		private rangerSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum rogueSubclass implements Subclass{
		THIEF("Thief"),ASSASSIN("Assassin"),ARCANETRICKSTER("Arcane Trickster"),//PHB
		MASTERMIND("Mastermind"),SWASHBUCKLER("Swashbuckler"),//SCAG
		INQUISITIVE("Inquisitive"),SCOUT("Scout");//XGtE, also contains mastermind and swashbuckler
		private String niceFormat;
		private rogueSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum sorcererSubclass implements Subclass{
		DRACONICBLOODLINE("Draconic Bloodline"),WILDMAGIC("Wild Magic"),//PHB
		STORMSORCERY("Storm Sorcery"),//SCAG
		DIVINESOUL("Divine Soul"),SHADOWMAGIC("Shadow Magic");//XGtE, also contains storm sorcery
		private String niceFormat;
		private sorcererSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum warlockSubclass implements Subclass{
		ARCHFEY("The Archfey"),FIEND("The Fiend"),GREATOLDONE("The Great Old One"),//PHB
		UNDYING("The Undying"),//SCAG
		CELESTIAL("The Celestial"),HEXBLADE("The Hexblade");//XGtE
		private String niceFormat;
		private warlockSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
	private enum wizardSubclass implements Subclass{
		ABJURATION("School of Abjuration"),CONJURATION("School of Conjuration"),DIVINATION("School of Divination"),
		ENCHANTMENT("School of Enchantment"),EVOCATION("School of Evocation"),ILLUSION("School of Illusion"),
		NECROMANCY("School of Necromancy"),TRANSMUTATION("School of Transmutation"),//PHB
		BLADESINGING("Bladesinging"),//SCAG
		WARMAGIC("School of War Magic");//XGtE
		private String niceFormat;
		private wizardSubclass(String niceFormat){this.niceFormat = niceFormat;}
		public String toNiceString(){return niceFormat;}
	}
}