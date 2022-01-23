package nextstep.subway.applicaion.station.domain;

import nextstep.subway.applicaion.domain.BaseEntity;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.section.domain.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Station extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@OneToMany(mappedBy = "upStation", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> upStationSection = new ArrayList<>();

	@OneToMany(mappedBy = "downStation", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> downStationSection = new ArrayList<>();

	protected Station() {
	}

	protected Station(String name) {
		this.name = name;
	}

	public static Station of(String name) {
		return new Station(name);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isLastDownStation(Line line) {
		return upStationSection.stream()
				.noneMatch(section -> section.isOnLine(line))
				&& downStationSection.stream()
				.anyMatch(section -> section.isOnLine(line));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Station station = (Station) o;
		return name.equals(station.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	public void addDownStationSection(Section section) {
		if (!downStationSection.contains(section)) {
			downStationSection.add(section);
		}
		if (!section.isDownStation(this)) {
			section.setDownStation(this);
		}
	}

	public void addUpStationSection(Section section) {
		if (!upStationSection.contains(section)) {
			upStationSection.add(section);
		}
		if (!section.isUpStation(this)) {
			section.setUpStation(this);
		}
	}
}
