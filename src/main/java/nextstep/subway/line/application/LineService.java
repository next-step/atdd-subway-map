package nextstep.subway.line.application;

import nextstep.subway.common.exception.LineNotFoundException;
import nextstep.subway.common.exception.StationNotFoundException;
import nextstep.subway.line.application.dto.LineRequest;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.application.dto.SectionRequest;
import nextstep.subway.line.application.manager.LineStationManager;
import nextstep.subway.line.application.manager.StationData;
import nextstep.subway.station.application.manager.StationLineManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static nextstep.subway.common.ErrorMessages.DUPLICATE_LINE_NAME;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;
    private final LineStationManager lineStationManager;

    public LineService(LineRepository lineRepository, LineStationManager lineStationManager) {
        this.lineRepository = lineRepository;
        this.lineStationManager = lineStationManager;
    }

    public LineResponse saveLine(LineRequest request) {
        checkExistsLineName(request.getName());
        Line line = lineRepository.save(request.toLine());

        if (request.hasSectionAvailable()) {
            SectionRequest sectionRequest = new SectionRequest(request.getUpStationId(), request.getDownStationId(), request.getDistance());
            saveSection(line, sectionRequest);
        }

        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createStationData).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = getLine(id);
        return createStationData(line);
    }

    public void updateLine(Long id, LineRequest updateRequest) {
        Line line = getLine(id);
        if (!line.getName().equals(updateRequest.getName()))
            checkExistsLineName(updateRequest.getName());

        line.update(updateRequest);
    }

    public void deleteLineById(Long id) {
        Line line = getLine(id);
        lineRepository.delete(line);
    }

    public void addSection(Long lineId, SectionRequest request) {
        Assert.isTrue(!Objects.equals(request.getUpStationId(), request.getDownStationId()),
                "상행역과 하행역은 동일할 수 없습니다.");

        Line line = getLine(lineId);
        saveSection(line, request);
    }

    public void removeSectionById(Long lineId, Long downStationId) {
        Line line = getLine(lineId);
        Sections sections = line.getSections();
        sections.checkRemoveValidation(downStationId);
        sections.removeDownStation(downStationId);
    }

    private Line getLine(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(LineNotFoundException::new);
    }

    private void checkExistsLineName(String name) {
        Assert.isTrue(!lineRepository.existsByName(name), DUPLICATE_LINE_NAME.getMessage());
    }

    private LineResponse createStationData(Line line) {
        LineResponse lineResponse = LineResponse.of(line);
        Sections sections = line.getSections();
        if (!sections.isEmpty()) {
            List<StationData> stations = lineStationManager.getAllInStations(sections.getSectionIds());
            lineResponse.setStations(stations);
        }
        return lineResponse;
    }

    private void saveSection(Line line, SectionRequest request) {
        if (!lineStationManager.isExistInStations(request.getUpStationId(), request.getDownStationId()))
            throw new StationNotFoundException();

        Sections sections = line.getSections();
        sections.checkAddValidation(request.getUpStationId(), request.getDownStationId());
        sections.addSection(request.toSection(line));
        //sectionRepository.save(request.toSection(line));
    }
}
