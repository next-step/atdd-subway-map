package subway.line;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.exception.CustomException;
import subway.exception.ErrorDto;
import subway.section.SectionRequest;
import subway.station.Station;
import subway.station.StationResponse;
import subway.station.StationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static subway.line.LineResponse.createLineResponse;

@Service
@Transactional(readOnly = true)
public class LineService {
    private final LineRepository lineRepository;
    private StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance()));
        line.addSection(stationService, lineRequest);

        return createLineResponse(line);
    }

    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::_createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse _createLineResponse(Line line) {
        return createLineResponse(line);
    }

    @Transactional
    public LineResponse updateLine(Long id, LineRequest lineRequest) throws CustomException {
        Line line = findLineById(id);
        lineRepository.save(line);
        return createLineResponse(line);
    }


    public Line findLineById(Long id) throws CustomException {
        return lineRepository.findById(id).orElseThrow(()->new CustomException(
                new ErrorDto(HttpStatus.NOT_FOUND, "지하철노선 Id("+ id +") 가 존재 하지 않습니다.")));
    }

    public LineResponse findLineResponseById(Long id) throws CustomException {
        createLineResponse(findLineById(id));
        return createLineResponse(findLineById(id));
    }


    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void deleteLines() {
        lineRepository.deleteAll();
    }

    @Transactional
    public void addSectionByLineId(Long lineId, SectionRequest sectionRequest) {
        Station upStation = stationService.findById(sectionRequest.getUpStationId());
        Station downStation = stationService.findById(sectionRequest.getDownStationId());
        Line line = findLineById(lineId);
        line.getSections().add(new Section(
                line
                , upStation
                , downStation
                , sectionRequest.getDistance()));
    }

    @Transactional
    public void deleteSection(Long lineId, Long stationId) {
        Line line = lineRepository.findById(lineId).orElseThrow(()->new CustomException(
                new ErrorDto(HttpStatus.NOT_FOUND, "지하철노선 Id("+ lineId +") 가 존재 하지 않습니다.")));
        Station station = stationService.findById(stationId);

        if (!line.getSections().get(line.getSections().size() - 1).getDownStation().equals(station)) {
            throw new CustomException(
                    new ErrorDto(HttpStatus.NOT_FOUND, "지하철노선 Id("+ lineId +") 가 존재 하지 않습니다.")
            );
        }

        line.getSections().remove(line.getSections().size() - 1);
    }
}
