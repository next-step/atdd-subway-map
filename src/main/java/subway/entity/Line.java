package subway.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String color;

	private Long startStationId;

	private Long endStationId;

	private int distance;

	@OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Section> sections;

	public Line() {

	}

	public Line(String name, String color, Long startStationId, Long endStationId, int distance) {
		this.name = name;
		this.color = color;
		this.startStationId = startStationId;
		this.endStationId = endStationId;
		this.distance = distance;
		this.sections = new ArrayList<>();
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

	public Long getStartStationId() {
		return startStationId;
	}

	public Long getEndStationId() {
		return endStationId;
	}

	public int getDistance() {
		return distance;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setUpdateInfo(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public void addSection(Section section) {
		this.endStationId = section.getDownStationId();
		this.distance = this.distance + section.getDistance();
		this.sections.add(section);
	}

	public void deleteSection(Section section) {
		this.endStationId = section.getUpStationId();
		this.distance = this.distance - section.getDistance();
		sections.remove(section);
	}

	public boolean hasStation(Long stationId) {
		for(Section section : sections) {
			if(stationId.equals(section.getDownStationId())) {
				return true;
			}

			if(stationId.equals(section.getUpStationId())) {
				return true;
			}
		}

		return false;
	}
}
