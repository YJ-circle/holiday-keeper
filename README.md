# Holiday Keeper

Nager.Date의 외부 API를 활용하여 전 세계 공휴일 데이터를 저장하고 관리하는 미니 서비스입니다.

## 기술 스택

- **언어**: Java 21
- **프레임워크**: Spring Boot 3.4.7
- **데이터베이스**: H2
- **ORM**: JPA (Hibernate)
- **빌드 도구**: Gradle
- **API 문서화**: OpenAPI 3 (Swagger UI)

## 실행 방법
### 로컬 환경에서 직접 실행 (인메모리 DB)
프로젝트를 클론 받은 후, 루트 디렉토리에서 아래 명령어를 실행하세요.

```bash
# Windows
./gradlew.bat bootRun

# macOS/Linux
./gradlew bootRun
```

애플리케이션이 실행되면 `http://localhost:8080` 에서 접속할 수 있습니다.

### Docker를 사용하여 실행
이 프로젝트를 실행하려면 **Docker**가 설치되어 있어야 합니다.  
원하는 폴더에서 터미널을 실행하고, 아래 명령어를 차례로 실행하세요.
```
git clone https://github.com/YJ-circle/holiday-keeper
cd holiday-keeper/infra
docker-compose -p "holiday-keeper" up -d
```

실행 후 로그를 확인하고자 하시면 아래 명령을 실행하세요.
```
docker-compose -p "holiday-keeper" logs -f
```

종료를 원하는 경우 아래 명령을 차례로 실행하세요.
```
docker-compose -p "holiday-keeper" down --rmi all --volumes --remove-orphans
```

## API 명세 요약

### 1. 데이터 적재
#### 1) 국가 정보
- **Endpoint**: `POST /api/scraper/countries`
- **Description**: 외부 API로부터 지원하는 모든 국가 정보를 가져와 데이터베이스에 저장합니다.
- **Request Body**: 없음
- **Response**: 성공 메시지("국가 수집 요청이 정상적으로 전달되었습니다.")

#### 2) 공휴일 정보
** 최초로 수집 시작 시에는 5개년 (요구 사항 예시에서는 6개년) 모든 국가의 공휴일을 수집합니다.
- **Endpoint**: `POST /api/scraper/holidays`
- **Description**: 특정 연도와 국가 코드에 해당하는 공휴일 정보를 외부 API로부터 가져와 데이터베이스에 저장합니다.
- **Request Body**:
  ```json
  {
    "year": 2025,
    "countryCodes": ["KR", "US"]
  }
  ```
- **Response**: 성공 메시지("수집 요청이 정상적으로 전달되었습니다.")
### 2. 공휴일 조회

- **Endpoint**: `GET /country/{country}/year/{year}`
  - `country` (String, required): 조회할 국가 코드 (e.g., "KR")
  - `year` (int, required): 조회할 연도
- **Description**: 연도, 국가 코드를 기준으로 필터링하여 공휴일 목록을 페이징 처리하여 조회합니다.
- **Query Parameters**:
  - `page` (int, optional, default=0): 페이지 번호
  - `size` (int, optional, default=10): 페이지 당 아이템 수
  - `sort` (String[], optional, default=date): 정렬 기준
  
- **Response Example**:
  ``` json
  {
    "message": "공휴일 조회 요청 성공",
    "data": {
      "content": [
        {
          "date": "2025-01-01",
          "localName": "새해",
          "name": "New Year's Day",
          "countryCode": "KR",
          "global": true,
          "counties": [],
          "types": [
            "Public"
          ]
        }
      ],
      "page": 0,
      "size": 1,
      "totalElements": 15,
      "totalPages": 15
    }
  }
  ```

### 3. 데이터 재동기화 (Upsert)
1. 데이터 적재 - 2) 공휴일 정보와 동일하게 실행하면 Upsert로 동작합니다.
- **Endpoint**: `POST /api/scraper/holidays`
- **Description**: 특정 연도와 국가 코드에 해당하는 공휴일 정보를 외부 API로부터 가져와 데이터베이스에 저장합니다.
- **Request Body**:
  ```json
  {
    "year": 2025,
    "countryCodes": ["KR", "US"]
  }
  ```
- **Response**: 성공 메시지("수집 요청이 정상적으로 전달되었습니다.")

### 4. 데이터 삭제

- **Endpoint**: `DELETE /api/holidays`
- **Description**: 요청 받은 특정 연도와 국가의 공휴일 데이터를 삭제합니다.
- **Request Body**:
  ```json
  {
    "countryCodes": "KR",
    "year": 2025
  }
  ```
- **Response**: 요청 성공 메시지와 삭제된 데이터 정보
  ``` json
    {
    "message": "공휴일 삭제 요청 성공",
    "data": [
      {
        "date": "2025-01-01",
        "localName": "새해",
        "name": "New Year's Day",
        "countryCode": "KR",
        "global": true,
        "counties": [],
        "types": [
          "Public"
        ]
      }, /.../
      {
        "date": "2025-12-25",
        "localName": "크리스마스",
        "name": "Christmas Day",
        "countryCode": "KR",
        "global": true,
        "counties": [],
        "types": [
          "Public"
        ]
      }
    ]
  }
  ```

**테스트 성공 스크린샷:**
![image](https://github.com/user-attachments/assets/cce441cc-7e64-448b-b2fe-b5ad1060a7c7)


## API 문서 확인 방법 (Swagger)

애플리케이션 실행 후, 웹 브라우저에서 아래 주소로 접속하면 Swagger UI를 통해 API 명세를 확인하고 직접 테스트해볼 수 있습니다.
- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## 웹 화면
웹화면 [http://localhost:8080](http://localhost:8080) 에서도 테스트 해볼 수 있습니다.
![image](https://github.com/user-attachments/assets/31f00688-6746-458f-b426-c188a60c92a4)
