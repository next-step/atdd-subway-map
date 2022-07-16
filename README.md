# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

## 1주차

### 요구사항
- 지하철역 인수 테스트 완성해보기
  - [x] 지하철역 목록 조회 인수 테스트 작성하기
  - [x] 지하철역 삭제 인수 테스트 작성하기
  - [x] 메소드 분리 하기

### 문제점

- 제거, 조회, 생성 따로 테스트를 돌리면 정상적으로 작동한다.
- test Class 전체를 돌리게 되면, 에러가 발생한다.
- 원인: 다른 테스트케이스가 다른곳에 영향을 줘서 발생

![problem](./docs/step1/problem.png)

#### 해결하는 과정

1. 첫번쨰

```@DirtiesContext 사용```

![DirtiesContext](./docs/step1/DirtiesContext.png)

결론부터 말하자면, 성공하였다.  
하지만, 테스트 마다 context를 초기화하여 SpringBoot가 다시 시직되는 불 필요함이 있다.

2. 두번쨰

```@BeforeEach 나 @AfterEach 에서 초기화```

![deleteAll](./docs/step1/deleteAll.png)

이것도 또한 방법중 하나가 될것이다.  
@DirtiesContext보다는 비용이 떨어지겠지만 데이터가 많아지면 테스트 성능이 떨어질 것이다.

3. 세번쨰

```@sql 사용```

![truncate](./docs/step1/truncate.png)

@SQL을 이용하여 truncate sql을 사용하여 해결할 수 있다.
delete 보다 truncate 가 트랜잭션 로그 공간도 차지 하지 않고, 락도 걸리지 않아 이중에서 제일 좋은 방법이라고 생각하였습니다.


## 2주차

- 지하철 노선 기능 구현하기
  - [x] 지하철 노선 생성하기
  - [x] 지하철 노선 목록 조회
  - [x] 지하철 노선 조회
  - [x] 지하철 노선 수정
  - [x] 지하철 노선 삭제

- 테스트 코드 작성하기
  - [x] 지하철 노선 생성하기 테스트 코드 작성
  - [x] 지하철 노선 목록 조회 테스트 코드 작성
  - [x] 지하철 노선 조회 테스트 코드 작성
  - [x] 지하철 노선 수정 테스트 코드 작성
  - [x] 지하철 노선 삭제 테스트 코드 작성

### 생각해보기

1주차때 테스트 코드에 서로 영향이 없게 수정을 하였는데, @SQL에도 큰 문제점이 있다.  
table이 늘어 날때 마다 sql을 작성해 줘야 한다.

![datacleanup](./docs/step2/datacleanup.png)

그래서 Entity를 모두 조회 후, truncate를 하는 방식으로 수정 하였다.


## step3 

## 코멘트 생각하기

- [x] 테스트 설정 관련 리팩토링 하기

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptionceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

}
```

만들어 사용.

- [x] 인수 테스트할때는 깔끔하게 작성하기 위해 필요 값은 위쪽에 만들어두기

```java
@DisplayName("구간 등록 기능 테스트")
@Test
void createSectionTest() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L , 6);

        ExtractableResponse<Response> 지하철구간_생성 = 지하철_구간_생성(1L, 2L, 3L, 4);
        assertThat(지하철구간_생성.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        ExtractableResponse<Response> response = 지하철_노선_조회(1L);
        하행선값_검증(response, "선릉역");
}
```

- [x] AccessLevel 설정하여 만들기

```java
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Section {
    
}
```

- [x] 도메인쪽에 Applcaiton Layer 참조하지 않고 사용하기

dto 에서 만들어 사용함.
```java
public StationLine toEntity() {
        return StationLine.builder()
            .name(name)
            .color(color)
            .upStationId(upStationId)
            .downStationId(downStationId)
            .distance(distance)
            .build();
    }
```

### 기능 정리

- [x] 구간 등록 기능추가
  - [x] (예외) 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
  - [x] (예외) 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.

- [x] 구간 삭제 기능 추가
  - [x] (예외) 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.
  - [x] (예외) 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.

### 테스트 케이스 추가

- [x] 구간 등록 테스트 작성
- [x] 구간 하행 종점역만 등록 가능하도록 예외 테스트 작성
- [x] 중복된 역은 추가 될수 없다.


- [x] 구간 삭제
- [x] 마지막 구간만 제거 되도록 테스트 코드 작성
- [x] (구간이 1개인 경우) 역을 삭제할 수 없다 테스트 코드 작성