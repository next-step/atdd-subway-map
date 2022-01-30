package nextstep.subway.domain;

import nextstep.subway.application.exception.DownStationInvalidException;
import nextstep.subway.application.exception.InvalidSectionRemovalException;
import nextstep.subway.application.exception.UpStationInvalidException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	public Sections() {
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
				.orElseThrow(() -> new InvalidSectionRemovalException(toRemoveLastDownStation));

		sections.remove(toRemoveSection);
	}

	public boolean hasNoSection() {
		return sections.isEmpty();
	}

	public Section getLastSection() {
		return sections.get(sections.size() - 1);
	}

	public List<Station> getStations() {
		if (sections.isEmpty()) {
			return emptyList();
		}
		return Stream.concat(
						sections.stream().map(Section::getUpStation),
						Stream.of(getLastDownStation()))
				.collect(toList());

	}

	public void removeSection(Station toRemoveLastDownStation) {
		validateLineHasOnlyOneSection();
		validateStationIsLastDownStation(toRemoveLastDownStation);
	}

	/**
	 * 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
	 */
	public void validateLineHasOnlyOneSection() {
		if (hasOnlyOneSection()) {
			throw new InvalidSectionRemovalException();
		}
	}

	/**
	 * 지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.
	 */
	public void validateStationIsLastDownStation(Station toRemoveStation) {
		if (!isLastDownStation(toRemoveStation)) {
			throw new InvalidSectionRemovalException(toRemoveStation);
		}
		removeSectionOfLastDownStation(toRemoveStation);
	}

	public void validateAppendingSection(Section section) {
		if (hasNoSection()) {
			return;
		}
		validateAppendingDownStation(section.getDownStation());
		validateAppendingUpStation(section.getUpStation());
	}

	/**
	 * 새로운 구간의 하행역은 등록될 노선에 등록되어 있는 역일 수 없다.
	 */
	private void validateAppendingDownStation(Station downStation) {
		if (hasStation(downStation)) {
			throw new DownStationInvalidException(downStation.getName());
		}
	}

	/**
	 * 새로운 구간의 상행역은 등록될 노선의 하행 종점역이어야 한다.
	 */
	private void validateAppendingUpStation(Station upStation) {
		if (!isLastDownStation(upStation)) {
			throw new UpStationInvalidException(upStation.getName());
		}
	}
}
