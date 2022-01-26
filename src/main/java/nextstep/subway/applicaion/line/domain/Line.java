package nextstep.subway.applicaion.line.domain;

import nextstep.subway.applicaion.domain.BaseEntity;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@Column(nullable = true)
	private String color;

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

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
		if (!sections.contains(section)) {
			sections.add(section);
		}
		if (!this.equals(section.getLine())) {
			section.setLine(this);
		}
	}

	public List<Section> getSections() {
		return sections;
	}

	public boolean hasStation(Station station) {
		return sections.stream()
				.anyMatch(section -> section.hasStation(station));
	}

	public boolean hasOnlyOneSection() {
		return sections.size() == 1;
	}

	public boolean hasNoSections() {
		return sections.isEmpty();
	}
}
