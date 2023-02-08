package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private long 강남역Id;
    private long 역삼역Id;
    private long 잠실역Id;
    private long 이호선Id;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        강남역Id = 지하철역을_생성한다("강남역").body().jsonPath().getLong("id");
        역삼역Id = 지하철역을_생성한다("역삼역").body().jsonPath().getLong("id");
        잠실역Id = 지하철역을_생성한다("잠실역").body().jsonPath().getLong("id");
        이호선Id = 노선을_생성한다("2호선", "bg-green-600", 강남역Id, 역삼역Id, 10)
            .body().jsonPath().getLong("id");
    }

    @Nested
    @DisplayName("지하철 구간 생성")
    class 지하철_구간_생성 {

        @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
        @Test
        void 새로운_구간의_하행역은_해당_노선에_등록되어있는_역일_수_없다() {
            final ExtractableResponse<Response> response = 구간을_추가_요청(이호선Id, 잠실역Id, 강남역Id, 5);

            assertThat(response.statusCode()).isNotEqualTo(200);
        }

        @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다")
        @Nested
        class 새로운_구간의_상행역은_해당_노선에_등록되어있는_하행_종점역이어야_한다 {

            @DisplayName("동일하지 않다면 등록 불가능하다")
            @Test
            void 동일하지_않다면_등록_불가능하다() {
                final ExtractableResponse<Response> response = 구간을_추가_요청(이호선Id, 강남역Id, 잠실역Id, 10);

                assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
            }

            @DisplayName("동일하다면 등록 가능하다")
            @Test
            void 동일하다면_등록_가능하다() {
                final ExtractableResponse<Response> response = 구간을_추가_요청(이호선Id, 역삼역Id, 잠실역Id, 10);

                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            }
        }
    }

    @DisplayName("지하철 노선 구간 제거")
    @Nested
    class 지하철_노선_구간_제거 {

        @DisplayName("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다.")
        @Nested
        class 지하철_노선에_등록된_역_하행_종점역_만_제거할_수_있다 {

            @BeforeEach
            void setUp() {
                구간을_추가_요청(이호선Id, 역삼역Id, 잠실역Id, 10);
            }

            @DisplayName("마지막 구간이 아니면 제거할 수 없다.")
            @Test
            void 마지막_구간이_아니면_제거할_수_없다() {
                final ExtractableResponse<Response> response = 구간_삭제_요청(이호선Id, 강남역Id);

                assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
            }

            @DisplayName("마지막 구간이면 제거할 수 있다")
            @Test
            void 마지막_구간이면_제거할_수_있다() {
                final ExtractableResponse<Response> response = 구간_삭제_요청(이호선Id, 잠실역Id);

                assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            }
        }

        @DisplayName("지하철 노선에 구간이")
        @Nested
        class 지하철_노선에_구간이 {

            @DisplayName("한개가 아닌 경우 삭제할 수 있다")
            @Test
            void 한개_가_아닌_경우_삭제할_수_있다() {
                구간을_추가_요청(이호선Id, 역삼역Id, 잠실역Id, 10);

                final ExtractableResponse<Response> response = 구간_삭제_요청(이호선Id, 잠실역Id);

                assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
            }

            @DisplayName("한개인 경우 삭제할 수 없다")
            @Test
            void 한개인_경우_삭제할_수_없다() {
                final ExtractableResponse<Response> response = 구간_삭제_요청(이호선Id, 잠실역Id);

                assertThat(response.statusCode()).isNotEqualTo(HttpStatus.NO_CONTENT.value());
            }
        }
    }
}
