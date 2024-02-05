# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

--- 
## 1주차 미션
### Review 정리
  - 가변인자를 받는 `containsAnyOf` 메서드 활용
  - 매직넘버를 사용하는 것을 지양할 것
  - 메서드명에 `And`와 같이 2가지 이상의 일을 하고 있음을 표현하는 것을 지양할 것

--- 
## 2주차 미션
### 진행 순서
  1. 인수 테스트 작성
  2. 작성한 테스트가 `Pass` 되도록 기능 구현

### 인수 테스트 작성
  - [X] 지하철 노선 생성
  - [X] 지하철 노선 목록 조회
  - [X] 지하철 노선 조회
  - [X] 지하철 노선 수정
  - [X] 지하철 노선 삭제

### 인수 테스트 성공
  - [X] 지하철 노선 생성
  - [X] 지하철 노선 목록 조회
  - [X] 지하철 노선 조회
  - [X] 지하철 노선 수정
  - [X] 지하철 노선 삭제

### 고민(해결 완료)
  - 테스트 검증 범위
     - 일부, 중요하다고 생각하는 필드만? 
        - 예) 지하철 노선명 + 색상 정도?
     - 모든 필드?
       - JsonPath를 반환받아서 `get` 혹은 `getList` 메서드를 통해 검증?
  - 진행 순서
     1. 모든 인수 테스트 작성(리팩토링 X)
        - 요청: 로그를 통해 request method, URI, body 정도?
        - 응답: 로그를 통해 실패하는? 응답 상태코드 정도?
     2. 기능 구현
        - Repository -> Service -> Controller
     3. 기능 리팩토링
     4. 인수 테스트 리팩토링 
     5. 인수 테스트 최종 검토
  - throw Exception
    - id에 해당하는 지하철 노선이 없을 경우, 예외를 Throw하는 위치
      - 요청이 제대로 오는지 확인, 응답을 제대로 전달하는 것에만 집중하는 Controller에서 하는 것이 아닌 서비스에 역할일까?
  - `updateStationLine` 메서드는 @Transaction 어노테이션이 없다면, 조회 시 수정 전 데이터가 조회된다.(`deleteStationLine` 동일)
    - 테스트 메서드에서는 EntityManager가 하나만 사용되는 것인가?
      - 아님. entity.clear 혹은 entity.flush를 호출해도 실패
  - 테스트 케이스의 수행 결과를 판별하는 메서드도 리팩토링하는 것이 좋을까?
    - 어떻게 분리하는 것이 좋을지
      - 분리한다면, 오히려 가독성이 떨어지는 것이 아닌지?

### Review 정리  

1. 클래스 구현 순서를 지킬 것
    ```
    Class T {
        상수(static final) 또는 클래스 변수
        인스턴스 변수
        생성자
        팩토리 메서드
        메서드
        기본 메서드(equals, hashCode, toString)
    }
    ```

2. 미션 페이지에 기재된 조건(API 명세 등)을 재차 확인할 것
3. 테스트 코드에서 동등성을 검증할 땐, 모든 필드를 꺼내기 보단  `usingRecursiveComparison` 메서드 활용할 것
4. 인수 테스트를 격리하는 여러 방법을 시도해보고, 스스로 장/단점을 정리해볼 것
5. `Test fixture`는 의미있는 변수명에 객체로 할당해서 사용할 것
6. 유효성 검증을 추가하는 것을 고려해볼 것
7. jpa에 사용하기 위한 생성자의 접근제어자는 `public`이 아닌 `protected`로 변경할 것
8. 인수 테스트의 가독성을 위해 한글을 적극적으로 사용해볼 것

--- 
## 3주차 미션

### 인수 테스트 정리

#### 인수 테스트 케이스 분리

0. 공통
   - 유효성 검증
     - `downStationId`, `upStationId`, `distance`은 0보다 커야한다.

1. 구간 등록
   - `성공`
     - 요청한 상행역이 연결하고자 하는 구간의 하행 종점역으로 등록되어 있을 경우 `&&` 요청한 하행역이 구간으로 등록되지 않은 역일 경우
   - `실패`
     - 상행역
       - 요청한 상행역이 하행 종점역으로 등록되어 있지 않을 경우
       - 요청한 상행역이 역으로 등록되어 있지 않은 경우
       - 요청한 상행역이 하행 종점역이 아닌, 구간에 등록되어 있는 경우
     - 하행역
       - 요청한 하행역이 이미 구간으로 등록되어 있는 경우
       - 요청한 하행역이 역으로 등록되어 있지 않은 경우
       - 요청한 하행역이 구간에 등록되어 있는 경우

2. 구간 제거
    - `성공`
      - 기존 2개의 구간 존재(equal. 최소 3개역) `&&` 하행 종점역(마지막 역)이 포함된 구간을 삭제하는 경우
    - `실패`
      - 1개의 구간만 존재할 경우(역이 2개만 존재할 경우)
      - 하행 종점역을 제거하는 것이 아닌 경우
      - 해당 역이 존재하지 않을 경우

### Review 정리  

1. 생성 혹은 수정을 의도하는 메서드명의 `prefix`를 명확하게 작성할 것
   - 가령, 노선 수정과 메서드를 `saveStationLine`로 작명해 놓았다면, 내부 구현을 모르는 다른 개발자에게 혼란을 줄 수 있음.
       ```
       // 생성
       public StationLine createStationLine() {}
    
       // 수정
       public StationLine updateStationLine() {}
       ```

2. DTO(Data Transfer Object)에 비즈니스 로직을 두는 것은 지양할 것
   - 가령, 매개변수의 수를 줄이기 위한 의도일 경우 정적 팩토리 메서드 활용
     ```
     StationSectionRequest mergedRequest = StationSectionRequest.mergeForCreateLine(stationLineId, request);
     StationSectionResponse stationSectionResponse = stationLineService.createStationSection(mergedRequest);
     ```
3. Entity 객체에 `@JsonIgnore`를 사용하는 것은 지양하고, DTO에 활용하는 것을 지향할 것
4. javadoc을 사용하지 않고, 읽기 좋은 코드를 만들기 위해 노력해볼 것
5. 인수 테스트 주석을 작성할 때, 잘 읽힐 수 있는 단어를 최대한 사용해볼 것 

### 고민

1. 도메인 로직에 대한 테스트 코드의 주석은 어떻게 작성하는 것이 좋을까?