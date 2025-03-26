package com.example.common.enums;

public enum TypeEnum {
    LOST("失物广场"),
    FOUND("招领广场"),
    ;

    public String type;
    TypeEnum(String type){
        this.type=type;
    }

}
