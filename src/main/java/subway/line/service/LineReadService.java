package subway.line.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.line.domain.Line;
import subway.line.exception.LineNotFoundException;
import subway.line.repository.LineRepository;
import subway.line.view.LineResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineReadService {
    private final LineRepository lineRepository;

    public Line getLine(Long id) {
        return lineRepository.findById(id)
                             .orElseThrow(LineNotFoundException::new);
    }

    public List<LineResponse> getList() {
        return lineRepository.findAll()
                             .stream()
                             .map(LineResponse::from)
                             .collect(Collectors.toUnmodifiableList());
    }
}
