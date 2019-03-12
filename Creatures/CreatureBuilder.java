package Creatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Creatures.Creature.Alignment;
import Creatures.Creature.DamageMultiplier;
import Creatures.Creature.DamageType;
import Creatures.Creature.Languages;
import Creatures.Creature.Region;
import Creatures.Creature.Senses;
import Creatures.Creature.Size;
import Creatures.Creature.Skills;
import Creatures.Creature.Speeds;
import Creatures.Creature.Stats;
import Creatures.Creature.StatusCondition;
import Creatures.Creature.Type;
import Creatures.Creature.Type.Subtype;
import Resources.Source;
import Spells.Spell;
import Spells.SpellBook;
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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

@SuppressWarnings("restriction")
public class CreatureBuilder {

	private final List<Creature> creatures;
	private final List<Spell> spells;
	private Stage secondaryStage = null;
	
	public CreatureBuilder(List<Creature> creatures, List<Spell> spells) {
		this.creatures = creatures;
		this.spells = spells;
	}
	
	private List<Creature> query;									//TODO - Label for global fields
	private GridPane creatureList;
		private List<Label> names;
		private List<Label> crs;
	
	private GridPane curCreature;
  		private TextField nameField;
  		private ToggleGroup sizePicker = new ToggleGroup();
  		private ToggleGroup typePicker = new ToggleGroup();
  			private GridPane subtypePanel;
  			private RadioButton shapechangerToggle;
  			private Map<Type,List<RadioButton>> subtypePicker;
  	  	private List<RadioButton> alignPicker;
  	  	
  		private ChoiceBox<Double> crPicker;
  	  	private TextField hpPicker, acPicker;
  	  	private Map<Speeds, TextField> speedPicker;
  	  	private Map<Stats, TextField> statPicker;
  	  	private Map<Stats, RadioButton> savesPicker;
	  		private Map<Stats, TextField> savesSetter;
  	  	private Map<Skills, RadioButton> skillPicker;
  	  		private Map<Skills, TextField> skillSetter;
  	  		
  	  	private Map<DamageMultiplier,Map<DamageType,RadioButton>> resistancePicker;
  	  	private Map<StatusCondition, RadioButton> conditionPicker;
  	  	private Map<Senses, RadioButton> sensePicker;
	  		private Map<Senses, TextField> senseSetter;
		private Map<Languages, RadioButton> languagePicker;
		private Map<Region, RadioButton> regionPicker;
	    

		private ChoiceBox<Integer> LegRes;
		private File innateSpells, stdSpells;
			private ChoiceBox<Stats> innateAbility, castAbility;
			private TextField innateMod, innateDC, castMod, castDC;
			private ChoiceBox<Integer> innateLevel, castLevel;
		private Map<RadioButton, String> standardPassives;
			private List<RadioButton> orderedStandardPassives;
		private Map<TextField, TextArea> passiveInputs;
			private int passiveCount = 0;
		private TextArea otherInfo;
		
		private TextArea multiattack;
		private int attackCount = 0;//May need a use limit field.
			private List<TextField> attackNames;
			private List<TextField> attackToHits;
			private List<TextField> attackShortRange;
			private List<TextField> attackLongRange;
			private List<TextArea> attackDesc;//Includes targets, on hit, and on miss.
		private int otherActionCount = 0;
			private List<TextField> otherActNames;
			private List<ChoiceBox<String>> otherActLimits;
			private List<TextArea> otherActDesc;
			
		private Map<TextField, TextArea> reactionInputs;
			private int reactionCount = 0;
		private ChoiceBox<Integer> legendActCount;
			private List<TextField> legendActNames;
			private List<ChoiceBox<Integer>> legendActCosts;
			private List<TextArea> legendActDesc;
			private int legendActEntryCount = 0;
		private List<TextArea> lairActions;
  		private ChoiceBox<Source> sourceSelect;
  		

