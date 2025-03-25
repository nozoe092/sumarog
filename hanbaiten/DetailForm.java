package com.smalog.form.hanbaiten;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.smalog.form.BaseForm;
import com.smalog.form.element.TantoushaForm;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailForm extends BaseForm implements Serializable{

    private int id;
    private String tokuisakiCode;
    private Integer shouhinjouhouNyuryokuRule;
    @NotNull
    private Integer mailJushinFlag;
    private Map<String, Integer> mailJushinFlagOptions;
    private LocalDateTime updateDate;

    public static final int START_TANTOUSHA_LIST_INDEX = 1;
    public static final int MAX_TANTOUSHA_LIST_INDEX = 10;

    

    @Valid
    private Map<Integer, TantoushaForm> tantoushaFormList;

    public DetailForm() {
        this.mailJushinFlagOptions = getFormBooleanRadioButtonOptions("する", "しない");
        this.tantoushaFormList = new LinkedHashMap<>();
    }

}