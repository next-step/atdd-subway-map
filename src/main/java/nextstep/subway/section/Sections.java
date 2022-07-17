package nextstep.subway.section;

import java.util.ArrayList;
import java.util.List;

import nextstep.subway.exception.CustomException;

public class Sections {

	private static final int MIN_SIZE = 2;

	private final List<Section> values = new ArrayList<>();

	public Sections(List<Section> sectionList) {
		if (sectionList == null) {
			throw new IllegalArgumentException("sectionList is Null");
		}
		values.addAll(sectionList);
	}

	public void validateAppendUpStationId(Long appendUpStationId) {
		currentDownStation().validateAppendUpStationId(appendUpStationId);
	}

	public void validateAppendDownStationId(Long downStationId) {
		currentUpStation().validateAppendDownStationId(downStationId);
	}

	public void validateMinimumSize() {
		if (this.values.size() == MIN_SIZE) {
			throw new CustomException(SectionErrorCode.MINIMUM_SECTION_COUNT);
		}
	}

	public void validateDeleteStationId(Long stationId) {
		validateExistence(stationId);
		currentDownStation().validateDeleteStationId(stationId);
	}

	private void validateExistence(Long stationId) {
		values.stream()
			.filter(section -> section.isIncludedStation(stationId))
			.findAny()
			.orElseThrow(() -> new CustomException(SectionErrorCode.NOT_INCLUDED_STATION));
	}

	private Section currentUpStation() {
		return values.get(0);
	}

	private Section currentDownStation() {
		return values.get(lastIndex());
	}

	private int lastIndex() {
		return this.values.size() - 1;
	}

}
