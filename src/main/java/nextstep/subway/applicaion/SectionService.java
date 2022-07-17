package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.section.SectionRequest;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.exception.*;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.repository.SubwayLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionService {

	private final SubwayLineRepository subwayLineRepository;
	private final SectionRepository sectionRepository;

	public void saveSection(Long subwayLineId, SectionRequest request) {
		SubwayLine subwayLine = subwayLineRepository.findById(subwayLineId).orElseThrow(NoSuchElementException::new);
		Section section = request.toSection();

		subwayLine.validate(section);

		sectionRepository.save(section);
		subwayLine.saveSection(section);
	}

	public void delete(Long subwayLineId, Long stationId) {
		SubwayLine subwayLine = subwayLineRepository.findById(subwayLineId).orElseThrow(NoSuchElementException::new);

		if (isSectionSizeEqualsOne(subwayLine)) {
			throw new CannotDeleteUniqueSectionException(ErrorCode.CANNOT_DELETE_UNIQUE_SECTION.getMessage());
		}

		if (isNotSameDownStationId(stationId, subwayLine)) {
			throw new CannotDeleteException(ErrorCode.CANNOT_DELETE_WITH_NOT_SAME_DOWN_STATION.getMessage());
		}

		Section deleteSection = subwayLine.deleteSection(stationId);
		sectionRepository.delete(deleteSection);
	}

	private boolean isSectionSizeEqualsOne(SubwayLine subwayLine) {
		return subwayLine.getSectionList().size() == 1;
	}

	private boolean isNotSameDownStationId(Long stationId, SubwayLine subwayLine) {
		return !subwayLine.getDownStationId().equals(stationId);
	}
}
