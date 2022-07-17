package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.LineUpdateRequest;

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
		stations.add(sections.get(sections.size() - 1).getDownStation());
		return stations;
	}

	public void update(LineUpdateRequest lineUpdateRequest) {
		this.name = lineUpdateRequest.getName();
		this.color = lineUpdateRequest.getColor();
	}
}
