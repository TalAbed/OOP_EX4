package dataStructure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class DGraph implements graph, Serializable {
    private HashMap<Integer , node_data> hashMap;
    private int MC;
	private int numberOfEdges;
    public DGraph()
    {
        hashMap = new HashMap<>();
        MC =0;
		numberOfEdges = 0;
    }

	/**
	 * gets a json representing a graph and initializes a new DGraph from it
	 * @param json - json string to init from
	 */
	public void init(String json){
		try {

			JSONObject js = new JSONObject(json);
			JSONArray Edges = js.getJSONArray("Edges");
			JSONArray Nodes = js.getJSONArray("Nodes");
			int id = -1;
			for (int i = 0; i <Nodes.length() ; i++) {
				JSONObject n = Nodes.getJSONObject(i);
				String[] pos = n.getString("pos").split(",");
				id = n.getInt("id");
				hashMap.put(id,new Node(id,new Point3D(Double.parseDouble(pos[0]),Double.parseDouble(pos[1])),0));
			}
			for (int i = 0; i <Edges.length() ; i++) {
				JSONObject e = Edges.getJSONObject(i);
				int src=e.getInt("src"),dest=e.getInt("dest");
				double w = e.getDouble("w");
				connect(src,dest,w);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public DGraph(DGraph dg)
	{
		HashMap<Integer, node_data> original = dg.hashMap;
		HashMap<Integer, node_data> copy = new HashMap<Integer, node_data>();
		for (Map.Entry<Integer, node_data> entry : original.entrySet())
		{
			copy.put(entry.getKey(), new Node((Node)entry.getValue()));
		}
		this.hashMap = copy;
		this.MC = dg.MC;
		this.numberOfEdges = dg.numberOfEdges;
	}

	@Override
	public node_data getNode(int key) {
		return (node_data) hashMap.get(key);
	}

	@Override
	public edge_data getEdge(int src, int dest) {
            Node n =(Node)(hashMap.get(src));
		return n.getEdge(dest);
	}

	@Override
	public void addNode(node_data n) {
		if (hashMap.get(n.getKey()) != null) {
			System.out.println("node already exist");
			return;
		}
		if (n instanceof Node) {
			numberOfEdges += ((Node) n).getNumofEdges();
		}
        hashMap.put(n.getKey(),n);
        MC++;
	}

	@Override
	public void connect(int src, int dest, double w) {
    	Edge e = new Edge(src,dest,w);
    	Node n = (Node)hashMap.get(src);
    	n.addEdge(e);
		numberOfEdges++;
    	MC++;
	}

	@Override
	public Collection<node_data> getV() {
		Collection<node_data> list = new ArrayList<node_data>(hashMap.values());
		return list;
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		Node node = (Node) hashMap.get(node_id);
		Collection<edge_data> list = new ArrayList<edge_data>(node.getEdges().values());
		return list;
	}

	public Collection<edge_data> getAllE() {
		ArrayList<node_data> nodes = new ArrayList<node_data>(hashMap.values());
		ArrayList<edge_data> edges = new ArrayList<edge_data>();
		for (node_data n:nodes) {
			edges.addAll(getE(n.getKey()));
		}
		return edges;
	}

	@Override
	public node_data removeNode(int key) {
		MC++;
		return hashMap.remove(key);
	}

	@Override
	public edge_data removeEdge(int src, int dest) {
    	Node n = (Node)hashMap.get(src);
    	MC++;
		numberOfEdges--;
		return n.removeEdge(dest);
	}

	@Override
	public int nodeSize() {
        return hashMap.size();
	}

	@Override
	public int edgeSize() {
		return numberOfEdges;
	}

	@Override
	public int getMC() {
		return MC;
	}
	public void save(String filename)
    {
        try {
            FileOutputStream out = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(this.hashMap);
            oos.writeObject(this.MC);
        }catch (Exception e){e.printStackTrace();}
    }

}