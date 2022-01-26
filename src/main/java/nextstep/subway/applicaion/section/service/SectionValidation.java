package nextstep.subway.applicaion.section.service;

import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.section.exception.DownStationInvalidException;
import nextstep.subway.applicaion.section.exception.InvalidSectionRemovalException;
import nextstep.subway.applicaion.section.exception.UpStationInvalidException;
import nextstep.subway.applicaion.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SectionValidation {

	/**
	 * 구간 생성 검증
	 */
	public void validateCreateStation(Line line, Section section) {
		validateCreateDownStation(line, section.getDownStation());
		validateCreateUpStation(line, section.getUpStation());
	}

	/**
	 * 새로운 구간의 하행역은 등록될 노선에 등록되어 있는 역일 수 없다.
	 */
	public void validateCreateDownStation(Line line, Station downStation) {
		if (line.hasStation(downStation)) {
			throw new DownStationInvalidException(downStation.getName());
		}
	}

	/**
	 * 새로운 구간의 상행역은 등록될 노선의 하행 종점역이어야 한다.
	 */
	public void validateCreateUpStation(Line line, Station upStation) {
		if (!upStation.isLastDownStation(line)) {
			throw new UpStationInvalidException(upStation.getName());
		}
	}

	/**
	 * 구간 삭제 검증
	 */
	public void validateRemoveSection(Line line, Station station) {
		// 노선에 하행종점역과 상행종점역만 있는지 확인
		validateLineHasOnlyOneSection(line);
		// 노선의 종점역인지 확인
		validateStationIsLastDownStation(line, station);
	}

	/**
	 * 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.
	 */
	public void validateLineHasOnlyOneSection(Line line) {
		if (line.hasOnlyOneSection()) {
			throw new InvalidSectionRemovalException(line);
		}
	}

	/**
	 * 지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.
	 */
	public void validateStationIsLastDownStation(Line line, Station station) {
		if (!station.isLastDownStation(line)) {
			throw new InvalidSectionRemovalException(station);
		}
	}


}
