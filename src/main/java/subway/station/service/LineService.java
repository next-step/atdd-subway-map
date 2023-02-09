package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.line.Line;
import subway.station.domain.line.LineRepository;
import subway.station.domain.section.Section;
import subway.station.domain.section.SectionRepository;
import subway.station.domain.station.Station;
import subway.station.domain.station.StationRepository;
import subway.station.service.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineSaveResponse save(LineSaveRequest lineSaveRequest) {
        Line line = saveLine(lineSaveRequest);
        Section section = saveSection(line.getId(), lineSaveRequest);

        line.addSection(section);

        return toDtoForSaveResponse(line);
    }

    public List<LineFindAllResponse> findAll() {
        List<Line> lines = lineRepository.findAll();

        return toDtoForFindAllResponse(lines);
    }

    public LineFindByLineResponse findById(Long id) {
        Line line = findLineById(id);

        return toDtoForFindByResponse(line);
    }

    @Transactional
    public LineUpdateResponse update(Long id, String name, String color) {
        Line line = findLineById(id);

        updateLine(line, name, color);

        return toDtoForUpdateResponse(line);
    }

    @Transactional
    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }

    private void updateLine(Line line, String name, String color) {
        if (!name.isEmpty()) {
            line.changeName(name);
        }
        if (!color.isEmpty()) {
            line.changeColor(color);
        }
    }

    private Section saveSection(Long id, LineSaveRequest lineSaveRequest) {

        return sectionRepository.save(
                Section.builder()
                        .upStation(findStation(lineSaveRequest.getUpStationId()))
                        .downStation(findStation(lineSaveRequest.getDownStationId()))
                        .distance(lineSaveRequest.getDistance())
                        .line(findLineById(id))
                        .build());
    }

    private Station findStation(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new IllegalArgumentException("검색된 지하철이 없습니다. id를 바꿔주세요."));
    }

    private Line saveLine(LineSaveRequest lineSaveRequest) {
        return lineRepository.save(
                Line.builder()
                        .name(lineSaveRequest.getName())
                        .color(lineSaveRequest.getColor())
                        .build());
    }

    private Line findLineById(Long lineId) {
        return lineRepository.findById(lineId).orElseThrow(() -> new IllegalArgumentException("검색된 노선이 없습니다. id를 바꿔주세요."));
    }

    private List<LineFindAllResponse> toDtoForFindAllResponse(List<Line> lines) {
        return lines.stream()
                .map(this::toDtoFoFindAllResponse)
                .collect(Collectors.toList());
    }

    private LineSaveResponse toDtoForSaveResponse(Line line) {
        return LineSaveResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line.getStations())
                .build();
    }

    private LineFindAllResponse toDtoFoFindAllResponse(Line line) {
        return LineFindAllResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line.getStations())
                .build();
    }

    private LineFindByLineResponse toDtoForFindByResponse(Line line) {
        return LineFindByLineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(line.getStations())
                .build();
    }

    private LineUpdateResponse toDtoForUpdateResponse(Line line) {
        return LineUpdateResponse.builder()
                .name(line.getName())
                .color(line.getColor())
                .build();
    }
}
