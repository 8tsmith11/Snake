package server;

import java.util.HashMap;

import processing.core.PApplet;
import processing.net.Client;
import processing.net.Server;

public class GameServer extends PApplet {

	public static void main(String[] args) {
		PApplet.main("server.GameServer");
	}
	
	private Server server;
	private World world;
	private HashMap<Integer, Snake> players;
	private HashMap<Client, Integer> clientIDs;
	private int tickDuration, lastMillis;
	
	public void settings() {
		size(500, 500);
	}
	
	public void setup() {
		server = new Server(this, 5204);
		world = new World(100, 100, 100);
		players = new HashMap<Integer, Snake>();
		clientIDs = new HashMap<Client, Integer>();
		tickDuration = 100;
	}
	
	public void draw() {
		Client nextClient = server.available();
		while (nextClient != null) {
			int id = nextClient.read();
			int input = nextClient.read();
			System.out.println("Player " + id + " input: " + input);
			players.get(id).input(input);
			
			nextClient = server.available();
		}
		
		for (Client client : clientIDs.keySet()) {
			if (client.available() == 0)
				sendWorld(client);
		}
		
		if (millis() - lastMillis >= tickDuration) {
			world.update();
			lastMillis = millis();
		}
		
		drawWorld();
	}
	
	private void sendWorld(Client client) {
		client.write((short)world.getWidth());
		int[] playerHead = players.get(clientIDs.get(client)).getSegments().get(0);
		client.write((short)playerHead[0]);
		client.write((short)playerHead[1]);
		
		for(Snake player : world.getPlayers()) {
			for (int[] segment : player.getSegments()) {
				client.write((short)segment[0]);
				client.write((short)segment[1]);
			}
		}
		
		client.write('_');
		
		for(int[] foodPos : world.getFoodPositions()) {
			client.write((short)foodPos[0]);
			client.write((short)foodPos[1]);
		}
	}
	
	private void drawWorld() {
		background(0);
		fill(255, 0, 0);
		int tileSize = width / world.getWidth();
		for(Snake player : world.getPlayers()) {
			for (int[] segment : player.getSegments()) {
				rect(segment[0] * tileSize, segment[1] * tileSize, tileSize, tileSize);
			}
		}
		
		fill(0, 0, 255);
		for(int[] foodPos : world.getFoodPositions()) {
			rect(foodPos[0] * tileSize, foodPos[1] * tileSize, tileSize, tileSize);
		}
	}

	// returns ID of new player added
	private int addPlayer() {
		Snake player = world.addPlayer();
		int id = 0;
		while (players.containsKey(id)) {
			id++;
		}
		players.put(id, player);
		return id;
	}
	
	public void serverEvent(Server someServer, Client newClient) {
		int id = addPlayer();
		newClient.write(id);
		clientIDs.put(newClient, id);
		System.out.println("Client Connected " + newClient.ip());
	}
}
