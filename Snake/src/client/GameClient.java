package client;

import processing.core.PApplet;
import processing.net.Client;

public class GameClient extends PApplet {
	public static void main(String[] args) {
		PApplet.main("client.GameClient");
	}
	
	private Client client;
	private int id;
	private byte[] worldSize;
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		client = new Client(this, "localhost", 5204);
		id = -1;
		worldSize = new byte[2];
	}
	
	public void draw() {
		if (id == -1) {
			id = client.read();
			return;
		}
			
		if (client.available() > 0) {
			client.readBytes(worldSize);
			byte[] segments = client.readBytesUntil('_');
			byte[] foodPositions = client.readBytes();
			if (segments != null && foodPositions != null)
				drawWorld(worldSize[0], worldSize[1], segments, foodPositions);
			else
				client.clear();
		}
	}
	
	private void drawWorld(byte w, byte h, byte[] segments, byte[] foodPositions) {
		background(0);
		int tileSize = width / w;
		if (height < width)
			tileSize = height / h;
		
		fill(255, 0, 0);
		for(int i = 0; i < segments.length - 1; i += 2) {
			int x = segments[i];
			int y = segments[i + 1];
			rect(x * tileSize, y * tileSize, tileSize, tileSize);
		}
		
		fill(0, 0, 255);
		for(int i = 0; i < foodPositions.length - 1; i += 2) {
			int x = foodPositions[i];
			int y = foodPositions[i + 1];
			rect(x * tileSize, y * tileSize, tileSize, tileSize);
		}
	}
	
	public void keyPressed() {
		if (id == -1)
			return;
		
		if(keyCode == PApplet.UP || keyCode == PApplet.DOWN || keyCode == PApplet.LEFT || keyCode == PApplet.RIGHT) {
			client.write((byte)id);
			client.write((byte)keyCode);
		}
	}

}