  		public Stage makeDisplay() {
  			if(secondaryStage != null) {secondaryStage.close();}
  			secondaryStage = new Stage();//make the window
  			secondaryStage.setTitle("Creature List");
  			GridPane grid = new GridPane();
  	        grid.setHgap(10);
  	      	grid.setVgap(10);
  	      	
  	      		query = new ArrayList<Creature>();
  	      		query.addAll(creatures);
  				names = new ArrayList<Label>();
  				crs = new ArrayList<Label>();
  				
  				GridPane topBar = new GridPane();
  				
  					Button save = new Button("Save changes");		//TODO - Label for the save button
  					save.setOnAction(new EventHandler<ActionEvent>() {
  						@Override
  						public void handle(ActionEvent event) {
  							System.out.println("Saving creatures");
  							//Perform a bubble sort to place new creatures in the list.
  							Creature slot;
  							boolean sorted = false;
  							while(!sorted){
  								sorted = true;
  								for(int i=creatures.size()-1; i>0; i--){
  									if(creatures.get(i).getCR()<creatures.get(i-1).getCR()
  										|| (creatures.get(i).getCR()==creatures.get(i-1).getCR()
  											&& creatures.get(i).getName().compareToIgnoreCase(creatures.get(i-1).getName())<0)){
  										sorted = false;
  										slot = creatures.get(i);
  										creatures.set(i, creatures.get(i-1));
  										creatures.set(i-1, slot);
  									}
  								}
  							}
  							//Build the new XML string
  							String xml = "";
  							for(int i=0; i<creatures.size(); i++){xml+=creatures.get(i).toXML();}
  							//Save the new creatures.
  							try (PrintWriter out = new PrintWriter(new File("Resources/CreatureList"))) {
  							    out.print(xml);
  							    System.out.println("Creature saving complete");
  							} catch (FileNotFoundException e) {
  								e.printStackTrace();
  							}
  						}
  					});
  					topBar.add(save, 0, 0);
  					
  				
  					Label label = new Label("\t\t");topBar.add(label, 1, 0);
  					//Set up filter inputs
  					TextField nameFilter = new TextField();

  					ChoiceBox<Double> crFilter = new ChoiceBox<Double>(FXCollections.observableArrayList(
  							null,0.0,0.125,0.25,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,
  							10.0,11.0,12.0,13.0,14.0,15.0,16.0,17.0,18.0,19.0,
  							20.0,21.0,22.0,23.0,24.0,25.0,26.0,27.0,28.0,29.0,30.0
  							));
  					crFilter.setConverter(new StringConverter<Double>() {
  						@Override public Double fromString(String string) {return null;}
  						@Override public String toString(Double object) {
  							if(object==null){return "CR";}
  							else if(object == 0.125){return "1/8";}
  							else if(object == 0.25){return "1/4";}
  							else if(object == 0.5){return "1/2";}
  							else{return ((Integer)object.intValue()).toString();}
  						}
  					});
  					crFilter.setValue(null);
  					ChoiceBox<Double> upperCRFilter = new ChoiceBox<Double>(FXCollections.observableArrayList(
  							null,0.0,0.125,0.25,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,
  							10.0,11.0,12.0,13.0,14.0,15.0,16.0,17.0,18.0,19.0,
  							20.0,21.0,22.0,23.0,24.0,25.0,26.0,27.0,28.0,29.0,30.0
  							));
  					upperCRFilter.setConverter(new StringConverter<Double>() {
  						@Override public Double fromString(String string) {return null;}
  						@Override public String toString(Double object) {
  							if(object==null){return "CR";}
  							else if(object == 0.125){return "1/8";}
  							else if(object == 0.25){return "1/4";}
  							else if(object == 0.5){return "1/2";}
  							else{return ((Integer)object.intValue()).toString();}
  						}
  					});
  					upperCRFilter.setValue(null);

  					ChoiceBox<Type> typeFilter = new ChoiceBox<Type>(FXCollections.observableArrayList());
  					typeFilter.getItems().add(null);
  					typeFilter.getItems().addAll(Type.values());
  					typeFilter.setValue(null);
  					typeFilter.setConverter(new StringConverter<Type>(){
  						@Override public Type fromString(String arg0) {return null;}
  						@Override public String toString(Type type) {
  							if(type != null){
  								return type.toNiceString();}
  							return "Type";
  						}});
  					ChoiceBox<String> subtypeFilter = new ChoiceBox<String>(FXCollections.observableArrayList());
  					subtypeFilter.getItems().add(null);
  					subtypeFilter.getItems().add("Shapechanger");
  					subtypeFilter.setValue(null);
  					subtypeFilter.setConverter(new StringConverter<String>(){
  						@Override public String fromString(String arg0) {return null;}
  						@Override public String toString(String string) {
  							if(string != null){
  								return string;}
  							return "Subtype";
  						}});
  					
  					ChoiceBox<Boolean> legResfilter = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
  					legResfilter.getItems().addAll(null,true,false);
  					legResfilter.setValue(null);
  					legResfilter.setConverter(new StringConverter<Boolean>(){
  						@Override public Boolean fromString(String arg0) {return null;}
  						@Override public String toString(Boolean toggle) {
  							if(toggle != null){
  								if(toggle){return "Yes";}
  								else{return "No";}}
  							return "LegRes";
  						}});
  					
  					ChoiceBox<Boolean> legActfilter = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
  					legActfilter.getItems().addAll(null,true,false);
  					legActfilter.setValue(null);
  					legActfilter.setConverter(new StringConverter<Boolean>(){
  						@Override public Boolean fromString(String arg0) {return null;}
  						@Override public String toString(Boolean toggle) {
  							if(toggle != null){
  								if(toggle){return "Yes";}
  								else{return "No";}}
  							return "LegAct";
  						}});
  					
  					ChoiceBox<Boolean> lairfilter = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
  					lairfilter.getItems().addAll(null,true,false);
  					lairfilter.setValue(null);
  					lairfilter.setConverter(new StringConverter<Boolean>(){
  						@Override public Boolean fromString(String arg0) {return null;}
  						@Override public String toString(Boolean toggle) {
  							if(toggle != null){
  								if(toggle){return "Yes";}
  								else{return "No";}}
  							return "Lair";
  						}});
  					
  					ChoiceBox<Boolean> castfilter = new ChoiceBox<Boolean>(FXCollections.observableArrayList());
  					castfilter.getItems().addAll(null,true,false);
  					castfilter.setValue(null);
  					castfilter.setConverter(new StringConverter<Boolean>(){
  						@Override public Boolean fromString(String arg0) {return null;}
  						@Override public String toString(Boolean toggle) {
  							if(toggle != null){
  								if(toggle){return "Yes";}
  								else{return "No";}}
  							return "Spellcasting";
  						}});
  					
  					ChoiceBox<Size> sizefilter = new ChoiceBox<Size>(FXCollections.observableArrayList());
  					sizefilter.getItems().add(null);
  					sizefilter.getItems().addAll(Size.values());
  					sizefilter.setValue(null);
  					sizefilter.setConverter(new StringConverter<Size>(){
  						@Override public Size fromString(String arg0) {return null;}
  						@Override public String toString(Size size) {
  							if(size != null){
  								return size.toNiceString();}
  							return "Size";
  						}});
  					
  					ChoiceBox<Speeds> speedFilter = new ChoiceBox<Speeds>(FXCollections.observableArrayList());
  					speedFilter.getItems().add(null);
  					speedFilter.getItems().addAll(Speeds.values());
  					speedFilter.setValue(null);
  					speedFilter.setConverter(new StringConverter<Speeds>(){
  						@Override public Speeds fromString(String arg0) {return null;}
  						@Override public String toString(Speeds speed) {
  							if(speed != null){
  								return speed.toNiceString();}
  							return "Speed";
  						}});
  					
  					ChoiceBox<DamageType> multiplierFilter = new ChoiceBox<DamageType>(FXCollections.observableArrayList());
  					multiplierFilter.getItems().add(null);
  					multiplierFilter.getItems().addAll(DamageType.values());
  					multiplierFilter.setValue(null);
  					multiplierFilter.setConverter(new StringConverter<DamageType>(){
  						@Override public DamageType fromString(String arg0) {return null;}
  						@Override public String toString(DamageType damage) {
  							if(damage != null){
  								return damage.toNiceString();}
  							return "Resistance";
  						}});
  					
  					ChoiceBox<Alignment> alignFilter = new ChoiceBox<Alignment>(FXCollections.observableArrayList());
  					alignFilter.getItems().add(null);
  					alignFilter.getItems().addAll(Alignment.values());
  					alignFilter.setValue(null);
  					alignFilter.setConverter(new StringConverter<Alignment>(){
  						@Override public Alignment fromString(String arg0) {return null;}
  						@Override public String toString(Alignment align) {
  							if(align != null){
  								return align.toNiceString();}
  							return "Alignment";
  						}});
  					
  					ChoiceBox<Senses> senseFilter = new ChoiceBox<Senses>(FXCollections.observableArrayList());
  					senseFilter.getItems().add(null);
  					senseFilter.getItems().addAll(Senses.values());
  					senseFilter.setValue(null);
  					senseFilter.setConverter(new StringConverter<Senses>(){
  						@Override public Senses fromString(String arg0) {return null;}
  						@Override public String toString(Senses sense) {
  							if(sense != null){
  								return sense.toNiceString();}
  							return "Sense";
  						}});
  					
  					ChoiceBox<Region> regionFilter = new ChoiceBox<Region>(FXCollections.observableArrayList());
  					regionFilter.getItems().add(null);
  					regionFilter.getItems().addAll(Region.values());
  					regionFilter.setValue(null);
  					regionFilter.setConverter(new StringConverter<Region>(){
  						@Override public Region fromString(String arg0) {return null;}
  						@Override public String toString(Region region) {
  							if(region != null){
  								return region.toNiceString();}
  							return "Region";
  						}});
  					
  					ChoiceBox<Source> sourceFilter = new ChoiceBox<Source>(FXCollections.observableArrayList());
  					sourceFilter.getItems().add(null);
  					sourceFilter.getItems().addAll(Source.values());
  					sourceFilter.setValue(null);
  					sourceFilter.setConverter(new StringConverter<Source>(){
  						@Override public Source fromString(String arg0) {return null;}
  						@Override public String toString(Source source) {
  							if(source != null){
  								return source.toNiceString();}
  							return "Source";
  						}});
  					
  					//Define the filtering action					//TODO - label for applying the filter
  					EventHandler<ActionEvent> filterQuery =new EventHandler<ActionEvent>() {
  						@Override
  						public void handle(ActionEvent event) {
  							query.removeAll(creatures);//text filter
  							for(Creature c:creatures) {
  								if(c.getName().toLowerCase().contains(nameFilter.getText().toLowerCase())) {
  									query.add(c);
  								}
  							}
  							if(query.isEmpty()) {query.addAll(creatures);}

  							if(crFilter.getValue()!=null || upperCRFilter.getValue()!=null) {//cr filter
  								//upperCRFilter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(crFilter.getValue()==null){//only upper
	  									if(!(c.getCR()==upperCRFilter.getValue())) {
	  										toRemove.add(c);}
  									}else if(upperCRFilter.getValue()==null){//only lower
	  									if(!(c.getCR()==crFilter.getValue())) {
	  										toRemove.add(c);}
	  								}else{//range
	  									if(!(c.getCR()>=crFilter.getValue() && c.getCR()<=upperCRFilter.getValue())){
	  										toRemove.add(c);}
	  								}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(typeFilter.getValue()!=null) {//type filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(c.getType()!=typeFilter.getValue()) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(subtypeFilter.getValue()!=null) {//subtype filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(subtypeFilter.getValue()=="Shapechanger" && !c.isShapechanger()){toRemove.add(c);}
  									if(c.getSubtype()==null || !c.getSubtype().toNiceString().equals(subtypeFilter.getValue())) {
  										toRemove.add(c);}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(legResfilter.getValue()!=null) {//legres filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!((c.getLegendaryResistances()>0)==legResfilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(legActfilter.getValue()!=null) {//legact filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!((c.getLegendaryActionCount()>0)==legActfilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(lairfilter.getValue()!=null) {//lair filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!(c.hasLair()==lairfilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(castfilter.getValue()!=null) {//casting filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!((c.getSpellcasting()!=null)==castfilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(sizefilter.getValue()!=null) {//size filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!(c.getSize()==sizefilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(speedFilter.getValue()!=null) {//speed filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!(c.hasSpeed(speedFilter.getValue()))) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(multiplierFilter.getValue()!=null) {//resistance filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!(c.getMultiplier(multiplierFilter.getValue())<1)) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(alignFilter.getValue()!=null) {//alignment filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!(c.isAlign(alignFilter.getValue()))) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(senseFilter.getValue()!=null) {//sense filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!c.hasSense(senseFilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(regionFilter.getValue()!=null) {//region filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!c.fromRegion(regionFilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(sourceFilter.getValue()!=null) {//source filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!c.fromSource(sourceFilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							updateCreatureList();
  						}
  					};
  					//Add filter inputs to panel, and set them up to apply when used.
  																	//TODO - Label for adding filters.
  					nameFilter.setOnAction(filterQuery);			topBar.add(nameFilter, 2, 0);
  					crFilter.setOnAction(filterQuery);				topBar.add(crFilter, 3, 0);
  					upperCRFilter.setOnAction(filterQuery);			topBar.add(upperCRFilter, 4, 0);
  					label = new Label("\t");						topBar.add(label, 5, 0);
  					typeFilter.setOnAction(filterQuery);			topBar.add(typeFilter, 6, 0);
  					subtypeFilter.setOnAction(filterQuery);			topBar.add(subtypeFilter, 7, 0);
  					label = new Label("\t");						topBar.add(label, 8, 0);
  					legResfilter.setOnAction(filterQuery);			topBar.add(legResfilter, 9, 0);
  					legActfilter.setOnAction(filterQuery);			topBar.add(legActfilter, 10, 0);
  					lairfilter.setOnAction(filterQuery);			topBar.add(lairfilter, 11, 0);
  					castfilter.setOnAction(filterQuery);			topBar.add(castfilter, 12, 0);
  					//Start the 2nd row
  					GridPane secondBar = new GridPane();
  					label = new Label("\t\t\t\t\t\t");				secondBar.add(label, 0, 0);
  					sizefilter.setOnAction(filterQuery);			secondBar.add(sizefilter, 1, 0);
  					speedFilter.setOnAction(filterQuery);			secondBar.add(speedFilter, 2, 0);
  					multiplierFilter.setOnAction(filterQuery);		secondBar.add(multiplierFilter, 3, 0);
  					alignFilter.setOnAction(filterQuery);			secondBar.add(alignFilter, 4, 0);
  					senseFilter.setOnAction(filterQuery);			secondBar.add(senseFilter, 5, 0);
  					regionFilter.setOnAction(filterQuery);			secondBar.add(regionFilter, 6, 0);
  					label = new Label("\t");						secondBar.add(label, 7, 0);
  					sourceFilter.setOnAction(filterQuery);			secondBar.add(sourceFilter, 8, 0);
  					
  				grid.add(topBar, 0, 0, 2, 1);
  				grid.add(secondBar, 0, 1, 2, 1);
  				

  				
  				
  				
  				
  				
  				
  				
  				
  				
  				
  				//1st pane for creature list						//TODO - Label for first pane
  	      		ScrollPane sp = new ScrollPane();//allow scrolling down the creature list
  	      		creatureList = new GridPane();
  	      		creatureList.setHgap(10);
  	      		label = new Label(" Creature");//make the list of creatures and their crs
  		  		creatureList.add(label, 0, 0);
  		  		label = new Label(" CR");
  		  		creatureList.add(label, 1, 0);
  		      	for(int i=0; i<creatures.size(); i++) {
  		      		label = new Label(" "+creatures.get(i).getName());
  		      		Tooltip toolTip = new Tooltip(creatures.get(i).toString());
  		      		toolTip.setWrapText(true);
  		      		toolTip.setMaxWidth(450);
  		      		label.setTooltip(toolTip);
  		      		names.add(label);
  		      		creatureList.add(label, 0, i+1);
  		      		String crText = "0";
  		      		if(creatures.get(i).getCR()>=1 || creatures.get(i).getCR()==0){
  		      			crText = ""+((int)creatures.get(i).getCR());
  		      		}else if(creatures.get(i).getCR() == 0.5){
  		      			crText = "1/2";
  		      		}else if(creatures.get(i).getCR() == 0.25){
  		      			crText = "1/4";
  		      		}else if(creatures.get(i).getCR() == 0.125){
  		      			crText = "1/8";
  		      		}
  		      		label = new Label(" "+crText);
  		      		crs.add(label);
  		      		creatureList.add(label, 1, i+1);
  		      	}
  		      	sp.setContent(creatureList);
  		      	grid.add(sp,0,2);
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	//2nd pane for creature addition					//TODO - Label for second pane
  		      	curCreature = new GridPane();
  		      	curCreature.setHgap(10);
  		      	curCreature.setVgap(10);
  		      	int layer = 0;

  		      	label = new Label(" Name");
  		      	curCreature.add(label, 0, layer);
  		      	nameField = new TextField();
  		      	curCreature.add(nameField, 1, layer);
  		      	layer++;
  		      	
  	  		    label = new Label(" Size");
  	  		    curCreature.add(label, 0, layer);
  	  		    GridPane sizePanel = new GridPane();
  	  		    sizePanel.setHgap(5);
  	  		    int i = 0;
  	  		    for(Size s:Size.values()){
  	  		    	RadioButton rb = new RadioButton(s.toNiceString());
  	  		    	rb.setToggleGroup(sizePicker);
  	  		    	sizePanel.add(rb, i, 0);
  	  		    	i++;
  	  		    }
  	  		    curCreature.add(sizePanel, 1, layer);
  	  		    layer++;
  		    	
  		      	label = new Label(" Type");							//TODO - Label for type selection
  		      	curCreature.add(label, 0, layer);
  		      	GridPane typePanel = new GridPane();
  		      	typePanel.setHgap(5);
  		      	i = 0;
  		      	for(Type t:Type.values()){
  		      		RadioButton rb = new RadioButton(t.toNiceString());
  		      		rb.setToggleGroup(typePicker);
  		      		rb.setOnAction(new EventHandler<ActionEvent>() {
						@Override public void handle(ActionEvent event) {
							updateSubtypeOptions(t);
						}});
  		      		typePanel.add(rb, i%7, i/7);
  	  		    	i++;
  		      	}
  		      	curCreature.add(typePanel, 1, layer);
  		      	layer++;

  		      	label = new Label(" Subtype");
  		      	curCreature.add(label, 0, layer);
  		      	subtypePanel = new GridPane();
  		      	subtypePanel.setHgap(5);
  		      	subtypePicker = new HashMap<Type,List<RadioButton>>();
      			shapechangerToggle = new RadioButton("Shapechanger\t");
      			subtypePanel.add(shapechangerToggle, 0, 0);
  		      	int j=1;
  		      	for(Type t: Type.values()){
  		      		i=0;
  		      		if(t.hasSubtype()){
  		      			subtypePicker.put(t, new ArrayList<RadioButton>());
		      			RadioButton rb = new RadioButton("None\t");
		      			rb.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent event) {
								for(RadioButton radio:subtypePicker.get(t)){
									if(radio.getText().equals("None\t")){radio.setSelected(true);}
									else{radio.setSelected(false);}
								}
							}
						});
		      			subtypePicker.get(t).add(rb);
		      			subtypePanel.add(rb, i, j);
		      			rb.setVisible(false);
		      			i++;
  		      			for(Subtype s:t.getSubtype(t)){
  		      				rb = new RadioButton(s.toNiceString()+"\t");
  			      			rb.setOnAction(new EventHandler<ActionEvent>() {
  								@Override public void handle(ActionEvent event) {//Deselect other options. If deseleting then set none to on.
  									RadioButton rb = null;
  									for(RadioButton radio:subtypePicker.get(t)){
  										if(radio.getText().equals(s.toNiceString()+"\t")){rb = radio;}
  									}
  									for(RadioButton radio:subtypePicker.get(t)){
  										if(radio.getText().equals("None\t")){radio.setSelected(!rb.isSelected());}
  										else if(radio != rb){radio.setSelected(false);}
  									}
  								}
  							});
  		      				subtypePicker.get(t).add(rb);
  		      				subtypePanel.add(rb, i%7, j+i/7);
  		      				rb.setVisible(false);
  		      				i++;
  		      			}
  		      		}
  		      		j+=i/7;
  		      	}
  		      	curCreature.add(subtypePanel, 1, layer);
  		      	updateSubtypeOptions(null);
  		      	layer++;

