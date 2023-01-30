package subway.domain.entity;

import javax.persistence.*;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 20, nullable = false)
	private String name;
	private String color;
	private Long upStationId;
	private Long downStationId;
	private Long distance;

	protected Line() {

	}

	public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
		this.name = name;
		this.color = color;
		this.upStationId = upStationId;
		this.downStationId = downStationId;
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Long getUpStationId() {
		return upStationId;
	}

	public void setUpStationId(Long upStationId) {
		this.upStationId = upStationId;
	}

	public Long getDownStationId() {
		return downStationId;
	}

	public void setDownStationId(Long downStationId) {
		this.downStationId = downStationId;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
