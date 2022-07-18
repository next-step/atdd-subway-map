package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.repository.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.repository.StationRepository;
import nextstep.subway.exception.SubwayException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {
	private final LineRepository lineRepository;
	private final StationRepository stationRepository;
	private final StationService stationService;

	@Transactional
	public LineResponse saveLine(LineRequest lineRequest) {
		Line line = Line.builder()
			.name(lineRequest.getName())
			.color(lineRequest.getColor())
			.upStationId(lineRequest.getUpStationId())
			.downStationId(lineRequest.getDownStationId())
			.distance(lineRequest.getDistance())
			.build();
		lineRepository.save(line);
		return createLineResponse(line);
	}

	public List<LineResponse> findAllLines() {
		return lineRepository.findAll().stream()
			.map(this::createLineResponse)
			.collect(Collectors.toList());
	}

	public LineResponse findLine(Long id) {
		Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayException("no subway"));
		return createLineResponse(line);
	}

	@Transactional
	public void updateLine(Long id, String name, String color) {
		Line line = lineRepository.findById(id).orElseThrow(() -> new SubwayException("no subway"));
		line.update(name, color);
	}

	@Transactional
	public void deleteLine(Long id) {
		lineRepository.deleteById(id);
	}

	private LineResponse createLineResponse(Line line) {
		return LineResponse.builder()
			.id(line.getId())
			.name(line.getName())
			.color(line.getColor())
			.sections(createSectionsResponse(line.getSections()))
			.build();
	}

	private List<SectionResponse> createSectionsResponse(List<Section> sections) {
		return sections.stream()
			.map((section -> {
				Station upStation = stationRepository.findById(section.getUpStationId())
					.orElseThrow(() -> new SubwayException("no subway"));
				Station downStation = stationRepository.findById(section.getDownStationId())
					.orElseThrow(() -> new SubwayException("no subway"));

				return new SectionResponse(stationService.createStationResponse(upStation),
					stationService.createStationResponse(downStation),
					section.getDistance());
			})).collect(Collectors.toList());
	}
}
