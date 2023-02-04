package subway.domain.entity;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Entity
public class Line {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 20, nullable = false)
	private String name;
	private String color;
	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
	private List<Section> sections = new ArrayList<>();

	protected Line() {

	}

	public Line(String name, String color) {
		this.name = name;
		this.color = color;
	}
	public void updateLineIfPresent(Line line) {
		ofNullable(line.getName()).ifPresent(nameToUpdate -> name = nameToUpdate);
		ofNullable(line.getColor()).ifPresent(colorToUpdate -> color = colorToUpdate);
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Long getId() {
		return id;
	}

	public List<Section> getSections() {
		return sections;
	}
}
