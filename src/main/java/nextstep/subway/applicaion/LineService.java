package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.*;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import nextstep.subway.error.exception.EntityDuplicateException;
import nextstep.subway.error.exception.InvalidValueException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final SectionService sectionService;

    public LineService(LineRepository lineRepository,
                       SectionService sectionService) {
        this.lineRepository = lineRepository;
        this.sectionService = sectionService;
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void checkDuplicated(String name) {
        lineRepository.findByName(name).ifPresent(l -> {
            throw new EntityDuplicateException();
        });
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

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = findById(id);
        List<StationResponse> stations = line.getStations()
                        .stream().map(StationResponse::of)
                        .collect(Collectors.toList());
        return LineResponse.of(line, stations);
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
        if (!line.isValidNewSection(
                sectionRequest.getUpStationId(),
                sectionRequest.getDownStationId())) {
            throw new InvalidValueException();
        }

        Section section = sectionService.createSection(line, sectionRequest);
        line.addSection(section);

        return SectionResponse.of(section);
    }

    public void removeSection(Long lineId, Long stationId) {
        Line line = findById(lineId);
        Section section = line.findSection(stationId);

        if (!line.isPossibleToRemove(section)) {
            throw new InvalidValueException();
        }
        line.removeSection(section);
    }


}
