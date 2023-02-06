package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionRequest;
import subway.exception.SubwayRestApiException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import java.util.List;
import java.util.stream.Collectors;

import static subway.exception.ErrorResponseEnum.*;

@Service
@Transactional(readOnly = true)
public class LineService {
    LineRepository lineRepository;
    StationRepository stationRepository;


    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(lineRequest.getName(), lineRequest.getColor()));
        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line){
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getSections());
    }

    public LineResponse findLineById(Long id) {
        return lineRepository.findById(id).map(this::createLineResponse).get();
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();

        line.change(lineRequest.getName(), lineRequest.getColor());

        lineRepository.save(line);
    }

    @Transactional
    public LineResponse addSection(String id, SectionRequest sectionRequest) throws Exception {
        Line line = lineRepository.findById(Long.valueOf(id)).orElseThrow(() -> new SubwayRestApiException(ERROR_NO_FOUND_LINE));

        Station upStation = stationRepository.findById(sectionRequest.getUpStationId()).orElseThrow(() -> new SubwayRestApiException(ERROR_NO_FOUND_STATION));
        Station downStation = stationRepository.findById(sectionRequest.getDownStationId()).orElseThrow(() -> new SubwayRestApiException(ERROR_NO_FOUND_STATION));

        Section section = new Section(upStation, downStation, sectionRequest.getDistance());

        line.addSection(section);
        line = lineRepository.save(line);

        return createLineResponse(line);
    }

    @Transactional
    public LineResponse deleteSection(String id, String sectionId) throws Exception{
        Line line = lineRepository.findById(Long.valueOf(id)).orElseThrow(() -> new SubwayRestApiException(ERROR_NO_FOUND_LINE));

        line.deleteSection(Long.valueOf(sectionId));
        Line deleteLine = lineRepository.save(line);

        return createLineResponse(deleteLine);
    }
}
