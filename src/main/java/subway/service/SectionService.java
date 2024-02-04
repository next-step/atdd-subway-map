package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.Line;
import subway.domain.entity.Section;
import subway.domain.entity.Station;
import subway.domain.request.SectionRequest;
import subway.exception.ApplicationException;
import subway.exception.ExceptionMessage;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private final StationService stationService;
    private final LineRepository lineRepository;

    public SectionService(StationService stationService, LineRepository lineRepository) {
        this.stationService = stationService;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).get();
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());

        // 새로운 구간의 상행역이 등록된 노선의 하행 종점역이 아니면 에러
        List<Section> sections = line.getSections();
        Section section = sections.get(sections.size() - 1);
        if (!section.getDownStation().equals(upStation)) {
            throw new ApplicationException(ExceptionMessage.UPSTATION_VALIDATION_EXCEPTION.getMessage());
        }

        // 새로운 구간의 하행역이 노선에 등록되어있는 역과 같으면 에러
        if (isRegisteredStation(sections, downStation)) {
            throw new ApplicationException(ExceptionMessage.DOWNSTATION_VALIDATION_EXCEPTION.getMessage());
        }

        // 새로운 구간의 상행역과 하행역이 같으면 에러
        if (sectionRequest.getUpStationId().equals(sectionRequest.getDownStationId())) {
            throw new ApplicationException(ExceptionMessage.NEW_SECTION_VALIDATION_EXCEPTION.getMessage());
        }

        line.addSection(upStation, downStation, sectionRequest.getDistance());
    }

    private boolean isRegisteredStation(List<Section> sections, Station station) {
        for (Section section : sections) {
            if(section.getUpStation().equals(station)){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).get();
        List<Section> sections = line.getSections();
        Section section = sections.get(sections.size() - 1); // 마지막 구간

        // stationId 는 마지막 하행 종착역 이어야 한다.
        if (!stationId.equals(section.getDownStation().getId())) {
            throw new ApplicationException(ExceptionMessage.DELETE_LAST_SECTION_EXCEPTION.getMessage());
        }

        // 구간이 1개인 경우 삭제할 수 없다.
        if (sections.size() == 1) {
            throw new ApplicationException(ExceptionMessage.DELETE_ONLY_ONE_SECTION_EXCEPTION.getMessage());
        }

        line.deleteSection(section);
    }
}
