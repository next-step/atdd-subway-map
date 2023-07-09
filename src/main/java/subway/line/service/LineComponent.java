package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.SubwayBadRequestException;
import subway.line.constant.LineMessage;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.SectionCreateRequest;
import subway.line.dto.SectionDeleteRequest;
import subway.line.model.Line;
import subway.line.model.Section;
import subway.station.model.Station;
import subway.station.service.StationService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LineComponent {

    private final LineService lineService;
    private final StationService stationService;
    private final SectionService sectionService;

    @Transactional
    public LineResponse createLine(LineCreateRequest lineRequest) {
        Station upStation = stationService.findStationById(lineRequest.getUpStationId());
        Station downStation = stationService.findStationById(lineRequest.getDownStationId());
        Line line = lineService.saveLine(lineRequest, upStation, downStation);
        Section section = Section.builder()
                .downStation(downStation)
                .upStation(upStation)
                .distance(lineRequest.getDistance())
                .build();
        line.addSection(section);
        lineService.saveLine(line);
        return LineResponse.from(line);
    }

    @Transactional
    public void appendSection(final Long lineId, SectionCreateRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Line line = lineService.findLineById(lineId);
        Section section = Section.builder()
                .downStation(downStation)
                .upStation(upStation)
                .distance(request.getDistance())
                .build();

        // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
        if (!line.getDownStation().equals(section.getUpStation())) {
            throw new SubwayBadRequestException(LineMessage.DOWN_STATION_NOT_MATCH_WITH_UP_STATION.getCode(),
                    LineMessage.DOWN_STATION_NOT_MATCH_WITH_UP_STATION.getMessage());
        }
        // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
        List<Station> stationsInLine = line.getStationsInSections();
        stationsInLine.stream()
                .filter(s -> s.equals(section.getDownStation()))
                .findAny()
                .ifPresent(e -> {
                    throw new SubwayBadRequestException(LineMessage.ADD_SECTION_STATION_DUPLICATION_VALID_MESSAGE.getCode(),
                            LineMessage.ADD_SECTION_STATION_DUPLICATION_VALID_MESSAGE.getMessage());
                });
        line.addSection(section);
        lineService.saveLine(line);
    }

    @Transactional
    public void deleteSectionByStationId(SectionDeleteRequest request) {
        Line line = lineService.findLineById(request.getLineId());
        Station station = stationService.findStationById(request.getStationId());
        Section section = null;
        section = line.deleteSectionByStation(station);
        lineService.saveLine(line);
        sectionService.delete(section);
    }
}
