package nextstep.subway.applicaion.section.service;

import nextstep.subway.applicaion.exception.EntityNotFoundException;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.repository.LineRepository;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.section.dto.SectionRequest;
import nextstep.subway.applicaion.section.dto.SectionResponse;
import nextstep.subway.applicaion.section.repository.SectionRepository.SectionRepository;
import nextstep.subway.applicaion.station.domain.Station;
import nextstep.subway.applicaion.station.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {

	private final SectionRepository sectionRepository;
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;
	private final SectionValidation sectionValidation;

	public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository, SectionValidation sectionValidation) {
		this.sectionRepository = sectionRepository;
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
		this.sectionValidation = sectionValidation;
	}

	public SectionResponse saveSection(long lineId, SectionRequest sectionRequest) {
		Section section = createSection(sectionRequest);
		final Line line = getLine(lineId);
		sectionValidation.validateCreateStation(line, section);

		sectionRepository.save(section);

		addSectionToLine(line, section);

		return SectionResponse.from(section);
	}

	private Line addSectionToLine(Line line, Section section) {
		line.addSection(section);
		return lineRepository.save(line);
	}

	private Section createSection(SectionRequest sectionRequest) {
		final Station downStation = getStation(sectionRequest.getDownStationId());
		final Station upStation = getStation(sectionRequest.getUpStationId());

		final int distance = sectionRequest.getDistance();

		return Section.of(upStation, downStation, distance);
	}

	public void removeSection(long lineId, long stationId) {
		final Station station = getStation(stationId);
		final Line line = getLine(lineId);
		sectionValidation.validateRemoveSection(line, station);
	}

	private Line getLine(long lineId) {
		return lineRepository.findById(lineId)
				.orElseThrow(() -> new EntityNotFoundException(lineId));
	}

	private Station getStation(long stationId) {
		return stationRepository.findById(stationId).orElseThrow(() -> new EntityNotFoundException(stationId));
	}
}
