package dataStructure;

import utils.Point3D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Node implements node_data, Serializable {
    private int numofEdges;
    private int key;
    private Point3D p;
    private double weight;
    private String info;
    private int tag;
    public Node() {;}
    private HashMap<Integer,edge_data> edgeMap;
    public Node(int key,Point3D p,double weight)
    {
        this.key = key;
        this.p = new Point3D(p);
        edgeMap =new HashMap<>();
    }

    public int getNumofEdges()
    {
        return numofEdges;
    }
    public edge_data getEdge(int dest)
    {
        return edgeMap.get(dest);
    }
    public void addEdge(Edge e) {
        edgeMap.put(e.getDest(),e);
        numofEdges++;
    }

    public Node(Node n){
        this.key = n.key;
        this.p = new Point3D(n.p.x(),n.p.y());
        this.info = n.info;
        this.edgeMap = new HashMap<Integer,edge_data>();
        for (Map.Entry<Integer, edge_data> entry : n.edgeMap.entrySet())
        {
            this.edgeMap.put(entry.getKey(), new Edge((Edge)entry.getValue()));
        }
    }

    public HashMap<Integer, edge_data> getEdges() {
        return edgeMap;
    }

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public Point3D getLocation() {
        return p;
    }

    @Override
    public void setLocation(Point3D p) {
        this.p = new Point3D(p.x(),p.y());
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        this.info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }


    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    public edge_data removeEdge(int dest) {
        numofEdges--;
       return edgeMap.remove(dest);
    }
}