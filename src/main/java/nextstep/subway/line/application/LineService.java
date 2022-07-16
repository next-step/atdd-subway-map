package nextstep.subway.line.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.line.Line;
import nextstep.subway.line.LineRepository;
import nextstep.subway.line.application.dto.CreateLineRequest;
import nextstep.subway.line.application.dto.CreateLineResponse;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.application.dto.ModifyLineRequest;
import nextstep.subway.line.application.dto.ModifyLineResponse;
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

	public CreateLineResponse createLine(CreateLineRequest request) {
		Line line = new Line(request.getName(), request.getColor(), request.getUpStationId(),
			request.getDownStationId(), request.getDistance());

		return createLineResponse(lineRepository.save(line));
	}

	public List<LineResponse> getLines() {
		return lineRepository.findAll()
			.stream()
			.map(this::lineResponse)
			.collect(Collectors.toList());
	}

	public LineResponse getLine(Long id) {
		return null;
	}

	public ModifyLineResponse modifyLine(Long id, ModifyLineRequest request) {
		return null;
	}

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
