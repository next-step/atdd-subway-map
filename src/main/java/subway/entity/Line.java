package subway.entity;

import javax.persistence.*;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String color;

	private Long startStationId;

	private Long endStationId;

	private int distance;

	public Line() {

	}

	public Line(String name, String color, Long startStationId, Long endStationId, int distance) {
		this.name = name;
		this.color = color;
		this.startStationId = startStationId;
		this.endStationId = endStationId;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Long getStartStationId() {
		return startStationId;
	}

	public Long getEndStationId() {
		return endStationId;
	}

	public int getDistance() {
		return distance;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setUpdateInfo(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public void addSection(Long stationId, int distance) {
		this.endStationId = stationId;
		this.distance = this.distance + distance;
	}
}
