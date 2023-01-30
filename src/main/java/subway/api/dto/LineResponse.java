package subway.api.dto;

import subway.domain.station.entity.Station;

import java.util.List;

public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private List<Station> stations;

	public LineResponse(Long id, String name, String color, List<Station> stations) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.stations = stations;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<Station> getStations() {
		return stations;
	}

	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
}
