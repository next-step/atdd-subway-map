package nextstep.subway.line.application;

import nextstep.subway.line.NotFoundLineException;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class LineServiceTest {

    @Autowired
    private LineService lineService;


    @Test
    public void loadContext(){
        assertThat(lineService).isNotNull();
    }

    @ParameterizedTest
    @CsvSource(value = {"1호선:blue", "3호선:orange", "5호선:purple"}, delimiter = ':')
    public void findLineById(String stationName, String color) {
        //Given
        LineResponse createdResponse = lineService.saveLine(new LineRequest(stationName, color));

        //When
        LineResponse response = lineService.findLineById(createdResponse.getId());

        //Then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(createdResponse.getId()),
                () -> assertThat(response.getName()).isEqualTo(createdResponse.getName()),
                () -> assertThat(response.getCreatedDate()).isEqualTo(createdResponse.getCreatedDate()),
                () -> assertThat(response.getModifiedDate()).isEqualTo(createdResponse.getModifiedDate())
        );
    }

    @Test
    public void notFoundLineException() {
        assertThatThrownBy(() -> {
                 Long id = lineService.saveLine(new LineRequest("6호선", "brown")).getId();
                 lineService.findLineById(id + 1);
        }).isInstanceOf(NotFoundLineException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"1호선:blue", "3호선:orange", "5호선:purple"}, delimiter = ':')
    public void updateLine(String stationName, String color) {
        //Given
        String postFixColor= "-600";
        LineResponse createdResponse = lineService.saveLine(new LineRequest(stationName, color));
        LineRequest lineRequest = new LineRequest(stationName, color+postFixColor);

        //When
        lineService.updateLine(createdResponse.getId(), lineRequest);
        LineResponse updatedResponse = lineService.findLineById(createdResponse.getId());

        //Then
        assertThat(updatedResponse.getColor()).isEqualTo(color+postFixColor);
    }

    @ParameterizedTest
    @CsvSource(value = {"1호선:blue", "3호선:orange", "5호선:purple"}, delimiter = ':')
    public void deleteLine(String stationName, String color) {
        assertThatThrownBy(() -> {
            LineResponse createdResponse = lineService.saveLine(new LineRequest(stationName, color));

            lineService.deleteLineById(createdResponse.getId());
            lineService.findLineById(createdResponse.getId());
        }).isInstanceOf(NotFoundLineException.class);
    }

    @Test
    public void findAllLines() {
        //Given
        int size = 2;
        List<LineResponse> created = createLines(size);

        //When
        List<LineResponse> lines = lineService.findAllLines();

        //Then
        assertThat(lines.size()).isEqualTo(created.size());
    }

    private List<LineResponse> createLines(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> lineService.saveLine(new LineRequest(i+"호선", "blue")))
                .collect(Collectors.toList());
    }
}
