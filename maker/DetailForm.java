package com.smalog.form.maker;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.smalog.constant.DBKeyLabelConstants;
import com.smalog.form.BaseForm;
import com.smalog.form.element.TantoushaForm;
import com.smalog.util.ApplicationUtils;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailForm extends BaseForm implements Serializable{

    private Integer id;
    private String tokuisakiCode;
    @Min(value=1, message="{Min.Empty.message}")
    private Integer shouhinjouhouNyuryokuRule;
    private List<Map<String, String>> shouhinjouhouNyuryokuRuleOptions;
    @NotNull
    private Integer mailJushinFlag;
    private Map<String, Integer> mailJushinFlagOptions;
    private LocalDateTime updateDate;   // 排他制御用

    public static final int START_TANTOUSHA_LIST_INDEX = 1;
    public static final int MAX_TANTOUSHA_LIST_INDEX = 5;

    

    @Valid
    private Map<Integer, TantoushaForm> tantoushaFormList;

    public DetailForm() {
        this.shouhinjouhouNyuryokuRuleOptions = ApplicationUtils.createFormSelectBoxEmptyDataList();
        this.shouhinjouhouNyuryokuRuleOptions.add(ApplicationUtils.createFormSelectBoxRecord(DBKeyLabelConstants.SHOUHIN_INFO_INPUT_RULE_NO_SETIING.getLabel(), DBKeyLabelConstants.SHOUHIN_INFO_INPUT_RULE_NO_SETIING.getStringValue(),""));
        this.shouhinjouhouNyuryokuRuleOptions.add(ApplicationUtils.createFormSelectBoxRecord(DBKeyLabelConstants.SHOUHIN_INFO_INPUT_RULE_YAMAKATAYA.getLabel(), DBKeyLabelConstants.SHOUHIN_INFO_INPUT_RULE_YAMAKATAYA.getStringValue(),""));
        this.mailJushinFlagOptions = ApplicationUtils.getFormBooleanRadioButtonOptions("する", "しない");
        this.tantoushaFormList = new LinkedHashMap<>();
    }

    public int getMaxTantoushaListIndex() {
        return MAX_TANTOUSHA_LIST_INDEX;
    }

}