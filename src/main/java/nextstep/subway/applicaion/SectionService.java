package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.section.SectionRequest;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.exception.AlreadyRegisterException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.ErrorCode;
import nextstep.subway.exception.SameUpStationException;
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

		if (hasSameStation(subwayLine, section)) {
			throw new AlreadyRegisterException(ErrorCode.ALREADY_REGISTER_SECTION.getMessage());
		}

		if (isSameUpStation(subwayLine.getUpStationId(), section.getUpStationId())) {
			throw new SameUpStationException(ErrorCode.CANNOT_REGISTER_WITH_UP_STATION.getMessage());
		}

		sectionRepository.save(section);
		subwayLine.saveSection(section);
	}

	public void delete(Long subwayLineId, Long stationId) {
		SubwayLine subwayLine = subwayLineRepository.findById(subwayLineId).orElseThrow(NoSuchElementException::new);

		if (isNotSameDownStationId(stationId, subwayLine)) {
			throw new CannotDeleteException(ErrorCode.CANNOT_DELETE_WITH_NOT_SAME_DOWN_STATION.getMessage());
		}

		Section deleteSection = subwayLine.deleteSection(stationId);
		sectionRepository.delete(deleteSection);
	}

	private boolean isNotSameDownStationId(Long stationId, SubwayLine subwayLine) {
		return !subwayLine.getDownStationId().equals(stationId);
	}

	private boolean hasSameStation(SubwayLine subwayLine, Section section) {
		return subwayLine.getDownStationId().equals(section.getDownStationId()) ||
				subwayLine.getUpStationId().equals(section.getDownStationId());
	}

	private boolean isSameUpStation(Long subwayLineUpStationId, Long sectionUpStationId) {
		return subwayLineUpStationId.equals(sectionUpStationId);
	}
}
