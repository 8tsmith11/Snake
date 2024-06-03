package server;

import java.util.ArrayList;

import java.util.LinkedList;

public class World {
	private int width, height;
	private ArrayList<Snake> players, deadPlayers;
	private ArrayList<int[]> foodPositions;
	
	private int foodCount;
	
	public World(int width, int height, int foodCount) {
		this.width = width;
		this.height = height;
		this.foodCount = foodCount;
		players = new ArrayList<Snake>();
		deadPlayers = new ArrayList<Snake>();
		foodPositions = new ArrayList<int[]>();
	}
	
	public void update() {
		for (Snake player : players) {
			player.update();
		}
		for (Snake player : deadPlayers) {
			players.remove(player);
		}
		if (deadPlayers.size() > 0)
			deadPlayers.clear();
		
		
		if(foodPositions.size() < foodCount) {
			int[] newFood = new int[2];
			newFood[0] = (int)(Math.random() * width);
			newFood[1] = (int)(Math.random() * height);
			
			foodPositions.add(newFood);
		}
	}
	
	public Snake addPlayer() {
		int x = (int)(Math.random() * width);
		int y = (int)(Math.random() * height);
		for (Snake player : players) {
			for (int[] segment : player.getSegments()) {
				if (segment[0] == x && segment[1] == y) {
					return addPlayer();
				}
			}	
		}
		return addPlayer(x, y);
	}
	
	public Snake addPlayer(int x, int y) {
		Snake player = new Snake(this, x, y);
		players.add(player);
		return player;
	}
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public ArrayList<int[]> getFoodPositions() { return foodPositions; }
	public ArrayList<Snake> getPlayers() { return players; }
	
	public void playerDeath(Snake player) {
		LinkedList<int[]> segments = player.getSegments();
		for(int[] segment : segments) {
			foodPositions.add(segment);
		}
		deadPlayers.add(player);
	}
}
