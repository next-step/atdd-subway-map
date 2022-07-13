package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.repository.SubwayLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {

	private final SubwayLineRepository subwayLineRepository;
	private final SectionRepository sectionRepository;

	@Transactional
	public void saveSection(Long subwayLineId, SectionRequest request) {
		SubwayLine subwayLine = subwayLineRepository.findById(subwayLineId).orElseThrow(NoSuchElementException::new);
		Section section = request.toSection();

		sectionRepository.save(section);
		subwayLine.saveSection(section);
	}
}
