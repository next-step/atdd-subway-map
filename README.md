# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

- [1단계](#1단계---지하철역-인수-테스트-작성)
- [2단계](#2단계---지하철-노선-관리)
- [3단계](#3단계---지하철-구간-관리)
- [RestAssured 공유사항](#restassured-guide)

# 1단계 - 지하철역 인수 테스트 작성
- [x] 지하철역 목록 조회 인수 테스트 작성하기
- [x] 지하철역 삭제 인수 테스트 작성하기

# 2단계 - 지하철 노선 관리
## 기능 요구사항
- 요구사항 설명에서 제공되는 인수 조건을 기반으로 지하철 노선 관리 기능을 구현하세요.
- 인수 조건을 검증하는 인수 테스트를 작성하세요.

## 기능 목록
- [x] 지하철 노선 생성
- [x] 지하철 노선 목록 조회
- [x] 지하철 노선 조회
- [x] 지하철 노선 수정
- [x] 지하철 노선 삭제

## 프로그래밍 요구사항
- 아래의 순서로 기능을 구현하세요.
  - 인수 조건을 검증하는 인수 테스트 작성
  - 인수 테스트를 충족하는 기능 구현
- 인수 테스트의 결과가 다른 인수 테스트에 영향을 끼치지 않도록 인수 테스트를 서로 격리 시키세요.
- 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링 하세요.

## 요구사항 설명
### 인수 조건
#### 1. 지하철노선 생성
- When 지하철 노선을 생성하면
- Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
- 
#### 2. 지하철노선 목록 조회
- Given 2개의 지하철 노선을 생성하고
- When 지하철 노선 목록을 조회하면
- Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.

#### 3. 지하철노선 조회
- Given 지하철 노선을 생성하고
- When 생성한 지하철 노선을 조회하면
- Then 생성한 지하철 노선의 정보를 응답받을 수 있다.

#### 4. 지하철노선 수정
- Given 지하철 노선을 생성하고
- When 생성한 지하철 노선을 수정하면
- Then 해당 지하철 노선 정보는 수정된다

### 5. 지하철노선 삭제
- Given 지하철 노선을 생성하고
- When 생성한 지하철 노선을 삭제하면
- Then 해당 지하철 노선 정보는 삭제된다

# 3단계 - 지하철 구간 관리
## 기능 요구사항
- [x] 요구사항 설명에서 제공되는 요구사항을 기반으로 지하철 구간 관리 기능을 구현하세요.
- [x] 요구사항을 정의한 인수 조건을 도출하세요.
- [x] 인수 조건을 검증하는 인수 테스트를 작성하세요.
- [ ] 예외 케이스에 대한 검증도 포함하세요.

## 기능 목록
- [x] 자하철 구간 등록
- [x] 지하철 구간 삭제

## 프로그래밍 요구사항
- [ ] 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현하세요.
  - [x] 요구사항 설명을 참고하여 인수 조건을 정의
  - [x] 인수 조건을 검증하는 인수 테스트 작성
  - [ ] 인수 테스트를 충족하는 기능 구현
- [x] 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
  - [x] 뼈대 코드의 인수 테스트를 참고
- [x] 인수 테스트의 결과가 다른 인수 테스트에 영향을 끼치지 않도록 인수 테스트를 서로 격리 시키세요.
- [ ] 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링 하세요.

## 요구사항 설명
### 인수 조건
#### 1. 지하철 구간 등록
- When 지하철 구간을 등록하면
- Then 지하철 구간 목록 조회 시 등록한 구간을 찾을 수 있다

### 2. 지하철 구간 삭제
- Given 지하철 구간을 등록하고
- When 등록한 지하철 구간을 삭제하면
- Then 해당 지하철 구간 정보는 삭제된다

---
# RestAssured Guide
```java
@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StationAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }
    
    // ...
    
    private RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();
    }
}
```

# AcceptanceTest
```java
@Service
@Profile(value = "acceptanceTest")
public class DatabaseCleanup implements InitializingBean {

    @PersistenceContext
    private EntityManager em;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        em.unwrap(Session.class)
            .doWork(this::extractTableNames);
    }

    @Transactional
    public void execute() {
        em.unwrap(Session.class)
            .doWork(this::truncate);
    }

    private void truncate(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : tableNames) {
            statement.executeUpdate("TRUNCATE TABLE " + tableName);
            statement.executeUpdate("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1");
        }
    }

    private void extractTableNames(Connection conn) {
        tableNames = em.getMetamodel()
            .getEntities().stream()
            .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
            .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
            .collect(Collectors.toList());
        
//        List<String> tableNames = new ArrayList<>();
//
//        ResultSet resultSet = conn.getMetaData()
//            .getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});
//
//        while (resultSet.next()) {
//            tableNames.add(resultSet.getString("table_name"));
//        }
//        this.tableNames = tableNames;
    }
}
```

# TRUNCATE
> 아까 TRUNCATE 관련 이슈 전 처음에 브라운 강사님 코드대로 TRUNCATE 했다가 ID 컬럼명이 달라서 오류가 나서 아래같이 해결했어요!

1. ALTER TABLE ... ID RESTART WITH 1 문 제거

2. dialect를 H2 database 사용 시 pk 초기화를 하려면 TRUNCATE 명령문에 RESTART IDENTITY를 추가하는 방법]
entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " RESTART IDENTITY").executeUpdate();

3. dialect를 MySQL 사용 시 TRUNCATE 진행 시 별다른 명령문이 없어도 AUTO_INCREMENT가 재설정 됨(5.7, 8.0 동일)
entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();


- H2 reference
  - https://www.h2database.com/html/commands.html?highlight=truncate&search=truncate#truncate_table
- mySQL reference
  - https://dev.mysql.com/doc/refman/5.7/en/truncate-table.html, https://dev.mysql.com/doc/refman/8.0/en/truncate-table.html

# Stub, Fake, Mock
- Mocking은 협력객체의 어떠한 행위를 했냐 안했냐를 판단할 떄 사용하고
- Stubbing은 협력객체의 값이 필요할 때 사용하는 건가요?
