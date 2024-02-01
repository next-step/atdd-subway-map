package subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.Line;
import subway.repository.LineRepository;

@Service
@Transactional(readOnly = true)
public class LineService {
	private final StationService stationService;
	private final LineRepository lineRepository;

	public LineService(StationService stationService, LineRepository lineRepository) {
		this.stationService = stationService;
		this.lineRepository = lineRepository;
	}

	@Transactional
	public LineResponse saveLines(LineRequest request) {
		StationResponse upStation = getStations(request.getUpStationId());
		StationResponse downStation = getStations(request.getDownStationId());

		Line line =
				lineRepository.save(
						new Line(
								request.getName(),
								request.getColor(),
								request.getUpStationId(),
								request.getDownStationId(),
								request.getDistance()));

		return createLineResponse(line, upStation, downStation);
	}

	public List<LineResponse> getLines() {
		return lineRepository.findAll().stream()
				.map(
						line ->
								createLineResponse(
										line, getStations(line.getUpStationId()), getStations(line.getDownStationId())))
				.collect(Collectors.toList());
	}

	private StationResponse getStations(Long stationId) {
		return stationService.findStationById(stationId);
	}

	private LineResponse createLineResponse(
			Line line, StationResponse upStation, StationResponse downStation) {
		return new LineResponse(
				line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation));
	}
}
