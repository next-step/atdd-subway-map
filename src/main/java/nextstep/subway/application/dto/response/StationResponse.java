package nextstep.subway.application.dto.response;

import nextstep.subway.domain.Sections;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class StationResponse {
	private Long id;
	private String name;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;

	private StationResponse() {
	}

	private StationResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime modifiedDate) {
		this.id = id;
		this.name = name;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}

	public static StationResponse from(Station station) {
		return new StationResponse(
				station.getId(),
				station.getName(),
				station.getCreatedDate(),
				station.getModifiedDate());
	}

	public static List<StationResponse> fromList(List<Station> stations) {
		return stations.stream()
				.map(StationResponse::from)
				.collect(toList());
	}

	public static List<StationResponse> ofSections(Sections sections) {
		return sections.getStations()
				.stream()
				.map(StationResponse::from)
				.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}
}
