package Encounters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import Creatures.Creature;
import Creatures.CreatureBuilder;
import Creatures.CreatureParser;
import Spells.Spell;
import Spells.SpellBook;
import Spells.SpellBuilder;
import Spells.SpellParser;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class EncounterManager extends Application{

	private List<Creature> creatures;
	private List<Spell> spells;
	private EncounterBuilder builder;
	private EncounterRunner runner;
	private SpellBuilder spellbuilder;
	private SpellBook spellbook;
	private CreatureBuilder creaturebuilder;

	public static void main(String[] args) {
		launch(args);
	}
	
	public EncounterManager() {
		spells = new SpellParser().Parse();
		creatures = new CreatureParser(spells).Parse();
		builder = new EncounterBuilder(creatures);
		runner = new EncounterRunner(creatures);
		spellbuilder = new SpellBuilder(spells);
		spellbook = new SpellBook(spells);
		creaturebuilder = new CreatureBuilder(creatures, spells);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Encounter manager");
        GridPane grid = new GridPane();
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
        			@Override public void handle(ActionEvent event) {builder.makeDisplay();}});
        encounterBar.add(encounterBuilderSpawner,0,0);
      	
        Button encounterRunnerSpawner = new Button("Encounter runner");//Creates a button for spawning EncounterRunner windows
        encounterRunnerSpawner.setOnAction(new EventHandler<ActionEvent>() {
        			@Override public void handle(ActionEvent event) {runner.makeDisplay();}});
        encounterBar.add(encounterRunnerSpawner,1,0);
        
        grid.add(encounterBar,1,0);


        
      	label = new Label(" Creature management");
      	grid.add(label,0,1);
      	
        Button creatureButton = new Button("Creature list");//Creates a button for spawning CreatureBuilder windows
        creatureButton.setOnAction(new EventHandler<ActionEvent>() {
        			@Override public void handle(ActionEvent event) {creaturebuilder.makeDisplay();}});
        creatureBar.add(creatureButton,0,0);
        
        grid.add(creatureBar,1,1);


        
      	label = new Label(" Spell management");
      	grid.add(label,0,2);
      	
        Button spellButton = new Button("Spell list");//Creates a button for spawning SpellBuilder windows
        spellButton.setOnAction(new EventHandler<ActionEvent>() {
        			@Override public void handle(ActionEvent event) {spellbuilder.makeDisplay();}});
        spellBar.add(spellButton,0,0);
      	
        Button spellBookButton = new Button("Spellbook");//Creates a button for spawning Spellbook windows
        spellBookButton.setOnAction(new EventHandler<ActionEvent>() {
        			@Override public void handle(ActionEvent event) {spellbook.makeDisplay();}});
        spellBar.add(spellBookButton,1,0);

        grid.add(spellBar,1,2);
        
        
        
        primaryStage.setScene(new Scene(grid, 600, 500));
        primaryStage.show();
        setupCustomTooltipBehavior(0,100000000,0);
	}

	
	

@SuppressWarnings({ "rawtypes", "unchecked" })
private void setupCustomTooltipBehavior(int openDelayInMillis, int visibleDurationInMillis, int closeDelayInMillis) {
        try {
            Class TTBehaviourClass = null;
            Class<?>[] declaredClasses = Tooltip.class.getDeclaredClasses();
            for (Class c:declaredClasses) {
                if (c.getCanonicalName().equals("javafx.scene.control.Tooltip.TooltipBehavior")) {
                    TTBehaviourClass = c;
                    break;
                }
            }
            if (TTBehaviourClass == null) {return;}// abort
            Constructor constructor = TTBehaviourClass.getDeclaredConstructor(
                    Duration.class, Duration.class, Duration.class, boolean.class);
            if (constructor == null) {return;}// abort
            constructor.setAccessible(true);
            Object newTTBehaviour = constructor.newInstance(
                    new Duration(openDelayInMillis), new Duration(visibleDurationInMillis), 
                    new Duration(closeDelayInMillis), false);
            if (newTTBehaviour == null) { return;}// abort
            Field ttbehaviourField = Tooltip.class.getDeclaredField("BEHAVIOR");
            if (ttbehaviourField == null) {return;}// abort
            ttbehaviourField.setAccessible(true);
            // Cache the default behavior if needed.
            Object defaultTTBehavior = ttbehaviourField.get(Tooltip.class);
            ttbehaviourField.set(Tooltip.class, newTTBehaviour);
        } catch (Exception e) {
            System.out.println("Aborted setup due to error:" + e.getMessage());
        }
    }
}
