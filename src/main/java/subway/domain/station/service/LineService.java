package subway.domain.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.api.dto.LineRequest;
import subway.api.dto.LineResponse;
import subway.domain.station.entity.Line;
import subway.domain.station.entity.Station;
import subway.domain.station.repository.LineRepository;
import subway.domain.station.repository.StationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
		return createLineResponse(line);
	}

	private LineResponse createLineResponse(Line line) {
		List<Station> stations = new ArrayList<>();
		Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow(EntityNotFoundException::new);
		Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow(EntityNotFoundException::new);
		stations.add(upStation);
		stations.add(downStation);

		return new LineResponse(
				line.getId(),
				line.getName(),
				line.getColor(),
				stations
		);
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll().stream()
				.map(this::createLineResponse)
				.collect(Collectors.toList());
	}
}
