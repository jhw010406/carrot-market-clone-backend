<h2>기술 스택</h2>
<p>
        <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&amp;logo=java&amp;logoColor=white">
        <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
        <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
        <img src="https://img.shields.io/badge/Amazon EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
        <img src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
        <img src="https://img.shields.io/badge/Amazon S3-6DB33F?style=for-the-badge&logo=amazons3&logoColor=white">
</p>
<br>

<h2>프로젝트 내용</h2>
<ol>
  <li><a href="#1-architecture">Architecture</a></li>
  <li><a href="#2-erd">ERD</a></li>
        <ul>
                <li>게시글, 유저 정보 등 조회 요청이 잦은 테이블엔 int 값을 primary key로 두어, 검색 성능에 영향을 줄이도록 하였습니다.</li>
                <li>고유의 int 값은 MySQL에서 제공하는 auto_increment 로 결정됩니다.</li>
        </ul>
  <li>구현 내용</li>
          <ul>
                  <li><a href="https://github.com/jhw010406/carrot-market-clone-backend/blob/master/src/main/java/com/jhw/carrot_market_clone_backend/filter/JwtAuthenticationFilter.java">사용자 인증</a></li>
                  <ul>
                          <li>filter에서 요청 헤더에 포함된 JWT를 불러와 검증합니다.</li>
                          <li>검증 실패 시, filter에서 요청이 종료되므로 불필요한 DispatcherServlet 호출을 하지 않습니다.</li>
                  </ul>
                  <br>
                  <li>
                          <a href="https://github.com/jhw010406/carrot-market-clone-backend/blob/master/src/main/java/com/jhw/carrot_market_clone_backend/service/RegisterService.java">회원가입</a>
                          /
                          <a href="https://github.com/jhw010406/carrot-market-clone-backend/blob/master/src/main/java/com/jhw/carrot_market_clone_backend/service/LoginService.java">로그인</a>
                  </li>
                  <ul>
                          <li>회원 가입 요청 시, 유저 아이디와 비밀번호를 db에 저장합니다.</li>
                          <li>비밀번호는 bcrypt로 암호화되어 저장되며, 로그인 요청 시 db에 저장되어있는 해시된 비밀번호와 요청으로 주어진 비밀번호를 비교하여 로그인 성공 여부를 판단합니다.</li>
                          <li>회원가입 또는 로그인 시, 클라이언트에게 JWT를 새로 발급해줍니다.</li>
                  </ul>
                  <br>
                  <li>게시글 생성 / 조회 / 수정 / 삭제</li>
                  <ul>
                          <li>게시글 생성</li>
                          <ul>
                                  <li>client가 이미지 업로드 요청 시, S3로부터 presigned url를 받아 client에게 전달합니다.</li>
                                  <li>게시글 생성 요청이 올 시, RDS에 데이터 삽입합니다.</li>
                          </ul>
                          <li>게시글 조회</li>
                          <ul>
                                  <li>RDS의 레코드들을 일정 개수 불러옵니다.</li>
                                  <li>JPA의 limit와 offset을 응용하여 페이징을 구현하였습니다.</li>
                          </ul>
                          <li>게시글 수정</li>
                          <ul>
                                  <li>게시글의 text 부분은 update 하되, image 부분은 전체 삭제 후, 다시 저장합니다.<br>이는 게시글 수정으로 인해 기존 이미지가 삭제된 경우를 반영하기 위함입니다.</li>
                          </ul>
                          <li>게시글 삭제</li>
                          <ul>
                                  <li>요청 client의 유저 uid와 삭제 요청 대상이 되는 게시글의 작성자 uid를 비교하여, 일치할 시 게시글을 삭제합니다.</li>
                          </ul>
                  </ul>
          </ul>
</ol>

<h2>1. Architecture</h2>
<img src="https://github.com/user-attachments/assets/b0c5dc5d-917f-4dba-b4b7-40cdca42b0b4" width=100%>

<h2>2. ERD</h2>
<img src="https://github.com/user-attachments/assets/94f52b8f-d4cb-4341-aa79-81077a10b6bf">
