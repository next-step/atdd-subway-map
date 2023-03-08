package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.mapper.LineMapper;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;

    private final StationRepository stationRepository;

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(EntityNotFoundException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(EntityNotFoundException::new);

        Line line = Line.builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .build();

        Line saveLine = lineRepository.save(line);
        return LineMapper.INSTANCE.toResponse(saveLine);
    }

    public List<LineResponse> findAllLines() {
        return LineMapper.INSTANCE.toResponseList(lineRepository.findAll());
    }

    public LineResponse findLineById(Long id) {
        Line line = getLine(id);
        return LineMapper.INSTANCE.toResponse(line);
    }

    @Transactional
    public void modifyLine(Long id, LineRequest lineRequest) {
        Line line = getLine(id);
        line.modify(lineRequest.getName(), lineRequest.getColor());
    }

    private Line getLine(final Long id){
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = getLine(id);
        lineRepository.delete(line);
    }
}