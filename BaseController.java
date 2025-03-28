package com.smalog.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.smalog.constant.ApplicationConstants;
import com.smalog.model.LoginSession;
import com.smalog.model.ValidationMessage;
import com.smalog.service.MessageService;
import com.smalog.util.ApplicationUtils;

/**
 * 基底コントローラー
 * アノテーションは使用しない
 */
//@Controller
public class BaseController implements ApplicationConstants {

	@Autowired
    private LoginSession loginSession;
	@Autowired
	private MessageService messageService;

	/**
	 * ログインユーザーが販売店かどうか
	 * @return
	 */
	public boolean isLoginUserHanbaiten() {
        return loginSession.isHanbaiten();
    }

	/**
	 * ログインユーザーがメーカーかどうか
	 * @return
	 */
    public boolean isLoginUserMaker() {
        return loginSession.isMaker();
    }

	/**
	 * ログインユーザーが印刷会社かどうか
	 * @return
	 */
    public boolean isLoginUserInsatusGaisha() {
        return loginSession.isInsatusGaisha();
    }

	/**
	 * ログインユーザーがサンリッチかどうか
	 * @return
	 */
    public boolean isLoginUserSunrich() {
        return loginSession.isSunrich();
    }
    
	/**
	 * ログインユーザーの権限が足りていなかった場合のエラーページを返却する
	 * @param model
	 * @return
	 */
	public String getPermissionErrorView(Model model) {
		return getErrorView(model, messageService.getMessage(MESSAGE_PROPERTY_NAME_ERROR_PERMISSION), PERMISSION_ERROR_STAUS);
	}

	/**
	 * エラーページを返却する
	 * @param model
	 * @return
	 */
	public String getErrorView(Model model) {
		return getErrorView(model, messageService.getMessage(MESSAGE_PROPERTY_NAME_ERROR));
	}

	/**
	 * エラーページを返却する
	 * @param model
	 * @param message
	 * @return
	 */
	public String getErrorView(Model model, String message) {
		return getErrorView(model, message, ERROR_STAUS);
	}

	/**
	 * エラーページを返却する
	 * @param model
	 * @param message
	 * @param status
	 * @return
	 */
	private String getErrorView(Model model, String message, HttpStatus status) {
		model.addAttribute("status", status.value());
		model.addAttribute("message", message == null ? messageService.getMessage(MESSAGE_PROPERTY_NAME_ERROR) : message);
		model.addAttribute("error", status.getReasonPhrase());
		return "error";
	}

	/**
	 * BindingResultからエラーメッセージを取得し、Mapに変換して返却する
	 * @param bindingResult	バリデーション結果
	 * @return
	 */
	public Map<String, ValidationMessage> parseBindingResult(BindingResult bindingResult){
		return ApplicationUtils.parseBindingResult(bindingResult);
	}

	/**
	 * REST APIの正常結果を取得する
	 * @return
	 */
	public Map<String, Object> getRestSuccessfulResult(){
		Map<String, Object> result =  new HashMap<>();
		result.put("status", OK_STATUS.value());
		return result;
	}
}
