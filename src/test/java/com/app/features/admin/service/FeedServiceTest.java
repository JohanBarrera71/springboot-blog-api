package com.app.features.admin.service;

import com.app.exceptions.PostNotFoundException;
import com.app.exceptions.UserNotFoundException;
import com.app.features.admin.dto.CommentDto;
import com.app.features.admin.model.Comment;
import com.app.features.admin.model.Post;
import com.app.features.admin.repository.CommentRepository;
import com.app.features.admin.repository.PostRepository;
import com.app.features.auth.model.User;
import com.app.features.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;


import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private FeedService feedService;

    // ################## RatePost() ##################
    @Test
    public void testRatePost_like_newLike() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        boolean isLike = true;

        User author = User.builder()
                .id(authorId)
                .build();
        Post post = Post.builder()
                .id(postId)
                .likes(new HashSet<>())
                .dislikes(new HashSet<>())
                .build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        Post result = feedService.ratePost(postId, authorId, isLike);

        // then
        assertTrue(result.getLikes().contains(author));
        assertFalse(result.getDislikes().contains(author));
        verify(postRepository).save(post);
    }

    @Test
    public void testRatePost_like_removeExistingLike() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        boolean isLike = true;

        User author = User.builder()
                .id(authorId)
                .build();
        Post post = Post.builder()
                .id(postId)
                .likes(new HashSet<>())
                .dislikes(new HashSet<>())
                .build();
        post.getLikes().add(author);

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        Post result = feedService.ratePost(postId, authorId, isLike);

        // then
        assertFalse(result.getLikes().contains(author));
        assertFalse(result.getDislikes().contains(author));
        verify(postRepository).save(post);
    }

    @Test
    public void testRatePost_dislike_newDislike() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        boolean isLike = false;

        User author = User.builder()
                .id(authorId)
                .build();
        Post post = Post.builder()
                .id(postId)
                .likes(new HashSet<>())
                .dislikes(new HashSet<>())
                .build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        Post result = feedService.ratePost(postId, authorId, isLike);

        // then
        assertFalse(result.getLikes().contains(author));
        assertTrue(result.getDislikes().contains(author));
        verify(postRepository).save(post);
    }

    @Test
    public void testRatePost_dislike_removeExistingDislike() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        boolean isLike = false;

        User author = User.builder()
                .id(authorId)
                .build();
        Post post = Post.builder()
                .id(postId)
                .likes(new HashSet<>())
                .dislikes(new HashSet<>())
                .build();
        post.getDislikes().add(author);

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        Post result = feedService.ratePost(postId, authorId, isLike);

        // then
        assertFalse(result.getLikes().contains(author));
        assertFalse(result.getDislikes().contains(author));
        verify(postRepository).save(post);
    }

    @Test
    public void testRatePost_changeFromDislikeToLike() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        boolean isLike = true;

        User author = User.builder()
                .id(authorId)
                .build();
        Post post = Post.builder()
                .id(postId)
                .likes(new HashSet<>())
                .dislikes(new HashSet<>())
                .build();
        post.getDislikes().add(author);

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        Post result = feedService.ratePost(postId, authorId, isLike);

        // then
        assertTrue(result.getLikes().contains(author));
        assertFalse(result.getDislikes().contains(author));
        verify(postRepository).save(post);
    }

    @Test
    public void testRatePost_changeFromLikeToDislike() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        boolean isLike = false;

        User author = User.builder()
                .id(authorId)
                .build();
        Post post = Post.builder()
                .id(postId)
                .likes(new HashSet<>())
                .dislikes(new HashSet<>())
                .build();
        post.getLikes().add(author);

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        Post result = feedService.ratePost(postId, authorId, isLike);

        // then
        assertFalse(result.getLikes().contains(author));
        assertTrue(result.getDislikes().contains(author));
        verify(postRepository).save(post);
    }

    @Test
    public void testRatePost_userNotFound() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        boolean isLike = true;

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> {
            feedService.ratePost(postId, authorId, isLike);
        });
        verify(postRepository, never()).save(any());
    }

    @Test
    public void testRatePost_postNotFound() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        boolean isLike = true;

        User author = User.builder()
                .id(authorId)
                .build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // then
        assertThrows(PostNotFoundException.class, () -> {
            feedService.ratePost(postId, authorId, isLike);
        });
        verify(postRepository, never()).save(any());
    }

    // ################## AddComment() ##################
    @Test
    public void testAddComment() {
        // given
        Long authorId = 1L;
        Long postId = 1L;
        CommentDto commentDto = CommentDto.builder()
                .content("Este es el comentario")
                .build();

        User author = User.builder()
                .id(1L)
                .build();
        Post post = Post.builder()
                .id(postId)
                .build();

        Comment expectedComment = Comment.builder()
                .post(post)
                .content(commentDto.getContent())
                .user(author)
                .build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(expectedComment);

        Comment actualComment = feedService.addComment(postId, commentDto, authorId);

        // then
        assertNotNull(actualComment);
        assertEquals(commentDto.getContent(), actualComment.getContent());
        assertEquals(postId, actualComment.getPost().getId());
        assertEquals(authorId, actualComment.getUser().getId());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void testAddComment_userNotFound() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        CommentDto commentDto = CommentDto.builder()
                .content("Este es el comentario")
                .build();

        // when
        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> {
            feedService.addComment(postId, commentDto, authorId);
        });
        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testAddComment_postNotFound() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        CommentDto commentDto = CommentDto.builder()
                .content("Este es el comentario")
                .build();

        // when
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // then
        assertThrows(PostNotFoundException.class, () -> {
            feedService.addComment(postId, commentDto, authorId);
        });
        verify(commentRepository, never()).save(any());
    }

}