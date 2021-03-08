package nextstep.subway.line.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.NotFoundLineException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.NotFoundStationException;

@Service
@Transactional
public class LineService {

	private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineService(StationRepository stationRepository,
		LineRepository lineRepository) {
		this.stationRepository = stationRepository;
		this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
		final Station upStation = findStationById(request.getUpStationId());
		final Station downStation = findStationById(request.getDownStationId());
		return LineResponse.of(
			lineRepository.save(
				Line.of(request.getName(), request.getColor(), upStation, downStation, request.getDistance()))
		);
    }

	@Transactional(readOnly = true)
	public List<LineResponse> findLines() {
		return lineRepository.findAll().stream()
			.map(LineResponse::of)
			.collect(toList());
	}

	@Transactional(readOnly = true)
	public LineResponse findLine(Long lineId) {
		return lineRepository.findById(lineId)
			.map(LineResponse::of)
			.orElseThrow(() -> new NotFoundLineException(lineId));
	}

	public void updateLine(Long lineId, LineRequest lineRequest) {
		final Line findLine = findLineById(lineId);

		findLine.update(lineRequest.toLine());
	}

	public void deleteLine(Long lineId) {
		lineRepository.delete(findLineById(lineId));
	}

	public LineResponse addSection(Long lineId, LineRequest lineRequest) {
		final Line line = findLineById(lineId);
		final Station upStation = findStationById(lineRequest.getUpStationId());
		final Station downStation = findStationById(lineRequest.getDownStationId());

		line.addSection(upStation, downStation, lineRequest.getDistance());
		return LineResponse.of(line);
	}

	public void deleteSection(Long lineId, Long stationId) {
		final Line line = findLineById(lineId);
		final Station station = findStationById(stationId);

		line.deleteSection(station);
	}

	private Line findLineById(Long lineId) {
		return lineRepository.findById(lineId)
			.orElseThrow(() -> new NotFoundLineException(lineId));
	}

	private Station findStationById(Long stationId) {
		return stationRepository.findById(stationId)
			.orElseThrow(() -> new NotFoundStationException(stationId));
	}
}
