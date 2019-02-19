package EncounterManager.src.Spells;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import EncounterManager.Resources.Source;
import EncounterManager.src.Creatures.Creature;
import EncounterManager.src.Encounters.EncounterEntity;
import EncounterManager.src.Encounters.EncounterManager;
import EncounterManager.src.Spells.Spell.Classes;
import EncounterManager.src.Spells.Spell.School;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
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

public class SpellBuilder {
	private final List<Spell> spells;
	private Stage secondaryStage = null;
	
	public SpellBuilder(List<Spell> spells) {
		this.spells = spells;
	}
	
	private List<Spell> query;
	private GridPane spellList;
	private GridPane curSpell;
	//TODO - delete if unneeded
//		private TextField nameField;
//		private ChoiceBox<Integer> levelPicker;
//		private ChoiceBox<School> schoolPicker;
//		private ToggleGroup classRadios;
//		private ToggleGroup sourceRadios;
	
	private Map<TextField,Label> inputs;
	private List<TextField> fields;
	private List<Label> fieldTags;
	private GridPane multiplierTable;
	private List<Button> party;
	
	public Stage makeDisplay() {
		if(secondaryStage != null) {secondaryStage.close();}
		secondaryStage = new Stage();//make the window
		secondaryStage.setTitle("Spells");
		GridPane grid = new GridPane();
        grid.setHgap(10);
      	grid.setVgap(10);
      	
      		query = new ArrayList<Spell>();
      		query.addAll(spells);
			List<Label> amounts = new ArrayList<Label>();
			fields = new ArrayList<TextField>();
			fieldTags = new ArrayList<Label>();
			inputs = new HashMap<TextField,Label>();
			
			GridPane topBar = new GridPane();
			
			//TODO make a button to save new spells
//				Button export = new Button("Export enounter");
//				export.setOnAction(new EventHandler<ActionEvent>() {
//					@Override
//					public void handle(ActionEvent event) {
//						System.out.println("Exporting encounter");
//						List<EncounterEntity> encounter = new ArrayList<EncounterEntity>();
//						for(Creature c:creatures) {
//							if(built.get(c)==1) {
//								encounter.add(new EncounterEntity(c,c.getName()));
//							}else {
//								for(int i=0; i<built.get(c); i++) {
//									encounter.add(new EncounterEntity(c,c.getName()+" "+(i+1)));
//								}
//							}
//						}
//						secondaryStage.close();
//						mainWindow.showEncounter(encounter);
//					}
//				});
//				topBar.add(export, 0, 0);
				
			
				Label label = new Label("\t\t");topBar.add(label, 1, 0);//TODO make this not suck
				//Set up filter inputs
				TextField nameFilter = new TextField();

				//TODO - allow selection of multiple
				ChoiceBox<Integer> levelFilter = new ChoiceBox<Integer>(FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9));
				levelFilter.setValue(null);
				
				ChoiceBox<School> schoolPicker = new ChoiceBox<School>(FXCollections.observableArrayList());
				schoolPicker.getItems().add(null);
				schoolPicker.getItems().addAll(Spell.School.values());
				schoolPicker.setValue(null);
				
				ChoiceBox<Classes> classPicker = new ChoiceBox<Classes>(FXCollections.observableArrayList());
				classPicker.getItems().add(null);
				classPicker.getItems().addAll(Spell.Classes.values());
				classPicker.setValue(null);
				
				ChoiceBox<Source> sourcePicker = new ChoiceBox<Source>(FXCollections.observableArrayList());
				sourcePicker.getItems().add(null);
				sourcePicker.getItems().addAll(Source.values());
				sourcePicker.setValue(null);
				
				//Define the filtering action
				EventHandler<ActionEvent> filterQuery =new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						query.removeAll(spells);//text filter
						for(Spell s:spells) {
							if(s.getName().toLowerCase().contains(nameFilter.getText().toLowerCase())) {
								query.add(s);
							}
						}
						if(query.isEmpty()) {query.addAll(spells);}

						if(levelFilter.getValue()!=null) {//level filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getLevel()==levelFilter.getValue())) {
									toRemove.add(s);
								}
							}
							for(Spell s:toRemove) {query.remove(s);}
						}
						if(schoolPicker.getValue()!=null) {//school filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!(s.getSchool()==schoolPicker.getValue())) {
									toRemove.add(s);
								}
							}
							for(Spell s:toRemove) {query.remove(s);}
						}
						if(classPicker.getValue()!=null) {//class filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!s.fromClass(classPicker.getValue())) {
									toRemove.add(s);
								}
							}
							for(Spell s:toRemove) {query.remove(s);}
						}
						if(sourcePicker.getValue()!=null) {//source filter
							List<Spell> toRemove = new ArrayList<Spell>();
							for(Spell s:query) {
								if(!s.fromSource(sourcePicker.getValue())) {
									toRemove.add(s);
								}
							}
							for(Spell s:toRemove) {query.remove(s);}
						}
						updateSpellList();
					}
				};
				//Add filter inputs to panel, and set them up to apply when used.
				nameFilter.setOnAction(filterQuery);
				topBar.add(nameFilter, 2, 0);
				levelFilter.setOnAction(filterQuery);
				topBar.add(levelFilter, 3, 0);
				schoolPicker.setOnAction(filterQuery);
				topBar.add(schoolPicker, 4, 0);
				classPicker.setOnAction(filterQuery);
				topBar.add(classPicker, 5, 0);
				sourcePicker.setOnAction(filterQuery);
				topBar.add(sourcePicker, 6, 0);
				
			grid.add(topBar, 0, 0, 2, 1);
			

			
      		ScrollPane sp = new ScrollPane();//allow scrolling down the spell list
      		spellList = new GridPane();
      		spellList.setHgap(10);
	  		label = new Label(" Spells");//make the list of spells and their levels
	  		spellList.add(label, 0, 0);
	  		label = new Label(" Level");
	  		spellList.add(label, 1, 0);
	      	for(int i=0; i<spells.size(); i++) {
	      		label = new Label(" "+spells.get(i).getName());
	      		Tooltip toolTip = new Tooltip(spells.get(i).toString());
	      		label.setTooltip(toolTip);
	      		fieldTags.add(label);
	      		spellList.add(label, 0, i+1);
	      		label = new Label(" "+spells.get(i).getLevel());
	      		spellList.add(label, 1, i+1);
	      	}
	      	sp.setContent(spellList);
	      	grid.add(sp,0,1);

	      	//TODO - 2nd pane for spell addition

	    //make the window
	    secondaryStage.setScene(new Scene(grid, 1200, 600));
		secondaryStage.show();
		return secondaryStage;
	}

	private void updateSpellList() {
		//TODO - update list for queries
//		for(int i=0; i<spells.size(); i++) {
//			boolean visible = query.contains(spells.get(i));
//			TextField curField = fields.get(i);
//			curField.setVisible(visible);//hide the text field for excluded creatures
//			Label curLabel = fieldTags.get(i);
//			curLabel.setVisible(visible);//hide its label
//			if(!visible) {
//				creatureList.getChildren().remove(curField);
//				creatureList.getChildren().remove(curLabel);
//			}else if(!(creatureList.getChildren().contains(curField)||creatureList.getChildren().contains(curLabel))){
//				creatureList.getChildren().add(curField);
//				creatureList.getChildren().add(curLabel);
//			}
//			
//		}
	}
}
