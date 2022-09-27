package com.winnie.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winnie.demo.dao.TransactionDao;
import com.winnie.demo.model.DAOTransaction;
import com.winnie.demo.model.DAOUser;
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
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TransactionDao transactionDao;

    @Test
    public void postAuthenticateWithValidResponse() throws Exception {
        // given
        DAOUser jwtReq = new DAOUser("winnie", "password");

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(jwtReq)));
        // then
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getValidResponse() throws Exception {
        // given
        DAOTransaction transaction = DAOTransaction.builder().id("89d3o179-abcd-465b-o9ee-e2d5f6ofEld48").currency("CHF")
                .iban("CH93-0000-0000-0000-0000-0").date("20210110").description("Online payment CHF")
                .amount(new BigDecimal(75)).build();

        // when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/transaction/create")
                .header("Authorization", "Bearer " + getAuthenticateToken())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(transaction)));

        // then
        response.andExpect(MockMvcResultMatchers.status().isOk());

        // delete testing data
        transactionDao.delete(transaction);
    }

    //     private helper
    private String getAuthenticateToken() throws Exception {
        DAOUser jwtReq = new DAOUser("winnie", "password");

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(jwtReq)));
        MockHttpServletResponse result = response.andReturn().getResponse();

        return new JSONObject(result.getContentAsString()).get("jwttoken").toString();
    }

}
