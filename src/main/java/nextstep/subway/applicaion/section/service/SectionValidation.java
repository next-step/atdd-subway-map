package nextstep.subway.applicaion.section.service;

import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.section.exception.DownStationInvalidException;
import nextstep.subway.applicaion.section.exception.UpStationInvalidException;
import nextstep.subway.applicaion.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SectionValidation {

	public void validateStation(Line line, Section section) {
		validateDownStation(line, section.getDownStation());
		validateUpStation(line, section.getUpStation());
	}

	/**
	 * 새로운 구간의 하행역은 등록될 노선에 등록되어 있는 역일 수 없다.
	 */
	public void validateDownStation(Line line, Station downStation) {
		if (line.hasStation(downStation)) {
			throw new DownStationInvalidException(downStation.getName());
		}
	}

	/**
	 * 새로운 구간의 상행역은 등록될 노선의 하행 종점역이어야 한다.
	 */
	public void validateUpStation(Line line, Station upStation) {
		if (!upStation.isLastDownStation(line)) {
			throw new UpStationInvalidException(upStation.getName());
		}
	}

}
