package com.smalog.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.smalog.dto.TantoushaInfoDTO;
import com.smalog.form.ChatMessageForm;
import com.smalog.model.ChatMessage;
import com.smalog.model.LoginSession;
import com.smalog.service.ProjectMakerService;
import com.smalog.service.ProjectService;
import com.smalog.service.TantoushaService;
import com.smalog.service.TokuisakiService;
import com.smalog.util.ApplicationUtils;

@Controller
public class DashboardController extends BaseController {
	@Autowired
    private TantoushaService tantoushaService;
	@Autowired
	private TokuisakiService tokuisakiService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjectMakerService projectMakerService;

	@Autowired
    private LoginSession loginSession;
	
	@Autowired
    private SpringTemplateEngine templateEngine;
    
	@GetMapping({"/","/dashboard"})
	public String dashboard(Model model) {
		List<TantoushaInfoDTO> test = tantoushaService.getTokuisakiTantousha(loginSession.getTokuisakiCode(), loginSession.getTantoushaNumber());
		model.addAttribute("pageName", "ダッシュボード");
		model.addAttribute("message", test.get(0).getKaishaName() + "：" + loginSession.getTantoushaName());
		model.addAttribute("testList", getTestSelectBoxList(10));

		model.addAttribute("hanbaitenSelectBoxOptions", tokuisakiService.getHanbaitenSelectBoxDataList(true));
		model.addAttribute("projectSelectBoxOptions", ApplicationUtils.insertFormSelectBoxIntegerDefaultRecord(ApplicationUtils.createFormSelectBoxEmptyDataList()));
		model.addAttribute("projectMakerSelectBoxOptions", ApplicationUtils.insertFormSelectBoxStringDefaultRecord(ApplicationUtils.createFormSelectBoxEmptyDataList()));
		

		return "dashboard/dashboard";
	}
	 
	/**
	 * JSONを受け取ってプレーンテキストを返す
	* @param param
	* @return
	*/
	@PostMapping("/dashboard/get-test-list")
	@ResponseBody
	public String getTestList(@RequestBody Map<String, String> param) {		
		 // データを渡す
		Context context = new Context();
		context.setVariable("valueText", "入力値");
		context.setVariable("testList", getTestSelectBoxList(30));
		context.setVariable("count", Integer.parseInt(param.get("count")) + 1);
		context.setVariable("testSelectedValue", "22");	// 型は揃える必要があるため選択肢はMapではなくクラスにした方が安全
		// HTML取得
		String result = templateEngine.process("element/test", context);
		return result;
	}

	/**
	 * テスト用セレクトボックスリストを取得
	 * @param max
	 * @return
	 */
	private List<Map<String, String>> getTestSelectBoxList(int max){
		List<Map<String, String>> optionsList = new ArrayList<>();

		for(int i=1; i<=max; i++){
			Map<String, String> option = new HashMap<>();
			option.put("value", String.valueOf(i));
			option.put("display", "表示値" + i);
			option.put("data-test", "test-" + i);

			optionsList.add(option);
		}
		return optionsList;
	}

	/**
	 * チャットメッセージを受け取ってブロードキャストする
	 * @param chatMessageForm
	 * @param sessionAttributes
	 * @return
	 * @throws Exception
	 */
	@MessageMapping("/send-chat-message")
    @SendTo("/receive/chat-message")
    public ChatMessage sendChatMessage(ChatMessageForm chatMessageForm, @Header("simpSessionAttributes") Map<String, Object> sessionAttributes) throws Exception {
		LoginSession loginSession = (LoginSession)sessionAttributes.get("scopedTarget.loginSession");
		// DBに登録等の処理を行う
		// メッセージをブロードキャストで返す
        return new ChatMessage(loginSession.getTokuisakiName(), loginSession.getTantoushaName(), HtmlUtils.htmlEscape(chatMessageForm.getMessage()));
    }



	/*
	 * 以下からは共通処理として残す
	 */

	/**
	 * 販売店変更時の企画データ取得
	 * @param param
	 * @return
	 */
	@PostMapping("/dashboard/change-hanbaiten-select-box")
	@ResponseBody
	public Map<String, Object> chanegeHanbaitenSelectBox(@RequestBody Map<String, String> param) {		
		Map<String, Object> result = getRestSuccessfulResult();
		result.put("projectSelectBoxOptions", projectService.getSelectBoxDataList(param.get("tokuisakiCode"), true));
		result.put("projectMakerSelectBoxOptions", ApplicationUtils.insertFormSelectBoxStringDefaultRecord(ApplicationUtils.createFormSelectBoxEmptyDataList()));
		return result;
	}

	/**
	 * 企画変更時のメーカーデータ取得
	 * @param param
	 * @return
	 */
	@PostMapping("/dashboard/change-project-select-box")
	@ResponseBody
	public Map<String, Object> chanegeProjectSelectBox(@RequestBody Map<String, String> param) {		
		Map<String, Object> result = getRestSuccessfulResult();
		result.put("projectMakerSelectBoxOptions", projectMakerService.getSelectBoxDataList(param.get("tokuisakiCode"), Integer.parseInt(param.get("projectId")), true));
		return result;
	}
}
