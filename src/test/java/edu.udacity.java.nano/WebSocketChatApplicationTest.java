package edu.udacity.java.nano;

import edu.udacity.java.nano.controller.LogInController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(WebSocketChatApplication.class)
public class WebSocketChatApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    private final static String TEST_USER = "test_user";

    @InjectMocks
    private LogInController logInController;


    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(logInController).build();
    }
//Load welcome page
    @Test
    public void login() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    //Submit login form, redirects properly
    @Test
    public void userLogin_Test() throws Exception{

            this.mockMvc.perform(post("/login")
            .param("username", TEST_USER))
                    .andExpect(model().attribute("username", TEST_USER))
                    .andExpect(redirectedUrl("/index?username="+TEST_USER))
                    .andExpect(status().isFound());
    }

    //user join - load chat window after login

    @Test
    public void loadChatWindow_Test() throws Exception{
        this.mockMvc.perform(get("/index")
                .param("username", TEST_USER))
                .andExpect(model().attribute("username", TEST_USER))
                .andExpect(view().name("chat"))
                .andExpect(status().isOk());

    }


}
