package nextstep.subway.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String color;

	protected Line() {
	}

	protected Line(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public static Line of(String name, String color) {
		return new Line(name, color);
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

	public void update(Line other) {
		this.name = other.name;
		this.color = other.color;
	}
}
