package subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.request.LineRequest;
import subway.dto.request.SectionRequest;
import subway.dto.response.LineResponse;
import subway.dto.response.SectionResponse;
import subway.exception.NoLineException;
import subway.exception.NoStationException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public LineResponse createLine(LineRequest request) {

        Line line = lineRepository.save(lineBuilder(request));
        Section section = sectionRepository.save(sectionBuilder(line.getId(), SectionRequest.of(request)));
        line.addSection(section);

        return LineResponse.of(line);
    }

    @Transactional
    public SectionResponse createSection(Long lineId, SectionRequest request) {

        Line line = getLineById(lineId);
        Section section = sectionBuilder(line.getId(), request);
        line.addSection(section);

        return new SectionResponse(line, section);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
            .stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(NoLineException::new);

        return LineResponse.of(line);
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = getLineById(lineId);
        Station station = getStationById(stationId);
        line.deleteSection(station);
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id)
            .orElseThrow(NoLineException::new);

        line.modify(request.getName(), request.getColor());
        return LineResponse.of(line);
    }

    private Line lineBuilder(LineRequest request) {
        return Line.GenerateLine()
            .name(request.getName())
            .color(request.getColor())
            .distance(request.getDistance())
            .build();
    }

    private Line getLineById(Long stationId) {
        return lineRepository.findById(stationId).orElseThrow(NoLineException::new);
    }

    private Station getStationById(long id) {
        return stationRepository.findById(id)
            .orElseThrow(NoStationException::new);
    }

    private Section sectionBuilder(long lineId, SectionRequest request) {

        Station upStation = getStationById(request.getUpStationId());
        Station downStation = getStationById(request.getDownStationId());

        return Section.GenerateSection()
            .lineId(lineId)
            .distance(request.getDistance())
            .upStation(upStation)
            .downStation(downStation)
            .build();
    }
}
