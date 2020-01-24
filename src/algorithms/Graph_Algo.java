package algorithms;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import dataStructure.*;
import utils.StdDraw;

/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 *
 * @author
 */

public class Graph_Algo implements graph_algorithms {
    private graph graph;

    public Graph_Algo() {
    }

    public Graph_Algo(graph g) {
        init(g);
    }

    @Override
    public void init(graph g) {
        this.graph = g;
    }

    @Override
    public graph copy() {
        if (this.graph == null)
            throw new RuntimeException("Graph_Algo not initiate");
        return new DGraph((DGraph) this.graph);

    }

    @Override
    public void init(String file_name) {
        try {
            FileInputStream streamIn = new FileInputStream(file_name);
            ObjectInputStream obj = new ObjectInputStream(streamIn);
            DGraph readCase = (DGraph) obj.readObject();
            StdDraw.clear();
            graph = readCase;
            streamIn.close();
            obj.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(String file_name) {
        try {
            FileOutputStream out = new FileOutputStream(file_name + ".ser");
            ObjectOutputStream oos = new ObjectOutputStream(out);
            if (graph == null) return;
            oos.writeObject(graph);
            oos.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
    }

    @Override
    public boolean isConnected() {
        ArrayList<node_data> nodes = (ArrayList<node_data>) this.graph.getV();
        if (nodes.size() <= 1)
            return true;
        node_data src = nodes.get(0);
        nodes.remove(0);
        for (node_data n : nodes) {
            double go = shortestPathDist(src.getKey(), n.getKey());
            double back = shortestPathDist(n.getKey(), src.getKey());
            if (go == Double.MAX_VALUE || back == Double.MAX_VALUE)
                return false;
        }
        return true;
    }

    private int minDistance(double dist[], boolean used[]) {

        double min = Double.MAX_VALUE;
        int min_index = -1;
        int V = this.graph.nodeSize();
        for (int v = 0; v < V; v++)
            if (used[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }

        return min_index;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        // TODO Auto-generated method stub
        if (graph.getNode(src) == null || graph.getNode(dest) == null) {
            throw new RuntimeException("One of The vertex is not exists");
        }
        int V = this.graph.nodeSize();
        double dist[] = new double[V];
        boolean used[] = new boolean[V];
        ArrayList<node_data> nodes = (ArrayList<node_data>) this.graph.getV();
        //initiate values
        for (int i = 0; i < V; i++) {
            dist[i] = Double.MAX_VALUE;
            used[i] = false;
        }
        dist[nodes.indexOf(graph.getNode(src))] = 0;
        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, used); //index of u in the array
            used[u] = true;
            HashMap<Integer, edge_data> edges = ((Node) nodes.get(u)).getEdges();
            for (Map.Entry<Integer, edge_data> edge : edges.entrySet()) {
                int adjindex = nodes.indexOf(graph.getNode(edge.getValue().getDest()));
                if (dist[u] + edge.getValue().getWeight() < dist[adjindex]) {
                    dist[adjindex] = dist[u] + edge.getValue().getWeight();
                }
            }
        }
        return dist[nodes.indexOf(graph.getNode(dest))];

    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (graph.getNode(src) == null || graph.getNode(dest) == null) {
            throw new RuntimeException("One of The vertex is not exists");
        }
        int V = this.graph.nodeSize();
        double dist[] = new double[V];
        int N[] = new int[V];
        boolean used[] = new boolean[V];
        ArrayList<node_data> nodes = (ArrayList<node_data>) this.graph.getV();
        //initiate values
        for (int i = 0; i < V; i++) {
            dist[i] = Double.MAX_VALUE;
            used[i] = false;
            N[i] = -1;
        }
        dist[nodes.indexOf(graph.getNode(src))] = 0;
        for (int count = 0; count < V - 1; count++) {
            int u = minDistance(dist, used); //index of u in the array
            used[u] = true;
            HashMap<Integer, edge_data> edges = ((Node) nodes.get(u)).getEdges();
            for (Map.Entry<Integer, edge_data> edge : edges.entrySet()) {
                int adjindex = nodes.indexOf(graph.getNode(edge.getValue().getDest()));
                if (dist[u] + edge.getValue().getWeight() < dist[adjindex]) {
                    dist[adjindex] = dist[u] + edge.getValue().getWeight();
                    N[adjindex] = u;
                }
            }
        }

        List<node_data> list1 = new LinkedList<>();
        int f = nodes.indexOf(graph.getNode(dest));
        if (dist[f] != Double.MAX_VALUE) {
            while (N[f] != -1) {
                list1.add(nodes.get(f));
                f = N[f];
            }

            list1.add(nodes.get(f));

            List<node_data> list2 = new LinkedList<>();
            for (int i = list1.size() - 1; i >= 0; i--) {
                list2.add(list1.get(i));
            }
          /* for (node_data n : list2) {
                System.out.print(n.getKey() + " ");
            }
            System.out.println();
            System.out.println(dist[nodes.indexOf(graph.getNode(dest))]);*/
            return list2;
        }
        return new LinkedList<>();
    }

    public static List<Integer> clone(List<Integer> source) {
        List<Integer> newList = new ArrayList<>();
        for (Integer intObj : source) {
            newList.add(intObj);
        }
        return newList;
    }

    @Override
    public List<node_data> TSP(List<Integer> targets) {
        ArrayList<Integer> ALtargets = (ArrayList<Integer>) clone(targets);
        int first = -1, last = -1;
        List<node_data> finallist = new ArrayList<node_data>();
        double min = Double.MAX_VALUE;
        for (int src : targets) {
            int index = ALtargets.indexOf(new Integer(src));
            ALtargets.remove(new Integer(src));
            List<node_data> newl = new ArrayList<node_data>();
            newl.add(graph.getNode(src));
            double[] tsp = TSP(src, ALtargets, newl);
            if (tsp[1] < min) {
                finallist = newl;
                first = src;
                last = (int) tsp[0];
                min = tsp[1];
            }
            ALtargets.add(index, src);
        }
        if (first == -1 || last == -1 || targets.size() > finallist.size())
            return new ArrayList<node_data>();

        return finallist;
    }

    public double[] TSP(int src, List<Integer> rest, List<node_data> newl) {
        rest = clone(rest);
        if (rest.size() == 0) {
            double[] ans = {src, 0};
            return ans;
        }
        double min = Double.MAX_VALUE;
        int last = -1;
        for (int dest : rest) {
            List<Integer> temp = clone(rest);
            temp.remove(new Integer(dest));
            double dist = shortestPathDist(src, dest);
            double[] tsp = TSP(dest, temp, new ArrayList<node_data>());
            if (tsp[0] != -1 && tsp[1] + dist < min) {
                min = tsp[1] + dist;
                last = dest;
            }
        }
        if (last == -1) {
            double[] ans = {-1, Double.MAX_VALUE};
            return ans;
        }
        rest.remove(new Integer(last));


        if (newl.size() != 0) {
                newl.remove(newl.size() - 1);
                for (node_data n : shortestPath(src, last)) {
                    newl.add(n);
                }
        }
        double[] ans = TSP(last, rest, newl);
        ans[1] += min;
        return ans;

    }
}