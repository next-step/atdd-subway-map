package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import nextstep.subway.exception.BusinessException;
import nextstep.subway.exception.ErrorCode;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
	private final List<Section> sections = new ArrayList<>();

	protected Sections() {

	}

	public List<Section> getSections() {
		return sections;
	}

	public void addSection(Section section) {
		if (sections.isEmpty()) {
			this.sections.add(section);
			return;
		}
		validateAddSection(section);
		this.sections.add(section);
	}

	public void validateAddSection(Section section) {
		if (!isSameLastDownStation(section.getUpStation())) {
			throw new BusinessException(ErrorCode.LAST_STATION_NOT_MATCH_UP_STATION);
		}

		if (containsStation(section.getDownStation())) {
			throw new BusinessException(ErrorCode.ALREADY_CONTAINS_STATION);
		}
	}

	public boolean isSameLastDownStation(Station station) {
		return getLastStation().equals(station);
	}

	public boolean containsStation(Station station) {
		return sections.stream()
			.anyMatch(section -> section.containsStation(station));
	}

	public List<Station> getStationList() {
		return sections.stream()
			.map(Section::getStationList)
			.flatMap(List::stream)
			.distinct()
			.collect(Collectors.toList());
	}

	public Station getLastStation() {
		return sections.get(sections.size() - 1).getDownStation();
	}
}
