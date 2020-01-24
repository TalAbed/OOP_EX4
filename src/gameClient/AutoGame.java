package gameClient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;

public class AutoGame {
	
	private game_service game;
	private DGraph dg;
	private Graph_Algo ga;
	private List<Fruit> fruits;
	private List<Robot> robots;

	public AutoGame (game_service game, List<Fruit> fruits, List<Robot> robots) {
		this.game = game;
		this.robots = robots;
		this.fruits = fruits;
		dg = new DGraph();
		dg.init(game.getGraph());
		ga = new Graph_Algo();
		ga.init(dg);
	}
	
	public void moveRobots () {
		List <Integer> dests = new ArrayList<Integer>();
		List <String> l = game.move();
		if (l!=null) {
			long timeLeft = game.timeToEnd();
			for (int i=0; i<l.size(); i++) {
				dests.add(robots.get(i).getDest());
			}
			for (int i=0; i<l.size(); i++) {
				String r = l.get(i);
				try {
					JSONObject obj = new JSONObject (r);
					JSONObject obj1 = obj.getJSONObject("robots");
					int id = obj1.getInt("id");
					int src = obj1.getInt("src");
					int dest = obj1.getInt("dest");
					if (dest == 1) {
						dest = nodeToMove (src, dests);
						dests.add(dest);
						game.chooseNextEdge(id, dest);
						System.out.println("Robot " + id + " is moving to node" + dest + ", time letf : " + (timeLeft/1000));
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int nodeToMove(int src, List<Integer> dests) {
		double min = Double.POSITIVE_INFINITY;
		edge_data ed = null;
		int ans = 0;
		boolean b = false;
		for (int i=0;i<fruits.size();i++) {
			double dist;
			List<node_data> nodes;
			int dest;
			ed = fruits.get(i).edgeFruitOn(dg);
			if (fruits.get(i).getType()==1) {
				dist = ga.shortestPathDist(src, ed.getDest());
				nodes = ga.shortestPath(src, ed.getSrc());
				if (nodes.size()==1)
					dest = ed.getDest();
				else
					dest = nodes.get(1).getKey();
			}
			else {
				dist = ga.shortestPathDist(src, ed.getDest());
				nodes = ga.shortestPath(src, ed.getDest());
				if (nodes.size()==1)
					dest = ed.getSrc();
				else
					dest = nodes.get(1).getKey();
			}
			if (dist<min && !dests.contains(dest)) {
				b = true;
				min = dist;
				ans = dest;
			}
		}
		if (b)
			return ans;
		else {
			ed = fruits.get(0).edgeFruitOn(dg);
			if (src == ed.getSrc())
				return ed.getDest();
			else {
				if (src == ed.getDest())
					return ed.getSrc();
			}
			List<node_data> ndl=ga.shortestPath(src, ed.getSrc());
			return (ndl.get(1).getKey());
		}
	}
	
	public  void update(game_service game,List<Fruit> fruits,List<Robot> robots)
	{
		this.game=game;
		this.fruits=fruits;
		this.robots=robots;
	}

}
