package subway.line;

import static java.util.stream.Collectors.*;

import java.util.List;

import subway.dto.StationDTO;
import subway.station.Station;

public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private List<StationDTO> stations;

	private LineResponse() {}

	private LineResponse(Long id, String name, String color, List<StationDTO> station) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.stations = station;
	}

	public static LineResponse of(Line line, List<Station> stations) {
		List<StationDTO> stationDto = stations.stream()
			.map(StationDTO::of)
			.collect(toList());
		return new LineResponse(line.getId(), line.getName(), line.getColor(), stationDto);
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

	public List<StationDTO> getStations() {
		return stations;
	}
}
