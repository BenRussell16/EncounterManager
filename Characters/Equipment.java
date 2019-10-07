package Characters;

import java.util.List;

public interface Equipment {
	public String getName();
	public int getWeight();
	public String toString();
	
	public interface Container extends Equipment{
		public List<Equipment> getContents();
		//Probably redundant.
		public void add(Equipment item);
		public void remove(Equipment item);
		public boolean contains(Equipment item);
		//Check nested extradimensional spaces >:D
		public boolean isBoH();
		public boolean containsBoH();
	}
}