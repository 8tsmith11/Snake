package server;

import java.util.LinkedList;

import processing.core.PApplet;

public class Test extends PApplet {

	public static void main(String[] args) {
		PApplet.main("server.Test");
	}
	
	private World world;
	private int period, lastMillis;
	private int tileSize;
	Snake player;
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		world = new World(100, 100, 100);
		period = 100;
		lastMillis = 0;
		tileSize = 500 / 10;
		world.addPlayer(10, 10);
		player = world.getPlayers().get(0);
	}
	
	public void draw() {
		if (player.getSegments().size() > 10)
			tileSize = 500 / player.getSegments().size();
		else tileSize = 500 / 10;
		background(0);
		if (millis() - lastMillis >= period) {
			world.update();
			lastMillis = millis();
		}
		
		for (Snake player : world.getPlayers()) {
			for (int[] segment : player.getSegments()) {
				int[] pos = getRelativePos(segment[0], segment[1]);
				fill(255, 0, 0);
				rect(pos[0] * tileSize, pos[1] * tileSize, tileSize, tileSize);
			}
		}
		
		fill(0, 0, 255);
		for (int[] food : world.getFoodPositions()) {
			int[] pos = getRelativePos(food[0], food[1]);
			rect(pos[0] * tileSize, pos[1] * tileSize, tileSize, tileSize);
		}	
	}
	
	private int[] getRelativePos(int x, int y) {
		int[] head = player.getSegments().get(0);
		int tileWidth = width / tileSize;
		int tileHeight = height / tileSize;
		int[] pos = new int[2];
		pos[0] = x + tileWidth / 2 - head[0];
		pos[1] = y + tileHeight / 2 - head[1];
		return pos;
	}
	
	public void keyPressed() {
		player.input(keyCode);
	}

}
