package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private List<LineStationResponse> stations;

	@Builder
	private LineResponse(Long id, String name, String color, List<LineStationResponse> stations) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.stations = stations;
	}

	public static LineResponse of(@NonNull Line line, List<Station> stations) {
		List<LineStationResponse> stationResponses = stations.stream()
			.map(LineStationResponse::of)
			.collect(Collectors.toList());

		return LineResponse.builder()
			.id(line.getId())
			.name(line.getName())
			.color(line.getColor())
			.stations(stationResponses)
			.build();
	}


	@Getter
	public static class LineStationResponse {
		private Long id;
		private String name;

		@Builder
		private LineStationResponse(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		public static LineStationResponse of(@NonNull Station station) {
			return LineStationResponse.builder()
				.id(station.getId())
				.name(station.getName())
				.build();
		}
	}
}
