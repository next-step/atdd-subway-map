package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.adapters.persistence.LineJpaAdapter;
import subway.line.dto.request.SaveLineRequestDto;
import subway.line.dto.request.UpdateLineRequestDto;
import subway.line.dto.response.LineResponseDto;
import subway.line.entity.Line;
import subway.station.adapters.persistence.StationJpaAdapter;
import subway.station.entity.Station;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final StationJpaAdapter stationJpaAdapter;

    private final LineJpaAdapter lineJpaAdapter;

    @Transactional
    public LineResponseDto saveLine(SaveLineRequestDto lineRequest) {
        Station upStation = stationJpaAdapter.findById(lineRequest.getUpStationId());
        Station downStation = stationJpaAdapter.findById(lineRequest.getDownStationId());

        Line line = lineJpaAdapter.save(lineRequest.toEntity(upStation, downStation));
        return LineResponseDto.of(line);
    }

    public List<LineResponseDto> findAllLines() {
        return lineJpaAdapter.findAll()
                .stream()
                .map(LineResponseDto::of)
                .collect(Collectors.toList());
    }

    public LineResponseDto findLineById(Long id) {
        return LineResponseDto.of(lineJpaAdapter.findById(id));
    }

    @Transactional
    public void updateLine(Long id, UpdateLineRequestDto lineRequest) {
        Line targetLine = lineJpaAdapter.findById(id);
        targetLine.updateLine(lineRequest.toEntity());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineJpaAdapter.deleteById(id);
    }
}
