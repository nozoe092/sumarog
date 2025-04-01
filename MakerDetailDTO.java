package com.smalog.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MakerDetailDTO implements MyBatisLogDTOInterface {
    private Integer id;	//ID
    private String tokuisakiCode;	//得意先コード
    private Integer shouhinjouhouNyuryokuRule;	//商品情報入力ルール
    private Integer mailJushinFlag;	//メール受信フラグ
    private String createTantoushaNumber;	//作成担当者番号
    private LocalDateTime createDate;	//作成日時
    private String updateTantoushaNumber;	//更新担当者番号
    private LocalDateTime updateDate;	//更新日時

    @Override
    public String getTableName() {
        return "SL_メーカー詳細";
    }
    @Override
    public String getLogTableName() {
        return "SL_メーカー詳細_logs";
    }
}
