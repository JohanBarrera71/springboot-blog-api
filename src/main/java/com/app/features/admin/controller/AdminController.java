package com.app.features.admin.controller;

import com.app.features.admin.dto.BlogDto;
import com.app.features.admin.dto.PostDto;
import com.app.features.admin.dto.ResponseDto;
import com.app.features.admin.model.Blog;
import com.app.features.admin.model.Post;
import com.app.features.admin.service.AdminService;
import com.app.features.auth.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/blogs-user")
    public ResponseEntity<List<Blog>> getAllBlogsByUser(@RequestAttribute("authenticatedUser") User user) {
        List<Blog> blogs = adminService.getAllBlogs(user.getId());
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/blogs")
    public ResponseEntity<Blog> createBlog(@RequestBody BlogDto blogDto, @RequestAttribute("authenticatedUser") User user) {
        Blog blog = adminService.createBlog(blogDto, user.getId());
        return ResponseEntity.ok(blog);
    }

    @PutMapping("/blogs/{blogId}")
    public ResponseEntity<Blog> editBlog(@PathVariable Long blogId, @RequestBody BlogDto blogDto, @RequestAttribute("authenticatedUser") User user) {
        Blog blog = adminService.editBlog(blogId, user.getId(), blogDto);
        return ResponseEntity.ok(blog);
    }

    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long blogId, @RequestAttribute("authenticatedUser") User user) {
        adminService.deleteBlog(blogId, user.getId());
        return ResponseEntity.noContent().build();
    }

    // Posts
    @GetMapping("/posts-user")
    public ResponseEntity<List<Post>> getAllPostsByUser(@RequestAttribute("authenticatedUser") User user) {
        List<Post> posts = adminService.getAllPostsByUser(user.getId());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getContentPost(@PathVariable Long postId) {
        Post post = adminService.getContentPost(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/posts/{blogId}")
    public ResponseEntity<Post> createPost(@PathVariable Long blogId, @RequestBody PostDto postDto, @RequestAttribute("authenticatedUser") User user) {
        Post post = adminService.createPost(blogId, postDto, user.getId());
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable Long postId, @RequestBody PostDto postDto, @RequestAttribute("authenticatedUser") User user) {
        Post post = adminService.editPost(postId, postDto, user.getId());
        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseDto> deletePost(@PathVariable Long postId, @RequestAttribute("authenticatedUser") User user) {
        adminService.deletePost(postId, user.getId());
        return ResponseEntity.ok(new ResponseDto("Post deleted successfully."));
    }
}
