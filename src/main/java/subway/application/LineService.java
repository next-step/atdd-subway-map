package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.*;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.LineRepository;
import subway.domain.StationRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Station upStation = stationRepository.findById(lineRequest.getUpStationId()).get();
        Station downStation = stationRepository.findById(lineRequest.getDownStationId()).get();
        int distance = lineRequest.getDistance();


        Line line = lineRepository.save(Line.create(
                lineRequest.getName(),
                lineRequest.getColor(),
                new Section(upStation, downStation, distance)
        ));

        return createLineResponse(line);
    }

    public List<LineResponse> readLines() {
        return lineRepository
                .findAll()
                .stream()
                .map(line -> createLineResponse(line))
                .collect(Collectors.toList());
    }

    public LineResponse readLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 노선입니다."));

        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getSections().getSections().stream()
                        .flatMap(it -> Stream.of(it.getUpStation(), it.getDownStation()))
                        .distinct()
                        .map(this::createStationResponse)
                        .collect(Collectors.toList())
        );
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }

    @Transactional
    public void modifyLine(Long lineId, LineModifyRequest lineModifyRequest) {
        String newName = lineModifyRequest.getName();
        String newColor = lineModifyRequest.getColor();

        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 노선입니다."));

        line.modify(newName, newColor);
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 노선입니다."));

        lineRepository.deleteById(lineId);
    }


    @Transactional
    public void addSection(SectionAddDto sectionAddDto) {
        Long lineId = sectionAddDto.getLineId();
        Long upStationId = sectionAddDto.getUpStationId();
        Long downStationId = sectionAddDto.getDownStationId();
        int distance = sectionAddDto.getDistance();

        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 노선입니다."));

        Station upStation = stationRepository.findById(upStationId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 역입니다."));

        Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 역입니다."));

        Section newSection = new Section(upStation, downStation, distance);

        line.addSection(newSection);
    }
}
