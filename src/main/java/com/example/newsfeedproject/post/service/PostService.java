package com.example.newsfeedproject.post.service;

import com.example.newsfeedproject.common.config.PasswordEncoder;
import com.example.newsfeedproject.post.dto.PostPageResponseDto;
import com.example.newsfeedproject.post.dto.PostPageResponseDto.PageInfo;
import com.example.newsfeedproject.post.dto.PostResponseDto;
import com.example.newsfeedproject.post.dto.PostUpdateResponseDto;
import com.example.newsfeedproject.post.dto.PostWithNameResponseDto;
import com.example.newsfeedproject.post.entity.Post;
import com.example.newsfeedproject.post.repository.PostRepository;
import com.example.newsfeedproject.user.entity.User;
import com.example.newsfeedproject.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //게시글을 생성하는 서비스 메서드
    public PostResponseDto createPost(HttpServletRequest request, String title, String content) {
        //requestSevlet을 통해 현재 로그인한 사용자의 세션을 가져온다.
        HttpSession session = request.getSession(false);

        //세션을 통해 게시글을 생성하려는 유저의 id를 가져온다.
        Long userId = (Long) session.getAttribute("userId");

        //세션을 통해 가져온 userId로 해당 유저 객체를 찾고 Post 객체를 생성
        User postUser = userRepository.findByIdOrElseThrow(userId);
        Post savedPost = postRepository.save(new Post(title, content, postUser));

        return new PostResponseDto(
            savedPost.getId(),
            postUser.getId(),
            savedPost.getTitle(),
            savedPost.getContent(),
            savedPost.getCreatedAt()
        );
    }

    //게시글 페이징 처리 로직
    public PostPageResponseDto getPostsPaginated(int page, int size) {
        //작성일 기준 내림차순 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        //pageable 객체를 통해 리포지토리에서 Post를 Page 객체로 조회해 가져온다.
        Page<Post> postPage = postRepository.findAll(pageable);

        //반환된 page 객체 내부의 Post 객체를 Dto로 반환하는 과정, 작성자의 이름을 포함한다.
        Page<PostWithNameResponseDto> posts = postPage.map(post -> new PostWithNameResponseDto(
            post.getId(),
            post.getUser().getName(),
            post.getTitle(),
            post.getContent(),
            post.getCreatedAt(),
            post.getModifiedAt()
        ));

        //페이지 정보 객체를 생성
        PageInfo pageInfo = new PageInfo(
            posts.getNumber() + 1,
            posts.getSize(),
            (int) posts.getTotalElements(),
            posts.getTotalPages()
        );

        //페이지 정보와 PostWithNameResponseDto 를 List형태로 담아서 반환
        return new PostPageResponseDto(posts.getContent(), pageInfo);
    }

    //게시글 수정 로직
    public PostUpdateResponseDto updatePost(HttpServletRequest request, Long postId, String title,
        String content, String password) {
        //자신의 게시글을 수정하는 것이 맞는지, 비밀번호가 맞는지 검증하고 맞다면 해당 게시글 객체를 반환
        Post post = validateUserAccess(request, postId, password);

        //수정할 게시글 제목, 내용을 setter로 설정 후 DB에 저장.
        post.setTitle(title);
        post.setContent(content);
        Post updatedPost = postRepository.save(post);

        return new PostUpdateResponseDto(
            updatedPost.getId(),
            updatedPost.getTitle(),
            updatedPost.getContent(),
            updatedPost.getModifiedAt()
        );
    }

    //게시글 삭제 로직
    public void deletePost(HttpServletRequest request, Long postId, String password) {
        //자신의 게시글을 수정하는 것이 맞는지, 비밀번호가 맞는지 검증하고 맞다면 해당 게시글 객체를 반환
        Post post = validateUserAccess(request, postId, password);

        //검증이 완료되면 DB에서 해당 게시글을 삭제
        postRepository.delete(post);
    }

    //게시글 수정,삭제 권한 및 비밀번호 검증 후 Post 객체 반환
    private Post validateUserAccess(HttpServletRequest request, Long postId, String password) {
        //requestSevlet을 통해 현재 로그인한 사용자의 세션을 가져온다.
        HttpSession session = request.getSession(false);

        //세션을 통해 게시글을 생성하려는 유저의 id를 가져온다.
        Long userId = (Long) session.getAttribute("userId");

        //요청 정보에 받아온 postId로 해당 게시물을 찾고 해당 게시물에 담겨있는 User객체를 가져옴
        Post post = postRepository.findByIdOrElseThrow(postId);
        User postUser = post.getUser();

        if (!userId.equals(postUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "자신의 게시물만 수정할 수 있습니다.");
        }
        if (!passwordEncoder.matches(password, postUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 일치하지 않습니다.");
        }

        return post;
    }

}
