package src.Spells;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Resources.Source;
import Resources.Classes.Subclass;
import Resources.Area;
import Resources.Classes;
import src.Spells.Spell.School;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

@SuppressWarnings("restriction")
public class SpellBook {
	private final List<Spell> spells;
	private Stage secondaryStage = null;
	
	public SpellBook(List<Spell> spells) {
		this.spells = spells;
	}
	
	private List<Spell> query;
	private GridPane spellList;
		private List<Label> names;
		private List<Label> levels;
		private List<Button> learn;

	private List<Spell> known;
	private List<Spell> knownQuery;
	private GridPane knownList;
		private int knownDepth = 1;
		private List<Label> knownNames;
		private List<Label> knownLevels;
		private List<Button> prepare;
		private List<Button> forget;
		
	private GridPane curSpell;
		private Map<Integer, List<Spell>> slots;
		private int[] numSlots = new int[9];
		private List<List<Spell>> dailys;
			private List<Integer> dailyUses;
	
	public Stage makeDisplay() {
		if(secondaryStage != null) {secondaryStage.close();}
		secondaryStage = new Stage();//make the window
		secondaryStage.setTitle("Spellbook");
		GridPane grid = new GridPane();
        grid.setHgap(10);
      	grid.setVgap(10);
      	
      		query = new ArrayList<Spell>();
      		query.addAll(spells);
      		known = new ArrayList<Spell>();
      		knownQuery = new ArrayList<Spell>();
			
			GridPane topBar = new GridPane();
			
				Button save = new Button("Save changes");		//TODO - Label for save button
				save.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						//TODO - save the spell
//						System.out.println("Saving spells");
//						//Perform a bubble sort to place new spells in the list.
//						Spell slot;
//						boolean sorted = false;
//						while(!sorted){
//							sorted = true;
//							for(int i=spells.size()-1; i>0; i--){
//								if(spells.get(i).getLevel()<spells.get(i-1).getLevel()
//									|| (spells.get(i).getLevel()==spells.get(i-1).getLevel()
//										&& spells.get(i).getName().compareToIgnoreCase(spells.get(i-1).getName())<0)){
//									sorted = false;
//									slot = spells.get(i);
//									spells.set(i, spells.get(i-1));
//									spells.set(i-1, slot);
//								}
//							}
//						}
//						//Build the new XML string
//						String xml = "";
//						for(int i=0; i<spells.size(); i++){xml+=spells.get(i).toXML();}
//						//Save the new spells.
//						try (PrintWriter out = new PrintWriter(new File("Resources/SpellList"))) {
//						    out.print(xml);
//						    System.out.println("Spell saving complete");
//						} catch (FileNotFoundException e) {
//							e.printStackTrace();
//						}
					}
				});
				topBar.add(save, 0, 0);
				
			
				Label label = new Label("\t\t");
				topBar.add(label, 1, 0);
				//Set up filter inputs							//TODO - Label for filter setup
				TextField nameFilter = new TextField();
				ChoiceBox<Integer> levelFilter = new ChoiceBox<Integer>(FXCollections.observableArrayList(null,0,1,2,3,4,5,6,7,8,9));
				levelFilter.setValue(null);
				
				ChoiceBox<School> schoolPicker = new ChoiceBox<School>(FXCollections.observableArrayList());
				schoolPicker.getItems().add(null);
				schoolPicker.getItems().addAll(School.values());
				schoolPicker.setValue(null);
				schoolPicker.setConverter(new StringConverter<School>(){
					@Override public School fromString(String arg0) {return null;}
					@Override public String toString(School school) {
						if(school != null){
							return school.toNiceString();}
						return "School";
					}});
	
				//Components
				ChoiceBox<Boolean> vPicker = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
				vPicker.getItems().addAll(null, false, true);
				vPicker.setValue(null);
				vPicker.setConverter(new StringConverter<Boolean>(){
					@Override public Boolean fromString(String arg0) {return null;}
					@Override public String toString(Boolean component) {
						if(component != null){
							if(component){
								return "Y";
							}else{return "N";}
						}
						return "V";
					}});
				ChoiceBox<Boolean> sPicker = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
				sPicker.getItems().addAll(null, false, true);
				sPicker.setValue(null);
				sPicker.setConverter(new StringConverter<Boolean>(){
					@Override public Boolean fromString(String arg0) {return null;}
					@Override public String toString(Boolean component) {
						if(component != null){
							if(component){
								return "Y";
							}else{return "N";}
						}
						return "S";
					}});
				ChoiceBox<Boolean> mPicker = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
				mPicker.getItems().addAll(null, false, true);
				mPicker.setValue(null);
				mPicker.setConverter(new StringConverter<Boolean>(){
					@Override public Boolean fromString(String arg0) {return null;}
					@Override public String toString(Boolean component) {
						if(component != null){
							if(component){
								return "Y";
							}else{return "N";}
						}
						return "M";
					}});
				//Gold cost filter
				ChoiceBox<Boolean> costPicker = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
				costPicker.getItems().addAll(null, false, true);
				costPicker.setValue(null);
				costPicker.setConverter(new StringConverter<Boolean>(){
					@Override public Boolean fromString(String arg0) {return null;}
					@Override public String toString(Boolean cost) {
						if(cost != null){
							if(cost){
								return "Yes";
							}else{return "No";}
						}
						return "GP cost";
					}});
				
				//Cast time filter
				ChoiceBox<String> timePicker = new ChoiceBox<String>(FXCollections.observableArrayList());
				timePicker.getItems().addAll(null,"Action","Bonus Action","Reaction",
		      			"1 Minute","10 Minutes","1 Hour","8 Hours","12 Hours","24 Hours");
				timePicker.setValue(null);
				timePicker.setConverter(new StringConverter<String>(){
					@Override public String fromString(String arg0) {return arg0;}
					@Override public String toString(String time) {
						if(time == null){return "Cast time";}
						return time;}});
				//Ritual filter
				ChoiceBox<Boolean> ritualPicker = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
				ritualPicker.getItems().addAll(null, false, true);
				ritualPicker.setValue(null);
				ritualPicker.setConverter(new StringConverter<Boolean>(){
					@Override public Boolean fromString(String arg0) {return null;}
					@Override public String toString(Boolean ritual) {
						if(ritual != null){
							if(ritual){
								return "Yes";
							}else{return "No";}
						}
						return "Ritual";
					}});
				//Concentration filter
				ChoiceBox<Boolean> concPicker = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
				concPicker.getItems().addAll(null, false, true);
				concPicker.setValue(null);
				concPicker.setConverter(new StringConverter<Boolean>(){
					@Override public Boolean fromString(String arg0) {return null;}
					@Override public String toString(Boolean conc) {
						if(conc != null){
							if(conc){
								return "Yes";
							}else{return "No";}
						}
						return "Concentration";
					}});
				
				//Area filter
				ChoiceBox<Area> areaPicker = new ChoiceBox<Area>(FXCollections.observableArrayList());
				areaPicker.getItems().add(null);
				areaPicker.getItems().addAll(Area.values());
				areaPicker.setValue(null);
				areaPicker.setConverter(new StringConverter<Area>(){
					@Override public Area fromString(String arg0) {return null;}
					@Override public String toString(Area area) {
						if(area != null){
							return area.toNiceString();}
						return "Area";
					}});
	
				//Class filters
				ChoiceBox<Classes> classPicker = new ChoiceBox<Classes>(FXCollections.observableArrayList());
				classPicker.getItems().add(null);
				classPicker.getItems().addAll(Classes.values());
				classPicker.setValue(null);
				classPicker.setConverter(new StringConverter<Classes>(){
					@Override public Classes fromString(String arg0) {return null;}
					@Override public String toString(Classes classes) {
						if(classes != null){
							return classes.toNiceString();}
						return "Class";
					}});
				ChoiceBox<Subclass> subclassPicker = new ChoiceBox<Subclass>(FXCollections.observableArrayList());
				subclassPicker.getItems().add(null);
				subclassPicker.setValue(null);
				subclassPicker.setConverter(new StringConverter<Subclass>(){
					@Override public Subclass fromString(String arg0) {return null;}
					@Override public String toString(Subclass classes) {
						if(classes != null){
							return classes.toNiceString();}
						return "Subclass";
					}});
				subclassPicker.setVisible(false);
	
				//Source filter
				ChoiceBox<Source> sourcePicker = new ChoiceBox<Source>(FXCollections.observableArrayList());
				sourcePicker.getItems().add(null);
				sourcePicker.getItems().addAll(Source.values());
				sourcePicker.setValue(null);
				sourcePicker.setConverter(new StringConverter<Source>(){
					@Override public Source fromString(String arg0) {return null;}
					@Override public String toString(Source source) {
						if(source != null){
							return source.toNiceString();}
						return "Source";
					}});
				
				//Define the filtering action					//TODO - Label for filter application
				EventHandler<ActionEvent> filterQuery = new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						query.removeAll(spells);
						knownQuery.removeAll(spells);
						for(Spell s:spells) {//text filter
							if(s.getName().toLowerCase().contains(nameFilter.getText().toLowerCase())) {
								query.add(s);
								if(known.contains(s)){knownQuery.add(s);}}}
						if(query.isEmpty()) {
							query.addAll(spells);
							knownQuery.addAll(known);
						}
	
						if(levelFilter.getValue()!=null) {//level filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getLevel()==levelFilter.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						if(schoolPicker.getValue()!=null) {//school filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getSchool()==schoolPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						
						if(vPicker.getValue()!=null) {//verbal component filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getComponents()[0]==vPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						if(sPicker.getValue()!=null) {//somatic component filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getComponents()[1]==sPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						if(mPicker.getValue()!=null) {//material component filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getComponents()[2]==mPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						if(costPicker.getValue()!=null) {//gp cost filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.gpCost()==costPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						
	
						if(timePicker.getValue()!=null) {//cast time filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.castTime().equals(timePicker.getValue()))) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						if(ritualPicker.getValue()!=null) {//ritual filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.isRitual()==ritualPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						if(concPicker.getValue()!=null) {//concentration filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.isConcentration()==concPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						if(areaPicker.getValue()!=null) {//area of effect filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getArea()==areaPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						
						if(classPicker.getValue()!=null) {//class filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!s.fromClass(classPicker.getValue())) {
									//Treats it as an or. If its not on that class list but is on the archetypes, it stays.
									if(subclassPicker.getValue() == null){toRemove.add(s);}
									else if(!s.fromArchetype(subclassPicker.getValue())){
										toRemove.add(s);}}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						
						if(sourcePicker.getValue()!=null) {//source filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!s.fromSource(sourcePicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {
								query.remove(s);
								if(knownQuery.contains(s)){knownQuery.remove(s);}
							}
						}
						
						updateSpellList();
					}
				};
				//Add filter inputs to panel, and set them up to apply when used.
																//TODO - Label for adding filters.
				nameFilter.setOnAction(filterQuery);		topBar.add(nameFilter, 2, 0);
				levelFilter.setOnAction(filterQuery);		topBar.add(levelFilter, 3, 0);
				schoolPicker.setOnAction(filterQuery);		topBar.add(schoolPicker, 4, 0);
				label = new Label("\t");					topBar.add(label, 5, 0);
				vPicker.setOnAction(filterQuery);			topBar.add(vPicker, 6, 0);
				sPicker.setOnAction(filterQuery);			topBar.add(sPicker, 7, 0);
				mPicker.setOnAction(filterQuery);			topBar.add(mPicker, 8, 0);
				costPicker.setOnAction(filterQuery);		topBar.add(costPicker, 9, 0);
				
				//Start the 2nd row
				GridPane secondBar = new GridPane();
				label = new Label("\t\t\t\t\t\t");			secondBar.add(label, 0, 0);
				timePicker.setOnAction(filterQuery);		secondBar.add(timePicker, 1, 0);
				ritualPicker.setOnAction(filterQuery);		secondBar.add(ritualPicker, 2, 0);
				concPicker.setOnAction(filterQuery);		secondBar.add(concPicker, 3, 0);
				areaPicker.setOnAction(filterQuery);		secondBar.add(areaPicker, 4, 0);
				label = new Label("\t");					secondBar.add(label, 5, 0);
				/*classPicker.setOnAction(filterQuery);*/	secondBar.add(classPicker, 6, 0);
				subclassPicker.setOnAction(filterQuery);	secondBar.add(subclassPicker, 7, 0);
				sourcePicker.setOnAction(filterQuery);		secondBar.add(sourcePicker, 8, 0);
	
				classPicker.setOnAction(new EventHandler<ActionEvent>() {
					//Set subclasses options based on selected class
					@Override public void handle(ActionEvent event) {
						if(classPicker.getValue() == null){
							subclassPicker.getItems().remove(1, subclassPicker.getItems().size());
							subclassPicker.setValue(null);
							subclassPicker.setVisible(false);
						}else{
							subclassPicker.getItems().remove(1, subclassPicker.getItems().size());
							subclassPicker.getItems().addAll(
									classPicker.getValue().getSubclasses(classPicker.getValue()));
							subclassPicker.setValue(null);
							subclassPicker.setVisible(true);
						}
						subclassPicker.getOnAction().handle(event);//Execute the filter method.
					}});
				
			grid.add(topBar, 0, 0, 3, 1);
			grid.add(secondBar, 0, 1, 3, 1);
			
			
			
			
			
			
			
			
			

			
			
			//1st pane for spell list							//TODO - Label for Pane 1
			label = new Label(" Spell list");
			grid.add(label, 0, 2);
			
			names = new ArrayList<Label>();
			levels = new ArrayList<Label>();
			learn = new ArrayList<Button>();
			
      		ScrollPane sp = new ScrollPane();//allow scrolling down the spell list
      		spellList = new GridPane();
      		spellList.setHgap(10);
	  		label = new Label(" Spell");//make the list of spells and their levels
	  		spellList.add(label, 0, 0);
	  		label = new Label(" Level");
	  		spellList.add(label, 1, 0);
	  		label = new Label(" Learn");
	  		spellList.add(label, 2, 0);
	  		
	      	for(int i=0; i<spells.size(); i++) {
	      		//Name label
	      		Spell spellI = spells.get(i);
	      		label = new Label(" "+spellI.getName());
	      		Tooltip toolTip = new Tooltip(spellI.toString());
	      		toolTip.setWrapText(true);
	      		toolTip.setMaxWidth(600);
	      		label.setTooltip(toolTip);
	      		names.add(label);
	      		spellList.add(label, 0, i+1);
	      		//Level label
	      		label = new Label(" "+spellI.getLevel());
	      		levels.add(label);
	      		spellList.add(label, 1, i+1);
	      		//Learn button
	      		Button button = new Button("\t");
	      		button.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						if(!known.contains(spellI)){
							//Add the spell to the known list.
							System.out.println("Learning spell: "+spellI.getName());
							known.add(spellI);
							//Perform a bubble sort to place new spells in the list.
							Spell slot;
							boolean sorted = false;
							while(!sorted){
								sorted = true;
								for(int i=known.size()-1; i>0; i--){
									if(known.get(i).getLevel()<known.get(i-1).getLevel()
										|| (known.get(i).getLevel()==known.get(i-1).getLevel()
											&& known.get(i).getName().compareToIgnoreCase(known.get(i-1).getName())<0)){
										sorted = false;
										slot = known.get(i);
										known.set(i, known.get(i-1));
										known.set(i-1, slot);
									}
								}
							}
							//Make the spell visible in the known list.
							int i=0;	boolean found = false;
					      	for(Spell s:spells) {
					      		if(!found){
					      			if(!s.getName().equals(spellI.getName())){ i++;	
					      			}else{found = true;}
					      		}}
					      	knownNames.get(i).setVisible(true);
					      	knownLevels.get(i).setVisible(true);
					      	prepare.get(i).setVisible(true);
					      	forget.get(i).setVisible(true);
						}else{
							System.out.println("Already know "+spellI.getName());
						}
						//TODO - remove this once it's represented elsewhere
						System.out.print("Know: ");
						for(Spell s:known){System.out.print(s.getName()+" ");}
						System.out.println();
					}
				});
	      		learn.add(button);
	      		spellList.add(button, 2, i+1);
	      	}
	      	
	      	sp.setContent(spellList);
	      	sp.setMinSize(200, 0);
	      	grid.add(sp,0,3);

	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	//2nd pane for spell addition						//TODO - Label for the 2nd pane
			label = new Label(" Known spells");
			grid.add(label, 1, 2);
			
	      	knownNames = new ArrayList<Label>();
	      	knownLevels = new ArrayList<Label>();
	      	prepare = new ArrayList<Button>();
	      	forget = new ArrayList<Button>();
			
      		sp = new ScrollPane();//allow scrolling down the spell list
      		knownList = new GridPane();
      		knownList.setHgap(10);
	  		label = new Label(" Spell");//make the list of spells and their levels
	  		knownList.add(label, 0, 0);
	  		label = new Label(" Level");
	  		knownList.add(label, 1, 0);
	  		label = new Label(" Prepare");
	  		knownList.add(label, 2, 0);
	  		label = new Label(" Forget");
	  		knownList.add(label, 3, 0);

	      	for(int i=0; i<spells.size(); i++) {
	      		//Name label
	      		Spell spellI = spells.get(i);
	      		Label namelabel = new Label(" "+spellI.getName());
	      		Tooltip toolTip = new Tooltip(spellI.toString());
	      		toolTip.setWrapText(true);
	      		toolTip.setMaxWidth(600);
	      		namelabel.setTooltip(toolTip);
	      		knownNames.add(namelabel);
	      		knownList.add(namelabel, 0, i+1);
	      		namelabel.setVisible(false);
	      		//Level label
	      		Label levellabel = new Label(" "+spellI.getLevel());
	      		knownLevels.add(levellabel);
	      		knownList.add(levellabel, 1, i+1);
	      		levellabel.setVisible(false);
	      		//Prepare button
	      		Button prepbutton = new Button("\t");
	      		prepare.add(prepbutton);
	      		knownList.add(prepbutton, 2, i+1);
	      		prepbutton.setVisible(false);
	      		//Forget button
	      		Button forgetbutton = new Button("\t");
	      		forget.add(forgetbutton);
	      		knownList.add(forgetbutton, 3, i+1);
	      		forgetbutton.setVisible(false);
	      		//Button functions
	      		prepbutton.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						if(!slots.get(spellI.getLevel()).contains(spellI)){
							//Add the spell to the prepared list.
							System.out.println("Learning spell: "+spellI.getName());
							slots.get(spellI.getLevel()).add(spellI);
							//Perform a bubble sort to place new spells in the list.
							Spell slot;
							boolean sorted = false;
							while(!sorted){
								sorted = true;
								for(int i=slots.get(spellI.getLevel()).size()-1; i>0; i--){
									if(slots.get(spellI.getLevel()).get(i).getLevel()<slots.get(spellI.getLevel()).get(i-1).getLevel()
										|| (slots.get(spellI.getLevel()).get(i).getLevel()==slots.get(spellI.getLevel()).get(i-1).getLevel()
											&& slots.get(spellI.getLevel()).get(i).getName().compareToIgnoreCase(slots.get(spellI.getLevel()).get(i-1).getName())<0)){
										sorted = false;
										slot = slots.get(spellI.getLevel()).get(i);
										slots.get(spellI.getLevel()).set(i, slots.get(spellI.getLevel()).get(i-1));
										slots.get(spellI.getLevel()).set(i-1, slot);
									}
								}
							}
						}else{
							System.out.println("Already prepared "+spellI.getName());
						}
						//TODO - remove this once it's represented elsewhere
						System.out.print("Prepared at level "+spellI.getLevel()+": ");
						for(Spell s:slots.get(spellI.getLevel())){System.out.print(s.getName()+" ");}
						System.out.println();
					}
				});
	      		forgetbutton.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						//Remove from the lists
						known.remove(spellI);
						if(slots.get(spellI.getLevel()).contains(spellI)){
							slots.get(spellI.getLevel()).remove(spellI);
							//TODO - remove window features for the spell
						}
						//Hide the entry in the known lists
			      		namelabel.setVisible(false);
			      		levellabel.setVisible(false);
			      		prepbutton.setVisible(false);
			      		forgetbutton.setVisible(false);
						//TODO - remove this once it's represented elsewhere
						System.out.print("Know: ");
						for(Spell s:known){System.out.print(s.getName()+" ");}
						System.out.println();
					}
				});
	      	}
	  		
	      	sp.setContent(knownList);
	      	sp.setMinSize(250, 0);
	      	grid.add(sp, 1, 3);
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	//3rd pane for current spells						//TODO - Label for the 3rd pane
			label = new Label(" Prepared spells");
			grid.add(label, 2, 2);
			
			//TODO - set up spell lists to be filled
			//TODO - spell unprepping
	      	//TODO - - cant unprep cantrips
			
	      	//grid.add(curSpell, 2, 3);

	      	
	      	
	    //make the window
		secondaryStage.setScene(new Scene(grid, 1000, 400));
		secondaryStage.show();
		return secondaryStage;
	}

	//TODO - update known list entries (add and remove)
	//TODO - known list queries
	//TODO - update prepared list entries (add and remove)
  	//TODO - autoprep known cantrips.
	
	private void updateSpellList() {
		for(int i=0; i<spells.size(); i++) {
			boolean visible = query.contains(spells.get(i));
			Label curName = names.get(i);
			Label curLevel = levels.get(i);
			curName.setVisible(visible);//hide the entry in the list for excluded spells
			curLevel.setVisible(visible);
			if(!visible) {
				spellList.getChildren().remove(curName);
				spellList.getChildren().remove(curLevel);
			}else if(!(spellList.getChildren().contains(curName)||spellList.getChildren().contains(curLevel))){
				spellList.getChildren().add(curName);
				spellList.getChildren().add(curLevel);
			}			
		}
	}
	
	//TODO - remove
