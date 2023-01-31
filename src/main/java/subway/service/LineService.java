package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.entity.Line;
import subway.entity.Section;
import subway.model.CreateLineRequest;
import subway.model.LineResponse;
import subway.model.UpdateLineRequest;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(CreateLineRequest req) {
        Section section = Section.of(stationRepository.findByIdInOrderById(
            List.of(
                req.getUpStationId(),
                req.getDownStationId()
            )), req.getDistance()
        );
        Line line = lineRepository.save(Line.create(req.getName(), req.getColor(), section));

        sectionRepository.save(section);
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
            .map(this::createLineResponse)
            .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id).map(this::createLineResponse).orElse(null);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line,
            line.getSections().stream()
                .map(s -> List.of(s.getUpStation(), s.getDownStation()))
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList())
        );
    }
    @Transactional
    public void updateLine(Long id, UpdateLineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.updateNameAndColor(request.getName(), request.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
