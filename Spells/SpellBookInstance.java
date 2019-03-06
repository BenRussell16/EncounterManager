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
	//TODO - Daily uses
	
	public SpellBookInstance(Stage stage, List<Spell> spells) {
		this.stage = stage;
		this.spells = spells;
		loadBook();
	}
	
	public List<Spell> getKnown(){return knownspells;}
	public int getSlots(int level){return numSlots.get(level);}
	public List<Spell> getPrepped(int level){return preparedSpells.get(level);}
	
	public void loadBook(){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("Resources/Spellbooks/"));
		File file = fileChooser.showOpenDialog(stage);
		
		if(file!=null){
			Scanner scan;
			System.out.println("Loading spellbook for "+file.getName());
			try {
				scan = new Scanner(file);
				scan.useDelimiter("<|>|\n|\t| |,");
				while(scan.hasNext()) {
					if(scan.hasNext("spellbook")) {
						//Setting fields to store spell values.
						knownspells = new ArrayList<Spell>();
						numSlots = new HashMap<Integer,Integer>();
							for(int i=1; i<=9; i++){numSlots.put(i, 0);}
						preparedSpells = new HashMap<Integer,List<Spell>>();
							for(int i=0; i<=9; i++){preparedSpells.put(i, new ArrayList<Spell>());};
						
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
							}else{scan.next();}
						}
					}
					scan.next();
				}//where parsing ends
				scan.close();
				System.out.println("Spellbook loading complete");
			} catch (FileNotFoundException e) {
				System.out.println("Spellbook loading failed");
				e.printStackTrace();
			}
		}else{System.out.println("No file selected");}
	}
}
