package com.smartdubai.service;

import com.smartdubai.model.PromoCode;
import com.smartdubai.repository.PromoCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class PromoCodeServiceTest {

    @Mock
    private PromoCodeRepository promoCodeRepository;


    @InjectMocks
    PromoCodeService promoCodeService;


    @Test
    void getPromoCodeByID() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCodeId(1);
        promoCode.setCodeName("Code 1");
        when(promoCodeRepository.findById(promoCode.getCodeId())).thenReturn(Optional.of(promoCode));
        assertThat(promoCodeService.getPromoCodeById(promoCode.getCodeId()).get().getCodeId()).isEqualTo(promoCode.getCodeId());
    }

    @Test
    void getAllBooks() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCodeId(1);
        promoCode.setCodeName("Code 1");
        List<PromoCode> promoCodes = new ArrayList<>();
        promoCodes.add(promoCode);
        when(promoCodeRepository.findAll()).thenReturn(promoCodes);
        assertThat(promoCodeService.getAllPromoCodes().stream().findAny().get().getCodeId()).isEqualTo(promoCode.getCodeId());
    }

    @Test
    void addOrUpdateBook() {
        PromoCode promoCode = new PromoCode();
        promoCode.setCodeId(1);
        promoCode.setCodeName("Code 1");
        when(promoCodeRepository.save(promoCode)).thenReturn(promoCode);
        assertThat(promoCodeService.saveOrUpdate(promoCode).getCodeId()).isEqualTo(promoCode.getCodeId());
    }
}