package com.smalog.constant;

import lombok.Getter;

/**
 * DBテーブルの区分などの定数をラベルと値のマッピングで定義する
 */
@Getter
public enum DBKeyLabelConstants {
    /* フラグ */
    INT_FLAG_ON("ON",1),
    INT_FLAG_OFF("OFF",0),
    STRING_FLAG_ON("ON","1"),
    STRING_FLAG_OFF("OFF","0"),
    /* 会社区分コード */
    KAISHA_KUBUN_CODE_HANBAITEN("販売店","1"),
    KAISHA_KUBUN_CODE_MAKER("メーカー","2"),
    KAISHA_KUBUN_CODE_INSATSU_GAISHA("印刷会社", "6"),
    KAISHA_KUBUN_CODE_SUNRICH("サンリッチ","9"),

    // ここに追加
    ;

    private final String label;
    private final Object value;

    private DBKeyLabelConstants(final String label, final Object value) {
        this.label = label;
        this.value = value;
    }
    
    public String getStringValue() {return this.value.toString();}
    public int getIntValue() {return Integer.parseInt(getStringValue());}
    public boolean getBooleanValue() {return  Boolean.parseBoolean(getStringValue());}
    
}