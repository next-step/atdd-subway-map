package nextstep.subway.domain;

import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import nextstep.subway.applicaion.dto.LineUpdateRequest;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String color;
	@Embedded
	private Sections sections = new Sections();

	public Line() {
	}

	public Line(String name, String color, Section section) {
		this.name = name;
		this.color = color;
		this.sections.addSection(section);
		section.setLine(this);
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

	public Sections getSections() {
		return sections;
	}

	public void update(LineUpdateRequest lineUpdateRequest) {
		if (lineUpdateRequest.getName() != null) {
			this.name = lineUpdateRequest.getName();
		}
		if (lineUpdateRequest.getColor() != null) {
			this.color = lineUpdateRequest.getColor();
		}
	}

	public void addSection(Section section) {
		this.sections.addSection(section);
		section.setLine(this);
	}

	public List<Station> getStations() {
		return getSections().getStationList();
	}

	public void removeSection(Station station) {
		sections.removeSection(station);
	}
}
