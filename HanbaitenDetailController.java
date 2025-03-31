package com.smalog.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.smalog.constant.MenuConstants;
import com.smalog.dto.HanbaitenDetailDTO;
import com.smalog.dto.TokuisakiDTO;
import com.smalog.form.element.TantoushaForm;
import com.smalog.form.hanbaiten.DetailForm;
import com.smalog.model.LoginSession;
import com.smalog.service.HanbaitenDetailService;
import com.smalog.service.MailService;
import com.smalog.service.TokuisakiService;
import com.smalog.util.ApplicationUtils;
import com.smalog.util.MessageUtils;

import jakarta.validation.Valid;


@Controller
public class HanbaitenDetailController extends BaseController {

	private static final String PAGE_NAME = "販売店詳細";
	private static final MenuConstants SELECTED_MENU = MenuConstants.MASTER_HANBAITEN_DETAIL;

	/* Autowired */
    private final TokuisakiService tokuisakiService;
	private final HanbaitenDetailService hanbaitenDetailService;
	private final MailService mailService;
	private final MessageUtils messageUtils;
    private final SpringTemplateEngine templateEngine;
    private final LoginSession loginSession;

    public HanbaitenDetailController(
		TokuisakiService tokuisakiService, 
		HanbaitenDetailService hanbaitenDetailService, 
		MailService mailService, 
		MessageUtils messageUtils, 
		SpringTemplateEngine templateEngine, 
		LoginSession loginSession) {
			this.tokuisakiService = tokuisakiService;
			this.hanbaitenDetailService = hanbaitenDetailService;
			this.mailService = mailService;
			this.messageUtils = messageUtils;
			this.templateEngine = templateEngine;
			this.loginSession = loginSession;
    }
    
	@GetMapping({"/hanbaiten/detail"})
	public String detail(@ModelAttribute DetailForm detailForm, Model model) throws Exception {		
		String tokuisakiCode = null;
		if(!isUseableUser()) {
			// 権限が無い場合
			return getPermissionErrorView(model);
		}

		if(isLoginUserHanbaiten()) {
			tokuisakiCode = loginSession.getTokuisakiCode();
			// 得意先コードをセット
			detailForm.setTokuisakiCode(tokuisakiCode);
		} else {
			// 暗号化して渡ってくる得意先コードを復号化する
			tokuisakiCode = ApplicationUtils.paramDecrypt(detailForm.getTokuisakiCode());
			detailForm.setTokuisakiCode(tokuisakiCode);
		}
	
		// 販売店の情報取得
		TokuisakiDTO tokuisakiDTO = tokuisakiService.findHanbaiten(tokuisakiCode);
		if(tokuisakiDTO == null) {
			// 販売店が存在しない場合
			return getErrorView(model, messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_NOTFOUND));
		}

		// データ取得処理
		HanbaitenDetailDTO hanbaitenDetailDTO = hanbaitenDetailService.findByTokuisakiCode(tokuisakiCode);
		if(hanbaitenDetailDTO == null) {
			// 新規登録モード
		} else {
			// 編集モード
			hanbaitenDetailService.setDtoToHanibaitenDetailForm(detailForm, hanbaitenDetailDTO);
		}

		// デフォルトで1つ入力欄
		// Map<Integer, TantoushaForm> tantoushaFormList = detailForm.getTantoushaFormList();
		// if(tantoushaFormList.size() == 0){
		// 	tantoushaFormList.put(DetailForm.START_TANTOUSHA_LIST_INDEX, new TantoushaForm());
		// }
		
		model.addAttribute("detailForm", detailForm);
		model.addAttribute("lblTargetHanbaitenInfo", tokuisakiCode + "／" + tokuisakiDTO.getTokuisakiName());
		model.addAttribute(PAGE_NAME_KEY, PAGE_NAME);	// 画面名
		model.addAttribute(SELECTED_MENU_ID_KEY, SELECTED_MENU.getId());	// メニューの選択状態
		
		return "hanbaiten/detail";
	}

	/**
	 * 担当者の入力欄を返す
	 * @param param
	 * @return
	 */
	@PostMapping("/hanbaiten/detail/get-tantousha-form")
	@ResponseBody
	public Map<String, Object> getTantoushaForm(@RequestBody Map<String, String> param) {	
		// 結果
		Map<String, Object> result = getRestSuccessfulResult();

		// データを渡す
		Context context = new Context();
		context.setVariable("tantoushaForm", new TantoushaForm());
		context.setVariable("index", Integer.parseInt(param.get("maxIndex")) + 1);
		
		// HTML取得
		String html = templateEngine.process("element/tantousha-form", context);
		
		result.put("html", html);
		return result;
	}

	/**
	 * 認証コード送信
	 * @param detailForm
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/hanbaiten/detail/send-auth-code")
	@ResponseBody
	public Map<String, Object> sendAuthCode(@RequestBody Map<String, String> param) {
		// 結果
		Map<String, Object> result = getRestSuccessfulResult();

		if(!isUseableUser()) {
			// 権限が無い場合
			throw new RuntimeException(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_PERMISSION));
		}

		// String mailAddress = param.get("mailAddress");
		// mailService.sendTextMail(mailAddress, "タイトル", "こんにちは");

		result.put("message", messageUtils.getMessage(MESSAGE_PROPERTY_NAME_MAIL_ATUTHCODE_SUCCESS));
		return result;
	}

	/**
	 * 保存
	 * @param detailForm
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/hanbaiten/detail/save")
	@ResponseBody
	public ResponseEntity<Object> save(@ModelAttribute @Valid DetailForm detailForm, BindingResult bindingResult) {
		// 結果
		Map<String, Object> result = getRestSuccessfulResult();

		if(!isUseableUser()) {
			// 権限が無い場合
			return ResponseEntity.status(PERMISSION_ERROR_HTTP_STAUS).body(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_PERMISSION));
		}

		if (bindingResult.hasErrors()) {
			// エラーがあった場合、400 Bad Request とともにエラーメッセージを返す
			return ResponseEntity.status(VALIDATION_INVALID_HTTP_STATUS).body(parseBindingResult(bindingResult));
		}

		// 保存処理
		try {
			boolean saveTransactionResult = hanbaitenDetailService.saveTransaction(detailForm, loginSession.getTantoushaNumber());
			if(saveTransactionResult){
				result.put("message", messageUtils.getMessage(MESSAGE_PROPERTY_NAME_SAVE_SUCCESS));
			}
		} catch (Exception e) {
			// 保存失敗
			outputExceptionLog(e);
			result.put("status", ERROR_STAUS);
			result.put("message", e.getMessage());
		}

		return ResponseEntity.ok(result);
	}

	/**
	 * ログインユーザーが使える画面か
	 * @return
	 */
	private boolean isUseableUser() {
		boolean result = false;
		if(isLoginUserSunrich() || isLoginUserHanbaiten()) {
			result = true;
		}
		return result;
	}
}
