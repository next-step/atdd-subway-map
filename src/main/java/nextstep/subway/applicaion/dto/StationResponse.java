package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.domain.Station;

public class StationResponse {
	private Long id;
	private String name;

	public StationResponse(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public static List<StationResponse> fromList(List<Station> stationList) {
		return stationList.stream()
			.map(station -> new StationResponse(station.getId(), station.getName()))
			.collect(Collectors.toList());
	}
}