  	  	  		label = new Label(" Alignment");
  	  	  		curCreature.add(label, 0, layer);
  	  	  		GridPane alignPanel = new GridPane();
  	  	  		alignPanel.setHgap(5);
  	  	  		alignPicker = new ArrayList<RadioButton>();
  	  	  		i = 0;
  	  	  		for(Alignment a:Alignment.values()){
  	  	  			RadioButton rb = new RadioButton(a.toNiceString());
  	  	  			alignPicker.add(rb);
  	  	  			rb.setOnAction(new EventHandler<ActionEvent>() {
						@Override public void handle(ActionEvent event) {
							//If unaligned can't have other alignments.
							if(rb.getText().equals("Unaligned")){
								for(RadioButton radio:alignPicker){
									if(radio!=rb){radio.setSelected(false);}
								}
							}else{//Cannot be unaligned if has other alignments.
								for(RadioButton radio:alignPicker){
									if(radio.getText().equals("Unaligned")){radio.setSelected(false);}
								}
							}
						}
					});
  	  	  			alignPanel.add(rb, i%3, i/3);
  	  		    	i++;
  	  	  		}
  	  	  		curCreature.add(alignPanel, 1, layer);
  	  	  		layer++;

  		      	label = new Label(" CR");
  		      	curCreature.add(label, 0, layer);
  		    	crPicker = new ChoiceBox<Double>(FXCollections.observableArrayList(
						null,0.0,0.125,0.25,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,
						10.0,11.0,12.0,13.0,14.0,15.0,16.0,17.0,18.0,19.0,
						20.0,21.0,22.0,23.0,24.0,25.0,26.0,27.0,28.0,29.0,30.0
						));
  		    	crPicker.setConverter(new StringConverter<Double>() {
					@Override public Double fromString(String string) {return null;}
					@Override public String toString(Double object) {
						if(object==null){return null;}
						else if(object == 0.125){return "1/8";}
						else if(object == 0.25){return "1/4";}
						else if(object == 0.5){return "1/2";}
						else{return ((Integer)object.intValue()).toString();}
					}
				});
  		    	crPicker.setValue(null);
  		    	curCreature.add(crPicker, 1, layer);
  		    	layer++;
  	  	  		
