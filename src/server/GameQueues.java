package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;

import server_client.Playmode;
import server_client.User;

public class GameQueues {
	
	private static GameQueues instance = null;
	private HashMap<Integer, Queue<User>> gameQueueMap;
	
	
	private GameQueues(){
		gameQueueMap = new HashMap<>();
	}
	
	public static GameQueues getInstance(){
		if(instance == null){
			instance = new GameQueues();
		}
		return instance;
	}
	
	public void setPlaymodes(Playmode[] playmodes){
		if(playmodes != null){
			for (Playmode playmode : playmodes) {
				if(gameQueueMap.get(playmode.getPid()) == null){
					Queue<User> gameQueue = new LinkedList<>();
					gameQueueMap.put(playmode.getPid(), gameQueue);
				}
			}
		}
	}
	
	public boolean addUserIntoQueue(User user, Playmode playmode){
		boolean playmodeExists = gameQueueMap.get(playmode.getPid()) != null;
		if(playmodeExists){ 
			Queue<User> gameQueue = gameQueueMap.get(playmode.getPid());
			gameQueue.add(user);
			int requiredPlayer = playmode.getNeededPlayerCount();
			if(gameQueue.size() >= requiredPlayer){
				User[] gameUser = new User[requiredPlayer];
				for (int i = 0; i < gameUser.length; i++) {
					gameUser[i]=gameQueue.poll();
				}
				GameManager.getInstance().createNewGame(playmode, gameUser);
			}
		}
		return playmodeExists;
	}
	
	public void leaveQueues(User leavingUser){
		Iterator<Entry<Integer, Queue<User>>> it = gameQueueMap.entrySet().iterator();
		while(it.hasNext()){
			it.next().getValue().remove(leavingUser);
		}
	}
}