package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.section.SectionRequest;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SubwayLine;
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

		subwayLine.validateOnSave(section);

		subwayLine.saveSection(section);
	}

	public void delete(Long subwayLineId, Long stationId) {
		SubwayLine subwayLine = subwayLineRepository.findById(subwayLineId).orElseThrow(NoSuchElementException::new);

		subwayLine.validateOnDelete(stationId);

		Section deleteSection = subwayLine.deleteSection(stationId);
		sectionRepository.delete(deleteSection);
	}
}
