package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(lineRequest.toDomain());

        return new LineResponse(line.getId(), line.getName(), line.getColor(),
                List.of(stationService.findStation(line.getUpStationId()),
                        stationService.findStation(line.getDownStationId())));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                List.of(stationService.findStation(line.getUpStationId()),
                        stationService.findStation(line.getDownStationId()))
        );
    }

    public LineResponse findLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return new LineResponse(line.getId(), line.getName(), line.getColor(),
                List.of(stationService.findStation(line.getUpStationId()),
                        stationService.findStation(line.getDownStationId())));
    }

    @Transactional
    public void updateLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        line.updateLineContent(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void addSectionToLine(Long id, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Section section = toSection(line, sectionRequest);
        line.addSection(section);
    }

    private Section toSection(Line line, SectionRequest sectionRequest) {
        return new Section(line
                , stationService.findStation(sectionRequest.getUpStationId())
                , stationService.findStation(sectionRequest.getDownStationId())
                , sectionRequest.getDistance());
    }
}
