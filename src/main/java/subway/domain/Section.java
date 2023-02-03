package subway.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.InvalidSectionDistanceException;
import subway.exception.SectionErrorCode;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {

	private static final int INVALID_SECTION_DISTANCE = 0;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "SUBWAY_LINE_ID")
	private SubwayLine subwayLine;

	@ManyToOne
	@JoinColumn(name = "UP_STATION_id")
	private Station upStation;

	@ManyToOne
	@JoinColumn(name = "DOWN_STATION_id")
	private Station downStation;

	private int distance;

	public Section(Station upStation, Station downStation, int distance, SubwayLine subwayLine) {
		if (distance <= INVALID_SECTION_DISTANCE) {
			throw new InvalidSectionDistanceException(SectionErrorCode.INVALID_SECTION_DISTANCE);
		}

		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
		this.subwayLine = subwayLine;
	}

	public Section(Long upStationId, Long downStationId, int distance, List<Station> stations) {

		Map<Long, Station> stationMap = toMap(stations);

		this.upStation = stationMap.get(upStationId);
		this.downStation = stationMap.get(downStationId);
		this.distance = distance;
	}

	public boolean isEqualUpStation(Station station) {
		return this.upStation.equals(station);
	}

	public boolean hasStation(Station target) {
		return this.upStation.equals(target) || this.downStation.equals(target);
	}

	public void connectSubwayLine(SubwayLine subwayLine) {
		this.subwayLine = subwayLine;
	}

	private Map<Long, Station> toMap(List<Station> stations) {
		return stations.stream()
			.collect(Collectors.toMap(Station::getId, Function.identity()));
	}
}
