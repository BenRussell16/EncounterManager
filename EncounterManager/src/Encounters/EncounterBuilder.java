package EncounterManager.src.Encounters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import EncounterManager.src.Creatures.Creature;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.collections.FXCollections;

@SuppressWarnings("restriction")
public class EncounterBuilder {
	//TODO cr evaluator?
		//for template creatures and customs
	
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
	private EncounterManager mainWindow;
	
	public EncounterBuilder(List<Creature> creatures, EncounterManager main) {
		this.creatures = creatures;
		built = new HashMap<Creature,Integer>();
		for(Creature c:creatures) {built.put(c, 0);}
		
		mainWindow = main;
		
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
		if(encounterSize()==1) {value*=multipliers.get(radios.getSelectedToggle().toString().split("\'")[1])[0];}
		else if(encounterSize()==2) {value*=multipliers.get(radios.getSelectedToggle().toString().split("\'")[1])[1];}
		else if(encounterSize()<=6) {value*=multipliers.get(radios.getSelectedToggle().toString().split("\'")[1])[2];}
		else if(encounterSize()<=10) {value*=multipliers.get(radios.getSelectedToggle().toString().split("\'")[1])[3];}
		else if(encounterSize()<=14) {value*=multipliers.get(radios.getSelectedToggle().toString().split("\'")[1])[4];}
		else {value*=multipliers.get(radios.getSelectedToggle().toString().split("\'")[1])[5];}
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
	private ToggleGroup radios = new ToggleGroup();
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
			
			//TODO build queries
			GridPane topBar = new GridPane();
				Button export = new Button("Export enounter");
				export.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Exporting encounter");
						List<EncounterEntity> encounter = new ArrayList<EncounterEntity>();
						for(Creature c:creatures) {
							if(built.get(c)==1) {
								encounter.add(new EncounterEntity(c,c.getName()));
							}else {
								for(int i=0; i<built.get(c); i++) {
									encounter.add(new EncounterEntity(c,c.getName()+" "+(i+1)));
								}
							}
						}
						secondaryStage.close();
						mainWindow.showEncounter(encounter);
					}
				});
				topBar.add(export, 0, 0);
				
			
				Label label = new Label("\t\t");topBar.add(label, 1, 0);//TODO make this not suck
				TextField search = new TextField();

				ChoiceBox<Creature.Type> typePicker = new ChoiceBox<Creature.Type>(FXCollections.observableArrayList());
				typePicker.getItems().add(null);
				typePicker.getItems().addAll(Creature.Type.values());
				typePicker.setValue(null);
				
				ChoiceBox<Creature.Region> regionPicker = new ChoiceBox<Creature.Region>(FXCollections.observableArrayList());
				regionPicker.getItems().add(null);
				regionPicker.getItems().addAll(Creature.Region.values());
				regionPicker.setValue(null);
				
				EventHandler<ActionEvent> filterQuery =new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						query.removeAll(creatures);//text filter
						for(Creature c:creatures) {
							if(c.getName().toLowerCase().contains(search.getText().toLowerCase())) {
								query.add(c);
							}
						}
						if(query.isEmpty()) {query.addAll(creatures);}
						
						if(typePicker.getValue()!=null) {//type filter
							List<Creature> toRemove = new ArrayList<Creature>();
							for(Creature c:query) {
								if(!c.isType((Creature.Type)typePicker.getValue())) {
									toRemove.add(c);
								}
							}
							for(Creature c:toRemove) {query.remove(c);}
						}
						if(regionPicker.getValue()!=null) {//region filter
							List<Creature> toRemove = new ArrayList<Creature>();
							for(Creature c:query) {
								if(!c.inRegion((Creature.Region)regionPicker.getValue())) {
									toRemove.add(c);
								}
							}
							for(Creature c:toRemove) {query.remove(c);}
						}
						updateCreatureList();
					}
				};
	      		search.setOnAction(filterQuery);
				topBar.add(search, 2, 0);
				typePicker.setOnAction(filterQuery);
				topBar.add(typePicker, 3, 0);
				regionPicker.setOnAction(filterQuery);
				topBar.add(regionPicker, 4, 0);
				
			grid.add(topBar, 0, 0, 3, 1);
			
			
			curEncounter = new GridPane();//create the list of labels used for presenting current amounts
			curEncounter.setHgap(10);
			label = new Label("Selections\t");
			curEncounter.add(label, 0, 0);
			for(int i=0; i<creatures.size(); i++) {
				label = new Label();
				amounts.add(label);
				curEncounter.add(label, 0, i+1);
			}
			grid.add(curEncounter,1,1);
			
			
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
	      		label.setTooltip(toolTip);//TODO make this stable
	      		fieldTags.add(label);
	      		creatureList.add(label, 0, i+1);
	      		TextField textField = new TextField();
	      		fields.add(textField);
	      		inputs.put(textField,amounts.get(i));
	      		textField.setText("0");
	      	    textField.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						if (!newValue.matches("\\d*")) {//remove non ints
	      	                textField.setText(newValue.replaceAll("[^\\d]", ""));
	      	            }
						if(newValue.isEmpty()) {textField.setText("0");}//ensure not empty
						textField.setText(""+Integer.parseInt(textField.getText()));//remove leading 0's
					}
	      	    });
	      		textField.setOnAction(new EventHandler<ActionEvent>() {//when text is confirmed update the current lists
					@Override
					public void handle(ActionEvent event) {
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
	      	grid.add(sp,0,1);

	      	
	      	xpDisplay = new GridPane();
	      	xpDisplay.setHgap(10);
	      	xpDisplay.setVgap(10);
			label = new Label("Encounter size: "+encounterSize());
			xpDisplay.add(label, 0, 0);
			label = new Label("Raw XP value: "+encounterValue());
			xpDisplay.add(label, 0, 1);
			label = new Label();	xpDisplay.add(label, 0, 2);//TODO make this not suck
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
					if("x1".equals(rb.getText())){rb.setSelected(true);}
					rb.setOnAction(new EventHandler<ActionEvent>() {//update XPpanel when changed
						public void handle(ActionEvent event) {updateXPPanel();}});
					multiplierTable.add(rb, i+1, 1);
					rb.setToggleGroup(radios);
					for(int j=0; j<multipliers.get(multHeads.get(i)).length;j++) {
						label = new Label(""+multipliers.get(multHeads.get(i))[j]);
						multiplierTable.add(label, i+1, j+2);
					}
				}
				RadioButton rb = new RadioButton("Custom multiplier");
				rb.setOnAction(new EventHandler<ActionEvent>() {//update XPpanel when changed
					public void handle(ActionEvent event) {updateXPPanel();}});
				multiplierTable.add(rb, multipliers.size()+1, 1);
				rb.setToggleGroup(radios);
				for(int j=0; j<6;j++) {
					TextField cusValue = new TextField();
					customMults [j] = cusValue;
					cusValue.setText("0");
					cusValue.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
						@Override
						public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
							if (!newValue.matches("\\d*")) {//remove non ints
								cusValue.setText(newValue.replaceAll("[^\\d]", ""));
		      	            }
							if(newValue.isEmpty()) {cusValue.setText("0");}//ensure not empty
							cusValue.setText(""+Integer.parseInt(cusValue.getText()));//remove leading 0's
						}
		      	    });
					cusValue.setOnAction(new EventHandler<ActionEvent>() {//when text is confirmed update the xp panel
						@Override
						public void handle(ActionEvent event) {
								double[] custom = multipliers.get("Custom multiplier");
								for(int i=0; i<custom.length; i++) {custom[i] = Integer.parseInt(customMults[i].getText());}
								updateXPPanel();
							
						}
		      		});
					multiplierTable.add(cusValue, multipliers.size()+1, j+2);
				}
				double[] custom ={0,0,0,0,0,0};
				multipliers.put("Custom multiplier", custom);
				xpDisplay.add(multiplierTable, 0, 3);
				
				label = new Label();	xpDisplay.add(label, 0, 4);//TODO make this not suck
				label = new Label("Modified XP: 0");
				xpDisplay.add(label, 0, 5);
				label = new Label();	xpDisplay.add(label, 0, 6);//TODO make this not suck
				
				//TODO party stuff constructor
				partyPanel = new GridPane();
			    //grid.setAlignment(Pos.UPPER_LEFT);
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
	      	grid.add(xpDisplay, 2, 1);

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
		label = new Label();	xpDisplay.add(label, 0, 2);//TODO make this not suck
		xpDisplay.add(multiplierTable, 0, 3);
		
		label = new Label();	xpDisplay.add(label, 0, 4);//TODO make this not suck
		label = new Label("Modified XP: "+postMultValue());
		xpDisplay.add(label, 0, 5);
		label = new Label();	xpDisplay.add(label, 0, 6);//TODO make this not suck
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
			label = new Label("Level\t");		tables.add(label, 0, 0);
			label = new Label("Easy\t\t");		tables.add(label, 1, 0);
			label = new Label("Medium\t");		tables.add(label, 2, 0);
			label = new Label("Hard\t");			tables.add(label, 3, 0);
			label = new Label("Deadly\t");		tables.add(label, 4, 0);
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
		}
	}
}
