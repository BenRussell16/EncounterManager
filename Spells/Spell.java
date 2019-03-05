package Spells;

import java.util.List;

import Resources.Area;
import Resources.Classes;
import Resources.Classes.Subclass;
import Resources.Source;

public interface Spell {

	public void constructor(String name, int level, School school,
			boolean[] components, boolean gold, String materials,
			String time, boolean ritual, String duration, boolean concentration,
			Area area, int range, int[] dimensions,
			String effect,
			List<Classes> classes, List<Subclass> archetypes,
			List<Source> sources);
	
	public String getName();
	public int getLevel();
	public School getSchool();
	
	public boolean[] getComponents();//return an array of if it has V,S,M components
	public boolean gpCost();
	public String materials();

	public String castTime();
	public boolean isRitual();
	public String duration();
	public boolean isConcentration();
	
	public Area getArea();
	public int getRange();
	public int[] getDimensions();
	
	public String getEffect();
	
	public List<Classes> getClasses();
	public boolean fromClass(Classes curClass);
	public boolean fromArchetype(Subclass curClass);
		
	public boolean fromSource(Source source);
	
	public String toString();	
	public default String toXML(){
		String builtString = "<spell>\n";
		//Basic info
		builtString += "\t<name>"+getName()+"</name>\n";
		builtString += "\t<level>"+getLevel()+"</level>\n";
		builtString += "\t<school>"+getSchool()+"</school>\n";
		
		//Components
		builtString += "\t<components>\n";
		builtString += "\t\t"+getComponents()[0]+","+getComponents()[1]+","+getComponents()[2]+"\n";
		if(getComponents()[2]){//Only bother storing if the material has a gold cost, and what it is if there is a material component.
			builtString += "\t\t<gold>"+gpCost()+"</gold>\n";
			builtString += "\t\t<material>"+materials()+"</material>\n";
		}
		builtString += "\t</components>\n";
		
		//Cast time
		builtString += "\t<casttime>"+isRitual()+","+castTime()+"</casttime>\n";
		//Duration
		builtString += "\t<duration>"+isConcentration()+","+duration()+"</duration>\n";

		//Area
		builtString += "\t<area>" + getArea();
		if(getArea() != Area.SELF && getArea() != Area.UNLIMITED){//Self targetting spells don't need a range
			builtString += ","+getRange();//If centered on self have 0;
			if(getArea() != Area.SINGLE){//Single target spells don't need an area
				for(int i=0; i<getDimensions().length; i++){
					builtString += ","+getDimensions()[i];
				}
			}
		}
		builtString += "</area>\n";

		//Effect
		builtString += "\t<effect>"+getEffect()+"</effect>\n";
			
		//Classes
		builtString += "\t<classes>";
		int i=0;
		for(Classes c:Classes.values()){
			if(fromClass(c)){
				if(i>0){builtString+=",";}
				builtString+=c.toString();
				i++;
			}
		}
		builtString += "</classes>\n";
		//Subclasses
		i=0;
		for(Classes c:Classes.values()){
			for(Subclass a: c.getSubclasses(c)){
				if(fromArchetype(a)){
					if(i>0){builtString+=",";}
					else{builtString += "\t<subclasses>";} // Only apply the surrounding tags if it is needed
					builtString+=a.toString();
					i++;
				}
			}
		}
		if(i>0){builtString += "</subclasses>\n";}
		
		//Sources
		builtString += "\t<source>";
		i=0;
		for(Source s:Source.values()){
			if(fromSource(s)){
				if(i>0){builtString+=",";}
				builtString+=s.toString();
				i++;
			}
		}
		builtString += "</source>\n";
		builtString += "</spell>\n";
		return builtString;
	}
	

	public enum School{
		ABJURATION,CONJURATION,DIVINATION,ENCHANTMENT,
		EVOCATION,ILLUSION,NECROMANCY,TRANSMUTATION;
		public String toNiceString(){return name().toUpperCase().substring(0, 1)
				+ name().toLowerCase().substring(1);}
	}
}
