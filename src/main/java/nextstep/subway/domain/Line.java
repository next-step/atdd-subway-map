package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Line extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@Column(nullable = true)
	private String color;

	@Embedded
	private Sections sections = new Sections();

	protected Line() {
	}

	private Line(String name, String color) {
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

	public void addSection(Section section) {
		sections.validateAppendingSection(section);

		if (!sections.contains(section)) {
			this.sections.add(section);
		}
		if (!this.equals(section.getLine())) {
			section.setLine(this);
		}
	}

	public Sections getSections() {
		return sections;
	}

	public void removeSection(Station toRemoveLastDownStation) {
		sections.removeSection(toRemoveLastDownStation);
	}

	public Section getLastSection() {
		return sections.getLastSection();
	}
}
