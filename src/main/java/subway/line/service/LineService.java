package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineModifyRequest;
import subway.line.dto.LineSaveRequest;
import subway.line.domain.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineCommandRepository;
import subway.line.repository.LineQueryRepository;
import subway.station.domain.Station;
import subway.station.service.StationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineCommandRepository lineCommand;
    private final LineQueryRepository lineQuery;

    private final StationService stationService;

    @Transactional
    public Line saveLine(LineSaveRequest saveRequest) {
        Line saveLine = buildSaveLine(saveRequest);
        Line line = lineCommand.save(saveLine);
        return line;
    }

    @Transactional
    public void modifyLine(Long id, LineModifyRequest modifyRequest) {
        Line line = lineQuery.findById(id).orElseThrow(LineNotFoundException::new);
        line.modify(modifyRequest);
    }

    public List<Line> findAllLines() {
        return lineQuery.findAll();
    }

    public Line findLineById(Long id) {
        return lineQuery.findById(id).orElseThrow(LineNotFoundException::new);
    }

    public void removeLineById(Long id) {
        lineCommand.deleteById(id);
    }

    private Line buildSaveLine(LineSaveRequest saveRequest) {
        Station upStation = stationService.findStationById(saveRequest.getUpStationId());
        Station downStation = stationService.findStationById(saveRequest.getDownStationId());
        Line line = Line.builder()
                .name(saveRequest.getName())
                .color(saveRequest.getColor())
                .build();
        line.addSection(upStation, downStation, saveRequest.getDistance());
        return line;
    }

}