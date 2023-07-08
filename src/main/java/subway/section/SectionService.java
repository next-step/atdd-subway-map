package subway.section;

import org.springframework.stereotype.Service;
import subway.Station;
import subway.StationNotFoundException;
import subway.StationRepository;
import subway.line.Line;
import subway.line.LineNotFoundException;
import subway.line.LineRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class SectionService {
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    private static final long MINIMUM_SECTION_COUNT = 1L;

    public SectionService(final SectionRepository sectionRepository, StationRepository stationRepository, LineRepository lineRepository) {
      this.sectionRepository = sectionRepository;
      this.stationRepository = stationRepository;
      this.lineRepository = lineRepository;
    }

    public SectionResponse createSection(final long lineId, final SectionRequest request) {
      final var line = lineRepository.findById(lineId).orElseThrow(() -> new LineNotFoundException(lineId));

      final var upStation = getStationById(request.getUpStationId());
      final var downStation = getStationById(request.getDownStationId());

      if (!line.isLastStation(upStation)) {
        throw new StationDoesNotMatchException();
      }

      final Section savedSection = sectionRepository.save(new Section(line, upStation, downStation, request.getDistance()));

      return new SectionResponse(savedSection);
    }

    private Station getStationById(final Long stationId) {
      return stationRepository.findById(stationId)
              .orElseThrow(() -> new StationNotFoundException(stationId));
    }

    public void deleteSection(Long lineId, Long stationId) {
        final var line = lineRepository.findById(lineId).orElseThrow(() -> new LineNotFoundException(lineId));
        final var station = getStationById(stationId);

        if (!line.isLastStation(station)) {
          throw new StationDoesNotMatchException();
        }

        if (hasMinimumSection(line)) {
          throw new IllegalArgumentException("구간은 최소 하나입니다");
        }

        sectionRepository.delete(line.getLastSection());
    }

    private static boolean hasMinimumSection(Line line) {
        return line.getSections().size() == MINIMUM_SECTION_COUNT;
    }
}
