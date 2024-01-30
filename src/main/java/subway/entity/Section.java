package subway.entity;

import javax.persistence.*;

@Entity
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	Line line;

	private Long downStationId;

	private Long upStationId;

	private int distance;

	public Long getDownStationId() {
		return downStationId;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public int getDistance() {
		return distance;
	}

	public Section() {
	}

	public Section(Line line, Long downStationId, Long upStationId, int distance) {
		this.line = line;
		this.downStationId = downStationId;
		this.upStationId = upStationId;
		this.distance = distance;
	}
}
