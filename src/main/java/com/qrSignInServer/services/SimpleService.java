package com.qrSignInServer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SimpleService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getData() {
        String sql = "select column_1 from public.test limit 1";
        return jdbcTemplate.queryForObject(sql, String.class);
    }

}
