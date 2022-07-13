package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String color;

	public Line() {
	}

	public Line(String stationName, String stationColor) {
		this.name = stationName;
		this.color = stationColor;

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

	public void updateStationLineInformation(String stationName, String stationColor) {
		this.name = stationName;
		this.color = stationColor;
	}

}
