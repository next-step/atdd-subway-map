package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.service.input.LineLoadUseCase;
import subway.application.service.input.SectionLoadUseCase;
import subway.application.service.output.LineLoadRepository;
import subway.domain.Line;
import subway.domain.LineLoadDto;
import subway.domain.Station;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
class LineLoadService implements LineLoadUseCase {

    private final LineLoadRepository lineLoadRepository;
    private final SectionLoadUseCase sectionLoadUseCase;

    public LineLoadService(LineLoadRepository lineLoadRepository, SectionLoadUseCase sectionLoadUseCase) {
        this.lineLoadRepository = lineLoadRepository;
        this.sectionLoadUseCase = sectionLoadUseCase;
    }

    @Override
    public Line loadLine(Long loadLineId) {
        Line line = lineLoadRepository.loadLine(loadLineId);
        return new Line(line.getId(), line.getName(), line.getColor());
    }

    @Override
    public LineLoadDto loadLineDto(Long loadLineId) {
        Line line = lineLoadRepository.loadLine(loadLineId);
        Set<Station> stations = getStations(loadLineId);
        return new LineLoadDto(line.getId(), line.getName(), line.getColor(), new ArrayList<>(stations));
    }

    @Override
    public List<LineLoadDto> loadLineDtos() {
        List<Line> lines = lineLoadRepository.loadLines();
        return lines.stream().map(line -> {
            Set<Station> stations = getStations(line.getId());
            return new LineLoadDto(line.getId(), line.getName(), line.getColor(), new ArrayList<>(stations));
        }).collect(Collectors.toList());
    }

    private Set<Station> getStations(Long loadLineId) {
        Set<Station> stations = new LinkedHashSet<>();

        sectionLoadUseCase.loadLineSection(loadLineId).forEach(section -> {
            stations.add(section.getUpStation());
            stations.add(section.getDownStation());
        });

        return stations;
    }

}
