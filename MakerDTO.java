package com.smalog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakerDTO {

    private String hanbaiten;
    private String makerCode;
    private String name;
    private String category;

    public MakerDTO() {}
}