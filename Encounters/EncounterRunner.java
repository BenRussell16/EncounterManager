package Encounters;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import Creatures.Creature;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
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
			
			groupingToggle = new RadioButton("Group");
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
					if(curTurn<0){curTurn = initiativePanes.size()-1;}
					initiativeBar.add(nextTurn, curTurn+1, 2);
					//TODO - things with the new creature who's turn it is.
				}
			});
			initiativeBar.add(nextTurn, curTurn+1, 2);
		ScrollPane sp = new ScrollPane();
		sp.setContent(initiativeBar);
		grid.add(sp, 0, 1);
				
				
				
				

	    secondaryStage.setScene(new Scene(grid, 800, 300));
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
							Label name = new Label(c.getName()+" "+(i+1));
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
				Label init = new Label(" 20 ");
				lair.add(init, 0, 1);
			initVals.put(lair, 20);
			initiativePanes.add(0, lair);
		}
		updateInitBar();
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
			} catch (FileNotFoundException e) {
				System.out.println("Encounter loading failed");
				e.printStackTrace();
			}
		}else{System.out.println("No file selected");}
	}

}
