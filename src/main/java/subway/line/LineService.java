package subway.line;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionCreateRequest;
import subway.section.SectionRepository;
import subway.station.Station;
import subway.station.StationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public Line createLine(LineCreateRequest lineCreateRequest) {
        Station downStation = stationRepository.findById(lineCreateRequest.getDownStationId())
                .orElseThrow(NoSuchElementException::new);
        Station upStation = stationRepository.findById(lineCreateRequest.getUpStationId())
                .orElseThrow(NoSuchElementException::new);

        Line line = Line.builder()
                .color(lineCreateRequest.getColor())
                .name(lineCreateRequest.getName())
                .build();

        line = lineRepository.save(line);

        Section section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineCreateRequest.getDistance())
                .build();
        section.setLine(line);

        sectionRepository.save(section);

        return line;
    }

    public List<Line> getLines() {
        return lineRepository.findAll();
    }

    public Line getLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public void updateLine(Long id, LineChangeRequest lineChangeRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);

        line.updateColor(lineChangeRequest.getColor());
        line.updateName(lineChangeRequest.getName());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public Section createSection(Long id, SectionCreateRequest sectionCreateRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
        Station downStation = stationRepository.findById(sectionCreateRequest.getDownStationId())
                .orElseThrow(NoSuchElementException::new);
        Station upStation = stationRepository.findById(sectionCreateRequest.getUpStationId())
                .orElseThrow(NoSuchElementException::new);

        Section section = Section.builder().downStation(downStation).upStation(upStation).build();
        validateSection(line, section);
        section.setLine(line);

        return sectionRepository.save(section);
    }

    private void validateSection(Line line, Section section) {
        List<Long> stationIds = line.getSections().stream().flatMap(
                s -> {
                    Long downStationId = s.getDownStation().getId();
                    Long upStationId = s.getUpStation().getId();

                    return Arrays.asList(downStationId, upStationId).stream();
                }
        ).collect(Collectors.toList());

        List<Long> newSectionStationIds
                = Arrays.asList(section.getDownStation().getId(), section.getUpStation().getId());

        if (stationIds.containsAll(newSectionStationIds)) {
            throw new IllegalArgumentException();
        }
        if (!(stationIds.contains(newSectionStationIds.get(0)) || stationIds.contains(newSectionStationIds.get(1)))) {
            throw new IllegalArgumentException();
        }
    }
}
