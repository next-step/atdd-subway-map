package subway.line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import subway.station.Station;
import subway.section.Section;

@Entity
public class Line {

	@Id
	@Column(name = "line_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String name;

	@Column(length = 30, nullable = false)
	private String color;

	@OneToMany
	private List<Section> sections = new ArrayList<>();

	public Line() {
	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public void updateLine(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public void addSection(Section section) {
		this.sections.add(section);
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

	public List<Station> getAllStation() {
		return this.sections.stream()
			.map(section -> List.of(section.getUpStation(), section.getDownStation()))
			.flatMap(Collection::stream)
			.distinct()
			.collect(Collectors.toList());
	}

	public Section getLastSection() {
		return sections.get(sections.size() - 1);
	}

	public boolean isLastSection() {
		return sections.size() == 1;
	}

	public Section removeLastSection() {
		return sections.remove(sections.size() - 1);
	}
}
