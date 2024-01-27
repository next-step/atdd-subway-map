package subway.line.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.exception.UpdateRequestNotValidException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineUpdateRequestTest {


    @Test
    @DisplayName("생성된 LineUpdateRequest 의 validate 로 필드들이 유효한지 검사 할 수 있다.")
    void lineUpdateRequestCreateTest() {
        final LineUpdateRequest LineUpdateRequest = new LineUpdateRequest("name", "color");

        assertDoesNotThrow(LineUpdateRequest::validate);
    }

    @Test
    @DisplayName("생성된 LineUpdateRequest 의 name 필드가 empty 면 UpdateRequestNotValidException 이 던져진다.")
    void lineUpdateRequestNameIsEmptyTest() {
        final LineUpdateRequest LineUpdateRequest = new LineUpdateRequest("", "color");

        assertThatThrownBy(LineUpdateRequest::validate).isInstanceOf(UpdateRequestNotValidException.class);
    }

    @Test
    @DisplayName("생성된 LineUpdateRequest 의 color 필드가 empty 면 UpdateRequestNotValidException 이 던져진다.")
    void lineUpdateRequestColorIsEmptyTest() {
        final LineUpdateRequest LineUpdateRequest = new LineUpdateRequest("name", "");

        assertThatThrownBy(LineUpdateRequest::validate).isInstanceOf(UpdateRequestNotValidException.class);
    }
    
}
