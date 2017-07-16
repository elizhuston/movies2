package mdb.webapp.movieDbApplication;

import com.google.gson.Gson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
@ContextConfiguration(classes = { Main.class })
@WebAppConfiguration
public class MdbAppJsonControllerTest {

	String PATH = "$.title";

	JsonPath compiledPath = JsonPath.compile(PATH);
	String name = "True Romance";

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
		Movie m = new Movie("Mary", "Today", "Comedy", null, null, null);
		String json = new Gson().toJson(m);
		mockMvc.perform(post("/api/movies/").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(json));

	}

	@Test
	public void getMovieByTitle() throws Exception {
		this.mockMvc.perform(get("/movie/title/True")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));
	}

}
