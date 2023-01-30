package subway.line;

import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import java.util.*;

@Service
@Transactional
public class LineFindEntityService {

    private final LineRepository lineRepository;

    public LineFindEntityService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line getById(final Long id) {

        return lineRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
