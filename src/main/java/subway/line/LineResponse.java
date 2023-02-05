package subway.line;

import java.util.List;
import java.util.stream.Collectors;

import subway.station.StationResponse;

public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private List<StationResponse> stations;

	public LineResponse(Line line) {
		this.id = line.getId();
		this.name = line.getName();
		this.color = line.getColor();
		this.stations = line.getAllStation().stream().map(StationResponse::new).collect(Collectors.toList());
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

	public List<StationResponse> getStations() {
		return stations;
	}
}
