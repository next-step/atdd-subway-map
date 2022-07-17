package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineUpdateRequest;
import nextstep.subway.exception.SubwayException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
public class Line {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String color;

	@OneToMany(fetch = LAZY)
	@JoinColumn
	private List<Section> sections = new ArrayList<>();

	protected Line() {
	}

	public Line(String name, String color, Section section) {
		this.name = name;
		this.color = color;
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

	public List<Station> getStations() {
		List<Station> stations = new ArrayList<>();
		for (Section section : sections) {
			stations.add(section.getUpStation());
		}
		stations.add(getLastStation());
		return stations;
	}

	public void update(LineUpdateRequest lineUpdateRequest) {
		this.name = lineUpdateRequest.getName();
		this.color = lineUpdateRequest.getColor();
	}

	public void addSection(Section section) {
		Station lastStation = getLastStation();
		if (!lastStation.equals(section.getUpStation())) {
			throw new SubwayException();
		}
		List<Station> stations = getStations();
		if (stations.contains(section.getDownStation())) {
			throw new SubwayException();
		}
		sections.add(section);
	}

	public Section deleteSectionOf(Station station) {
		Station lastStation = getLastStation();

		if (!lastStation.equals(station)) {
			throw new SubwayException();
		}

		Section lastSection = sections.get(sections.size() - 1);
		sections.remove(lastSection);
		return lastSection;
	}

	private Station getLastStation() {
		return sections.get(sections.size() - 1).getDownStation();
	}
}
