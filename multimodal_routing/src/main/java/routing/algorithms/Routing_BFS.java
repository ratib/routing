package routing.algorithms;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import routing.pojos.Graph;
import routing.pojos.Node;
import routing.pojos.Path;
import routing.pojos.PathProperties;
import routing.pojos.Relationship;

public class Routing_BFS {
	private Map<Relationship, Boolean> marked;
	private Map<Node, List<Relationship>> edgeTo;
	private Node source;
	private List<Path> paths;
	private double capacityConstrained;
	private boolean loopFlag = false;

	public Routing_BFS(Graph g, Node source, double capacityConstrained) {
		if (!g.getNodes().contains(source)) {
			System.out.println("Source Nod doesn't exist");
		}
		this.capacityConstrained = capacityConstrained;
		this.paths = new ArrayList<>();
		marked = new HashMap<>(g.getRelationships().size());
		edgeTo = new HashMap<>(g.getNodes().size());
		this.source = source;
		bfs(g, source);

	}

	public List<Path> getPathTo(Node target, PathProperties sortingProp, int maxRoutes) {

		if (!edgeTo.containsKey(target)) {
			return null;
		}
		if (target.equals(source)) {
			paths.add(new Path(source, target, new Stack<>()));
			return paths;
		}
		findPathTo(target, new Stack<>());
		Collections.sort(paths, new Comparator<Path>() {

			@Override
			public int compare(Path o1, Path o2) {
				double val1 = (double) o1.getProperties().get(sortingProp);
				double val2 = (double) o2.getProperties().get(sortingProp);
				return Double.compare(val1, val2);
			}

		});

		return paths.subList(0, Math.min(maxRoutes, paths.size()));
	}

	public Map<Node, List<Relationship>> getEdgeTo() {
		return edgeTo;
	}

	public List<Path> getPathTo(Node target) {

		return getPathTo(target, PathProperties.COST, 100);
	}

	private void findPathTo(Node target, Stack<Relationship> routes) {

		for (Relationship route : edgeTo.get(target)) {
			loopFlag = false;
			// this is to check for any loop

			for (Relationship r : routes) {
				if (route.getSource().equals(r.getTarget())) {
					loopFlag = true;
					break;
				}
			}

			// this is to check for self loop
			if (route.getSource().equals(target) || loopFlag == true) {
				continue;
			}

			routes.push(route);
			if (route.getSource().equals(source)) {
				Path path = new Path(source, target, routes);
				paths.add(path);
				routes.pop();

			} else {

				findPathTo(route.getSource(), routes);
				if (!routes.isEmpty()) {

					routes.pop();

				}
			}

		}

	}

	private void bfs(Graph g, Node source) {
		Queue<Node> q = new ArrayDeque<>();
		// mark all the paths in the graph as unvisited
		for (Relationship relationship : g.getRelationships()) {
			marked.put(relationship, false);
		}
		// initialize edgeTo for each node as an empty list
		for (Node node : g.getNodes()) {
			edgeTo.put(node, new ArrayList<Relationship>());

		}

		q.add(source);

		while (!q.isEmpty()) {
			Node node = q.poll();
			// for the node check all its outgoing relationships
			for (Relationship r : g.getAdjacentRelationships(node)) {
				if (!marked.get(r)) {
					marked.replace(r, true);
					// here capacity constrained is checked, if it satisfies then this route will be
					// added for further traversal
					if (r.getCapacity() >= capacityConstrained) {
						edgeTo.get(r.getTarget()).add(r);

						if (!q.contains(r.getTarget())) {
							q.add(r.getTarget());
						}
					}

				}
			}
		}
	}

}
