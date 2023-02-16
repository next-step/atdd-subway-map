package subway.services;

import java.util.List;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.request.LineRequest;
import subway.dto.request.SectionRequest;
import subway.models.Line;
import subway.models.Section;
import subway.models.Sections;
import subway.models.Station;
import subway.repositories.LineRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    @Transactional
    public Line saveLine(LineRequest.Create lineRequest) {
        Station upStation = stationService.findById(lineRequest.getUpStationId());
        Station downStation = stationService.findById(lineRequest.getDownStationId());

        Line line = Line.builder()
            .name(lineRequest.getName())
            .color(lineRequest.getColor())
            .upStation(upStation)
            .downStation(downStation)
            .distance(lineRequest.getDistance())
            .build();

        return lineRepository.save(line);
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    public Line findById(Long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void update(Long id, LineRequest.Update lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        line.update(lineRequest.getName(), lineRequest.getColor());
        lineRepository.save(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public Line addSection(Long id, SectionRequest request) {
        Line line = findById(id);
        Station upStation = stationService.findById(request.getUpStationId());
        Station downStation = stationService.findById(request.getDownStationId());

        line.validateSectionForAdd(upStation, downStation);

        line.addSection(Section.builder()
            .line(line)
            .upStation(upStation)
            .downStation(downStation)
            .distance(request.getDistance())
            .build());

        return lineRepository.save(line);
    }

    @Transactional
    public void deleteSection(Long id, Long stationId) {
        Line line = findById(id);
        Station station = stationService.findById(stationId);

        line.validateStationForRemove(station);

        line.removeLastSection();
    }
}
