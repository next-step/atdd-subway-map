package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.*;
import subway.exception.IllegalSectionException;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SectionService {

}
