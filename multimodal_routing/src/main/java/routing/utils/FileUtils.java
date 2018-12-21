package routing.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import routing.pojos.CustomerOrder;
import routing.pojos.Node;
import routing.pojos.Path;
import routing.pojos.PathProperties;
import routing.pojos.Relationship;
import routing.pojos.ShipmentMode;

public class FileUtils {
	// This is the csv file path, from which input data is read
	private static final String ROUTING_DATA_FILE_PATH = "routing_data.csv";
	// This the file created from the routing data contains all the node objects
	// (PORTS)
	private static final String NODE_OBJECT_FILE_PATH = "./src/main/resources/nodes";
	// This is the file created from the routing data contains all the relationship
	// objects (Routes)
	private static final String RELATIONSHIP_OBJECT_FILE_PATH = "./src/main/resources/relationships";
	// This is the file from which customer orders are read
	private static final String CUSTOMER_ORDER_FILE_PATH = "customer_orders";

	public FileUtils() {

	}

	public static void createNodeAndRelationshipFiles() {
		Set<Node> distinctNodes = new HashSet<>();
		Set<Relationship> distinctRelationships = new HashSet<>();
		try (InputStreamReader reader = new InputStreamReader(
				ClassLoader.getSystemResourceAsStream(ROUTING_DATA_FILE_PATH));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.EXCEL.withHeader().withIgnoreSurroundingSpaces())) {

			for (CSVRecord record : csvParser) {

				String src = record.get("source").toLowerCase().trim();
				String dest = record.get("destination").toLowerCase().trim();
				String shipmentMode = record.get("shipment_mode").toUpperCase().trim();
				double capacity = Double.valueOf(record.get("container_size"));
				double cost = Double.valueOf(record.get("fixed_freight_cost"));
				cost += Double.valueOf(record.get("port_handling_cost"));
				cost += Double.valueOf(record.get("fuel_cost"));
				cost += Double.valueOf(record.get("documentation_cost"));
				cost += Double.valueOf(record.get("equipment_cost"));
				cost += Double.valueOf(record.get("extra_cost"));
				cost += Double.valueOf(record.get("warehouse_cost"));
				double speed = Double.valueOf(record.get("speed"));
				double distance = Double.valueOf(record.get("distance"));
				double duration = Double.valueOf(record.get("custom_clearancetime_hours"));
				duration += Double.valueOf(record.get("port_handling_hours"));
				duration += Double.valueOf(record.get("extra_time_hours"));
				duration += distance / speed;
				double coEmission = Double.valueOf(record.get("co_emission")) * distance;

				Node srcNode = new Node(src);
				Node destNode = new Node(dest);
				Map<PathProperties, Double> properties = new HashMap<>();
				properties.put(PathProperties.CAPACITY, capacity);
				properties.put(PathProperties.CO_EMISSION, coEmission);
				properties.put(PathProperties.COST, cost);
				properties.put(PathProperties.DISTANCE, distance);
				properties.put(PathProperties.DURATION, duration);
				properties.put(PathProperties.SPEED, speed);
				distinctNodes.add(srcNode);
				distinctNodes.add(destNode);
				distinctRelationships
						.add(new Relationship(srcNode, destNode, getShipmentMode(shipmentMode), properties));

			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		List<Relationship> relationships = new ArrayList<>(distinctRelationships);
		List<Node> nodes = new ArrayList<>(distinctNodes);
		System.out.println("created nodes: " + nodes.size());
		System.out.println("created relationships: " + relationships.size());
		// Storing each list to files
		new SerializationHelper<List<Node>>().serialize(nodes, new File(NODE_OBJECT_FILE_PATH));
		new SerializationHelper<List<Relationship>>().serialize(relationships, new File(RELATIONSHIP_OBJECT_FILE_PATH));
	}

	private static ShipmentMode getShipmentMode(String str) {
		if (str.equals("SHIP")) {
			return ShipmentMode.SHIP;
		} else if (str.equals("AIR")) {
			return ShipmentMode.AIR;
		} else if (str.equals("TRUCK")) {
			return ShipmentMode.TRUCK;
		} else if (str.equals("RAIL")) {
			return ShipmentMode.RAIL;
		} else {
			return ShipmentMode.ANY;
		}
	}

	public static List<CustomerOrder> getCustomerOrders() {
		List<CustomerOrder> customerOrders = new ArrayList<>();
		try (InputStreamReader reader = new InputStreamReader(
				ClassLoader.getSystemResourceAsStream(CUSTOMER_ORDER_FILE_PATH));
				CSVParser csvParser = new CSVParser(reader,
						CSVFormat.EXCEL.withHeader().withIgnoreSurroundingSpaces())) {
			for (CSVRecord record : csvParser) {
				String srcName = record.get("source").toLowerCase().trim();
				String destName = record.get("destination").toLowerCase().trim();
				double capacity = Double.valueOf(record.get("capacity"));
				customerOrders.add(new CustomerOrder(srcName, destName, capacity));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerOrders;
	}

	public static List<Node> getNodes() {
		return new SerializationHelper<List<Node>>().deserialize(ClassLoader.getSystemResourceAsStream("nodes"));
	}

	public static List<Relationship> getRelationships() {
		return new SerializationHelper<List<Relationship>>()
				.deserialize(ClassLoader.getSystemResourceAsStream("relationships"));
	}

	public static void storeResult(List<Path> paths) {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("./src/main/resources/result.csv"));

				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("paths", "shipment_modes",
						"distance", "cost", "co_emission", "delivery_time", "num_of_changes"));) {
			for (Path p : paths) {
				csvPrinter.printRecord(p.getRoutesAsString(), p.getShipmentModesAsString(), p.getDistance(),
						p.getCost(), p.getCoEmission(), p.getDuration(), p.getNumberOfChange());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
