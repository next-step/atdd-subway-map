package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.*;
import nextstep.subway.exception.LineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private static String DUPLICATE_NAME_ERROR_MASSAGE = "사용중인 노선 이름입니다.";

    private LineRepository lineRepository;
    private StationRepository stationRepository;
    private SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository,
                       StationRepository stationRepository,
                       SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new LineException(DUPLICATE_NAME_ERROR_MASSAGE);
        }

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));

        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        line.update(lineRequest.getName(), lineRequest.getColor());

        createLineResponse(lineRepository.save(line));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public void saveSection(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).get();
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).get();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).get();

        line.validationSectionStation(upStation, downStation);

        Section section = createSection(line, upStation, downStation, lineRequest.getDistance());

        sectionRepository.save(section);
    }

    private Section createSection(Line line, Station upStation, Station downStation, int distance) {
        return new Section(
                line,
                upStation,
                downStation,
                distance
        );
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations()
                        .stream()
                        .map(this::createStationResponse)
                        .collect(Collectors.toList()),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName(),
                station.getCreatedDate(),
                station.getModifiedDate()
        );
    }
}
