package com.backend.management.controller;

import com.backend.management.exception.InvalidPostDateException;
import com.backend.management.model.Post;
import com.backend.management.model.User;
import com.backend.management.repository.PostRepository;
import com.backend.management.service.PostService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    //1.add post
    @PostMapping(value = "/post")
    public void addPost(@RequestBody Post post, Principal principal) {

        LocalDate postDate = LocalDate.now();

        Post post1 = new Post.Builder()
                .setCategory(post.getCategory())
                .setContent(post.getContent())
                .setPostTitle(post.getPostTitle())
                .setTime(postDate)
                .setTenantId(new User.Builder().setUsername(principal.getName()).build())
                .build();

        postService.add(post1);
    }

    //2.get post by tenant
    @GetMapping(value = "/post")
    public List<Post> listPosts(Principal principal) {
        return postService.postByTenant(principal.getName());
    }

    //3.get post by id
    @GetMapping(value = "/post/{postId}")
    public Post getPost(@PathVariable Long postId, Principal principal) {
        return postService.findByIdAndTenant(postId, principal.getName());
    }

    //4.delete post by id
    @DeleteMapping(value = "/post/{postId}")
    public void deletePost(@PathVariable Long postId, Principal principal) {
        postService.delete(postId, principal.getName());
    }

    //5.get all posts
    @GetMapping(value = "/posts")
    public List<Post> getAllPosts(Principal principal) {
        return postService.allPosts(principal.getName());
    }

}
