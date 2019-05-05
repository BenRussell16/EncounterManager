package Encounters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Creatures.Creature;
import Creatures.Creature.Alignment;
import Creatures.Creature.DamageType;
import Creatures.Creature.Region;
import Creatures.Creature.Senses;
import Creatures.Creature.Size;
import Creatures.Creature.Speeds;
import Creatures.Creature.Type;
import Creatures.Creature.Type.Subtype;
import Resources.Source;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.collections.FXCollections;

@SuppressWarnings("restriction")
public class EncounterBuilder {
	
	private final List<Creature> creatures;
	private Map<Creature,Integer> built;
	private final Map<String,double[]> multipliers;
	private final List<String> multHeads = new ArrayList<String>();
	private int[][] difficulties = {
		{25,50,75,100},//1
		{50,100,150,200},//2
		{75,150,225,400},//3
		{125,250,375,500},//4
		{250,500,750,1100},//5
		{300,600,900,1400},//6
		{350,750,1100,1700},//7
		{450,900,1400,2100},//8
		{550,1100,1600,2400},//9
		{600,1200,1900,2800},//10
		{800,1600,2400,3600},//11
		{1000,2000,4000,4500},//12
		{1100,2200,3400,5100},//13
		{1250,2500,3800,5700},//14
		{1400,2800,4300,6400},//15
		{1600,3200,4800,7200},//16
		{2000,3900,5900,8800},//17
		{2100,4200,6300,9500},//18
		{2400,4900,7300,10900},//19
		{2800,5700,8500,12700}//20
	};
	private Stage secondaryStage = null;
	
	public EncounterBuilder(List<Creature> creatures) {
		this.creatures = creatures;
		built = new HashMap<Creature,Integer>();
		for(Creature c:creatures) {built.put(c, 0);}
		multipliers = new HashMap<String,double[]>();
		double[] x1 = {1,1,1,1,1,1};
		multipliers.put("x1", x1);
		multHeads.add("x1");
		double[] small = {1.5,2,2.5,3,4,5};
		multipliers.put("Small party", small);
		multHeads.add("Small party");
		double[] standard = {1,1.5,2,2.5,3,4};
		multipliers.put("Standard", standard);
		multHeads.add("Standard");
		double[] large = {0.5,1,1.5,2,2.5,3};
		multipliers.put("Large party", large);
		multHeads.add("Large party");
	}

	private int encounterValue() {
		int i=0;
		for(Creature c:creatures) {
			i+=built.get(c)*c.getXP();
		}
		return i;
	}
	private int postMultValue() {
		int value = encounterValue();
		if(encounterSize()==1) {value*=multipliers.get(multSelect.getSelectedToggle().toString().split("\'")[1])[0];}
		else if(encounterSize()==2) {value*=multipliers.get(multSelect.getSelectedToggle().toString().split("\'")[1])[1];}
		else if(encounterSize()<=6) {value*=multipliers.get(multSelect.getSelectedToggle().toString().split("\'")[1])[2];}
		else if(encounterSize()<=10) {value*=multipliers.get(multSelect.getSelectedToggle().toString().split("\'")[1])[3];}
		else if(encounterSize()<=14) {value*=multipliers.get(multSelect.getSelectedToggle().toString().split("\'")[1])[4];}
		else {value*=multipliers.get(multSelect.getSelectedToggle().toString().split("\'")[1])[5];}
		return value;
	}
	private int encounterSize() {
		int i=0;
		for(Creature c:creatures) {
			i+=built.get(c);
		}
		return i;
	}
	
	private GridPane creatureList;
	private GridPane curEncounter;
	private GridPane xpDisplay;
	private GridPane partyPanel;
	List<Creature> query;
	private Map<TextField,Label> inputs;
	private List<TextField> fields;
	private List<Label> fieldTags;
	private GridPane multiplierTable;
	private TextField[] customMults = new TextField[6];
	private ToggleGroup multSelect = new ToggleGroup();
	private List<Button> party;
	private ChoiceBox<Integer> levelPicker;
	
