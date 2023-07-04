package subway.line.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.service.request.LineCreateRequest;
import subway.line.service.request.LineUpdateRequest;
import subway.line.service.response.LineResponse;
import subway.station.domain.StationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(final LineCreateRequest request) {
        final var line = lineRepository.save(convertToLine(request));
        return LineResponse.toResponse(line);
    }

    private Line convertToLine(final LineCreateRequest request) {
        final var upStation = stationRepository.getById(request.getUpStationId());
        final var downStation = stationRepository.getById(request.getDownStationId());

        return new Line(
                request.getName(),
                request.getColor(),
                upStation,
                downStation,
                request.getDistance()
        );
    }

    @Transactional
    public void updateLine(final Long id, final LineUpdateRequest request) {
        final var line = lineRepository.getById(id);
        line.update(request.getName(), request.getColor());
    }

    @Transactional
    public void deleteLine(final Long id) {
        final var line = lineRepository.getById(id);
        lineRepository.delete(line);
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
}
