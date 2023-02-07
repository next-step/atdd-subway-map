package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.StationResponse;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static subway.service.StationService.createStationResponse;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(StationRepository stationRepository, LineRepository lineRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(StationNotFoundException::new);
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(StationNotFoundException::new);

        Line line = lineRepository.save(lineRequest.toEntity(downStation, upStation));
        sectionRepository.save(line.getSectionByStations(downStation, upStation));

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(LineNotFoundException::new);
        return createLineResponse(line);
    }

    @Transactional
    public void updateLine(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.update(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {

        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow(StationNotFoundException::new);
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow(StationNotFoundException::new);

        StationResponse upStationResponse = createStationResponse(upStation);
        StationResponse downStationResponse = createStationResponse(downStation);

        return new LineResponse(line, List.of(upStationResponse, downStationResponse));
    }
}
