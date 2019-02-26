package src.Spells;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import Resources.Source;
import Resources.Classes.Subclass;
import Resources.Area;
import Resources.Classes;
import src.Spells.Spell.School;

public class SpellParser {
	public List<Spell> Parse() {
		Scanner scan;
		List<Spell> spells = new ArrayList<Spell>();
		try {
			scan = new Scanner(new File("Resources/SpellList"));
			scan.useDelimiter("<|>|\n|\t| |,");
			while(scan.hasNext()) {
				if(scan.hasNext("spell")) {
					//Setting fields to store spell values.
					String name = "";
					int level = 0;
					School school = null;
					
					boolean[] components = new boolean[3];
					boolean gpcost = false;
					String materials = "";
					String casttime = "";
					boolean ritual = false;
					String duration = "";
					boolean conc = false;
					Area area = null;
					int range = 0;
					int[] dimensions = null;

					String effect = "";
					List<Classes> classes = new ArrayList<Classes>();
					List<Subclass> archetypes = new ArrayList<Subclass>();
					List<Source> sources = new ArrayList<Source>();
					
					
					
					//Parse the spell.									//TODO - Label for parsing start
					while(!scan.hasNext("/spell")) {
						if(scan.hasNext("name")) {
							scan.next();
							while(!scan.hasNext("/name")) {
								if(name.length()>0) {name+=" ";}
								name+=scan.next();
							}
							scan.next();
						}
						if(scan.hasNext("level")) {
							scan.next();
							level = scan.nextInt();
							scan.next();
						}
						if(scan.hasNext("school")) {
							scan.next();
							for(School s: School.values()) {
								if(scan.hasNext(s.toString())) {school = s;}
							}
							scan.next();
							scan.next();
						}
						
						if(scan.hasNext("components")) {
							scan.next();
							while(!scan.hasNextBoolean()){scan.next();}//Skip indent
							components[0] = scan.nextBoolean();//Scan the components
							components[1] = scan.nextBoolean();
							components[2] = scan.nextBoolean();
							if(components[2]){//Get details if there's a material component
								while(!scan.hasNext("gold")){scan.next();}//Skip indent
								scan.next();
								gpcost = scan.nextBoolean();
								while(!scan.hasNext("material")){scan.next();}//Skip indent
								scan.next();
								Pattern oldDelimiter = scan.delimiter();
								scan.useDelimiter(">|<");
								materials = scan.next();//Read the materials
								scan.useDelimiter(oldDelimiter);
							}
							while(!scan.hasNext("/components")){scan.next();}//Skip garbage
							scan.next();
						}
						
						if(scan.hasNext("casttime")) {
							scan.next();
							ritual = scan.nextBoolean();//Read the ritual tag
							Pattern oldDelimiter = scan.delimiter();
							scan.useDelimiter(">|<");
							casttime = scan.next().substring(1);//Read the cast time skipping the comma
							scan.useDelimiter(oldDelimiter);
							scan.next();
						}
						if(scan.hasNext("duration")) {
							scan.next();
							conc = scan.nextBoolean();//Read the concentration tag
							Pattern oldDelimiter = scan.delimiter();
							scan.useDelimiter(">|<");
							duration = scan.next().substring(1);//Read the duration skipping the comma
							scan.useDelimiter(oldDelimiter);
							scan.next();
						}
						
						if(scan.hasNext("area")) {
							scan.next();
							boolean areaFound = false;
							for(Area a: Area.values()){//Read the shape of the area.
								if(!areaFound && scan.hasNext(a.toString())){
									areaFound = true;
									area = a;
									scan.next();
								}
							}
							if(area != Area.SELF){
								range = scan.nextInt();//Range should be first if not area of self.
								switch (area) {
								case CONE://Areas with 1 needed length
								case SPHERE:
								case CUBE:
									dimensions = new int[1];
									dimensions[0] = scan.nextInt();
									break;
								case LINE://Areas with 2 needed lengths
								case CYLINDER:
									dimensions = new int[2];
									dimensions[0] = scan.nextInt();
									dimensions[1] = scan.nextInt();
									break;
								default://Self or Single, shouldn't need a length
									break;
								}
							}
							scan.next();
						}
						
						if(scan.hasNext("effect")) {
							scan.next();
							Pattern oldDelimiter = scan.delimiter();
							scan.useDelimiter(">|<");
							effect = scan.next();
							scan.useDelimiter(oldDelimiter);
							scan.next();
						}
						
						if(scan.hasNext("classes")) {
							scan.next();
							while(!scan.hasNext("/classes")) {
								for(Classes c: Classes.values()) {
									if(scan.hasNext(c.toString())) {
										classes.add(c);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("subclasses")) {
							scan.next();
							while(!scan.hasNext("/subclasses")) {
								for(Classes c: Classes.values()) {
									for(Subclass s: c.getSubclasses(c)) {
										if(scan.hasNext(s.toString())) {
											archetypes.add(s);
											scan.next();
										}
									}
								}
							}
							scan.next();
						}
						
						if(scan.hasNext("source")) {
							scan.next();
							while(!scan.hasNext("/source")) {
								for(Source s:Source.values()) {
									if(scan.hasNext(s.name())) {
										sources.add(s);
										scan.next();
									}
								}
							}
							scan.next();
						}
						else {/*System.out.print(*/scan.next()/*)*/;}
					}
					scan.next();
					//Spell fully parsed here
					
					
					
					
					
					Spell current = new Spell() {						//TODO - label for spell creation start.
						String name="";
						int level=0;
						School school = null;
						boolean[] components = null;
						boolean gpCost = false;
						String materials = "";
						String castTime = null;
						boolean isRitual = false;
						String duration = null;
						boolean isConc = false;
						Area area = null;
						int range = 0;
						int[] dimensions = null;
						String effect = null;
						List<Classes> classes = null;
						List<Subclass> archetypes = null;
						List<Source> sources = null;
						
						@Override public void constructor(String name, int level, School school,
								boolean[] components, boolean gold, String materials,
								String time, boolean ritual, String duration, boolean concentration,
								Area area, int range, int[] dimensions,
								String effect, List<Classes> classes, List<Subclass> archetypes,
								List<Source> sources) {
							this.name = name;
							this.level = level;
							this.school = school;
							this.components = components;
							this.gpCost = gold;
							this.materials = materials;
							this.castTime = time;
							this.isRitual = ritual;
							this.duration = duration;
							this.isConc = concentration;
							this.area = area;
							this.range = range;
							this.dimensions = dimensions;
							this.effect = effect;
							this.classes = classes;
							this.archetypes = archetypes;
							this.sources = sources;
						}

						@Override public String getName() {return name;}
						@Override public int getLevel() {return level;}
						@Override public School getSchool() {return school;}

						@Override public boolean[] getComponents(){return components;}
						@Override public boolean gpCost(){return gpCost;}
						@Override public String materials(){return materials;}

						@Override public String castTime(){return castTime;}
						@Override public boolean isRitual(){return isRitual;}
						@Override public String duration(){return duration;}
						@Override public boolean isConcentration() {return isConc;}

						@Override public Area getArea(){return area;}
						@Override public int getRange(){return range;}
						@Override public int[] getDimensions(){return dimensions;}
						
						@Override public String getEffect(){return effect;}
						
						@Override public List<Classes> getClasses() {return classes;}
						@Override public boolean fromClass(Classes curClass) {return classes.contains(curClass);}
						@Override public boolean fromArchetype(Subclass curClass) {return archetypes.contains(curClass);}
						
						@Override public boolean fromSource(Source source) {return sources.contains(source);}
						
						@Override public String toString() {
							String builtString = name+"\n";
							//Nicely formatted level and school
							if(getLevel()==0){builtString += getSchool().toNiceString()+" cantrip\n";}
							else{
								builtString += getLevel();
								switch (getLevel()){
								case 1: builtString+="st";break;
								case 2: builtString+="nd";break;
								case 3:	builtString+="rd";break;
								default:builtString+="th";break;
								}
								builtString += " level "+getSchool().toNiceString()+"\n";
							}
							
							//Components
							if(components[0]){
								builtString+="V";
								if(components[1]||components[2]){builtString+=", ";}
							}if(components[1]){
								builtString+="S";
								if(components[2]){builtString+=", ";}
							}if(components[2]){
								builtString+="M ("+materials+")";
							}
							
							//Casting time
							builtString += "\nCasting time: "+castTime;
							if(isRitual){builtString += " (R)";}
							//Duration
							builtString += "\nDuration: "+duration;
							if(isConc){builtString += " (Concentration)";}
							
							builtString += "\n"+effect;//List spell body
							
							builtString += "\nClasses: ";
							int i=0;
							for(Classes c:Classes.values()){
								if(fromClass(c)){
									if(i>0){builtString+=", ";}
									builtString+=c.toNiceString();
									i++;
								}
							}
							for(Classes c:Classes.values()){//Reiterate for subclasses to come after full classes
								for(Subclass s:c.getSubclasses(c)){
									if(fromArchetype(s)){
										builtString+=", "+s.toNiceString();
									}
								}
							}
							
							builtString += "\nSource: ";
							i=0;
							for(Source s:Source.values()){
								if(fromSource(s)){
									if(i>0){builtString+=", ";}
									builtString+=s.toNiceString();
									i++;
								}
							}
							return builtString;
						}
					};
					current.constructor(name, level, school,
							components, gpcost, materials,
							casttime,ritual,duration,conc,
							area,range,dimensions,
							effect,
							classes,archetypes,
							sources);
					spells.add(current);
				}
				else {/*System.out.println(*/scan.next()/*)*/;}//Skip to next spell
			}//where parsing ends
			
			
			
			
			scan.close();
			System.out.println("Spell parsing complete");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return spells;
	}
}
