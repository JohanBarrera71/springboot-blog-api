package com.app.features.admin.service;

import com.app.features.admin.dto.BlogDto;
import com.app.features.admin.dto.LabelDto;
import com.app.features.admin.dto.PostDto;
import com.app.features.admin.model.Blog;
import com.app.features.admin.model.Label;
import com.app.features.admin.model.Post;
import com.app.features.admin.repository.BlogRepository;
import com.app.features.admin.repository.LabelRepository;
import com.app.features.admin.repository.PostRepository;
import com.app.features.auth.model.User;
import com.app.features.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private AdminService adminService;

    // ############# GetAllBlogs() #############
    @Test
    public void testGetAllBlogs() {
        // given
        Long authorId = 1L;
        User author = User.builder().id(authorId).build();
        List<Blog> expectedBlogs = List.of(new Blog(), new Blog());

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.findByAuthorId(authorId)).thenReturn(expectedBlogs);

        List<Blog> actualBlogs = adminService.getAllBlogs(authorId);

        // then
        assertNotNull(actualBlogs);
        assertEquals(2, actualBlogs.size());
        assertEquals(expectedBlogs, actualBlogs);
        verify(blogRepository).findByAuthorId(authorId);
    }

    @Test
    public void testGetAllBlogs_userNotFound() {
        // given
        Long authorId = 1L;

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.getAllBlogs(authorId);
        });
        verify(blogRepository, never()).findByAuthorId(any());
    }

    // ############# CreateBlog() #############
    @Test
    public void testCreateBlog() {
        // given
        Long authorId = 1L;
        User author = User.builder().id(authorId).build();
        BlogDto blogDto = BlogDto.builder()
                .title("Title")
                .description("Description")
                .build();
        Blog expectedBlog = Blog.builder()
                .title(blogDto.getTitle())
                .description(blogDto.getDescription())
                .author(author)
                .build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.save(any(Blog.class))).thenReturn(expectedBlog);

        Blog actualBlog = adminService.createBlog(blogDto, authorId);

        // then
        assertNotNull(actualBlog);
        assertEquals(blogDto.getTitle(), actualBlog.getTitle());
        assertEquals(blogDto.getDescription(), actualBlog.getDescription());
        assertEquals(authorId, actualBlog.getAuthor().getId());
        verify(blogRepository).save(any(Blog.class));
    }

    @Test
    public void testCreateBlog_userNotFound() {
        // given
        Long authorId = 1L;
        User author = User.builder().id(authorId).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        //then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.createBlog(new BlogDto(), authorId);
        });
        verify(blogRepository, never()).save(any());
    }

    // ############# EditBlog() #############
    @Test
    public void testEditBlog_success() {
        // given
        Long blogId = 1L;
        Long authorId = 2L;
        BlogDto blogDto = BlogDto.builder().title("Title").description("Description").build();
        User author = User.builder().id(authorId).build();
        Blog expectedBlog = Blog.builder().title(blogDto.getTitle()).description(blogDto.getDescription()).author(author).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(expectedBlog));
        when(blogRepository.save(any(Blog.class))).thenReturn(expectedBlog);

        Blog actualBlog = adminService.editBlog(blogId, authorId, blogDto);

        // then
        assertNotNull(actualBlog);
        assertEquals(blogDto.getTitle(), actualBlog.getTitle());
        assertEquals(blogDto.getDescription(), actualBlog.getDescription());
        verify(blogRepository).save(any(Blog.class));
    }

    @Test
    public void testEditBlog_isNotAuthorOfBlog() {
        // given
        Long blogId = 1L;
        Long authorId = 2L;
        BlogDto blogDto = new BlogDto();

        User author = User.builder().id(authorId).build();

        User anotherUser = User.builder().id(3L).build();

        Blog blog = Blog.builder().id(blogId).author(anotherUser).build();

        // when
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));


        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.editBlog(blogId, authorId, blogDto);
        });
        verify(blogRepository, never()).save(any());
    }

    @Test
    public void testEditBlog_blogNotFound() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        BlogDto blogDto = new BlogDto();

        // when
        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.editBlog(blogId, authorId, blogDto);
        });
        verify(blogRepository, never()).save(any());
    }

    @Test
    public void testEditBlog_userNotFound() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        BlogDto blogDto = new BlogDto();

        //when
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(new Blog()));
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.editBlog(blogId, authorId, blogDto);
        });
        verify(blogRepository, never()).save(any());
    }

    // ############# DeleteBlog() #############
    @Test
    public void testDeleteBlog_success() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        User author = User.builder().id(authorId).build();
        Blog blog = Blog.builder().id(blogId).author(author).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        adminService.deleteBlog(blogId, authorId);

        // then
        verify(blogRepository).delete(blog);
    }

    @Test
    public void testDeleteBlog_isNotAuthorOfBlog() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        User author = User.builder().id(authorId).build();
        User anotherAuthor = User.builder().id(2L).build();
        Blog blog = Blog.builder().id(blogId).author(anotherAuthor).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteBlog(blogId, authorId);
        });
        verify(blogRepository, never()).delete(any());
    }

    @Test
    public void testDeleteBlog_userNotFound() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteBlog(blogId, authorId);
        });
        verify(blogRepository, never()).delete(any());
    }

    @Test
    public void testDeleteBlog_blogNotFound() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;

        //when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(new User()));
        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteBlog(blogId, authorId);
        });
        verify(blogRepository, never()).delete(any());
    }

    // ############# GetAllPosts() #############
    @Test
    public void testGetAllPost() {
        // given
        List<Post> expectedPost = List.of(new Post(), new Post());

        // when
        when(postRepository.findAllByOrderByPublishDateDesc()).thenReturn(expectedPost);

        List<Post> actualPost = adminService.getAllPosts();

        // then
        assertNotNull(actualPost);
        assertEquals(2, actualPost.size());
        assertEquals(expectedPost, actualPost);
        verify(postRepository).findAllByOrderByPublishDateDesc();
    }

    // ############# GetAllPosts() #############
    @Test
    public void testGetAllPostsByUser_success() {
        // given
        Long authorId = 1L;
        User author = new User();
        author.setId(authorId);
        List<Post> expectedPosts = List.of(new Post(), new Post());

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findByAuthor_IdOrderByPublishDateDesc(authorId)).thenReturn(expectedPosts);

        List<Post> actualPost = adminService.getAllPostsByUser(authorId);

        // then
        assertNotNull(actualPost);
        assertEquals(2, actualPost.size());
        assertEquals(expectedPosts, actualPost);
        verify(postRepository).findByAuthor_IdOrderByPublishDateDesc(authorId);
    }

    @Test
    public void testGetAllPostsByUser_userNotFound() {
        // given
        Long authorId = 1L;

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.getAllPostsByUser(authorId);
        });
        verify(postRepository, never()).findByAuthor_IdOrderByPublishDateDesc(any());
    }

    // ############# GetContentPost() #############
    @Test
    public void testGetContentPost() {
        // given
        Long postId = 1L;
        Post expectedPost = Post.builder().id(postId).build();

        // when
        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPost));

        Post actualPost = adminService.getContentPost(postId);

        // then
        verify(postRepository).findById(postId);
    }

    @Test
    public void testGetContentPost_postNotFound() {
        // given
        Long postId = 1L;

        // when
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        //then
        assertThrows(NoSuchElementException.class, () -> {
            adminService.getContentPost(postId);
        });
    }

    // ############# CreatePost() #############
    @Test
    public void testCratePost_success() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();
        User author = User.builder().id(authorId).build();

        Blog blog = Blog.builder().id(blogId).id(blogId).author(author).build();

        Post expectedPost = Post.builder().title(postDto.getTitle()).content(postDto.getContent()).blog(blog).author(author).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(postRepository.save(any(Post.class))).thenReturn(expectedPost);

        Post actualPost = adminService.createPost(blogId, postDto, authorId);

        // then
        assertNotNull(actualPost);
        assertEquals(postDto.getTitle(), actualPost.getTitle());
        assertEquals(postDto.getContent(), actualPost.getContent());
        assertEquals(blog, actualPost.getBlog());
        assertEquals(author, actualPost.getAuthor());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    public void testCreatePost_isNotAuthorOfBlog() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();
        User author = User.builder().id(authorId).build();
        User anotherUser = User.builder().id(3L).build();

        Blog blog = Blog.builder().id(blogId).id(blogId).author(anotherUser).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.createPost(blogId, postDto, authorId);
        });
        verify(postRepository, never()).save(any());
    }

    @Test
    public void testCreatePost_withLabels() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        LabelDto labelDto2 = LabelDto.builder().name("label 2").build();
        LabelDto labelDto1 = LabelDto.builder().name("label 1").build();
        PostDto postDto = PostDto.builder().title("title").content("content").labels(List.of(labelDto1, labelDto2)).build();

        User author = User.builder().id(authorId).build();

        Blog blog = Blog.builder().id(blogId).author(author).build();

        Label label1 = Label.builder().name(labelDto1.getName()).build();
        Label label2 = Label.builder().name(labelDto2.getName()).build();
        when(labelRepository.save(any(Label.class))).thenReturn(label1, label2);

        Post expectedPost = Post.builder().title(postDto.getTitle()).content(postDto.getContent()).blog(blog).author(author).labels(List.of(label1, label2)).build();

        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));
        when(postRepository.save(any(Post.class))).thenReturn(expectedPost);

        Post actualPost = adminService.createPost(blogId, postDto, authorId);

        // then
        assertNotNull(actualPost);
        assertEquals(2, actualPost.getLabels().size());
        assertEquals(expectedPost.getLabels(), actualPost.getLabels());
        verify(labelRepository, times(2)).save(any(Label.class));
        verify(postRepository).save(any(Post.class));
    }

    @Test
    public void testCreatePost_userNotFound() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.createPost(blogId, postDto, authorId);
        });
        verify(postRepository, never()).save(any());
    }

    @Test
    public void testCreatePost_blogNotFound() {
        // given
        Long blogId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();
        User author = User.builder().id(authorId).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.createPost(blogId, postDto, authorId);
        });
        verify(postRepository, never()).save(any());
    }

    // ############# EditPost() #############
    @Test
    public void testEditPost_success() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();

        User author = User.builder().id(authorId).build();

        Post post = Post.builder().id(postId).author(author).build();

        Post expectedPost = Post.builder()
                .id(postId)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .author(author).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(expectedPost);

        Post actualPost = adminService.editPost(postId, postDto, authorId);

        // then
        assertNotNull(actualPost);
        assertEquals(postDto.getTitle(), actualPost.getTitle());
        assertEquals(postDto.getContent(), actualPost.getContent());
        verify(postRepository).save(post);
    }

    @Test
    public void testEditPost_isNotAuthorOfPost() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();

        User author = User.builder().id(authorId).build();
        User anotherUser = User.builder().id(3L).build();

        Post post = Post.builder().id(postId).author(anotherUser).build();

        Post expectedPost = Post.builder()
                .id(postId)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .author(anotherUser).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));


        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.editPost(postId, postDto, authorId);
        });
        verify(postRepository, never()).save(any());
    }

    @Test
    public void testEditPost_withLabels() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();
        LabelDto labelDto1 = LabelDto.builder().name("Label 1").build();
        LabelDto labelDto2 = LabelDto.builder().name("Label 2").build();
        postDto.setLabels(List.of(labelDto1, labelDto2));

        User author = User.builder().id(authorId).build();

        Post post = Post.builder().id(postId).author(author).labels(List.of()).build();

        Label label1 = Label.builder().name(labelDto1.getName()).build();
        Label label2 = Label.builder().name(labelDto2.getName()).build();
        when(labelRepository.save(any(Label.class))).thenReturn(label1, label2);

        Post expectedPost = Post.builder()
                .id(postId)
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .author(author)
                .labels(List.of(label1, label2)).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenReturn(expectedPost);

        Post actualPost = adminService.editPost(postId, postDto, authorId);

        // then
        assertNotNull(actualPost);
        assertEquals(2, actualPost.getLabels().size());
        assertEquals(expectedPost.getLabels(), actualPost.getLabels());
        verify(labelRepository, times(2)).save(any(Label.class));
        verify(postRepository).save(post);
    }

    @Test
    public void testEditPost_userNotFound() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.editPost(postId, postDto, authorId);
        });
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    public void testEditPost_postNotFound() {
        // given
        Long postId = 1L;
        Long authorId = 1L;
        PostDto postDto = PostDto.builder().title("title").content("content").build();

        User author = User.builder().id(authorId).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.editPost(postId, postDto, authorId);
        });
        verify(postRepository, never()).save(any(Post.class));
    }

    // ############# DeletePost() #############
    @Test
    public void testDeletePost_success(){
        // given
        Long postId = 1L;
        Long authorId = 1L;
        User author = User.builder().id(authorId).build();
        Post post = Post.builder().id(postId).author(author).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        adminService.deletePost(postId, authorId);
        verify(postRepository).delete(post);
    }

    @Test
    public void testDeletePost_isNotAuthorOfPost(){
        // given
        Long postId = 1L;
        Long authorId = 1L;
        User author = User.builder().id(authorId).build();
        User anotherAuthor = User.builder().id(3L).build();
        Post post = Post.builder().id(postId).author(anotherAuthor).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.deletePost(postId, authorId);
        });
        verify(postRepository, never()).delete(any());
    }

    @Test
    public void testDeletePost_userNotFound(){
        // given
        Long postId = 1L;
        Long authorId = 1L;

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.deletePost(postId, authorId);
        });
        verify(postRepository, never()).delete(any());
    }

    @Test
    public void testDeletePost_postNotFound(){
        // given
        Long postId = 1L;
        Long authorId = 1L;
        User author = User.builder().id(authorId).build();

        // when
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.deletePost(postId, authorId);
        });
        verify(postRepository, never()).delete(any());
    }
}