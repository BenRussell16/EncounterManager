package Encounters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Creatures.Creature;
import Creatures.Creature.DamageType;
import Creatures.Creature.Stats;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;

@SuppressWarnings("restriction")
public class EncounterRunner {
	
	private final List<Creature> creatures;
	private Map<Creature,Integer> encounterCounts;
	
	private GridPane initiativeBar;
		private List<GridPane> initiativePanes;
		private Map<GridPane,Integer> initVals;
		private Map<GridPane,Creature> initCreatures;
		private GridPane lair=null;
	
	private GridPane healthBar;
		private List<GridPane> healthPanes;
		private Map<GridPane,GridPane> healthtoinit;
	
	private GridPane statsBar;

	private RadioButton groupingToggle;
	private int curTurn = -1;
	private Stage secondaryStage = null;
	
	public EncounterRunner(List<Creature> creatures) {
		this.creatures = creatures;
		encounterCounts = new HashMap<Creature,Integer>();
	}

	public Stage makeDisplay() {
		if(secondaryStage != null) {secondaryStage.close();}
		secondaryStage = new Stage();//make the window
		secondaryStage.setTitle("Encounter runner");
		GridPane grid = new GridPane();
        grid.setHgap(10);
      	grid.setVgap(10);
		GridPane topBar = new GridPane();
			Button loadEncounter = new Button("Load encounter");
			loadEncounter.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					loadEncounter();}});
			topBar.add(loadEncounter, 0, 0);
			Button loadParty = new Button("Load party");
			loadParty.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					//TODO
				}
			});
			topBar.add(loadParty, 1, 0);
		grid.add(topBar, 0, 0);
		
		initiativeBar = new GridPane();						//TODO - Label for the initiative bar.
			initiativePanes = new ArrayList<GridPane>();
			initVals = new HashMap<GridPane,Integer>();
			initCreatures = new HashMap<GridPane,Creature>();
			Button newInit = new Button("New Entry");
			newInit.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent arg0) {
					GridPane newCreature = new GridPane();
						TextField name = new TextField("Name");
						newCreature.add(name, 0, 0);
						TextField init = new TextField("");
						newCreature.add(init, 0, 1);
						init.textProperty().addListener(makeIntFilter(init));
						init.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent event) {
								initVals.put(newCreature,Integer.parseInt(init.getText()));
								updateInitBar();
							}
						});
					initiativePanes.add(0, newCreature);
					updateInitBar();
				}
			});
			initiativeBar.add(newInit, 0, 0);
			
			groupingToggle = new RadioButton("Group\t\t");
			groupingToggle.setSelected(true);
			groupingToggle.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {updateCreatures();}});
			groupingToggle.setSelected(true);
			initiativeBar.add(groupingToggle, 0, 1);
			
			Button nextTurn = new Button("Next turn");
			nextTurn.setOnAction(new EventHandler<ActionEvent>() {
				@Override public void handle(ActionEvent event) {
					initiativeBar.getChildren().remove(nextTurn);
					curTurn-=1;
					if(curTurn<0 || curTurn>=initiativePanes.size()){
						curTurn = initiativePanes.size()-1;}
					initiativeBar.add(nextTurn, curTurn+1, 2);
					//TODO - things with the new creature who's turn it is.
				}
			});
			initiativeBar.add(nextTurn, curTurn+1, 2);
		ScrollPane sp = new ScrollPane();
		sp.setContent(initiativeBar);
		sp.setMinViewportHeight(70);
		grid.add(sp, 0, 1);

		healthBar = new GridPane();				//TODO - Label for the initiative bar.
		grid.add(healthBar, 0, 2);
		
			//TODO Reactions spent, Recharge status, Legendaries (res and act) remaining
			//Spell slots.
		
		statsBar = new GridPane();							//TODO - Label for the stat blocks bar.
		sp = new ScrollPane();
		sp.setContent(statsBar);
		grid.add(sp, 0, 3);
				
				
				

	    secondaryStage.setScene(new Scene(grid, 1200, 800));
		secondaryStage.show();
		return secondaryStage;
	}

	
	
	private void updateInitBar(){							//TODO - Label for redrawing and sorting the initiative order.
		//Clear the old creature entries from the lists.
		List<GridPane> toRemove = new ArrayList<GridPane>();
		for(int i=0; i<initiativeBar.getChildren().size(); i++){
			if(initiativeBar.getChildren().get(i) instanceof GridPane){
				toRemove.add((GridPane)initiativeBar.getChildren().get(i));
			}
		}
		initiativeBar.getChildren().removeAll(toRemove);
		//Perform a bubble sort to place reorganise entries in the list.
		GridPane slot;
		boolean sorted = false;
		while(!sorted){
			sorted = true;
			for(int i=0; i<initiativePanes.size()-1; i++){
				if(
					(initVals.containsKey(initiativePanes.get(i)) && !initVals.containsKey(initiativePanes.get(i+1)))||
						//Put things with values before those without.
					(initVals.containsKey(initiativePanes.get(i)) && initVals.containsKey(initiativePanes.get(i+1)) && (
						(initVals.get(initiativePanes.get(i)) > initVals.get(initiativePanes.get(i+1)))||
						//If they both have values sort by initiative value.
						(initVals.get(initiativePanes.get(i)) == initVals.get(initiativePanes.get(i+1)) && (
							(initCreatures.containsKey(initiativePanes.get(i)) && !initCreatures.containsKey(initiativePanes.get(i+1)))||
							//If they have the same initiative, prioritize what has a creature
							(initCreatures.containsKey(initiativePanes.get(i)) && initCreatures.containsKey(initiativePanes.get(i+1)) && (
									(initCreatures.get(initiativePanes.get(i)).getStats().get(Stats.DEX) > initCreatures.get(initiativePanes.get(i+1)).getStats().get(Stats.DEX))//||
									//If they both have creatures, sort by their dex.
									//(initCreatures.get(initiativePanes.get(i)).getStats().get(Stats.DEX) == initCreatures.get(initiativePanes.get(i+1)).getStats().get(Stats.DEX))
							))
						))
					))
				){
					sorted = false;
					slot = initiativePanes.get(i);
					initiativePanes.set(i, initiativePanes.get(i+1));
					initiativePanes.set(i+1, slot);
				}
			}
		}
		//Replace the initiative entries
		for(int i=0; i<initiativePanes.size(); i++){
			initiativeBar.add(initiativePanes.get(i), i+1, 0, 1, 2);
		}
	}
	
	private void updateCreatures(){							//TODO - Label for updating lists when an encounter is loaded or grouping changes.
		//Clear the old list
		List<GridPane> toRemove = new ArrayList<GridPane>();
		for(GridPane gp:initiativePanes){
			if(initCreatures.containsKey(gp)){toRemove.add(gp);}}
		initiativePanes.removeAll(toRemove);
		for(GridPane c:toRemove){
			initCreatures.remove(c);
			initVals.remove(c);
		}
		if(lair!=null){
			initiativePanes.remove(lair);
			lair = null;
		}
		//Add the creatures
		boolean hasLair = false;
		for(Creature c:encounterCounts.keySet()){
			if(encounterCounts.get(c)>0){
				hasLair = hasLair || c.hasLair();
				if(groupingToggle.isSelected()){//Grouped by creature
					GridPane newCreature = new GridPane();
						Label name = new Label(c.getName());
						newCreature.add(name, 0, 0);
						TextField init = new TextField("");
						newCreature.add(init, 0, 1);
						init.textProperty().addListener(makeIntFilter(init));
						init.setOnAction(new EventHandler<ActionEvent>() {
							@Override public void handle(ActionEvent event) {
								initVals.put(newCreature,Integer.parseInt(init.getText()));
								updateInitBar();
							}
						});
					initCreatures.put(newCreature, c);
					initiativePanes.add(0, newCreature);
				}else{//Individual initiatives
					for(int i=0; i<encounterCounts.get(c); i++){
						GridPane newCreature = new GridPane();
							Label name = new Label(c.getName()+" "+(encounterCounts.get(c)-i));
							if(encounterCounts.get(c)==1){name.setText(c.getName());}//Don't need suffix if alone.
							newCreature.add(name, 0, 0);
							TextField init = new TextField("");
							newCreature.add(init, 0, 1);
							init.textProperty().addListener(makeIntFilter(init));
							init.setOnAction(new EventHandler<ActionEvent>() {
								@Override public void handle(ActionEvent event) {
									initVals.put(newCreature,Integer.parseInt(init.getText()));
									updateInitBar();
								}
							});
						initCreatures.put(newCreature, c);
						initiativePanes.add(0, newCreature);
					}
				}
			}
		}
		if(hasLair){
			lair = new GridPane();
				Label name = new Label(" Lair");
				name.setMinWidth(70);
				lair.add(name, 0, 0);
				Label init = new Label(" 20");
				lair.add(init, 0, 1);
			initVals.put(lair, 20);
			initiativePanes.add(0, lair);
		}
		updateHealths();
		updateInitBar();
	}
	
	private void updateHealths(){							//TODO - Label for health bars when an encounter is loaded or grouping changes.
		//Clear the old list
		healthBar.getChildren().clear();
		healthPanes = new ArrayList<GridPane>();
		healthtoinit = new HashMap<GridPane,GridPane>();
		//Add each bar
		int i=0;
		for(GridPane gp:initiativePanes){
			if(initCreatures.containsKey(gp)){
				if(groupingToggle.isSelected()){
					for(int j=0; j<encounterCounts.get(initCreatures.get(gp)); j++){
						addHealthBar(gp,(" "+initCreatures.get(gp).getName()+" "+(j+1)),i);
						i++;
					}
				}else{
					Label curLabel = null;
					for(int j=0; j<gp.getChildren().size(); j++){
						if(gp.getChildren().get(j) instanceof Label){
							curLabel = (Label)gp.getChildren().get(j);
						}
					}//Use the label of the init being paired with.
					addHealthBar(gp,(" "+curLabel.getText()),i);
					i++;
				}
			}
		}
	}
	private void addHealthBar(GridPane initPane, String label, int row){
		GridPane healthRow = new GridPane();
		healthBar.add(healthRow, 0, row);
		healthPanes.add(healthRow);
		healthtoinit.put(healthRow, initPane);
		
		Creature c = initCreatures.get(initPane);
		Label nameLabel = new Label(label);
		if(encounterCounts.get(c)==1){nameLabel.setText(" "+c.getName());}//Don't need suffix if alone.
		nameLabel.setPrefWidth(200);
		healthRow.add(nameLabel, 0, 0);
		
		Slider slider = new Slider();
			slider.setMin(0);
			slider.setMax(c.getHP());
			slider.setValue(c.getHP());
			slider.setShowTickLabels(true);
			slider.setShowTickMarks(true);
			slider.setMajorTickUnit(25);
			slider.setMinorTickCount(5);
			slider.setBlockIncrement(1);
			slider.setSnapToTicks(true);
		healthRow.add(slider, 1, 0);
		
		Label healthLabel = new Label("\t"+((int) slider.getValue())+" / "+c.getHP()+"\t");
		healthRow.add(healthLabel, 2, 0);
		slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				healthLabel.setText("\t"+((int) slider.getValue())+" / "+c.getHP()+"\t");
            }
        });
		
		TextField change = new TextField("0");
		change.textProperty().addListener(makeIntFilter(change));
		healthRow.add(change, 3, 0);
		ChoiceBox<String> type = new ChoiceBox<String>();
		type.getItems().addAll(null,"Healing");
		for(DamageType d:DamageType.values()){type.getItems().add(d.toNiceString());}
		healthRow.add(type, 4, 0);
		change.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent event) {
				//Determine health change
				int damage = Integer.parseInt(change.getText());
				if (type.getValue()!=null){
					if(type.getValue().equals("Healing")){damage = -damage;}
					else{
						for(DamageType d:DamageType.values()){
							if(d.toNiceString().equals(type.getValue())){
								damage = (int) c.getMultiplier(d)*damage;
							}
						}
					}
				}
				//Apply health change
				//TODO tempHP
				int postChange = (int) slider.getValue()-damage;
				slider.setValueChanging(true);
				if(postChange<0){slider.setValue(0);}
				else if(postChange>c.getHP()){slider.setValue(c.getHP());}
				else{slider.setValue(postChange);}
				healthLabel.setText("\t"+((int) slider.getValue())+" / "+c.getHP()+"\t");
			}
		});

		healthRow.add(new Label("\t\t"), 5, 0);
		Button remove = new Button("Remove");
		remove.setOnAction(new EventHandler<ActionEvent>() {
			@Override public void handle(ActionEvent arg0) {
				//Remove from init bar if appropriate
				boolean isLast = true;
				for(GridPane other:healthtoinit.keySet()){
					if(healthtoinit.get(other)==initPane && other!=healthRow){
						isLast=false;
					}
				}
				if(isLast){
					initiativeBar.getChildren().remove(healthtoinit.get(healthRow));
					initiativePanes.remove(healthtoinit.get(healthRow));
					initVals.remove(healthtoinit.get(healthRow));
					initCreatures.remove(healthtoinit.get(healthRow));
					updateInitBar();
				}
				//Remove from health bar
				healthBar.getChildren().remove(healthRow);
				healthPanes.remove(healthRow);
				healthtoinit.remove(healthRow);
				//Remove from stat display if appropriate
				encounterCounts.put(c, encounterCounts.get(c)-1);
				updateStatDisplay();
			}});
		healthRow.add(remove, 6, 0);
		
		//TODO track stuff here.
			//Track conditions
			//Temp hp
			//Bloodied colours?
	}
	
	private void updateStatDisplay(){						//TODO - Label for printing statblocks.
		statsBar.getChildren().clear();
		int i=0;
		for(Creature c:creatures){
			if(encounterCounts.get(c)>0){
				Label stat = new Label(c.toString());
				stat.setWrapText(true);
				TitledPane dd = new TitledPane(c.getName(),stat);
				dd.setExpanded(false);
				dd.setMaxWidth(600);
				statsBar.add(dd, 0, i);
				i++;
			}
		}
	}
	
	private ChangeListener<String> makeIntFilter(TextField tf){
		return new ChangeListener<String>() {//ensure only int values can be applied
			@Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("-?\\d*")) {//remove non ints
					tf.setText(newValue.replaceAll("[^-\\d]", ""));
		            }
				if(newValue.isEmpty()) {tf.setText("0");}//ensure not empty
				if(newValue.equals("-")) {tf.setText("-0");}
				tf.setText(""+Integer.parseInt(tf.getText()));//remove leading 0's
			}
	    };
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
						encounterCounts = new HashMap<Creature,Integer>();
						for(Creature c:creatures){encounterCounts.put(c, 0);}
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
											encounterCounts.put(c, count);
										}
									}
								}
							}else{scan.next();}
						}
					}
					scan.next();
				}//where parsing ends
				scan.close();
				System.out.println("Encounter loading complete");
				updateCreatures();
				updateStatDisplay();
			} catch (FileNotFoundException e) {
				System.out.println("Encounter loading failed");
				e.printStackTrace();
			}
		}else{System.out.println("No file selected");}
	}

}