package gameClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;

import Server.game_service;
import utils.Point3D;



public class CreateObject {

	game_service game;

	public CreateObject(game_service game) {
		this.game = game;
	}

	public void setGame(game_service game) {
		this.game = game;
	}
	
	public List<Fruit> createFruitList() {
		List<Fruit> temp = new ArrayList <Fruit>();
		//System.out.println(this.game.getFruits());
		Iterator <String> fi = this.game.getFruits().iterator();
		while (fi.hasNext()) {
			String f = fi.next();
			f = f.substring(9, f.length()-1);
			Fruit fruit = createFruitObject (f);
			temp.add(fruit);
		}
		for (int i=0; i<temp.size(); i++) {
			for (int j = temp.size()-1; j>i; j--) {
				if (temp.get(j).getValue()>temp.get(j-1).getValue()) {
					Fruit ftemp = temp.get(j);
					temp.set(j, temp.get(j-1));
					temp.set(j-1, ftemp);
				}
			}
		}
		return temp;
	}

	private Fruit createFruitObject(String s)
	{
		try {
			JSONObject j = new JSONObject(s);
			Fruit f = new Fruit();
			String[] ssss = j.getString("pos").split(",");
			f.setLocation(new Point3D(Double.parseDouble(ssss[0]),Double.parseDouble(ssss[1])));
			return f;
		}
		catch(Exception e) {
			throw new RuntimeException ("Error");
		}
	}
	
	public static GameServer createGameServer (String s) {
		s = s.substring(14, s.length()-1);
		Gson g = new Gson();
		try {
			GameServer gs = g.fromJson(s, GameServer.class);
			return gs;
		}
		catch (Exception e) {
			throw new RuntimeException ("Error");
		}
	}
	
	public List<Robot> createRobotList() {
		List <String> robots = this.game.getRobots();
		List <Robot> temp = new ArrayList <Robot>();
		for (int i=0; i<robots.size();i++) {
			Robot r = creatRobotObject (robots.get(i));
			temp.add(r);
		}
		return temp;
	}

	private Robot creatRobotObject(String s) {
		s = s.substring(9, s.length()-1);
		Gson g = new Gson();
		try {
			Robot r = g.fromJson(s, Robot.class);
			return r;
		}
		catch (Exception e) {
			throw new RuntimeException ("Error"); 
		}
	}
	
}
