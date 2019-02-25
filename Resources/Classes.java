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
		BERSERKER,TOTEMWARRIOR,//PHB	TODO-totem warrior space
		BATTLERAGER,//SCAG
		ANCESTRALGUARDIAN,STORMHERALD,ZEALOT;//XGtE	TODO - AG,SH space
		public String toNiceString(){return "Path of the "+name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum bardSubclass implements Subclass{
		LORE,VALOR,//PHB
		GLAMOUR,SWORDS,WHISPERS;//XGtE
		public String toNiceString(){return "College of "+name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
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
		LAND,MOON,//PHB
		DREAMS,SHEPHERD,//XGtE	TODO - dreams no the
		SPORES;//GGtR	TODO - no the
		public String toNiceString(){return "Circle of the "+name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum fighterSubclass implements Subclass{
		CHAMPION,BATTLEMASTER,ELDRITCHKNIGHT,//PHB	TODO-battle master and eldritch knight space
		BANNERET,//SCAG Purple Dragon Knight
		ARCANEARCHER,CAVALIER,SAMURAI;//XGtE	TODO - AA space
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum monkSubclass implements Subclass{
		OPENHAND,SHADOW,FOURELEMENTS,//PHB	TODO-open hand and four elements space, shadow no the
		LONGDEATH,SUNSOUL,//SCAG	TODO-long death and sun soul space
		DRUNKENMASTER,KENSEI;//XGtE, also contains sun soul	TODO - DM space
		public String toNiceString(){return "Way of the "+name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum paladinSubclass implements Subclass{
		DEVOTION,ANCIENTS,VENGEANCE,//PHB	TODO-ancients the
		OATHBREAKER,//DMG	TODO - no prefix
		CROWN,//SCAG
		CONQUEST,REDEMPTION;//XGtE
		public String toNiceString(){return "Oath of "+name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum rangerSubclass implements Subclass{
		HUNTER,BEASTMASTER,//PHB	TODO - beast master space
		GLOOMSTALKER,HORIZONWALKER,MONSTERSLAYER;//XGtE	TODO - spaces
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum rogueSubclass implements Subclass{
		THIEF,ASSASSIN,ARCANETRICKSTER,//PHB	TODO-arcane trickster space
		MASTERMIND,SWASHBUCKLER,//SCAG
		INQUISITIVE,SCOUT;//XGtE, also contains mastermind and swashbuckler
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum sorcererSubclass implements Subclass{
		DRACONICBLOODLINE,WILDMAGIC,//PHB	TODO-spaces in both
		STRORMSORCERY,//SCAG	TODO-space
		DIVINESOUL,SHADOWMAGIC;//XGtE, also contains storm sorcery	TODO-spaces
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum warlockSubclass implements Subclass{
		ARCHFEY,FIEND,GREATOLDONE,//PHB	TODO-GOO spaces
		UNDYING,//SCAG
		CELESTIAL,HEXBLADE;//XGtE
		public String toNiceString(){return "The "+name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
	private enum wizardSubclass implements Subclass{
		ABJURATION,CONJURATION,DIVINATION,ENCHANTMENT,EVOCATION,ILLUSION,NECROMANCY,TRANSMUTATION,//PHB
		BLADESINGING,//SCAG	TODO - no prefix
		WARMAGIC;//XGtE	TODO - space and no prefix
		public String toNiceString(){return "School of "+name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
}