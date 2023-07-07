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


    public void delete(Section section) {
        sectionRepository.delete(section);
    }
}
