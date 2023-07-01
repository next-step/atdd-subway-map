package subway.line;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LineService {


    @Transactional
    public void createStation(LineCreateRequest request) {

    }
}
