package subway.line;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import subway.Station;

@Entity
public class Line {

	@Id
	@Column(name = "line_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String name;

	@Column(length = 30, nullable = false)
	private String color;

	@ManyToOne
	private Station upStationId;

	@ManyToOne
	private Station downStationId;

	@Column(length = 20, nullable = false)
	private int distance;

	public Line() {
	}

	public Line(String name, String color, Station upStationId, Station downStationId, int distance) {
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public void updateLine(String name, String color) {
		this.name = name;
		this.color = color;
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

	public List<Station> getStations() {
		List<Station> stations = new ArrayList<>();
		stations.add(upStationId);
		stations.add(downStationId);
		return stations;
	}

	public int getDistance() {
		return distance;
	}
}
