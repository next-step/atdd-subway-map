package subway.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.domain.Station;
import subway.domain.StationLine;
import subway.domain.StationLineRepository;
import subway.domain.StationRepository;
import subway.exception.EntityNotFoundException;
import subway.service.dto.StationLineRequest;
import subway.service.dto.StationLineResponse;

@Service
@RequiredArgsConstructor
public class StationLineService {
	private final StationRepository stationRepository;
	private final StationLineRepository stationLineRepository;

	@Transactional
	public StationLineResponse createStationLine(StationLineRequest request) {
		final Station upStation = stationRepository.findById(request.getUpStationId())
			.orElseThrow(() -> new EntityNotFoundException("upStation not found"));

		final Station downStation = stationRepository.findById(request.getDownStationId())
			.orElseThrow(() -> new EntityNotFoundException("downStation not found"));

		final StationLine stationLine = StationLine.builder()
			.name(request.getName())
			.color(request.getColor())
			.upStation(upStation)
			.downStation(downStation)
			.distance(request.getDistance())
			.build();

		return StationLineResponse.fromEntity(stationLineRepository.save(stationLine));
	}

	@Transactional(readOnly = true)
	public List<StationLineResponse> getStationLines() {
		return stationLineRepository.findAll()
			.stream()
			.filter(Objects::nonNull)
			.map(StationLineResponse::fromEntity)
			.collect(Collectors.toList());
	}
}
