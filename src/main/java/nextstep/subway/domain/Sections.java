package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {
	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
	private List<Section> sections = new ArrayList<>();

	protected Sections() {

	}

	public Sections(List<Section> sections) {
		this.sections = sections;
	}

	public void addSection(Section section) {
		this.sections.add(section);
	}
}
