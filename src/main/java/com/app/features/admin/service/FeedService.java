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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Post ratePost(Long postId, Long authorId, boolean isLike) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new UserNotFoundException("User not found."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found."));

        if (isLike) { // Like
            if (post.getDislikes().contains(author)) {
                post.getDislikes().remove(author);
            }
            if (post.getLikes().contains(author)) {
                post.getLikes().remove(author);
            } else {
                post.getLikes().add(author);
            }
        } else { // Dislike
            if (post.getLikes().contains(author)) {
                post.getLikes().remove(author);
            }
            if (post.getDislikes().contains(author)) {
                post.getDislikes().remove(author);
            } else {
                post.getDislikes().add(author);
            }
        }

        return postRepository.save(post);
    }

    public Comment addComment(Long postId, CommentDto commentDto, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found."));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        Comment comment = Comment.builder()
                .post(post)
                .content(commentDto.getContent())
                .user(user)
                .build();
        return commentRepository.save(comment);
    }
}
