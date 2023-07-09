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
//        lineService.saveLine(line);
        return LineResponse.from(line);
    }

    @Transactional
    public void appendSection(final Long lineId, SectionCreateRequest request) {
        Station upStation = stationService.findStationById(request.getUpStationId());
        Station downStation = stationService.findStationById(request.getDownStationId());
        Line foundLine = lineService.findLineById(lineId);
        Section section = Section.builder()
                .downStation(downStation)
                .upStation(upStation)
                .distance(request.getDistance())
                .build();
        foundLine.addSection(section);
        lineService.saveLine(foundLine);
    }

    @Transactional
    public void deleteSectionByStationId(SectionDeleteRequest request) {
        Line line = lineService.findLineById(request.getLineId());
        Station station = stationService.findStationById(request.getStationId());
        line.deleteSectionByStation(station);
        lineService.saveLine(line);
    }
}