  		      	label = new Label(" AC");							//TODO - Label for ac and hp input
  		      	curCreature.add(label, 0, layer);
  		      	GridPane HPandAC = new GridPane();
  		      	HPandAC.setHgap(10);
  		      	acPicker = new TextField();
  		      	acPicker.setText("0");
  		      	acPicker.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						if (!newValue.matches("\\d*")) {//remove non ints
							acPicker.setText(newValue.replaceAll("[^\\d]", ""));
	      	            }
						if(newValue.isEmpty()) {acPicker.setText("0");}//ensure not empty
						acPicker.setText(""+Integer.parseInt(acPicker.getText()));//remove leading 0's
					}
	      	    });
  		      	HPandAC.add(acPicker, 0, 0);
  		      	
  		      	label = new Label(" HP");
  		      	HPandAC.add(label, 1, 0);
  		      	hpPicker = new TextField();
  		      	hpPicker.setText("0");
  		      	hpPicker.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						if (!newValue.matches("\\d*")) {//remove non ints
							hpPicker.setText(newValue.replaceAll("[^\\d]", ""));
	      	            }
						if(newValue.isEmpty()) {acPicker.setText("0");}//ensure not empty
						hpPicker.setText(""+Integer.parseInt(hpPicker.getText()));//remove leading 0's
					}
	      	    });
  		      	HPandAC.add(hpPicker, 2, 0);
  		      	curCreature.add(HPandAC, 1, layer);
  		      	layer++;

  		      	label = new Label(" Speed");						//TODO - Label for speed input
  		      	curCreature.add(label, 0, layer);
  		      	speedPicker = new HashMap<Speeds,TextField>();
  		      	GridPane speedPane = new GridPane();
  		      	speedPane.setHgap(10);
	  		      	TextField walkSpeed = new TextField();
	  		      	walkSpeed.setText("0");
	  		      	walkSpeed.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
							if (!newValue.matches("\\d*")) {//remove non ints
								walkSpeed.setText(newValue.replaceAll("[^\\d]", ""));
		      	            }
							if(newValue.isEmpty()) {walkSpeed.setText("0");}//ensure not empty
							walkSpeed.setText(""+Integer.parseInt(walkSpeed.getText()));//remove leading 0's
						}
		      	    });
	  		      	speedPicker.put(Speeds.WALK, walkSpeed);
	  		      	speedPane.add(walkSpeed, 0, 0);
	  		      	i = 1;
	  		      	for(Speeds s:Speeds.values()){
	  		      		if(s!=Speeds.WALK){
	  		  		      	RadioButton rb = new RadioButton(s.toNiceString());
	  		  		      	speedPane.add(rb, i, 0);
	  		  		      	i++;
	  		  		      	TextField curSpeed = new TextField();
	  		  		      	curSpeed.setText("0");
	  		  		      	curSpeed.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
	  							@Override
	  							public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	  								if (!newValue.matches("\\d*")) {//remove non ints
	  									curSpeed.setText(newValue.replaceAll("[^\\d]", ""));
	  			      	            }
	  								if(newValue.isEmpty()) {curSpeed.setText("0");}//ensure not empty
	  								curSpeed.setText(""+Integer.parseInt(curSpeed.getText()));//remove leading 0's
	  							}
	  			      	    });
	  		  		      	speedPicker.put(s, curSpeed);
	  		  		      	speedPane.add(curSpeed, i, 0);
	  		  		      	curSpeed.setVisible(false);
	  		  		      	i++;
	  		  		      	speedPane.getChildren().remove(curSpeed);
	  		  		      	rb.setOnAction(new EventHandler<ActionEvent>() {//Radio toggles visibility of the textbox.
								@Override public void handle(ActionEvent event) {
									curSpeed.setText("0");
									if(rb.isSelected() && !speedPane.getChildren().contains(curSpeed)){
										speedPane.getChildren().add(curSpeed);
									}else{speedPane.getChildren().remove(curSpeed);}
									curSpeed.setVisible(rb.isSelected());
								}
							});
	  		  		    }
	  		      	}
  		      	curCreature.add(speedPane, 1, layer);
  		      	layer++;


  		      	label = new Label(" Stats");						//TODO - Label for start of stats input
  		      	curCreature.add(label, 0, layer);
  		      	statPicker = new HashMap<Stats,TextField>();
  		      	GridPane statsPane = new GridPane();
  		      	statsPane.setHgap(10);
	  		      	i = 0;
	  		      	for(Stats s:Stats.values()){
	  		  	      	label = new Label(s.name());
	  		  	      	statsPane.add(label, i%6, i/6);
	  	  		      	i++;
	  			      	TextField curStat = new TextField();
	  			      	curStat.setText("0");
	  			      	curStat.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
	  						@Override
	  						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	  							if (!newValue.matches("\\d*")) {//remove non ints
	  								curStat.setText(newValue.replaceAll("[^\\d]", ""));
	  			   	            }
	  							if(newValue.isEmpty()) {curStat.setText("0");}//ensure not empty
	  							curStat.setText(""+Integer.parseInt(curStat.getText()));//remove leading 0's
	  						}
	  			        });
	  			      	statPicker.put(s, curStat);
	  			      	statsPane.add(curStat, i%6, i/6);
	  		  	      	i++;
	  		      	}
  		      	curCreature.add(statsPane, 1, layer);
  		      	layer++;
  		      	
  		      	
  		      	label = new Label(" Saves");
  		      	curCreature.add(label, 0, layer);
  		      	savesPicker = new HashMap<Stats,RadioButton>();
  		      	savesSetter = new HashMap<Stats,TextField>();
  		      	GridPane savesPane = new GridPane();
  		      	savesPane.setHgap(10);
	  		      	i = 0;
	  		      	for(Stats s:Stats.values()){
	  		  	      	RadioButton rb = new RadioButton(s.name());
	  		  	      	savesPicker.put(s, rb);
	  		  	      	savesPane.add(rb, i%6, i/6);
	  	  		      	i++;
	  			      	TextField curSave = new TextField();
	  			      	curSave.setText("0");
	  			      	curSave.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
	  						@Override
	  						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	  							if (!newValue.matches("\\d*")) {//remove non ints
	  								curSave.setText(newValue.replaceAll("[^\\d]", ""));
	  			   	            }
	  							if(newValue.isEmpty()) {curSave.setText("0");}//ensure not empty
	  							curSave.setText(""+Integer.parseInt(curSave.getText()));//remove leading 0's
	  						}
	  			        });
	  			      	savesSetter.put(s, curSave);
	  		  		    savesPane.add(curSave, i%6, i/6);
	  		  		    curSave.setVisible(false);
	  		  	      	i++;
	  		  	      	savesPane.getChildren().remove(curSave);
	  		  		   	rb.setOnAction(new EventHandler<ActionEvent>() {//Radio toggles visibility of the textbox.
							@Override public void handle(ActionEvent event) {
								curSave.setText("0");
								if(rb.isSelected() && !savesPane.getChildren().contains(curSave)){
									savesPane.getChildren().add(curSave);
								}else{savesPane.getChildren().remove(curSave);}
								curSave.setVisible(rb.isSelected());
							}
						});
	  		      	}
  		      	curCreature.add(savesPane, 1, layer);
  		      	layer++;
  		      	
  		      	label = new Label(" Skills");
  		      	curCreature.add(label, 0, layer);
  		      	skillPicker = new HashMap<Skills,RadioButton>();
  		      	skillSetter = new HashMap<Skills,TextField>();
  		      	GridPane skillsPane = new GridPane();
  		      	skillsPane.setHgap(10);
	  		      	i = 0;
	  		      	for(Skills s:Skills.values()){
	  		  	      	RadioButton rb = new RadioButton(s.toNiceString());
	  		  	      	skillPicker.put(s, rb);
	  		  	      	skillsPane.add(rb, i%4, i/4);
	  	  		      	i++;
	  			      	TextField curSkill = new TextField();
	  			      	curSkill.setText("0");
	  			      	curSkill.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
	  						@Override
	  						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	  							if (!newValue.matches("\\d*")) {//remove non ints
	  								curSkill.setText(newValue.replaceAll("[^\\d]", ""));
	  			   	            }
	  							if(newValue.isEmpty()) {curSkill.setText("0");}//ensure not empty
	  							curSkill.setText(""+Integer.parseInt(curSkill.getText()));//remove leading 0's
	  						}
	  			        });
	  			      	skillSetter.put(s, curSkill);
	  			      	skillsPane.add(curSkill, i%4, i/4);
	  			      	curSkill.setVisible(false);
	  		  	      	i++;
	  		  	      	skillsPane.getChildren().remove(curSkill);
	  		  		   	rb.setOnAction(new EventHandler<ActionEvent>() {//Radio toggles visibility of the textbox.
							@Override public void handle(ActionEvent event) {
								curSkill.setText("0");
								if(rb.isSelected() && !skillsPane.getChildren().contains(curSkill)){
									skillsPane.getChildren().add(curSkill);
								}else{skillsPane.getChildren().remove(curSkill);}
								curSkill.setVisible(rb.isSelected());
							}
						});
	  		      	}
  		      	curCreature.add(skillsPane, 1, layer);
  		      	layer++;

  		      														//TODO - Label for start of resistance/immunity... input
  		      	resistancePicker = new HashMap<DamageMultiplier,Map<DamageType,RadioButton>>();
  		      	for(DamageMultiplier mult: DamageMultiplier.values()){
  		      		if(mult != DamageMultiplier.NONE && mult != DamageMultiplier.HEALING){
  		      			resistancePicker.put(mult, new HashMap<DamageType,RadioButton>());
  		      			if(mult == DamageMultiplier.RESISTANCE){label = new Label(" Resistances");}
  		      			else if(mult == DamageMultiplier.IMMUNITY){label = new Label(" Immunities");}
  		      			else if(mult == DamageMultiplier.VULNERABILITY){label = new Label(" Vulnerabilities");}
//  		      			else if(mult == DamageMultiplier.HEALING){label = new Label("Healed by");}
  		  		      	curCreature.add(label, 0, layer);
  		  		      	GridPane multsPane = new GridPane();
  		  		      	multsPane.setHgap(10);
  		  		      	i=0;
  		  		      	for(DamageType type:DamageType.values()){
  		  		      		RadioButton rb = new RadioButton(type.toNiceString());
  		  		      		if(type==DamageType.NONMAGICALBASIC){rb.setText("Nonmagical basic");}
  		  		      		resistancePicker.get(mult).put(type, rb);
  		  		      		rb.setOnAction(new EventHandler<ActionEvent>() {//Only one row can be selected for each type.
								@Override public void handle(ActionEvent event) {
									if(rb.isSelected()){
										for(DamageMultiplier m:resistancePicker.keySet()){
											if(m != mult){resistancePicker.get(m).get(type).setSelected(false);}
										}
									}
								}
							});
  		  		      		multsPane.add(rb, i%8, i/8);
  		  		      		i++;
  		  		      	}
  		  		      	curCreature.add(multsPane, 1, layer);
  		      			layer++;
  		      		}
  		      	}
  		      	
  		      	label = new Label(" Condition immunities");
	  		    curCreature.add(label, 0, layer);
	  		    conditionPicker = new HashMap<StatusCondition, RadioButton>();
	  		    GridPane conditionPane = new GridPane();
	  		    conditionPane.setHgap(10);
	  		    i=0;
	  		    for(StatusCondition c: StatusCondition.values()){
	  		    	RadioButton rb = new RadioButton(c.toNiceString());
	  		    	conditionPane.add(rb, i%8, i/8);
	  		    	conditionPicker.put(c, rb);
	  		    	i++;
	  		    }
	  		    curCreature.add(conditionPane, 1, layer);
	  		    layer++;

  		      	label = new Label(" Senses");
  		      	curCreature.add(label, 0, layer);
  		      	sensePicker = new HashMap<Senses,RadioButton>();
  		      	senseSetter = new HashMap<Senses,TextField>();
  		      	GridPane sensePane = new GridPane();
  		      	sensePane.setHgap(10);
	  		      	i = 0;
	  		      	for(Senses s:Senses.values()){
	  		  	      	RadioButton rb = new RadioButton(s.toNiceString());
	  		  	      	sensePicker.put(s, rb);
	  		  	      	sensePane.add(rb, i%4, i/4);
	  	  		      	i++;
	  			      	TextField curSense = new TextField();
	  			      	curSense.setText("0");
	  			      	curSense.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
	  						@Override
	  						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	  							if (!newValue.matches("\\d*")) {//remove non ints
	  								curSense.setText(newValue.replaceAll("[^\\d]", ""));
	  			   	            }
	  							if(newValue.isEmpty()) {curSense.setText("0");}//ensure not empty
	  							curSense.setText(""+Integer.parseInt(curSense.getText()));//remove leading 0's
	  						}
	  			        });
	  			      	senseSetter.put(s, curSense);
	  			      	sensePane.add(curSense, i%4, i/4);
	  			      	curSense.setVisible(false);
	  		  	      	i++;
	  		  	      	sensePane.getChildren().remove(curSense);
	  		  		   	rb.setOnAction(new EventHandler<ActionEvent>() {//Radio toggles visibility of the textbox.
							@Override public void handle(ActionEvent event) {
								curSense.setText("0");
								if(rb.isSelected() && !sensePane.getChildren().contains(curSense)){
									sensePane.getChildren().add(curSense);
								}else{sensePane.getChildren().remove(curSense);}
								curSense.setVisible(rb.isSelected());
							}
						});
	  		      	}
  		      	curCreature.add(sensePane, 1, layer);
  		      	layer++;

  		      	label = new Label(" Languages");
	  		    curCreature.add(label, 0, layer);
	  		    languagePicker = new HashMap<Languages, RadioButton>();
	  		    GridPane languagePane = new GridPane();
	  		    languagePane.setHgap(10);
	  		    i=0; j=0;
	  		    for(Languages l: Languages.values()){
	  		    	RadioButton rb = new RadioButton(l.toNiceString());
	  		    	languagePane.add(rb, i%8, j+i/8);
	  		    	languagePicker.put(l, rb);
	  		    	i++;
	  		    	//Apply rows for language groups.
	  		    	if(j==0 && i>=8){j++; i=0;}	//Common and uncommon languages
	  		    	if(j==1 && i>=6){j++; i=0;}	//Rare languages
	  		    	if(j==2 && i>=8){j++; i=0;}	//Fiendish, elemental, and universal languages
	  		    								//Other languages
	  		    	//TODO - maybe drop down the misc, one off languages.
	  		    }
	  		    curCreature.add(languagePane, 1, layer);
	  		    layer++;

  		      	label = new Label(" Regions");
	  		    curCreature.add(label, 0, layer);
	  		    regionPicker = new HashMap<Region, RadioButton>();
	  		    GridPane regionPane = new GridPane();
	  		    regionPane.setHgap(10);
	  		    i=0;
	  		    for(Region r: Region.values()){
	  		    	RadioButton rb = new RadioButton(r.toNiceString());
	  		    	regionPane.add(rb, i%6, i/6);
	  		    	regionPicker.put(r, rb);
	  		    	i++;
	  		    }
	  		    curCreature.add(regionPane, 1, layer);
	  		    layer++;
  		    	
	  		    
	  		    label = new Label("\t");
	  		    curCreature.add(label, 2, 0);//Column separator.
	  		    
	  		    
  		    	GridPane abilities = new GridPane();				//TODO - Label for start of 2nd column
  		    	abilities.setHgap(10);
  		    	abilities.setVgap(10);
  	      		sp = new ScrollPane();//allow scrolling down the action inputs
  		      	sp.setContent(abilities);
  		      	sp.setMaxHeight(750);
  				curCreature.add(sp, 3, 1, 2, layer-3);
  		      	
  		    	int abilityLayer = 0;
		    		label = new Label(" Passives");
		    		abilities.add(label, 0, abilityLayer);
		    		abilityLayer++;
		    		GridPane passiveSet = new GridPane();
		    		passiveSet.setHgap(5);
		    		passiveSet.setVgap(15);
		    		int passiveLayer = 0;
		    			label = new Label("Legendary resistances");
		    			passiveSet.add(label, 0, passiveLayer);
			    		LegRes = new ChoiceBox<Integer>(FXCollections.observableArrayList(0,1,2,3,4,5));
			    		LegRes.setValue(0);
		    			passiveSet.add(LegRes, 1, passiveLayer);
		    			passiveLayer++;
		    			
		    			
		    			
		    	        Button spellBookButton = new Button("Spellbook generator");//Creates a button for spawning Spellbook windows
		    	        spellBookButton.setOnAction(new EventHandler<ActionEvent>() {
		    	        			@Override
		    	        			public void handle(ActionEvent event) {new SpellBook(spells).makeDisplay();}
		    	        		});
		    	        passiveSet.add(spellBookButton,0,passiveLayer);
		    	        passiveLayer++;
		    	        
		    			label = new Label("Innate spellcasting");	//TODO - Label for start of spellcasting input
		    			passiveSet.add(label, 0, passiveLayer);
		    			GridPane innatePane = new GridPane();
			    			innateSpells = null;
			    			//Second line
			    			label = new Label("Ability: ");
			    			innatePane.add(label, 0, 1);
			    			innateAbility = new ChoiceBox<Stats>();
			    				innateAbility.getItems().add(null);
			    				innateAbility.getItems().addAll(Stats.values());
			    				innateAbility.setValue(null);
			    			innatePane.add(innateAbility, 1, 1);

			    			label = new Label("\tMod: ");
			    			innatePane.add(label, 2, 1);
			    			innateMod = new TextField();
			    			innateMod.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
								@Override
								public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
									if (!newValue.matches("-?\\d*")) {//remove non ints, allow negation
										innateMod.setText(newValue.replaceAll("[^-\\d]", ""));
				      	            }
								}
				      	    });
			    			innatePane.add(innateMod, 3, 1);

			    			label = new Label("\tDC: ");
			    			innatePane.add(label, 4, 1);
			    			innateDC = new TextField();
			    			innateDC.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
								@Override
								public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
									if (!newValue.matches("\\d*")) {//remove non ints
										innateDC.setText(newValue.replaceAll("[^\\d]", ""));
				      	            }
								}
				      	    });
			    			innatePane.add(innateDC, 5, 1);
			    			
			    			label = new Label("\tLevel: ");
			    			innatePane.add(label, 6, 1);
			    			innateLevel = new ChoiceBox<Integer>(FXCollections.observableArrayList(null,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));
			    			innateLevel.setConverter(new StringConverter<Integer>() {
		  						@Override public Integer fromString(String string) {return null;}
		  						@Override public String toString(Integer object) {
		  							if(object==null){return "Level";}
		  							else{return object.toString();}}});
			    			innateLevel.setValue(null);
			    			innatePane.add(innateLevel, 7, 1);
			    			
			    			//First line
			    			Label curInnate = new Label("No file selected");
			    			innatePane.add(curInnate, 3, 0, 3, 1);
			    			Button loadInnate = new Button("Load spellbook");
			    			loadInnate.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {
									FileChooser fileChooser = new FileChooser();
									fileChooser.setInitialDirectory(new File("Resources/Spellbooks/Creatures/"));
									innateSpells = fileChooser.showOpenDialog(secondaryStage);
									//Reset values
									innateAbility.setValue(null);
									innateMod.setText("0");
									innateDC.setText("0");
									//Set the curFile label
									if(innateSpells == null){curInnate.setText("No file selected");}
									else{curInnate.setText(innateSpells.getName());}
								}
							});
			    			innatePane.add(loadInnate, 0, 0, 2, 1);
			    		passiveSet.add(innatePane, 1, passiveLayer);
		    			passiveLayer++;
		    	        
		    			
		    			label = new Label("Spellcasting");
		    			passiveSet.add(label, 0, passiveLayer);
		    			GridPane castingPane = new GridPane();
			    			stdSpells = null;
			    			//Second line
			    			label = new Label("Ability: ");
			    			castingPane.add(label, 0, 1);
			    			castAbility = new ChoiceBox<Stats>();
				    			castAbility.getItems().add(null);
				    			castAbility.getItems().addAll(Stats.values());
				    			castAbility.setValue(null);
			    				castingPane.add(castAbility, 1, 1);

			    			label = new Label("\tMod: ");
			    			castingPane.add(label, 2, 1);
			    			castMod = new TextField();
			    			castMod.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
								@Override
								public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
									if (!newValue.matches("-?\\d*")) {//remove non ints, allow negation
										castMod.setText(newValue.replaceAll("[^-\\d]", ""));
				      	            }
								}
				      	    });
			    			castingPane.add(castMod, 3, 1);

			    			label = new Label("\tDC: ");
			    			castingPane.add(label, 4, 1);
			    			castDC = new TextField();
			    			castDC.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
								@Override
								public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
									if (!newValue.matches("\\d*")) {//remove non ints
										castDC.setText(newValue.replaceAll("[^\\d]", ""));
				      	            }
								}
				      	    });
			    			castingPane.add(castDC, 5, 1);
			    			
			    			label = new Label("\tLevel: ");
			    			castingPane.add(label, 6, 1);
			    			castLevel = new ChoiceBox<Integer>(FXCollections.observableArrayList(null,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));
			    			castLevel.setConverter(new StringConverter<Integer>() {
		  						@Override public Integer fromString(String string) {return null;}
		  						@Override public String toString(Integer object) {
		  							if(object==null){return "Level";}
		  							else{return object.toString();}}});
			    			castLevel.setValue(null);
			    			castingPane.add(castLevel, 7, 1);
			    			//First line
			    			Label curSpells = new Label("No file selected");
			    			castingPane.add(curSpells, 3, 0, 3, 1);
			    			Button loadCast = new Button("Load spellbook");
			    			loadCast.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {
									FileChooser fileChooser = new FileChooser();
									fileChooser.setInitialDirectory(new File("Resources/Spellbooks/Creatures/"));
									stdSpells = fileChooser.showOpenDialog(secondaryStage);
									//Reset values
									castAbility.setValue(null);
									castMod.setText("0");
									castDC.setText("0");
									//Set the curFile label
									if(stdSpells == null){curSpells.setText("No file selected");}
									else{curSpells.setText(stdSpells.getName());}
								}
							});
			    			castingPane.add(loadCast, 0, 0, 2, 1);
			    		passiveSet.add(castingPane, 1, passiveLayer);
		    			passiveLayer++;
		    			
		    														//TODO - Label for start of general passives.
		    			label = new Label("Standards");
		    			passiveSet.add(label, 0, passiveLayer);
			    		standardPassives = new HashMap<RadioButton, String>();
			    		orderedStandardPassives = new ArrayList<RadioButton>();
			    		GridPane standardPassivePane = new GridPane();
			    		standardPassivePane.setHgap(5);
		    				i=0; j=0;//Have a new line between groups
		    				String[][] basics = {
		    						{"Magic resistance","Has advantage on saving throws against spells and other magical effects."},
		    						{"Magic weapons","It's weapon attacks are magical."}
		    				};
			    			for(String[] passive:basics){
				    			RadioButton rb = new RadioButton(passive[0]);
						      		Tooltip toolTip = new Tooltip(passive[1]);
						      		toolTip.setWrapText(true);
						      		toolTip.setMaxWidth(600);
						      		rb.setTooltip(toolTip);
				    			standardPassives.put(rb, passive[1]);
				    			orderedStandardPassives.add(rb);
				    			standardPassivePane.add(rb, i%4, j+i/4);
				    			i++;
			    			}
			    			String[][] standards = {
			    					{"Amphibious","This creature can breathe air and water."},
			    					{"Avoidance","If this creature is subjected to an effect that allows it to make a saving throw to take only half damage, it instead takes no damage if it succeeds on the saving throw, and only half damage if it fails."},
			    					{"Antimagic susceptibility","This creature is incapacitated while in the area of an antimagic field. If targeted by dispel magic, this creature must succeed on a Constitution saving throw against the caster's spell save DC or fall unconcious for 1 minute."},
			    					{"Devil's sight","Magical darkness doesn't impede this creatures darkvision."},
			    					{"Earth glide","This creature can burrow through nonmagical, unworked earth and stone. While doing so, it doesn't disturb the material it moves through."},
			    					{"Flyby","This creature doesn't provoke an opportunity attack when it flies out of an enemies reach."},
			    					{"Immutable form","This creature is immune to any spell or effect that would alter its form."},
			    					{"Incorporeal movement","This creature can move through other creatures and objects as if they were difficult terrain. It takes 5(1d10) force damage if it ends its turn inside an object."},
			    					{"Keen hearing","This creature has advantage on Wisdom (Perception) checks that rely on hearing."},
			    					{"Keen sight","This creature has advantage on Wisdom (Perception) checks that rely on sight."},
			    					{"Keen smell","This creature has advantage on Wisdom (Perception) checks that rely on smell."},
			    					{"Nimble escape","This creature can take the diesngage or hide action as a bonus action on each of its turns."},
			    					{"Pack tactics","This creature has advantage on an attack roll against a creature if at least one of this creature's allies is within 5 feet of the creature and the ally isn't incapacitated."},
			    					{"Shadow stealth","While in dim light or darkness, this creature can take the hide action as a bonus action."},
			    					{"Siege monster","This creature deals double damage to objects and structures."},
			    					{"Spider climb","This creature can climb difficult surfaces, including upside down on cielings, without needing to make an ability check."},
			    					{"Sunlight sensitivity","While in sunlight, this creature has disadvantage on attack rolls, as well as Wisdom (Perception) checks that rely on sight."},
			    					{"Turn immunity","This creature is immune to effects that turn undead."},
			    					{"Turn resistance","This creature has advantage on saving throws against any effect that turns undead."},
			    					{"Web sense","While in contact with a web, this creature knows the exact location of any other creature in contact with the same web."},
			    					{"Web walker","This creature ignores movement restrictions caused by webbing."}
			    			};
			    			j++; i=0;
