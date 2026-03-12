package com.murillo.bufonio.util;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class LocationUtil {

    public static URI getLocation(String basePath, Long idResource) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(basePath)
                .path("/{id}")
                .buildAndExpand(idResource)
                .toUri();
    }
}