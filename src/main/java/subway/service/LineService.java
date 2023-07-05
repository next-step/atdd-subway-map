package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.*;
import subway.jpa.Line;
import subway.jpa.LineRepository;
import subway.jpa.StationRepository;

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

    public LineDto saveLine(LineDto lineDto) {
        Line lineEntity = lineDto.toEntity();

        Line savedLineEntity = lineRepository.save(lineEntity);

        List<StationDto> stationDtos = List.of();

        LineDto responseDto = LineDto.from(savedLineEntity, stationDtos);
        return responseDto;
    }
}
