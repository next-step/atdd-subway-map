package subway.line.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.CreateRequestNotValidException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineCreateRequestTest {

    @Test
    @DisplayName("생성된 lineCreateRequest 의 validate 로 필드들이 유효한지 검사 할 수 있다.")
    void lineCreateRequestCreateTest() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("name", "color", 1L, 2L, 3L);

        assertDoesNotThrow(lineCreateRequest::validate);
    }

    @Test
    @DisplayName("생성된 lineCreateRequest 의 name 필드가 empty 면 CreateRequestNotValidException 이 던져진다.")
    void lineCreateRequestNameIsEmptyTest() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("", "color", 1L, 2L, 3L);

        assertThatThrownBy(lineCreateRequest::validate).isInstanceOf(CreateRequestNotValidException.class);
    }

    @Test
    @DisplayName("생성된 lineCreateRequest 의 color 필드가 empty 면 CreateRequestNotValidException 이 던져진다.")
    void lineCreateRequestColorIsEmptyTest() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("name", "", 1L, 2L, 3L);

        assertThatThrownBy(lineCreateRequest::validate).isInstanceOf(CreateRequestNotValidException.class);
    }

    @Test
    @DisplayName("생성된 lineCreateRequest 의 upStation 필드가 null 이면 CreateRequestNotValidException 이 던져진다.")
    void lineCreateRequestUpStationIsNullTest() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("name", "color", null, 2L, 3L);

        assertThatThrownBy(lineCreateRequest::validate).isInstanceOf(CreateRequestNotValidException.class);
    }

    @Test
    @DisplayName("생성된 lineCreateRequest 의 downStation 필드가 null 이면 CreateRequestNotValidException 이 던져진다.")
    void lineCreateRequestDownStationIsNullTest() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("name", "color", 1L, null, 3L);

        assertThatThrownBy(lineCreateRequest::validate).isInstanceOf(CreateRequestNotValidException.class);
    }

    @Test
    @DisplayName("생성된 lineCreateRequest 의 distance 필드가 0보다 작거나 같으면 CreateRequestNotValidException 이 던져진다.")
    void lineCreateRequestDistanceIsLoeZeroTest() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("name", "color", 1L, 2L, 0L);

        assertThatThrownBy(lineCreateRequest::validate).isInstanceOf(CreateRequestNotValidException.class);
    }

    @Test
    @DisplayName("생성된 lineCreateRequest 의 upStation 필드와 downStation 필드가 같으면 CreateRequestNotValidException 이 던져진다.")
    void lineCreateRequestUpStationSameWithDownStationTest() {
        final LineCreateRequest lineCreateRequest = new LineCreateRequest("name", "color", 1L, 1L, 3L);

        assertThatThrownBy(lineCreateRequest::validate).isInstanceOf(CreateRequestNotValidException.class);
    }
}
