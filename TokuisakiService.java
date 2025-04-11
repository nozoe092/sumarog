package com.smalog.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smalog.constant.DBKeyLabelConstants;
import com.smalog.dto.TokuisakiDTO;
import com.smalog.dto.common.SelectBoxDTO;
import com.smalog.mapper.TokuisakiMapper;

@Service
public class TokuisakiService extends BaseService {
	
	@Autowired
	private TokuisakiMapper tokuisakiMapper;

    /**
     * 販売店の情報を取得する
     * @param tokuisakiCode
     * @param tokuisakiName
     * @param tokuisakiNameKana
     * @return
     */
    public List<TokuisakiDTO> getHanbaitenList(String tokuisakiCode, String tokuisakiName, String tokuisakiNameKana) {
        return tokuisakiMapper.getHanbaitenList(DBKeyLabelConstants.KAISHA_KUBUN_CODE_HANBAITEN.getStringValue(), DBKeyLabelConstants.STRING_FLAG_ON.getStringValue(), tokuisakiCode, tokuisakiName, tokuisakiNameKana);
    }

     /**
     * 販売店の情報を取得する
     * @param hanbaiten
     * @param tokuisakiCode
     * @param tokuisakiName
     * @return
     */
    public List<TokuisakiDTO> getHanbaitenListByHanbaiten(String hanbaiten, String tokuisakiCode, String tokuisakiName) {
        return tokuisakiMapper.getHanbaitenListByHanbaiten(DBKeyLabelConstants.KAISHA_KUBUN_CODE_HANBAITEN.getStringValue(), DBKeyLabelConstants.STRING_FLAG_ON.getStringValue(), hanbaiten, tokuisakiCode, tokuisakiName);
    }

    /**
     * 販売店条件を含むメーカー情報を取得する
     * @param hanbaiten 販売店コード
     * @param tokuisakiCode 得意先コード
     * @param tokuisakiName 得意先名
     * @return メーカー情報のリスト
     */
    public List<TokuisakiDTO> getHanbaitenListByShop(String hanbaiten, String tokuisakiCode, String tokuisakiName,String category) {
        return tokuisakiMapper.getHanbaitenListByShop(hanbaiten, tokuisakiCode, tokuisakiName,category);
    }

     /**
     * カテゴリリストを取得する
     */
    public List<TokuisakiDTO> getCategoryList() {
        return tokuisakiMapper.getCategoryList(); // Mapper から取得
    }

    /**
     * 販売店の情報を取得する
     * @param kaishaKubunCode
     * @param tokuisakiCode
     * @return
     */
    public TokuisakiDTO findHanbaiten(String tokuisakiCode) {
        return findTokuisaki(DBKeyLabelConstants.KAISHA_KUBUN_CODE_HANBAITEN.getStringValue(), tokuisakiCode);
    }

    /**
     * 販売店の情報を取得する
     * @param tokuisakiCode
     * @param tokuisakiName
     * @return
     */
    public List<TokuisakiDTO> getHanbaitenList(String tokuisakiCode, String tokuisakiName) {
        return getHanbaitenList(tokuisakiCode, tokuisakiName, null);
    }

    /**
     * メーカーの情報を取得する
     * @param kaishaKubunCode
     * @param tokuisakiCode
     * @return
     */
    public TokuisakiDTO findMaker(String tokuisakiCode) {
        return findTokuisaki(DBKeyLabelConstants.KAISHA_KUBUN_CODE_MAKER.getStringValue(), tokuisakiCode);
    }

    /**
     * 販売店のセレクトボックス用のデータを取得する
     * @param useDefaultRecord
     * @return
     */
    public List<Map<String, String>> getHanbaitenSelectBoxDataList(boolean useDefaultRecord) {
        return this.getHanbaitenSelectBoxDataList(null, useDefaultRecord);
    }

    /**
     * 販売店のセレクトボックス用のデータを取得する
     * @param tokuisakiCode
     * @param useDefaultRecord
     * @return
     */
    public List<Map<String, String>> getHanbaitenSelectBoxDataList(String tokuisakiCode, boolean useDefaultRecord) {
        List<SelectBoxDTO> selectBoxDTOList = tokuisakiMapper.getSelectBoxDataList(DBKeyLabelConstants.KAISHA_KUBUN_CODE_HANBAITEN.getStringValue(), DBKeyLabelConstants.STRING_FLAG_ON.getStringValue(), tokuisakiCode);
        List<Map<String, String>> formSelectBoxDataList = convertDTOSelectBoxData(selectBoxDTOList);
        if (useDefaultRecord) {
            insertSelectBoxDataStringDefaultRecord(formSelectBoxDataList);
        }
        return formSelectBoxDataList;
    }

    /**
     * 得意先の情報を取得する
     * @param kaishaKubunCode
     * @param tokuisakiCode
     * @return
     */
    private TokuisakiDTO findTokuisaki(String kaishaKubunCode, String tokuisakiCode) {
        return tokuisakiMapper.findTokuisaki(kaishaKubunCode, tokuisakiCode, DBKeyLabelConstants.STRING_FLAG_ON.getStringValue());
    }

    /**
     * ログインした得意先のデータを取得する
     * @param tokuisakiCode
     * @return
     */
    public TokuisakiDTO findTokuisakiByCode(String tokuisakiCode) {
        return tokuisakiMapper.findTokuisakiByCode(tokuisakiCode);
    }

}
