package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class LineResponse {
	private Long id;
	private String name;
	private String color;
	private List<LineStationResponse> stations;

	@Builder
	public LineResponse(Long id, String name, String color, List<LineStationResponse> stations) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.stations = stations;
	}

	public static LineResponse of(Line line, List<Station> stations) {
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
	@NoArgsConstructor
	public static class LineStationResponse {
		private Long id;
		private String name;

		@Builder
		public LineStationResponse(Long id, String name) {
			this.id = id;
			this.name = name;
		}

		public static LineStationResponse of(Station station) {
			return LineStationResponse.builder()
				.id(station.getId())
				.name(station.getName())
				.build();
		}
	}
}
