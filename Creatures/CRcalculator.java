package Creatures;

import java.util.List;
import java.util.Map;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;

@SuppressWarnings("restriction")
public class CRcalculator {

	private int[][] crCalcValues = {
		//			Defensive	-----Offensive-----
		//Prof, AC	HP bounds	Attack bonus, SaveDC, Damage bounds
		{2, 13,		1, 6,		3, 13,		0, 1},//CR 0	//AC, Attack mod, and DC are max's for this one
		{2, 13,		7, 35,		3, 13,		2, 3},//CR 1/8
		{2, 13,		36, 49,		3, 13,		4, 5},//CR 1/4
		{2, 13,		50, 70,		3, 13,		6, 8},//CR 1/2
		{2, 13,		71, 85,		3, 13,		9, 14},//CR 1
		{2, 13,		86, 100,	3, 13,		15, 20},//CR 2
		{2, 13,		101, 115,	4, 13,		21, 26},//CR 3
		{2, 14,		116, 130,	5, 14,		27, 32},//CR 4
		{3, 15,		131, 145,	6, 15,		33, 38},//CR 5
		{3, 15,		146, 160,	6, 15,		39, 44},//CR 6
		{3, 15,		161, 175,	6, 15,		45, 50},//CR 7
		{3, 16,		176, 190,	7, 16,		51, 56},//CR 8
		{4, 16,		191, 205,	7, 16,		57, 62},//CR 9
		{4, 17,		206, 220,	7, 16,		63, 68},//CR 10
		{4, 17,		221, 235,	8, 17,		69, 74},//CR 11
		{4, 17,		236, 250,	8, 17,		75, 80},//CR 12
		{5, 18,		251, 265,	8, 18,		81, 86},//CR 13
		{5, 18,		266, 280,	8, 18,		87, 92},//CR 14
		{5, 18,		281, 295,	8, 18,		93, 98},//CR 15
		{5, 18,		296, 310,	9, 18,		99, 104},//CR 16
		{6, 19,		311, 325,	10, 19,		105, 110},//CR 17
		{6, 19,		326, 340,	10, 19,		111, 116},//CR 18
		{6, 19,		341, 355,	10, 19,		117, 122},//CR 19
		{6, 19,		356, 400,	10, 19,		123, 140},//CR 20
		{7, 19,		401, 445,	11, 20,		141, 158},//CR 21
		{7, 19,		446, 490,	11, 20,		159, 176},//CR 22
		{7, 19,		491, 535,	11, 20,		177, 194},//CR 23
		{7, 19,		536, 580,	12, 21,		195, 212},//CR 24
		{8, 19,		581, 625,	12, 21,		213, 230},//CR 25
		{8, 19,		626, 670,	12, 21,		231, 248},//CR 26
		{8, 19,		671, 715,	13, 22,		249, 266},//CR 27
		{8, 19,		716, 760,	13, 22,		267, 284},//CR 28
		{9, 19,		761, 805,	13, 22,		285, 302},//CR 29
		{9, 19,		806, 850,	14, 23,		303, 320}//CR 30
	};
	private Stage secondaryStage = null;
	
	int ac, hp, vulns, resists, immunes, saveprofs;
	boolean hasfly;
	Map<String,String> passives,actions,reactions,legendaryactions;
	List<String> lairactions;
	
	public CRcalculator(int ac, int hp, int vulns, int resists, int immunes, int saveprofs,
			boolean hasfly,
			Map<String,String> passives, Map<String,String> actions, Map<String,String> reactions,
			Map<String,String> legendaryactions, List<String> lairactions) {
		this.ac = ac;
		this.hp = hp;
		this.vulns = vulns;
		this.resists = resists;
		this.immunes = immunes;
		this.saveprofs = saveprofs;
		this.hasfly = hasfly;
		this.passives = passives;
		this.actions = actions;
		this.reactions = reactions;
		this.legendaryactions = legendaryactions;
		this.lairactions = lairactions;
	}

	public Stage makeDisplay() {
		if(secondaryStage != null) {secondaryStage.close();}
		secondaryStage = new Stage();//make the window
		secondaryStage.setTitle("CR Calculator");
		GridPane grid = new GridPane();
        grid.setHgap(10);
      	grid.setVgap(10);
      	
      	grid.add(new Label("Defensive factors"), 0, 0);
	  		grid.add(new Label("AC: "+ac+"\tHP: "+hp), 1, 1);
	  		grid.add(new Label("Vulnerabilties: "+vulns+"\tResistances: "+resists+"\tImmunities: "+immunes), 1, 2);
	  		grid.add(new Label("Save proficiencies: "+saveprofs), 1, 3);
	  		if(hasfly){grid.add(new Label("Has a fly speed."), 1, 4);}
      	
	  	GridPane abilities = new GridPane();
	  		int i=0;
	  		abilities.add(new Label("Passives"), 0, i++);
	  		for(String s:passives.keySet()){
	  			abilities.add(new Label(s), 1, i);
	  			abilities.add(new Label(passives.get(s)), 2, i);
	  			i++;
	  		}
	  		abilities.add(new Label("Actions"), 0, i++);
	  		for(String s:actions.keySet()){
	  			abilities.add(new Label(s), 1, i);
	  			abilities.add(new Label(actions.get(s)), 2, i);
	  			i++;
	  		}
	  		abilities.add(new Label("Reactions"), 0, i++);
	  		for(String s:reactions.keySet()){
	  			abilities.add(new Label(s), 1, i);
	  			abilities.add(new Label(reactions.get(s)), 2, i);
	  			i++;
	  		}
	  		abilities.add(new Label("Legendary Actions"), 0, i++);
	  		for(String s:legendaryactions.keySet()){
	  			abilities.add(new Label(s), 1, i);
	  			abilities.add(new Label(legendaryactions.get(s)), 2, i);
	  			i++;
	  		}
	  		abilities.add(new Label("Lair Actions"), 0, i++);
	  		for(String s:lairactions){
	  			abilities.add(new Label(s), 1, i++);
	  		}
	  	grid.add(abilities, 0, 5, 2, 1);
	  	
	  	
      	//Needed values.
      	//	Expected CR & Prof (in window)
      	//	AC, HP, Attack mod.
      	//	Number of vulns, resistances, and damage immunities.
      	//	Passives, Actions, Reactions, Legendary actions, Lair actions. (refined in window)
      	//	If has fly speed.
      	//	Number of save profs.
      	//	Average damage, attack mod, and save DC (in window)
      	
      	//Get prof from expected CR
      	//Multipliers
      		//Multiple vulns halve HP
      		//Multiple Resistances		CR 1-4 HPx2		CR 5-10 HPx1.5	CR 11-16 HPx1.25	CR >17 HPx1
      		//Multiple Immunities		CR 1-4 HPx2		CR 5-10 HPx2	CR 11-16 HPx1.5		CR >17 HPx1.25
      	//Need to calculate average damage/round.
      		//Include all damage of the round, such as legendary actions, lair actions and auras.
      		//Take the most damaging option available.
      			//If it changes between rounds (eg limited use being the biggest) average the first 3 turns.
      	//Passives & reactions have individualized effects (DMG pg 280).
      		//Spellcasting in general has no effect
      			//More damaging spells or AC effecting spells need to be accounted for in other calcs.
      	//If it can fly and deal ranged damage with an expected CR of <10, get +2 AC.
      	//3/4 save profs acts as +2 AC.	5/6 save profs acts as +4 AC.
      	
      	
      	//make the window
  	    secondaryStage.setScene(new Scene(grid, 900, 500));
  		secondaryStage.show();
  		return secondaryStage;
  	}
}
