package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineModifyRequest;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineRequest;
import nextstep.subway.applicaion.dto.subwayline.SubwayLineResponse;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.SectionRepository;
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
	private final SectionRepository sectionRepository;

	@Transactional
	public SubwayLineResponse createSubwayLine(SubwayLineRequest request) {
		Section section = sectionRepository.save(new Section(request.getUpStationId(), request.getDownStationId(), request.getDistance()));
		SubwayLine savedLine = lineRepository.save(request.toEntity(section));
		List<Station> upAndDownStation = getUpAndDownStation(request.getUpStationId(), request.getDownStationId());
		return new SubwayLineResponse(savedLine, upAndDownStation);
	}

	@Transactional
	public void modifySubwayLine(Long id, SubwayLineModifyRequest request) {
		SubwayLine subwayLine = findSubwayLineById(id);
		subwayLine.modify(request);
	}

	@Transactional
	public void deleteSubwayLine(Long id) {
		SubwayLine subwayLine = findSubwayLineById(id);
		lineRepository.delete(subwayLine);
	}

	public List<SubwayLineResponse> findAll() {
		return lineRepository.findAll().stream()
				.map(line -> new SubwayLineResponse(
						line, getUpAndDownStation(line.getUpStationId(), line.getDownStationId())))
				.collect(Collectors.toList());
	}

	public SubwayLineResponse findById(Long id) {
		SubwayLine subwayLine = findSubwayLineById(id);
		return new SubwayLineResponse(subwayLine, getUpAndDownStation(subwayLine.getUpStationId(), subwayLine.getDownStationId()));
	}

	private List<Station> getUpAndDownStation(Long upStationId, Long downStationId) {
		return stationRepository.findAllById(List.of(upStationId, downStationId));
	}

	private SubwayLine findSubwayLineById(Long id) {
		return lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
	}
}