//	private void learnSpell(Spell spell){		
//      		//Name label
//      		Label namelabel = new Label(" "+spell.getName());
//      		Tooltip toolTip = new Tooltip(spell.toString());
//      		toolTip.setWrapText(true);
//      		toolTip.setMaxWidth(600);
//      		namelabel.setTooltip(toolTip);
//      		knownNames.add(namelabel);
//      		knownList.add(namelabel, 0, knownDepth);
//      		
//      		//Level label
//      		Label levellabel = new Label(" "+spell.getLevel());
//      		knownLevels.add(levellabel);
//      		knownList.add(levellabel, 1, knownDepth);
//      		
//      		//Prepare buttons
//      		Button prepbutton = new Button("\t");
//      		prepbutton.setOnAction(new EventHandler<ActionEvent>() {
//				@Override public void handle(ActionEvent event) {
//					if(!slots.get(spell.getLevel()).contains(spell)){
//						//Add the spell to the prepared list.
//						System.out.println("Preparing spell: "+spell.getName());
//						slots.get(spell.getLevel()).add(spell);
//						//Perform a bubble sort to place new spells in the list.
//						Spell slot;
//						boolean sorted = false;
//						while(!sorted){
//							sorted = true;
//							for(int i=slots.get(spell.getLevel()).size()-1; i>0; i--){
//								if(slots.get(spell.getLevel()).get(i).getLevel()<slots.get(spell.getLevel()).get(i-1).getLevel()
//									|| (slots.get(spell.getLevel()).get(i).getLevel()==slots.get(spell.getLevel()).get(i-1).getLevel()
//										&& slots.get(spell.getLevel()).get(i).getName().compareToIgnoreCase(slots.get(spell.getLevel()).get(i-1).getName())<0)){
//									sorted = false;
//									slot = slots.get(spell.getLevel()).get(i);
//									slots.get(spell.getLevel()).set(i, slots.get(spell.getLevel()).get(i-1));
//									slots.get(spell.getLevel()).set(i-1, slot);
//								}
//							}
//						}
//					}else{
//						System.out.println("Already prepared "+spell.getName());
//					}
//					//TODO - remove this once it's represented elsewhere
//					System.out.print("Prepared at level "+spell.getLevel()+": ");
//					for(Spell s:slots.get(spell.getLevel())){System.out.print(s.getName()+" ");}
//					System.out.println();
//				}
//			});
//      		prepare.add(prepbutton);
//      		knownList.add(prepbutton, 2, knownDepth);
//      		
//      		//Forget button
//      		Button forgetbutton = new Button("\t");
//      		forgetbutton.setOnAction(new EventHandler<ActionEvent>() {
//				@Override public void handle(ActionEvent event) {
//					//Remove the spell from the list
//					known.remove(spell);
//					//Remove window elements from tracking lists
//		      		knownNames.remove(namelabel);
//		      		knownLevels.remove(levellabel);
//		      		prepare.remove(prepbutton);
//		      		forget.remove(forgetbutton);
//		      		//Remove window elements from the window
//		      		knownList.add(namelabel, 0, knownDepth);
//		      		knownList.add(levellabel, 1, knownDepth);
//		      		knownList.add(prepbutton, 2, knownDepth);
//		      		knownList.add(forgetbutton, 3, knownDepth);
//				}
//			});
//      		forget.add(forgetbutton);
//      		knownList.add(forgetbutton, 3, knownDepth);
//      		
//      		knownDepth++;
//	}
}
