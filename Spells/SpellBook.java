package Spells;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import Resources.Source;
import Resources.Classes.Subclass;
import Spells.Spell.School;
import Resources.Classes;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

@SuppressWarnings("restriction")
public class SpellBook {
	private final List<Spell> spells;
	private Stage secondaryStage = null;
	
	public SpellBook(List<Spell> spells) {
		this.spells = spells;
	}
	
	private TextField nameFilter;
	private List<Spell> query;
	private GridPane spellList;
		private List<Label> names;
		private List<Label> levels;
		private List<Button> learn;

	private List<Spell> known;
	private List<Spell> knownQuery;
	private GridPane knownList;
		private List<Label> knownNames;
		private List<Label> knownLevels;
		private List<Button> prepare;
		private List<Button> forget;
		
	private GridPane preparedList;
		private Map<Integer, List<Spell>> slots;
		private Map<Integer, ChoiceBox<Integer>> slotCounts;
		private Map<Integer, GridPane> levelPanes;
		private List<Label> preppedNames;
		private List<Button> unprep;
	
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
				Button save = new Button("Save spellbook");		//TODO - Label for save and load inputs
				save.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						saveBook();}});
				topBar.add(save, 0, 0);
				Button load = new Button("Load spellbook");
				load.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						loadBook();}});
				topBar.add(load, 1, 0);
				
			
				Label label = new Label("\t\t");
				topBar.add(label, 2, 0);
				//Set up filter inputs							//TODO - Label for filter setup
				nameFilter = new TextField();
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
		      			"1 Minute","10 Minutes","1 Hour","8 Hours","12 Hours","24 Hours","Special");
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
						updateKnownList();
					}
				};
				//Add filter inputs to panel, and set them up to apply when used.
																//TODO - Label for adding filters.
				nameFilter.setOnAction(filterQuery);				topBar.add(nameFilter, 3, 0);
				levelFilter.setOnAction(filterQuery);				topBar.add(levelFilter, 4, 0);
				schoolPicker.setOnAction(filterQuery);				topBar.add(schoolPicker, 5, 0);
				label = new Label("\t");							topBar.add(label, 6, 0);
				vPicker.setOnAction(filterQuery);					topBar.add(vPicker, 7, 0);
				sPicker.setOnAction(filterQuery);					topBar.add(sPicker, 8, 0);
				mPicker.setOnAction(filterQuery);					topBar.add(mPicker, 9, 0);
				costPicker.setOnAction(filterQuery);				topBar.add(costPicker, 10, 0);
				
				//Start the 2nd row
				GridPane secondBar = new GridPane();
				label = new Label("\t\t\t\t\t\t\t\t\t\t");secondBar.add(label, 0, 0);
				timePicker.setOnAction(filterQuery);				secondBar.add(timePicker, 1, 0);
				ritualPicker.setOnAction(filterQuery);				secondBar.add(ritualPicker, 2, 0);
				concPicker.setOnAction(filterQuery);				secondBar.add(concPicker, 3, 0);
				label = new Label("\t");							secondBar.add(label, 4, 0);
				/*classPicker.setOnAction(filterQuery);*/			secondBar.add(classPicker, 5, 0);
				subclassPicker.setOnAction(filterQuery);			secondBar.add(subclassPicker, 6, 0);
				sourcePicker.setOnAction(filterQuery);				secondBar.add(sourcePicker, 7, 0);
	
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
			GridPane listHeader = new GridPane();
			label = new Label(" Spell list\t\t\t\t");
			listHeader.add(label, 0, 0);
			Button addAll = new Button("Learn all"); //Button for adding all queried.
				//Good for clerics and druids and things which can prepare from the whole class list
			addAll.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					for(Spell s: query){
						if(!known.contains(s)){
							known.add(s);
							if(s.getLevel()==0){							//Cantrips are always prepared
								slots.get(0).add(s);}
						}
						if(subclassPicker.getValue()!=null){				//Automatically prepare domain spells.
							//Cleric, Druid, Paladin, and Ranger archetypes have always prepared domain spells.
								//Warlock just gives more choices
							if(classPicker.getValue()==Classes.CLERIC || classPicker.getValue()==Classes.DRUID
								|| classPicker.getValue()==Classes.PALADIN || classPicker.getValue()==Classes.RANGER){
								if(s.fromArchetype(subclassPicker.getValue()) && !slots.get(s.getLevel()).contains(s)){
									slots.get(s.getLevel()).add(s);
								}
							}
						}
					}
					nameFilter.getOnAction().handle(null);//Update filtered list with the new spell
				}
			});
			listHeader.add(addAll, 1, 0);
			grid.add(listHeader, 0, 2);
			
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
							known.add(spellI);
							if(spellI.getLevel()==0){slots.get(0).add(spellI);}//Cantrips are always prepared.
							nameFilter.getOnAction().handle(null);//Update filtered list with the new spell
						}
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
	      		Button prepbutton = null;
	      		if(spellI.getLevel()!=0){//Cantrips don't need prepared
	      			prepbutton = new Button("\t");
	      			knownList.add(prepbutton, 2, i+1);
	      			prepbutton.setVisible(false);
	      		}
	      		prepare.add(prepbutton);
	      		//Forget button
	      		Button forgetbutton = new Button("\t");
	      		forget.add(forgetbutton);
	      		knownList.add(forgetbutton, 3, i+1);
	      		forgetbutton.setVisible(false);
	      		//Button functions
	      		if(prepbutton!=null){
		      		prepbutton.setOnAction(new EventHandler<ActionEvent>() {
						@Override public void handle(ActionEvent event) {
							if(!slots.get(spellI.getLevel()).contains(spellI)){
								//Add the spell to the prepared list.
								slots.get(spellI.getLevel()).add(spellI);
								updatePreppedList();
							}
						}
					});
		      	}
	      		forgetbutton.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						//Remove from the lists
						known.remove(spellI);
						if(knownQuery.contains(spellI)){knownQuery.remove(spellI);}
						if(slots.get(spellI.getLevel()).contains(spellI)){
							slots.get(spellI.getLevel()).remove(spellI);
						}
						updateKnownList();//Remove spell from the window.
					}
				});
	      	}
	  		
	      	sp.setContent(knownList);
	      	sp.setMinSize(250, 0);
	      	grid.add(sp, 1, 3);
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	//3rd pane for current spells						//TODO - Label for the 3rd pane
			label = new Label(" Prepared spells");
			grid.add(label, 2, 2);
			
			slots = new HashMap<Integer, List<Spell>>();//Set up lists.
			for(int i=0; i<=9; i++){
				slots.put(i, new ArrayList<Spell>());
			}
			slotCounts = new HashMap<Integer, ChoiceBox<Integer>>();
			levelPanes = new HashMap<Integer, GridPane>();
			preppedNames = new ArrayList<Label>();
			unprep = new ArrayList<Button>();

			preparedList = new GridPane();
				GridPane slotPane = new GridPane();
					//Header row.
					label = new Label("Spell level");
					label.setMinWidth(60);
					slotPane.add(label, 0, 0);
					label = new Label("Slots");
					slotPane.add(label, 1, 0, 2, 1);
					label = new Label("Spell list");
					slotPane.add(label, 3, 0);
					//Each spell level
					for(int i=0; i<=9; i++){
						//Level label
						label = new Label("Level "+i);
						if(i==0){label.setText("Cantrip");}
						slotPane.add(label, 0, i+1);
						//Slot management						//TODO - Label for slot management
						if(i!=0){//Cantrips don't have slots
							//Slot count select
							final ChoiceBox<Integer> slotcount;
							if(i == 1){
								slotcount = new ChoiceBox<Integer>(FXCollections.observableArrayList(0,1,2,3,4));
							}else if(i >= 2 && i <=5){
								slotcount = new ChoiceBox<Integer>(FXCollections.observableArrayList(0,1,2,3));
							}else if(i == 6 || i == 7){
								slotcount = new ChoiceBox<Integer>(FXCollections.observableArrayList(0,1,2));
							}else/*(i == 8 || i == 9)*/{
								slotcount = new ChoiceBox<Integer>(FXCollections.observableArrayList(0,1));
							}
							slotcount.setValue(0);
							slotCounts.put(i, slotcount);
							slotPane.add(slotcount, 1, i+1);
							//Slot tracker
							GridPane slotRadioPane = new GridPane();
								List<RadioButton> slotRadios = new ArrayList<RadioButton>();
								for(int j=0; j<slotcount.getItems().size(); j++){
									RadioButton rb = new RadioButton();
									slotRadios.add(rb);
									slotRadioPane.add(rb, j, 0);
									rb.setVisible(false);
								}
							slotPane.add(slotRadioPane, 2, i+1);
							//Slot count varying visibility
							slotcount.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {
									for(int j=0; j<slotcount.getItems().size(); j++){
										if(j<slotcount.getValue()){
											slotRadios.get(j).setVisible(true);
										}else{//Deselect when they're no longer visible.
												//This means when visible again they aren't selected.
											slotRadios.get(j).setSelected(false);
											slotRadios.get(j).setVisible(false);
										}
									}
								}
							});
						}
						//Spell list							//TODO - Label for prepared spell lists
						sp = new ScrollPane();//allow scrolling around the spell list
						GridPane preppedSpellList = new GridPane();
					      	for(int j=0; j<spells.size(); j++) {
					      		if(spells.get(j).getLevel()==i){
						      		Spell spellI = spells.get(j);
						      		if(i!=0){
							      		//Unprepare button
							      		Button button = new Button("x");
							      		button.setOnAction(new EventHandler<ActionEvent>() {
											@Override public void handle(ActionEvent event) {
												slots.get(spellI.getLevel()).remove(spellI);
												updatePreppedList();
											}
										});
							      		unprep.add(button);
							      		preppedSpellList.add(button, preppedSpellList.getChildren().size(), 0);
							      		button.setVisible(false);
						      		}else{unprep.add(null);}//Cantrips are always prepared
						      		//Name label
						      		label = new Label(" "+spellI.getName()+"\t");
						      		Tooltip toolTip = new Tooltip(spellI.toString());
						      		toolTip.setWrapText(true);
						      		toolTip.setMaxWidth(600);
						      		label.setTooltip(toolTip);
						      		preppedNames.add(label);
						      		preppedSpellList.add(label, preppedSpellList.getChildren().size(), 0);
						      		label.setVisible(false);
					      		}
					      	}
						levelPanes.put(i, preppedSpellList);
