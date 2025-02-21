package com.app.features.admin.controller;

import com.app.exceptions.dto.MessageResponse;
import com.app.features.admin.dto.CommentDto;
import com.app.features.admin.model.Comment;
import com.app.features.admin.model.Post;
import com.app.features.admin.service.AdminService;
import com.app.features.admin.service.FeedService;
import com.app.features.auth.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
@Tag(name = "Feed", description = "Controller for Feed of Posts where all users will be able to see, comment on posts.")
public class FeedController {

    private final FeedService feedService;
    private final AdminService adminService;

    // ######### POSTS #########
    @Operation(
            summary = "Get All Posts",
            description = "An authenticated user will be able to see any post of any user.",
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
                    )
            }
    )
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = adminService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(
            summary = "Like a Post",
            description = "An authenticated user will be able to like any post.",
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
    @PutMapping("/posts/{postId}/like")
    public ResponseEntity<Post> likePost(@PathVariable Long postId, @RequestAttribute("authenticatedUser") User user) {
        Post post = feedService.ratePost(postId, user.getId(), true);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Dislike a Post",
            description = "An authenticated user will be able to dislike any post.",
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
    @PutMapping("/posts/{postId}/dislike")
    public ResponseEntity<Post> dislikePost(@PathVariable Long postId, @RequestAttribute("authenticatedUser") User user) {
        Post post = feedService.ratePost(postId, user.getId(), false);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Add Comment on Post",
            description = "An authenticated user will be able to add a comment or comments on any post.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))
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
    @PostMapping("/posts/{postId}/comment")
    public ResponseEntity<Comment> addComment(@PathVariable Long postId, @RequestBody CommentDto commentDto, @RequestAttribute("authenticatedUser") User user) {
        Comment comment = feedService.addComment(postId, commentDto, user.getId());
        return ResponseEntity.ok(comment);
    }
}
