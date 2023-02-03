package subway.presentation.response;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Station;
import subway.domain.SubwayLine;

public class SubwayLineResponse {

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class CreateInfo {

		private Long id;

		private String name;

		private String color;

		private List<StationInfo> stations;

		public CreateInfo(SubwayLine subwayLine) {
			this.id = subwayLine.getId();
			this.name = subwayLine.getName();
			this.color = subwayLine.getColor();
			this.stations = toStationInfo(subwayLine.getUpAndDownStations());
		}

		private List<StationInfo> toStationInfo(List<Station> stations) {
			return stations.stream()
				.map(StationInfo::new)
				.collect(Collectors.toUnmodifiableList());
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class StationInfo {
		private Long id;

		private String name;

		public StationInfo(Station station) {
			this.id = station.getId();
			this.name = station.getName();
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class LineInfo {
		private Long id;

		private String name;

		private String color;

		private List<StationInfo> stations;

		public LineInfo(SubwayLine subwayLine) {
			this.id = subwayLine.getId();
			this.name = subwayLine.getName();
			this.color = subwayLine.getColor();

			this.stations = toStationInfos(subwayLine);
		}

		private List<StationInfo> toStationInfos(SubwayLine subwayLine) {
			return subwayLine.getUpAndDownStations()
				.stream()
				.map(StationInfo::new)
				.collect(Collectors.toUnmodifiableList());
		}

	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class UpdateInfo {

		private Long id;

		private String name;

		private String color;

		public UpdateInfo(SubwayLine subwayLine) {
			this.id = subwayLine.getId();
			this.name = subwayLine.getName();
			this.color = subwayLine.getColor();
		}
	}
}
