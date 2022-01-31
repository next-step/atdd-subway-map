# Requirements

## [X] Feature: 구간 조회 기능

### [X] Scenario: 구간 조회 시 역 목록 조회 성공
* Given 노선을 생성하고 해당 노선에 구간을 추가한 후
* When 해당 구간을 삭제하면
* Then 구간이 삭제된다.

## [X] Feature: 지하철역 관리 기능

### [X] Scenario: 중복이름으로 지하철역 생성
* Given 지하철역 생성을 요청 하고
* When 같은 이름으로 지하철역 생성을 요청 하면
* Then 지하철역 생성이 실패한다.
```
POST /stations HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8
content-length: 20
host: localhost:13189
connection: Keep-Alive
user-agent: Apache-HttpClient/4.5.13 (Java/11.0.12)
accept-encoding: gzip,deflate
```
```
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 22 Jan 2022 16:01:47 GMT
Connection: close

{
    "timestamp": "2022-01-22T16:01:47.283+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/stations"
}
```

## [X] Feature: 지하철 노선 관리 기능

### [X] Scenario: 노선에 하행 종점 구간 삭제
* Given 노선을 생성하고 해당 노선에 구간을 추가한 후
* When 구간 삭제를 요청하면
* Then 구간이 삭제된다.

### [X] Scenario: 노선에 구간 추가
* Given 노선을 생성하고 해당 노선에 종점역을 추가한 후
* When 해당 노선에 상, 하선의 구간 추가를 요청하면
* Then 구간 추가가 성공한다.

### [X] Scenario: 중복이름으로 지하철 노선 생성
* Given 지하철 노선 생성을 요청 하고
* When 같은 이름으로 지하철 노선 생성을 요청 하면
* Then 지하철 노선 생성이 실패한다.
```
Request method:	POST
Request URI:	http://localhost:3615/lines
Headers:		Accept=*/*
				Content-Type=application/json; charset=UTF-8
Body:
{
    "color": "bg-red-600",
    "name": "신분당선"
}
```
```
HTTP/1.1 400 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 22 Jan 2022 16:54:15 GMT
Connection: close

{
    "timestamp": "2022-01-22T16:54:15.618+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/lines"
}
```

### [X] Scenario: 지하철 노선 생성
* 역 2개를 생성하고
* When 지하철 노선(상행 종점, 하행 종점 포함) 생성을 요청 하면
* Then 지하철 노선 생성이 성공한다.    
```
POST /lines HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8

{
    "color": "bg-red-600",
    "name": "신분당선"
}
```
```
HTTP/1.1 201 
Location: /lines/1
Content-Type: application/json
Date: Fri, 13 Nov 2020 00:11:51 GMT

{
    "id": 1,
    "name": "신분당선",
    "color": "bg-red-600",
    "createdDate": "2020-11-13T09:11:51.997",
    "modifiedDate": "2020-11-13T09:11:51.997"
}
```

### [X] Scenario: 지하철 노선 목록 조회
* Given 지하철 노선 생성을 요청 하고
* Given 새로운 지하철 노선 생성을 요청 하고
* When 지하철 노선 목록 조회를 요청 하면
* Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
```
GET /lines HTTP/1.1
accept: application/json
host: localhost:49468
```
```
HTTP/1.1 200 
Content-Type: application/json
Date: Fri, 13 Nov 2020 00:11:51 GMT

[
    {
        "id": 1,
        "name": "신분당선",
        "color": "bg-red-600",
        "stations": [
            
        ],
        "createdDate": "2020-11-13T09:11:52.084",
        "modifiedDate": "2020-11-13T09:11:52.084"
    },
    {
        "id": 2,
        "name": "2호선",
        "color": "bg-green-600",
        "stations": [
            
        ],
        "createdDate": "2020-11-13T09:11:52.098",
        "modifiedDate": "2020-11-13T09:11:52.098"
    }
]
```
### [X] Scenario: 지하철 노선 조회
* Given 지하철 노선 생성을 요청 하고
* When 생성한 지하철 노선 조회를 요청 하면
* Then 생성한 지하철 노선을 응답받는다
```
GET /lines/1 HTTP/1.1
accept: application/json
host: localhost:49468
```
```
HTTP/1.1 200 
Content-Type: application/json
Date: Fri, 13 Nov 2020 00:11:51 GMT

{
    "id": 1,
    "name": "신분당선",
    "color": "bg-red-600",
    "stations": [
        
    ],
    "createdDate": "2020-11-13T09:11:51.866",
    "modifiedDate": "2020-11-13T09:11:51.866"
}
```

### [X] Scenario: 지하철 노선 수정
* Given 지하철 노선 생성을 요청 하고
* When 지하철 노선의 정보 수정을 요청 하면
* Then 지하철 노선의 정보 수정은 성공한다.
```
PUT /lines/1 HTTP/1.1
accept: */*
content-type: application/json; charset=UTF-8
content-length: 45
host: localhost:49468

{
    "color": "bg-blue-600",
    "name": "구분당선"
}
```
```
HTTP/1.1 200 
Date: Fri, 13 Nov 2020 00:11:51 GMT
```

### [X] Scenario: 지하철 노선 삭제
* Given 지하철 노선 생성을 요청 하고
* When 생성한 지하철 노선 삭제를 요청 하면
* Then 생성한 지하철 노선 삭제가 성공한다.
```
DELETE /lines/1 HTTP/1.1
accept: */*
host: localhost:49468
```
```
HTTP/1.1 204 
Date: Fri, 13 Nov 2020 00:11:51 GMT
```
