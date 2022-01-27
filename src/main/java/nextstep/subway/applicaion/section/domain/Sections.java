package nextstep.subway.applicaion.section.domain;

import nextstep.subway.applicaion.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public List<Section> getSections() {
		return sections;
	}

	public boolean contains(Section section) {
		return sections.contains(section);
	}

	public void add(Section section) {
		sections.add(section);
	}

	public boolean hasStation(Station station) {
		return sections.stream()
				.anyMatch(section -> section.hasStation(station));
	}

	public boolean hasOnlyOneSection() {
		return sections.size() == 1;
	}

	public boolean isEmpty() {
		return sections.isEmpty();
	}

	public boolean isLastDownStation(Station station) {
		return getLastDownStation().equals(station);
	}

	private Station getLastDownStation() {
		return getLastSection().getDownStation();
	}

	public void removeSectionOfLastDownStation(Station toRemoveLastDownStation) {
		Section toRemoveSection = sections.stream()
				.filter(section -> section.isDownStation(toRemoveLastDownStation))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);

		sections.remove(toRemoveSection);
	}

	public boolean hasNoSection() {
		return sections.isEmpty();
	}

	public Section getLastSection() {
		return sections.get(sections.size() - 1);
	}
}
