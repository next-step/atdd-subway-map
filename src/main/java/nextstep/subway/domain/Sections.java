package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

	@OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Section> sections = new ArrayList<>();

	public Sections() {
	}

	public void add(Section section) {
		this.sections.add(section);
	}

	public void remove(Section section) {
		this.sections.remove(section);
	}
}
