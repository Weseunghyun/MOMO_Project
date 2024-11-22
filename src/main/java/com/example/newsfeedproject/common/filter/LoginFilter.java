package com.example.newsfeedproject.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

//로그인된 사용자만 특정 URL에 접근할 수 있도록 필터링하는 역할을 수행한다.
//인증이 필요한 URL에 대한 요청이 들어올 때 세션에 userId가 존재하는지 확인하고
//존재하지 않는다면 예외를 발생시켜서 접근을 차단한다.
@Slf4j
public class LoginFilter implements Filter {

    // 인증을 하지 않아도될 URL Path 배열
    // 회원가입, 로그인, 로그아웃, 로그인 안한 상태의 게시물 조회는 인증을 할 필요가 없다.
    private static final String[] WHITE_LIST = {"/", "/api/posts", "/api/users/signup",
        "/api/users/login",
        "/api/users/logout"};

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {

        // 다양한 기능을 사용하기 위해 다운 캐스팅
        //해당 요청에 대한 정보를 얻어옴.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        log.info("로그인 필터 로직 실행");

        // 로그인을 체크 해야하는 URL인지 검사
        // whiteListURL에 포함된 경우 true 반환 -> !true = false
        if (!isWhiteList(requestURI)) {

            // 로그인 확인 -> 로그인하면 session에 값이 저장되어 있다는 가정.
            // 세션이 존재하면 가져온다. 세션이 없으면 session = null
            HttpSession session = httpRequest.getSession(false);

            // 로그인하지 않은 사용자인 경우
            if (session == null || session.getAttribute("userId") == null) {
                // JSON 형태의 응답 작성
                httpResponse.setContentType("application/json");
                httpResponse.setCharacterEncoding("UTF-8");
                httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

                // JSON 형태의 에러 메시지 작성
                String jsonResponse = "{\"error\": \"로그인 후 할 수 있는 작업입니다.\"}";
                httpResponse.getWriter().write(jsonResponse);
                return; // 필터 종료
            }

        }

        // 1번경우 : whiteListURL에 등록된 URL 요청이면 바로 chain.doFilter()
        // 2번경우 : 필터 로직 통과 후 다음 필터 호출 chain.doFilter()
        // 다음 필터 없으면 Servlet -> Controller 호출
        chain.doFilter(request, response);
    }

    // 로그인 여부를 확인하는 URL인지 체크하는 메서드
    private boolean isWhiteList(String requestURI) {
        // request URI가 whiteListURL에 포함되는지 확인
        // 포함되면 true 반환
        // 포함되지 않으면 false 반환
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
