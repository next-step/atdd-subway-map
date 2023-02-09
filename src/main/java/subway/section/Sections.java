package subway.section;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import subway.station.Station;

@Embeddable
public class Sections {

	@OneToMany
	private List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public Section addSection(Section section) {
		this.sections.add(section);
		if (getLastSection() == section) {
			return getLastSection();
		}
		throw new IllegalArgumentException("구간이 저장되지 않았습니다.");
	}

	public List<Station> getAllStation() {
		return this.sections.stream()
			.map(section -> List.of(section.getUpStation(), section.getDownStation()))
			.flatMap(Collection::stream)
			.distinct()
			.collect(Collectors.toList());
	}

	public Section getLastSection() {
		return sections.size() == 0 ? null : sections.get(sections.size() - 1);
	}

	public boolean removeSection(Section section) {
		if (getLastSection() != section) {
			throw new NoSuchElementException("삭제하려는 구간의 하행역이 노선의 하행 종점역과 일치하지 않습니다.");
		}
		if (this.sections.size() == 1) {
			throw new IllegalArgumentException("삭제하려는 구간이 노선의 마지막 구간입니다.");
		}
		return sections.remove(section);
	}
}
