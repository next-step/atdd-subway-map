package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.repository.SectionRepository;
import subway.station.domain.Station;

import java.util.Comparator;
import java.util.List;

@Transactional
@Service
public class SectionService {

    private static final String NOT_FOUND_SECTION_MESSAGE = "등록된 구간이 없습니다.";
    private static final String ALREADY_EXITS_SECTION_MESSAGE = "이미 등록된 역입니다.";

    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section createSection(Line line, Station station) {
        return sectionRepository.save(new Section(line, station));
    }

    public void createSections(List<Section> sections) {
        sectionRepository.saveAll(sections);
    }

    public Section getSection(Line line, Station station) {
        return sectionRepository.findByLineAndStation(line, station)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_SECTION_MESSAGE));
    }

    public Section getLastSectionByLine(Line line) {
        return sectionRepository.findTopByLineOrderByIdDesc(line)
                .orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_SECTION_MESSAGE));
    }

    public void validateSection(Line line, Station station) {
        sectionRepository.findByLineAndStation(line, station)
                .ifPresent(section -> {
                    throw new IllegalArgumentException(ALREADY_EXITS_SECTION_MESSAGE);
                });
    }

    public void deleteSection(Section section) {
        sectionRepository.delete(section);
    }

    public List<Section> getSections(Line line) {
        return sectionRepository.findAllByLine(line);
    }

    public Section getLastSection(List<Section> sections) {
        return sections.stream()
                .max(Comparator.comparing(Section::getId)).orElseThrow(() -> new IllegalArgumentException(NOT_FOUND_SECTION_MESSAGE));
    }

}
