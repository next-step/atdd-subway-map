package nextstep.subway.applicaion;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.exception.SubwayException;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.repository.SectionRepository;
import nextstep.subway.repository.StationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionService {
	private final LineRepository lineRepository;
	private final SectionRepository sectionRepository;

	@Transactional
	public void addSection(Long lineId, SectionRequest sectionRequest) {
		Line line = lineRepository.findById(lineId).orElseThrow(() -> new SubwayException("no subway"));
		line.addSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId(),
			sectionRequest.getDistance());
	}

	@Transactional
	public void subSection(Long lineId, Long stationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(() -> new SubwayException("no subway"));
		Section section = line.subSection(stationId);
		sectionRepository.delete(section);
	}
}
