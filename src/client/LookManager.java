package client;

public class LookManager {

	private static LookManager instance;
	public static final int GREEN_LAYOUT = 0;
	
	private int layout = GREEN_LAYOUT;
	
	private LookManager(){
		
	}
	
	public static LookManager getInstance(){
		if(instance == null){
			instance = new LookManager();
		}
		return instance;
	}
	
	public int getLayout(){
		return this.layout;
	}
}
