package EncounterManager.src.Creatures;

public class LegendaryAction extends Action {
	private final int cost;
	private final Action action;

	public LegendaryAction(Action action, int cost) {
		super(action.getName(), action.getRecharge(), action.getUses(), action.getTime());
		this.action = action;
		this.cost = cost;
	}

	public int getCost() {return cost;}
	public Action getAction() {return action;}
	@Override public String toString() {
		String s = "";
		if(cost>1) {s+="Costs "+cost+" actions: ";}
		s+=action.toString();
		return s;
	}
}
