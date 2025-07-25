package com.hotelaria.sistema_hotelaria_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration 
public class RequestLoggingFilterConfig {

    @Bean 
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true); 
        filter.setIncludeQueryString(true); 
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000); 
        filter.setIncludeHeaders(false); 
        filter.setAfterMessagePrefix("Requisição Recebida: [");
        filter.setBeforeMessagePrefix("Iniciando Requisição: ["); 
        return filter;
    }
}