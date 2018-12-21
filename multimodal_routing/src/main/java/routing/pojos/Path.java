package routing.pojos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Path {
	private Node source;
	private Node target;
	private Stack<Relationship> routes;
	private Map<PathProperties, Double> properties;
	private Set<ShipmentMode> shipmentModes;

	public Path() {
		this.properties = new HashMap<>();
		this.shipmentModes = new HashSet<>();
	}

	public Path(Node source, Node target, Stack<Relationship> relationships) {
		this();
		this.source = source;
		this.target = target;
		this.routes = new Stack<Relationship>();
		this.routes.addAll(relationships);
		calculateMeasurements();
	}

	private void calculateMeasurements() {
		double cost = 0.0;
		double coEmission = 0.0;
		double distance = 0.0;
		double duration = 0.0;
		double capacity = Double.MAX_VALUE;
		for (Relationship route : routes) {
			shipmentModes.add(route.getMode());
			cost += route.getCost();
			coEmission += route.getCoEmission();
			distance += route.getDistance();
			capacity = Math.min(capacity, route.getCapacity());
			duration += route.getDuration();
		}
		properties.put(PathProperties.CO_EMISSION, coEmission);
		properties.put(PathProperties.DISTANCE, distance);
		properties.put(PathProperties.COST, cost);
		properties.put(PathProperties.DURATION, duration);
		properties.put(PathProperties.CAPACITY, capacity);

	}

	public Map<PathProperties, Double> getProperties() {
		return properties;
	}

	public Set<ShipmentMode> getRoutingMode() {
		return shipmentModes;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public Stack<Relationship> getRoutes() {
		return routes;
	}

	public void setRoutes(Stack<Relationship> routes) {
		this.routes = routes;
	}

	public double getCost() {
		return properties.get(PathProperties.COST);
	}

	public double getCoEmission() {
		return properties.get(PathProperties.CO_EMISSION);
	}

	public double getDistance() {
		return properties.get(PathProperties.DISTANCE);
	}

	public double getCapacity() {
		return properties.get(PathProperties.CAPACITY);
	}

	public double getDuration() {
		return properties.get(PathProperties.DURATION);
	}

	public String getRoutesAsString() {
		StringBuilder str = new StringBuilder(getSource().getName());
		for (int i = routes.size() - 1; i >= 0; i--) {
			Relationship r = routes.get(i);
			str.append(" -> " + r.getTarget().getName());
		}

		return str.toString();
	}

	public String getShipmentModesAsString() {
		StringBuilder str = new StringBuilder();
		for (ShipmentMode mode : shipmentModes) {
			str.append(mode + " ");
		}
		return str.toString();
	}

	public int getNumberOfChange() {
		return routes.size() - 1;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder(getSource().getName());

		for (int i = routes.size() - 1; i >= 0; i--) {
			Relationship r = routes.get(i);
			str.append(r.getSource().getName() + " - " + r.getMode() + " - " + r.getTarget().getName() + "  ");
		}
		str.append(" Cost " + this.getCost());
		str.append(" coEmission " + this.getCoEmission());
		return str.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((routes == null) ? 0 : routes.hashCode());
		result = prime * result + ((shipmentModes == null) ? 0 : shipmentModes.hashCode());
		result = prime * result + ((source == null) ? 0 : source.hashCode());
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (routes == null) {
			if (other.routes != null)
				return false;
		} else if (!routes.equals(other.routes))
			return false;
		if (shipmentModes == null) {
			if (other.shipmentModes != null)
				return false;
		} else if (!shipmentModes.equals(other.shipmentModes))
			return false;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

}
