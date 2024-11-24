## 프로젝트 개요

- **제목 : MOMO (모두모여)**

프로젝트 MOMO는 사람들이 낯선 사람들과 쉽게 급약속을 잡고, 다양한 동아리를 형성할 수 있도록 지원하는 커뮤니티 SNS 플랫폼입니다. 이 프로젝트는 사용자들이 새로운 사람들과의 만남을 통해 다양한 경험을 공유하고, 유대감을 형성할 수 있는 공간을 제공합니다.

---

## 와이어 프레임

![image](https://github.com/user-attachments/assets/0ea49c72-9ce3-4268-a42e-540348e6a6f9)

---

## 뉴스피드 프로젝트 API 명세서

### 참고 사항

1. 세션 기반 인증 방식을 사용. 
    
    `HttpServletRequest`를 통해 `authorId`를 세션에 저장하여 인증을 관리한다.
    
2. 로그인 필터를 사용해 로그인 되었음을 확인한다.

1. `TimeBaseEntity` 를 생성하여 `Auditing`을 사용해 작성일, 수정일을  포함하도록 한다.

1. `PasswordEncoder`를 생성하여 요청되는 패스워드를 암호화하여 사용한다.

---

### **1. 유저 회원가입 (POST /api/users/signup)**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Content-Type: application/json`
        - **Body**:
            
            ```json
            {
                "userName": "예시명",
                "userEmail": "example@example.com",
                "profileImageUrl": "http://example.com/profile.jpg",
                "password": "1234qwer"
            }
            
            ```
            
        - 설명:
            
            
            | # | 이름 | 타입 | 설명 | Nullable |
            | --- | --- | --- | --- | --- |
            | 1 | userName | String | 유저명 | X |
            | 2 | userEmail | String | 유저 이메일 | X |
            | 3 | profileImageUrl | String | 유저 프로필 이미지 링크 | O |
            | 4 | password | String | 유저 비밀번호 | X |
    - **응답**
        - **Status Code**: `201 Created`
        - **Body**:
            
            ```json
            {
                "userId": 1,
                "userName": "예시명",
                "userEmail": "example@example.com",
                "profileImageUrl": "http://example.com/profile.jpg",
                "createdAt": "2024-11-13T14:12:27.223",
                "modifiedAt": "2024-11-13T14:12:27.223"
            }
            
            ```
            
        - 설명:
            
            
            | # | 이름 | 타입 | 설명 | Nullable |
            | --- | --- | --- | --- | --- |
            | 1 | userId | Long | 유저 고유 식별자 | X |
            | 2 | userName | String | 유저명 | X |
            | 3 | userEmail | String | 유저 이메일 | X |
            | 4 | profileImageUrl | String | 유저 프로필 이미지 링크 | O |
            | 5 | createdAt | String | 유저 최초 등록 일시 | X |
            | 6 | modifiedAt | String | 유저 수정 일시 | X |
    

---

### **2. 유저 로그인 (POST /api/users/login)**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Content-Type: application/json`
        - **Body**:
            
            ```json
            {
                "userEmail": "example@example.com",
                "password": "1234qwer"
            }
            
            ```
            
        - 설명:
            
            
            | # | 이름 | 타입 | 설명 | Nullable |
            | --- | --- | --- | --- | --- |
            | 1 | userEmail | String | 유저 이메일 | X |
            | 2 | password | String | 유저 비밀번호 | X |
    - **응답**
        - **Status Code**: `200 OK`
        - **Body : X**
    - **동작**
        - 성공 시, `HttpServletRequest`를 사용하여 세션 생성 및  `userId` 저장.
        - 예:
        
        ```java
        HttpSession session = request.getSession();
        session.setAttribute("userId", userId);
        ```
        
        - 예외
            - `404 Not Found`: 해당 사용자가 존재하지 않는 경우
            - `401 Unauthorized`: 세션이 유효하지 않거나 만료된 경우

---

### **3. 유저 로그아웃 (POST /api/users/logout)**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**: 없음
    - **동작**
        - 세션 제거
            
            ```java
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            
            ```
            
    - **응답**
        - **Status Code**: `200 OK`
        - **Body**: X

---

### **4. 유저 회원탈퇴 (DELETE /api/users/{userId})**

회원 탈퇴 기능을 구현하기 위해서 User 테이블에 boolean 타입의 칼럼이 하나 있어야함.

- 상세 내용 펼쳐보기
    - **요청**
        - **Path Variables:**
            - `{userId}` : 탈퇴할 사용자의 ID
        - **Body:**
        
        ```json
        {
            "password": "1234qwer"
        }
        ```
        
    - **응답**
        - **Status Code**: `200 OK`
        - **Body**: X
        
        - 예외
            - `404 Not Found`: 해당 사용자가 존재하지 않는 경우, 이미 탈퇴처리된 사용자인 경우
            - `403 Forbidden`: 비밀번호가 일치하지 않는 경우
            
             
            

### **5. 프로필 관리 기능**

비밀번호 수정과 정보 수정을 따로 분리하였다.

### **5-1. 프로필 조회 (GET /api/users/profiles/{userId})**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}` 로그인하면 자동으로 요청됨.
        - **Path Variables**:
            - `{userId}`: 조회할 사용자의 ID
    - **응답**
        - **Status Code**: 200 OK
        - **Body**:
            
            ```json
            {
                "userId": 1,
                "userName": "예시명",
                "userEmail": "example@example.com",
                "profileImageUrl": "http://example.com/profile.jpg"
            }
            ```
            
            | # | 이름 | 타입 | 설명 | Nullable |
            | --- | --- | --- | --- | --- |
            | 1 | userId | String | 유저 고유 식별자 | X |
            | 2 | userName | String | 유저명 | X |
            | 3 | userEmail | String | 유저 이메일 | X |
            | 4 | profileImageUrl | String | 유저 프로필 이미지 링크 | O |
        
        - **예외**
            - `404 Not Found`: 해당 사용자가 존재하지 않는 경우
            - `401 Unauthorized`: 세션이 유효하지 않거나 만료된 경우

---

### **5-2. 프로필 정보 수정 (PUT /api/users/profiles)**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        - **Path Variables**: X
        - **Body**:
            
            ```json
            {
                "userName": "새 이름",
                "profileImageUrl": "http://example.com/new-profile.jpg",
                "password" : "1234qwer"
            }
            ```
            
            | # | 이름 | 타입 | 설명 | Nullable |
            | --- | --- | --- | --- | --- |
            | 1 | userName | String | 수정할 유저명 | X |
            | 2 | profileImageUrl | String | 수정할 유저 프로필 이미지 링크 | X |
            | 3 | password | String | 수정 검증할 유저 비밀번호 | X |
    - **응답**
        - **Status Code**: 200 OK
        - **Body**:
            
            ```json
            {
                "userId": 1,
                "username": "새 이름",
                "profileImageUrl": "http://example.com/new-profile.jpg",
                "modifiedAt": "2024-11-19T14:12:27.2238587"
            }
            ```
            
    - **예외**
        - `400 Bad Request`: 요청 필드가 잘못된 경우

---

### **5-3. 비밀번호 수정 (PATCH /api/users/profiles)**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        - **Body**:
            
            ```json
            {
                "password": "1234qwer",
                "newPassword": "1234asdf"
            }
            ```
            
            | # | 이름 | 타입 | 설명 | Nullable |
            | --- | --- | --- | --- | --- |
            | 1 | password | String | 해당 유저 비밀번호 | X |
            | 2 | newPassword | String | 변경할 비밀번호 | X |
    - **응답**
        - **Status Code**: 200 OK
        - **Body**: X
    - **예외**
        - `403 Forbidden`:
            - 현재 비밀번호가 일치하지 않을 경우
            - 새로운 비밀번호가 현재 비밀번호와 동일한 경우

---

### **6. 뉴스피드 게시물 관리**

### 참고 사항

게시물 작성이나 수정 시 HTTP 세션에 로그인할 때 저장해둔 userId를 가지고와서

해당 유저가 글을 작성한다고 생각하고 만들 예정

따라서 따로 userId 를 정보로 넣어줄 필요가 없음.

### **6-1. 게시물 작성 (POST /api/posts)**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        - **Body**:
            
            ```json
            {
                "title": "게시물 제목",
                "content": "게시물 내용"
            }
            ```
            
            | # | 이름 | 타입 | 설명 | Nullable |
            | --- | --- | --- | --- | --- |
            | 1 | title | String | 게시물 제목 | X |
            | 2 | content | String | 게시물 내용 | X |
    - **응답**
        - **Status Code**: 201 Created
        - **Body**:
            
            ```json
            {
                "postId": 1,
                "userId": 1,
                "title": "게시물 제목",
                "content": "게시물 내용",
                "createdAt": "2024-11-19T14:12:27.2238587"
            }
            ```
            
    - **예외**
        - `400 Bad Request`: 제목 또는 내용이 누락된 경우

---

### **6-2. 게시물 조회 (GET /api/posts/?page=1&size=10)**

기본 정렬은 생성일자 ****기준으로 내림차순 정렬.

- 상세 내용 펼쳐보기
    - **요청**
        - **Query Parameters**:
            - `page` (default: 1): 조회할 페이지 번호
            - `size` (default: 10): 페이지당 게시물 개수
    - **응답**
        - **Status Code**: 200 OK
        - **Body**:
            
            ```json
            {
                "currentPage": 1,
                "totalPages": 5,
                "posts": [
                    {
                        "postId": 1,
                        "userName": "홍길동",
                        "title": "게시물 제목",
                        "content": "게시물 내용",
                        "createdAt": "2024-11-19T14:12:27.2238587"
                    }
                ]
            }
            
            ```
            
    

---

### **6-3. 게시물 수정 (PATCH /api/posts/{postId})**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        - **Path Variables**:
            - `{postId}`: 수정할 게시물 ID
        - **Body**:
            
            ```json
            {
                "title": "수정된 제목",
                "content": "수정된 내용",
                "password": "1234qwer"
            }
            
            ```
            
    - **응답**
        - **Status Code**: 200 OK
        - **Body**:
            
            ```json
            {
                "postId": 1,
                "title": "수정된 제목",
                "content": "수정된 내용",
                "modifiedAt": "2024-11-19T15:00:00.123456"
            }
            
            ```
            
    - **예**
        - `403 Forbidden`: 작성자가 아닌 사용자가 수정하려는 경우
        - `404 Not Found`: 해당 게시물이 없는 경우

---

### **6-4. 게시물 삭제 (DELETE /api/posts/{postId})**

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        - **Path Variables**:
            - `{postId}`: 삭제할 게시물 ID
        - Body:
            
            ```json
            {
            		"password" : "1234qwer"
            }
            ```
            
    - **응답**
        - **Status Code**: 200 OK
        - **Body**: X
    - **예외**
        - `403 Forbidden`: 작성자가 아닌 사용자가 삭제하려는 경우
        - `404 Not Found`: 해당 게시물이 없는 경우

---

### 7. 친구 관련 기능

### 참고사항

`HttpServletRequest request` 를 사용하여 세션에 저장된 사용자 정보를 가져와 사용한다.

`Friend` 객체를 생성할 때 `receiver_id` 에 요청할 때 받아온 `receiverId`를 넣고 

`requester_id` 는세션에 저장된 `사용자 정보의 id 값`을 넣는다.

`Request status` 라는 ENUM 상태값을 만들어 요청 보냈을 때 상태값 `WAITING`,

요청 수락했을 때 상태값 `ACCEPTED` 로 설정해 요청 수락 여부를 확인

### 7-1. 친구 신청 (POST /api/friends/request)

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        
        - **Body**:
            
            ```json
            {
              "receiverId": 2
            }
            ```
            
    - **응답**
        - **Status Code**: 200 OK
        - **Body**: X
    - **예외**
        - `404 Not Found`: 해당 유저가 존재하지않는 경우

---

### 7-2. 친구 요청 수락 (POST /api/friends/request/{friendId})

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        - **Path Variable**: `friendId`(요청을 수락할 friendId)
    - **응답**
        - **Status Code**: 200 OK
        - **Body**: X
    - **예외**
        - `404 Not Found`: 해당 친구 신청이 존재하지 않는 경우
    

---

### 7-3. 친구 요청 거절(삭제) (DELETE /api/friends/{friendId})

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
        
        - **Path Variable**: `friendId`(요청을 삭제할 friendId)
    - **응답**
        - **Status Code**: 200 OK
        - **Body**: X
    - **예외**
        - `404 Not Found`: 해당 친구 신청이 존재하지 않는 경우

---

### 7-4. 친구의 최신 게시물 조회 (GET /api/friends/newsfeed)

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
        - **응답**
            - **Status Code**: 200 OK
            - **Body**:
            
            ```json
            [
              {
                "postId": 1,
                "author": "홍길동",
                "title": "게시물 제목",
                "content": "게시물 내용",
                "createdAt": "2024-11-19T14:00:00"
              },
              {
                "postId": 2,
                "author": "김철수",
                "title": "게시물 제목",
                "content": "게시물 내용",
                "createdAt": "2024-11-18T12:00:00"
              }
            ]
            ```
            
        

---

### 7-5. 친구 삭제 (DELETE /api/friends/remove

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
        - Body:
        
        ```json
        {
          "friendId": 2
        }
        ```
        
        - **응답**
            - **Status Code**: 200 OK
            - **Body**:
        - **예외**
            - `404 Not Found`: 해당 친구 신청이 존재하지 않는 경우

---

### 8. 댓글 관련 기능

### 8-1. 댓글 작성 (POST/api/posts/{postId}/comments)

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        
        - **Body**:
            
            ```json
            {
               "content": "우와 재밌겠다"
            }
            ```
            
    - **응답**
        - **Status Code**: 201 Created
        
        - **Body**:
            
            ```json
            {
                "id": 1,
                "userName": "짱구",
                "content": "우와 재밌겠다",
                "createdAt": "2024-11-22T10:00:00",
            }
            ```
            
        
    - **예외**
        - `400 Bad Request`: 내용이 누락된 경우

---

### 8-2. 댓글 조회 (GET/api/posts/{postId}/comments)

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
        
    - **응답**
        - **Status Code**: 200 OK
        - **Body**:
            
            ```json
            [
                {
                    "id": 1,
                    "userName": "짱구",
                    "content": "우와 재밌겠다",
                    "createdAt": "2024-11-22T10:00:00"
                }
            ]
            ```
            
    - **예외**
        - `404 Not Found`: 해당 유저가 존재하지 않는 경우
        - `404 Not Found`: 해당 게시글이 존재하지 않는 경우

---

### 8-3. 댓글 수정 (PUT/api/posts/comments/{commentId})

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
            - `Content-Type: application/json`
        - **Path Variables**:
            - `{commentId}`: 수정할 댓글 ID
        - **Body**:
            
            ```json
            {
                "content": "우와 재밌겠다",
                "password": "1234Qwer!"
            }
            ```
            
    - **응답**
        - **Status Code**: 200 OK
        - **Body**:
            
            ```json
            {
                "id": 1,
                "userName": "짱구",
                "comment": "우와 재밌겠다",
                "updatedAt": "2024-11-22T11:00:00"
            }
            ```
            
    - **예외**
        - `403 FORBIDDEN`: 비밀번호가 일치하지 않는 경우, 댓글 작성자가 아닌 유저가 수정을 시            도하려고 할 경우
        - `404 Not Found` : 존재하지 않는 댓글인 경우

---

### 8-4. 댓글 삭제 (DELETE/api/posts/comments/{commentId})

- 상세 내용 펼쳐보기
    - **요청**
        - **Headers**:
            - `Cookie: JSESSIONID={sessionId}`
        - **Path Variables**:
            - `{commentId}`: 삭제할 댓글 ID
        
    - **응답**
        - **Status Code**: 204 No Content
        - **Body**: X
    - **예외**
        - `403 Forbidden`: 댓글 작성자 본인이나 게시글 작성자가 아닌 사용자가 삭제하려는 경우
        - `404 Not Found`: 해당 댓글이 없는 경우
     
---


## ERD

- 필수 구현
![image](https://github.com/user-attachments/assets/d93e728c-00fa-48f3-bd8a-cae39676bbb2)

- 도전 구현
![image](https://github.com/user-attachments/assets/a6e71788-294a-44bf-a7c6-be9aa3d3633c)
