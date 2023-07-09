package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.section.SectionResponse;
import subway.controller.dto.section.SectionSaveRequest;
import subway.model.line.Line;
import subway.model.line.LineRepository;
import subway.model.section.Section;
import subway.model.section.SectionRepository;
import subway.model.station.Station;
import subway.model.station.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionService(SectionRepository sectionRepository, LineRepository lineRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionSaveRequest sectionSaveRequest) {

        Line line = lineRepository.findById(lineId)
                                  .orElseThrow(() -> new IllegalArgumentException("line id doesn't exist"));

        Section newSection = makeSection(sectionSaveRequest, line);

        if (!line.isAddableSection(newSection)) {
            throw new IllegalArgumentException("추가할 수 없는 구간입니다.");
        }

        sectionRepository.save(newSection);
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

        Line line = lineRepository.findById(lineId)
                                  .orElseThrow(() -> new IllegalArgumentException("line id doesn't exist"));
        return sectionRepository.findByLine(line)
                                .stream()
                                .map(SectionResponse::from)
                                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSectionByStationId(Long lineId, Long stationId) {

        Station targetStation = stationRepository.findById(stationId)
                                                 .orElseThrow(() -> new IllegalArgumentException("station id doesn't exist"));

        Section targetSection = sectionRepository.findByDownStation(targetStation)
                                                 .orElseThrow(() -> new IllegalArgumentException("section doesn't exist"));

        Line line = lineRepository.findById(lineId)
                                  .orElseThrow(() -> new IllegalArgumentException("line id doesn't exist"));

        line.getSections()
            .remove(targetSection);

        sectionRepository.deleteById(targetSection.getId());
    }

    public SectionResponse findById(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                                           .orElseThrow(() -> new IllegalArgumentException("section doesn't exist"));
        return SectionResponse.from(section);
    }
}
