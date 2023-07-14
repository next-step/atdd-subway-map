package subway.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.dto.LineDto;
import subway.line.entity.Line;
import subway.line.entity.LineRepository;
import subway.line.dto.SectionDto;
import subway.line.entity.Section;
import subway.station.dto.StationDto;
import subway.station.entity.Station;
import subway.station.entity.StationRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    @Transactional
    public LineDto saveLine(LineDto lineDto) {
        Station upStation = getStation(lineDto.getUpStationId());
        Station downStation = getStation(lineDto.getDownStationId());

        Line newLine = lineDto.toEntity(upStation, downStation);
        Line savedLine = lineRepository.save(newLine);

        List<Station> stations = savedLine.getStations();
        return LineDto.from(savedLine, stations.stream()
                                                .map(StationDto::from)
                                                .collect(Collectors.toList()));
    }

    public List<LineDto> getLines() {
        return lineRepository.findAll().stream()
                .map(line -> LineDto.from(line, line.getStations().stream()
                                                  .map(StationDto::from)
                                                  .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public LineDto getLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("노선이 존재하지 않습니다. id:%s", id)));

        return LineDto.from(line, line.getStations().stream()
                                    .map(StationDto::from)
                                    .collect(Collectors.toList()));
    }

    @Transactional
    public void updateLine(Long id, LineDto dto) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("노선이 존재하지 않습니다. id:%s", id)));

        line.update(dto.getName(), dto.getColor());
    }

    @Transactional
    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Station getStation(Long lineDto) {
        Station upStation = stationRepository.findById(lineDto)
                .orElseThrow(() -> new IllegalArgumentException(String.format("역이 존재하지 않습니다. id:%s", lineDto)));
        return upStation;
    }

    @Transactional
    public void enroll(long lineId, SectionDto sectionDto) {
        Line line = getLine(lineId);

        Station newSectionUpStation = getStation(sectionDto.getUpStationId());
        Station newSectionDownStation = getStation(sectionDto.getDownStationId());
        Section newSection = sectionDto.toEntity(line, newSectionUpStation, newSectionDownStation);

        line.addSection(newSection);
    }

    @Transactional
    public void deleteSection(long lineId, long stationId) {
        Station deleteReqStation = getStation(stationId);

        Line line = getLine(lineId);

        line.removeSection(deleteReqStation);
    }

    private Line getLine(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 호선입니다. 호선id:%s", lineId)));
    }
}
