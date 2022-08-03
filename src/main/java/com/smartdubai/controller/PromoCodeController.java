package com.smartdubai.controller;

import com.smartdubai.model.PromoCode;
import com.smartdubai.model.PromoCodeDTO;
import com.smartdubai.service.PromoCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "Manage PromoCodes", tags = {"promo codes"})
@RequestMapping("/api")
public class PromoCodeController {

    @Autowired
    PromoCodeService promoCodeService;

    @GetMapping("/promoCode")
    @ApiOperation(value = "API to get all PromoCodes ", response = PromoCode.class)
    public ResponseEntity<List<PromoCode>> getAllPromoCodes() {
        try {

            List<PromoCode> promoCodes = new ArrayList<>(promoCodeService.getAllPromoCodes());

            if (promoCodes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(promoCodes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/promoCode/{PromoCodeid}")
    @ApiOperation(value = "API to get PromoCode by id ", response = PromoCode.class)
    public ResponseEntity<PromoCode> getPromoCodeByID(@PathVariable("PromoCodeid") @NonNull int promoCodeId) {
        Optional<PromoCode> promoCode = promoCodeService.getPromoCodeById(promoCodeId);
        return promoCode.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/promoCode/{promoCodeid}")
    @ApiOperation(value = "API to delete PromoCode by id ", response = HttpStatus.class)
    public ResponseEntity<HttpStatus> deletePromoCode(@PathVariable("promoCodeid") @NonNull int promoCodeId) {
        try {
            Optional<PromoCode> promoCodeData = promoCodeService.getPromoCodeById(promoCodeId);
            if (promoCodeData.isPresent()) {
                promoCodeService.delete(promoCodeId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/promoCodes")
    @ApiOperation(value = "API to add PromoCode ", response = PromoCode.class)
    public ResponseEntity<PromoCode> savePromoCode(@RequestBody PromoCodeDTO promoCodes) {
        try {
            PromoCode promoCode = promoCodeService.saveOrUpdate(new PromoCode(promoCodes.getCodeId(), promoCodes.getCodeName()));
            return new ResponseEntity<>(promoCode, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/promoCodes")
    @ApiOperation(value = "API to update PromoCode ", response = PromoCode.class)
    public ResponseEntity<PromoCode> update(@RequestBody PromoCodeDTO promoCodes) {
        Optional<PromoCode> promoCodeData = promoCodeService.getPromoCodeById(promoCodes.getCodeId());
        if (promoCodeData.isPresent()) {
            return new ResponseEntity<>(promoCodeService.saveOrUpdate(new PromoCode(promoCodes.getCodeId(), promoCodes.getCodeName())), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
