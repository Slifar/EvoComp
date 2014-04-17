
public class Item {
	int size = 0;
	int value = 0;
	
	public Item(int givensize, int givenvalue){
		size = givensize;
		value = givenvalue;
	}
	
	public int getValue(){
		return value;
	}
	
	public int getSize(){
		return size;
	}
}
