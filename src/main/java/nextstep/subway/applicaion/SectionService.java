package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineSectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {

	private final SectionRepository sectionRepository;
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public SectionService(SectionRepository sectionRepository,
												LineRepository lineRepository,
												StationRepository stationRepository
	) {
		this.sectionRepository = sectionRepository;
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public SectionResponse createSection(final Long id, final LineSectionRequest lineSectionRequest) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		Station upStation =
						stationRepository.findById(lineSectionRequest.getUpStationId()).orElseThrow(RuntimeException::new);
		Station downStation =
						stationRepository.findById(lineSectionRequest.getDownStationId()).orElseThrow(RuntimeException::new);
		Section section = sectionRepository.save(Section.of(line, upStation, downStation, lineSectionRequest.getDistance()));

		return SectionResponse.of(section.getId(), upStation.getId(), downStation.getId(), section.getDistance());
	}
}
