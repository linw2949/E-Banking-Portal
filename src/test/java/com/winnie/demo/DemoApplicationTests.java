package com.winnie.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winnie.demo.dao.MockDAO;
import com.winnie.model.JwtReq;
import com.winnie.model.Transaction;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockDAO mockDAO;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postAuthenticateWithValidResponse() throws Exception {
        JwtReq jwtReq = new JwtReq("winnie", "password");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(jwtReq)));
        response.andExpect(MockMvcResultMatchers.status().isOk());
        MockHttpServletResponse result = response.andReturn().getResponse();
        String token = new JSONObject(result.getContentAsString()).get("jwttoken").toString();
        System.out.println(token);
    }

    @Test
    public void getValidResponse() throws Exception {
        Transaction transaction = Transaction.builder().id("89d3o179-abcd-465b-o9ee-e2d5f6ofEld47").currency("CHF")
                .iban("CH93-0000-0000-0000-0000-0").date("20210110").description("Online payment CHF")
                .amount(new BigDecimal(75)).build();

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/create")
                .header("Authorization","Bearer "+getAuthenticateToken())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(transaction)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    // private helper
    private String getAuthenticateToken() throws Exception {
        JwtReq jwtReq = new JwtReq("winnie", "password");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(jwtReq)));
        MockHttpServletResponse result = response.andReturn().getResponse();
        return new JSONObject(result.getContentAsString()).get("jwttoken").toString();
    }

}
