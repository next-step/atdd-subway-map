package nextstep.subway.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String color;
	@OneToMany
	private List<Station> stations;

	public Line() {
	}

	public Line(String name, String color, List<Station> stations) {
		this.name = name;
		this.color = color;
		this.stations = stations;
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
		return stations;
	}
}
