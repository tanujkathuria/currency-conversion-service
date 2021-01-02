package com.example.currencyconversionservice;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConverterController {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private  CurrencyExchangeService currencyExchangeService;

    @GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean  retrieveExchangeValue(@PathVariable String from, @PathVariable String to , @PathVariable BigDecimal quantity){
        Map<String,String> urlVariables = new HashMap<>();
        urlVariables.put("from", from);
        urlVariables.put("to", to);
        ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity("http://localhost:8002/currency-exchange/from/{from}/to/{to}",CurrencyConversionBean.class, urlVariables);
        CurrencyConversionBean response =  responseEntity.getBody();
        return new CurrencyConversionBean(response.getId(),from,to,response.getConversionMultiple(),
                quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());
    }

    @GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversionBean  retrieveExchangeValueFeign(@PathVariable String from, @PathVariable String to , @PathVariable BigDecimal quantity){
        CurrencyConversionBean response = currencyExchangeService.retrieveExchangeValue(from,to);
        logger.info("{}", response);
        return new CurrencyConversionBean(response.getId(),from,to,response.getConversionMultiple(),
                quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());
    }
}
