package com.app.features.admin.controller;

import com.app.features.admin.model.Post;
import com.app.features.admin.service.AdminService;
import com.app.features.admin.service.FeedService;
import com.app.features.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;
    private final AdminService adminService;

    // ######### POSTS #########
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = adminService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<Post> likePost(@PathVariable Long postId, @RequestAttribute("authenticatedUser") User user) {
        Post post = feedService.likePost(postId, user.getId());
        return ResponseEntity.ok(post);
    }

    @PutMapping("/posts/{postId}/dislike")
    public ResponseEntity<Post> dislikePost(@PathVariable Long postId, @RequestAttribute("authenticatedUser") User user){
        Post post = feedService.dislikePost(postId, user.getId());
        return ResponseEntity.ok(post);
    }
}
