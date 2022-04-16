# Todo API
Todo API 서버로, TODO 앱(프론트)을 만들때, 해당 서버와 연동하여 
프로젝트 실습을 진행하실 수 있습니다.


### 사용법
별도의 프로그램 설치없이 도커 이미지를 만들고, 실행시키기만 하면 끝!
1. 프로젝트 클론 받기
2. 도커 실행하기
3. 클론 받은 프로젝트 내에서 다음 명령어를 실행하여 도커 이미지 만들기.
    
    
    $ ./gradlew bootBuildImage

4. 다음 명령어로 도커 이미지를 확인합니다.


    $ docker run --rm -p 8080:8080 todo-api

5.다음 명령어로 생성된 이미지를 실행합니다. 

    $ docker run --rm -p 8080:8080 {Image Id}



### API 정보
#### 1. 할일 리스트 추가

API 기본 정보

메서드 | 요청 URI | 요청 타입| 출력 포맷
---| ---| ---| ---|
Post | /v1/todo | application/json | json

요청 

요청 변수명 | 타입 | 필수 여부 | 기본값 | 설명 
---| ---| ---| ---| ---|
desc | String | Y | - | 해야 할일을 파라미터로 넣습니다.

성공 응답

응답 변수명 | 타입 | 필수 여부 | 기본값 | 설명
---| ---| ---| ---| ---|
id | Long | Y | - | 생성된 할일의 유니크한 id입니다.
desc | String | Y | - | 생성된 할일입니다.
performStatus | TodoPerformStatus(enum) | Y | - | 할일의 상태입니다. 다음 두가지 상태를 갖습니다.(COMPLETED(완료), ACTIVE(해야할 일))   
updatedAt | LocalDateTime | Y | - | 수정 시간

실패 응답

없음


#### 2. 할일 리스트 조회
API 기본 정보

메서드 | 요청 URI | 요청 타입| 출력 포맷
---| ---| ---| ---|
Get | /v1/todo | application/json | json

요청

요청 변수명 | 타입 | 필수 여부 | 기본값 | 설명
---| ---| ---| ---| ---|
performStatus | TodoPerformStatus(enum) | N | ACTIVE | 상태를 필터링해서 조회할 수 있습니다. null일 시에는 모든 상태를 조회합니다. 다음 두가지 상태를 갖습니다.(COMPLETED(완료), ACTIVE(해야할 일))
isUpdatedAtDesc | boolean | N | true | 수정일 기준 오름차/내림차순 조회, true면 내림차순 조회 

성공 응답 

응답 변수명 | 타입 | 필수 여부 | 기본값 | 설명
---| ---| ---| ---| ---|
- | Todo[] | Y | []  | 할일 리스트

Todo

응답 변수명 | 타입 | 필수 여부 | 기본값 | 설명
---| ---| ---| ---| ---|
id | Long | Y | - | 생성된 할일의 유니크한 id입니다.
desc | String | Y | - | 생성된 할일입니다.
performStatus | TodoPerformStatus(enum) | Y | - | 할일의 상태입니다. 다음 두가지 상태를 갖습니다.(COMPLETED(완료), ACTIVE(해야할 일))
updatedAt | LocalDateTime | Y | - | 수정 시간

실패 응답

없음




#### 3. 할일 삭제
API 기본 정보

메서드 | 요청 URI | 요청 타입| 출력 포맷
---| ---| ---| ---|
Delete | /v1/todo/{todoId} | application/json | json

요청

요청 변수명 | 타입 | 필수 여부 | 기본값 | 설명
---| ---| ---| ---| ---|
todoId | Long | Y | - | 삭제 할 할일의 id

성공 응답

없음

실패 응답

없음

#### 3. 할일 수정
API 기본 정보

메서드 | 요청 URI | 요청 타입| 출력 포맷
---| ---| ---| ---|
Put | /v1/todo/{todoId} | application/json | json

요청

요청 변수명 | 타입 | 필수 여부 | 기본값 | 설명
---| ---| ---| ---| ---|
todoId | Long | Y | - | 변경 할 할일의 id
desc | String | n | - | 변경 할 할일 설명
performStatus | TodoPerformStatus(enum) | n | 변경할 상태, 다음 두가지 상태를 갖습니다.(COMPLETED(완료), ACTIVE(해야할 일))

성공 응답

응답 변수명 | 타입 | 필수 여부 | 기본값 | 설명
---| ---| ---| ---| ---|
id | Long | Y | - | 변경된 할일의 유니크한 id입니다.
desc | String | Y | - | 변경된 할일입니다.
performStatus | TodoPerformStatus(enum) | Y | - | 할일의 상태입니다. 다음 두가지 상태를 갖습니다.(COMPLETED(완료), ACTIVE(해야할 일))
updatedAt | LocalDateTime | Y | - | 수정 시간


실패 응답

http Status code | errorCode | extra
---| ---| ---|
404 | NOT_FOUND | -


### 사용 기술
- WebFlux
- Spring R2DBC
- H2 DB

### 회고
R2DBC를 이용하여 논블록킹으로 DB의 데이터를 읽고 쓸 수 있는 장점이 있다.
하지만 아직 테스트 코드에서 @Transaction 어노테이션에 대해서 지원하지 않아서 우회 방법이 필요하다.
또한, R2dbcEntityTemplate를 이용해서 동적 쿼리를 만들 수 없는 점 또한 아쉽다.
