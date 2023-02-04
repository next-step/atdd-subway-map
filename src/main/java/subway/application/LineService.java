package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.*;
import subway.domain.*;
import subway.domain.exceptions.EntityNotFoundException;

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
    public LineDto saveLine(LineCreateDto lineCreateDto) {
        Station upStation = stationRepository.findById(lineCreateDto.getUpStationId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 역입니다."));

        Station downStation = stationRepository.findById(lineCreateDto.getDownStationId())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 역입니다."));

        int distance = lineCreateDto.getDistance();

        Line line = lineRepository.save(Line.create(
                lineCreateDto.getName(),
                lineCreateDto.getColor(),
                new Section(upStation, downStation, distance)
        ));

        return createLineResponse(line);
    }

    public List<LineDto> readLines() {
        return lineRepository
                .findAll()
                .stream()
                .map(line -> createLineResponse(line))
                .collect(Collectors.toList());
    }

    public LineDto readLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노선입니다."));

        return createLineResponse(line);
    }

    private LineDto createLineResponse(Line line) {
        return new LineDto(
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

    private StationDto createStationResponse(Station station) {
        return new StationDto(
                station.getId(),
                station.getName()
        );
    }

    @Transactional
    public void modifyLine(Long lineId, LineModifyDto lineModifyDto) {
        String newName = lineModifyDto.getName();
        String newColor = lineModifyDto.getColor();

        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노선입니다."));

        line.modify(newName, newColor);
    }

    @Transactional
    public void deleteLine(Long lineId) {
        lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노선입니다."));

        lineRepository.deleteById(lineId);
    }


    @Transactional
    public void addSection(SectionAddDto sectionAddDto) {
        Long lineId = sectionAddDto.getLineId();
        Long upStationId = sectionAddDto.getUpStationId();
        Long downStationId = sectionAddDto.getDownStationId();
        int distance = sectionAddDto.getDistance();

        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노선입니다."));

        Station upStation = stationRepository.findById(upStationId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 역입니다."));

        Station downStation = stationRepository.findById(downStationId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 역입니다."));

        Section newSection = new Section(upStation, downStation, distance);

        line.addSection(newSection);
    }

    @Transactional
    public void deleteSection(SectionDeleteDto sectionDeleteDto) {
        Long lineId = sectionDeleteDto.getLineId();
        Long stationId = sectionDeleteDto.getStationId();

        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 노선입니다."));

        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 역입니다."));

        line.deleteSection(station);
    }
}
