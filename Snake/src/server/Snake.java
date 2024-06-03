package server;

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;

public class Snake {
	private LinkedList<int[]> segments;
	private int vx, vy;
	private World world;
	private boolean alive;
	
	public Snake(World world, int x, int y) {
		this.world = world;
		segments = new LinkedList<int[]>();
		segments.add(new int[2]);
		segments.get(0)[0] = x;
		segments.get(0)[1] = y;
		vx = 0;
		vy = 0;
		alive = true;
	}
	
	public void update() {
		if (! alive)
			return;
		
		checkCollisions();
		if (! alive)
			return;
		move();
		checkFood();
	}
	
	public void input(int key) {
		if (key == PApplet.UP) {
			if (vy == 1)
				return;
			vy = -1;
			vx = 0;
		}
		else if (key == PApplet.DOWN) {
			if (vy == -1)
				return;
			vy = 1;
			vx = 0;
		}
		else if (key == PApplet.RIGHT) {
			if (vx == -1)
				return;
			vx = 1;
			vy = 0;
		}
		else if (key == PApplet.LEFT) {
			if (vx == 1)
				return;
			vx = -1;
			vy = 0;
		}
	}
	
	public LinkedList<int[]> getSegments() {
		return segments;
	}
	
	private void addSegment() {
		int[] segment = new int[2];
		if(segments.size() == 1) {
			int[] head = segments.get(0);
			segment[0] = head[0] - vx;
			segment[1] = head[1] - vy;
		}
		else {
			int[] last = segments.get(segments.size() - 1);
			int[] nextToLast = segments.get(segments.size() - 2);
			int dx = last[0] - nextToLast[0];
			int dy = last[1] - nextToLast[1];
			segment[0] = last[0] + dx;
			segment[1] = last[1] + dy;
		}
		segments.add(segment);
	}
	
	private void move() {
		int[] head = segments.get(0);
		int[] tail = segments.get(segments.size() - 1);
		tail[0] = head[0] + vx;
		tail[1] = head[1] + vy;
		segments.add(0, segments.remove(segments.size() - 1));
	}
	private void checkFood() {
		int[] head = segments.get(0);
		ArrayList<int[]> foods = world.getFoodPositions();
		for (int i = 0; i < foods.size(); i++) {
			int[] food = foods.get(i);
			if(head[0] == food[0] && head[1] == food[1]) {
				foods.remove(i);
				addSegment();
			}
		}
	}
	
	private void checkCollisions() {
		int[] head = segments.get(0);
		if(head[0] + vx < 0 || head[0] + vx >= world.getWidth() || head[1] + vy < 0 || head[1] + vy >= world.getHeight()) {
			world.playerDeath(this);
			alive = false;
			return;
		}
			
		for(Snake player : world.getPlayers()) {
			for (int[] segment : player.getSegments()) {
				if (segment != head && head[0] + vx == segment[0] && head[1] + vy == segment[1]) {
					world.playerDeath(this);
					alive = false;
					return;
				}
			}
		}
	}
}
