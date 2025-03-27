package com.smalog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smalog.constant.DBKeyLabelConstants;
import com.smalog.dto.TokuisakiDTO;
import com.smalog.mapper.TokuisakiMapper;

@Service
public class TokuisakiService {
	
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
        return tokuisakiMapper.getHanbaitenList(DBKeyLabelConstants.KAISHA_KUBUN_CODE_HANBAITEN.getStringValue(), tokuisakiCode, tokuisakiName, tokuisakiNameKana);
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
     * 
     * @param tokuisakiCode
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
     * 得意先の情報を取得する
     * @param kaishaKubunCode
     * @param tokuisakiCode
     * @return
     */
    private TokuisakiDTO findTokuisaki(String kaishaKubunCode, String tokuisakiCode) {
        return tokuisakiMapper.findTokuisaki(kaishaKubunCode, tokuisakiCode);
    }
}
