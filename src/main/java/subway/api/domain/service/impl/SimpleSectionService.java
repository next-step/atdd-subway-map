package subway.api.domain.service.impl;

import static org.springframework.http.HttpStatus.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.api.domain.dto.inport.SectionCreateCommand;
import subway.api.domain.dto.outport.SectionInfo;
import subway.api.domain.model.entity.Line;
import subway.api.domain.model.entity.Section;
import subway.api.domain.model.entity.Station;
import subway.api.domain.operators.LineResolver;
import subway.api.domain.operators.SectionFactory;
import subway.api.domain.operators.StationResolver;
import subway.api.domain.service.SectionService;
import subway.common.exception.LineNotFoundException;
import subway.common.exception.SectionCreationNotValidException;
import subway.common.exception.SectionDeletionNotValidException;
import subway.common.exception.StationNotFoundException;

/**
 * @author : Rene Choi
 * @since : 2024/01/31
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SimpleSectionService implements SectionService {
	private final LineResolver lineResolver;
	private final SectionFactory sectionFactory;
	private final StationResolver stationResolver;

	@Override
	@Transactional
	public SectionInfo addSection(Long lineId, SectionCreateCommand createCommand) {
		Line line = lineResolver.fetchOptional(lineId).orElseThrow(() -> new LineNotFoundException(BAD_REQUEST));

		if (isNotSatisfiedByCreationRule(createCommand, line)) {
			throw new SectionCreationNotValidException();
		}

		Station upStation = stationResolver.fetchOptional(createCommand.getUpStationId()).orElseThrow(() -> new StationNotFoundException(BAD_REQUEST));
		Station downStation = stationResolver.fetchOptional(createCommand.getDownStationId()).orElseThrow(() -> new StationNotFoundException(BAD_REQUEST));

		Section newSection = sectionFactory.createSection(createCommand, line, upStation, downStation);
		line.addSection(newSection);

		return SectionInfo.from(newSection);
	}

	private boolean isNotSatisfiedByCreationRule(SectionCreateCommand createCommand, Line line) {
		return isRequestedDownStationDuplicate(createCommand, line) || isRequestedUpStationNotAnExpectedDownStation(createCommand, line);
	}

	private boolean isRequestedUpStationNotAnExpectedDownStation(SectionCreateCommand createCommand, Line line) {
		return line.isNotDownEndStation(createCommand.getUpStationId());
	}

	private boolean isRequestedDownStationDuplicate(SectionCreateCommand createCommand, Line line) {
		return line.isContainsAnyStation(createCommand.getDownStationId());
	}

	@Override
	@Transactional
	public void deleteSection(Long lineId, Long stationId) {
		Line line = lineResolver.fetchOptional(lineId).orElseThrow(() -> new LineNotFoundException(BAD_REQUEST));

		if (line.isNotDownEndStation(stationId) || line.isSectionCountBelowThreshold(1)) {
			throw new SectionDeletionNotValidException();
		}

		line.removeLastSection();
	}
}
