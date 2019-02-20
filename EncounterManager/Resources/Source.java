package EncounterManager.Resources;

public enum Source{
	PLAYERSHANDBOOK("Players Handbook"),
	MONSTERMANUAL("Monster Manual"),
	DUNGEONMASTERSGUIDE("Dungeon Masters Guide"),
	
	VOLOSGUIDETOMONSTERS("Volo's Guide to Monsters"),
	MORDENKAINENSTOMEOFFOES("Mordenkainen's Tome of Foes"),
	XANATHARSGUIDETOEVERYTHING("Xanathar's Guide to Everything"),
	SWORDCOASTADVENTURERSGUIDE("Sword Coast Adventurer's Guide"),
		
	CURSEOFSTRAHD("Curse of Strahd"),
	GUILDMASTERSGUIDETORAVNICA("Guildmasters' Guide to Ravnica"),
	HOARDOFTHEDRAGONQUEEN("Hoard of the Dragon Queen"),
	LOSTLABORATORYOFKWALISH("Lost Laboratory of Kwalish"),
	LOSTMINEOFPHANDELVER("Lost Mine of Phandelver"),
	OUTOFTHEABYSS("Out of the Abyss"),
	PRINCESOFTHEAPOCALYPSE("Princes of the Apocolypse"),
	STORMKINGSTHUNDER("Storm King's Thunder"),
	THERISEOFTIAMAT("The Rise of Tiamat"),
	TALESFROMTHEYAWNINGPORTAL("Tales From the Yawning Portal"),
	TOMBOFANNIHILATION("Tomb of Annihilation"),
	WATERDEEPDRAGONHEIST("Waterdeep: Dragon Heist"),
	WATERDEEPDUNGEONOFTHEMADMAGE("Waterdeep: Dungeon of the Mad Mage"),
	
	HUNTERSMARK("Hunter's Mark"),
	KARTHUNLANDSOFCONFLICT("Karthun: Lands of Conflict"),
	STRONGHOLDSANDFOLLOWERS("Strongholds and Followers"),
	TALDOREICAMPAIGNSETTING("Tal'Dorei Campaign Setting"),
	
	CUSTOM("Custom");
	
	private String niceFormat;
	private Source(String niceFormat){this.niceFormat = niceFormat;}
	public String toNiceString(){return niceFormat;}
}
