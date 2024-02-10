package subway.section.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.CheckDuplicateStationException;
import subway.exception.InvalidUpStationException;
import subway.exception.NotFoundException;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.section.entity.Section;
import subway.section.repository.SectionRepository;
import subway.section.request.SectionCreateRequest;
import subway.section.response.SectionResponse;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;
import subway.station.response.StationResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private SectionRepository sectionRepository;
    private StationRepository stationRepository;
    private LineRepository lineRepository;

    public SectionService(SectionRepository sectionRepository, StationRepository stationRepository, LineRepository lineRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public SectionResponse saveSection(Long lineId, SectionCreateRequest request) {
        Station upStation = getStationById(Long.valueOf(request.getUpStationId()));
        Station downStation = getStationById(Long.valueOf(request.getDownStationId()));

        Line line = getLineById(lineId);

        validDownStation(Long.valueOf(request.getDownStationId()), line);
        validUpStation(Long.valueOf(request.getUpStationId()), line);

        Section section = sectionRepository.save(Section.builder()
                .line(line)
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build());

        return SectionResponse.builder()
                .id(section.getId())
                .lineId(section.getLine().getId())
                .upStations(StationResponse.builder()
                        .id(section.getUpStation().getId())
                        .name(section.getUpStation().getName())
                        .build())
                .downStations(StationResponse.builder()
                        .id(section.getDownStation().getId())
                        .name(section.getDownStation().getName())
                        .build())
                .distance(section.getDistance())
                .build();
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLineById(lineId);
        line.deleteSection(stationId);
    }

    private static void validDownStation(Long downStationId, Line line) {
        List<Long> registeredStationIds = line.getStationIds();

        if (registeredStationIds.contains(downStationId)) {
            throw new CheckDuplicateStationException("이미 해당노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
        }
    }

    private static void validUpStation(Long upStationId, Line line) {
        Long downStationId = line.findDownStationId();

        if (downStationId != upStationId) {
            throw new InvalidUpStationException("새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 합니다.");
        }
    }

    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("지하철역이 존재하지 않습니다."));
    }

    private Line getLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("지하철 노선이 존재하지 않습니다."));
    }
}
