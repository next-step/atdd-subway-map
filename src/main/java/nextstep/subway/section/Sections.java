package nextstep.subway.section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nextstep.subway.exception.CustomException;

public class Sections {

	private static final int MIN_SIZE = 1;

	private final List<Section> values = new ArrayList<>();

	public Sections(List<Section> sectionList) {
		if (sectionList == null) {
			throw new IllegalArgumentException("sectionList is Null");
		}
		values.addAll(sectionList);
	}

	public void validateAppendUpStationId(Long appendUpStationId) {
		currentLastSection().validateAppendUpStationId(appendUpStationId);
	}

	public void validateAppendDownStationId(Long downStationId) {
		currentFirstSection().validateAppendDownStationId(downStationId);
	}

	public void validateMinimumSize() {
		if (this.values.size() == MIN_SIZE) {
			throw new CustomException(SectionErrorCode.MINIMUM_SECTION_COUNT);
		}
	}

	public void validateDeleteStationId(Long stationId) {
		validateExistence(stationId);
		currentLastSection().validateDeleteStationId(stationId);
	}

	private void validateExistence(Long stationId) {
		boolean exist = values.stream()
			.anyMatch(section -> section.isIncludedStation(stationId));

		if (!exist) {
			throw new CustomException(SectionErrorCode.NOT_INCLUDED_STATION);
		}
	}

	private Section currentFirstSection() {
		return values.get(0);
	}

	public Section currentLastSection() {
		return values.get(lastIndex());
	}

	private int lastIndex() {
		return this.values.size() - 1;
	}

	public List<Long> getStationIds() {
		List<Long> result = new ArrayList<>();

		result.add(currentFirstSection().getUpStationId());

		List<Long> downStations = values.stream()
			.map(Section::getDownStationId)
			.collect(Collectors.toList());
		result.addAll(downStations);

		return result;
	}
}
