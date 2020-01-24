package gameClient;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class Robot {

	private int id;
	private String location;
	private int src, dest;
	private double value, speed;
    
    public Robot(int id, String location, int src, int dest, double value, double speed) {
		super();
		this.id = id;
		this.location = location;
		this.src = src;
		this.dest = dest;
		this.value = value;
		this.speed = speed;
	}

	public Robot() {
	}

	public int getId() {
		return id;
	}

	public int getSrc() {
		return src;
	}

	public int getDest() {
		return dest;
	}

	public double getValue() {
		return value;
	}

	public double getSpeed() {
		return speed;
	}
	
    public Point3D getLocation() {
    	Point3D p=new Point3D(this.location);
		return p;
    }
    
    public void setSrc(int src) {
		this.src=src;
	}
    
    public void setDest(int dest) {
		this.dest=dest;
	}
    
    public void setPos(Point3D p) {
		this.location=""+p.x()+","+p.y()+","+p.z();
	}
    
    public edge_data edgeRobotOn (graph g) {
    	double min = Double.POSITIVE_INFINITY;
    	edge_data ans = null;
    	for (Iterator<node_data> ndi = g.getV().iterator();ndi.hasNext();) {
    		int n = ndi.next().getKey();
    		try {
    			for (Iterator <edge_data> edi = g.getE(n).iterator(); edi.hasNext();) {
    				edge_data ed = edi.next();
    				double dist1 = this.getLocation().distance2D(g.getNode(ed.getSrc()).getLocation());
    				double dist2 = this.getLocation().distance2D(g.getNode(ed.getDest()).getLocation());
    				double dist3 = g.getNode(ed.getSrc()).getLocation().distance2D(g.getNode(ed.getDest()).getLocation());
    				if (Math.abs((dist1+dist2)-dist3)<=min) {
    					min = Math.abs((dist1+dist2)-dist3);
    					ans = ed;
    				}
    			}
    		}
    		catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    	if (ans.getSrc()-ans.getDest()<0)
    		return ans;
    	ans = g.getEdge(ans.getDest(), ans.getSrc());
    	return ans;
    }
}
