package subway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.domain.color.Color;
import subway.dto.color.CreateColorRequest;
import subway.dto.color.CreateColorResponse;
import subway.dto.color.ReadColorResponse;
import subway.repository.ColorRepository;

@Service
@RequiredArgsConstructor
public class ColorService {
    private final ColorRepository colorRepository;

    public CreateColorResponse createColor(CreateColorRequest request) {
        Color color = colorRepository.save(new Color(request.getName()));
        return new CreateColorResponse(color.getId(), color.getName());
    }

    public ReadColorResponse readColor(Long id) {
        Color color = colorRepository.findById(id).orElseThrow();
        return new ReadColorResponse(color.getId(), color.getName());
    }

}
