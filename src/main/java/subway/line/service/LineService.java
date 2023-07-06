package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.global.error.code.ErrorCode;
import subway.global.error.exception.NotEntityFoundException;
import subway.line.dto.request.SaveLineRequestDto;
import subway.line.dto.response.LineResponseDto;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;
import subway.station.entity.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final StationRepository stationRepository;

    private final LineRepository lineRepository;

    @Transactional
    public LineResponseDto saveLine(SaveLineRequestDto lineRequest) {
        Station upStation = findStationInLineByStationId(lineRequest.getUpStationId());
        Station downStation = findStationInLineByStationId(lineRequest.getDownStationId());

        Line line = lineRepository.save(lineRequest.toEntity(upStation, downStation));
        return LineResponseDto.of(line);
    }

    public List<LineResponseDto> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponseDto::of)
                .collect(Collectors.toList());
    }

    private Station findStationInLineByStationId(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() ->
                        new NotEntityFoundException(ErrorCode.NOT_EXIST_STATION));
    }

}
