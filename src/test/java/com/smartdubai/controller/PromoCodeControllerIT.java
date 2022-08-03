package com.smartdubai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartdubai.model.PromoCode;
import com.smartdubai.model.PromoCodeDTO;
import com.smartdubai.service.PromoCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PromoCodeController.class)
class PromoCodeControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PromoCodeService promoCodeService;


    @Test
    void getAllPromoCodesWithNoContent() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("promoCodeName", "xyz");
        mockMvc.perform(get("/api/promoCode").params(paramsMap))
                .andExpect(status().isNoContent());
    }

    @Test
    void createPromoCode() throws Exception {

        PromoCodeDTO promoCodeDTO = new PromoCodeDTO();
        promoCodeDTO.setCodeName("PromoCode 1");
        mockMvc.perform(post("/api/promoCodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(promoCodeDTO)))
                .andExpect(status().isCreated());

    }

    @Test
    void getAllPromoCodes()
            throws Exception {

        PromoCode promoCode = new PromoCode();
        promoCode.setCodeName("PromoCode 1");

        List<PromoCode> allPromoCodes = Collections.singletonList(promoCode);

        given(promoCodeService
                .getAllPromoCodes())
                .willReturn(allPromoCodes);

        mockMvc.perform(get("/api/promoCode")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test()
    void updatePromoCodes() throws Exception {

        PromoCodeDTO promoCodeData = new PromoCodeDTO();
        promoCodeData.setCodeId(1);
        promoCodeData.setCodeName("PromoCode 1");

        PromoCode promoCode = new PromoCode(promoCodeData.getCodeId(), promoCodeData.getCodeName());
        given(promoCodeService.getPromoCodeById
                (promoCode.getCodeId()))
                .willReturn(Optional.of(promoCode));

        given(promoCodeService
                .saveOrUpdate(promoCode))
                .willReturn(promoCode);

        ObjectMapper mapper = new ObjectMapper();

        mockMvc.perform(put("/api/promoCodes")
                        .content(mapper.writeValueAsString(promoCodeData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deletePromoCodes() throws Exception {
        PromoCodeDTO promoCodeData = new PromoCodeDTO();
        promoCodeData.setCodeId(1);
        promoCodeData.setCodeName("PromoCode 1");

        PromoCode promoCode = new PromoCode(promoCodeData.getCodeId(), promoCodeData.getCodeName());
        given(promoCodeService.getPromoCodeById
                (promoCode.getCodeId()))
                .willReturn(Optional.of(promoCode));
        doNothing().when(promoCodeService).delete(1);
        mockMvc.perform(delete("/api/promoCode/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}