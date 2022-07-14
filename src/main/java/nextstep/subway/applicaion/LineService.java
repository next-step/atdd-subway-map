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
        Line line = lineRepository.save(toLine(lineRequest));
        return new LineResponse(line.getId(), line.getName(), line.getColor(),
                List.of(stationService.findStation(line.getUpStationId()),
                        stationService.findStation(line.getDownStationId())));
    }

    private Line toLine(LineRequest lineRequest) {
        Long upStationId = lineRequest.getUpStationId();
        Long downStationId = lineRequest.getDownStationId();
        return new Line(lineRequest.getName(), lineRequest.getColor(), stationService.findStation(upStationId),
                stationService.findStation(downStationId), lineRequest.getDistance());
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

    public void deleteSectionFromLine(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(EntityNotFoundException::new);
        line.deleteSection(stationId);
    }
}