//			    			label = new Label();//Spacer
//			    			standardPassivePane.add(label, 0, j);
//			    			j++;
			    			for(String[] passive:standards){
			    				RadioButton rb = new RadioButton(passive[0]);
						      		Tooltip toolTip = new Tooltip(passive[1]);
						      		toolTip.setWrapText(true);
						      		toolTip.setMaxWidth(600);
						      		rb.setTooltip(toolTip);
				    			standardPassives.put(rb, passive[1]);
				    			orderedStandardPassives.add(rb);
				    			standardPassivePane.add(rb, i%4, j+i/4);
				    			i++;
			    			}
			    			String[][] natures = {
			    					{"Constructed nature","This creature doesn't require air, food, drink, or sleep."},
			    					{"Elemental nature","An elemental doesn't require air, food, drink, or sleep."},
			    					{"Fey ancestry","This creature has advantage on saving throws against being charmed, and magic can't put it to sleep."},
			    					{"Immortal nature","This creature doesn't require food, drink or sleep."},
			    					{"Ooze nature","An ooze doesn't equire sleep."},
			    					{"Undead nature","An undead doesn't require air, food, drink, or sleep."}
			    			};
			    			j+=i/4; i=0;
			    			label = new Label();//Spacer
			    			standardPassivePane.add(label, 0, j);
			    			j++;
			    			for(String[] passive:natures){
			    				RadioButton rb = new RadioButton(passive[0]);
						      		Tooltip toolTip = new Tooltip(passive[1]);
						      		toolTip.setWrapText(true);
						      		toolTip.setMaxWidth(600);
						      		rb.setTooltip(toolTip);
				    			standardPassives.put(rb, passive[1]);
				    			orderedStandardPassives.add(rb);
				    			standardPassivePane.add(rb, i%4, j+i/4);
				    			i++;
			    			}
			    		passiveSet.add(standardPassivePane, 1, passiveLayer);
			    		passiveLayer++;

	  		    		label = new Label(" Custom passives");		//TODO - Label for custom passives
	  		    		passiveSet.add(label, 0, passiveLayer);
	  		    		Button newPassive = new Button("New passive");
	  		    		passiveSet.add(newPassive, 1, passiveLayer);
	  		    		passiveLayer++;
			    		GridPane customPassiveSet = new GridPane();
			    		passiveInputs = new HashMap<TextField, TextArea>();
			    		newPassive.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent event) {//Add a new passive field set
								TextField passiveName = new TextField();
								TextArea passiveDesc = new TextArea();
								passiveDesc.setMaxHeight(10);
								passiveDesc.setMaxWidth(250);
								Button remove = new Button("x");
								customPassiveSet.add(passiveName, 0, passiveCount);
								customPassiveSet.add(passiveDesc, 1, passiveCount);
								customPassiveSet.add(remove, 2, passiveCount);
								passiveInputs.put(passiveName, passiveDesc);
								passiveCount++;
								remove.setOnAction(new EventHandler<ActionEvent>() {
									@Override public void handle(ActionEvent event) {//Remove the entry row
										customPassiveSet.getChildren().remove(passiveName);
										customPassiveSet.getChildren().remove(passiveDesc);
										customPassiveSet.getChildren().remove(remove);
										passiveInputs.remove(passiveName);
									}
								});
							}
						});
			    		passiveSet.add(customPassiveSet, 1, passiveLayer);
			    		passiveLayer++;

		    			label = new Label("Other notes");//for notes like this spell is self only and things
		    			passiveSet.add(label, 0, passiveLayer);
			    		otherInfo = new TextArea();
			    		otherInfo.setMaxHeight(50);
			    		passiveSet.add(otherInfo, 1, passiveLayer);
			    		passiveLayer++;
		    		abilities.add(passiveSet, 1, abilityLayer);
		    		abilityLayer++;
		    		
		    		
  		    		label = new Label(" Actions");					//TODO - Label for the start of actions
  		    		abilities.add(label, 0, abilityLayer);
  		    		GridPane actionButtons = new GridPane();
	  		    		Button newAttack = new Button("New Attack");
	  		    		actionButtons.add(newAttack, 0, abilityLayer);
	  		    		Button newEffect = new Button("New Effect");
	  		    		actionButtons.add(newEffect, 1, abilityLayer);
  		    		abilities.add(actionButtons, 1, abilityLayer);
		    		abilityLayer++;
		    		GridPane actionSet = new GridPane();
		    			GridPane multiattackPanel = new GridPane();
				    		label = new Label("Multiattack: ");
				    		multiattackPanel.add(label, 0, 0);
				    		multiattack = new TextArea();
				    		multiattack.setMaxHeight(10);
				    		multiattack.setMaxWidth(450);
				    		multiattackPanel.add(multiattack, 1, 0);
		    			actionSet.add(multiattackPanel, 0, 0);
		    			
		    			GridPane attacksPanel = new GridPane();
		    				attackNames = new ArrayList<TextField>();
		    				attackToHits = new ArrayList<TextField>();
		    				attackShortRange = new ArrayList<TextField>();
		    				attackLongRange = new ArrayList<TextField>();
		    				attackDesc = new ArrayList<TextArea>();
		    				newAttack.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {
				    				TextField name = new TextField("Name");
				    				attackNames.add(name);
				    				attacksPanel.add(name, 0, attackCount);
				    				
				    				TextField toHit = new TextField("To Hit");
				    				toHit.setMaxWidth(60);
				    				toHit.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
										@Override
										public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
											if (!newValue.matches("-?\\d*")) {//remove non ints, allows negative
												toHit.setText(newValue.replaceAll("[^-\\d]", ""));
						      	            }
											if(newValue.isEmpty()) {toHit.setText("0");}//ensure not empty
											toHit.setText(""+Integer.parseInt(toHit.getText()));//remove leading 0's
										}
						      	    });
				    				attackToHits.add(toHit);
				    				attacksPanel.add(toHit, 1, attackCount);
				    				
				    				TextField shortRange = new TextField("Short");
				    				shortRange.setMaxWidth(60);
				    				shortRange.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
										@Override
										public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
											if (!newValue.matches("\\d*")) {//remove non ints
												shortRange.setText(newValue.replaceAll("[^\\d]", ""));
						      	            }
											if(newValue.isEmpty()) {shortRange.setText("0");}//ensure not empty
											shortRange.setText(""+Integer.parseInt(shortRange.getText()));//remove leading 0's
										}
						      	    });
				    				attackShortRange.add(shortRange);
				    				attacksPanel.add(shortRange, 2, attackCount);
				    				
				    				TextField longRange = new TextField("Long");
				    				longRange.setMaxWidth(60);
				    				longRange.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
										@Override
										public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
											if (!newValue.matches("\\d*")) {//remove non ints
												longRange.setText(newValue.replaceAll("[^\\d]", ""));
						      	            }
										}
						      	    });
				    				attackLongRange.add(longRange);
				    				attacksPanel.add(longRange, 3, attackCount);
				    				
				    				TextArea desc = new TextArea();
				    				desc.setMaxSize(250, 50);
				    				attackDesc.add(desc);
				    				attacksPanel.add(desc, 4, attackCount);
				    				
									Button remove = new Button("x");
									attacksPanel.add(remove, 5, attackCount);
									remove.setOnAction(new EventHandler<ActionEvent>() {
										@Override public void handle(ActionEvent event) {//Remove the entry row
											attacksPanel.getChildren().remove(name);
											attackNames.remove(name);
											attacksPanel.getChildren().remove(toHit);
											attackToHits.remove(toHit);
											attacksPanel.getChildren().remove(shortRange);
											attackShortRange.remove(shortRange);
											attacksPanel.getChildren().remove(longRange);
											attackLongRange.remove(longRange);
											attacksPanel.getChildren().remove(desc);
											attackDesc.remove(desc);
											attacksPanel.getChildren().remove(remove);
										}
									});
				    				attackCount++;
								}
							});
		    			actionSet.add(attacksPanel, 0, 1);

		    			GridPane effectsPanel = new GridPane();
	    					otherActNames = new ArrayList<TextField>();
	    					otherActLimits = new ArrayList<ChoiceBox<String>>();
	    					otherActDesc = new ArrayList<TextArea>();
	    					newEffect.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent event) {
			    				TextField name = new TextField();
			    				otherActNames.add(name);
			    				effectsPanel.add(name, 0, otherActionCount);
			    				ChoiceBox<String> limit = new ChoiceBox<String>(FXCollections.observableArrayList(null,
			    						"1/Day","2/Day","3/Day","Recharge 4-6","Recharge 5-6","Recharge 6"));
			    				limit.setValue(null);
			    				otherActLimits.add(limit);
			    				effectsPanel.add(limit, 1, otherActionCount);
			    				TextArea description = new TextArea();
			    				description.setMaxHeight(10);
			    				description.setMaxWidth(225);
			    				otherActDesc.add(description);
			    				effectsPanel.add(description, 2, otherActionCount);
								Button remove = new Button("x");
								effectsPanel.add(remove, 3, otherActionCount);
								remove.setOnAction(new EventHandler<ActionEvent>() {
									@Override public void handle(ActionEvent event) {//Remove the entry row
										effectsPanel.getChildren().remove(name);
										otherActNames.remove(name);
										effectsPanel.getChildren().remove(limit);
										otherActLimits.remove(limit);
										effectsPanel.getChildren().remove(description);
										otherActDesc.remove(description);
										effectsPanel.getChildren().remove(remove);
									}
								});
								otherActionCount++;
							}
						});
		    			actionSet.add(effectsPanel, 0, 2);
		    		abilities.add(actionSet, 1, abilityLayer);
		    		abilityLayer++;
		    		
		    		
  		    		label = new Label(" Reactions");
  		    		abilities.add(label, 0, abilityLayer);
  		    		Button newReact = new Button("New reaction");
  		    		abilities.add(newReact, 1, abilityLayer);
		    		abilityLayer++;
		    		GridPane reactionSet = new GridPane();
		    		reactionInputs = new HashMap<TextField,TextArea>();
		    		newReact.setOnAction(new EventHandler<ActionEvent>() {
						@Override public void handle(ActionEvent event) {//Add a new reaction field set
							TextField reactName = new TextField();
							TextArea reactDesc = new TextArea();
							reactDesc.setMaxHeight(10);
							reactDesc.setMaxWidth(250);
							Button remove = new Button("x");
							reactionSet.add(reactName, 0, reactionCount);
							reactionSet.add(reactDesc, 1, reactionCount);
							reactionSet.add(remove, 2, reactionCount);
							reactionInputs.put(reactName, reactDesc);
							reactionCount++;
							remove.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {//Remove the entry row
									reactionSet.getChildren().remove(reactName);
									reactionSet.getChildren().remove(reactDesc);
									reactionSet.getChildren().remove(remove);
									reactionInputs.remove(reactName);
								}
							});
						}
					});
		    		abilities.add(reactionSet, 1, abilityLayer);
		    		abilityLayer++;
		    		
  		    		label = new Label(" Legendary actions");		//TODO - Label for start of legendary aspects
  		    		abilities.add(label, 0, abilityLayer);
  		    		GridPane legActHeader = new GridPane();
  		    			legendActCount = new ChoiceBox<Integer>(FXCollections.observableArrayList(0,1,2,3,4,5));
  		    			legendActCount.setValue(0);
  		    			legActHeader.add(legendActCount, 0, 0);
  		    			Button newLegAct = new Button("New legendary action");
  		    			legActHeader.add(newLegAct, 1, 0);
  		    		abilities.add(legActHeader, 1, abilityLayer);
		    		abilityLayer++;
		    		GridPane legActSet = new GridPane();
		    		legendActNames = new ArrayList<TextField>();
		    		legendActCosts = new ArrayList<ChoiceBox<Integer>>();
		    		legendActDesc = new ArrayList<TextArea>();
		    		newLegAct.setOnAction(new EventHandler<ActionEvent>() {
						@Override public void handle(ActionEvent event) {
		    				TextField name = new TextField();
		    				legendActNames.add(name);
		    				legActSet.add(name, 0, legendActEntryCount);
		    				ChoiceBox<Integer> cost = new ChoiceBox<Integer>(FXCollections.observableArrayList(1,2,3));
		    				cost.setValue(1);
		    				legendActCosts.add(cost);
		    				legActSet.add(cost, 1, legendActEntryCount);
		    				TextArea description = new TextArea();
		    				description.setMaxHeight(10);
		    				description.setMaxWidth(225);
		    				legendActDesc.add(description);
		    				legActSet.add(description, 2, legendActEntryCount);
							Button remove = new Button("x");
		    				legActSet.add(remove, 3, legendActEntryCount);
							remove.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {//Remove the entry row
									legActSet.getChildren().remove(name);
									legendActNames.remove(name);
									legActSet.getChildren().remove(cost);
									legendActCosts.remove(cost);
									legActSet.getChildren().remove(description);
									legendActDesc.remove(description);
									legActSet.getChildren().remove(remove);
								}
							});
		    				
		    				legendActEntryCount++;
						}
					});
		    		abilities.add(legActSet, 1, abilityLayer);
		    		abilityLayer++;
		    		
  		    		label = new Label(" Lair actions");
  		    		abilities.add(label, 0, abilityLayer);
  		    		RadioButton lairToggle = new RadioButton();
  		    		abilities.add(lairToggle, 1, abilityLayer);
		    		abilityLayer++;
		    		GridPane lairSet = new GridPane();
		    		lairActions = new ArrayList<TextArea>();
		    			for(j=0; j<3; j++){
		    				TextArea lairaction = new TextArea();
		    				lairaction.setMaxHeight(50);
		    				lairaction.setMaxWidth(400);
		    				lairActions.add(lairaction);
		    				lairSet.add(lairaction, 0, j);
		    				lairSet.getChildren().remove(lairaction);
		    			}
		    		lairToggle.setOnAction(new EventHandler<ActionEvent>() {//Toggle field visibility
						@Override public void handle(ActionEvent event) {
							if(lairToggle.isSelected()){
								for(TextArea la: lairActions){lairSet.getChildren().add(la);}
							}else{
								for(TextArea la: lairActions){
									la.setText("");
									lairSet.getChildren().remove(la);
								}
							}
						}
					});
		    		abilities.add(lairSet, 1, abilityLayer);
		    		abilityLayer++;
  		    	
  		    	layer = layer-2;
  		      	
  		      	label = new Label(" Source");
  		      	curCreature.add(label, 3, layer);
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
  				curCreature.add(sourceSelect, 4, layer);
  				layer++;
  		      	
  				
  				Button add = new Button("Add creature");			//TODO - Label for creature constructor
  				add.setOnAction(new EventHandler<ActionEvent>() {
  					@Override
  					public void handle(ActionEvent event) {
						Creature newcreature = new Creature() {
							String name;
							Size size;
							Type type;
							Subtype subtype;
							boolean isShapechanger;
							List<Alignment> align;
							double cr;
							int hp;
							int ac;
							Map<Speeds,Integer> speed;
							Map<Stats,Integer> stats;
							Map<Stats,Integer> saves;
							Map<Skills,Integer> skills;
							Map<DamageMultiplier,List<DamageType>> damageMultipliers;
							List<StatusCondition> conditionImmunities;
							Map<Senses,Integer> senses;
							List<Languages> languages;
							List<Region> regions;
							int legendaryResistances;
							Spellcasting innate;
							Spellcasting casting;
							Map<String,String> passives;
							List<String> orderedPassives;
							String otherNotes;
							String multiattack;
							List<Attack> attacks;
							List<Effect> otherActions;
							Map<String,String> reactions;
							int legendaryActionCount;
							List<LegendaryAction> legendaryActions;
							List<String> lairActions;
							List<Source> sources;
	
							@Override public void constructor(String name, Size size, Type type, Subtype subtype,
									boolean isShapechanger, List<Alignment> align, double cr, int hp, int ac,
									Map<Speeds, Integer> speed, Map<Stats, Integer> stats, Map<Stats, Integer> saves,
									Map<Skills, Integer> skills, Map<DamageMultiplier, List<DamageType>> damageMultipliers,
									List<StatusCondition> conditionImmunities, Map<Senses, Integer> senses,
									List<Languages> languages, List<Region> regions, int legendaryResistances,
									Spellcasting innate, Spellcasting casting, Map<String, String> passives, List<String> orderedPassives,
									String otherNotes, String multiattack, List<Attack> attacks,
									List<Effect> otherActions, Map<String, String> reactions, int legendaryActionCount,
									List<LegendaryAction> legendaryActions, List<String> lairActions,
									List<Source> sources) {
								this.name=name;
								this.size=size;
								this.type=type;
								this.subtype=subtype;
								this.isShapechanger=isShapechanger;
								this.align=align;
								this.cr=cr;
								this.hp=hp;
								this.ac=ac;
								this.speed=speed;
								this.stats=stats;
								this.saves=saves;
								this.skills=skills;
								this.damageMultipliers=damageMultipliers;
								this.conditionImmunities=conditionImmunities;
								this.senses=senses;
								this.languages=languages;
								this.regions=regions;
								this.legendaryResistances=legendaryResistances;
								this.innate=innate;
								this.casting=casting;
								this.passives=passives;
								this.orderedPassives=orderedPassives;
								this.otherNotes=otherNotes;
								this.multiattack=multiattack;
								this.attacks=attacks;
								this.otherActions=otherActions;
								this.reactions=reactions;
								this.legendaryActionCount=legendaryActionCount;
								this.legendaryActions=legendaryActions;
								this.lairActions=lairActions;
								this.sources=sources;
							}
	
							@Override public String getName() {return name;}
							@Override public Size getSize() {return size;}
							@Override public Type getType() {return type;}
							@Override public Subtype getSubtype() {return subtype;}
							@Override public boolean isShapechanger() {return isShapechanger;}
							@Override public List<Alignment> getAlignment() {return align;}
							@Override public double getCR() {return cr;}
							@Override public int getHP() {return hp;}
							@Override public int getAC() {return ac;}
							@Override public Map<Speeds, Integer> getSpeeds() {return speed;}
							@Override public Map<Stats, Integer> getStats() {return stats;}
							@Override public Map<Stats, Integer> getSaves() {return saves;}
							@Override public Map<Skills, Integer> getSkills() {return skills;}
							@Override public Map<DamageMultiplier, List<DamageType>> getMultipliers() {return damageMultipliers;}
							@Override public List<StatusCondition> getConditionImmunities() {return conditionImmunities;}
							@Override public Map<Senses, Integer> getSenses() {return senses;}
							@Override public List<Languages> getLanguages() {return languages;}
							@Override public List<Region> getRegions() {return regions;}
							@Override public int getLegendaryResistances() {return legendaryResistances;}
							@Override public Spellcasting getInnateCasting() {return innate;}
							@Override public Spellcasting getSpellcasting() {return casting;}
							@Override public Map<String, String> getPassives() {return passives;}
							@Override public List<String> orderedPassives() {return orderedPassives;}
							@Override public String otherNotes() {return otherNotes;}
							@Override public String getMultiattack() {return multiattack;}
							@Override public List<Attack> getAttacks() {return attacks;}
							@Override public List<Effect> getEffects() {return otherActions;}
							@Override public Map<String, String> getReactions() {return reactions;}
							@Override public int getLegendaryActionCount() {return legendaryActionCount;}
							@Override public List<LegendaryAction> getLegendaryActions() {return legendaryActions;}
							@Override public List<String> getLairActions() {return lairActions;}
							@Override public List<Source> getSource() {return sources;}
							
							@Override public String toString(){
								String builtString = getName()+"\n";
								builtString += size.toNiceString()+" "+type.toNiceString();
								if(getSubtype()!=null || isShapechanger()){
									builtString += " (";
									if(getSubtype()!=null){builtString += getSubtype().toNiceString();}
									if(getSubtype()!=null && isShapechanger()){builtString += ", ";}
									if(isShapechanger()){builtString += "shapechanger";}
									builtString += ")";
								}
								builtString += ", "+alignDescription()+"\n";
								builtString += "------------------------------------------------------------------------------------------\n";
								
								builtString += "Armour Class: "+getAC()+"\n";
								builtString += "Hit Points: "+getHP()+"\n";
	  							builtString += "Speed: "+getSpeeds().get(Speeds.WALK)+" ft";
	  							for(Speeds s:Speeds.values()){if(s!=Speeds.WALK && getSpeeds().containsKey(s) && getSpeeds().get(s)>0){
	  								builtString += ", "+s.toNiceString()+" "+getSpeeds().get(s)+" ft";
	  							}}
	  							builtString += "\n";
	  							builtString += "------------------------------------------------------------------------------------------\n";
	  								
	  							for(Stats s:Stats.values()){
	  								builtString += s.toString()+" "+getStats().get(s)+"(";
	  								if(Creature.scoreToMod(getStats().get(s))>=0){builtString+="+";}
	  								builtString += Creature.scoreToMod(getStats().get(s))+")\t";
	  							}
	  							builtString += "\n";
	  							builtString += "------------------------------------------------------------------------------------------\n";
	
	  							if(!getSaves().isEmpty()){
	  								builtString += "Saving Throws: ";
	  								boolean first = true;
	  								for(Stats s:Stats.values()){
	  									if(getSaves().containsKey(s)){
		  									if(!first){builtString += ", ";}
		  									builtString += s.toString()+" ";
		  									if(getSaves().get(s)>=0){builtString += "+";}
		  									builtString += getSaves().get(s);
		  									first = false;
	  									}
	  								}
	  								builtString += "\n";
	  							}
	  							if(!getSkills().isEmpty()){
	  								builtString += "Skills: ";
	  								boolean first = true;
	  								for(Skills s:Skills.values()){
	  									if(getSkills().containsKey(s)){
		  									if(!first){builtString += ", ";}
		  									builtString += s.toString()+" ";
		  									if(getSkills().get(s)>=0){builtString += "+";}
		  									builtString += getSkills().get(s);
		  									first = false;
	  									}
	  								}
	  								builtString += "\n";
	  							}
	  							if(!getVulnerabilities().isEmpty()){
	  								builtString += "Damage Vulnerabilities: ";
	  								boolean first = true;
	  								for(DamageType d:getVulnerabilities()){
	  									if(!first){builtString += ", ";}
	  									builtString += d.toNiceString();
	  									first = false;
	  								}
	  								builtString += "\n";
	  							}
	  							if(!getResistances().isEmpty()){
	  								builtString += "Damage Resistances: ";
	  								boolean first = true;
	  								for(DamageType d:getResistances()){
	  									if(!first){builtString += ", ";}
	  									builtString += d.toNiceString();
	  									first = false;
	  								}
	  								builtString += "\n";
	  							}
	  							if(!getImmunities().isEmpty()){
	  								builtString += "Damage Immunities: ";
	  								boolean first = true;
	  								for(DamageType d:getImmunities()){
	  									if(!first){builtString += ", ";}
	  									builtString += d.toNiceString();
	  									first = false;
	  								}
	  								builtString += "\n";
	  							}
	  							if(!getConditionImmunities().isEmpty()){
	  								builtString += "Condition Immunities: ";
	  								boolean first = true;
	  								for(StatusCondition s:getConditionImmunities()){
	  									if(!first){builtString += ", ";}
	  									builtString += s.toNiceString();
	  									first = false;
	  								}
	  								builtString += "\n";
	  							}
	  							if(!getSenses().isEmpty()){
	  								builtString += "Senses: ";
	  								boolean first = true;
	  								for(Senses s:Senses.values()){
	  									if(getSenses().containsKey(s)){
	  										if(!first){builtString += ", ";}
	  										builtString += s.toNiceString()+" "+ getSenses().get(s)+" ft";
	  										first = false;
	  									}
	  								}
	  								if(!first){builtString += ", ";}
	  								builtString += "Passive Perception "+(10+getSkillMod(Skills.PERCEPTION))+"\n";
	  							}
	  							if(!getLanguages().isEmpty()){
	  								builtString += "Languages: ";
	  								boolean first = true;
	  								for(Languages l:getLanguages()){
	  									if(!first){builtString += ", ";}
	  									builtString += l.toNiceString();
	  									first = false;
	  								}
	  								builtString += "\n";
	  							}
	  							builtString += "Challenge: ";
									if(getCR() == 0.125){builtString += "1/8";}
									else if(getCR() == 0.25){builtString += "1/4";}
									else if(getCR() == 0.5){builtString += "1/2";}
									else{builtString += ((int)getCR());}
	  							builtString += " ("+getXP()+" XP)\n";
	  							builtString += "------------------------------------------------------------------------------------------\n";
	  								
	  							if(getLegendaryResistances()>0){
	  								builtString += "Legendary Resistance ("+getLegendaryResistances()+"/Day). ";
	  								builtString += "If this creature fails a saving throw, it can choose to succeed instead.\n";
	  							}
	  							if(getInnateCasting()!=null){
	  								builtString += "Innate Spellcasting. ";
	  								builtString += "This creatures spellcasting ability is "+getInnateCasting().getAbility().toNiceString();
	  								if(getInnateCasting().getDC()!=null || getInnateCasting().getToHit()!=null){
	  									builtString += " (";
	  									if(getInnateCasting().getDC()!=null){
	  										builtString += "spell save DC "+getInnateCasting().getDC();
	  									}
	  									if(getInnateCasting().getDC()!=null && getInnateCasting().getToHit()!=null){
	  										builtString += ", ";
	  									}
	  									if(getInnateCasting().getToHit()!=null){
	  										if(getInnateCasting().getToHit()>=0){builtString += "+";}
	  										builtString += getInnateCasting().getToHit()+" to hit with spell attacks";
	  									}
	  									builtString += ")";
	  								}
	  								builtString += ". This creature can innately cast the following spells, ";
	  								builtString += "requiring no material components:\n";
	  								builtString += getInnateCasting().getSpellList().toString()+"\n\n";
	  							}
	  							if(getSpellcasting()!=null){
	  								builtString += "Spellcasting. ";
	  								builtString += "This creature is a ";
	  								if(getSpellcasting().getLevel()==1){builtString += "1st";}
	  								else if(getSpellcasting().getLevel()==1){builtString += "2nd";}
	  								else if(getSpellcasting().getLevel()==1){builtString += "3rd";}
	  								else{builtString += getSpellcasting().getLevel()+"th";}
	  								builtString += "-level spellcaster. ";
	  								builtString += "Its spellcasting ability is "+getSpellcasting().getAbility().toNiceString();
	  								if(getSpellcasting().getDC()!=null || getSpellcasting().getToHit()!=null){
	  									builtString += " (";
	  									if(getSpellcasting().getDC()!=null){
	  										builtString += "spell save DC "+getSpellcasting().getDC();
	  									}
	  									if(getSpellcasting().getDC()!=null && getSpellcasting().getToHit()!=null){
	  										builtString += ", ";
	  									}
	  									if(getSpellcasting().getToHit()!=null){
	  										if(getSpellcasting().getToHit()>=0){builtString += "+";}
	  										builtString += getSpellcasting().getToHit()+" to hit with spell attacks";
	  									}
	  									builtString += ")";
	  								}
	  								builtString += ". This creature has the following spells prepared:\n";
	  								builtString += getSpellcasting().getSpellList().toString()+"\n\n";
	  							}
	  							for(String p:orderedPassives()){
	  								builtString += p+". "+getPassives().get(p)+"\n";
	  							}
	  							if(otherNotes().length()>0){
	  								if(getLegendaryResistances()>0 || getInnateCasting()!=null || getSpellcasting()!= null || !getPassives().isEmpty()){
	  									builtString += "\n";}
	  								builtString += otherNotes()+"\n";
	  							}
	  							if(getLegendaryResistances()>0 || getInnateCasting()!=null || getSpellcasting()!= null
	  									|| !getPassives().isEmpty() || otherNotes().length()>0){
	  							builtString += "------------------------------------------------------------------------------------------\n";}
	
	  							if(!getActions().isEmpty()){
	  								builtString += "Actions\n";
									for(Object action:getActions()){
										builtString += action.toString()+"\n";}
	  							builtString += "------------------------------------------------------------------------------------------\n";}
	  								
	  							if(!getReactions().isEmpty()){
	  								builtString += "Reactions\n";
									for(String r:getReactions().keySet()){
										builtString += r+". "+getReactions().get(r)+"\n";}
	  							builtString += "------------------------------------------------------------------------------------------\n";}
	  								
	  							if(!getLegendaryActions().isEmpty()){
	  								builtString += "Legendary Actions\n";
	  								builtString += "This creature can take "+getLegendaryActionCount()+" legendary actions, choosing from the options below. ";
	  								builtString += "Only one legendary action option can be used at a time and only at the end of another creatures turn.";
	  								builtString += "This creature regains spent legendary actions at the start of it's turn.\n";
									for(LegendaryAction la:getLegendaryActions()){
										builtString += la.toString()+"\n";}
	  							builtString += "------------------------------------------------------------------------------------------\n";}
	  								
	  							if(!getLairActions().isEmpty()){
	  								builtString += "Lair Actions\n";
	  								builtString += "On initiative count 20 (losing initiative ties), ";
	  								builtString += "this creature takes a lair action to cause one of the following effects; ";
	  								builtString += "this creature can't use the same effect two rounds in a row:\n";
	  								for(String l:getLairActions()){builtString += l+"\n";}
	  							builtString += "------------------------------------------------------------------------------------------\n";}
	
	  							builtString += "Regions: ";
	  							boolean first = true;
	  							for(Region r:getRegions()){
	  								if(!first){builtString += ",";}
	  								builtString += r.toNiceString();
	  								first = false;
	  							}
	  							builtString += "\n";
	  							
	  							return builtString;
	  						}
						};
  						
																		//TODO - Label for start of data coalition
  						Size size = null;//Handle the fields that need extra work.
  						for(Size s:Size.values()){
  							if(((RadioButton)sizePicker.getSelectedToggle()).getText().equals(s.toNiceString())){size = s;}}
  						Type type = null;
  						for(Type t:Type.values()){
  							if(((RadioButton)typePicker.getSelectedToggle()).getText().equals(t.toNiceString())){type = t;}}
  						Subtype subtype = null;
  						if(type.hasSubtype()){
  							for(Subtype s:type.getSubtype(type)){
  								for(RadioButton rb:subtypePicker.get(type)){
  									if(rb.isSelected() && rb.getText().equals(s.toNiceString()+"\t")){subtype = s;}
  								}}}
  						
  						List<Alignment> align = new ArrayList<Alignment>();
  						for(Alignment a:Alignment.values()){
  							for(RadioButton rb:alignPicker){
  								if(rb.isSelected() && rb.getText().equals(a.toNiceString())){align.add(a);}}}

  						Map<Speeds,Integer> speed = new HashMap<Speeds,Integer>();
  						for(Speeds s:Speeds.values()){
  							if(speedPicker.get(s).getText().length()>0){speed.put(s, Integer.parseInt(speedPicker.get(s).getText()));}}
  						Map<Stats,Integer> stats = new HashMap<Stats,Integer>();
  						for(Stats s:Stats.values()){
  							if(statPicker.get(s).getText().length()>0){stats.put(s, Integer.parseInt(statPicker.get(s).getText()));}}
  						Map<Stats,Integer> saves = new HashMap<Stats,Integer>();
  						for(Stats s:Stats.values()){
  							if(savesPicker.get(s).isSelected()){saves.put(s, Integer.parseInt(savesSetter.get(s).getText()));}}
  						Map<Skills,Integer> skills = new HashMap<Skills,Integer>();
  						for(Skills s:Skills.values()){
  							if(skillPicker.get(s).isSelected()){skills.put(s, Integer.parseInt(skillSetter.get(s).getText()));}}
  						
  						Map<DamageMultiplier,List<DamageType>> damageMultipliers = new HashMap<DamageMultiplier,List<DamageType>>();
  						for(DamageMultiplier dm:DamageMultiplier.values()){
  	  						damageMultipliers.put(dm, new ArrayList<DamageType>());
  	  						for(DamageType dt:DamageType.values()){
  	  							if(resistancePicker.containsKey(dm) && resistancePicker.get(dm).get(dt).isSelected()){
  	  								damageMultipliers.get(dm).add(dt);
  	  							}
  	  						}}
  						List<StatusCondition> conditionImmunities = new ArrayList<StatusCondition>();
  						for(StatusCondition s:StatusCondition.values()){
  							if(conditionPicker.get(s).isSelected()){conditionImmunities.add(s);}}

  						Map<Senses,Integer> senses = new HashMap<Senses,Integer>();
  						for(Senses s:Senses.values()){
  							if(sensePicker.get(s).isSelected()){
  								senses.put(s, Integer.parseInt(senseSetter.get(s).getText()));}}
  						List<Languages> languages = new ArrayList<Languages>();
  						for(Languages l:Languages.values()){
  							if(languagePicker.get(l).isSelected()){languages.add(l);}}
  						List<Region> regions = new ArrayList<Region>();
  						for(Region r:Region.values()){
  							if(regionPicker.get(r).isSelected()){regions.add(r);}}
  						
  						Spellcasting innate = null;
	  						if(innateSpells!=null){
	  							Integer toHit = null, DC = null;
	  							if(innateMod.getText().length()>0){toHit = Integer.parseInt(innateMod.getText());}
	  							if(innateDC.getText().length()>0){DC = Integer.parseInt(innateDC.getText());}
	  							innate = new Spellcasting(innateSpells, innateAbility.getValue(), toHit, DC, innateLevel.getValue(), spells);}
  						Spellcasting casting = null;
	  						if(stdSpells!=null){
	  							Integer toHit = null, DC = null;
	  							if(castMod.getText().length()>0){toHit = Integer.parseInt(castMod.getText());}
	  							if(castDC.getText().length()>0){DC = Integer.parseInt(castDC.getText());}
	  							casting = new Spellcasting(stdSpells, castAbility.getValue(), toHit, DC, castLevel.getValue(), spells);}
  						Map<String,String> passives = new HashMap<String,String>();
  							List<String> orderedPassives = new ArrayList<String>();
  							for(RadioButton rb:orderedStandardPassives){
  								if(rb.isSelected()){
  									passives.put(rb.getText(),standardPassives.get(rb));
  									orderedPassives.add(rb.getText());}}
  							for(TextField p:passiveInputs.keySet()){
  								passives.put(p.getText(), passiveInputs.get(p).getText());
  								orderedPassives.add(p.getText());}
  						
  						List<Attack> attacks = new ArrayList<Attack>();
  							for(int i=0; i<attackNames.size();i++){
  								Integer longRange = null;
  								if(attackLongRange.get(i).getText().length()>0){
  									longRange = Integer.parseInt(attackLongRange.get(i).getText());}
  								attacks.add(new Attack(
  										attackNames.get(i).getText(),
  										Integer.parseInt(attackToHits.get(i).getText()),
  										Integer.parseInt(attackShortRange.get(i).getText()),
  										longRange,
  										attackDesc.get(i).getText()));}
  						List<Effect> otherActions = new ArrayList<Effect>();
  							for(int i=0; i<otherActNames.size();i++){
  								otherActions.add(new Effect(
  										otherActNames.get(i).getText(),
  										otherActLimits.get(i).getValue(),
  										otherActDesc.get(i).getText()));}
  						Map<String,String> reactions = new HashMap<String,String>();
  							for(TextField r:reactionInputs.keySet()){reactions.put(r.getText(), reactionInputs.get(r).getText());}
  							
  						List<LegendaryAction> legendaryActions = new ArrayList<LegendaryAction>();
  							for(int i=0; i<legendActNames.size();i++){
  								legendaryActions.add(new LegendaryAction(
  										legendActNames.get(i).getText(),
  										legendActCosts.get(i).getValue(),
  										legendActDesc.get(i).getText()));}
  						List<String> lairs = new ArrayList<String>();
  							for(TextArea a:lairActions){if(a.getText().length()>0){lairs.add(a.getText());}}
  						List<Source> sourcesList = new ArrayList<Source>();
  							sourcesList.add(sourceSelect.getValue());
  						
  						newcreature.constructor(//Create the new creature.
  								nameField.getText(), size, type, subtype, shapechangerToggle.isSelected(),
  								align,crPicker.getValue(),Integer.parseInt(hpPicker.getText()),Integer.parseInt(acPicker.getText()),
  								speed,stats,saves,skills,
  								damageMultipliers,conditionImmunities,
  								senses,languages,regions,
  								LegRes.getValue(),innate,casting,passives,orderedPassives,otherInfo.getText(),
  								multiattack.getText(),attacks,otherActions,reactions,
  								legendActCount.getValue(),legendaryActions,lairs,
  								sourcesList);
  						
  						System.out.println("Adding creature "+newcreature.getName());//Add the new creature to the list.
	  			      		Label label = new Label(" "+newcreature.getName());
	  			      		Tooltip toolTip = new Tooltip(newcreature.toString());
	  	  		      		toolTip.setWrapText(true);
	  	  		      		toolTip.setMaxWidth(450);
	  			      		label.setTooltip(toolTip);
	  			      		names.add(label);
	  			      		creatureList.add(label, 0, creatures.size()+1);
	  	  		      		String crText = "0";
	  	  		      		if(newcreature.getCR()>=1 || newcreature.getCR()==0){
	  	  		      			crText = ""+((int)newcreature.getCR());
	  	  		      		}else if(newcreature.getCR() == 0.5){
	  	  		      			crText = "1/2";
	  	  		      		}else if(newcreature.getCR() == 0.25){
	  	  		      			crText = "1/4";
	  	  		      		}else if(newcreature.getCR() == 0.125){
	  	  		      			crText = "1/8";
	  	  		      		}
	  	  		      		label = new Label(" "+crText);
	  			      		crs.add(label);
	  			      		creatureList.add(label, 1, creatures.size()+1);
	  						creatures.add(newcreature);
  						updateCreatureList();
  						nameFilter.getOnAction().handle(null);//Update filtered list with the new spell
  					}
  				});
  				curCreature.add(add, 3, layer);
  		      	
