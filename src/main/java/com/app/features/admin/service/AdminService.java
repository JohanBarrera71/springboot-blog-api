package com.app.features.admin.service;

import com.app.features.admin.dto.BlogDto;
import com.app.features.admin.dto.PostDto;
import com.app.features.admin.model.Blog;
import com.app.features.admin.model.Label;
import com.app.features.admin.model.Post;
import com.app.features.admin.repository.BlogRepository;
import com.app.features.admin.repository.LabelRepository;
import com.app.features.admin.repository.PostRepository;
import com.app.features.auth.model.User;
import com.app.features.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;
    private final LabelRepository labelRepository;

    public List<Blog> getAllBlogs(Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        return blogRepository.findByAuthorId(author.getId());
    }

    public Blog createBlog(BlogDto blogDto, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Blog blog = new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setDescription(blogDto.getDescription());
        blog.setAuthor(author);
        return blogRepository.save(blog);
    }

    public Blog editBlog(Long blogId, Long authorId, BlogDto blogDto) {
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("Blog not found."));
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));

        if (!blog.getAuthor().equals(author)) {
            throw new IllegalArgumentException("User is not the author of the blog.");
        }

        blog.setTitle(blogDto.getTitle());
        blog.setDescription(blogDto.getDescription());

        return blogRepository.save(blog);
    }

    public void deleteBlog(Long blogId, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("Blog not found."));

        if (!blog.getAuthor().equals(author)) {
            throw new IllegalArgumentException("User is not the author the blog.");
        }

        blogRepository.delete(blog);
    }

    // Posts
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByPublishDateDesc();
    }

    public List<Post> getAllPostsByUser(Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        return postRepository.findByAuthor_IdOrderByPublishDateDesc(author.getId());
    }

    public Post getContentPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new NoSuchElementException("Post not found."));
    }

    public Post createPost(Long blogId, PostDto postDto, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Blog blog = blogRepository.findById(blogId).orElseThrow(() -> new IllegalArgumentException("Blog not found."));
        Post post = new Post();

        if (!blog.getAuthor().equals(author)) {
            throw new IllegalArgumentException("User is not the author of either the blog or the post.");
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setBlog(blog);
        post.setAuthor(author);

        if (postDto.getLabels() != null) {
            List<Label> labels = postDto.getLabels().stream()
                    .map(labelDto -> {
                        Label label = Label.builder().name(labelDto.getName()).build();
                        return labelRepository.save(label);
                    })
                    .collect(Collectors.toList());

            post.setLabels(labels);
        }
        return postRepository.save(post);
    }

    public Post editPost(Long postId, PostDto postDto, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found."));

        if (!post.getAuthor().equals(author)) {
            throw new IllegalArgumentException("User is not the author of the post.");
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        if (postDto.getLabels() != null) {
            List<Label> newLabels = postDto.getLabels().stream()
                    .map(labelDto -> {
                        Label label = Label.builder().name(labelDto.getName()).build();
                        return labelRepository.save(label);
                    })
                    .collect(Collectors.toList());

            post.setLabels(newLabels);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId, Long authorId) {
        User author = userRepository.findById(authorId).orElseThrow(() -> new IllegalArgumentException("User not found."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found."));

        if(!post.getAuthor().equals(author)){
            throw new IllegalArgumentException("User is not the author of the post.");
        }

        postRepository.delete(post);
    }
}
