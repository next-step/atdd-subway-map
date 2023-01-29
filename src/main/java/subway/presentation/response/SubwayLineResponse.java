package subway.presentation.response;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Station;
import subway.domain.SubwayLine;
import subway.domain.SubwayLineStationGroup;

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
			this.stations = toStationInfos(subwayLine.getSubwayLineStationGroups());
		}

		private static List<StationInfo> toStationInfos(List<SubwayLineStationGroup> subwayLineStationGroups) {
			return subwayLineStationGroups.stream()
				.map(subwayLineStationGroup -> new StationInfo(subwayLineStationGroup.getStation()))
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
			this.stations = toStationInfos(subwayLine.getSubwayLineStationGroups());
		}

		private static List<StationInfo> toStationInfos(List<SubwayLineStationGroup> subwayLineStationGroups) {
			return subwayLineStationGroups.stream()
				.map(subwayLineStationGroup -> new StationInfo(subwayLineStationGroup.getStation()))
				.collect(Collectors.toUnmodifiableList());
		}
	}
}
