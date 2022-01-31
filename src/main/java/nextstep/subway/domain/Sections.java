package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
			Section lastSection = sections.get(sections.size() - 1);

			if(section.getUpStation() != lastSection.getDownStation()) {
				throw new IllegalEntityException(ExceptionMessage.NOT_ADD_SECTION.getMessage());
			}

			if(isValidContainDownStation(section)) {
				throw new IllegalEntityException(ExceptionMessage.NOT_ADD_SECTION.getMessage());
			}
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

	private void validDownStation(Station station) {
		Section lastSection = sections.get(sections.size() - 1);

		if(!lastSection.isDownStation(station)) {
			throw new IllegalEntityException(ExceptionMessage.NOT_REMOVE_SECTION.getMessage());
		}
	}

	private boolean isValidContainDownStation(Section section) {
		return sections.stream()
			.anyMatch(row -> row.isContainStation(section.getDownStation()));
	}

	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}
}
