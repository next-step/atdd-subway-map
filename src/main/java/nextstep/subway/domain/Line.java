package nextstep.subway.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

@Entity
@Getter
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Integer distance;

	public Line() {
	}

	public Line(String name, String color, Long upStationId, Long downStationId, Integer distance) {
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public void update(String name, String color) {
		this.name = name;
		this.color = color;
	}
}
