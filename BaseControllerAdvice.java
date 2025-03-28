package com.smalog.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.smalog.constant.ApplicationConstants;
import com.smalog.model.LoginSession;
import com.smalog.model.Menu;
import com.smalog.util.ApplicationUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 全てのControllerで共通して使用する値を返すクラス
 */
@ControllerAdvice
public class BaseControllerAdvice implements ApplicationConstants{

    @Autowired
    private LoginSession loginSession;
    private static final Logger logger = LoggerFactory.getLogger(BaseControllerAdvice.class);

    /* 常に共有して返す値 */
    /**
     * @return ログイン 得意先コード
     */
    @ModelAttribute("loginTokuisakiCode")
    public String loginTokuisakiCode() {
        return loginSession.getTokuisakiCode();
    }

    /**
     * @return ログイン 得意先名
     */
    @ModelAttribute("loginTokuisakiName")
    public String loginTokuisakiName() {
        return loginSession.getTokuisakiName();
    }

    /**
     * @return ログイン 担当者名
     */
    @ModelAttribute("loginTantoushaName")
    public String loginTantoushaName() {
        return loginSession.getTantoushaName();
    }

    /**
     * @return メニュー一覧
     */
    @ModelAttribute("menuList")
    public List<Menu> menuList() {
        return loginSession.getMenuList();
    }

    /**
     * エラーページに遷移するか、JSONを返す
     * @param ex 例外
     * @param model Modelオブジェクト
     * @param request HttpServletRequest
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex, Model model, HttpServletRequest request) {
        logger.error("エラーが発生しました", ex);
        // AJAXリクエストかどうかを判定
        if (ApplicationUtils.isRequestAjax(request)) {
            // AJAXならJSONレスポンスを返す
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", ERROR_STAUS);
            errorResponse.put("message", ex.getMessage());
            errorResponse.put("error", ERROR_STAUS.getReasonPhrase());
            
            return ResponseEntity.status(ERROR_STAUS).body(errorResponse);
        }

        // 通常リクエストならエラーページに遷移
        model.addAttribute("status", ERROR_STAUS);
		model.addAttribute("message", ex.getMessage());
        model.addAttribute("error", ERROR_STAUS.getReasonPhrase());
        return "error";
    }
}
