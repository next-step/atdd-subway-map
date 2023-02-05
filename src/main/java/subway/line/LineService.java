package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.DomainException;
import subway.common.DomainExceptionType;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public LineResponse saveLine(CreateLineRequest request) {
        Line line = lineRepository.save(request.toEntity());

        Optional<Section> section = sectionRepository.findByDownStationId(line.getDownStationId());

        if (section.isPresent()) {
            return LineResponse.entityToResponse(line);
        }

        Optional<Station> downStation = stationRepository.findById(line.getDownStationId());
        Optional<Station> upStation = stationRepository.findById(line.getUpStationId());

        if (downStation.isEmpty() || upStation.isEmpty())
            throw new DomainException(DomainExceptionType.NO_STATION);

        line.addSection(
                new Section(
                        line.getId(), downStation.get(), upStation.get(), line.getLineDistance()));

        return LineResponse.entityToResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::entityToResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id).map(LineResponse::entityToResponse).orElse(null);
    }

    @Transactional
    public void updateLineById(Long id, UpdateLineRequest request) {
        Line line =
                lineRepository
                        .findById(id)
                        .orElseThrow(() -> new DomainException(DomainExceptionType.NO_LINE));

        line.updateNameAndColor(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
