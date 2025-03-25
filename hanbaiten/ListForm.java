package com.smalog.form.hanbaiten;

import java.io.Serializable;

import com.smalog.form.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListForm extends BaseForm implements Serializable{

    private String tokuisakiCode;
    private String tokuisakiName;
	private String tokuisakiNameKana;

    public ListForm() {}

}