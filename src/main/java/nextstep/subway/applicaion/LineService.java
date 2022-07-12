package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        final Line line = lineRepository.save(lineRequest.toEntity());
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        final Line line = lineRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        final List<StationResponse> stationResponses = stationRepository.findAllById(Arrays.asList(line.getUpStationId(), line.getDownStationId()))
                .stream()
                .map(StationService::createStationResponse)
                .collect(Collectors.toList());

        return new LineResponse(line, stationResponses);
    }

    @Transactional
    public LineResponse updateById(Long id, LineRequest lineRequest) {
        final Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        final Line update = line.update(lineRequest.toEntity());
        return createLineResponse(update);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        final Line line = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
        final Long upStationId = Long.parseLong(sectionRequest.getUpStationId());
        final Long downStationId = Long.parseLong(sectionRequest.getDownStationId());
        final Section section = new Section(line, upStationId, downStationId, sectionRequest.getDistance());
        line.addSection(section);

        return new SectionResponse(section.getUpStationId(), section.getDownStationId(), section.getDistance());
    }
}
