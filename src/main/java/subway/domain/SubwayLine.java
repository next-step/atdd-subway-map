package subway.domain;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.SectionErrorCode;
import subway.exception.SectionRegisterException;
import subway.exception.SectionRemoveException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class SubwayLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String color;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UP_STATION_ID")
	private Station upStation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOWN_STATION_ID")
	private Station downStation;

	@Embedded
	private final Sections sections = new Sections();

	public SubwayLine(
		String name,
		String color,
		Long upStationId,
		Long downStationId,
		int distance,
		List<Station> stations) {
		this.name = name;
		this.color = color;

		Map<Long, Station> stationMap = toMap(stations);

		this.upStation = stationMap.get(upStationId);
		this.downStation = stationMap.get(downStationId);
		this.sections.add(createInitialSection(distance));
	}

	public void updateInfo(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public List<Station> getUpAndDownStations() {
		return List.of(upStation, downStation);
	}

	private Map<Long, Station> toMap(List<Station> stations) {
		return stations.stream()
			.collect(Collectors.toMap(Station::getId, Function.identity()));
	}

	private Section createInitialSection(int distance) {
		return new Section(
			this.upStation,
			this.downStation,
			distance,
			this
		);
	}

	public void updateSection(Section section) {
		if (this.sections.hasStation(section.getDownStation())) {
			throw new SectionRegisterException(SectionErrorCode.ALREADY_STATION_REGISTERED);
		}

		if (!section.isEqualUpStation(this.downStation)) {
			throw new SectionRegisterException(SectionErrorCode.INVALID_SECTION_UP_STATION);
		}

		this.downStation = section.getDownStation();

		section.connectSubwayLine(this);
		this.sections.add(section);
	}

	public void removeSection(Station station) {
		if (this.sections.haveOnlyUpAndDownFinalStation()) {
			throw new SectionRemoveException(SectionErrorCode.INVALID_SECTION_REMOVE_BECAUSE_ONLY_ONE_SECTION);
		}

		if (isNotEqualsDownStation(station)) {
			throw new SectionRemoveException(SectionErrorCode.INVALID_SECTION_REMOVE_BECAUSE_DOWN_STATION);
		}

		Section removedSection = this.sections.remove(station);
		this.downStation = removedSection.getUpStation();
	}

	private boolean isNotEqualsDownStation(Station station) {
		return !this.downStation.equals(station);
	}
}
