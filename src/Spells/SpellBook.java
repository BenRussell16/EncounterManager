package src.Spells;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Resources.Source;
import Resources.Classes;
import src.Spells.Spell.School;
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
import javafx.util.StringConverter;

@SuppressWarnings("restriction")
public class SpellBook {
	private final List<Spell> spells;
	private Stage secondaryStage = null;
	
	public SpellBook(List<Spell> spells) {
		this.spells = spells;
	}
	
	private List<Spell> query;
	private GridPane spellList;
		private List<Label> names;
		private List<Label> levels;
	private List<Spell> known;
	private GridPane knownList;
		private List<Label> knownNames;
		private List<Label> knownLevels;
	private GridPane curSpell;
		private Map<Integer, List<Spell>> slots;
		private int[] numSlots = new int[9];
		private List<List<Spell>> dailys;
			private List<Integer> dailyUses;
	
	public Stage makeDisplay() {
		if(secondaryStage != null) {secondaryStage.close();}
		secondaryStage = new Stage();//make the window
		secondaryStage.setTitle("Spellbook");
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
						//Perform a bubble sort to place new spells in the list.
						Spell slot;
						boolean sorted = false;
						while(!sorted){
							sorted = true;
							for(int i=spells.size()-1; i>0; i--){
								if(spells.get(i).getLevel()<spells.get(i-1).getLevel()
									|| (spells.get(i).getLevel()==spells.get(i-1).getLevel()
										&& spells.get(i).getName().compareToIgnoreCase(spells.get(i-1).getName())<0)){
									sorted = false;
									slot = spells.get(i);
									spells.set(i, spells.get(i-1));
									spells.set(i-1, slot);
								}
							}
						}
						//Build the new XML string
						String xml = "";
						for(int i=0; i<spells.size(); i++){xml+=spells.get(i).toXML();}
						//Save the new spells.
						try (PrintWriter out = new PrintWriter(new File("Resources/SpellList"))) {
						    out.print(xml);
						    System.out.println("Spell saving complete");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				});
				topBar.add(save, 0, 0);
				
			
				Label label = new Label("\t\t");topBar.add(label, 1, 0);
				//Set up filter inputs
				TextField nameFilter = new TextField();

				//TODO - allow selection of multiple
				ChoiceBox<Integer> levelFilter = new ChoiceBox<Integer>(FXCollections.observableArrayList(null,0,1,2,3,4,5,6,7,8,9));
				levelFilter.setValue(null);
				
				ChoiceBox<School> schoolPicker = new ChoiceBox<School>(FXCollections.observableArrayList());
				schoolPicker.getItems().add(null);
				schoolPicker.getItems().addAll(School.values());
				schoolPicker.setValue(null);
				schoolPicker.setConverter(new StringConverter<School>(){
					@Override public School fromString(String arg0) {// TODO Auto-generated method stub
						return null;}
					@Override public String toString(School school) {
						if(school != null){
							return school.toNiceString();}
						return null;
					}});
				
				ChoiceBox<Classes> classPicker = new ChoiceBox<Classes>(FXCollections.observableArrayList());
				classPicker.getItems().add(null);
				classPicker.getItems().addAll(Classes.values());
				classPicker.setValue(null);
				classPicker.setConverter(new StringConverter<Classes>(){
					@Override public Classes fromString(String arg0) {// TODO Auto-generated method stub
						return null;}
					@Override public String toString(Classes classes) {
						if(classes != null){
							return classes.toNiceString();}
						return null;
					}});
				
				ChoiceBox<Source> sourcePicker = new ChoiceBox<Source>(FXCollections.observableArrayList());
				sourcePicker.getItems().add(null);
				sourcePicker.getItems().addAll(Source.values());
				sourcePicker.setValue(null);
				sourcePicker.setConverter(new StringConverter<Source>(){
					@Override public Source fromString(String arg0) {// TODO Auto-generated method stub
						return null;}
					@Override public String toString(Source source) {
						if(source != null){
							return source.toNiceString();}
						return null;
					}});
				
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
				
			grid.add(topBar, 0, 0, 3, 1);
			
			
			
			
			
			
			
			
			

			
			
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
	      	//grid.add(knownList, 1, 1);
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	
	      	//3rd pane for current spells
	      	//grid.add(curSpell, 1, 2);

	      	
	      	
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
			curName.setVisible(visible);//hide the entry in the list for excluded spells
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
