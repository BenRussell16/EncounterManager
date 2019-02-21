package EncounterManager.src.Creatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import EncounterManager.Resources.Source;
import EncounterManager.src.Creatures.Creature.Alignment;
import EncounterManager.src.Creatures.Creature.Senses;
import EncounterManager.src.Creatures.Creature.Size;
import EncounterManager.src.Creatures.Creature.Speeds;
import EncounterManager.src.Creatures.Creature.Type;
import EncounterManager.src.Spells.Spell;
import EncounterManager.src.Spells.Spell.Classes;
import EncounterManager.src.Spells.Spell.School;
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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

@SuppressWarnings("restriction")
public class CreatureBuilder {

	private final List<Creature> creatures;
	private Stage secondaryStage = null;
	
	public CreatureBuilder(List<Creature> creatures) {
		this.creatures = creatures;
	}
	
	private List<Creature> query;
	private GridPane creatureList;
		private List<Label> names;
		private List<Label> crs;
	private GridPane curCreature;
	//Good for encounter building.
  		private TextField nameField;
  		private ChoiceBox<Double> crPicker;
  		private ToggleGroup sizePicker = new ToggleGroup();
  		private ToggleGroup typePicker = new ToggleGroup();
  			private ToggleGroup subTypePicker = new ToggleGroup();
  	  	private ToggleGroup alignPicker = new ToggleGroup();
  		//private RadioButton lg, ng, cg, ln, tn, cn, le, ne, ce, un,
  		//						nonG, nonE, nonL, nonC, g, e, l, c;
  		private ChoiceBox<Source> sourceSelect;
  	//Nice to have
  		//AC
  		//HP
  		//Stats
  		//Saves
  		//Skills
  		//Speeds
  		//Senses
  		//Damage multipliers.
  		//	Condition immunities
  		//Languages
  		//Spells
  		//Passive effects, regen, actions...
  			//Legendary actions, legendary resistances, lair actions
  		

