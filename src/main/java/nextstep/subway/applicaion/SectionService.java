package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
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

	public SectionResponse createSection(final Long id, final SectionRequest sectionRequest) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		Station upStation =
						stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(RuntimeException::new);
		Station downStation =
						stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(RuntimeException::new);
		Section section = sectionRepository.save(Section.of(line, upStation, downStation, sectionRequest.getDistance()));

		return SectionResponse.of(section.getId(), upStation.getId(), downStation.getId(), section.getDistance());
	}

	// 새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.
	private void verifyStationsRelation() {

	}

}
