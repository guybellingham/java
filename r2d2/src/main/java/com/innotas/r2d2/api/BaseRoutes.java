package com.innotas.r2d2.api;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseRoutes {
    @Autowired
    protected HttpServletRequest httpRequest;
}
