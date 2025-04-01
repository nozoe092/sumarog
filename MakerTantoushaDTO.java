package com.smalog.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakerTantoushaDTO implements MyBatisLogDTOInterface {
    private Integer id;	//ID
    private String tokuisakiCode;	//得意先コード
    private Integer makerDetailId;	//販売店詳細ID
    private String tantoushaName;	//担当者名
    private String mailAddress; //メールアドレス1
    private String createTantoushaNumber;	//作成担当者番号
    private LocalDateTime createDate;	//作成日時
    private String updateTantoushaNumber;	//更新担当者番号
    private LocalDateTime updateDate;	//更新日時

    @Override
    public String getTableName() {
        return "SL_販売店担当者";
    }
    @Override
    public String getLogTableName() {
        return "SL_販売店担当者_logs";
    }
}
