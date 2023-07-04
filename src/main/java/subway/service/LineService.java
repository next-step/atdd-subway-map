package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineUpdateRequest;
import subway.dto.SectionRequest;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineResponse findLineById(Long id){
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return createLineResponse(line);
    }
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }
    public LineService(LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).orElseThrow(NoSuchElementException::new);
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).orElseThrow(NoSuchElementException::new);
        Line line = lineRequest.toEntity(upStation, downStation);
        lineRepository.save(line);
        return createLineResponse(line);
    }

    @Transactional
    public void deleteLineById(Long id){
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        return LineResponse.from(line);
    }
    @Transactional
    public void updateStationById(Long id, LineUpdateRequest lineUpdateRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NoSuchElementException::new);
        line.updateNameAndColor(lineUpdateRequest.getName(), lineUpdateRequest.getColor());
    }
    @Transactional
    public LineResponse saveSection(Long lineId, SectionRequest sectionRequest){
        Line line = lineRepository.findById(lineId).orElseThrow(NoSuchElementException::new);
        line.addSection(sectionRequest.getUpStationId(), sectionRequest.getDownStationId());
        return createLineResponse(line);
    }
    @Transactional
    public void deleteSection(Long lineId, Long stationId){
        Line line = lineRepository.findById(lineId).orElseThrow(NoSuchElementException::new);
        line.deleteSection(stationId);
    }
}
