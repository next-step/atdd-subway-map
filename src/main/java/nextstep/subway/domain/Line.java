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
	private Long upStationId;
	private Long downStationId;
	private Long distance;

	public Line() {
	}

	public Line(String stationName, String stationColor, Long upStationId, Long downStationId, Long distance) {
		this.name = stationName;
		this.color = stationColor;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
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

	public Long getUpStationId() {
		return upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public void updateStationLineInformation(String stationName, String stationColor) {
		this.name = stationName;
		this.color = stationColor;
	}

	public boolean isRegistrable(long upStationIdFromSelection, long downStationIdFromSelection) {
		if (upStationIdFromSelection != this.downStationId) {
			throw new RuntimeException();
		}

		if (this.upStationId == downStationIdFromSelection || this.downStationId == downStationIdFromSelection) {
			throw new RuntimeException();
		}
		return true;
	}

	public boolean isDeletable(long stationId) {
		if (this.downStationId != stationId) {
			throw new RuntimeException();
		}
		return true;
	}

	public void updateDownStationId(long downStationIdFromSelection) {
		this.downStationId = downStationIdFromSelection;
	}
}
