package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class AcceptanceTestThen {
    private static final String LOCATION_KEY_IN_HEADER = "Location";

    private final ExtractableResponse<Response> response;

    private AcceptanceTestThen(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public static AcceptanceTestThen fromWhen(ExtractableResponse<Response> response) {
        return new AcceptanceTestThen(response);
    }

    public AcceptanceTestThen equalsHttpStatus(HttpStatus status) {
        assertThat(response.statusCode())
            .withFailMessage(FailMessage.NOT_MATCH_HTTP_STATUS.message)
            .isEqualTo(status.value());
        return this;
    }

    public AcceptanceTestThen hasNotLocation() {
        assertThat(response.header(LOCATION_KEY_IN_HEADER))
            .withFailMessage(FailMessage.NOT_BLANK_LOCATION.message)
            .isBlank();
        return this;
    }

    public AcceptanceTestThen hasLocation() {
        assertThat(response.header(LOCATION_KEY_IN_HEADER))
            .withFailMessage(FailMessage.BLANK_LOCATION.message)
            .isNotBlank();
        return this;
    }

    public <E> AcceptanceTestThen containsAll(String jsonPath, Collection<E> expert) {
        List<E> responseData = response.jsonPath().getList(jsonPath);
        assertThat(responseData)
            .withFailMessage(FailMessage.BLANK_LOCATION.message)
            .containsAll(expert);
        return this;
    }

    private enum FailMessage {
        NOT_MATCH_HTTP_STATUS("Http Status가 예상한 값과 다릅니다."),
        NOT_BLANK_LOCATION("Location Header가 존재합니다."),
        BLANK_LOCATION("Location Header가 존재하지 않습니다."),
        NOT_MATCH_EXPERT("예상한 값이 다릅니다.");

        FailMessage(String message) {
            this.message = message;
        }

        private final String message;

    }
}
