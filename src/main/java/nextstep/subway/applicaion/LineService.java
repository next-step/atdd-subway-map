package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import nextstep.subway.domain.line.Section;
import nextstep.subway.domain.station.StationRepository;
import nextstep.subway.exception.DataNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        validateStation(lineRequest.getUpStationId());
        validateStation(lineRequest.getDownStationId());

        Section section = new Section(null, lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance());

        Line createLine = new Line(
            null, lineRequest.getName(), lineRequest.getColor(), section
        );

        Line createdLine = lineRepository.save(createLine);
        return getLineResponse(createdLine);
    }

    @Transactional(readOnly = true)
    public LineResponse findOneLine(Long id) {
        Line findLine = getLine(id);
        return getLineResponse(findLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream().map(this::getLineResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public LineResponse editLine(Long id, LineRequest lineRequest) {
        Line findLine = getLine(id);
        findLine.edit(lineRequest);
        return getLineResponse(findLine);
    }

    private LineResponse getLineResponse(Line createdLine) {
        List<SectionResponse> sectionResponses = createdLine.getSections()
            .getSections().stream()
            .map(
                section -> new SectionResponse(section.getId(), section.getUpStationId(),
                    section.getDownStationId(), section.getDistance())).collect(Collectors.toList()
            );

        return new LineResponse(
            createdLine.getId(), createdLine.getName(), createdLine.getColor(),
            sectionResponses);
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id).orElseThrow(
            () -> new DataNotFoundException("Line 데이터가 없습니다.")
        );
    }

    private void validateStation(Long stationId) {
        if (!stationRepository.findById(stationId).isPresent()) {
            throw new DataNotFoundException("Station 데이터가 없습니다.");
        }
    }
}
