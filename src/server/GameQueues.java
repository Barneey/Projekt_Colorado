package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import server_client.Playmode;
import server_client.User;

public class GameQueues {
	
	private static GameQueues instance = null;
	private HashMap<Integer, Queue<User>> gameQueueMap;
	
	
	private GameQueues(){
		gameQueueMap = new HashMap<>();
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
			gameQueueMap.get(playmode.getPid()).add(user);
		}
		return playmodeExists;
	}
	
	public static GameQueues getInstance(){
		if(instance == null){
			instance = new GameQueues();
		}
		return instance;
	}
}