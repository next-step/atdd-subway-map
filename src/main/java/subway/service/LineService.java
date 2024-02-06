package subway.service;

import static subway.dto.LineResponse.createLineResponse;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import subway.domain.Section;
import subway.dto.SectionRequest;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.domain.Station;
import subway.exception.NoLineException;
import subway.exception.NoStationException;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = getStation(lineRequest.getUpStationId());
        Station downStation = getStation(lineRequest.getDownStationId());
        Line line = Line.of(lineRequest, upStation, downStation);
        lineRepository.save(line);
        return createLineResponse(line);
    }

    private Station getStation(Long stationId) {
        return stationRepository.findById(stationId)
                                .orElseThrow(() -> new NoStationException(stationId + "에 해당하는 지하철 역이 존재하지 않습니다."));
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::createLineResponse)
                             .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new NoLineException(id + "에 해당하는 지하철 노선이 존재하지 않습니다."));
        return LineResponse.createLineResponse(line);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new NoLineException(id + "에 해당하는 지하철 노선이 존재하지 않습니다."));
        line.updateLine(lineRequest.getName(), lineRequest.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new NoLineException(id + "에 해당하는 지하철 노선이 존재하지 않습니다."));
        lineRepository.delete(line);
    }

    public void addSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new NoLineException(lineId + "에 해당하는 지하철 노선이 존재하지 않습니다."));
        Station upStation = stationRepository.findById(sectionRequest.getUpStationId())
                                             .orElseThrow(() -> new NoStationException(sectionRequest.getUpStationId() + "에 해당하는 지하철 역이 존재하지 않습니다."));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId())
                                               .orElseThrow(() -> new NoStationException(sectionRequest.getDownStationId() + "에 해당하는 지하철 역이 존재하지 않습니다."));
        Section section = Section.from(upStation, downStation, sectionRequest.getDistance());
        line.addSection(section);
    }
}
