package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.CreateLineRequest;
import subway.line.dto.UpdateLineRequest;
import subway.line.repository.Line;
import subway.line.repository.LineRepository;
import subway.station.repository.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public
class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public Line saveLine(CreateLineRequest request) {
        return lineRepository.save(
                Line.builder()
                        .name(request.getName())
                        .color(request.getColor())
                        .upStation(stationRepository.getReferenceById(request.getUpStationId()))
                        .downStation(stationRepository.getReferenceById(request.getDownStationId()))
                        .build()
        );
    }

    public List<Line> findAllLines() {
        return lineRepository.findAll();
    }

    public Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
    }

    @Transactional
    public void updateLineById(Long id, UpdateLineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Exist Line"));
        line.changeName(request.getName());
        line.changeColor(request.getColor());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
