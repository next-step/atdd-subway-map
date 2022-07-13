package nextstep.subway.domain.service;

import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.entity.Section;
import nextstep.subway.domain.entity.Station;
import nextstep.subway.infra.SectionRepository;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepository sectionRepository;

    public Section findSection(final Long id) {
        return sectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("section is not found"));
    }

    public Section save(final Section section) {
        return sectionRepository.save(section);
    }

    public List<Section> findByLine(final Line line) {
        return sectionRepository.findByLine(line);
    }

    public void delete(Section section) {
        sectionRepository.delete(section);
    }

    public void deleteAll(List<Section> sections) {
        sectionRepository.deleteAllInBatch(sections);
    }

    public boolean existsByLineAndDownStation(final Line line, final Station downStation) {
        return sectionRepository.existsByLineAndDownStation(line, downStation);
    }

    public boolean existsByLineAndUpStation(final Line line, final Station upStation) {
        return sectionRepository.existsByLineAndUpStation(line, upStation);
    }

    public boolean existsByLineAnyStation(final Line line, final Station station) {
        return sectionRepository.existsByLineAndUpStationOrDownStation(line, station, station);
    }

}
