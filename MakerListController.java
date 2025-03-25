package com.smalog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smalog.constant.MenuConstants;
import com.smalog.dto.TokuisakiDTO;

import com.smalog.form.maker.ListForm;
import com.smalog.service.TokuisakiService;
import com.smalog.service.HanbaitenDetailService;
import com.smalog.service.MessageService;
import com.smalog.util.ApplicationUtils;

import jakarta.validation.Valid;

@Controller
public class MakerListController extends BaseController {

	private static final String PAGE_NAME = "メーカ一覧";
	private static final MenuConstants SELECTED_MENU = MenuConstants.MASTER_MAKER_LIST;

	@Autowired
    private TokuisakiService tokuisakiService;


	@Autowired
	private HanbaitenDetailService hanbaitenDetailService;

	@Autowired
    private MessageService messageService;
    
	@GetMapping({"/maker/list"})
	public String list(@ModelAttribute ListForm listForm, Model model) throws Exception {
		if(!isLoginUserSunrich()) {
			// 権限が無い場合
			return getPermissionErrorView(model);
		}
		model.addAttribute(PAGE_NAME_KEY, PAGE_NAME);	// 画面名
		model.addAttribute(SELECTED_MENU_ID_KEY, SELECTED_MENU.getId());	// メニューの選択状態
		model.addAttribute("listForm", listForm);
		return "maker/list";
	}

	//  /**
    //  * メーカー一覧の初期表示
    //  */
    // @GetMapping
    // public String showMakerList(Model model, @RequestParam(value = "tokuisakiCode", required = false) String tokuisakiCode) {
    //     // ① 販売店データを取得（プルダウン用）
    //     List<TokuisakiDTO> hanbaitenList = tokuisakiService.findHanbaitenList(tokuisakiCode);
    //     model.addAttribute("hanbaitenList", hanbaitenList);

    //     // ② 検索条件を設定
    //     model.addAttribute("listForm", new MakerSearchForm());

    //     return "maker/maker-list";
    // }

	/**
	 * メーカ一覧のデータを取得する
	 * @param listForm
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/maker/list/get-data")
	@ResponseBody
	public ResponseEntity<Object> getData(@ModelAttribute @Valid ListForm listForm, BindingResult bindingResult) {
		// 結果
		Map<String, Object> result = getRestSuccessfulResult();

		if(!isLoginUserSunrich()) {
			// 権限が無い場合
			return ResponseEntity.status(PERMISSION_ERROR_STAUS).body(messageService.getMessage(MESSAGE_PROPERTY_NAME_ERROR_PERMISSION));
		}

		List<Map<String, String>> dataList = new ArrayList<>();
		if (bindingResult.hasErrors()) {
			// エラーがあった場合、400 Bad Request とともにエラーメッセージを返す
			return ResponseEntity.status(VALIDATION_INVALID_HTTP_STATUS).body(parseBindingResult(bindingResult));
		}

 // `findMaker` で取得したメーカー情報をリストに変換（得意先コード & 得意先名のみ）
 TokuisakiDTO maker = tokuisakiService.findMaker(listForm.getTokuisakiCode());
 if (maker != null) {
	 Map<String, String> data = new HashMap<>();
	 data.put("tokuisakiCode", maker.getTokuisakiCode());  // 得意先コード
	 data.put("tokuisakiName", maker.getTokuisakiName());  // 得意先名
	 dataList.add(data);
 }
		result.put("dataList", dataList);
		return ResponseEntity.ok(result);
	}
}