package com.cisco.microservices.users;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.hamcrest.core.IsNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author roshankumarm
 * 
 * UsersController Test cases.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
@WebAppConfiguration
public class UsersControllerTest {

	private MockMvc mockMvc;

	private MediaType contentType = new MediaType(
			MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters(HttpMessageConverter<?>[] converters) {

		this.mappingJackson2HttpMessageConverter = Arrays
				.asList(converters)
				.stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
				.findAny().get();

		Assert.assertNotNull("the JSON message converter must not be null",
				this.mappingJackson2HttpMessageConverter);
	}

	/**
	 * Configure set up which will run before each test.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@Before
	public void setUp() throws IOException, Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
		User user = new User();
		user.setUserName("junituser");
		user.setUserType("User");
		mockMvc.perform(delete("/user/delete/junituser").contentType(
				MediaType.ALL));

		user.setUserName("mockuser");
		user.setUserType("User");
		mockMvc.perform(
				post("/user/addUser").content(this.json(user)).contentType(
						contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(
						jsonPath("$.description",
								is("User added successfully!")));

	}

	/**
	 * Configure set up which will run after each test.
	 * 
	 * @throws IOException
	 * @throws Exception
	 */
	@After
	public void tearDown() throws IOException, Exception {
		mockMvc.perform(
				delete("/user/delete/mockuser").contentType(MediaType.ALL))
				.andExpect(status().isOk());
	}

	/**
	 * Simple add user scenario.
	 * 
	 * @throws Exception
	 */
	@Test
	public void addUser() throws Exception {
		User user = new User();
		user.setUserName("junituser");
		user.setUserType("User");

		mockMvc.perform(
				post("/user/addUser").content(this.json(user)).contentType(
						contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(
						jsonPath("$.description",
								is("User added successfully!")));

	}

	/**
	 * Status 400 should be sent if same user creation request sent.
	 * 
	 * @throws Exception
	 */
	@Test
	public void userAddAlreadyExist() throws Exception {
		User user = new User();
		user.setUserName("junituser");
		user.setUserType("User");
		mockMvc.perform(post("/user/addUser").content(this.json(user))
				.contentType(contentType));
		// user is added. Now try again adding same user and type.
		mockMvc.perform(
				post("/user/addUser").content(this.json(user)).contentType(
						contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.status", is("400")))
				.andExpect(jsonPath("$.description", is("User already exist!")));

	}

	/**
	 * Status 200 as there is atleast one user present as we have inserted a
	 * user in set up method.
	 * 
	 * @throws Exception
	 */
	@Test
	public void getListUsers() throws Exception {
		mockMvc.perform(get("/user/list").contentType(contentType))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.status", is("200")))
				.andExpect(
						jsonPath("$.description", is("This is the user's list")))
				.andExpect(jsonPath("$.error").value(IsNull.nullValue()));

	}

	/**
	 * Status 400 should be sent if same wrong user name deletion request sent.
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteUserFailed() throws Exception {
		mockMvc.perform(
				delete("/user/delete/mockusernotpresent").contentType(
						MediaType.ALL)).andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("400")));

	}

	/**
	 * Status 200 should be sent if same correct user name deletion request
	 * sent.
	 * 
	 * @throws Exception
	 */
	@Test
	public void deleteUserPassed() throws Exception {
		mockMvc.perform(
				delete("/user/delete/mockuser").contentType(MediaType.ALL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("200")));

	}

	protected String json(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write(o,
				MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}

}
