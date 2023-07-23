package subway.section.service;

import org.springframework.stereotype.Service;
import subway.global.exception.BusinessException;
import subway.global.exception.TargetNotFound;
import subway.line.domain.Line;
import subway.line.repository.LineRepository;
import subway.section.domain.Section;
import subway.section.dto.SectionRequest;
import subway.section.repository.SectionRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SectionCreationService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public SectionCreationService(LineRepository lineRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public Section createSection(Long lineId, SectionRequest request){
        Line foundLine = lineRepository.findById(lineId).orElseThrow(IllegalArgumentException::new);
        foundLine.checkIfUpStationIdEqual(request.getUpStationId());
        checkDownStationId(lineId, request.getDownStationId());
        Section section = new Section(request.getDownStationId(), request.getUpStationId(), request.getDistance(), lineId);
        foundLine.changeDownStation(section.getDownStationId());
        return sectionRepository.save(section);
    }

    public Section getSection(Long sectionId){
        return sectionRepository.findById(sectionId).orElseThrow(TargetNotFound::new);
    }

    //    새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
    private void checkDownStationId(Long lineId, Long downStationId){
        List<Section> foundSections = sectionRepository.findByLineId(lineId);
        foundSections.forEach(o -> o.checkIfDownStationAlreadyExisted(downStationId));
    }
}
