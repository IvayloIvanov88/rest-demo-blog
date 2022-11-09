package demos.springdata.restdemo;

import demos.springdata.restdemo.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PostControllerTest extends AbstractTest {

    private String uri = "/api/posts";

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    public void testGetPostShouldReturnPost() throws Exception {
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Post[] posts = super.mapFromJson(content, Post[].class);
        assertTrue(posts.length > 0);
    }

    @Test
    public void testAddPostShouldReturnPost() throws Exception {
        Post post = new Post();
        post.setId(3L);
        post.setContent("Spring Rest api");
        post.setTitle("Spring test");
        post.setImageUrl("http://empty.jpg");

        String inputJson = super.mapToJson(post);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        assertEquals(3L, post.getId());
        assertEquals("Spring Rest api", post.getContent());
        assertEquals("Spring test", post.getTitle());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdatePost() throws Exception {
        String uriUpdate = uri + "/2";
        Post post = new Post();
        post.setImageUrl("http://newImg.img");
        String inputJson = super.mapToJson(post);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uriUpdate)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        assertEquals("http://newImg.img", post.getImageUrl());
    }

}