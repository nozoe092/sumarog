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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smalog.constant.MenuConstants;
import com.smalog.util.ApplicationUtils;
import com.smalog.util.MessageUtils;


import com.smalog.form.maker.ListForm;
import com.smalog.service.ProjectService;
import com.smalog.service.TokuisakiService;

import jakarta.validation.Valid;

@Controller
public class MakerListController extends BaseController {

	private static final String PAGE_NAME = "メーカ一覧";
	private static final MenuConstants SELECTED_MENU = MenuConstants.MASTER_MAKER_LIST;

	/* Autowired */
	private final TokuisakiService tokuisakiService;
    private final MessageUtils messageUtils;

	@Autowired
	private ProjectService projectService;


	public MakerListController(TokuisakiService tokuisakiService, MessageUtils messageUtils) {
		this.tokuisakiService = tokuisakiService;
		this.messageUtils = messageUtils;
	}
    
	@GetMapping({"/maker/list"})
	public String list(@ModelAttribute ListForm listForm,
						@ModelAttribute("loginTokuisakiCode") String loginTokuisakiCode, 
	 					Model model) throws Exception {
		if(!isLoginUserSunrich()) {
			// 権限が無い場合
			return getPermissionErrorView(model);
		}
		model.addAttribute(PAGE_NAME_KEY, PAGE_NAME);	// 画面名
		model.addAttribute(SELECTED_MENU_ID_KEY, SELECTED_MENU.getId());	// メニューの選択状態
		model.addAttribute("listForm", listForm);	

		 // カテゴリリストの取得
		 model.addAttribute("categoryList", tokuisakiService.getCategoryList());
		 

		//model.addAttribute("hanbaitenSelectBoxOptions", tokuisakiService.getHanbaitenSelectBoxDataList(true)); // 販売店のオプションを追加	
		

	// 販売店データの取得
    List<Map<String, String>> hanbaitenSelectBoxOptions = tokuisakiService.getHanbaitenSelectBoxDataList(true);
    model.addAttribute("hanbaitenSelectBoxOptions", hanbaitenSelectBoxOptions); // 販売店のオプションを追加

    // デバッグ用のログ出力
    for (Map<String, String> option : hanbaitenSelectBoxOptions) {
        System.out.println("Option: " + option);
    }
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
			return ResponseEntity.status(PERMISSION_ERROR_HTTP_STAUS).body(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_PERMISSION));
		}

		List<Map<String, String>> dataList = new ArrayList<>();
		if (bindingResult.hasErrors()) {
			// エラーがあった場合、400 Bad Request とともにエラーメッセージを返す
			return ResponseEntity.status(VALIDATION_INVALID_HTTP_STATUS).body(parseBindingResult(bindingResult));
		}

		 // データを取得してグリッド用に加工する
		 tokuisakiService.getHanbaitenListByShop(
			listForm.getHanbaiten(), 
            listForm.getTokuisakiCode(),
            listForm.getTokuisakiName()
			listForm.getCategory()
        ).forEach(dto -> {
            Map<String, String> data = new HashMap<>();
            data.put("tokuisakiCode", dto.getTokuisakiCode());
            data.put("tokuisakiName", dto.getTokuisakiName());
			data.put("category", dto.getCategory());
            dataList.add(data);
        });


		result.put("dataList", dataList);
		return ResponseEntity.ok(result);
	}


	/**
	 * 販売店変更時のセレクトボックスデータ取得
	 * @param param
	 * @return
	 */
	@PostMapping("/maker/change-hanbaiten-select-box")
	@ResponseBody
	public Map<String, Object> chanegeHanbaitenSelectBox(@RequestBody Map<String, String> param) {		
		Map<String, Object> result = getRestSuccessfulResult();
		result.put("projectSelectBoxOptions", projectService.getSelectBoxDataList(param.get("tokuisakiCode"), true));
		result.put("projectMakerSelectBoxOptions", ApplicationUtils.insertFormSelectBoxStringDefaultRecord(ApplicationUtils.createFormSelectBoxEmptyDataList()));
		return result;
	}
}