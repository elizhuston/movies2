package mdb.webapp.movieDbApplication;
import com.google.gson.Gson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.condition.AllOf;
import org.hamcrest.Matchers;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Is;
import org.springframework.http.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.jayway.jsonpath.JsonPath;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MdbApp.class })
@WebAppConfiguration
public class MovieTest {
    String PATH = "$.title";
    JsonPath compiledPath = JsonPath.compile(PATH);
    String name = "Pulp Fiction";
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @InjectMocks
    private MdbAppJsonController controller;
    // @Autowired
    // MockServletContext servletContext; // cached
    // @Autowired
    // MockHttpServletRequest request;
    //
    // @Autowired
    // MockHttpServletResponse response;
    //
    // @Autowired
    // ServletWebRequest webRequest;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    } 
    
    @Test
    public void createMovie() throws Exception {
        Movie m = new Movie("Inglourious Basterds", "2009", "drama");
        String json = new Gson().toJson(m);
        mockMvc.perform(post("/api/movie/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isCreated());;
    }
    
//    @Test
//    public void getTest() throws Exception{
//        mockMvc.perform(get("/api/director/quen"))
//        .andExpect(status().isOk())
//        .andExpect(model().attribute(
//        		"personList", 
//        		Matchers.everyItem(AllOf.allOf(
//        				HasPropertyWithValue.hasProperty("name", Is<Person>.is("Quentin Tarantino"))))));
//        (jsonPath("$.name").value("Quentin Tarantino"));
    }
//    @Test
//    public void getMovieByTitle() throws Exception {
//        this.mockMvc.perform(get("api/movie/title/Mary")).andExpect(status().isOk())
//                .andExpect(content().contentType("MediaType.APPLICATION_JSON"));
//    }
