package com.smalog.form.maker;

import java.io.Serializable;

import com.smalog.form.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListForm extends BaseForm implements Serializable{

    private String hanbaiten;
    private String tokuisakiCode;
    private String tokuisakiName;
    private String category;
    
    public String getHanbaiten() {
        return hanbaiten;
    }

    public void setHanbaiten(String hanbaiten) {
        this.hanbaiten = hanbaiten;
    }

    private String category(){
        return category;
    }


    public ListForm() {}
}