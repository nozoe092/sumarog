package com.smalog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smalog.dto.HanbaitenTantoushaDTO;
import com.smalog.form.element.TantoushaForm;
import com.smalog.mapper.HanbaitenTantoushaMapper;


@Service
public class HanbaitenTantoushaService extends BaseService {
	
	@Autowired
	private HanbaitenTantoushaMapper hanbaitenTantoushaMapper;

    /**
     * 販売店担当者の情報を取得する
     * @param id
     * @return
     */
    public HanbaitenTantoushaDTO findById(Integer id) {
        return hanbaitenTantoushaMapper.findById(id);
    }

    /**
     * 販売店担当者の情報を取得する
     * 結果が無い場合は0件のリストを返す
     * @param idList
     * @return
     */
    public List<HanbaitenTantoushaDTO> findByIdList(List<Integer> idList) {
        List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTOList = new ArrayList<>();
        if(idList.size() != 0) {
            hanbaitenTantoushaDTOList = hanbaitenTantoushaMapper.findByIdList(idList);
        }
        return hanbaitenTantoushaDTOList;
    }

    /**
     * 同じ販売店詳細のIDに含まない担当者を取得する
     * 削除対象を取得するためのメソッド
     * @param hanbaitenDetailId
     * @param idList
     * @return
     */
    public List<HanbaitenTantoushaDTO> getNotIdList(Integer hanbaitenDetailId, List<Integer> idList) {
        return hanbaitenTantoushaMapper.getNotIdList(hanbaitenDetailId, idList);
    }

    /**
     * 販売店担当者の情報を販売店詳細IDで取得する
     * @param hanbaitenDetailId
     * @return
     */
    public List<HanbaitenTantoushaDTO> fidByHanbaitenDetailId(Integer hanbaitenDetailId) {
        return hanbaitenTantoushaMapper.findByHanbaitenDetailId(hanbaitenDetailId);
    }

    /**
     * 登録する
     * 登録されたIDを返す
     * @param hanbaitenTantoushaDTO
     * @return
     */
    public Integer insert(HanbaitenTantoushaDTO hanbaitenTantoushaDTO) {
        LocalDateTime now = LocalDateTime.now();
        if(hanbaitenTantoushaDTO.getCreateDate() == null) {
            hanbaitenTantoushaDTO.setCreateDate(now);
        }
        if(hanbaitenTantoushaDTO.getUpdateDate() == null) {
            hanbaitenTantoushaDTO.setUpdateDate(now);
        }

        insertList(List.of(hanbaitenTantoushaDTO));
        return hanbaitenTantoushaDTO.getId();
    }

    /**
     * 登録する
     * @param hanbaitenTantoushaDTOList
     * @return
     */
    public void insertList(List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTOList) {
        hanbaitenTantoushaMapper.insertList(hanbaitenTantoushaDTOList);
    }

    /**
     * 更新する
     * 更新行数を返す
     * @param hanbaitenTantoushaDTO
     * @return
     */
    public int update(HanbaitenTantoushaDTO hanbaitenTantoushaDTO) {
        if(hanbaitenTantoushaDTO.getUpdateDate() == null) {
            hanbaitenTantoushaDTO.setUpdateDate(LocalDateTime.now());
        }
        // 更新ログを出力する
        insertUpdateLog(hanbaitenTantoushaDTO);
        return hanbaitenTantoushaMapper.update(hanbaitenTantoushaDTO);
    }

    /**
     * 削除する
     * 更新行数を返す
     * @param hanbaitenTantoushaDTOList
     * @return
     */
    public int deleteList(List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTOList) {
        int result = 0;
        // 更新ログを出力する
        if(hanbaitenTantoushaDTOList.size() > 0){
            insertDeleteLog(hanbaitenTantoushaDTOList);
            result = hanbaitenTantoushaMapper.deleteList(hanbaitenTantoushaDTOList);
        }
        return result;
    }
    
    /**
     * 登録するためのdtoを作成する
     * @param hanbaitenTantoushaFormList
     * @param hanbaitenDetailId
     * @param tokuisakiCode
     * @param tantoushaNumber
     * @return
     */
    public List<HanbaitenTantoushaDTO> createInsertDtoList(List<TantoushaForm> hanbaitenTantoushaFormList, Integer hanbaitenDetailId, String tokuisakiCode, String tantoushaNumber){
        List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTOList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for(TantoushaForm tantoushaForm : hanbaitenTantoushaFormList) {
            HanbaitenTantoushaDTO hanbaitenTantoushaDTO = new HanbaitenTantoushaDTO();
            hanbaitenTantoushaDTO.setHanbaitenDetailId(hanbaitenDetailId);
            hanbaitenTantoushaDTO.setTokuisakiCode(tokuisakiCode);
            hanbaitenTantoushaDTO.setTantoushaName(tantoushaForm.getName());
            hanbaitenTantoushaDTO.setMailAddress(tantoushaForm.getMailAddress());
            hanbaitenTantoushaDTO.setCreateTantoushaNumber(tantoushaNumber);
            hanbaitenTantoushaDTO.setCreateDate(now);
            hanbaitenTantoushaDTO.setUpdateTantoushaNumber(tantoushaNumber);
            hanbaitenTantoushaDTO.setUpdateDate(now);
            hanbaitenTantoushaDTOList.add(hanbaitenTantoushaDTO);
        }
        return hanbaitenTantoushaDTOList;
	}

    /**
     * 更新するためのdtoを作成する
     * @param hanbaitenTantoushaFormList
     * @param hanbaitenTantoushaIdList
     * @param tantoushaNumber
     * @return
     */
    public List<HanbaitenTantoushaDTO> createUpdateDtoList(List<TantoushaForm> hanbaitenTantoushaFormList, List<Integer> hanbaitenTantoushaIdList, String tantoushaNumber){
        LocalDateTime now = LocalDateTime.now();
        List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTOList = this.findByIdList(hanbaitenTantoushaIdList);
        for(TantoushaForm tantoushaForm : hanbaitenTantoushaFormList) {
            Integer targetId = tantoushaForm.getId();
            for(HanbaitenTantoushaDTO hanbaitenTantoushaDTO : hanbaitenTantoushaDTOList) {
                if(hanbaitenTantoushaDTO.getId().equals(targetId)){
                    hanbaitenTantoushaDTO.setTantoushaName(tantoushaForm.getName());
                    hanbaitenTantoushaDTO.setMailAddress(tantoushaForm.getMailAddress());
                    hanbaitenTantoushaDTO.setUpdateTantoushaNumber(tantoushaNumber);
                    hanbaitenTantoushaDTO.setUpdateDate(now);
                }
            }
        }
        return hanbaitenTantoushaDTOList;
	}
    
}
