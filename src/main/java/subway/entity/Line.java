package subway.entity;

import org.springframework.util.ObjectUtils;

import javax.persistence.*;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String color;

	private Long upStationId;

	private Long downStationId;

	private int distance;

	public Line() {

	}

	public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
		this.name = name;
		this.color = color;
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

	public void setUpdateInfo(String name, String color, Long upStationId, Long downStationId, int distance) {
		if(!ObjectUtils.isEmpty(name)) {
			this.name = name;
		}

		if(!ObjectUtils.isEmpty(color)) {
			this.color = color;
		}

		if(!ObjectUtils.isEmpty(upStationId)) {
			this.upStationId = upStationId;
		}

		if(!ObjectUtils.isEmpty(downStationId)) {
			this.downStationId = downStationId;
		}

		if(!ObjectUtils.isEmpty(distance)) {
			this.distance = distance;
		}
	}
}
