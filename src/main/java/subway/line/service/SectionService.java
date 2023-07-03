package subway.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.line.domain.Section;
import subway.line.service.request.SectionRequest;
import subway.line.service.response.LineResponse;
import subway.station.domain.StationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SectionService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    @Transactional
    public LineResponse appendSection(final Long lineId, final SectionRequest request) {
        final var line = lineRepository.getById(lineId);
        line.appendSection(convertToSection(line, request));

        return LineResponse.toResponse(line);
    }

    private Section convertToSection(final Line line, final SectionRequest request) {
        final var upStation = stationRepository.getById(request.getUpStationId());
        final var downStation = stationRepository.getById(request.getDownStationId());

        return new Section(line, upStation, downStation, request.getDistance());
    }

    @Transactional
    public void removeSection(final Long lineId, final Long stationId) {
        final var line = lineRepository.getById(lineId);
        final var station = stationRepository.getById(stationId);
        line.removeSection(station);
    }
}
