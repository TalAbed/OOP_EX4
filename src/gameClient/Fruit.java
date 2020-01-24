package gameClient;

import java.util.Iterator;

import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

public class Fruit{
    private double value;
    private int type;
    private String location;
    
    public Fruit(double value,int type,String pos) {
		this.value=value;
		this.type=type;
		this.location=pos;
	}
    
    public Fruit() {
	}

	public double getValue() {
		return value;
	}
    
    public int getType() {
		return type;
	}
    
    public Point3D getLocation() {
		Point3D p=new Point3D(location);
		return p;
	}
    
    public void setLocation(Point3D p) {
		this.location=""+p.x()+","+p.y()+","+p.z();
		System.out.println(this.location);
	}
    public void setValue(double d) {
    	this.value = d;
    }
    public void setType(int type) {
    	this.type =type;
    }
    public edge_data edgeFruitOn (graph g) {
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
