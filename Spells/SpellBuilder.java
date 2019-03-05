package Spells;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Resources.Source;
import Resources.Area;
import Resources.Classes;
import Resources.Classes.Subclass;
import Spells.Spell.School;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

@SuppressWarnings("restriction")
public class SpellBuilder {
	private final List<Spell> spells;
	private Stage secondaryStage = null;
	
	public SpellBuilder(List<Spell> spells) {
		this.spells = spells;
	}
	
	private List<Spell> query;
	private GridPane spellList;
		private List<Label> names;
		private List<Label> levels;
	private GridPane curSpell;
  		private TextField nameField;
  		private ChoiceBox<Integer> levelPicker;
  		private ChoiceBox<School> schoolSelect;
  		
  		private RadioButton vRadio, sRadio, mRadio;
  		private RadioButton gpCostToggle;
  		private TextField materialsField;
  		
  		private ChoiceBox<String> castTimeSelect;
  		private RadioButton ritualSelect;
  		private ChoiceBox<String> durationSelect;
  		private RadioButton concSelect;
  		
  		private ChoiceBox<Area> areaSelect;
  		private TextField rangeField;
  		private TextField lengthAField, lengthBField;
  		private Label rangeLabel, lengthALabel, lengthBLabel; //Labels need to be here to be hidden and changed.
  		
  		private TextArea spellBodyField;
  		private Map<Classes, RadioButton> classButtons;
  		private Map<Classes, Map<Subclass, RadioButton>> archetypes;
  		private ChoiceBox<Source> sourceSelect;
	