//  		    grid.add(curCreature, 1, 1);
  		      	sp = new ScrollPane();//allow scrolling the form for smaller screens
  		      	sp.setContent(curCreature);
  		      	grid.add(sp,1,2);

  		      	
  		      	
  		    //make the window
  		    secondaryStage.setScene(new Scene(grid, 1900, 1000));
  			secondaryStage.show();
  			return secondaryStage;
  		}

  		
  		
  		
  		

	private void updateCreatureList() {								//TODO - Label for query update
		for(int i=0; i<creatures.size(); i++) {
			boolean visible = query.contains(creatures.get(i));
			Label curName = names.get(i);
			Label curCR = crs.get(i);
			curName.setVisible(visible);//hide the entry in the list for excluded creatures
			curCR.setVisible(visible);
			if(!visible) {
				creatureList.getChildren().remove(curName);
				creatureList.getChildren().remove(curCR);
			}else if(!(creatureList.getChildren().contains(curName)||creatureList.getChildren().contains(curCR))){
				creatureList.getChildren().add(curName);
				creatureList.getChildren().add(curCR);
			}			
		}
	}
	
	private void updateSubtypeOptions(Type t) {//When a type is selected, display the appropriate subtypes.
		for(Type type:subtypePicker.keySet()){
			for(RadioButton rb:subtypePicker.get(type)) {
				boolean visible = type == t;
				rb.setVisible(visible);
				if(!visible) {
					rb.setSelected(false);
					subtypePanel.getChildren().remove(rb);
				}else{
					if(!subtypePanel.getChildren().contains(rb)){
						subtypePanel.getChildren().add(rb);
					}
					if(rb.getText().equals("None\t")){rb.setSelected(true);}
					else{rb.setSelected(false);}
				}			
			}
		}
	}
}
