package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.Line;
import subway.station.domain.line.LineRepository;
import subway.station.domain.section.Section;
import subway.station.domain.section.SectionRepository;
import subway.station.domain.station.Station;
import subway.station.domain.station.StationRepository;
import subway.station.service.dto.SectionSaveRequest;
import subway.station.service.dto.SectionSaveResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectionService {
    private final SectionRepository sectionRepository;

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository, StationRepository stationRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public SectionSaveResponse save(Long id, SectionSaveRequest sectionSaveRequest) {
        List<Section> sections = findLine(id).getSections();
        if (!sections.get(sections.size() - 1).getDownStation().getId().equals(sectionSaveRequest.getUpStationId())) {
            return null;
        }
        for (Section section : sections) {
            if (section.getUpStation().getId().equals(sectionSaveRequest.getDownStationId())) {
                return null;
            }
            if (section.getDownStation().getId().equals(sectionSaveRequest.getDownStationId())) {
                return null;
            }
        }
        Section section = saveSection(id, sectionSaveRequest);

        return toDtoForSaveResponse(section);
    }

    @Transactional
    public boolean delete(Long lineId, Long stationId) {
        List<Section> sections = findLine(lineId).getSections();
        Long upStationId = sections.get(sections.size() - 1).getUpStation().getId();
        Long downStationId = sections.get(sections.size() - 1).getDownStation().getId();

        if(!(upStationId.equals(findStation(stationId).getId()) || downStationId.equals(findStation(stationId).getId()))) {
            return false;
        }
        if(sections.size() == 1) {
            return false;
        }
        sectionRepository.delete(sections.get(sections.size()-1));
        return true;
    }

    private SectionSaveResponse toDtoForSaveResponse(Section section) {
        return SectionSaveResponse.builder()
                .id(section.getId())
                .upStation(section.getUpStation())
                .downStation(section.getDownStation())
                .distance(section.getDistance())
                .build();
    }

    private Section saveSection(Long id, SectionSaveRequest sectionSaveRequest) {
        return sectionRepository.save(
                Section.builder()
                        .upStation(findStation(sectionSaveRequest.getUpStationId()))
                        .downStation(findStation(sectionSaveRequest.getDownStationId()))
                        .distance(sectionSaveRequest.getDistance())
                        .line(findLine(id))
                        .build()
        );
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요."));
    }

    private Line findLine(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new IllegalArgumentException("검색된 노선이 없습니다. id를 바꿔주세요."));
    }
}
