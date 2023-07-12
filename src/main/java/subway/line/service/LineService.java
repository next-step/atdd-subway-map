package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.UpdateLineRequest;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.section.dto.CreateSectionRequest;
import subway.section.policy.SectionPolicy;
import subway.section.repository.Section;
import subway.section.repository.SectionRepository;
import subway.station.repository.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public
class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public Line saveLine(CreateLineRequest request) {
        Section initSection = createSection(request.getUpStationId(), request.getDownStationId(), request.getDistance());

        return lineRepository.save(Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .initSection(initSection)
                .build());
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
    }

    @Transactional
    public void updateLineById(Long id, UpdateLineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
        line.changeName(request.getName());
        line.changeColor(request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public Line addSection(Long id, CreateSectionRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
        SectionPolicy.validate(line, request.getUpStationId(), request.getDownStationId());
        line.addSection(createSection(request.getUpStationId(), request.getDownStationId(), request.getDistance()));
        return line;
    }

    private Section createSection(Long upStationId, Long downStationId, Long distance) {
        return sectionRepository.save(Section.builder()
                .upStation(stationRepository.getReferenceById(upStationId))
                .downStation(stationRepository.getReferenceById(downStationId))
                .distance(distance)
                .build());
    }
}
