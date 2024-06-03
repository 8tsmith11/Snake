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
	private byte[] position;
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		client = new Client(this, "localhost", 5204);
		id = -1;
		worldSize = new byte[2];
		position = new byte[4];
	}
	
	public void draw() {
		if (id == -1) {
			id = client.read();
			return;
		}
			
//		if (client.available() > 0) {
//			client.readBytes(worldSize);
//			client.readBytes(position);
//			System.out.println("first byte = " + worldSize[0]);
//			System.out.println("second byte = " + worldSize[1]);
//			byte[] segments = client.readBytesUntil('_');
//			byte[] foodPositions = client.readBytes();
//			//System.out.println("Segment bytes: " + segments.length);
//		}
	}
	
	private short bytesToShort(byte a, byte b) {
	    short sh = (short)a;
	    sh <<= 8;
	    short ret = (short)(sh | b);
	    return ret;
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
