package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.domain.SubwayLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubwayLineService {

	private final SubwayLineRepository lineRepository;
	private final StationRepository stationRepository;

	@Transactional
	public SubwayLineResponse createSubwayLine(SubwayLineRequest request) {
		SubwayLine savedLine = lineRepository.save(request.toEntity());

		Station upStation = stationRepository.findById(request.getUpStationId())
				.orElseThrow(NoSuchElementException::new);
		Station downStation = stationRepository.findById(request.getDownStationId())
				.orElseThrow(NoSuchElementException::new);

		return new SubwayLineResponse(savedLine, List.of(upStation, downStation));
	}
}
