package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.repository.SubwayLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubwayLineService {

	private final SubwayLineRepository lineRepository;
	private final StationRepository stationRepository;

	@Transactional
	public SubwayLineResponse createSubwayLine(SubwayLineRequest request) {
		SubwayLine savedLine = lineRepository.save(request.toEntity());
		List<Station> findStations = stationRepository.findAllById(
				List.of(request.getUpStationId(),
						request.getDownStationId())
		);

		return new SubwayLineResponse(savedLine, findStations);
	}

	public List<SubwayLineResponse> findAll() {
		return lineRepository.findAll().stream()
				.map(line -> new SubwayLineResponse(
						line,
						stationRepository.findAllById(List.of(line.getUpStationId(),
								line.getDownStationId()))
				))
				.collect(Collectors.toList());
	}
}
