package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineModifyRequest;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.domain.SubwayLine;
import nextstep.subway.repository.SubwayLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
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
		List<Station> upAndDownStation = getUpAndDownStation(request.getUpStationId(), request.getDownStationId());
		return new SubwayLineResponse(savedLine, upAndDownStation);
	}

	private List<Station> getUpAndDownStation(Long upStationId, Long downStationId) {
		return stationRepository.findAllById(List.of(upStationId, downStationId));
	}

	@Transactional
	public void modifySubwayLine(Long id, SubwayLineModifyRequest request) {
		SubwayLine lineEntity = findSubwayLineEntityById(id);
		lineEntity.modify(request);
	}

	private SubwayLine findSubwayLineEntityById(Long id) {
		return lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
	}

	@Transactional
	public void deleteSubwayLine(Long id) {
		SubwayLine lineEntity = findSubwayLineEntityById(id);
		lineRepository.delete(lineEntity);
	}

	public List<SubwayLineResponse> findAll() {
		return lineRepository.findAll().stream()
				.map(line -> new SubwayLineResponse(
						line, getUpAndDownStation(line.getUpStationId(), line.getDownStationId())))
				.collect(Collectors.toList());
	}

	public SubwayLineResponse findById(Long id) {
		SubwayLine findLine = findSubwayLineEntityById(id);
		return new SubwayLineResponse(findLine, getUpAndDownStation(findLine.getUpStationId(), findLine.getDownStationId()));
	}
}
