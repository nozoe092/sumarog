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
import org.springframework.web.bind.annotation.ResponseBody;

import com.smalog.constant.MenuConstants;
import com.smalog.form.hanbaiten.ListForm;
import com.smalog.service.MessageService;
import com.smalog.service.TokuisakiService;

import jakarta.validation.Valid;


@Controller
public class HanbaitenListController extends BaseController {

	@Autowired
    private MessageService messageService;

	private static final String PAGE_NAME = "販売店一覧";
	private static final MenuConstants SELECTED_MENU = MenuConstants.MASTER_HANBAITEN_LIST;

	@Autowired
    private TokuisakiService tokuisakiService;
    
	@GetMapping({"/hanbaiten/list"})
	public String list(@ModelAttribute ListForm listForm, Model model) throws Exception {
		if(!isLoginUserSunrich()) {
			// 権限が無い場合
			return getPermissionErrorView(model);
		}
		model.addAttribute(PAGE_NAME_KEY, PAGE_NAME);	// 画面名
		model.addAttribute(SELECTED_MENU_ID_KEY, SELECTED_MENU.getId());	// メニューの選択状態
		model.addAttribute("listForm", listForm);
		return "hanbaiten/list";
	}

	/**
	 * 販売店一覧のデータを取得する
	 * @param listForm
	 * @param bindingResult
	 * @return
	 */
	@PostMapping("/hanbaiten/list/get-data")
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

		// データを取得してグリッド用に加工する
		tokuisakiService.getHanbaitenList(listForm.getTokuisakiCode(), listForm.getTokuisakiName(), listForm.getTokuisakiNameKana()).forEach(dto -> {
			Map<String, String> data = new HashMap<>();
			data.put("tokuisakiCode", dto.getTokuisakiCode());
			data.put("tokuisakiName", dto.getTokuisakiName());
			data.put("tokuisakiNameKana", dto.getTokuisakiNameKana());
			dataList.add(data);
		});

		result.put("dataList", dataList);
		return ResponseEntity.ok(result);
	}
}
