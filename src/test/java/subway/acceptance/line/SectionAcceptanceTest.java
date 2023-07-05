package subway.acceptance.line;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import subway.acceptance.AcceptanceTest;
import subway.acceptance.helper.spec.Given;
import subway.acceptance.helper.spec.Then;
import subway.acceptance.helper.spec.When;
import subway.acceptance.station.StationFixture;
import subway.line.service.request.LineCreateRequest;
import subway.line.service.request.SectionRequest;

@DisplayName("지하철구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {
    private final StationFixture stationFixture = new StationFixture();
    private final LineFixture lineFixture = new LineFixture();
    private final SectionFixture sectionFixture = new SectionFixture();

    private Long 논현역, 신논현역, 강남역, 양재역;

    @BeforeEach
    public void setUp() {
        논현역 = stationFixture.지하철역을_생성한다("논현역").getId();
        신논현역 = stationFixture.지하철역을_생성한다("신논현역").getId();
        강남역 = stationFixture.지하철역을_생성한다("강남역").getId();
        양재역 = stationFixture.지하철역을_생성한다("양재역").getId();
    }

    @Given
    class 지하철_노선을_생성하고 {

        private Long 신분당선;

        @BeforeEach
        public void setUp() {
            final var 노선_논현역_신논현역 = new LineCreateRequest("신분당선", "bg-red-600", 논현역, 신논현역, 10L);
            신분당선 = lineFixture.지하철노선을_생성한다(노선_논현역_신논현역).getId();
        }

        @When
        class 지하철_구간을_등록하면 {

            @Then
            void 등록한_구간을_응답받을_수_있다() {
                // when
                final var 신논현역_강남역 = new SectionRequest(신논현역, 강남역, 10L);
                final var response = sectionFixture.지하철구간을_등록한다(신분당선, 신논현역_강남역);

                // then
                assertThat(response.getStations()).hasSize(3);
            }

            @When
            class 새로운_구간의_상행역이_노선의_하행_종점역이_아니라면 {

                @Then
                void 새로운_구간을_등록할_수_없다() {
                    // when
                    final var 구간_강남역_양재역 = new SectionRequest(강남역, 양재역, 10L);
                    sectionFixture.지하철구간_등록에_실패한다(신분당선, 구간_강남역_양재역);

                    // then
                    assertThat(lineFixture.지하철노선을_조회한다(신분당선).getStations()).hasSize(2);
                }
            }

            @When
            class 새로운_구간의_하행역이_노선에_등록되어_있는_역이라면 {

                @Then
                void 새로운_구간을_등록할_수_없다() {
                    // when
                    final var 구간_신논현역_논현역 = new SectionRequest(신논현역, 논현역, 10L);
                    sectionFixture.지하철구간_등록에_실패한다(신분당선, 구간_신논현역_논현역);

                    // then
                    assertThat(lineFixture.지하철노선을_조회한다(신분당선).getStations()).hasSize(2);
                }
            }
        }

        @Given
        class 지하철_구간을_등록하고 {

            @BeforeEach
            public void setUp() {
                final var 구간_신논현역_강남역 = new SectionRequest(신논현역, 강남역, 10L);
                sectionFixture.지하철구간을_등록한다(신분당선, 구간_신논현역_강남역);
            }

            @When
            class 등록한_지하철_구간을_삭제하면 {

                @Then
                void 해당_지하철_구간_정보는_삭제된다() {
                    // when
                    sectionFixture.지하철구간을_제거한다(신분당선, 강남역);

                    // then
                    assertThat(lineFixture.지하철노선을_조회한다(신분당선).getStations()).hasSize(2);
                }


                @When
                class 삭제하고자_하는_구간이_마지막_구간이_아니라면 {

                    @Then
                    void 해당_지하철_구간_정보는_삭제할_수_없다() {
                        // when
                        sectionFixture.지하철구간_제거에_실패한다(신분당선, 신논현역);

                        // then
                        assertThat(lineFixture.지하철노선을_조회한다(신분당선).getStations()).hasSize(3);
                    }
                }

                @When
                class 노선에_상행_종점역과_하행_종점역만_있다면 {

                    @Then
                    void 해당_지하철_구간_정보는_삭제할_수_없다() {
                        // when
                        sectionFixture.지하철구간을_제거한다(신분당선, 강남역);
                        sectionFixture.지하철구간_제거에_실패한다(신분당선, 신논현역);

                        // then
                        assertThat(lineFixture.지하철노선을_조회한다(신분당선).getStations()).hasSize(2);
                    }
                }
            }
        }
    }
}
