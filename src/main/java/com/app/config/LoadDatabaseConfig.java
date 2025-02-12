package com.app.config;

import com.app.features.auth.model.Role;
import com.app.features.auth.model.User;
import com.app.features.auth.repository.UserRepository;
import com.app.features.admin.model.Blog;
import com.app.features.admin.model.Label;
import com.app.features.admin.model.Post;
import com.app.features.admin.repository.BlogRepository;
import com.app.features.admin.repository.LabelRepository;
import com.app.features.admin.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class LoadDatabaseConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, BlogRepository blogRepository, PostRepository postRepository, LabelRepository labelRepository) {
        return args -> {
            List<User> users = createUsers(userRepository);
            List<Label> labels = createLabels(labelRepository);
            createBlog(blogRepository, postRepository, users, labels);
        };
    }

    private void createBlog(BlogRepository blogRepository, PostRepository postRepository, List<User> users, List<Label> labels) {
        // Crear blogs y asociar usuarios
        Blog blog1 = new Blog();
        blog1.setTitle("Explorando el Universo");
        blog1.setDescription("Artículos sobre astronomía y exploración espacial");
        blog1.setAuthor(users.get(0)); // Asignar autor (Johan)
        blog1.setPosts(new ArrayList<>());
        blogRepository.save(blog1);

        Blog blog2 = new Blog();
        blog2.setTitle("Innovaciones Tecnológicas");
        blog2.setDescription("Últimas tendencias en tecnología y ciencia");
        blog2.setAuthor(users.get(1)); // Asignar autor (Ana)
        blog2.setPosts(new ArrayList<>());
        blogRepository.save(blog2);

        // Lista de blogs creada
        List<Blog> blogs = Arrays.asList(blog1, blog2);

        // Llamar a createPosts para añadir publicaciones
        createPosts(postRepository, users, labels, blogs);
    }

    private void createPosts(PostRepository postRepository, List<User> users, List<Label> labels, List<Blog> blogs) {
        // Crear publicaciones y asociar etiquetas
        Post post1 = new Post();
        post1.setTitle("La Teoría del Big Bang");
        post1.setContent("Una explicación detallada sobre la teoría del Big Bang.");
        post1.setBlog(blogs.get(0));
        post1.setAuthor(users.get(0)); // Asignar autor (Johan)
        post1.setLabels(Arrays.asList(labels.get(0), labels.get(2))); // Asociar etiquetas (Ciencia, Historia)
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Inteligencia Artificial en la Medicina");
        post2.setContent("Cómo la IA está revolucionando la atención médica.");
        post2.setBlog(blogs.get(1));
        post2.setAuthor(users.get(1)); // Asignar autor (Ana)
        post2.setLabels(Arrays.asList(labels.get(1))); // Asociar etiquetas (Tecnología)
        postRepository.save(post2);
    }

    private List<Label> createLabels(LabelRepository labelRepository) {
        List<Label> labels = List.of(
                createLabel("Ciencia"),
                createLabel("Tecnologia"),
                createLabel("Historia")
        );
        labelRepository.saveAll(labels);
        return labels;
    }

    private Label createLabel(String name) {
        Label label = new Label();
        label.setName(name);
        return label;
    }

    private List<User> createUsers(UserRepository userRepository) {
        List<User> users = List.of(
                createUser("johan@gmail.com", "johan", "Johan", "Lopez", "Mexico",
                        "https://images.unsplash.com/photo-1633332755192-727a05c4013d?q=80&w=3560&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "Men", "Software Enginer", "Apasionado en la redaccion de articulos"),
                createUser("ana@example.com", "ana123", "Ana", "Garcia", "Mexico",
                        "https://images.unsplash.com/photo-1599577167220-e7e73ff79e6d?q=80&w=3560&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "Women", "Graphic Designer", "Enamorada del arte visual"),
                createUser("luis@example.com", "luis", "Luis", "Martinez", "Mexico",
                        "https://images.unsplash.com/photo-1603415526960-2182283f0f04?q=80&w=3560&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "Men", "Data Scientist", "Fascinado por los datos y la analítica"),
                createUser("maria@example.com", "maria89", "Maria", "Hernandez", "Mexico",
                        "https://images.unsplash.com/photo-1589215468060-90f98e32dce9?q=80&w=3560&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                        "Women", "Content Creator", "Amante de la creación de contenido digital")
        );

        userRepository.saveAll(users);
        return users;
    }

    private User createUser(String username, String password, String firstname, String lastname, String country, String imageProfile, String gender, String occupation, String descriptionProfile) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setCountry(country);
        user.setImageProfile(imageProfile);
        user.setRole(Role.USER);
        user.setGender(gender);
        user.setOccupation(occupation);
        user.setDescriptionProfile(descriptionProfile);
        return user;
    }
}
