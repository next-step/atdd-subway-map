package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.application.dto.CreateLineRequest;
import nextstep.subway.line.application.dto.CreateLineResponse;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.ModifyLineRequest;
import nextstep.subway.station.Station;
import nextstep.subway.station.StationRepository;

@Service
public class LineService {

	private final LineRepository lineRepository;
	private final StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public CreateLineResponse createLine(CreateLineRequest request) {
		Line line = new Line(request.getName(), request.getColor(), request.getUpStationId(),
			request.getDownStationId(), request.getDistance());

		return createLineResponse(lineRepository.save(line));
	}

	@Transactional(readOnly = true)
	public List<LineResponse> getLines() {
		return lineRepository.findAll()
			.stream()
			.map(this::lineResponse)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public LineResponse getLine(Long id) {
		Line line = lineRepository.findById(id).orElseThrow();
		return lineResponse(line);
	}

	@Transactional
	public void modifyLine(Long id, ModifyLineRequest request) {
		Line line = lineRepository.findById(id).orElseThrow();
		line.modifyLine(request.getName(), request.getColor());
	}

	@Transactional(readOnly = true)
	public void deleteLine(Long id) {

	}

	private CreateLineResponse createLineResponse(Line line) {
		Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow();
		Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow();

		return new CreateLineResponse(line, upStation, downStation);
	}

	private LineResponse lineResponse(Line line) {
		Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow();
		Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow();

		return new LineResponse(line, upStation, downStation);
	}
}
