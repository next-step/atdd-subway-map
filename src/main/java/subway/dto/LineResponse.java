package subway.dto;

import subway.entity.Line;
import subway.entity.Station;

import java.util.List;

public class LineResponse {
	private Long id;

	private String name;

	private String color;

	private List<Station> staions;

	public LineResponse(Line line, List<Station> stations) {
		this.id = line.getId();
		this.name = line.getName();
		this.color = line.getColor();
		this.staions = stations;
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
}
