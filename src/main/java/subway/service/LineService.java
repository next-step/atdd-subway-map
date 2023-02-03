package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.line.*;
import subway.domain.Line;
import subway.repository.LineRepository;
import subway.domain.Station;
import subway.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineCreateResponse createStationLine(LineCreateRequest request) {
        Line line = lineRepository.save(request.convertToEntity());
        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow();
        return new LineCreateResponse(line, List.of(upStation, downStation));
    }

    public List<LineReadListResponse> readStationLineList() {
        List<Line> lines = lineRepository.findAll();
        List<LineReadListResponse> response = new ArrayList<>();
        for (Line line : lines) {
            Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow();
            Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow();
            response.add(new LineReadListResponse(line, List.of(upStation, downStation)));
        }
        return response;
    }

    public LineReadResponse readStationLine(Long stationLineId) {
        Line line = lineRepository.findById(stationLineId).orElseThrow();
        Station upStation = stationRepository.findById(line.getUpStationId()).orElseThrow();
        Station downStation = stationRepository.findById(line.getDownStationId()).orElseThrow();
        return new LineReadResponse(line, List.of(upStation, downStation));
    }

    @Transactional
    public void updateStationLine(Long stationLineId, LineUpdateRequest request) {
        Line line = lineRepository.findById(stationLineId).orElseThrow();
        line.updateNameAndColor(request.convertToEntity());
    }

    @Transactional
    public void deleteStationLine(Long stationLineId) {
        lineRepository.deleteById(stationLineId);
    }

    @Transactional
    public LineExtendResponse extendStationLine(LineExtendRequest request) {
        return null;
    }
}
