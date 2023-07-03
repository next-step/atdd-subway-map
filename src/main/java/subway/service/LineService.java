package subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.StationRepository;
import subway.service.dto.request.LineCreateRequest;
import subway.service.dto.request.LineUpdateRequest;
import subway.service.dto.response.LineResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public LineResponse saveLine(final LineCreateRequest request) {
        final var line = lineRepository.save(convertToLine(request));
        return LineResponse.toResponse(line);
    }

    private Line convertToLine(final LineCreateRequest request) {
        final var upStation = stationRepository.getById(request.getUpStationId());
        final var downStation = stationRepository.getById(request.getDownStationId());

        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }

    public LineResponse findLine(final Long id) {
        final var line = lineRepository.getById(id);
        return LineResponse.toResponse(line);
    }

    @Transactional
    public void updateLineById(final Long id, final LineUpdateRequest request) {
        final var line = lineRepository.getById(id);
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }
}
