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

	public Long getId() {
		return id;
	}

	public Line getLine() {
		return line;
	}

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

	public Section(Line line, Long upStationId, Long downStationId, int distance) {
		this.line = line;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}
}
