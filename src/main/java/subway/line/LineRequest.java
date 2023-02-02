package subway.line;

public class LineRequest {
	public String name;
	public String color;
	public Long upStationsId;
	public Long downStationsId;
	public int distance;

	public LineRequest(String name, String color, Long upStationsId, Long downStationsId, int distance) {
		this.name = name;
		this.color = color;
		this.upStationsId = upStationsId;
		this.downStationsId = downStationsId;
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Long getUpStationsId() {
		return upStationsId;
	}

	public Long getDownStationsId() {
		return downStationsId;
	}

	public int getDistance() {
		return distance;
	}
}
