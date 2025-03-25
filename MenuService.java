package com.smalog.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smalog.constant.ApplicationConstants;
import com.smalog.constant.MenuConstants;
import com.smalog.model.Menu;

@Service
public class MenuService implements ApplicationConstants{

    /**
     * ユーザー権限によってメニューを取得する
     * TODO：パーミッションによって生成するメニューを変更する
     * @param kaishaKubunCode
     * @param adminFlag
     * @return
     */
    public List<Menu> getMenuList(String kaishaKubunCode, String adminFlag) {
        // 順序を保持するために LinkedHashMap を使用
        Map<String, Menu> menuMap = new LinkedHashMap<>();
        List<Menu> menuList = new ArrayList<>();

        // メニューを順番通りに作成
        for (MenuConstants menuConst : MenuConstants.values()) {
            menuMap.put(menuConst.getId(), new Menu(menuConst));
        }

        // 親子関係を設定
        for (MenuConstants menuConst : MenuConstants.values()) {
            Menu menu = menuMap.get(menuConst.getId());
            String parentId = menuConst.getParentId();

            // 最上位階層の場合
            if (parentId == null) {
                // パーミッションによる制御追加
                if(isVisibleMenu(menuConst.getUsableKaishaKubunCode(), kaishaKubunCode)){
                    menuList.add(menu);
                }
            } else {
                // 最上位以外の階層の場合
                Menu parentMenu = menuMap.get(parentId);
                if (parentMenu != null) {
                    // パーミッションによる制御追加
                    if(isVisibleMenu(menuConst.getUsableKaishaKubunCode(), kaishaKubunCode)){
                        parentMenu.addMenu(menu);
                    }
                }
            }
        }

        return menuList;
    }

    /**
     * 表示するメニューかどうかを判定する
     * @param usableKaishaKubunCode
     * @param kaishaKubunCode
     * @return
     */
    private boolean isVisibleMenu(String usableKaishaKubunCode, String kaishaKubunCode) {
        boolean result = false;
        if(usableKaishaKubunCode.isEmpty()){
            // ブランクは全てのユーザーに表示する
            result = true;
        }else{
            String[] usableKaishaKubunCodeArray = usableKaishaKubunCode.split(USABLE_KAISHA_KUBUN_CODE_SEPARATOR);
            for (String usableKaishaKubunCodeValue : usableKaishaKubunCodeArray) {
                if (usableKaishaKubunCodeValue.equals(kaishaKubunCode)) {
                    result = true;
                    break;
                }
            } 
        }
        return result;
    }
}