	public Stage makeDisplay() {
		if(secondaryStage != null) {secondaryStage.close();}
		secondaryStage = new Stage();//make the window
		secondaryStage.setTitle("Spell List");
		GridPane grid = new GridPane();
        grid.setHgap(10);
      	grid.setVgap(10);
      	
      		query = new ArrayList<Spell>();
      		query.addAll(spells);
			names = new ArrayList<Label>();
			levels = new ArrayList<Label>();
			
			GridPane topBar = new GridPane();
			
				Button save = new Button("Save changes");		//TODO - Label for save button
				save.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Saving spells");
						List<Spell> sortedspells = new ArrayList<Spell>(spells);
						//Perform a bubble sort to place new spells in the list.
						Spell slot;
						boolean sorted = false;
						while(!sorted){
							sorted = true;
							for(int i=sortedspells.size()-1; i>0; i--){
								if(sortedspells.get(i).getLevel()<sortedspells.get(i-1).getLevel()
									|| (sortedspells.get(i).getLevel()==sortedspells.get(i-1).getLevel()
										&& sortedspells.get(i).getName().compareToIgnoreCase(sortedspells.get(i-1).getName())<0)){
									sorted = false;
									slot = sortedspells.get(i);
									sortedspells.set(i, sortedspells.get(i-1));
									sortedspells.set(i-1, slot);
								}
							}
						}
						//Build the new XML string
						String xml = "";
						for(int i=0; i<sortedspells.size(); i++){xml+=sortedspells.get(i).toXML();}
						//Save the new spells.
						try (PrintWriter out = new PrintWriter(new File("Resources/SpellList"))) {
						    out.print(xml);
						    System.out.println("Spell saving complete");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
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
						query.removeAll(spells);//text filter
						for(Spell s:spells) {
							if(s.getName().toLowerCase().contains(nameFilter.getText().toLowerCase())) {
								query.add(s);}}
						if(query.isEmpty()) {query.addAll(spells);}

						if(levelFilter.getValue()!=null) {//level filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getLevel()==levelFilter.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						if(schoolPicker.getValue()!=null) {//school filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getSchool()==schoolPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						
						if(vPicker.getValue()!=null) {//verbal component filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getComponents()[0]==vPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						if(sPicker.getValue()!=null) {//somatic component filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getComponents()[1]==sPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						if(mPicker.getValue()!=null) {//material component filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getComponents()[2]==mPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						if(costPicker.getValue()!=null) {//gp cost filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.gpCost()==costPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						

						if(timePicker.getValue()!=null) {//cast time filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.castTime().equals(timePicker.getValue()))) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						if(ritualPicker.getValue()!=null) {//ritual filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.isRitual()==ritualPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						if(concPicker.getValue()!=null) {//concentration filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.isConcentration()==concPicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						
						if(classPicker.getValue()!=null) {//class filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!s.fromClass(classPicker.getValue())) {
									//Treats it as an or. If its not on that class list but is on the archetypes, it stays.
									if(subclassPicker.getValue() == null){toRemove.add(s);}
									else if(!s.fromArchetype(subclassPicker.getValue())){
										toRemove.add(s);}}
							}for(Spell s:toRemove) {query.remove(s);}
						}
						
						if(sourcePicker.getValue()!=null) {//source filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!s.fromSource(sourcePicker.getValue())) {
									toRemove.add(s);}
							}for(Spell s:toRemove) {query.remove(s);}
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
				label = new Label("\t");					secondBar.add(label, 4, 0);
				/*classPicker.setOnAction(filterQuery);*/	secondBar.add(classPicker, 5, 0);
				subclassPicker.setOnAction(filterQuery);	secondBar.add(subclassPicker, 6, 0);
				sourcePicker.setOnAction(filterQuery);		secondBar.add(sourcePicker, 7, 0);

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
				
			grid.add(topBar, 0, 0, 2, 1);
			grid.add(secondBar, 0, 1, 2, 1);
			

			
			
			//1st pane for spell list							//TODO - Label for Pane 1
      		ScrollPane sp = new ScrollPane();//allow scrolling down the spell list
      		spellList = new GridPane();
      		spellList.setHgap(10);
	  		label = new Label(" Spell");//make the list of spells and their levels
	  		spellList.add(label, 0, 0);
	  		label = new Label(" Level");
	  		spellList.add(label, 1, 0);
	      	for(int i=0; i<spells.size(); i++) {
	      		label = new Label(" "+spells.get(i).getName());
	      		Tooltip toolTip = new Tooltip(spells.get(i).toString());
	      		toolTip.setWrapText(true);
	      		toolTip.setMaxWidth(600);
	      		label.setTooltip(toolTip);
	      		names.add(label);
	      		spellList.add(label, 0, i+1);
	      		label = new Label(" "+spells.get(i).getLevel());
	      		levels.add(label);
	      		spellList.add(label, 1, i+1);
	      	}
	      	sp.setContent(spellList);
	      	grid.add(sp,0,2);

	      	
	      	
	      	//2nd pane for spell addition						//TODO - Label for pane 2
	      	sp = new ScrollPane();//Allow scrolling for smaller screens
	      	curSpell = new GridPane();
	      	curSpell.setHgap(10);
			curSpell.setVgap(5);
	      	int layer = 0;

	      	label = new Label(" Name");
	      	curSpell.add(label, 0, layer);
	      	nameField = new TextField();
	      	nameField.setMaxWidth(800);
	      	curSpell.add(nameField, 1, layer);
	      	layer++;

	      	//Spell level
	      	label = new Label(" Level");
	      	curSpell.add(label, 0, layer);
	    	levelPicker = new ChoiceBox<Integer>(FXCollections.observableArrayList(null,0,1,2,3,4,5,6,7,8,9));
	    	levelPicker.setValue(null);
	      	curSpell.add(levelPicker, 1, layer);
	      	layer++;

	      	//School
	      	label = new Label(" School");
	      	curSpell.add(label, 0, layer);
			schoolSelect = new ChoiceBox<School>(FXCollections.observableArrayList());
			schoolSelect.getItems().add(null);
			schoolSelect.getItems().addAll(Spell.School.values());
			schoolSelect.setValue(null);
			schoolSelect.setConverter(new StringConverter<School>(){
				@Override public School fromString(String arg0) {return null;}
				@Override public String toString(School school) {
					if(school != null){
						return school.toNiceString();}
					return null;
				}});
	      	curSpell.add(schoolSelect, 1, layer);
	      	layer++;
	      	
	      	//components
	      	label = new Label(" Components");
	      	curSpell.add(label, 0, layer);
	      	GridPane componentsBar = new GridPane();
	      	componentsBar.setHgap(10);
	      	vRadio = new RadioButton("V");
	      	sRadio = new RadioButton("S");
	      	mRadio = new RadioButton("M");
	      	mRadio.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					//Make the material details visible if theres material components.
					if(mRadio.isSelected()){
						gpCostToggle.setVisible(true);
						materialsField.setVisible(true);
					}else{
						gpCostToggle.setSelected(false);
						materialsField.setText("");
						gpCostToggle.setVisible(false);
						materialsField.setVisible(false);
					}
				}
	      	});
	      	componentsBar.add(vRadio, 0, 0);
	      	componentsBar.add(sRadio, 1, 0);
	      	componentsBar.add(mRadio, 2, 0);
	      	//Material details
	  		materialsField = new TextField();
	  		gpCostToggle = new RadioButton("Has gold cost");
	      	componentsBar.add(materialsField, 3, 0);
	      	componentsBar.add(gpCostToggle, 4, 0);
	      	materialsField.setVisible(false);
	      	gpCostToggle.setVisible(false);
	      	curSpell.add(componentsBar, 1, layer);
	      	layer++;
	      	
	      	//times
	      	label = new Label(" Cast time");
	      	curSpell.add(label, 0, layer);
	      	GridPane timePanel = new GridPane();
	      	//Cast time
	      	castTimeSelect = new ChoiceBox<String>(FXCollections.observableArrayList());
	      	castTimeSelect.getItems().addAll(null,"Action","Bonus Action","Reaction",
	      			"1 Minute","10 Minutes","1 Hour","8 Hours","12 Hours","24 Hours","Special");
	      	castTimeSelect.setValue(null);
	      	timePanel.add(castTimeSelect, 0, 0);
	      	ritualSelect = new RadioButton("Ritual");
	      	timePanel.add(ritualSelect, 1, 0);
	      	//Duration
	      	label = new Label("\tDuration");
	      	timePanel.add(label, 2, 0);
	      	//arbitrary duration skipped
	      	durationSelect = new ChoiceBox<String>(FXCollections.observableArrayList());
	      	durationSelect.getItems().addAll(null,"Instantaneous","1 Round","6 Rounds","1 Minute","10 Minutes",
	      			"1 Hour","2 Hours","8 Hours","24 Hours","1 Day","7 Days","10 Days","30 Days",
	      			"Special","Until Dispelled","Until Dispelled or Triggered");
	      	durationSelect.setValue(null);
	      	timePanel.add(durationSelect, 3, 0);
	      	concSelect = new RadioButton("Concentration");
	      	timePanel.add(concSelect, 4, 0);
	      	timePanel.setHgap(5);
	      	curSpell.add(timePanel, 1, layer);
	      	layer++;
	      	
	      	
	      	
	      	//area
	      	label = new Label(" Area");
	      	curSpell.add(label, 0, layer);
	      	GridPane areaPanel = new GridPane();
	      	areaPanel.setHgap(10);
	      	areaSelect = new ChoiceBox<Area>(FXCollections.observableArrayList());
	      	areaSelect.getItems().add(null);
	      	areaSelect.getItems().addAll(Area.values());
	      	areaSelect.setValue(null);
	      	areaSelect.setConverter(new StringConverter<Area>(){
				@Override public Area fromString(String arg0) {return null;}
				@Override public String toString(Area area) {
					if(area != null){
						return area.toNiceString();}
					return null;
				}});
	      	areaSelect.setOnAction(new EventHandler<ActionEvent>() {
	      		//Adjust visibility and labelling of other area fields based on selection.
				@Override public void handle(ActionEvent event) {
					Area value = areaSelect.getValue();
					if(value == null || value == Area.SELF || value == Area.UNLIMITED) { //Hide other fields if unneeded.
				  		rangeField.setText("0");
				  		lengthAField.setText("0");
						lengthBField.setText("0");
				  		rangeField.setVisible(false);
				  		lengthAField.setVisible(false);
						lengthBField.setVisible(false);
				  		rangeLabel.setVisible(false);
				  		lengthALabel.setVisible(false);
						lengthBLabel.setVisible(false);
					} else if(value == Area.SINGLE) { //Single only should need a range
				  		rangeField.setText("0");
				  		lengthAField.setText("0");
						lengthBField.setText("0");
				  		rangeField.setVisible(true);
				  		lengthAField.setVisible(false);
						lengthBField.setVisible(false);
				  		rangeLabel.setVisible(true);
				  		lengthALabel.setVisible(false);
						lengthBLabel.setVisible(false);
					} else if(value == Area.CONE || value == Area.SPHERE || value == Area.CUBE) { //Areas with 1 length
				  		rangeField.setText("0");
				  		lengthAField.setText("0");
						lengthBField.setText("0");
				  		rangeField.setVisible(true);
				  		lengthAField.setVisible(true);
						lengthBField.setVisible(false);
				  		rangeLabel.setVisible(true);
				  		if(value == Area.CONE) {lengthALabel.setText("Length: ");}
				  		if(value == Area.SPHERE) {lengthALabel.setText("Radius: ");}
				  		if(value == Area.CUBE) {lengthALabel.setText("Width: ");}
				  		lengthALabel.setVisible(true);
						lengthBLabel.setVisible(false);
					} else if(value == Area.LINE || value == Area.CYLINDER) { //Areas with 2 lengths
				  		rangeField.setText("0");
				  		lengthAField.setText("0");
						lengthBField.setText("0");
				  		rangeField.setVisible(true);
				  		lengthAField.setVisible(true);
						lengthBField.setVisible(true);
				  		rangeLabel.setVisible(true);
				  		if(value == Area.LINE) {lengthALabel.setText("Length: ");}
				  		if(value == Area.CYLINDER) {lengthALabel.setText("Radius: ");}
				  		lengthALabel.setVisible(true);
				  		if(value == Area.LINE) {lengthBLabel.setText("Width: ");}
				  		if(value == Area.CYLINDER) {lengthBLabel.setText("Height: ");}
						lengthBLabel.setVisible(true);
					}
				}
	      	});
	      	areaPanel.add(areaSelect, 0, 0);
	      	//Range field
	      	rangeLabel = new Label("Range: ");
	      	areaPanel.add(rangeLabel, 1, 0);
	      	rangeLabel.setVisible(false);
	      	rangeField = new TextField();
	      	rangeField.setText("0");
	      	rangeField.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
	      		@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	      			if (!newValue.matches("\\d*")) {//remove non ints
	      				rangeField.setText(newValue.replaceAll("[^\\d]", ""));
	      			}
	      			if(newValue.isEmpty()) {rangeField.setText("0");}//ensure not empty
	      			rangeField.setText(""+Integer.parseInt(rangeField.getText()));//remove leading 0's
				}
      	    });
	      	areaPanel.add(rangeField, 2, 0);
	      	rangeField.setVisible(false);
	      	//length A
	      	lengthALabel = new Label();
	      	areaPanel.add(lengthALabel, 3, 0);
	      	lengthALabel.setVisible(false);
	      	lengthAField = new TextField();
	      	lengthAField.setText("0");
	      	lengthAField.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
	      		@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	      			if (!newValue.matches("\\d*")) {//remove non ints
	      				lengthAField.setText(newValue.replaceAll("[^\\d]", ""));
	      			}
	      			if(newValue.isEmpty()) {lengthBField.setText("0");}//ensure not empty
	      			lengthAField.setText(""+Integer.parseInt(lengthAField.getText()));//remove leading 0's
				}
      	    });
	      	areaPanel.add(lengthAField, 4, 0);
	      	lengthAField.setVisible(false);
	      	//length B
	      	lengthBLabel = new Label();
	      	areaPanel.add(lengthBLabel, 5, 0);
	      	lengthBLabel.setVisible(false);
	      	lengthBField = new TextField();
	      	lengthBField.setText("0");
	      	lengthBField.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
	      		@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	      			if (!newValue.matches("\\d*")) {//remove non ints
	      				lengthBField.setText(newValue.replaceAll("[^\\d]", ""));
	      			}
	      			if(newValue.isEmpty()) {lengthBField.setText("0");}//ensure not empty
	      			lengthBField.setText(""+Integer.parseInt(lengthBField.getText()));//remove leading 0's
				}
      	    });
	      	areaPanel.add(lengthBField, 6, 0);
	      	lengthBField.setVisible(false);
	      	
	      	curSpell.add(areaPanel, 1, layer);
	      	layer++;
	      	
	      	
	      	
	      	//Spell description
	      	label = new Label(" Description");
	      	curSpell.add(label, 0, layer);
	      	spellBodyField = new TextArea();
	      	spellBodyField.setWrapText(true);
	      	spellBodyField.setMaxWidth(800);
	      	curSpell.add(spellBodyField, 1, layer);
	      	layer++;
	      	
	      	//Classes
	      	label = new Label(" Classes");
	      	curSpell.add(label, 0, layer);
	      	GridPane classPanel = new GridPane();
	      	classPanel.setHgap(10);
	      	Classes[] casterClasses = {Classes.BARD, Classes.CLERIC, Classes.DRUID, Classes.PALADIN,
	      			Classes.RANGER, Classes.SORCERER, Classes.WARLOCK, Classes.WIZARD};
	      	classButtons = new HashMap<Classes, RadioButton>();
      		int x = 0;
	      	for(Classes c: casterClasses){
	      		RadioButton rb = new RadioButton(c.toNiceString());
	      		classButtons.put(c, rb);
	      		classPanel.add(rb, x, 0);
	      		x++;
	      	}
	      	curSpell.add(classPanel, 1, layer);
	      	layer++;
	      	//Archetypes.
	      	label = new Label(" Archetypes");
	      	curSpell.add(label, 0, layer);
	      	GridPane subclassPanel = new GridPane();
	      	subclassPanel.setVgap(5);
	      	archetypes = new HashMap<Classes, Map<Subclass, RadioButton>>();
	      	x = 0;
	      	int y=0;
	      	GridPane classRow = new GridPane();
	      	classRow.setHgap(5);
	      	for(Classes c: Classes.values()){
	      		RadioButton rb = new RadioButton(c.toNiceString());
		      	rb.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						//Make the archetype buttons hideable by groups of class.
						if(rb.isSelected()){
							for(Subclass s: c.getSubclasses(c)){
								archetypes.get(c).get(s).setVisible(true);
								archetypes.get(c).get(s).setSelected(false);
							}
						}else{
							for(Subclass s: c.getSubclasses(c)){
								archetypes.get(c).get(s).setVisible(false);
								archetypes.get(c).get(s).setSelected(false);
							}
						}
					}
		      	});
		      	classRow.add(rb, x, 0);
	      		x++;
	      	}
	      	subclassPanel.add(classRow, 0, 0);
	      	for(Classes c: Classes.values()){
	      		//Gridpane each row for spacing to be independent.
	      		y++;
	      		x=0;
	      		classRow = new GridPane();
		      	classRow.setHgap(5);
		      	archetypes.put(c, new HashMap<Subclass, RadioButton>());
	      		for(Subclass s: c.getSubclasses(c)){
		      		RadioButton rb = new RadioButton(s.toNiceString());
		      		archetypes.get(c).put(s, rb);//Fill the maps so all the buttons can be easily iterated later
			      	classRow.add(rb, x%7, x/7);
			      	rb.setVisible(false);
	      			x++;
	      		}
		      	subclassPanel.add(classRow, 0, y);
	      	}
	      	curSpell.add(subclassPanel, 1, layer);
	      	layer++;
	      	
	      	label = new Label(" Source");
	      	curSpell.add(label, 0, layer);
			sourceSelect = new ChoiceBox<Source>(FXCollections.observableArrayList());
			sourceSelect.getItems().add(null);
			sourceSelect.getItems().addAll(Source.values());
			sourceSelect.setValue(null);
			sourceSelect.setConverter(new StringConverter<Source>(){
				@Override public Source fromString(String arg0) {return null;}
				@Override public String toString(Source source) {
					if(source != null){
						return source.toNiceString();}
					return null;
				}});
	      	curSpell.add(sourceSelect, 1, layer);
	      	layer++;
	      	
			Button add = new Button("Add spell");				//TODO - Label for spell creation.
			add.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					//Set up anonymous class
					Spell newSpell = new Spell() {
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
						@Override public boolean isConcentration(){return isConc;}

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
							if(isRitual){builtString += "(R)";}
							//Duration
							builtString += "\nDuration: "+duration;
							if(isConc){builtString += " (Concentration)";}
							
							builtString += "\n\n"+effect+"\n";//List spell body
							
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
					
					
					
					//Set up constructed field results
					boolean[] materials = new boolean[3];
						materials[0] = vRadio.isSelected();
						materials[1] = sRadio.isSelected();
						materials[2] = mRadio.isSelected();
					int[] dimensions = new int[2];//Includes some redundancy on the initial entry. Subsequent saves remove the dead entries.
						dimensions[0] = Integer.parseInt(lengthAField.getText());
						dimensions[1] = Integer.parseInt(lengthBField.getText());
					List<Classes> classList = new ArrayList<Classes>();
						for(Classes c: classButtons.keySet()){
							if(classButtons.get(c).isSelected()){classList.add(c);}}
					List<Subclass> subclassList = new ArrayList<Subclass>();
						for(Classes c: archetypes.keySet()){
							for(Subclass s: archetypes.get(c).keySet()){
								if(archetypes.get(c).get(s).isSelected()){subclassList.add(s);}}}
					List<Source> sourcesList = new ArrayList<Source>();
						sourcesList.add(sourceSelect.getValue());
						
					//Create instance
					newSpell.constructor(nameField.getText(), levelPicker.getValue(), schoolSelect.getValue(),
							materials, gpCostToggle.isSelected(), materialsField.getText(),
							castTimeSelect.getValue(), ritualSelect.isSelected(), durationSelect.getValue(), concSelect.isSelected(),
							areaSelect.getValue(), Integer.parseInt(rangeField.getText()), dimensions,
							"\n"+spellBodyField.getText()+"\n\t",//Some extra formatting so it can be formatted nicely.
							classList, subclassList,
							sourcesList);
					
					System.out.println("Adding spell "+newSpell.getName());
		      			Label label = new Label(" "+newSpell.getName());
		      			Tooltip toolTip = new Tooltip(newSpell.toString());
			      		toolTip.setMaxWidth(600);
		      			label.setTooltip(toolTip);
		      			names.add(label);
		      			spellList.add(label, 0, spells.size()+1);
		      			label = new Label(" "+newSpell.getLevel());
		      			levels.add(label);
		      			spellList.add(label, 1, spells.size()+1);
					spells.add(newSpell);
					nameFilter.getOnAction().handle(null);//Update filtered list with the new spell
				}
			});
	      	add.setMinWidth(100);
	      	//The widest part of the first column, so this min width should apply to the column.
			curSpell.add(add, 0, layer);
	      	
	      	sp.setContent(curSpell);
	      	grid.add(sp,1,2);

	      	
	      	
	    //make the window
	    secondaryStage.setScene(new Scene(grid, 1550, 900));
		secondaryStage.show();
		return secondaryStage;
	}

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
}
