package nextstep.subway.applicaion.line.domain;

import nextstep.subway.applicaion.domain.BaseEntity;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.section.domain.Sections;
import nextstep.subway.applicaion.section.exception.DownStationInvalidException;
import nextstep.subway.applicaion.section.exception.InvalidSectionRemovalException;
import nextstep.subway.applicaion.section.exception.UpStationInvalidException;
import nextstep.subway.applicaion.station.domain.Station;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@Column(nullable = true)
	private String color;

	@Embedded
	private Sections sections = new Sections();

	protected Line() {
	}

	private Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public static Line of(String name, String color) {
		return new Line(name, color);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public void update(Line other) {
		this.name = other.name;
		this.color = other.color;
	}

	public void addSection(Section section) {
		validateAppendingSection(section);

		if (!sections.contains(section)) {
			this.sections.add(section);
		}
		if (!this.equals(section.getLine())) {
			section.setLine(this);
		}
	}

	public Sections getSections() {
		return sections;
	}

	public boolean hasStation(Station station) {
		return sections.hasStation(station);
	}

	public boolean hasOnlyOneSection() {
		return sections.hasOnlyOneSection();
	}

	public boolean hasNoSections() {
		return sections.isEmpty();
	}

	public boolean isLastDownStation(Station station) {
		return sections.isLastDownStation(station);
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
			throw new InvalidSectionRemovalException(this);
		}
	}

	/**
	 * 지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.
	 */
	public void validateStationIsLastDownStation(Station toRemoveStation) {
		if (!sections.isLastDownStation(toRemoveStation)) {
			throw new InvalidSectionRemovalException(toRemoveStation);
		}
		sections.removeSectionOfLastDownStation(toRemoveStation);
	}

	private void validateAppendingSection(Section section) {
		if (sections.hasNoSection()) {
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
	public void validateAppendingUpStation(Station upStation) {
		if (!isLastDownStation(upStation)) {
			throw new UpStationInvalidException(upStation.getName());
		}
	}

	public Section getLastSection() {
		return sections.getLastSection();
	}
}
