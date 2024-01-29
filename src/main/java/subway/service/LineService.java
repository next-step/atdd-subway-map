package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.controller.dto.StationResponse;
import subway.repository.LineRepository;
import subway.service.dto.LineDto;
import subway.service.dto.SaveLineDto;
import subway.service.dto.UpdateLineDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineDto saveLine(SaveLineDto saveLineDto) {
        Line line = lineRepository.save(Line.create(
                saveLineDto.getName(),
                saveLineDto.getColor(),
                saveLineDto.getUpStationId(),
                saveLineDto.getDownStationId(),
                saveLineDto.getDistance()
        ));
        return this.createLineDto(line);
    }

    public List<LineDto> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineDto).collect(Collectors.toList());
    }

    public LineDto getLineByIdOrFail(Long id) {
        Line line = this.findLineByIdOrFail(id);
        return this.createLineDto(line);
    }

    @Transactional
    public void updateLine(UpdateLineDto updateLineDto) {
        Line line = this.findLineByIdOrFail(updateLineDto.getTargetId());
        line.update(updateLineDto.getName(), updateLineDto.getColor());
        lineRepository.save(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findLineByIdOrFail(Long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * step2 에서는 노선과 지하철역 매핑까지 처리하지 않는다. 매핑 작업 전에는 단순 더미 처리로 구현.
     */
    private List<StationResponse> getStations(Long upStationId, Long downStationId) {
        return List.of(
                new StationResponse(upStationId, "dummy"),
                new StationResponse(downStationId, "dummy")
        );
    }

    private LineDto createLineDto(Line line) {
        return new LineDto(
                line.getId(),
                line.getName(),
                line.getColor(),
                this.getStations(line.getUpStationId(), line.getDownStationId())
        );
    }
}
