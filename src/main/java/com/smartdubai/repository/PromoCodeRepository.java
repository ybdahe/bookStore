package com.smartdubai.repository;

import com.smartdubai.model.PromoCode;
import org.springframework.data.repository.CrudRepository;

public interface PromoCodeRepository extends CrudRepository<PromoCode, Integer>
{
    PromoCode findByCodeName(String codeName);
}