	public Stage makeDisplay() {
		if(secondaryStage != null) {secondaryStage.close();}
		secondaryStage = new Stage();//make the window
		secondaryStage.setTitle("Encounter builder");
		GridPane grid = new GridPane();
        grid.setHgap(10);
      	grid.setVgap(10);
      	
      		query = new ArrayList<Creature>();
      		query.addAll(creatures);
			List<Label> amounts = new ArrayList<Label>();
			fields = new ArrayList<TextField>();
			fieldTags = new ArrayList<Label>();
			inputs = new HashMap<TextField,Label>();
			
			GridPane topBar = new GridPane();
				Button export = new Button("Save encounter");
				export.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						saveEncounter();}});
				topBar.add(export, 0, 0);
				Button load = new Button("Load encounter");
				load.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent event) {
						loadEncounter();}});
				topBar.add(load, 1, 0);
				
			
				Label label = new Label("\t\t");topBar.add(label, 2, 0);
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
  									if(subtypeFilter.getValue()=="Shapechanger"){
  										if(!c.isShapechanger()){toRemove.add(c);}
  									}
  									else if(c.getSubtype()==null || !c.getSubtype().toNiceString().equals(subtypeFilter.getValue())) {
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
					nameFilter.setOnAction(filterQuery);			topBar.add(nameFilter, 3, 0);
					crFilter.setOnAction(filterQuery);				topBar.add(crFilter, 4, 0);
					upperCRFilter.setOnAction(filterQuery);			topBar.add(upperCRFilter, 5, 0);
					label = new Label("\t");						topBar.add(label, 6, 0);
  					/*typeFilter.setOnAction(filterQuery);*/		topBar.add(typeFilter, 7, 0);
					subtypeFilter.setOnAction(filterQuery);			topBar.add(subtypeFilter, 8, 0);
					label = new Label("\t");						topBar.add(label, 9, 0);
					legResfilter.setOnAction(filterQuery);			topBar.add(legResfilter, 10, 0);
					legActfilter.setOnAction(filterQuery);			topBar.add(legActfilter, 11, 0);
					lairfilter.setOnAction(filterQuery);			topBar.add(lairfilter, 12, 0);
					castfilter.setOnAction(filterQuery);			topBar.add(castfilter, 13, 0);
					//Start the 2nd row
					GridPane secondBar = new GridPane();
					label = new Label("\t\t\t\t\t\t\t\t\t\t");		secondBar.add(label, 0, 0);
					sizefilter.setOnAction(filterQuery);			secondBar.add(sizefilter, 1, 0);
					speedFilter.setOnAction(filterQuery);			secondBar.add(speedFilter, 2, 0);
					multiplierFilter.setOnAction(filterQuery);		secondBar.add(multiplierFilter, 3, 0);
					alignFilter.setOnAction(filterQuery);			secondBar.add(alignFilter, 4, 0);
					senseFilter.setOnAction(filterQuery);			secondBar.add(senseFilter, 5, 0);
					regionFilter.setOnAction(filterQuery);			secondBar.add(regionFilter, 6, 0);
					label = new Label("\t");						secondBar.add(label, 7, 0);
					sourceFilter.setOnAction(filterQuery);			secondBar.add(sourceFilter, 8, 0);

  					typeFilter.setOnAction(new EventHandler<ActionEvent>() {
  						//Set subclasses options based on selected class
  						@Override public void handle(ActionEvent event) {
  							if(typeFilter.getValue() == null || !typeFilter.getValue().hasSubtype()){
  								subtypeFilter.getItems().remove(2, subtypeFilter.getItems().size());
  								subtypeFilter.setValue(null);
  							}else{
  								subtypeFilter.getItems().remove(2, subtypeFilter.getItems().size());
  								for(Subtype s:typeFilter.getValue().getSubtype(typeFilter.getValue())){
  									subtypeFilter.getItems().add(s.toNiceString());}
  								subtypeFilter.setValue(null);
  							}
  							subtypeFilter.getOnAction().handle(event);//Execute the filter method.
  						}});
					
				grid.add(topBar, 0, 0, 3, 1);
				grid.add(secondBar, 0, 1, 3, 1);
			
				
				
				
				
				
			
			curEncounter = new GridPane();//create the list of labels used for presenting current amounts
			curEncounter.setHgap(10);
			label = new Label("Selections\t");
			curEncounter.add(label, 0, 0);
			for(int i=0; i<creatures.size(); i++) {
				label = new Label();
				amounts.add(label);
				curEncounter.add(label, 0, i+1);
			}
			grid.add(curEncounter,1,2);
			
			
			
			
			
			
      		ScrollPane sp = new ScrollPane();//allow scrolling down the creature list
			creatureList = new GridPane();
			creatureList.setHgap(10);
	  		label = new Label(" Creature");//make the list of creatures and input fields
	  		creatureList.add(label, 0, 0);
	  		label = new Label("Amount");
	  		creatureList.add(label, 1, 0);
	      	for(int i=0; i<creatures.size(); i++) {
	      		label = new Label(" "+creatures.get(i).getName());
	      		Tooltip toolTip = new Tooltip(creatures.get(i).toString());
		      	toolTip.setWrapText(true);
		      	toolTip.setMaxWidth(450);
	      		label.setTooltip(toolTip);
	      		fieldTags.add(label);
	      		creatureList.add(label, 0, i+1);
	      		TextField textField = new TextField();
	      		fields.add(textField);
	      		inputs.put(textField,amounts.get(i));
	      		textField.setText("0");
	      	    textField.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
					@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						if (!newValue.matches("\\d*")) {//remove non ints
	      	                textField.setText(newValue.replaceAll("[^\\d]", ""));
	      	            }
						if(newValue.isEmpty()) {textField.setText("0");}//ensure not empty
						textField.setText(""+Integer.parseInt(textField.getText()));//remove leading 0's
					}
	      	    });
	      		textField.setOnAction(new EventHandler<ActionEvent>() {//when text is confirmed update the current lists
					@Override public void handle(ActionEvent event) {
						Label label = inputs.get(textField);
						Creature creature = null;
						for(int i=0; i<amounts.size();i++) {
							if(amounts.get(i)==label) {creature = creatures.get(i);}
						}
						if(Integer.parseInt(textField.getText())>0) {
							label.setText(textField.getText()+"x "+creature.getName());
						}else {label.setText("");}
						built.put(creature, Integer.parseInt(textField.getText()));
						updateCurBuilt();
						updateXPPanel();
					}
	      		});
	      		creatureList.add(textField, 1, i+1);
	      	}
	      	sp.setContent(creatureList);
	      	updateCurBuilt();
	      	grid.add(sp,0,2);
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	

	      	
	      	xpDisplay = new GridPane();
	      	xpDisplay.setHgap(10);
	      	xpDisplay.setVgap(10);
			label = new Label("Encounter size: "+encounterSize());
			xpDisplay.add(label, 0, 0);
			label = new Label("Raw XP value: "+encounterValue());
			xpDisplay.add(label, 0, 1);
			label = new Label();	xpDisplay.add(label, 0, 2);
				multiplierTable = new GridPane();
				multiplierTable.setHgap(10);
				label = new Label("Multiplers");
				multiplierTable.add(label, 0, 0);

				label = new Label("Encounter size");
				multiplierTable.add(label, 0, 1);
				String[] sizes = {"1","2","3-6","7-10","11-14","15+"};
				for(int i=0;i<sizes.length;i++) {
					label = new Label(sizes[i]);
					multiplierTable.add(label, 0, i+2);
				}
				for(int i=0;i<multHeads.size();i++) {
					RadioButton rb = new RadioButton(multHeads.get(i));
					if("Standard".equals(rb.getText())){rb.setSelected(true);}
					rb.setOnAction(new EventHandler<ActionEvent>() {//update XPpanel when changed
						public void handle(ActionEvent event) {updateXPPanel();}});
					multiplierTable.add(rb, i+1, 1);
					rb.setToggleGroup(multSelect);
					for(int j=0; j<multipliers.get(multHeads.get(i)).length;j++) {
						label = new Label(""+multipliers.get(multHeads.get(i))[j]);
						multiplierTable.add(label, i+1, j+2);
					}
				}
				RadioButton rb = new RadioButton("Custom multiplier");
				rb.setOnAction(new EventHandler<ActionEvent>() {//update XPpanel when changed
					public void handle(ActionEvent event) {updateXPPanel();}});
				multiplierTable.add(rb, multipliers.size()+1, 1);
				rb.setToggleGroup(multSelect);
				for(int j=0; j<6;j++) {
					TextField cusValue = new TextField();
					customMults [j] = cusValue;
					cusValue.setText("0");
					cusValue.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
							if (!newValue.matches("\\d*.\\d")) {//remove non ints
								cusValue.setText(newValue.replaceAll("[^.\\d]", ""));
		      	            }
							if(newValue.isEmpty()) {cusValue.setText("0");}//ensure not empty
							cusValue.setText(""+Double.parseDouble(cusValue.getText()));//remove leading 0's
							cusValue.getOnAction().handle(null);//Update values if you forget to hit enter.
						}
		      	    });
					cusValue.setOnAction(new EventHandler<ActionEvent>() {//when text is confirmed update the xp panel
						@Override
						public void handle(ActionEvent event) {
								double[] custom = multipliers.get("Custom multiplier");
								for(int i=0; i<custom.length; i++) {custom[i] = Double.parseDouble(customMults[i].getText());}
								updateXPPanel();
							
						}
		      		});
					multiplierTable.add(cusValue, multipliers.size()+1, j+2);
				}
				double[] custom ={0,0,0,0,0,0};
				multipliers.put("Custom multiplier", custom);
				xpDisplay.add(multiplierTable, 0, 3);
				
				label = new Label();	xpDisplay.add(label, 0, 4);
				label = new Label("Modified XP: 0");
				xpDisplay.add(label, 0, 5);
				label = new Label();	xpDisplay.add(label, 0, 6);
				
				partyPanel = new GridPane();									//TODO - Label for party stuff constructor
				partyPanel.setHgap(10);
				partyPanel.setVgap(10);
				party = new ArrayList<Button>();
				
				label = new Label("New player level:");
				partyPanel.add(label, 0, 0);
				levelPicker = new ChoiceBox<Integer>(FXCollections.observableArrayList(
					    1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)
					);
				levelPicker.setValue(1);
				partyPanel.add(levelPicker, 1, 0);
				Button partySpawner = new Button("Add party member");
				partySpawner.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Button btn = new Button();
						btn.setText("Level "+levelPicker.getValue());
						btn.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								party.remove(btn);
								updatePartyPanel();
							}
						});
						party.add(btn);
						updatePartyPanel();
					}
				});
				partyPanel.add(partySpawner, 2, 0);
				xpDisplay.add(partyPanel, 0, 7);
	      	grid.add(xpDisplay, 2, 2);

	    secondaryStage.setScene(new Scene(grid, 1200, 600));
		secondaryStage.show();
		return secondaryStage;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	private void updateCreatureList() {
		for(int i=0; i<creatures.size(); i++) {
			boolean visible = query.contains(creatures.get(i));
			TextField curField = fields.get(i);
			curField.setVisible(visible);//hide the text field for excluded creatures
			Label curLabel = fieldTags.get(i);
			curLabel.setVisible(visible);//hide its label
			if(!visible) {
				creatureList.getChildren().remove(curField);
				creatureList.getChildren().remove(curLabel);
			}else if(!(creatureList.getChildren().contains(curField)||creatureList.getChildren().contains(curLabel))){
				creatureList.getChildren().add(curField);
				creatureList.getChildren().add(curLabel);
			}
			
		}
	}
	private void updateCurBuilt() {
		List<Node> old = new ArrayList<Node>();
		old.addAll(curEncounter.getChildren());
		curEncounter.getChildren().removeAll(old);
		
		Label label = new Label("Selections\t");
		curEncounter.add(label, 0, 0);
		int j=1;
		for(TextField t:fields) {
			if(Integer.parseInt(t.getText())>0) {
				curEncounter.add(inputs.get(t), 0, j);
				Button remover = new Button("X");
				remover.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						t.setText(""+(Integer.parseInt(t.getText())-1));
						int i=fields.indexOf(t);
						built.put(creatures.get(i), Integer.parseInt(t.getText()));
						inputs.get(t).setText(built.get(creatures.get(i))+"x "+creatures.get(i).getName());
						updateCurBuilt();
						updateXPPanel();
					}
				});
				curEncounter.add(remover, 1, j++);
			}
		}
	}
	private void updateXPPanel() {
		List<Node> old = new ArrayList<Node>();
		old.addAll(xpDisplay.getChildren());
		xpDisplay.getChildren().removeAll(old);

		Label label = new Label("Encounter size: "+encounterSize());
		xpDisplay.add(label, 0, 0);
		label = new Label("Raw XP value: "+encounterValue());
		xpDisplay.add(label, 0, 1);
		label = new Label();	xpDisplay.add(label, 0, 2);
		xpDisplay.add(multiplierTable, 0, 3);
		
		label = new Label();	xpDisplay.add(label, 0, 4);
		label = new Label("Modified XP: "+postMultValue());
		xpDisplay.add(label, 0, 5);
		label = new Label();	xpDisplay.add(label, 0, 6);
		xpDisplay.add(partyPanel,0,7);
		updatePartyPanel();
	}
	private void updatePartyPanel() {
		List<Node> old = new ArrayList<Node>();
		old.addAll(partyPanel.getChildren());
		partyPanel.getChildren().removeAll(old);
		
		Label label = new Label("New player level:");
		partyPanel.add(label, 0, 0);
		partyPanel.add(levelPicker, 1, 0);
		Button partySpawner = new Button();
		partySpawner.setText("Add party member");
		partySpawner.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button btn = new Button();
				btn.setText("Level "+levelPicker.getValue());
				btn.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						party.remove(btn);
						updatePartyPanel();
					}
				});
				party.add(btn);
				updatePartyPanel();
			}
		});
		partyPanel.add(partySpawner, 2, 0);
		
		int rowSize = 6;
		GridPane partyDisplay = new GridPane();
		for(int i=0; i<party.size(); i++) {
			partyDisplay.add(party.get(i), i%rowSize, i/rowSize);
		}
		partyPanel.add(partyDisplay, 0, 1, 3, 1);
		
		Map<Integer,Integer> levels = new HashMap<Integer,Integer>();
		for(Button btn:party) {
			int lvl = Integer.parseInt(btn.getText().split(" ")[1]);
			if(!levels.containsKey(lvl)) {levels.put(lvl, 0);}
			levels.put(lvl, levels.get(lvl)+1);
		}
		if(!party.isEmpty()) {
			GridPane tables = new GridPane();
			label = new Label("Level\t\t");			tables.add(label, 0, 0);
			label = new Label("Easy\t\t");			tables.add(label, 1, 0);
			label = new Label("Medium\t\t");		tables.add(label, 2, 0);
			label = new Label("Hard\t\t");			tables.add(label, 3, 0);
			label = new Label("Deadly\t\t");		tables.add(label, 4, 0);
			for(int i=1; i<=20; i++) {
				if(levels.containsKey(i)) {
					label = new Label(i+"");							tables.add(label, 0, i);
					for(int j=0; j<4; j++) {
						label = new Label(difficulties[i-1][j]+"");		tables.add(label, j+1, i);
					}
				}
			}
			int[] thresholds = new int[4];
			for(int i:levels.keySet()) {
				for(int j=0; j<4; j++) {thresholds[j] += levels.get(i) * difficulties[i-1][j];}
			}
			label = new Label("Total");			tables.add(label, 0, 21);
			for(int j=0; j<4; j++) {
				label = new Label(thresholds[j]+"");		tables.add(label, j+1, 21);
			}

			int[] differences = {thresholds[0]-postMultValue(),
					thresholds[1]-postMultValue(),
					thresholds[2]-postMultValue(),
					thresholds[3]-postMultValue()};
			int lowest=0;
			for(int i=0;i<4;i++) {
				if(differences[i]<0) {differences[i]*=-1;}
				if(differences[i]<differences[lowest]) {lowest=i;}
			}
			String difficultyLabel;
			if(lowest == 0) {difficultyLabel="Easy";}
			else if(lowest == 1) {difficultyLabel="Medium";}
			else if(lowest == 2) {difficultyLabel="Hard";}
			else {difficultyLabel="Deadly";}
			label = new Label(postMultValue()+"XP is "+difficultyLabel);		tables.add(label, 0, 22, 4, 23);
			partyPanel.add(tables, 0, 2, 3, 2);
			
			
	      	GridPane randomButtons = new GridPane();							//TODO - Label for random encounter generation.
	      	randomButtons.setHgap(10);
	      	randomButtons.setVgap(10);
	      		randomButtons.add(new Label("Generate Random Encounter."), 0, 0);
	      		for(int i=0; i<4; i++){
	      			Button gen = new Button();
	      			switch (i) {
	      				case 0:
	      					gen.setText("Easy");
	      					break;
	      				case 1:
	      					gen.setText("Medium");
	      					break;
	      				case 2:
	      					gen.setText("Hard");
	      					break;
	      				case 3:
	      					gen.setText("Deadly");
	      					break;
					}
	      			randomButtons.add(gen, i+1, 0);
	      			final int j = i;
	      			gen.setOnAction(new EventHandler<ActionEvent>() {
						@Override public void handle(ActionEvent event) {
							// TODO Auto-generated method stub
							System.out.println(thresholds[j]);
						}
					});
	      		}
	      	partyPanel.add(randomButtons, 0, 5, 3, 1);
		}
	}
	
	
	
	
	
	public void saveEncounter(){							//TODO - Label for encounter saving
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("Resources/Encounters/"));
		File file = fileChooser.showSaveDialog(secondaryStage);
		
		if(file!=null){
			System.out.println("Saving encounter for "+file.getName());
			//Build the new XML string
			String xml = "<encounter>\n";
			xml+="\t<name>"+file.getName()+"</name>\n";
			for(Creature c:built.keySet()){
				if(built.get(c)>0){
					xml+="\t<creature>"+built.get(c)+","+c.getName()+"</creature>\n";
				}
			}
			xml+="\t<mult>";
			xml+=((RadioButton)multSelect.getSelectedToggle()).getText();
			if(((RadioButton)multSelect.getSelectedToggle()).getText().equals("Custom multiplier")){
				for(double d:multipliers.get("Custom multiplier")){xml+=","+d;}//If custom is used, store it.
			}
			xml+="</mult>\n";
			if(!party.isEmpty()){
				xml+="\t<party>";
				boolean first = true;
				for(Button b:party){
					Integer level = Integer.parseInt(b.getText().substring(6));
					if(!first){xml+=",";}
					xml+=level;
					first=false;
				}
				xml+="</party>\n";
			}
			xml+="</encounter>\n";
			//Save the new encounter.
			try (PrintWriter out = new PrintWriter(file)) {
			    out.print(xml);
			    System.out.println("Encounter saving complete");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else{System.out.println("Encounter saving failed");}
	}

	public void loadEncounter(){							//TODO - Label for encounter loading
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("Resources/Encounters/"));
		File file = fileChooser.showOpenDialog(secondaryStage);

		if(file!=null){
			Scanner scan;
			System.out.println("Loading encounter for "+file.getName());
			try {
				scan = new Scanner(file);
				scan.useDelimiter("<|>|,");
				while(scan.hasNext()) {
					if(scan.hasNext("encounter")) {
						//Setting fields to store encounter values.
						built = new HashMap<Creature,Integer>();
						for(Creature c:creatures){built.put(c, 0);}
						party = new ArrayList<Button>();
						
						//Parse the encounter.
						while(!scan.hasNext("/encounter")) {
							if(scan.hasNext("name")){//Skip the name, it's there for human readers
								while(!scan.hasNext("/name")){scan.next();}
							}else if(scan.hasNext("creature")){
								scan.next();
								while(!scan.hasNext("/creature")){//Parse each creature entry
									int count = scan.nextInt();
									String name = scan.next();
									for(Creature c:creatures){
										if(c.getName().equals(name)){
											built.put(c, count);
										}
									}
								}
							}else if(scan.hasNext("mult")){
								scan.next();
								while(!scan.hasNext("/mult")){//Parse the multiplier
									String mult = scan.next();
									for(Toggle rb:multSelect.getToggles()){//Activate the mult.
										if(((RadioButton)rb).getText().equals(mult)){
											multSelect.selectToggle(rb);
										}
									}
									if(((RadioButton)multSelect.getSelectedToggle()).getText().equals("Custom multiplier")){
										double[] custom = new double[6];//Set values if custom mult is stored.
										for(int i=0; i<6; i++){
											custom[i]=scan.nextDouble();
											customMults[i].setText(""+custom[i]);
										}
										multipliers.put("Custom multiplier", custom);
									}
								}
							}else if(scan.hasNext("party")){
								scan.next();
								while(!scan.hasNext("/party")){//Parse the party
									Button p = new Button("Level "+scan.next());
									p.setOnAction(new EventHandler<ActionEvent>() {
										@Override public void handle(ActionEvent event) {
											party.remove(p);
											updatePartyPanel();}});
									party.add(p);
								}
							}else{scan.next();}
						}
					}
					scan.next();
				}//where parsing ends
				scan.close();
				System.out.println("Encounter loading complete");
				//Apply to the window
				for(int i=0; i<creatures.size(); i++) {
					TextField curField = fields.get(i);
					curField.setText(""+built.get(creatures.get(i)));
					curField.getOnAction().handle(null);
				}
				updateCreatureList();
				updateCurBuilt();
				updatePartyPanel();
				updateXPPanel();
			} catch (FileNotFoundException e) {
				System.out.println("Encounter loading failed");
				e.printStackTrace();
			}
		}else{System.out.println("No file selected");}
	}

}
