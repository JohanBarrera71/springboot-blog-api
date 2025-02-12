package com.app.features.admin.model;

import com.app.features.auth.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String url; // TODO: Revisar si realmente es necesario este campo

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @JsonIgnore
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @CreationTimestamp
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
}
