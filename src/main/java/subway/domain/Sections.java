package subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

	private static final long NOT_HAVA_STATION_COUNT = 0L;

	@OneToMany(
		mappedBy = "subwayLine",
		cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
		orphanRemoval = true
	)
	private final List<Section> sections = new ArrayList<>();

	public void add(Section section) {
		this.sections.add(section);
	}

	public boolean hasStation(Station target) {
		long count = this.sections.stream()
			.filter(section -> section.hasStation(target))
			.count();

		return count > NOT_HAVA_STATION_COUNT;
	}
}
