package routing.pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private List<Node> nodes;
    private List<Relationship> relationships;
    private Map<Node, List<Relationship>> adj;

    public Graph(List<Node> nodes) {
	this.nodes = new ArrayList<>(nodes);
	this.relationships = new ArrayList<>();
	this.adj = new HashMap<>(nodes.size());
	for (Node n : nodes) {
	    adj.put(n, new ArrayList<Relationship>());
	}
    }

    public Graph(List<Node> nodes, List<Relationship> relationships) {
	this.nodes = nodes;
	this.relationships = relationships;
	this.adj = new HashMap<>(nodes.size());
	for (Node n : nodes) {
	    adj.put(n, new ArrayList<Relationship>());
	}
	for (Relationship r : relationships) {
	    adj.get(r.getSource()).add(r);
	}
    }

    public boolean addAdjacentNodes(Node node, List<Relationship> relationships) {
	if (node == null || !nodes.contains(node) || relationships == null) {
	    return false;
	}
	relationships.addAll(relationships);
	return adj.get(node).addAll(relationships);
    }

    public boolean addAdjacentNode(Node node, Relationship relationship) {
	if (node == null || !nodes.contains(node) || relationship == null) {
	    return false;
	}
	relationships.add(relationship);
	return adj.get(node).add(relationship);
    }

    public boolean removeRelationship(Node node, Relationship relationship) {
	if (node == null || !nodes.contains(node) || relationship == null) {
	    return false;
	}
	relationships.remove(relationship);
	return adj.get(node).remove(relationship);
    }

    public List<Node> getAdjacentNodes(Node node) {
	List<Relationship> relationships = adj.get(node);
	List<Node> neighbors = new ArrayList<>(relationships.size());
	for (Relationship re : relationships) {
	    neighbors.add(re.getTarget());
	}
	return neighbors;
    }

    public List<Relationship> getAdjacentRelationships(Node node) {
	return adj.get(node);
    }

    public List<Node> getNodes() {
	return nodes;
    }

    public List<Relationship> getRelationships() {
	return relationships;
    }

    public Map<Node, List<Relationship>> getAdj() {
	return adj;
    }

}
