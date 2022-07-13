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

	public boolean isRegistrable(long upStationIdFromSction, long downStationIdFromSection) {
		if (upStationIdFromSction != this.downStationId) {
			throw new IllegalStateException();
		}

		if (this.upStationId == downStationIdFromSection || this.downStationId == downStationIdFromSection) {
			throw new IllegalStateException();
		}
		return true;
	}

	public boolean isDeletable(long stationId) {
		if (this.downStationId != stationId) {
			throw new IllegalStateException();
		}
		return true;
	}

	public void updateDownStationId(long downStationIdFromSection) {
		this.downStationId = downStationIdFromSection;
	}
}
