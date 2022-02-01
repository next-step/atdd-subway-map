package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.exception.IllegalEntityException;
import nextstep.subway.ui.ExceptionMessage;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	List<Section> sections = new ArrayList<>();

	public void add(Section section) {
		if(!sections.isEmpty()) {
			validLastDownStation(section);
			validContainDownStation(section);
		}

		sections.add(section);
	}

	public void removeSection(Station station) {
		validEmpty();
		validDownStation(station);
		sections.removeIf(row -> row.getDownStation().equals(station));
	}

	private void validEmpty() {
		if(sections.isEmpty() || sections.size() == 1) {
			throw new IllegalEntityException(ExceptionMessage.NOT_REMOVE_SECTION.getMessage());
		}
	}

	private void validLastDownStation(Section section) {
		Section lastSection = sections.get(sections.size() - 1);

		if(section.getUpStation() != lastSection.getDownStation()) {
			throw new IllegalEntityException(ExceptionMessage.NOT_ADD_SECTION.getMessage());
		}
	}

	private void validDownStation(Station station) {
		Section lastSection = sections.get(sections.size() - 1);

		if(!lastSection.isDownStation(station)) {
			throw new IllegalEntityException(ExceptionMessage.NOT_REMOVE_SECTION.getMessage());
		}
	}

	private void validContainDownStation(Section section) {
		if(sections.stream()
			.anyMatch(row -> row.isContainStation(section.getDownStation()))) {
			throw new IllegalEntityException(ExceptionMessage.NOT_ADD_SECTION.getMessage());
		}
	}

	public List<Station> getStations() {
		List<Station> stations = sections.stream()
			.map(Section::getUpStation)
			.collect(Collectors.toList());

		stations.add(sections.get(sections.size() - 1).getDownStation());

		return Collections.unmodifiableList(stations);
	}
}
