package com.sqx.modules.utils.translate;

import lombok.Data;

import java.io.Serializable;

@Data
public class TransResult implements Serializable {

    private static final long serialVersionUID = 1L;
    //翻译后文本
    private String dst;
    //源文本
    private String src;
}
