package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.Line;
import subway.station.domain.line.LineRepository;
import subway.station.domain.station.Station;
import subway.station.domain.station.StationRepository;
import subway.station.web.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineSaveResponse save(LineSaveRequest lineSaveRequest) {
        Line line = saveLine(lineSaveRequest);

        return toDtoForSaveResponse(line);
    }

    public List<LineFindAllResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return toDtoForFindAllResponse(lines);
    }

    @Transactional
    public LineUpdateResponse update(Long id, String name, String color) {
        Line line = findLineById(id);
        update(line, name, color);
        return toDtoForUpdateResponse(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    private List<LineFindAllResponse> toDtoForFindAllResponse(List<Line> lines) {
        return lines.stream()
                .map(this::toDtoFoFindAllResponse)
                .collect(Collectors.toList());
    }

    public LineFindByLineResponse findById(Long id) {
        Line line = findLineById(id);
        return toDtoForFindByResponse(line);
    }

    private void update(Line line, String name, String color) {
        line.changeName(name);
        line.changeColor(color);
    }

    private Line saveLine(LineSaveRequest lineSaveRequest) {
        return lineRepository.save(Line.builder()
                .name(lineSaveRequest.getName())
                .color(lineSaveRequest.getColor())
                .upStation(findStation(lineSaveRequest.getUpStationId()))
                .downStation(findStation(lineSaveRequest.getDownStationId()))
                .distance(lineSaveRequest.getDistance())
                .build());
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new IllegalArgumentException("검색된 노선이 없습니다. id를 바꿔주세요."));
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요."));
    }

    private LineSaveResponse toDtoForSaveResponse(Line line) {
        return LineSaveResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .upStation(line.getUpStation())
                .downStation(line.getDownStation())
                .build();
    }

    private LineFindAllResponse toDtoFoFindAllResponse(Line line) {
        return LineFindAllResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .upStation(line.getUpStation())
                .downStation(line.getDownStation())
                .build();
    }

    private LineFindByLineResponse toDtoForFindByResponse(Line line) {
        return LineFindByLineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .upStation(line.getUpStation())
                .downStation(line.getDownStation())
                .build();
    }

    private LineUpdateResponse toDtoForUpdateResponse(Line line) {
        return LineUpdateResponse.builder()
                .name(line.getName())
                .color(line.getColor())
                .build();
    }
}
