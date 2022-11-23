package demos.springdata.restdemo.unit;

import demos.springdata.restdemo.exception.EntityNotFoundException;
import demos.springdata.restdemo.exception.InvalidEntityException;
import demos.springdata.restdemo.model.Post;
import demos.springdata.restdemo.model.User;
import demos.springdata.restdemo.repository.PostRepository;
import demos.springdata.restdemo.repository.UserRepository;
import demos.springdata.restdemo.service.PostService;
import demos.springdata.restdemo.service.impl.PostServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository mockedPostRepository;
    @Mock
    private UserRepository mockedUserRepository;


    private PostService postService;
    private ApplicationEventPublisher applicationEventPublisher;
    private User testUser;
    private User testUser2;
    private Post testPost;
    private Post testPost2;
    private long unExistingId = 123456789L;

    @BeforeEach
    public void init() {
        postService = new PostServiceImpl(mockedPostRepository, mockedUserRepository, applicationEventPublisher);

        testUser = new User();
        testUser.setId(123L);
        testUser.setUsername("Random user");
        testUser.setPassword("1234");
        mockedUserRepository.save(testUser);

        testUser2 = new User();
        testUser2.setId(333L);
        mockedUserRepository.save(testUser2);



        testPost = new Post();
        testPost.setAuthor(testUser);
        testPost.setTitle("Test Post");
        testPost.setImageUrl("http://test.com");
        testPost.setId(666L);
        testPost.setContent("Test content");
        testPost.setCreated(Date.from(Instant.now()));
        mockedPostRepository.save(testPost);

        testPost2 = new Post();
        testPost.setId(666666L);
        testPost2.setAuthor(testUser);
        mockedPostRepository.save(testPost2);

    }

    @Test
    public void testGetPostShouldReturnPost() {
        Mockito.when(mockedPostRepository.findAll()).thenReturn(List.of(testPost));

        List<Post> actual = (List<Post>) postService.getPosts();

        Assertions.assertEquals(testPost.getId(), actual.get(0).getId());
        Assertions.assertEquals(testPost.getTitle(), actual.get(0).getTitle());
        Assertions.assertEquals(testPost.getContent(), actual.get(0).getContent());
        Assertions.assertEquals(testPost.getAuthor(), actual.get(0).getAuthor());
        Assertions.assertEquals(testPost.getImageUrl(), actual.get(0).getImageUrl());
    }

    @Test
    public void getPostByIdShouldThrowException() throws EntityNotFoundException {
        Mockito.when(mockedPostRepository.findById(unExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.getPostById(unExistingId));
    }

    @Test
    public void getPostByIdShouldReturnPost(){
        Mockito.when(mockedPostRepository.findById(testPost.getId())).thenReturn(Optional.ofNullable(testPost));

        Post actual = postService.getPostById(testPost.getId());

        Assertions.assertEquals(testPost.getId(), actual.getId());
        Assertions.assertEquals(testPost.getTitle(), actual.getTitle());
        Assertions.assertEquals(testPost.getContent(), actual.getContent());
        Assertions.assertEquals(testPost.getAuthor(), actual.getAuthor());
        Assertions.assertEquals(testPost.getImageUrl(), actual.getImageUrl());
    }

    @Test
    public void deletePostShouldThrowEntityNotFoundException() throws EntityNotFoundException{
        Mockito.when(mockedPostRepository.findById(unExistingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> postService.deletePost(unExistingId));
    }

}
