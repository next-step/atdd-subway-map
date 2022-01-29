package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.error.exception.EntityDuplicateException;
import nextstep.subway.error.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository,
                       StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public LineResponse saveLine(LineRequest lineRequest) {
        checkDuplicated(lineRequest.getName());

        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        addSection(line.getId(),new SectionRequest(
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()));

        return LineResponse.of(line);
    }

    private void checkDuplicated(String name) {
        lineRepository.findByName(name).ifPresent(line -> {
            throw new EntityDuplicateException(name);
        });
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> LineResponse.of(line, getStationResponses(line)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = findById(id);
        List<StationResponse> stations = getStationResponses(line);
        return LineResponse.of(line, stations);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException(id);
                });
    }

    private List<StationResponse> getStationResponses(Line line) {
        return line.getStations()
                .stream().map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.edit(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.of(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public SectionResponse addSection(Long id, SectionRequest sectionRequest) {
        Line line = findById(id);
        line.validateNewSection(
                sectionRequest.getUpStationId(),sectionRequest.getDownStationId());

        Section section = createSection(line, sectionRequest);
        line.addSection(section);

        return SectionResponse.of(section);
    }

    public Section createSection(Line line, SectionRequest sectionRequest) {
        Station upStation = stationService.findStationById(sectionRequest.getUpStationId());
        Station downStation = stationService.findStationById(sectionRequest.getDownStationId());
        return new Section(line, upStation, downStation, sectionRequest.getDistance());
    }

    public void removeSection(Long lineId, Long stationId) {
        Line line = findById(lineId);
        line.removeSection(stationId);
    }

}