  		public Stage makeDisplay() {
  			if(secondaryStage != null) {secondaryStage.close();}
  			secondaryStage = new Stage();//make the window
  			secondaryStage.setTitle("Spells");
  			GridPane grid = new GridPane();
  	        grid.setHgap(10);
  	      	grid.setVgap(10);
  	      	
  	      		query = new ArrayList<Creature>();
  	      		query.addAll(creatures);
  				names = new ArrayList<Label>();
  				crs = new ArrayList<Label>();
  				
  				GridPane topBar = new GridPane();
  				
  					Button save = new Button("Save changes");
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
  							//TODO - set up new XML
  							//for(int i=0; i<creatures.size(); i++){xml+=creatures.get(i).toXML();}
  							//Save the new creatures.
  							try (PrintWriter out = new PrintWriter(new File("EncounterManager/Resources/CreatureList"))) {
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

  					//TODO - allow selection of multiple
  					ChoiceBox<Double> crFilter = new ChoiceBox<Double>(FXCollections.observableArrayList(
  							//null,0,0.125,0.25,0.5,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30
  							//TODO fill this list
  							));
  					crFilter.setValue(null);
  					
  					ChoiceBox<Type> typeFilter = new ChoiceBox<Type>(FXCollections.observableArrayList());
  					typeFilter.getItems().add(null);
  					typeFilter.getItems().addAll(Type.values());
  					typeFilter.setValue(null);
  					typeFilter.setConverter(new StringConverter<Type>(){
  						@Override public Type fromString(String arg0) {// TODO Auto-generated method stub
  							return null;}
  						@Override public String toString(Type type) {
  							if(type != null){
  								return type.toNiceString();}
  							return null;
  						}});
  					
  					ChoiceBox<Size> sizefilter = new ChoiceBox<Size>(FXCollections.observableArrayList());
  					sizefilter.getItems().add(null);
  					sizefilter.getItems().addAll(Size.values());
  					sizefilter.setValue(null);
  					sizefilter.setConverter(new StringConverter<Size>(){
  						@Override public Size fromString(String arg0) {
  							return null;}
  						@Override public String toString(Size size) {
  							if(size != null){
  								return size.toNiceString();}
  							return null;
  						}});
  					
  					ChoiceBox<Alignment> alignFilter = new ChoiceBox<Alignment>(FXCollections.observableArrayList());
  					alignFilter.getItems().add(null);
  					alignFilter.getItems().addAll(Alignment.values());
  					alignFilter.setValue(null);
  					alignFilter.setConverter(new StringConverter<Alignment>(){
  						@Override public Alignment fromString(String arg0) {// TODO Auto-generated method stub
  							return null;}
  						@Override public String toString(Alignment align) {
  							if(align != null){
  								return align.toNiceString();}
  							return null;
  						}});
  					
  					ChoiceBox<Source> sourceFilter = new ChoiceBox<Source>(FXCollections.observableArrayList());
  					sourceFilter.getItems().add(null);
  					sourceFilter.getItems().addAll(Source.values());
  					sourceFilter.setValue(null);
  					sourceFilter.setConverter(new StringConverter<Source>(){
  						@Override public Source fromString(String arg0) {// TODO Auto-generated method stub
  							return null;}
  						@Override public String toString(Source source) {
  							if(source != null){
  								return source.toNiceString();}
  							return null;
  						}});
  					
  					//Define the filtering action
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

  							if(crFilter.getValue()!=null) {//cr filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!(c.getCR()==crFilter.getValue())) {
  										toRemove.add(c);
  									}
  								}
  								for(Creature c:toRemove) {query.remove(c);}
  							}
  							if(typeFilter.getValue()!=null) {//type filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!c.isType(typeFilter.getValue())) {
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
  							if(alignFilter.getValue()!=null) {//alignment filter
  								List<Creature> toRemove = new ArrayList<Creature>();
  								for(Creature c:query) {
  									if(!(c.getAlign()==alignFilter.getValue())) {
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
  					nameFilter.setOnAction(filterQuery);
  					topBar.add(nameFilter, 2, 0);
  					crFilter.setOnAction(filterQuery);
  					topBar.add(crFilter, 3, 0);
  					typeFilter.setOnAction(filterQuery);
  					topBar.add(typeFilter, 4, 0);
  					sizefilter.setOnAction(filterQuery);
  					topBar.add(sizefilter, 5, 0);
  					alignFilter.setOnAction(filterQuery);
  					topBar.add(alignFilter, 6, 0);  					
  					sourceFilter.setOnAction(filterQuery);
  					topBar.add(sourceFilter, 13, 0);
  					
  				grid.add(topBar, 0, 0, 2, 1);
  				

  				
  				
  				//1st pane for creature list
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
  		      		label.setTooltip(toolTip);
  		      		names.add(label);
  		      		creatureList.add(label, 0, i+1);
  		      		label = new Label(" "+creatures.get(i).getCR());
  		      		crs.add(label);
  		      		creatureList.add(label, 1, i+1);
  		      	}
  		      	sp.setContent(creatureList);
  		      	grid.add(sp,0,1);

  		      	
  		      	
  		      	//2nd pane for spell addition
  		      	curCreature = new GridPane();
  		      	curCreature.setHgap(10);
  		      	curCreature.setVgap(10);

  		      	label = new Label(" Name");
  		      	curCreature.add(label, 0, 0);
  		      	nameField = new TextField();
  		      	curCreature.add(nameField, 1, 0);

  		      	label = new Label(" CR");
  		      	curCreature.add(label, 0, 1);
  		    	crPicker = new ChoiceBox<Double>(FXCollections.observableArrayList(
						//null,0,0.125,0.25,0.5,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30
						//TODO fill this list
						));
  		    	crPicker.setValue(null);
  		    	curCreature.add(crPicker, 1, 1);
  		      	
  	  		    label = new Label(" Size");
  	  		    curCreature.add(label, 0, 2);
  	  		    GridPane sizePanel = new GridPane();
  	  		    sizePanel.setHgap(5);
  	  		    int i = 0;
  	  		    for(Size s:Size.values()){
  	  		    	RadioButton rb = new RadioButton(s.toNiceString());
  	  		    	rb.setToggleGroup(sizePicker);
  	  		    	sizePanel.add(rb, i, 0);
  	  		    	i++;
  	  		    }
  	  		    curCreature.add(sizePanel, 1, 2);
  		    	
  		      	label = new Label(" Type");
  		      	curCreature.add(label, 0, 3);
  		      	GridPane typePanel = new GridPane();
  		      	typePanel.setHgap(5);
  		      	i = 0;
  		      	for(Type t:Type.values()){
  		      		RadioButton rb = new RadioButton(t.toNiceString());
  		      		rb.setToggleGroup(typePicker);
  		      		typePanel.add(rb, i%4, i/4);
  	  		    	i++;
  		      	}
  		      	curCreature.add(typePanel, 1, 3);

  	  	  		label = new Label(" Alignment");
  	  	  		curCreature.add(label, 0, 4);
  	  	  		GridPane alignPanel = new GridPane();
  	  	  		alignPanel.setHgap(5);
  	  	  		i = 0;
  	  	  		for(Alignment a:Alignment.values()){
  	  	  			RadioButton rb = new RadioButton(a.toNiceString());
  	  	  			rb.setToggleGroup(alignPicker);
  	  	  			alignPanel.add(rb, i%3, i/3);
  	  		    	i++;
  	  	  		}
  	  	  		curCreature.add(alignPanel, 1, 4);
  		    	
  		      	label = new Label(" Source");
  		      	curCreature.add(label, 0, 5);
  				sourceSelect = new ChoiceBox<Source>(FXCollections.observableArrayList());
  				sourceSelect.getItems().add(null);
  				sourceSelect.getItems().addAll(Source.values());
  				sourceSelect.setValue(null);
  				sourceSelect.setConverter(new StringConverter<Source>(){
  					@Override public Source fromString(String arg0) {// TODO Auto-generated method stub
  						return null;}
  					@Override public String toString(Source source) {
  						if(source != null){
  							return source.toNiceString();}
  						return null;
  					}});
  				curCreature.add(sourceSelect, 1, 5);
  		      	
  				Button add = new Button("Add creature");
  				add.setOnAction(new EventHandler<ActionEvent>() {
  					@Override
  					public void handle(ActionEvent event) {
//  						Spell newSpell = new Spell() {
//  							String name="";
//  							int level=0;
//  							School school = null;
//  							List<Classes> classes = null;
//  							List<Source> sources = null;
//  							
//  							public void constructor(String name, int level, School school, List<Classes> classes, List<Source> sources){
//  								this.name = name;
//  								this.level = level;
//  								this.school = school;
//  								this.classes = classes;
//  								this.sources = sources;
//  							}
//
//  							@Override public String getName() {return name;}
//  							@Override public int getLevel() {return level;}
//  							@Override public School getSchool() {return school;}
//  							@Override public List<Classes> getClasses() {return classes;}
//  							@Override public boolean fromClass(Classes curClass) {return classes.contains(curClass);}
//  							@Override public boolean fromSource(Source source) {return sources.contains(source);}
//  							@Override public String toString() {return name;}
//  						};
//  						
//  						List<Classes> classList = new ArrayList<Classes>();
//  							if(c1.isSelected()){classList.add(Classes.BARD);}
//  							if(c2.isSelected()){classList.add(Classes.CLERIC);}
//  							if(c3.isSelected()){classList.add(Classes.DRUID);}
//  							if(c4.isSelected()){classList.add(Classes.PALADIN);}
//  							if(c5.isSelected()){classList.add(Classes.RANGER);}
//  							if(c6.isSelected()){classList.add(Classes.SORCERER);}
//  							if(c7.isSelected()){classList.add(Classes.WARLOCK);}
//  							if(c8.isSelected()){classList.add(Classes.WIZARD);}
//  						List<Source> sourcesList = new ArrayList<Source>();
//  							sourcesList.add(sourceSelect.getValue());
//  						newSpell.constructor(nameField.getText(), levelPicker.getValue(), schoolSelect.getValue(),
//  								classList, sourcesList);
//  						
//  						System.out.println("Adding creature "+newSpell.getName());
//  			      			Label label = new Label(" "+newSpell.getName());
//  			      			Tooltip toolTip = new Tooltip(newSpell.toString());
//  			      			label.setTooltip(toolTip);
//  			      			names.add(label);
//  			      			spellList.add(label, 0, spells.size()+1);
//  			      			label = new Label(" "+newSpell.getLevel());
//  			      			levels.add(label);
//  			      			spellList.add(label, 1, spells.size()+1);
//  						spells.add(newSpell);
  						updateCreatureList();
  					}
  				});
  				curCreature.add(add, 0, 6);
  		      	
  		      	grid.add(curCreature, 1, 1);

  		      	
  		      	
  		    //make the window
  		    secondaryStage.setScene(new Scene(grid, 900, 600));
  			secondaryStage.show();
  			return secondaryStage;
  		}

  		
  		
  		
  		

	private void updateCreatureList() {
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
}
