package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.ALL})
	private List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public void add(Section section) {
		this.sections.add(section);
	}

	public void remove(Section section) {
		this.sections.remove(section);
	}

	public void addAll(Section upSection, Section downSection) {
		this.sections.add(upSection);
		this.sections.add(downSection);
	}

	public List<Station> getStations(){
		List<Station> stations = new ArrayList<>();
		for (Section section : sections){
			stations.add(section.getUpStation());
			stations.add(section.getDownStation());
		}

		return stations;
	}

	public List<Section> getSections() {
		List<Section> stations = new ArrayList<>();
		Optional<Section> firstStation = findUpSection();

		while (firstStation.isPresent()) {
			Section section = firstStation.get();
			stations.add(section);
			firstStation = findDownSection(section.getDownStation());
		}

		return stations;
	}

	private Optional<Section> findUpSection() {
		return this.sections.stream()
			.filter(section -> section.getUpStation() == null)
			.findFirst();
	}

	private Optional<Section> findDownSection(Station downStation) {
		return this.sections.stream()
			.filter(section -> section.getUpStation() == downStation)
			.findFirst();
	}
}
