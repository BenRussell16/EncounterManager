package src.Spells;

import java.util.List;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
	}
}
