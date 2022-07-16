package nextstep.subway.line.application.dto;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.line.Line;
import nextstep.subway.station.Station;
import nextstep.subway.station.applicaion.dto.StationResponse;

public class LineResponse {

	private final Long id;
	private final String name;
	private final String color;
	private final List<StationResponse> stations = new ArrayList<>();

	public LineResponse(Line line, Station upStation, Station downStation) {
		this.id = line.getId();
		this.name = line.getName();
		this.color = line.getColor();
		this.stations.add(new StationResponse(upStation.getId(), upStation.getName()));
		this.stations.add(new StationResponse(downStation.getId(), downStation.getName()));
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
