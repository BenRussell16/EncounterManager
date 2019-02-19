package EncounterManager.src.Spells;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import EncounterManager.Resources.Source;
import EncounterManager.src.Creatures.Creature;
import EncounterManager.src.Spells.Spell.Classes;
import EncounterManager.src.Spells.Spell.School;

public class SpellParser {

	public SpellParser() {
		// TODO Auto-generated constructor stub
	}

	public List<Spell> Parse() {
		Scanner scan;
		List<Spell> spells = new ArrayList<Spell>();
		try {
			scan = new Scanner(new File("EncounterManager/Resources/SpellList"));
			scan.useDelimiter("<|>|\n|\t| |,");
			while(scan.hasNext()) {
				if(scan.hasNext("spell")) {
					//Setting fields to store spell values.
					String name = "";
					int level = 0;
					School school = null;
					List<Classes> classes = new ArrayList<Classes>();
					List<Source> sources = new ArrayList<Source>();
					//Parse the spell.
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
							for(Spell.School s:Spell.School.values()) {
								if(scan.hasNext(s.toString().toLowerCase())) {school = s;}
							}
							scan.next();
							scan.next();
						}
						if(scan.hasNext("classes")) {
							scan.next();
							while(!scan.hasNext("/classes")) {
								for(Spell.Classes c:Spell.Classes.values()) {
									if(scan.hasNext(c.toString().toLowerCase())) {
										classes.add(c);
										scan.next();
									}
								}
							}
							scan.next();
						}
						if(scan.hasNext("source")) {
							scan.next();
							while(!scan.hasNext("/source")) {
								for(Source s:Source.values()) {
									if(scan.hasNext(s.toString().toLowerCase())) {
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
					
					Spell current = new Spell() {
						String name="";
						int level=0;
						School school = null;
						List<Classes> classes = null;
						List<Source> sources = null;
						
						public void constructor(String name, int level, School school, List<Classes> classes, List<Source> source){
							this.name = name;
							this.level = level;
							this.school = school;
							this.classes = classes;
							this.sources = sources;
						}

						@Override public String getName() {return name;}
						@Override public int getLevel() {return level;}
						@Override public School getSchool() {return school;}
						@Override public List<Classes> getClasses() {return classes;}
						@Override public boolean fromClass(Classes curClass) {return classes.contains(curClass);}
						@Override public boolean fromSource(Source source) {return sources.contains(source);}
						
						@Override public String toString() {return name;}
					};
					current.constructor(name, level, school, classes, sources);
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
