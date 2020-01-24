package gui;

import algorithms.Graph_Algo;
import dataStructure.*;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;
import utils.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class Graph_GUI implements Runnable {
    private static double max_x, min_x, min_y, max_y;//parameters to hold max/min coordinates
    //public Graph_GUI() {
    // StdDraw.setCanvasSize(600, 600);
    // }
    private static graph lastGraph;
    private static int mc = -1;

    public static Range getX() {
        return new Range(min_x, max_x);
    }

    public static Range getY() {
        return new Range(min_y, max_y);
    }
    /**
     * saves last graph to a given file
     * @param filename
     * @throws IOException
     */
    public static void save(String filename) throws IOException {
        FileOutputStream out = new FileOutputStream(filename+".ser");
        ObjectOutputStream oos = new ObjectOutputStream(out);
        if(lastGraph == null)return;
        oos.writeObject(lastGraph);
        oos.close();
        out.close();
    }

    /**
     * loads and draws from given file
     * @param filename
     * @throws Exception
     */
    public static void load(String filename)throws Exception {
        FileInputStream streamIn = new FileInputStream(filename);
        ObjectInputStream obj = new ObjectInputStream(streamIn);
        DGraph readCase = (DGraph) obj.readObject();
        StdDraw.clear();
        draw(readCase);
        lastGraph = readCase;
        streamIn.close();
        obj.close();
    }

    public static graph getLastGraph() {
        return lastGraph;
    }

    public static void setLastGraph(graph g) {
        lastGraph = (DGraph) g;
    }
    //auxiliary to find scale
    private static void setScale(graph graph)
    {
        Collection<node_data> c = graph.getV();
        Iterator<node_data> iterator = c.iterator();
        double max_x,min_x,max_y,min_y;
        if(iterator.hasNext()) {
            Point3D p = iterator.next().getLocation();
            max_x = p.x();
            min_x= p.x();
            max_y=p.y();
            min_y=p.y();
        }
        else {
            return;
        }
        while (iterator.hasNext()) {
            Point3D p = iterator.next().getLocation();
            if(p.x() < min_x)
            {
                min_x = p.x();
            }
            else if(p.x() > max_x)
            {
                max_x = p.x();
            }
            if(p.y() < min_y)
            {
                min_y = p.y();
            }
            else if(p.y() > max_y)
            {
                max_y = p.y();
            }
        }
        double prec = 0.00001;
        max_x = max_x + max_x * prec;
        min_x = min_x - min_x * prec;
        max_y = max_y + max_y * prec;
        min_y = min_y - min_y * prec;
        StdDraw.setXscale(min_x - min_x * prec, max_x + max_x * prec);
        StdDraw.setYscale(min_y - min_y * prec, max_y + max_y * prec);
    }
    /**
     * draws given graph in GUI
     * @param graph
     */
    public static void draw(graph graph)
    {
        Graph_GUI.mc = graph.getMC();
        setScale(graph);
        Collection<node_data> c = graph.getV();
        Iterator<node_data> iterator = c.iterator();
        StdDraw.setPenRadius(0.005);
        iterator = c.iterator();
        while (iterator.hasNext())
        {
            node_data n =iterator.next();
            Iterator<edge_data> e = graph.getE(n.getKey()).iterator();
            while (e.hasNext())
            {
                edge_data ed = e.next();
                Point3D src = n.getLocation();
                Point3D dest = graph.getNode(ed.getDest()).getLocation();
                double w = ed.getWeight() ;
                StdDraw.setPenColor(Color.red);
                StdDraw.line(src.x(), src.y(),dest.x(),dest.y());
                StdDraw.setPenColor(Color.black);
                double pointx = src.x() + (dest.x() - src.x()) * 0.95, pointy = src.y() + (dest.y() - src.y()) * 0.95;
                double point2x = src.x() + (dest.x() - src.x()) * 0.91, point2y = src.y() + (dest.y() - src.y()) * 0.91;
                double m = Math.tan(0.26) * (point2y - pointy) / (point2x - pointx);
                double m2 = Math.tan(Math.PI - 0.26) * (point2y - pointy) / (point2x - pointx);
                double newl = m * (point2x - pointx) + pointy;
                double newr = m2 * (point2x - pointx) + pointy;
                StdDraw.line(pointx, pointy, point2x, newl);
                StdDraw.line(pointx, pointy, point2x, newr);
                //StdDraw.filledSquare(src.x()+(dest.x()-src.x())*0.95,src.y()+(dest.y()-src.y())*0.95,0.00008);
                StdDraw.text(src.x() + (dest.x() - src.x()) * 0.8, src.y() + (dest.y() - src.y()) * 0.8, String.format("%.2f", w));
            }
        }
        iterator = c.iterator();
        StdDraw.setPenRadius(0.03);
        while (iterator.hasNext()) {
            StdDraw.setPenColor(Color.blue);
            node_data n = iterator.next();
            Point3D p = n.getLocation();
            StdDraw.point(p.x(), p.y());
            StdDraw.setPenColor(Color.BLACK);
            String s = n.getKey() + "";
            StdDraw.text(p.x(), p.y(), s);
        }
        lastGraph = graph;
    }


    @Override
    public void run() {
        draw(lastGraph);
        /*
        while (true) {
            synchronized (lastGraph) {
                if (mc < lastGraph.getMC()) {
                    StdDraw.clear();
                    draw(lastGraph);
                } else {
                    try {
                        Thread.sleep(1500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
         */
    }
}