//						slotPane.add(preppedSpellList, 3, i+1);
				      	sp.setContent(preppedSpellList);
				      	sp.setMinHeight(40);
				      	slotPane.add(sp,3,i+1);
					}
				slotPane.setHgap(10);
				slotPane.setVgap(5);
				preparedList.add(slotPane, 0, 0);

				
			//TODO - Daily panel
			//TODO - note level of daily uses
				//TODO - Note at wills
			
	      	grid.add(preparedList, 2, 3);

	      	
	    nameFilter.getOnAction().handle(null);//Update filtered list with the new spell
	    //make the window
		secondaryStage.setScene(new Scene(grid, 1200, 600));
		secondaryStage.show();
		return secondaryStage;
	}

	
	
	
	
	
	
	
	
	
	
	
	public void saveBook(){							//TODO - Label for book saving
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("Resources/Spellbooks/"));
		File file = fileChooser.showSaveDialog(secondaryStage);
		
		if(file!=null){
			System.out.println("Saving spellbook for "+file.getName());
			//Build the new XML string
			String xml = "<spellbook>\n";
			xml+="\t<name>"+file.getName()+"</name>\n";
			
			xml+="\t<slot>\n";//Slot based spells
			xml+="\t\t<known>";//Known spells
			boolean first = true;
			for(Spell s:known){
				if(!first){xml+=",";}
				else{first = false;}
				xml+=s.getName();
			}
			xml+="</known>\n";
			
			for(int i=0; i<=9;i++){//Prepared spells
				if(!slots.get(i).isEmpty() || (i!=0 && slotCounts.get(i).getValue()>0)){//only make an entry if that level has slots or spells
					xml+="\t\t<"+i+">";
					if(i!=0){xml+=slotCounts.get(i).getValue()+",";}//Cantrips dont have slots.
					first = true;
					for(Spell s:slots.get(i)){
						if(!first){xml+=",";}
						else{first = false;}
						xml+=s.getName();
					}
					xml+="</"+i+">\n";
				}
			}
			xml+="\t</slot>\n";
			
			//TODO - Daily uses
			//		<daily>
			//			<3(uses per day)>dancing lights, faerie fire (list of spells)</3>
			//			<3>can have multiple of the some number of uses.</3>
			//		</daily>
			
			xml+="</spellbook>\n";
			//Save the new spells.
			try (PrintWriter out = new PrintWriter(file)) {
			    out.print(xml);
			    System.out.println("Spellbook saving complete");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else{System.out.println("Spellbook saving failed");}
	}

	public void loadBook(){							//TODO - Label for book loading
		SpellBookInstance loading = new SpellBookInstance(secondaryStage, spells);
		//Apply the data to the window
		known = loading.getKnown();
		for(int i=0; i<=9; i++){
			if(i!=0){
				slotCounts.get(i).setValue(loading.getSlots(i));
			}
			slots.put(i, loading.getPrepped(i));
		}
		//TODO - Daily uses
		nameFilter.getOnAction().handle(null);//Update filtered list with the new spell
	}
	
	
	
	
	private void updateSpellList() {							//TODO - Label for update methods
		for(int i=0; i<spells.size(); i++) {
			boolean visible = query.contains(spells.get(i));
			Label curName = names.get(i);
			Label curLevel = levels.get(i);
			Button curLearn = learn.get(i);
			curName.setVisible(visible);//hide the entry in the list for excluded spells
			curLevel.setVisible(visible);
			curLearn.setVisible(visible);
			if(!visible) {
				spellList.getChildren().remove(curName);
				spellList.getChildren().remove(curLevel);
				spellList.getChildren().remove(curLearn);
			}else if(!(spellList.getChildren().contains(curName)||spellList.getChildren().contains(curLevel)
					 ||spellList.getChildren().contains(curLearn))){
				spellList.getChildren().add(curName);
				spellList.getChildren().add(curLevel);
				spellList.getChildren().add(curLearn);
			}			
		}
	}
	
	private void updateKnownList() {
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
		//Update the display of the known list.
		for(int i=0; i<spells.size(); i++) {
			boolean visible = knownQuery.contains(spells.get(i));
			Label curName = knownNames.get(i);
			Label curLevel = knownLevels.get(i);
			Button curPrep = prepare.get(i);
			Button curForget = forget.get(i);
			curName.setVisible(visible);//hide the entry in the list for excluded spells
			curLevel.setVisible(visible);
			if(curPrep!=null){curPrep.setVisible(visible);}
			curForget.setVisible(visible);
			if(!visible) {
				knownList.getChildren().remove(curName);
				knownList.getChildren().remove(curLevel);
				if(curPrep!=null){knownList.getChildren().remove(curPrep);}
				knownList.getChildren().remove(curForget);
			}else if(!(knownList.getChildren().contains(curName)				   ||knownList.getChildren().contains(curLevel)
				    ||(curPrep!=null && knownList.getChildren().contains(curPrep)) ||knownList.getChildren().contains(curForget))){
				knownList.getChildren().add(curName);
				knownList.getChildren().add(curLevel);
				if(curPrep!=null){knownList.getChildren().add(curPrep);}
				knownList.getChildren().add(curForget);
			}			
		}
		updatePreppedList();
	}
	
	private void updatePreppedList() {
		for(int i=0; i<spells.size(); i++) {
			int level = spells.get(i).getLevel();
			boolean visible = slots.get(level).contains(spells.get(i));
			GridPane spellLevel = levelPanes.get(level);
			Label curName = preppedNames.get(i);
			Button curUnprep = unprep.get(i);
			curName.setVisible(visible);//hide the entry in the list for excluded spells
			if(curUnprep!=null){curUnprep.setVisible(visible);}
			if(!visible) {
				spellLevel.getChildren().remove(curName);
				if(curUnprep!=null){spellLevel.getChildren().remove(curUnprep);}
			}else if(!(spellLevel.getChildren().contains(curName)||(curUnprep!=null && spellLevel.getChildren().contains(curUnprep)))){
				spellLevel.getChildren().add(curName);
				if(curUnprep!=null){spellLevel.getChildren().add(curUnprep);}
			}			
		}
	}
	
}
