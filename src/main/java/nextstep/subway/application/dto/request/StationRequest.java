package nextstep.subway.application.dto.request;

import nextstep.subway.domain.Station;

public class StationRequest {
	private String name;

	private StationRequest() {
	}

	private StationRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Station toEntity() {
		return Station.of(name);
	}
}
