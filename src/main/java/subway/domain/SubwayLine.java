package subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.application.Section;
import subway.exception.AlreadyRegisteredStationException;
import subway.exception.SectionErrorCode;
import subway.exception.SectionRegisterException;

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

	@Column(nullable = false)
	private Long upStationId;

	@Column(nullable = false)
	private Long downStationId;

	@OneToMany(mappedBy = "subwayLine", cascade = {CascadeType.PERSIST, CascadeType.DETACH}, orphanRemoval = true)
	private List<SubwayLineStationGroup> subwayLineStationGroups = new ArrayList<>();

	public SubwayLine(
		String name,
		String color,
		Long upStationId,
		Long downStationId,
		List<Station> stations) {
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;

		createSubwayLineStationGroups(stations);
	}

	public void updateInfo(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public Section createSection(int distance) {
		return new Section(this.upStationId, this.downStationId, distance);
	}

	public void exchangeDownStation(Section section, Station newDownStation) {
		validateNewSectionUpStationEqualDownStation(section);
		validateDownStationRegistration(newDownStation);

		subwayLineStationGroups.removeIf(
			subwayLineStationGroup -> subwayLineStationGroup.equalStationId(this.downStationId)
		);

		this.downStationId= newDownStation.getId();
		this.subwayLineStationGroups.add(createSubwayLineStationGroup(newDownStation));
	}

	public boolean isPossibleRemove(Section section) {
		return section.equalDownStationId(this.downStationId) && !section.equalUpStationId(this.upStationId);
	}

	public void removeSection(Section section, Station newDownStation) {
		this.downStationId = newDownStation.getId();

		subwayLineStationGroups.removeIf(
			subwayLineStationGroup -> subwayLineStationGroup.equalStationId(section.getDownStationId())
		);

		subwayLineStationGroups.add(createSubwayLineStationGroup(newDownStation));
	}

	private void validateNewSectionUpStationEqualDownStation(Section section) {
		if (!section.equalUpStationId(this.downStationId)) {
			throw new SectionRegisterException(SectionErrorCode.INVALID_SECTION_UP_STATION);
		}
	}

	private void validateDownStationRegistration(Station downStation) {
		boolean hasDownStation = subwayLineStationGroups.stream()
			.anyMatch(subwayLineStationGroup -> subwayLineStationGroup.equalStationId(downStation.getId()));

		if (hasDownStation) {
			throw new AlreadyRegisteredStationException(SectionErrorCode.ALREADY_STATION_REGISTERED);
		}
	}

	private SubwayLineStationGroup createSubwayLineStationGroup(Station station) {
		return new SubwayLineStationGroup(station, this);
	}

	private void createSubwayLineStationGroups(List<Station> stations) {
		for (Station station : stations) {
			SubwayLineStationGroup subwayLineStationGroup = new SubwayLineStationGroup(station, this);
			this.subwayLineStationGroups.add(subwayLineStationGroup);
		}
	}
}
