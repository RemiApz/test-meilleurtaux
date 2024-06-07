/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.meilleurtaux.restservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommuneControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Get single result
    @Test
    public void paramZipcodeSingleResult() throws Exception {
        this.mockMvc.perform(get("/commune").param("zipcode", "75008"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Paris"))
                .andExpect(jsonPath("$[0].population").value("2133111"));
    }

    // Get multiple results
    @Test
    public void paramZipcodeMultipleResults() throws Exception {
        ResultActions actions = this.mockMvc.perform(get("/commune").param("zipcode", "54490"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
        for (int i = 0; i < 7; i++) {
            actions = actions.andExpect(jsonPath("$[" + i + "].nom").isString());
            actions = actions.andExpect(jsonPath("$[" + i + "].population").isNumber());
        }
    }

    // Get no results
    @Test
    public void paramZipcodeNoResult() throws Exception {
        this.mockMvc.perform(get("/commune").param("zipcode", "99999"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(status().reason(is("Unable to find any commune with zipcode : 99999")));
    }

    // Invalid Zipcode tests
    @Test
    public void noParamZipcodeBadRequest() throws Exception {
        this.mockMvc.perform(get("/commune")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(status().reason(is("Required parameter 'zipcode' is not present.")));
    }

    @Test
    public void paramZipcodeRegexTextBadRequest() throws Exception {
        this.mockMvc.perform(get("/commune").param("zipcode", "test")).andExpect(status().isBadRequest())
                .andExpect(status().reason(is("test is not a valid zipcode")));
    }

    @Test
    public void paramZipcodeRegexDigitBadRequest() throws Exception {
        this.mockMvc.perform(get("/commune").param("zipcode", "000")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(status().reason(is("000 is not a valid zipcode")));
    }

}
