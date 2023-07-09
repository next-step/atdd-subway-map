package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.section.SectionResponse;
import subway.controller.dto.section.SectionSaveRequest;
import subway.model.line.Line;
import subway.model.line.LineService;
import subway.model.section.Section;
import subway.model.section.SectionService;
import subway.model.station.Station;
import subway.model.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SectionCompositeService {

    private final SectionService sectionService;
    private final LineService lineService;
    private final StationRepository stationRepository;

    public SectionCompositeService(SectionService sectionService, LineService lineService, StationRepository stationRepository) {
        this.sectionService = sectionService;
        this.lineService = lineService;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionSaveRequest sectionSaveRequest) {

        Line line = lineService.findById(lineId);

        Section newSection = makeSection(sectionSaveRequest, line);

        if (!line.isAddableSection(newSection)) {
            throw new IllegalArgumentException("추가할 수 없는 구간입니다.");
        }

        sectionService.save(newSection);
        line.addSection(newSection);

        return SectionResponse.from(newSection);
    }

    private Section makeSection(SectionSaveRequest sectionSaveRequest, Line line) {
        Station upStation = stationRepository.findById(sectionSaveRequest.getUpStationId())
                                             .orElseThrow(() -> new IllegalArgumentException("station id doesn't exist"));
        Station downStation = stationRepository.findById(sectionSaveRequest.getDownStationId())
                                               .orElseThrow(() -> new IllegalArgumentException("station id doesn't exist"));

        return Section.builder()
                      .upStation(upStation)
                      .downStation(downStation)
                      .distance(sectionSaveRequest.getDistance())
                      .line(line)
                      .build();
    }

    public List<SectionResponse> findSectionsByLine(Long lineId) {

        Line line = lineService.findById(lineId);

        return sectionService.findByLine(line)
                             .stream()
                             .map(SectionResponse::from)
                             .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSectionByStationId(Long lineId, Long stationId) {

        Station targetStation = stationRepository.findById(stationId)
                                                 .orElseThrow(() -> new IllegalArgumentException("station id doesn't exist"));

        Section targetSection = sectionService.findByDownStation(targetStation);

        Line line = lineService.findById(lineId);

        if (!line.isDeletableStation(targetStation)) {
            throw new IllegalArgumentException("삭제할 수 없는 정거장입니다.");
        }

        line.getSections().remove(targetSection);

        sectionService.deleteById(targetSection.getId());
    }

    public SectionResponse findById(Long sectionId) {
        Section section = sectionService.findById(sectionId);
        return SectionResponse.from(section);
    }
}
