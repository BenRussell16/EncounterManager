package EncounterManager.src.Spells;

import java.util.ArrayList;
import java.util.List;

import EncounterManager.Resources.Source;
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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class SpellBuilder {
	private final List<Spell> spells;
	private Stage secondaryStage = null;
	
	public SpellBuilder(List<Spell> spells) {
		this.spells = spells;
	}
	
	private List<Spell> query;
	private GridPane spellList;
		private List<Label> names;
		private List<Label> levels;
	private GridPane curSpell;
  		private TextField nameField;
  		private ChoiceBox<Integer> levelPicker;
  		private ChoiceBox<School> schoolSelect;
  		private RadioButton c1, c2, c3, c4, c5, c6, c7, c8;
  		private ChoiceBox<Source> sourceSelect;
	
	public Stage makeDisplay() {
		if(secondaryStage != null) {secondaryStage.close();}
		secondaryStage = new Stage();//make the window
		secondaryStage.setTitle("Spells");
		GridPane grid = new GridPane();
        grid.setHgap(10);
      	grid.setVgap(10);
      	
      		query = new ArrayList<Spell>();
      		query.addAll(spells);
			names = new ArrayList<Label>();
			levels = new ArrayList<Label>();
			
			GridPane topBar = new GridPane();
			
				Button save = new Button("Save changes");
				save.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						System.out.println("Saving spells");
						//TODO - Save the new spells. create a new XML?
							//include sort. sort alphabetically then iterate by level?
					}
				});
				topBar.add(save, 0, 0);
				
			
				Label label = new Label("\t\t");topBar.add(label, 1, 0);//TODO make this not suck
				//Set up filter inputs
				TextField nameFilter = new TextField();

				//TODO - allow selection of multiple
				ChoiceBox<Integer> levelFilter = new ChoiceBox<Integer>(FXCollections.observableArrayList(null,0,1,2,3,4,5,6,7,8,9));
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
							//TODO - fix
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
			

			
			
			//1st pane for spell list
      		ScrollPane sp = new ScrollPane();//allow scrolling down the spell list
      		spellList = new GridPane();
      		spellList.setHgap(10);
	  		label = new Label(" Spell");//make the list of spells and their levels
	  		spellList.add(label, 0, 0);
	  		label = new Label(" Level");
	  		spellList.add(label, 1, 0);
	      	for(int i=0; i<spells.size(); i++) {
	      		label = new Label(" "+spells.get(i).getName());
	      		Tooltip toolTip = new Tooltip(spells.get(i).toString());
	      		label.setTooltip(toolTip);
	      		names.add(label);
	      		spellList.add(label, 0, i+1);
	      		label = new Label(" "+spells.get(i).getLevel());
	      		levels.add(label);
	      		spellList.add(label, 1, i+1);
	      	}
	      	sp.setContent(spellList);
	      	grid.add(sp,0,1);

	      	
	      	
	      	//2nd pane for spell addition
	      	curSpell = new GridPane();
	      	curSpell.setHgap(10);

	      	label = new Label(" Name");
	      	curSpell.add(label, 0, 0);
	      	nameField = new TextField();
	      	curSpell.add(nameField, 1, 0);

	      	label = new Label(" Level");
	      	curSpell.add(label, 0, 1);
	    	levelPicker = new ChoiceBox<Integer>(FXCollections.observableArrayList(null,0,1,2,3,4,5,6,7,8,9));
	    	levelPicker.setValue(null);
	      	curSpell.add(levelPicker, 1, 1);

	      	label = new Label(" School");
	      	curSpell.add(label, 0, 2);
			schoolSelect = new ChoiceBox<School>(FXCollections.observableArrayList());
			schoolSelect.getItems().add(null);
			schoolSelect.getItems().addAll(Spell.School.values());
			schoolSelect.setValue(null);
	      	curSpell.add(schoolSelect, 1, 2);

	      	label = new Label(" Classes");
	      	curSpell.add(label, 0, 3);
	      	GridPane classPanel = new GridPane();
	      	classPanel.setHgap(10);
	      		c1 = new RadioButton("Bard");
	      		classPanel.add(c1, 0, 0);
	      		c2 = new RadioButton("Cleric");
	      		classPanel.add(c2, 1, 0);
	      		c3 = new RadioButton("Druid");
	      		classPanel.add(c3, 2, 0);
	      		c4 = new RadioButton("Paladin");
	      		classPanel.add(c4, 3, 0);
	      		c5 = new RadioButton("Ranger");
	      		classPanel.add(c5, 4, 0);
	      		c6 = new RadioButton("Sorcerer");
	      		classPanel.add(c6, 5, 0);
	      		c7 = new RadioButton("Warlock");
	      		classPanel.add(c7, 6, 0);
	      		c8 = new RadioButton("Wizard");
	      		classPanel.add(c8, 7, 0);
	      	curSpell.add(classPanel, 1, 3);

	      	label = new Label(" Source");
	      	curSpell.add(label, 0, 4);
			sourceSelect = new ChoiceBox<Source>(FXCollections.observableArrayList());
			sourceSelect.getItems().add(null);
			sourceSelect.getItems().addAll(Source.values());
			sourceSelect.setValue(null);
	      	curSpell.add(sourceSelect, 1, 4);
	      	
			Button add = new Button("Add spell");
			add.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					Spell newSpell = new Spell() {
						String name="";
						int level=0;
						School school = null;
						List<Classes> classes = null;
						List<Source> sources = null;
						
						public void constructor(String name, int level, School school, List<Classes> classes, List<Source> sources){
							this.name = name;
							this.level = level;
							this.school = school;
							this.classes = classes;
							this.sources = sources;
						}

						@Override public String getName() {return name;}
						@Override public int getLevel() {return level;}
						@Override public School getSchool() {return school;}
						@Override public List<Classes> getClasses() {return classes;}
						@Override public boolean fromClass(Classes curClass) {return classes.contains(curClass);}
						@Override public boolean fromSource(Source source) {return sources.contains(source);}
						@Override public String toString() {return name;}
					};
					
					List<Classes> classList = new ArrayList<Classes>();
						if(c1.isSelected()){classList.add(Classes.BARD);}
						if(c2.isSelected()){classList.add(Classes.CLERIC);}
						if(c3.isSelected()){classList.add(Classes.DRUID);}
						if(c4.isSelected()){classList.add(Classes.PALADIN);}
						if(c5.isSelected()){classList.add(Classes.RANGER);}
						if(c6.isSelected()){classList.add(Classes.SORCERER);}
						if(c7.isSelected()){classList.add(Classes.WARLOCK);}
						if(c8.isSelected()){classList.add(Classes.WIZARD);}
					List<Source> sourcesList = new ArrayList<Source>();
						sourcesList.add(sourceSelect.getValue());
					newSpell.constructor(nameField.getText(), levelPicker.getValue(), schoolSelect.getValue(),
							classList, sourcesList);
					
					System.out.println("Adding spell "+newSpell.getName());
		      			Label label = new Label(" "+newSpell.getName());
		      			Tooltip toolTip = new Tooltip(newSpell.toString());
		      			label.setTooltip(toolTip);
		      			names.add(label);
		      			spellList.add(label, 0, spells.size()+1);
		      			label = new Label(" "+newSpell.getLevel());
		      			levels.add(label);
		      			spellList.add(label, 1, spells.size()+1);
					spells.add(newSpell);
					updateSpellList();
				}
			});
			curSpell.add(add, 0, 5);
	      	
	      	grid.add(curSpell, 1, 1);

	      	
	      	
	    //make the window
	    secondaryStage.setScene(new Scene(grid, 850, 400));
		secondaryStage.show();
		return secondaryStage;
	}

	private void updateSpellList() {
		for(int i=0; i<spells.size(); i++) {
			boolean visible = query.contains(spells.get(i));
			Label curName = names.get(i);
			Label curLevel = levels.get(i);
			curName.setVisible(visible);//hide the spell in the list for excluded creatures
			curLevel.setVisible(visible);
			if(!visible) {
				spellList.getChildren().remove(curName);
				spellList.getChildren().remove(curLevel);
			}else if(!(spellList.getChildren().contains(curName)||spellList.getChildren().contains(curLevel))){
				spellList.getChildren().add(curName);
				spellList.getChildren().add(curLevel);
			}			
		}
	}
}
