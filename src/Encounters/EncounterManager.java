package src.Encounters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.Creatures.Action;
import src.Creatures.Creature;
import src.Creatures.Creature.DamageType;
import src.Creatures.CreatureBuilder;
import src.Creatures.CreatureParser;
import src.Spells.Spell;
import src.Spells.SpellBook;
import src.Spells.SpellBuilder;
import src.Spells.SpellParser;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class EncounterManager extends Application{
	//TODO initiative, toggle groups of like creatures sharing initiative
	//TODO reactions spent
	//TODO resistances
	//TODO lair actions
	
	//TODO fancy grid shit? lighting?

	private List<Creature> creatures;
	private List<Spell> spells;
	private EncounterBuilder builder;
	private SpellBuilder spellbuilder;
	private SpellBook spellbook;
	private CreatureBuilder creaturebuilder;

	public static void main(String[] args) {
		launch(args);
	}
	
	public EncounterManager() {
		creatures = new CreatureParser().Parse();
		spells = new SpellParser().Parse();
		builder = new EncounterBuilder(creatures, this);
		spellbuilder = new SpellBuilder(spells);
		spellbook = new SpellBook(spells);
		creaturebuilder = new CreatureBuilder(creatures);
	}

	private GridPane encounterPanel;
	List<EncounterEntity> encounterEntities;
	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Encounter manager");
        GridPane grid = new GridPane();
        //grid.setAlignment(Pos.UPPER_LEFT);
        grid.setHgap(10);
      	grid.setVgap(10);
      	GridPane encounterBar = new GridPane();
      	encounterBar.setHgap(10);
      	GridPane creatureBar = new GridPane();
      	creatureBar.setHgap(10);
      	GridPane spellBar = new GridPane();
      	spellBar.setHgap(10);

      	Label label = new Label(" Encounter management");
      	grid.add(label,0,0);
      	
        Button encounterBuilderSpawner = new Button("Encounter builder");//Creates a button for spawning EncounterBuilder windows
        encounterBuilderSpawner.setOnAction(new EventHandler<ActionEvent>() {
        			@Override
        			public void handle(ActionEvent event) {builder.makeDisplay();}
        		});
        encounterBar.add(encounterBuilderSpawner,0,0);

        Button saveEncounter = new Button("Save encounter");//Creates a button for saving encounters
        saveEncounter.setOnAction(new EventHandler<ActionEvent>() {
        			@Override
        			public void handle(ActionEvent event) {saveEncounter(encounterEntities);}
        		});
        encounterBar.add(saveEncounter,1,0);

        Button loadEncounter = new Button("Load encounter");//Creates a button for loading encounters
        loadEncounter.setOnAction(new EventHandler<ActionEvent>() {
        			@Override
        			public void handle(ActionEvent event) {
        				encounterEntities = loadEncounter();
        				showEncounter(encounterEntities);
        			}
        		});
        encounterBar.add(loadEncounter,2,0);
        
        grid.add(encounterBar,1,0);


      	label = new Label(" Creature management");
      	grid.add(label,0,1);
      	
        Button creatureButton = new Button("Creature list");//Creates a button for spawning CreatureBuilder windows
        creatureButton.setOnAction(new EventHandler<ActionEvent>() {
        			@Override
        			public void handle(ActionEvent event) {creaturebuilder.makeDisplay();}
        		});
        creatureBar.add(creatureButton,0,0);
        
        grid.add(creatureBar,1,1);


      	label = new Label(" Spell management");
      	grid.add(label,0,2);
      	
        Button spellButton = new Button("Spell list");//Creates a button for spawning SpellBuilder windows
        spellButton.setOnAction(new EventHandler<ActionEvent>() {
        			@Override
        			public void handle(ActionEvent event) {spellbuilder.makeDisplay();}
        		});
        spellBar.add(spellButton,0,0);
      	
        Button spellBookButton = new Button("Spellbook");//Creates a button for spawning Spellbook windows
        spellBookButton.setOnAction(new EventHandler<ActionEvent>() {
        			@Override
        			public void handle(ActionEvent event) {spellbook.makeDisplay();}
        		});
        spellBar.add(spellBookButton,1,0);

        grid.add(spellBar,1,2);
        
        encounterPanel = new GridPane();
        encounterPanel.setVgap(10);
        grid.add(encounterPanel, 0, 3);
        
        primaryStage.setScene(new Scene(grid, 600, 500));
        primaryStage.show();
	}

	public void showEncounter(List<EncounterEntity> encounter) {//TODO this
		encounterEntities = encounter;

		List<Node> old = new ArrayList<Node>();
		old.addAll(encounterPanel.getChildren());
		encounterPanel.getChildren().removeAll(old);
		
//		for(EncounterEntity e:encounterEntities) {System.out.println(e.label);}
		for (int i = 0; i < encounterEntities.size(); i++) {
			EncounterEntity entity = encounterEntities.get(i);
			GridPane statBlock = new GridPane();
			statBlock.setHgap(10);
			
				Label label = new Label(" "+entity.label);
				statBlock.add(label, 0, 0);
				label = new Label(entity.getHP()+" / "+entity.creature.getHP()+" ("+(entity.creature.getHP()/2)+")");
				statBlock.add(label, 1, 0);
				
				ChoiceBox<DamageType> damType = new ChoiceBox<DamageType>();
				damType.getItems().addAll(DamageType.values());
				damType.setValue(DamageType.NONMAGICALBASIC);
				statBlock.add(damType, 3, 0);
				TextField damAmount = new TextField();
				damAmount.setText("0");
				damAmount.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
						if (!newValue.matches("\\d*")) {//remove non ints
							damAmount.setText(newValue.replaceAll("[^\\d]", ""));
	      	            }
						if(newValue.isEmpty()) {damAmount.setText("0");}//ensure not empty
						damAmount.setText(""+Integer.parseInt(damAmount.getText()));//remove leading 0's
					}
	      	    });
				damAmount.setOnAction(new EventHandler<ActionEvent>() {//when text is confirmed update creature hp
					@Override
					public void handle(ActionEvent event) {
						entity.dealDamage(Integer.parseInt(damAmount.getText()), damType.getValue());
						showEncounter(encounterEntities);
					}
	      		});
				statBlock.add(damAmount, 2, 0);
			
			encounterPanel.add(statBlock, 0, i);
		}
		Button initiatives = new Button("Initiative");
        initiatives.setOnAction(new EventHandler<ActionEvent>() {
        		private GridPane grid;
        		private Stage initiativeStage;
			@Override
			public void handle(ActionEvent event) {
				initiativeStage = new Stage();
				initiativeStage.setTitle("Initiative");
				grid = new GridPane();
				fillGrid(true);
				initiativeStage.setScene(new Scene(grid, 400, 600));
				initiativeStage.show();
			}
			private void fillGrid(boolean grouped) {
				List<Node> old = new ArrayList<Node>();
				old.addAll(grid.getChildren());
				grid.getChildren().removeAll(old);
				
				Button groupToggle = new Button("Toggle grouped initiative");
				groupToggle.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {fillGrid(!grouped);}
				});
				grid.add(groupToggle, 0, 0, 2, 1);
				
				if(grouped) {
					Map<Creature, List<EncounterEntity>> groups = new HashMap<Creature,List<EncounterEntity>>();
					for(EncounterEntity e:encounterEntities) {
						if(groups.keySet().contains(e.creature)) {groups.get(e.creature).add(e);}
						else {
							List<EncounterEntity> list = new ArrayList<EncounterEntity>();
							list.add(e);
							groups.put(e.creature, list);
						}
					}
					for(int i=0; i<creatures.size(); i++) {
						Creature c = creatures.get(i);
						if(groups.keySet().contains(c)) {
							Label label = new Label(c.getName()+"\t");
							grid.add(label, 0, i+1);
						
							TextField initValue = new TextField();
							initValue.setText(groups.get(c).get(0).getInitiative()+"");
							initValue.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
								@Override
								public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
									if (!newValue.matches("\\d*")) {//remove non ints
										initValue.setText(newValue.replaceAll("[^\\d]", ""));
			      	            		}
									if(newValue.isEmpty()) {initValue.setText("0");}//ensure not empty
									initValue.setText(""+Integer.parseInt(initValue.getText()));//remove leading 0's
								}
			      	    		});
							initValue.setOnAction(new EventHandler<ActionEvent>() {//when text is confirmed update creature initiatives
								@Override
								public void handle(ActionEvent event) {
									for(EncounterEntity e: groups.get(c)) {e.setInitiative(Integer.parseInt(initValue.getText()));}
								}
			      			});
							grid.add(initValue, 1, i+1);
						}
					}
					Button commit = new Button("Commit");
					commit.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							List<EncounterEntity> sorted = new ArrayList<EncounterEntity>();
							while(!groups.isEmpty()) {
								Creature earliest = null;
								for(Creature c:groups.keySet()) {
									if (earliest == null) {earliest = c;}
									else {
										if(groups.get(c).get(0).getInitiative()>groups.get(earliest).get(0).getInitiative()) {
											earliest = c;
										}else if(groups.get(c).get(0).getInitiative() == groups.get(earliest).get(0).getInitiative()) {
											if(c.getStats()[1]>earliest.getStats()[1]) {earliest = c;}
										}
									}
								}
								sorted.addAll(groups.get(earliest));
								groups.remove(earliest); 
							}
							initiativeStage.close();
							showEncounter(sorted);
						}
					});
					grid.add(commit, 0, creatures.size()+1);
				}//if !grouped
				else {
					for(int i=0; i<encounterEntities.size(); i++) {
						EncounterEntity e = encounterEntities.get(i);
						Label label = new Label(e.label);
						grid.add(label, 0, i+1);
						
						TextField initValue = new TextField();
						initValue.setText(e.getInitiative()+"");
						initValue.textProperty().addListener(new ChangeListener<String>() {//ensure only int values can be applied
							@Override
							public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
								if (!newValue.matches("\\d*")) {//remove non ints
									initValue.setText(newValue.replaceAll("[^\\d]", ""));
		      	            		}
								if(newValue.isEmpty()) {initValue.setText("0");}//ensure not empty
								initValue.setText(""+Integer.parseInt(initValue.getText()));//remove leading 0's
							}
		      	    		});
						initValue.setOnAction(new EventHandler<ActionEvent>() {//when text is confirmed update creature initiatives
							@Override
							public void handle(ActionEvent event) {
								e.setInitiative(Integer.parseInt(initValue.getText()));
							}
		      			});
						grid.add(initValue, 1, i+1);
					}
					Button commit = new Button("Commit");
					commit.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							List<EncounterEntity> sorted = new ArrayList<EncounterEntity>();
							while(!encounterEntities.isEmpty()) {
								EncounterEntity earliest = null;
								for(EncounterEntity e: encounterEntities) {
									if (earliest == null) {earliest = e;}
									else {
										if(e.getInitiative()>earliest.getInitiative()) {
											earliest = e;
										}else if(e.getInitiative() == earliest.getInitiative()) {
											if(e.creature.getStats()[1]>earliest.creature.getStats()[1]) {earliest = e;}
										}
									}
								}
								sorted.add(earliest);
								encounterEntities.remove(earliest); 
							}
							initiativeStage.close();
							showEncounter(sorted);
						}
					});
					grid.add(commit, 0, creatures.size()+1);
				}
			}
		});
		encounterPanel.add(initiatives, 0, encounterEntities.size());
	}
	
	private List<EncounterEntity> loadEncounter(){//TODO make this take a string
		return null;//TODO this
	}
	private void saveEncounter(List<EncounterEntity> encounter){
		//TODO this
	}
}
