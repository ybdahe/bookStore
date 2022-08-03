package com.smartdubai.service;

import com.smartdubai.model.PromoCode;
import com.smartdubai.repository.PromoCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PromoCodeService {
    @Autowired
    PromoCodeRepository promoCodeRepository;

    public List<PromoCode> getAllPromoCodes() {
        List<PromoCode> promoCodes = new ArrayList<>();
        promoCodeRepository.findAll().forEach(promoCodes::add);
        return promoCodes;
    }

    public Optional<PromoCode> getPromoCodeById(int id) {
        try {
            Optional<PromoCode> promoCode = promoCodeRepository.findById(id);
            if(promoCode.isPresent()){
                return promoCode;
            }else {
                return Optional.ofNullable(null);
            }
        }catch (Exception e){
            return Optional.ofNullable(null);
        }
    }

    public Optional<PromoCode> getPromoCodeByCodeName(String codeName) {
        try {
            return Optional.of(promoCodeRepository.findByCodeName(codeName));
        }catch (Exception e){
            return Optional.ofNullable(null);
        }
    }

    public PromoCode saveOrUpdate(PromoCode promoCode) {
        return promoCodeRepository.save(promoCode);
    }

    public void delete(int id) {
        promoCodeRepository.deleteById(id);
    }

}