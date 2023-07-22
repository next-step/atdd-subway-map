package subway.section.service;

import org.springframework.stereotype.Service;
import subway.global.exception.BusinessException;
import subway.global.exception.TargetNotFound;
import subway.line.domain.Line;
import subway.line.repository.LineRepository;
import subway.section.domain.Section;
import subway.section.repository.SectionRepository;

import java.util.List;
import java.util.Optional;


@Service
public class SectionDeletionService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public SectionDeletionService(LineRepository lineRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public void deleteSection(Long lineId, Long stationId){
        Line foundLine = lineRepository.findById(lineId).orElseThrow(TargetNotFound::new);
        checkDownStation(stationId, foundLine);
        List<Section> foundSections = sectionRepository.findByLineId(lineId);
        checkSectionSize(foundSections);
        Section foundLastSection = findLastSection(foundSections, stationId);
        sectionRepository.delete(foundLastSection);
    }

    private Section findLastSection(List<Section> foundSections, Long stationId) {
        Optional<Section> foundSection = foundSections.stream().filter(o -> o.getDownStationId().equals(stationId)).findFirst();
        return foundSection.orElseThrow(IllegalAccessError::new);
    }

    private void checkSectionSize(List<Section> sections) {
        if(sections.size() == 1) throw new BusinessException("제거하려는 지하철 노선의 구간이 1개인 경우 삭제할 수 없습니다.");
    }

    private void checkDownStation(Long stationId, Line foundLine) {
        if(!foundLine.getDownStationId().equals(stationId)) throw new BusinessException("제거하는 지하철 구간이 노선의 마지막 구간이 아닐 경우 삭제할 수 없습니다.");
    }
}
