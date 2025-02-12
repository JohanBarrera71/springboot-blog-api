package com.app.features.admin.model;

import com.app.features.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String content;
    private int duration;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToMany
    @JoinTable(name = "post_likes", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<User> likes;

    @ManyToMany
    @JoinTable(name = "post_dislikes", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<User> dislikes;

    @ManyToMany
    @JoinTable(name = "post_labels", joinColumns = @JoinColumn(name = "id_post"), inverseJoinColumns = @JoinColumn(name = "id_label"))
    private List<Label> labels;

    @UpdateTimestamp
    @Column(name = "publish_date")
    private LocalDateTime publishDate;
}
