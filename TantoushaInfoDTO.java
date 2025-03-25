package com.smalog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TantoushaInfoDTO {
	private String tokuisakiCode;	// 得意先コード
    private String tokuisakiName;	// 得意先名
    private String tantoushaNumber;	// 担当者番号
    private String tantoushaName;	// 担当者名
    private String password;		// パスワード
    private String publicLevel;		// 公開レベル
    private String kaishaCode;		// 会社コード
    private String kaishaName;		// 会社名
    private String kaishaKubunCode;	// 会社区分コード
    private String periodCondition;	// 期間条件
    private String adminFlag;		// 管理者フラグ
    private String web2000ManagementFlag;	// WEB2000管理フラグ
    private String externalPublicFlag;	// 外部公開フラグ;
    private String sunrichTorihikiKubunCode;	// サンリッチ取引区分コード;
    private String expirationDate;	// 有効期限
    private Integer loginCount;		// ログイン回数
    
}
