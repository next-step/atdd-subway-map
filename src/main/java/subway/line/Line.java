package subway.line;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Line {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String name;

	@Column(length = 20, nullable = false)
	private String color;

	@Column(nullable = false)
	private Integer distance;

	@OneToMany(mappedBy = "line")
	private final List<LineStationMap> lineStationMaps = new ArrayList<>();

	public Line() {
	}

	public Line(String name, String color, Integer distance) {
		this.name = name;
		this.color = color;
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

	public Integer getDistance() {
		return distance;
	}

	public List<LineStationMap> getLineStationMaps() {
		return lineStationMaps;
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void changeColor(String color) {
		this.color = color;
	}
}
