package subway.section.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.service.LineService;
import subway.section.dto.SectionDto;
import subway.line.jpa.Line;
import subway.line.jpa.LineRepository;
import subway.section.jpa.Section;
import subway.section.jpa.SectionRepository;
import subway.station.jpa.Station;
import subway.station.jpa.StationRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SectionService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final LineService lineService;

    @Transactional
    public SectionDto enroll(long lineId, SectionDto sectionDto) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("노선 아이디가 존재하지 않습니다. id:%s", lineId)));

        Station newSectionUpStation = getStation(sectionDto.getUpStationId());
        Station newSectionDownStation = getStation(sectionDto.getDownStationId());
        Section newSection = sectionDto.toEntity(line, newSectionUpStation, newSectionDownStation);

        validate(line, newSection);

        Section savedSection = sectionRepository.save(newSection);
        return SectionDto.from(savedSection);
    }

    private void validate(Line line, Section newSection) {
        // 에러1: 새로운 구간의 상행 역이 해당 노선에 등록되어있는 하행 종적역이 아님.
        if (!newSection.getUpStation().equals(lineService.getDownStation(line))) {
            throw new IllegalArgumentException("새로운 구간의 상행 역이 해당 노선에 등록되어있는 하행 종적역이 아님.");
        }

        // 에러2: 새로운 구간의 하행역이 해당 노선에 등록되어있는 역임.
        if (lineService.getRelatedStations(line).contains(newSection.getDownStation())) {
            throw new IllegalArgumentException("새로운 구간의 하행역이 해당 노선에 등록되어있는 역임.");
        }
    }

    private Station getStation(Long stationId) {
        Station newSectionUpStation = stationRepository.findById(stationId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("역이 존재하지 않습니다. 역id:%s", stationId)));
        return newSectionUpStation;
    }

    @Transactional
    public void deleteSection(long lineId, long stationId) {
        // 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
        // 역을 조회한다
        Station deleteReqStation = getStation(stationId);

        // 노선의 마지막 역인지 확인한다
        Line line = getLine(lineId);
        validate(stationId, deleteReqStation, line);

        // 구간을 제거한다.
        sectionRepository.deleteByDownStation(deleteReqStation);
        stationRepository.delete(deleteReqStation);
    }

    private void validate(long stationId, Station deleteReqStation, Line line) {
        // 마지막 구간 아닌 구간 제거한 경우
        if (!lineService.getDownStation(line).equals(deleteReqStation)) {
            throw new IllegalArgumentException(String.format("노선의 마지막 역이 아닙니다. 역id:%s", stationId));
        }
        
        // 상행 종점역과 하행 종점역만 있는 경우
        if (sectionRepository.findAllByLine(line).size() == 1) {
            throw new IllegalArgumentException("상행 종점역과 하행 종점역만 존재합니다.");
        }
    }

    private Line getLine(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 호선입니다. 호선id:%s", lineId)));
    }
}
