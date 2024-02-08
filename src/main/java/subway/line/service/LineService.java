package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.NotFoundException;
import subway.line.response.LineResponse;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.line.request.LineCreateRequest;
import subway.line.request.LineUpdateRequest;
import subway.section.entity.Section;
import subway.section.repository.SectionRepository;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;
import subway.station.response.StationResponse;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;

    private StationRepository stationRepository;

    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository, SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineCreateRequest lineCreateRequest) {
        Station upStation = getStationById(lineCreateRequest.getUpStationId());
        Station downStation = getStationById(lineCreateRequest.getDownStationId());

        Section section = Section.builder()
                .upStation(upStation)
                .downStation(downStation)
                .distance(lineCreateRequest.getDistance())
                .build();

        Line line = Line.builder()
                .name(lineCreateRequest.getName())
                .color(lineCreateRequest.getColor())
                .distance(lineCreateRequest.getDistance())
                .sections(Collections.singletonList(section))
                .build();

        line = lineRepository.save(line);

        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(convertStationsToStationResponses(line.getSections()))
                .build();
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest request) {
        Line findLine = getLineById(id);
        findLine.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        Line findLine = getLineById(id);
        lineRepository.deleteById(findLine.getId());
    }

    public List<LineResponse> findAllLine() {
        return lineRepository.findAll().stream()
                .map(line -> LineResponse.builder().id(line.getId())
                        .name(line.getName())
                        .color(line.getColor())
                        .stations(convertStationsToStationResponses(line.getSections()))
                        .build())
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long id) {
        Line findLine = getLineById(id);

        return LineResponse.builder()
                .id(findLine.getId())
                .color(findLine.getColor())
                .name(findLine.getName())
                .stations(convertStationsToStationResponses(findLine.getSections()))
                .build();
    }

    private Line getLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("지하철 노선이 존재하지 않습니다."));
    }

    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(() -> new NotFoundException("지하철역이 존재하지 않습니다."));
    }

    private List<StationResponse> convertStationsToStationResponses(List<Section> sections) {
        Set<StationResponse> stationResponses = new HashSet<>();
        for (Section section : sections) {
            StationResponse upStationResponse = StationResponse.builder()
                    .id(section.getUpStation().getId())
                    .name(section.getUpStation().getName())
                    .build();

            StationResponse downStationResponse = StationResponse.builder()
                    .id(section.getDownStation().getId())
                    .name(section.getDownStation().getName())
                    .build();

            stationResponses.add(upStationResponse);
            stationResponses.add(downStationResponse);
        }
        return new ArrayList<>(stationResponses);
    }

}
