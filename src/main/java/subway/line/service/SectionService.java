package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.model.Line;
import subway.line.model.Section;
import subway.line.repository.SectionRepository;
import subway.station.model.Station;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public Section saveSection(Line line,
                               Station upStation,
                               Station downStation,
                               Long distance) {
        Section section = Section.builder()
                .downStation(downStation)
                .upStation(upStation)
                .line(line)
                .distance(distance)
                .build();
        return sectionRepository.save(section);
    }
}
