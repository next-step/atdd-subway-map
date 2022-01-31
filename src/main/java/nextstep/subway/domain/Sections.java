package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.exception.SubwayException;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.ALL})
	private List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public void add(Section section) {
		this.sections.add(section);
	}

	public void removeSection(Long stationId) {
		if (this.sections.size() == 2) {
			throw new SubwayException.CanNotDeleteException("구간이 하나만 존재하므로 지울 수 없습니다.");
		}

		Section deleteSection = this.sections.stream()
			.filter(section -> section.getDownStation().getId().equals(stationId))
			.findFirst()
			.orElseThrow(SubwayException.NotFoundException::new);

		if (deleteSection.isFirst()) {
			throw new SubwayException.CanNotDeleteException("하행구간만 지울 수 있습니다.");
		}

		this.sections.remove(deleteSection);
	}

	public void addAll(Section upSection, Section downSection) {
		this.sections.add(upSection);
		this.sections.add(downSection);
	}

	public List<Station> getStations() {
		List<Station> stations = new ArrayList<>();
		for (Section section : sections) {
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
