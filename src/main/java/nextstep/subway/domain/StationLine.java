package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StationLine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL)
	private Station station;

	private String color;
	private Long upStationId;
	private Long downStationId;
	private Long distance;

	public StationLine() {
	}

	public StationLine(String stationName, String stationColor, Long upStationId, Long downStationId, Long distance) {
		this.station = new Station(stationName);
		this.color = stationColor;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public String getStationName() {
		return this.station.getName();
	}

	public String getColor() {
		return color;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public void updateStationLineInformation(String stationName, String stationColor) {
		this.station.updateStationName(stationName);
		this.color = stationColor;
	}
}
