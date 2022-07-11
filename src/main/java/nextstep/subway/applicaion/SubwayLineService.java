package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.domain.SubwayLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
