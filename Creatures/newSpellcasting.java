package Creatures;

import java.io.File;
import java.util.List;

import Creatures.Creature.Stats;
import Spells.Spell;
import Spells.SpellBookInstance;

public class newSpellcasting {
	
	private final Stats ability;
	private final Integer toHit;
	private final Integer DC;
	private final Integer level;
	private final File book;
	private final SpellBookInstance spells;

	public newSpellcasting(File book, Stats ability, Integer toHit, Integer DC, Integer level, List<Spell> spellList) {
		this.ability = ability;
		this.toHit = toHit;
		this.DC = DC;
		this.level = level;
		this.book = book;
		spells = new SpellBookInstance(book, spellList);
	}
	
	public Stats getAbility(){return ability;}
	public Integer getToHit(){return toHit;}
	public Integer getDC(){return DC;}
	public Integer getLevel(){return level;}
	public File getFile(){return book;}
	public SpellBookInstance getSpellList(){return spells;}
}
