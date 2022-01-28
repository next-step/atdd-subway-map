package nextstep.subway.application.service;

import nextstep.subway.application.dto.request.LineRequest;
import nextstep.subway.application.dto.request.SectionRequest;
import nextstep.subway.application.dto.response.LineResponse;
import nextstep.subway.application.dto.response.SectionResponse;
import nextstep.subway.application.exception.UniqueKeyExistsException;
import nextstep.subway.application.repository.LineRepository;
import nextstep.subway.application.repository.StationRepository;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class LineService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public LineResponse saveLine(LineRequest request) {
		validateCreateLine(request);

		final Line line = request.toEntity();

		final Line sectionAddedLine = addLastStationSection(line, request);

		return LineResponse.from(
				lineRepository.save(
						sectionAddedLine));
	}

	public SectionResponse saveSection(long lineId, SectionRequest sectionRequest) {
		Line line = findLineById(lineId);
		Station downStation = getStation(sectionRequest.getDownStationId());
		Station upStation = getStation(sectionRequest.getUpStationId());
		Section createdSection = Section.of(upStation, downStation, sectionRequest.getDistance());

		line.addSection(createdSection);
		lineRepository.save(line);
		return SectionResponse.from(line.getLastSection());
	}

	public LineResponse updateLine(long id, LineRequest request) {
		validateUpdateLine(id, request);

		final Line line = findLineById(id);

		line.update(request.toEntity());

		return LineResponse.from(line);
	}

	public void deleteLine(long id) {
		lineRepository.deleteById(id);
	}

	private Line addLastStationSection(Line line, LineRequest request) {
		if (request.isStationNotProvided()) {
			return line;
		}
		final Station upStation = getStation(request.getUpStationId());
		final Station downStation = getStation(request.getDownStationId());

		line.addSection(Section.of(upStation, downStation, request.getDistance()));
		return line;
	}

	private Station getStation(long stationId) {
		return stationRepository.findById(stationId)
				.orElseThrow(EntityNotFoundException::new);
	}

	private Line findLineById(long id) {
		return lineRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);
	}

	private void validateUpdateLine(long id, LineRequest line) {
		if (lineRepository.existsByNameAndIdIsNot(line.getName(), id)) {
			throw new UniqueKeyExistsException(line.getName());
		}
	}

	private void validateCreateLine(LineRequest line) {
		if (lineRepository.existsByName(line.getName())) {
			throw new UniqueKeyExistsException(line.getName());
		}
	}

	public void deleteSection(long lineId, long toRemoveLastDownStationId) {
		final Line line = findLineById(lineId);
		Station toRemoveLastDownStation = getStation(toRemoveLastDownStationId);
		line.removeSection(toRemoveLastDownStation);
	}

}
