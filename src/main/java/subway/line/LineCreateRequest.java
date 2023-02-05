package subway.line;

public class LineCreateRequest {
	private String name;
	private String color;
	private Long upStationsId;
	private Long downStationsId;
	private int distance;

	public LineCreateRequest(String name, String color, Long upStationsId, Long downStationsId, int distance) {
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
