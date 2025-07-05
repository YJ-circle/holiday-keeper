# Holiday Keeper

이 프로젝트는 Nager.Date 무인증 API 활용해 전 세계 공휴일 관리 서비스를 제공합니다.

## 실행 방법
`infra` 폴더에서 다음 명령을 실행하세요:
```
docker-compose -p "holiday-keeper" up -d
```

## 프로젝트 설명

### 데이터베이스
이 프로젝트는 H2 데이터베이스를 활용합니다.

프로젝트 실행 방법을 참고하여, 프로젝트를 실행하면

자동으로 H2 데이터베이스 컨테이너가 실행되며, 아래와 같은 사용자 ID가 생성됩니다.
```
ID: test
PW: 1234
```

#### 접속 방법
데이터베이스는 웹을 통해서도 접속이 가능합니다.
1) 웹 브라우저에서 `http://localhost:18181/`로 접속합니다.
2) 아래 이미지를 참고하여, 로그인합니다.
JDBC URL: jdbc:h2:tcp://localhost:9092/test
사용자명: test
비밀번호: 1234
![image](https://github.com/user-attachments/assets/d7f44159-1433-439b-877f-3a706367ada4)

