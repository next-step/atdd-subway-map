package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.common.error.NotFoundException;
import subway.line.domain.Line;
import subway.line.dto.AddLineRequest;
import subway.line.dto.LineResponse;
import subway.line.dto.ModifyLineRequest;
import subway.line.dto.ModifyLineResponse;
import subway.line.repository.LineRepository;
import subway.station.domain.Station;
import subway.station.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static subway.line.mapper.LineMapper.LINE_MAPPER;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineResponse createLine(AddLineRequest addLineRequest) {
        Line line = LINE_MAPPER.mapToLine(addLineRequest);
        Station upStation = findStationById(addLineRequest.getUpStationId());
        Station downStation = findStationById(addLineRequest.getDownStationId());
        line.addStation(upStation);
        line.addStation(downStation);
        Line savedLine = lineRepository.save(line);
        return LINE_MAPPER.mapToLineResponse(savedLine);
    }

    public List<LineResponse> getLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LINE_MAPPER::mapToLineResponse)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = findLineById(id);
        return LINE_MAPPER.mapToLineResponse(line);
    }

    @Transactional
    public ModifyLineResponse modifyLine(Long id, ModifyLineRequest modifyLineRequest) {
        Line line = findLineById(id);
        line.setName(modifyLineRequest.getName());
        Line modifiedLine = lineRepository.save(line);
        return LINE_MAPPER.mapToModifyLineResponse(modifiedLine);
    }

    @Transactional
    public void deleteLine(Long id) {
        if (!lineRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        lineRepository.deleteById(id);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    private Station findStationById(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
