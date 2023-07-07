package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import subway.line.dto.LineCreateRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.SectionCreateRequest;
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

    public LineResponse createLine(LineCreateRequest lineRequest) {
        Station upStation = stationService.findEntityById(lineRequest.getUpStationId());
        Station downStation = stationService.findEntityById(lineRequest.getDownStationId());
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

    public void appendSection(final Long lineId, SectionCreateRequest request) {
        Station upStation = stationService.findEntityById(request.getUpStationId());
        Station downStation = stationService.findEntityById(request.getDownStationId());
        Line line = lineService.findLineById(lineId);
        Section section = Section.builder()
                .downStation(downStation)
                .upStation(upStation)
                .distance(request.getDistance())
                .build();

        // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
        if (!line.getDownStation().equals(section.getUpStation())) {
            throw new IllegalArgumentException("기존 노선의 하행역과 추가 하고자 하는 상행역이 일치하지 않습니다.");
        }
        // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
        List<Station> stationsInLine = line.getStationsInSections();
        stationsInLine.stream()
                .filter(s -> s.equals(section.getDownStation()))
                .findAny()
                .ifPresent(e -> {throw new IllegalArgumentException("기존 노선에 등록된 역은 추가 하고자 하는 구간의 역이 될 수 없습니다.");});
        line.addSection(section);
        lineService.saveLine(line);
    }

}
