package nextstep.subway.domain;

import static nextstep.subway.common.exception.errorcode.StatusErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import nextstep.subway.common.exception.BusinessException;

public class Sections {
	private static final int MINIMUM_COUNT = 1;
	private final List<Section> values;

	public Sections(List<Section> sectionList) {
		values = new ArrayList<>(sectionList);
	}

	public void validationOfRegistration(long addUpStationId, long addDownStationId) {
		if (CollectionUtils.isEmpty(values)) {
			return;
		}

		//마지막 구간 종점과, 추가된 상행선은 같아야 한다.
		if (getLastSection().getDownStationId() != addUpStationId) {
			throw new BusinessException(INVALID_STATUS);
		}

		//기존 구간에 등록된 역이면 안되용
		if (values.stream()
			.anyMatch(station -> station.getDownStationId() == addDownStationId
				|| station.getUpStationId() == addDownStationId)) {
			throw new BusinessException(INVALID_STATUS);
		}
	}

	public void validationOfDelete(long stationId) {
		if (CollectionUtils.isEmpty(values)) {
			throw new BusinessException(INVALID_STATUS);
		}
		if (values.size() <= MINIMUM_COUNT) {
			throw new BusinessException(INVALID_STATUS);
		}

		if (getLastSection().getDownStationId() != stationId) {
			throw new BusinessException(INVALID_STATUS);
		}

	}

	public Section getLastSection() {
		return values.stream()
			.reduce((first, second) -> second)
			.orElseThrow(IllegalStateException::new);
	}

}
