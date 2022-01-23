package nextstep.subway.applicaion.section.service;

import nextstep.subway.applicaion.exception.EntityNotFoundException;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.repository.LineRepository;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.section.dto.SectionRequest;
import nextstep.subway.applicaion.section.dto.SectionResponse;
import nextstep.subway.applicaion.station.domain.Station;
import nextstep.subway.applicaion.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;
	private final SectionValidation sectionValidation;

	public SectionService(LineRepository lineRepository, StationRepository stationRepository, SectionValidation sectionValidation) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.sectionValidation = sectionValidation;
	}

	public SectionResponse saveSection(long lineId, SectionRequest sectionRequest) {
		final Line line = lineRepository.findById(lineId)
				.orElseThrow(() -> new EntityNotFoundException(lineId));
		Section section = createSection(sectionRequest);

		sectionValidation.validateStation(line, section);

		line.addSection(section);

		return SectionResponse.from(section);
	}

	private Section createSection(SectionRequest sectionRequest) {
		final Station downStation = getStation(sectionRequest.getDownStationId());
		final Station upStation = getStation(sectionRequest.getUpStationId());

		final int distance = sectionRequest.getDistance();

		return Section.of(upStation, downStation, distance);
	}

	private Station getStation(long stationId) {
		return stationRepository.findById(stationId).orElseThrow(() -> new EntityNotFoundException(stationId));
	}
}
