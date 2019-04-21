package Spells;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class SpellBookInstance {
	private Stage stage;
	private List<Spell> spells;
	private List<Spell> knownspells;
	private Map<Integer, Integer> numSlots;
	private Map<Integer, List<Spell>> preparedSpells;
	private Map<Integer, List<Spell>> innateSpells;
	private Map<Integer, List<Boolean>> innateSelfOnly;
	private Map<Integer, List<Integer>> innateCastLevel;
	
	/**
	 * A constructor that includes file selection.
	 * @param stage	- A stage to spawn the file selection window from.
	 * @param spells
	 */
	public SpellBookInstance(Stage stage, List<Spell> spells) {
		this.stage = stage;
		this.spells = spells;
		loadBook();
	}
	/**
	 * A constructor for when the file is already known.
	 * @param book - The file of the selected spellbook.
	 * @param spells
	 */
	public SpellBookInstance(File book, List<Spell> spells){
		this.spells = spells;
		loadBook(book);
	}
	
	public List<Spell> getKnown(){return knownspells;}
	public int getSlots(int level){
		if(!numSlots.containsKey(level)){return 0;}
		return numSlots.get(level);
	}
	public List<Spell> getPrepped(int level){return preparedSpells.get(level);}
	public boolean hasPrepped(){
		for(int i=0; i<=9; i++){
			if(preparedSpells.containsKey(i) && !preparedSpells.get(i).isEmpty()){
				return true;
			}
		}
		return false;
	}
	
	public List<Spell> getDaily(int uses){return innateSpells.get(uses);}//0 uses is at will.
	public boolean hasDaily(){
		for(int i=0; i<=3; i++){
			if(innateSpells.containsKey(i) && !innateSpells.get(i).isEmpty()){
				return true;
			}
		}
		return false;
	}
	public boolean isSelfOnly(int uses, Spell spell){return innateSelfOnly.get(uses).get(innateSpells.get(uses).indexOf(spell));}
	public Integer levelCastAt(int uses, Spell spell){return innateCastLevel.get(uses).get(innateSpells.get(uses).indexOf(spell));}
	
	
	public String toString(){
		String builtString = "";
		if(!getPrepped(0).isEmpty()){//Add slot based spells
			builtString+="Cantrips (at will): ";
			boolean first = true;
			for(Spell s:getPrepped(0)){
				if(!first){builtString+=", ";}
				builtString+=s.getName();
				first = false;
			}
		}
		for(int i=1; i<=9; i++){
			if(!getPrepped(i).isEmpty() || getSlots(i)>0){
				if(builtString.length()!=0){builtString+="\n";}
				if(i==1){builtString+="1st";}
				else if(i==2){builtString+="2nd";}
				else if(i==3){builtString+="3rd";}
				else{builtString+=i+"th";}
				builtString+=" level ("+getSlots(i)+" slot";
				if(getSlots(i)>1){builtString+="s";}
				builtString+="): ";
				boolean first = true;
				for(Spell s:getPrepped(i)){
					if(!first){builtString+=", ";}
					builtString+=s.getName();
					first = false;
				}
			}
		}
		
		boolean hasDailies = false;
		for(int i=0; i<4; i++){hasDailies = hasDailies || !getDaily(i).isEmpty();}
		if(builtString.length()>0 && hasDailies){builtString+="\n\n";}//Add buffer if both slots and dailies
		
		if(!getDaily(0).isEmpty()){//Add daily based spells
			builtString+="At will: ";
			boolean first = true;
			for(Spell s:getDaily(0)){
				if(!first){builtString+=", ";}
				builtString+=s.getName();
				int index = innateSpells.get(0).indexOf(s);
				if(innateSelfOnly.get(0).get(index)||innateCastLevel.get(0).get(index)!=null){
					builtString+=" (";
					if(innateSelfOnly.get(0).get(index)){builtString+="self only";}
					if(innateSelfOnly.get(0).get(index)
							&&innateCastLevel.get(0).get(index)!=null){builtString+=", ";}
					if(innateCastLevel.get(0).get(index)!=null){
						if(s.getLevel()==0){
							builtString+="cast as a level "+innateCastLevel.get(0).get(index)+" caster";
						}else{
							int castLevel = innateCastLevel.get(0).get(index);
							builtString+="cast at "+castLevel;
							switch (castLevel) {
							case 1:
								builtString+="st";
								break;
							case 2:
								builtString+="nd";
								break;
							case 3:
								builtString+="rd";
								break;
							default:
								builtString+="th";
								break;
							}
							builtString+=" level";
						}
					}
					builtString+=")";
				}
				first = false;
			}
		}
		for(int i=3; i>=1; i--){
			if(!getDaily(i).isEmpty()){
				if(builtString.length()!=0){builtString+="\n";}
				builtString+=i+"/Day each: ";
				boolean first = true;
				for(Spell s:getDaily(i)){
					if(!first){builtString+=", ";}
					builtString+=s.getName();
					int index = innateSpells.get(i).indexOf(s);
					if(innateSelfOnly.get(i).get(index)||innateCastLevel.get(i).get(index)!=null){
						builtString+=" (";
						if(innateSelfOnly.get(i).get(index)){builtString+="self only";}
						if(innateSelfOnly.get(i).get(index)
								&&innateCastLevel.get(i).get(index)!=null){builtString+=", ";}
						if(innateCastLevel.get(i).get(index)!=null){
							builtString+="cast at "+innateCastLevel.get(i).get(index)+"level";}
						builtString+=")";
					}
					first = false;
				}
			}
		}
		return builtString;
	}
	
	
	
	public void loadBook(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("Resources/Spellbooks/"));
		File file = fileChooser.showOpenDialog(stage);
		loadBook(file);
	}
	
	public void loadBook(File book){
		if(book!=null){
			Scanner scan;
			System.out.println("Loading spellbook for "+book.getName());
			try {
				scan = new Scanner(book);
				scan.useDelimiter("<|>|\n|\t| |,");
				while(scan.hasNext()) {
					if(scan.hasNext("spellbook")) {
						//Setting fields to store spell values.
						knownspells = new ArrayList<Spell>();
						numSlots = new HashMap<Integer,Integer>();
							for(int i=1; i<=9; i++){numSlots.put(i, 0);}
						preparedSpells = new HashMap<Integer,List<Spell>>();
							for(int i=0; i<=9; i++){preparedSpells.put(i, new ArrayList<Spell>());}
						innateSpells = new HashMap<Integer,List<Spell>>();
						innateSelfOnly = new HashMap<Integer,List<Boolean>>();
						innateCastLevel = new HashMap<Integer,List<Integer>>();
							for(int i=0; i<=3; i++){
								innateSpells.put(i, new ArrayList<Spell>());
								innateSelfOnly.put(i, new ArrayList<Boolean>());
								innateCastLevel.put(i, new ArrayList<Integer>());}
						
						//Parse the spellbook.
						while(!scan.hasNext("/spellbook")) {
							if(scan.hasNext("name")){//Skip the name, it's there for human readers
								while(!scan.hasNext("/name")){scan.next();}
							}else if(scan.hasNext("slot")){
								scan.next();
								while(!scan.hasNext("/slot")){//Parse the slot based spells
									if(scan.hasNext("known")){
										scan.next();
										Pattern oldDelimiter = scan.delimiter();
										scan.useDelimiter(">|<|,");
										List<String> knowns = new ArrayList<String>();
										while(!scan.hasNext("/known")){//Read known spells
											knowns.add(scan.next());
										}
										scan.useDelimiter(oldDelimiter);
										for(Spell s:spells){//Translate names to spells
											if(knowns.contains(s.getName())){
												knownspells.add(s);
											}
										}
										
									}else if(scan.hasNextInt()){
										int level = scan.nextInt();//Read level, and if not cantrip read number of slots
										if(level!=0){numSlots.put(level, scan.nextInt());}
										Pattern oldDelimiter = scan.delimiter();
										scan.useDelimiter(">|<|,");
										List<String> prepped = new ArrayList<String>();
										while(!scan.hasNext("/"+level)){//Read prepared spells
											prepped.add(scan.next());
										}
										scan.useDelimiter(oldDelimiter);
										for(Spell s:spells){//Translate names to spells
											if(prepped.contains(s.getName())){
												preparedSpells.get(level).add(s);
											}
										}
										scan.next();
									}else {scan.next();}
								}
							}else if(scan.hasNext("daily")){
								scan.next();
								while(!scan.hasNext("/daily")){//Parse the daily based spells
									if(scan.hasNextInt()){
										int uses = scan.nextInt();
										while(!scan.hasNext("/"+uses)){
											if(scan.hasNext("spell")){
												scan.next();
												Pattern oldDelimiter = scan.delimiter();//Read a spell entry
												scan.useDelimiter(">|<|,");
												String name = scan.next();
												scan.useDelimiter(oldDelimiter);
												boolean selfOnly = scan.nextBoolean();
												Integer level = null;
												if(scan.hasNextInt()){level = scan.nextInt();}
												for(Spell s:spells){//Translate names to spells
													if(s.getName().equals(name)){
														innateSpells.get(uses).add(s);
													}
												}
												innateSelfOnly.get(uses).add(selfOnly);
												innateCastLevel.get(uses).add(level);
												scan.next();
											}else{scan.next();}
										}scan.next();
									}else {scan.next();}
								}
							}else{scan.next();}
						}
					}
					scan.next();
				}//where parsing ends
				scan.close();
				//System.out.println("Spellbook loading complete");
			} catch (FileNotFoundException e) {
				System.out.println("Spellbook loading failed");
				e.printStackTrace();
			}
		}else{System.out.println("No file selected");}
	}
}
