package com.smalog.constant;

import org.springframework.http.HttpStatus;

/**
 * プログラムで使用する定数を定義する
 */
public interface ApplicationConstants {
    // static final String FLG_ON = "1";
    // static final String FLG_OFF = "0";

    public static final String USABLE_KAISHA_KUBUN_CODE_SEPARATOR = ",";

    static final String LOG_LOGIN = "[ログインしました] 得意先コード：{} / 担当者番号：{}";
    static final String LOG_START_FROMAT = "START: {}.{}";
    static final String LOG_END_FROMAT = "END: {}.{}";
    static final String LOG_TOKUISAKI_CODE_KEY = "tokuisakiCode";
    static final String LOG_TANTOUSHA_NUMBER_KEY = "tantoushaNumber";

    // ログインページのURL
    static final String LOGIN_PAGE_URL = "/login";
    // ログアウトページのURL
    static final String LOGOUT_PAGE_URL = "/logout";
    // ログイン後のデフォルトページのURL
    static final String DEFAULT_PAGE_URL = "/dashboard";
    // 画面名のラベルキー
    static final String PAGE_NAME_KEY = "pageName";
    static final String SELECTED_MENU_ID_KEY = "selectedMenuId";

    /* プロパティ */
    static final String PROPERTY_DATE_TIME_FORMAT = "spring.mvc.format.date-time";
    /* メッセージ */
    static final String MESSAGE_PROPERTY_NAME_SAVE_SUCCESS = "message.save.success";    // 保存完了
    static final String MESSAGE_PROPERTY_NAME_ERROR_OPTIMISTICLOCK = "message.error.optimisticlock";    // 排他エラー
    static final String MESSAGE_PROPERTY_NAME_ERROR_SAVE_FAILED = "message.error.save.failed";    // 保存失敗
    static final String MESSAGE_PROPERTY_NAME_ERROR_UPDATE_FAILED = "message.error.update.failed";    // 更新失敗
    static final String MESSAGE_PROPERTY_NAME_ERROR_NOTFOUND = "message.error.notfound";    // データなし

    static final String MESSAGE_PROPERTY_NAME_MAIL_ATUTHCODE_SUCCESS = "message.mail.authcode.success";    // 認証コード送信完了

    /* HTTPステータ */
    // 権限エラー時
    static final HttpStatus PERMISSION_ERROR_HTTP_STAUS = HttpStatus.FORBIDDEN;
    static final String MESSAGE_PROPERTY_NAME_ERROR_PERMISSION = "message.error.permission";
    // ログインセッションが切れた場合
    static final HttpStatus UNAUTHORIZED_HTTP_STATUS = HttpStatus.UNAUTHORIZED;
    static final String MESSAGE_PROPERTY_NAME_ERROR_UNAUTHORIZED = "message.error.unauthorized";
    // 404エラー時
    static final HttpStatus NOT_FOUND_HTTP_STATUS = HttpStatus.NOT_FOUND;
    // バリデーションエラー時のHTTPステータス 主にajaxで使用する
    static final HttpStatus VALIDATION_INVALID_HTTP_STATUS = HttpStatus.BAD_REQUEST;
    // その他のエラー
    static final HttpStatus ERROR_STAUS = HttpStatus.INTERNAL_SERVER_ERROR;
    static final String MESSAGE_PROPERTY_NAME_ERROR = "message.error";
    // 正常時のHTTPステータス 主にajaxで使用する
    static final HttpStatus OK_STATUS = HttpStatus.OK;

    // 暗号化・複合化のキー
    static final String AES_KEY = "0123456789abcdef";
}