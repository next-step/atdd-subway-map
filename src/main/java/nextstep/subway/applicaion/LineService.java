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
    private SectionRepository sectionRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }


    public LineResponse saveLine(LineRequest request) {
        final String name = request.getName();

        if (lineRepository.existsByName(name)) {
            throw new IllegalArgumentException(String.format("이미 존재하는 노선입니다. %s", request));
        }

        Line line = lineRepository.save(this.fromLine(request));

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
                .orElseThrow(() -> new NotFoundException(id));
        return createLineResponse(foundLine);
    }

    public LineResponse editLineById(long id, @RequestBody LineRequest lineRequest) {
        Line foundLine = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
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
            final Section section = sections.get(0);

            final Station upStation = section.getUpStation();
            final StationResponse upStationResponse = new StationResponse(upStation.getId(),
                    upStation.getName(),
                    upStation.getCreatedDate(),
                    upStation.getModifiedDate());

            final Station downStation = section.getDownStation();

            final StationResponse downStationResponse = new StationResponse(downStation.getId(),
                    downStation.getName(),
                    downStation.getCreatedDate(),
                    downStation.getModifiedDate());
            responseStation.addAll(Arrays.asList(upStationResponse, downStationResponse));
        }

        return new LineResponse(line.getId(),
                line.getName(),
                line.getColor(),
                responseStation,
                line.getCreatedDate(),
                line.getModifiedDate());
    }

    private Line fromLine(LineRequest lineRequest) {
        final Long downStationId = lineRequest.getDownStationId();
        final Station downStation = stationRepository.getById(downStationId);
        final Long upStationId = lineRequest.getUpStationId();
        final Station upStation = stationRepository.getById(upStationId);
        final Integer distance = lineRequest.getDistance();

        final Section section = new Section(upStation, downStation, distance);

        List<Section> sections = new ArrayList<>();
        sections.add(section);


        return new Line.Builder()
                .name(lineRequest.getName())
                .color(lineRequest.getColor())
                .sections(Arrays.asList(section))
                .build();
    }
}
