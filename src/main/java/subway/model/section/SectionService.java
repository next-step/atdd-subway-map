package subway.model.section;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.model.line.Line;
import subway.model.station.Station;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section save(Section newSection) {
        return sectionRepository.save(newSection);
    }

    public Section findById(Long lineId) {
        return sectionRepository.findById(lineId)
                                .orElseThrow(() -> new IllegalArgumentException("section id doesn't exist"));
    }

    public void deleteById(Long sectionId) {
        sectionRepository.deleteById(sectionId);
    }

    public List<Section> findByLine(Line line) {
        return sectionRepository.findByLine(line);
    }

    public Section findByDownStation(Station targetStation) {
        return sectionRepository.findByDownStation(targetStation)
                                .orElseThrow(() -> new IllegalArgumentException("section doesn't exist"));
    }
}
