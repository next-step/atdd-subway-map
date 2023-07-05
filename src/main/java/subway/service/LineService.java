package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.*;
import subway.jpa.Line;
import subway.jpa.LineRepository;
import subway.jpa.Station;
import subway.jpa.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public LineDto saveLine(LineDto lineDto) {
        Station upStation = stationRepository.findById(lineDto.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("상행역이 존재하지 않습니다. id:{}", lineDto.getUpStationId())));
        Station downStation = stationRepository.findById(lineDto.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException(String.format("하행역 존재하지 않습니다. id:{}", lineDto.getDownStationId())));
        Line lineEntity = lineDto.toEntity(upStation, downStation);

        Line savedLineEntity = lineRepository.save(lineEntity);

        return LineDto.from(savedLineEntity);
    }

    public List<LineDto> getLines() {
        return lineRepository.findAll().stream()
                .map(LineDto::from)
                .collect(Collectors.toList());
    }

    public LineDto getLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("노선이 존재하지 않습니다. id:{}", id)));

        return LineDto.from(line);
    }

    @Transactional
    public void updateLine(Long id, LineDto dto) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("노선이 존재하지 않습니다. id:{}", id)));

        line.update(dto.getName(), dto.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
