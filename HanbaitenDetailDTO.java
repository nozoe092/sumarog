package com.smalog.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HanbaitenDetailDTO {
    private int id;	//ID
    private String tokuisakiCode;	//得意先コード
    private Integer shouhinjouhouNyuryokuRule;	//商品情報入力ルール
    private Integer mailJushinFlag;	//メール受信フラグ
    private String tantoushaName1;	//担当者名1
    private String mailAddress1;	//メールアドレス1
    private String tantoushaName2;	//担当者名2
    private String mailAddress2;	//メールアドレス2
    private String tantoushaName3;	//担当者名3
    private String mailAddress3;	//メールアドレス3
    private String tantoushaName4;	//担当者名4
    private String mailAddress4;	//メールアドレス4
    private String tantoushaName5;	//担当者名5
    private String mailAddress5;	//メールアドレス5
    private String tantoushaName6;	//担当者名6
    private String mailAddress6;	//メールアドレス6
    private String tantoushaName7;	//担当者名7
    private String mailAddress7;	//メールアドレス7
    private String tantoushaName8;	//担当者名8
    private String mailAddress8;	//メールアドレス8
    private String tantoushaName9;	//担当者名9
    private String mailAddress9;	//メールアドレス9
    private String tantoushaName10;	//担当者名10
    private String mailAddress10;	//メールアドレス10
    private String createTantoushaNumber;	//作成担当者番号
    private LocalDateTime createDate;	//作成日時
    private String updateTantoushaNumber;	//更新担当者番号
    private LocalDateTime updateDate;	//更新日時


}
