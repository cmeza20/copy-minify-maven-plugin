package com.cmeza.deployer.plugin.minify.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MinifyType {
    css("CSS"),
    js("JavaScript");

    @Getter
    String description;
}
