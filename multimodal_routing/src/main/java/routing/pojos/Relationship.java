package routing.pojos;

import java.io.Serializable;
import java.util.Map;

public class Relationship implements Serializable {
	private Node source;
	private Node target;
	private ShipmentMode shipmentMode;
	private Map<PathProperties, Double> properties;

	public Relationship(Node source, Node target, ShipmentMode shipmentMode, Map<PathProperties, Double> properties) {
		this.properties = properties;
		this.shipmentMode = shipmentMode;
		this.source = source;
		this.target = target;
	}

	public Node getSource() {
		return source;
	}

	public Node getTarget() {
		return target;
	}

	public double getDistance() {
		return properties.get(PathProperties.DISTANCE);
	}

	public void setDistance(double distance) {
		properties.put(PathProperties.DISTANCE, distance);
	}

	public ShipmentMode getMode() {
		return shipmentMode;
	}

	public void setMode(ShipmentMode mode) {
		this.shipmentMode = mode;
	}

	public double getCost() {
		return properties.get(PathProperties.COST);
	}

	public void setCost(double cost) {
		properties.put(PathProperties.COST, cost);
	}

	public double getCoEmission() {
		return properties.get(PathProperties.CO_EMISSION);
	}

	public void setCoEmission(double coEmission) {
		properties.put(PathProperties.CO_EMISSION, coEmission);
	}

	public double getDuration() {
		return properties.get(PathProperties.DURATION);
	}

	public void setDuration(double duration) {
		properties.put(PathProperties.DURATION, duration);
	}

	public double getCapacity() {
		return (double) properties.get(PathProperties.CAPACITY);
	}

	public void setCapacity(double capacity) {
		properties.put(PathProperties.CAPACITY, capacity);
	}

	public Map<PathProperties, Double> getPathProperties() {
		return properties;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		result = prime * result + ((shipmentMode == null) ? 0 : shipmentMode.hashCode());
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
		Relationship other = (Relationship) obj;
		if (properties == null) {
			if (other.properties != null)
				return false;
		} else if (!properties.equals(other.properties))
			return false;
		if (shipmentMode != other.shipmentMode)
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
