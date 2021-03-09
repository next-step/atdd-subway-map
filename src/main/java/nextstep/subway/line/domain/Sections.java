package nextstep.subway.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.line.exception.AlreadyExistDownStationException;
import nextstep.subway.line.exception.NotFoundLineException;
import nextstep.subway.line.exception.NotLastStationException;
import nextstep.subway.line.exception.NotSameUpStationException;
import nextstep.subway.line.exception.TooLowLengthSectionsException;
import nextstep.subway.station.domain.Station;

@Embeddable
public class Sections {

	public static final int SECTION_MIN_SIZE = 1;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public List<Section> getSections() {
		return sections;
	}

	public List<Station> getStations() {
		return sections.stream()
			.sorted()
			.flatMap(Section::getStations)
			.distinct()
			.collect(Collectors.toList());
	}

	public void addSection(Section section) {
		if (sections.isEmpty()) {
			sections.add(section);
			return;
		}

		final Station lastStation = findLastStation();
		validateAddSection(section.getUpStation(), section.getDownStation(), lastStation);
		sections.add(section);
	}

	public void deleteSection(Station station) {
		validateDeleteSection(station);

		final Section target = sections.stream()
			.filter(section -> section.getDownStation().equals(station))
			.findAny()
			.orElseThrow(() -> new NotFoundLineException(station.getId()));

		sections.remove(target);
	}

	private Station findLastStation() {
		return getSections().get(sections.size() - 1).getDownStation();
	}

	private void validateAddSection(Station upStation, Station downStation, Station lastStation) {
		if (!upStation.equals(lastStation)) {
			throw new NotSameUpStationException(upStation.getName(), lastStation.getName());
		}
		if (getStations().contains(downStation)) {
			throw new AlreadyExistDownStationException(downStation.getName());
		}
	}

	private void validateDeleteSection(Station station) {
		if (sections.size() <= SECTION_MIN_SIZE) {
			throw new TooLowLengthSectionsException(SECTION_MIN_SIZE);
		}
		if (isNotLastStation(station)) {
			throw new NotLastStationException(station.getName());
		}
	}

	private boolean isNotLastStation(Station station) {
		return !findLastStation().equals(station);
	}
}
