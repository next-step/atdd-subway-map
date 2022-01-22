# Requirements
## [X] Feature: 지하철 노선 관리 기능

### [X] Scenario: 지하철 노선 생성
* When 지하철 노선 생성을 요청 하면
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
