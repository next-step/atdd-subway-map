package subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import subway.exception.NotFoundSectionInSubwayLineException;
import subway.exception.SectionErrorCode;

@Embeddable
public class Sections {

	private static final long NOT_HAVA_STATION_COUNT = 0L;

	private static final int HAVE_UP_AND_DOWN_FINAL_STATION_SIZE = 1;

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

	public Section remove(Station station) {
		Section findSection = this.sections.stream()
			.filter(section -> section.isEqualDownStation(station))
			.findFirst()
			.orElseThrow(
				() -> new NotFoundSectionInSubwayLineException(SectionErrorCode.NOT_FOUND_SECTION_IN_SUBWAY_LINE)
			);

		this.sections.remove(findSection);
		return findSection;
	}

	public boolean haveOnlyUpAndDownFinalStation() {
		return this.sections.size() == HAVE_UP_AND_DOWN_FINAL_STATION_SIZE;
	}
}
