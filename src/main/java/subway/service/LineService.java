package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository,StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(lineRequest.toEntity());
        return LineResponse.fromEntity(line);
    }


    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long lineId) {
        return lineRepository.findById(lineId)
                .map(LineResponse::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("line not found"));
    }

    @Transactional
    public void updateLine(Long lineId, LineRequest lineRequest) {
        final Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("line not found"));

        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("line not found"));

        lineRepository.delete(line);
    }






}
