package Spells;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class SpellManager extends Application{
	private List<Spell> spells;
	private SpellBuilder spellbuilder;
	private SpellBook spellbook;

	public static void main(String[] args) {
		launch(args);
	}
	
	public SpellManager() {
		spells = new SpellParser().Parse();
		spellbuilder = new SpellBuilder(spells);
		spellbook = new SpellBook(spells);
	}

	@Override public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("");//"Spell manager");
        GridPane grid = new GridPane();
        grid.setHgap(10);
      	
        Button spellButton = new Button("Spell list");//Creates a button for spawning SpellBuilder windows
        spellButton.setOnAction(new EventHandler<ActionEvent>() {
        			@Override
        			public void handle(ActionEvent event) {spellbuilder.makeDisplay();}
        		});
        grid.add(spellButton,0,0);
      	
        Button spellBookButton = new Button("Spellbook");//Creates a button for spawning Spellbook windows
        spellBookButton.setOnAction(new EventHandler<ActionEvent>() {
        			@Override
        			public void handle(ActionEvent event) {spellbook.makeDisplay();}
        		});
        grid.add(spellBookButton,1,0);

        primaryStage.setScene(new Scene(grid, 140, 25));
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
            if (TTBehaviourClass == null) {
                // abort
                return;
            }
            Constructor constructor = TTBehaviourClass.getDeclaredConstructor(
                    Duration.class, Duration.class, Duration.class, boolean.class);
            if (constructor == null) {
                // abort
                return;
            }
            constructor.setAccessible(true);
            Object newTTBehaviour = constructor.newInstance(
                    new Duration(openDelayInMillis), new Duration(visibleDurationInMillis), 
                    new Duration(closeDelayInMillis), false);
            if (newTTBehaviour == null) {
                // abort
                return;
            }
            Field ttbehaviourField = Tooltip.class.getDeclaredField("BEHAVIOR");
            if (ttbehaviourField == null) {
                // abort
                return;
            }
            ttbehaviourField.setAccessible(true);
             
            // Cache the default behavior if needed.
            Object defaultTTBehavior = ttbehaviourField.get(Tooltip.class);
            ttbehaviourField.set(Tooltip.class, newTTBehaviour);
             
        } catch (Exception e) {
            System.out.println("Aborted setup due to error:" + e.getMessage());
        }
    }
}
