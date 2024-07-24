package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.LinesResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.entity.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;
import subway.section.entity.Section;
import subway.section.entity.Sections;
import subway.section.repository.SectionRepository;
import subway.station.dto.StationResponse;
import subway.station.entity.Station;
import subway.station.service.StationService;

import java.util.List;

import static subway.common.constant.ErrorCode.LINE_NOT_FOUND;
import static subway.converter.LineConverter.convertToLineResponseByLine;
import static subway.converter.LineConverter.convertToLineResponseByLineAndStations;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(final CreateLineRequest createLineRequest) {
        Station upStation = stationService.getStationByIdOrThrow(createLineRequest.getUpStationId());
        Station downStation = stationService.getStationByIdOrThrow(createLineRequest.getDownStationId());

        Section section = Section.of(upStation, downStation, createLineRequest.getDistance(), 0L);

        Sections sections = new Sections();
        sections.addSection(section);

        Line line = Line.of(createLineRequest.getName(), createLineRequest.getColor(), createLineRequest.getDistance(), sections);
        lineRepository.save(line);

        StationResponse upStationResponse = new StationResponse(upStation.getId(), upStation.getName());
        StationResponse downStationResponse = new StationResponse(downStation.getId(), downStation.getName());

        LineResponse lineResponse = convertToLineResponseByLineAndStations(line, List.of(upStationResponse, downStationResponse));

        return lineResponse;
    }

    @Transactional(readOnly = true)
    public LinesResponse findAllLines() {
        List<Line> lines = lineRepository.findAll();
        LinesResponse linesResponse = new LinesResponse();
        for (Line line : lines) {
            linesResponse.addLineResponse(convertToLineResponseByLine(line));
        }

        return linesResponse;
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long id) {
        Line line = getLineByIdOrThrow(id);

        return convertToLineResponseByLine(line);
    }

    @Transactional
    public void modifyLine(final Long id, final ModifyLineRequest modifyLineRequest) {
        Line line = getLineByIdOrThrow(id);
        line.changeName(modifyLineRequest.getName());
        line.changeColor(modifyLineRequest.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public void deleteLine(final Long id) {
        Line line = getLineByIdOrThrow(id);
        lineRepository.delete(line);
    }

    public Line getLineByIdOrThrow(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(String.valueOf(LINE_NOT_FOUND)));
    }

    public Line saveLine(Line line) {
        return lineRepository.save(line);
    }

}
