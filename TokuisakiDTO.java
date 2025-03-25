package com.smalog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokuisakiDTO {
    private String tokuisakiCode;	// 得意先コード
    private String tokuisakiNameKana;	// 得意先カナ名
    private String tokuisakiName;	// 得意先名
    private String kashoCode;	// 箇所コード
    private String kashoName;	// 箇所名
    private String kaishaCode;	// 会社コード
    private String jouiGrCode;	// 上位ＧＲコード
    private String yuubinNumber;	// 郵便番号
    private String juusho1;	// 住所１
    private String juusho2;	// 住所２
    private String telNumber;	// 電話番号
    private String faxNumber;	// ＦＡＸ番号
    private String kaishaKubunCode;	// 会社区分コード

}
