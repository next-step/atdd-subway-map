package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import subway.Station;

@Entity
public class Line {

	@Id
	@Column(name = "line_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 20, nullable = false)
	private String name;

	@Column(length = 30, nullable = false)
	private String color;

	@ManyToOne()
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_up_station_id"))
	private Station upStationId;

	@ManyToOne()
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_down_station_id"))
	private Station downStationId;

	@Column(length = 20, nullable = false)
	private int distance;

	public Line() {
	}

	public Line(String name, String color, Station upStationId, Station downStationId, int distance) {
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

	public Station getUpStationId() {
		return upStationId;
	}

	public Station getDownStationId() {
		return downStationId;
	}

	public int getDistance() {
		return distance;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
