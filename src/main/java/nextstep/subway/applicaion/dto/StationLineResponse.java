package nextstep.subway.applicaion.dto;

import java.util.Arrays;
import java.util.List;

public class StationLineResponse {
	private Long id;
	private String name;
	private String color;
	private List<StationResponse> stations;

	public StationLineResponse(Long id, String name, String color, StationResponse upStationResponse,
		StationResponse downStationResponse) {

		this.id = id;
		this.name = name;
		this.color = color;
		this.stations = Arrays.asList(upStationResponse, downStationResponse);
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
