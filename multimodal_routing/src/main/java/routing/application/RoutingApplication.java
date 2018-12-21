package routing.application;

import java.util.List;

import routing.algorithms.Routing_BFS;
import routing.pojos.Graph;
import routing.pojos.Node;
import routing.pojos.Path;
import routing.pojos.PathProperties;
import routing.utils.FileUtils;
/**
 * RoutingApplication implements multimodal routing problem using BFS algorithm
 * The application creates list of nodes and relationships object files from a csv datasource.
 * Then Graph is created using the stored list of nodes and relationships. 
 * The algorithm requires the user to provide Graph object, source node and capacity of the load, then 
 * the algorithm traverse the graph and stores all the reachable nodes constraint to the provided capacity.
 * Then the user provides the target node, path property like cost, duration, co_emission etc on with the 
 * results will be sorted and maximum number of the paths to be shown. User can use the FileUtils to 
 * store the result inside resources folder.
 * @author RatibHussaini
 *
 */
public class RoutingApplication {

	public static void main(String[] args) {
		// Create list of node and relationship object files
		 FileUtils.createNodeAndRelationshipFiles();

		// Create Graph using created nodes and relationships object files
		Graph graph = new Graph(FileUtils.getNodes(), FileUtils.getRelationships());
		//initialize the bfs algorithm by passing the graph, source node and capacity of the load
		Routing_BFS bfs = new Routing_BFS(graph, new Node("singapore port"), 20);
		List<Path> paths = bfs.getPathTo(new Node("shanghai port"), PathProperties.COST, 50);
		FileUtils.storeResult(paths);

	}

}
