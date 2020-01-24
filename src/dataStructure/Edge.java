package dataStructure;

import java.io.Serializable;

public class Edge implements edge_data, Serializable {
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public Edge() {
    }

    public Edge(int src, int dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public Edge(Edge e)
    {
        this.src = e.src;
        this.dest = e.dest;
        this.weight = e.weight;
    }

    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }
    @Override
    public double getWeight() {
        return weight;
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
}