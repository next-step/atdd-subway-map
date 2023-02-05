package subway.line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
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

	public void validAddSection(Station upStation, Station downStation) {
		if (getLastSection().getDownStation() != upStation) {
			throw new NoSuchElementException("등록하려는 새로운 구간의 상행역이 노선의 하행 종점역과 일치하지 않습니다.");
		}
		if (getAllStation().contains(downStation)) {
			throw new IllegalArgumentException("등록하려는 새로운 구간의 하행 종점역이 이미 노선에 등록되어 있습니다.");
		}
	}

	public void addSection(Section section) {
		this.sections.add(section);
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

	public boolean removeSection(Section section) {
		if (getLastSection() != section) {
			throw new NoSuchElementException("삭제하려는 구간의 하행역이 노선의 하행 종점역과 일치하지 않습니다.");
		}
		if (this.sections.size() == 1) {
			throw new IllegalArgumentException("삭제하려는 구간이 노선의 마지막 구간입니다.");
		}
		return sections.remove(section);
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
}
