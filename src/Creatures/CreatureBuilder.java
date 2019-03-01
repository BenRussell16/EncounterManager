package src.Creatures;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Resources.Source;
import src.Creatures.Creature.Alignment;
import src.Creatures.Creature.DamageMultiplier;
import src.Creatures.Creature.DamageType;
import src.Creatures.Creature.Languages;
import src.Creatures.Creature.Senses;
import src.Creatures.Creature.Size;
import src.Creatures.Creature.Skills;
import src.Creatures.Creature.Speeds;
import src.Creatures.Creature.Stats;
import src.Creatures.Creature.StatusCondition;
import src.Creatures.Creature.Type;
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
	
	private List<Creature> query;									//TODO - Label for global fields
	private GridPane creatureList;
		private List<Label> names;
		private List<Label> crs;
	private GridPane curCreature;
  		private TextField nameField;
  		private ToggleGroup sizePicker = new ToggleGroup();
  		private ToggleGroup typePicker = new ToggleGroup();
  			private Label subtypeLabel;
  			private GridPane subtypePanel;
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
	    	
	  		//TODO more inputs
	    		//Passives
	    			//Innate spellcasting
	    			//Spellcasting
	    			//Legendary resistances (int)
  					//Other
	    		//Actions
	    			//Multiattacks
	    		//Bonus actions
	    		//Reactions
	    		//Legendary actions (and number per round)
	    		//Lair actions
  		
  		private ChoiceBox<Source> sourceSelect;
  		

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
  					TextField nameFilter = new TextField();			//TODO - label for start of filter fields

  					ChoiceBox<Double> crFilter = new ChoiceBox<Double>(FXCollections.observableArrayList(
  							null,0.0,0.125,0.25,0.5,1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,
  							10.0,11.0,12.0,13.0,14.0,15.0,16.0,17.0,18.0,19.0,
  							20.0,21.0,22.0,23.0,24.0,25.0,26.0,27.0,28.0,29.0,30.0
  							));
  					crFilter.setConverter(new StringConverter<Double>() {
  						@Override public Double fromString(String string) {return null;}
  						@Override public String toString(Double object) {
  							if(object==null){return null;}
  							else if(object == 0.125){return "1/8";}
  							else if(object == 0.25){return "1/4";}
  							else if(object == 0.5){return "1/2";}
  							else{return ((Integer)object.intValue()).toString();}
  						}
  					});
  					//TODO - allow selection of multiple
  					crFilter.setValue(null);
  					
  					ChoiceBox<Type> typeFilter = new ChoiceBox<Type>(FXCollections.observableArrayList());
  					typeFilter.getItems().add(null);
  					typeFilter.getItems().addAll(Type.values());
  					typeFilter.setValue(null);
  					typeFilter.setConverter(new StringConverter<Type>(){
  						@Override public Type fromString(String arg0) {return null;}
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
  						@Override public Alignment fromString(String arg0) {return null;}
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
  						@Override public Source fromString(String arg0) {return null;}
  						@Override public String toString(Source source) {
  							if(source != null){
  								return source.toNiceString();}
  							return null;
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
  		      	grid.add(sp,0,1);

  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
  		      	
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

  		      	subtypeLabel = new Label(" Subtype");
  		      	curCreature.add(subtypeLabel, 0, layer);
  		      	subtypeLabel.setVisible(false);
  		      	subtypePanel = new GridPane();
  		      	subtypePicker = new HashMap<Type,List<RadioButton>>();
  		      	i=0;
  		      	for(Type t: Type.values()){
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
		      			subtypePanel.add(rb, i, 0);
		      			rb.setVisible(false);
		      			i++;
  		      			for(Type.subType s:t.getSubtype(t)){
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
  		      				subtypePanel.add(rb, i, 0);
  		      				rb.setVisible(false);
  		      				i++;
  		      			}
  		      		}
  		      	}
  		      	subtypePanel.setVisible(false);
  		      	curCreature.add(subtypePanel, 1, layer);
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

  		      	label = new Label("Speed");							//TODO - Label for speed input
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


  		      	label = new Label("Stats");							//TODO - Label for start of stats input
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
  		      	
  		      	
  		      	label = new Label("Saves");
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
  		      	
  		      	label = new Label("Skills");
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
  		      		if(mult != DamageMultiplier.NONE){
  		      			resistancePicker.put(mult, new HashMap<DamageType,RadioButton>());
  		      			if(mult == DamageMultiplier.RESISTANCE){label = new Label("Resistances");}
  		      			else if(mult == DamageMultiplier.IMMUNITY){label = new Label("Immunities");}
  		      			else if(mult == DamageMultiplier.VULNERABILITY){label = new Label("Vulnerabilities");}
  		      			else if(mult == DamageMultiplier.HEALING){label = new Label("Healed by");}
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
  		      	
  		      	label = new Label("Condition immunities");
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

  		      	label = new Label("Senses");
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

  		      	label = new Label("Languages");
	  		    curCreature.add(label, 0, layer);
	  		    languagePicker = new HashMap<Languages, RadioButton>();
	  		    GridPane languagePane = new GridPane();
	  		    languagePane.setHgap(10);
	  		    i=0;
	  		    for(Languages l: Languages.values()){
	  		    	RadioButton rb = new RadioButton(l.toNiceString());
	  		    	languagePane.add(rb, i%8, i/8);
	  		    	languagePicker.put(l, rb);
	  		    	i++;
	  		    }
	  		    curCreature.add(languagePane, 1, layer);
	  		    layer++;
  		    	
	  		    
  		    	layer = 1;											//TODO - Label for start of 2nd column
  		    	
  		    	
  		  		//TODO more inputs
  		    		//Passives
  		    			//Innate spellcasting
  		    			//Spellcasting
  		    			//Legendary resistances (int)
  	  					//Other
  		    		//Actions
  		    			//Multiattacks
  		    		//Bonus actions
  		    		//Reactions
  		    		//Legendary actions (and number per round)
  		    		//Lair actions
  		      	
  		      	label = new Label(" Source");
  		      	curCreature.add(label, 2, layer);
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
  				curCreature.add(sourceSelect, 3, layer);
  				layer++;
  		      	
  				
  				Button add = new Button("Add creature");			//TODO - Label for creature constructor
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
  				curCreature.add(add, 2, layer);
  		      	
  		      	grid.add(curCreature, 1, 1);

  		      	
  		      	
  		    //make the window
  		    secondaryStage.setScene(new Scene(grid, 1700, 1000));
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
		subtypeLabel.setVisible(t.hasSubtype());
		subtypePanel.setVisible(t.hasSubtype());
		
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
