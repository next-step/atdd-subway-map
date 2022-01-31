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

	public void remove(Long stationId) {
		// 삭제할 역 찾기 -> 구간에서 상행선이나 하행선 중 삭제할 역과 같은 id. -> 하행선만 찾으면 된다
		if (this.sections.stream()
			.anyMatch(section -> section.getUpStation().getId().equals(stationId))) {
			throw new SubwayException.CanNotDeleteException();
		}

		Section deleteSection = this.sections.stream()
			.filter(section -> section.getDownStation().getId().equals(stationId))
			.findFirst()
			.orElseThrow(SubwayException.NotFoundException::new);

		this.sections.remove(deleteSection);
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
