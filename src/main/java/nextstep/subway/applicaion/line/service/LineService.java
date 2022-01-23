package nextstep.subway.applicaion.line.service;

import nextstep.subway.applicaion.exception.UniqueKeyExistsException;
import nextstep.subway.applicaion.line.domain.Line;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.line.dto.LineResponse;
import nextstep.subway.applicaion.line.repository.LineRepository;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.station.domain.Station;
import nextstep.subway.applicaion.station.repository.StationRepository;
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

		final Line sectionAddedLine = addSection(line, request);

		return LineResponse.from(
				lineRepository.save(
						sectionAddedLine));
	}

	private Line addSection(Line line, LineRequest request) {
		if (request.isStationNotProvided()) {
			return line;
		}
		final Station upStation = getUpStation(request);
		final Station downStation = getDownStation(request);

		line.addSection(Section.of(upStation, downStation, request.getDistance()));
		return line;
	}

	private Station getUpStation(LineRequest request) {
		return stationRepository.findById(request.getUpStationId())
				.orElseThrow(EntityNotFoundException::new);
	}

	private Station getDownStation(LineRequest request) {
		return stationRepository.findById(request.getDownStationId())
				.orElseThrow(EntityNotFoundException::new);
	}

	private Line findLineById(long id) {
		return lineRepository.findById(id)
				.orElseThrow(EntityNotFoundException::new);
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
}
