package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.applicaion.exception.NotFoundException;
import nextstep.subway.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }


    public LineResponse saveLine(LineRequest request) {
        final String name = request.getName();

        if (lineRepository.existsByName(name)) {
            throw new IllegalArgumentException(String.format("이미 존재하는 노선입니다. %s", request));
        }

        Line line = lineRepository.save(this.toLine(request));

        return this.createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(long id) {
        final Line foundLine = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 대상을 찾을 수 없습니다. id : %s", id)));
        return createLineResponse(foundLine);
    }

    public LineResponse editLineById(long id, @RequestBody LineRequest lineRequest) {
        Line foundLine = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("해당하는 대상을 찾을 수 없습니다. id : %s", id)));
        foundLine.updateLine(lineRequest.getName(), lineRequest.getColor());
        final Line savedLine = lineRepository.save(foundLine);
        return createLineResponse(savedLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(Line line) {
        final List<StationResponse> responseStation = new ArrayList<>();
        final List<Section> sections = line.getSections();
        if (!sections.isEmpty()) {
            responseStation.addAll(createStationResponses(sections));
        }

        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                responseStation,
                line.getCreatedDate(),
                line.getModifiedDate());
    }

    private List<StationResponse> createStationResponses(List<Section> sections) {
        List<StationResponse> responseList = new ArrayList<>();
        final StationResponse firstStation = createStationResponse(sections.get(0).getUpStation());
        responseList.add(firstStation);

        for (Section section : sections) {
            final Station downStation = section.getDownStation();
            final StationResponse downStationResponse = createStationResponse(downStation);
            responseList.add(downStationResponse);
        }

        return responseList;
    }

    private StationResponse createStationResponse(Station upStation) {
        return new StationResponse(upStation.getId(),
                upStation.getName(),
                upStation.getCreatedDate(),
                upStation.getModifiedDate());
    }

    private Line toLine(LineRequest lineRequest) {
        final Long downStationId = lineRequest.getDownStationId();
        final Station downStation = stationRepository.getById(downStationId);

        final Long upStationId = lineRequest.getUpStationId();
        final Station upStation = stationRepository.getById(upStationId);

        final Integer distance = lineRequest.getDistance();
        final Section section = new Section(upStation, downStation, distance);

        return new Line.Builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .sections(Arrays.asList(section))
                .build();
    }
}
