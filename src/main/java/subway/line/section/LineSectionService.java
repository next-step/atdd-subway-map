package subway.line.section;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LineSectionService {

  private final LineSectionRepository sectionRepository;

  @Transactional(readOnly = true)
  public List<LineSectionResponse> getAllSectionsOfLineAsResponse(Long lineId) {
    return sectionRepository.findByLineId(lineId).stream()
        .map(LineSectionResponse::fromEntity)
        .collect(Collectors.toUnmodifiableList());
  }
}
