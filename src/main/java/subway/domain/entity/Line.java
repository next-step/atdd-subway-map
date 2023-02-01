package subway.domain.entity;

import javax.persistence.*;

import static java.util.Optional.ofNullable;

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
	public void updateLineIfPresent(Line line) {
		ofNullable(line.getName()).ifPresent(nameToUpdate -> name = nameToUpdate);
		ofNullable(line.getColor()).ifPresent(colorToUpdate -> color = colorToUpdate);
		ofNullable(line.getUpStationId()).ifPresent(upStationIdToUpdate -> upStationId = upStationIdToUpdate);
		ofNullable(line.getDownStationId()).ifPresent(downStationIdToUpdate -> downStationId = downStationIdToUpdate);
		ofNullable(line.getDistance()).ifPresent(distanceToUpdate -> distance = distanceToUpdate);
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

	public Long getDistance() {
		return distance;
	}

	public Long getId() {
		return id;
	}
}
