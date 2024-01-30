package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionService;
import subway.station.Station;
import subway.station.StationRepository;
import subway.station.StationResponse;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionService sectionService;

    @Transactional
    public LineResponse create(LineCreateRequest request) {
        Station upstation = stationRepository.findById(request.getUpstationId()).orElseThrow(EntityNotFoundException::new);
        Station downstation = stationRepository.findById(request.getDownstationId()).orElseThrow(EntityNotFoundException::new);

        Line line = LineCreateRequest.toEntity(request, upstation, downstation);

        lineRepository.save(line);
        Section section = sectionService.init(line);
        line.addSection(section);

        return LineResponse.from(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAll() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        return LineResponse.from(line);
    }

    @Transactional
    public LineResponse update(Long id, LineUpdateRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        line.updateName(request.getName());
        line.updateColor(request.getColor());

        return LineResponse.from(line);
    }

    public void delete(Long id) {
        lineRepository.deleteById(id);
    }
}
