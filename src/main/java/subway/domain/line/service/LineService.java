package subway.domain.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.global.error.ErrorCode;
import subway.global.error.exception.NotFoundException;
import subway.infrastructure.line.LineRepository;
import subway.presentation.line.dto.request.LineRequest;
import subway.presentation.line.dto.request.LineUpdateRequest;
import subway.presentation.line.dto.response.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    @Transactional
    public LineResponse createLine(LineRequest request, Station upStation, Station downStation) {
        Section section = Section.of(upStation, downStation, request.getDistance());
        Sections sections = Sections.of(List.of(section));

        Line initLine = request.toEntity(sections);
        Line line = lineRepository.save(initLine);

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long lineId) {
        Line line = lineRepository.findByIdWithStations(lineId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_LINE));
        return LineResponse.of(line);
    }

    @Transactional
    public void updateLine(Long lineId, LineUpdateRequest request) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_LINE));
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(Long lineId) {
        Line line = lineRepository.findByIdWithStations(lineId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_LINE));
        lineRepository.delete(line);
    }

    @Transactional
    public LineResponse addSection(Long lineId, Station upStation, Station downStation, Integer distance) {
        Line line = lineRepository.findByIdWithStations(lineId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_LINE));
        int sectionDistance = line.getDistance() - distance;
        Section section = Section.of(upStation, downStation, sectionDistance);
        line.addSection(section);

        return LineResponse.of(line);
    }

    @Transactional
    public void deleteSection(Long lineId, Station station) {
        Line line = lineRepository.findByIdWithStations(lineId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_LINE));
        Section section = line.getSection(station);
        line.deleteSection(section);
    }
}