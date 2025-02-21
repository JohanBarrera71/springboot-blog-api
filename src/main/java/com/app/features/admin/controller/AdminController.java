package com.app.features.admin.controller;

import com.app.exceptions.dto.MessageResponse;
import com.app.features.admin.dto.BlogDto;
import com.app.features.admin.dto.PostDto;
import com.app.features.admin.dto.ResponseDto;
import com.app.features.admin.model.Blog;
import com.app.features.admin.model.Post;
import com.app.features.admin.service.AdminService;
import com.app.features.auth.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Controller for Administration of Blogs and Posts")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/blogs-user")
    @Operation(
            summary = "Get All Blogs By User",
            description = "Get all blogs by an authenticated user",
            //tags = {"Administration", "Blogs"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Blog.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<List<Blog>> getAllBlogsByUser(
            @Parameter(name = "Authenticated user", required = true) @RequestAttribute("authenticatedUser") User user) {
        List<Blog> blogs = adminService.getAllBlogs(user.getId());
        return ResponseEntity.ok(blogs);
    }

    @PostMapping("/blogs")
    @Operation(
            summary = "Create a Blog",
            description = "This endpoint creates a Blog with a reference to user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Blog.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Blog> createBlog(
            @Parameter(name = "Blog details", required = true) @Valid @RequestBody BlogDto blogDto,
            @Parameter(name = "Authenticated user", required = true) @RequestAttribute("authenticatedUser") User user) {
        Blog blog = adminService.createBlog(blogDto, user.getId() + 6);
        return ResponseEntity.ok(blog);
    }

    @Operation(
            summary = "Edit a Blog",
            description = "Edit a Blog through the authentication user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Blog.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"string\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    )
            }
    )
    @PutMapping("/blogs/{blogId}")
    public ResponseEntity<Blog> editBlog(@PathVariable Long blogId, @Valid @RequestBody BlogDto blogDto, @RequestAttribute("authenticatedUser") User user) {
        Blog blog = adminService.editBlog(blogId, user.getId(), blogDto);
        return ResponseEntity.ok(blog);
    }

    @Operation(
            summary = "Delete a Blog",
            description = "An author will be able to delete their own blog and when delete it, the reference post or posts to that blog will also be eliminated.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
                    )
            }
    )
    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<MessageResponse> deleteBlog(@PathVariable Long blogId, @RequestAttribute("authenticatedUser") User user) {
        adminService.deleteBlog(blogId, user.getId());
        return ResponseEntity.ok(new MessageResponse("Blog deleted successfully."));
    }

    // Posts
    @Operation(
            summary = "Get All Post",
            description = "Get all post by authenticated user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/posts-user")
    public ResponseEntity<List<Post>> getAllPostsByUser(@RequestAttribute("authenticatedUser") User user) {
        List<Post> posts = adminService.getAllPostsByUser(user.getId());
        return ResponseEntity.ok(posts);
    }

    @Operation(
            summary = "Get Content Post",
            description = "This endpoint is when an authenticated user wants to see the content post of someone.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    @GetMapping("/posts/{postId}")
    public ResponseEntity<Post> getContentPost(@PathVariable Long postId) {
        Post post = adminService.getContentPost(postId);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Create a Post",
            description = "An authenticated user will be able to create a post into their created blog.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"string\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/posts/{blogId}")
    public ResponseEntity<Post> createPost(@PathVariable Long blogId, @Valid @RequestBody PostDto postDto, @RequestAttribute("authenticatedUser") User user) {
        Post post = adminService.createPost(blogId, postDto, user.getId());
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Edit a Post",
            description = "An authenticated user will be able to edit a post.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"message\": \"string\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MessageResponse.class)
                            )
                    )
            }
    )
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable Long postId, @Valid @RequestBody PostDto postDto, @RequestAttribute("authenticatedUser") User user) {
        Post post = adminService.editPost(postId, postDto, user.getId());
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Delete a Post",
            description = "An author will be able to delete their own Post.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))
                    )
            }
    )
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseDto> deletePost(@PathVariable Long postId, @RequestAttribute("authenticatedUser") User user) {
        adminService.deletePost(postId, user.getId());
        return ResponseEntity.ok(new ResponseDto("Post deleted successfully."));
    }
}
