package com.smalog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smalog.dto.MakerTantoushaDTO;
import com.smalog.form.element.TantoushaForm;
import com.smalog.mapper.MakerTantoushaMapper;


@Service
public class MakerTantoushaService extends BaseService {
	
	@Autowired
	private MakerTantoushaMapper makerTantoushaMapper;

    /**
     * 販売店担当者の情報を取得する
     * @param id
     * @return
     */
    public MakerTantoushaDTO findById(Integer id) {
        return makerTantoushaMapper.findById(id);
    }

    /**
     * 販売店担当者の情報を取得する
     * 結果が無い場合は0件のリストを返す
     * @param idList
     * @return
     */
    public List<MakerTantoushaDTO> findByIdList(List<Integer> idList) {
        List<MakerTantoushaDTO> makerTantoushaDTOList = new ArrayList<>();
        if(idList.size() != 0) {
            makerTantoushaDTOList = makerTantoushaMapper.findByIdList(idList);
        }
        return makerTantoushaDTOList;
    }

    /**
     * 同じ販売店詳細のIDに含まない担当者を取得する
     * 削除対象を取得するためのメソッド
     * @param makerDetailId
     * @param idList
     * @return
     */
    public List<MakerTantoushaDTO> getNotIdList(Integer makerDetailId, List<Integer> idList) {
        return makerTantoushaMapper.getNotIdList(makerDetailId, idList);
    }

    /**
     * 販売店担当者の情報を販売店詳細IDで取得する
     * @param makerDetailId
     * @return
     */
    public List<MakerTantoushaDTO> fidByMakerDetailId(Integer makerDetailId) {
        return makerTantoushaMapper.findByMakerDetailId(makerDetailId);
    }

    /**
     * 登録する
     * 登録されたIDを返す
     * @param makerTantoushaDTO
     * @return
     */
    public Integer insert(MakerTantoushaDTO makerTantoushaDTO) {
        LocalDateTime now = LocalDateTime.now();
        if(makerTantoushaDTO.getCreateDate() == null) {
            makerTantoushaDTO.setCreateDate(now);
        }
        if(makerTantoushaDTO.getUpdateDate() == null) {
            makerTantoushaDTO.setUpdateDate(now);
        }

        insertList(List.of(makerTantoushaDTO));
        return makerTantoushaDTO.getId();
    }

    /**
     * 登録する
     * @param makerTantoushaDTOList
     * @return
     */
    public void insertList(List<MakerTantoushaDTO> makerTantoushaDTOList) {
        makerTantoushaMapper.insertList(makerTantoushaDTOList);
    }

    /**
     * 更新する
     * 更新行数を返す
     * @param makerTantoushaDTO
     * @return
     */
    public int update(MakerTantoushaDTO makerTantoushaDTO) {
        if(makerTantoushaDTO.getUpdateDate() == null) {
            makerTantoushaDTO.setUpdateDate(LocalDateTime.now());
        }
        // 更新ログを出力する
        insertUpdateLog(makerTantoushaDTO);
        return makerTantoushaMapper.update(makerTantoushaDTO);
    }

    /**
     * 削除する
     * 更新行数を返す
     * @param makerTantoushaDTOList
     * @return
     */
    public int deleteList(List<MakerTantoushaDTO> makerTantoushaDTOList) {
        int result = 0;
        // 更新ログを出力する
        if(makerTantoushaDTOList.size() > 0){
            insertDeleteLog(makerTantoushaDTOList);
            result = makerTantoushaMapper.deleteList(makerTantoushaDTOList);
        }
        return result;
    }
    
    /**
     * 登録するためのdtoを作成する
     * @param makerTantoushaFormList
     * @param makerDetailId
     * @param tokuisakiCode
     * @param tantoushaNumber
     * @return
     */
    public List<MakerTantoushaDTO> createInsertDtoList(List<TantoushaForm> makerTantoushaFormList, Integer makerDetailId, String tokuisakiCode, String tantoushaNumber){
        List<MakerTantoushaDTO> makerTantoushaDTOList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for(TantoushaForm tantoushaForm : makerTantoushaFormList) {
            MakerTantoushaDTO makerTantoushaDTO = new MakerTantoushaDTO();
            makerTantoushaDTO.setMakerDetailId(makerDetailId);
            makerTantoushaDTO.setTokuisakiCode(tokuisakiCode);
            makerTantoushaDTO.setTantoushaName(tantoushaForm.getName());
            makerTantoushaDTO.setMailAddress(tantoushaForm.getMailAddress());
            makerTantoushaDTO.setCreateTantoushaNumber(tantoushaNumber);
            makerTantoushaDTO.setCreateDate(now);
            makerTantoushaDTO.setUpdateTantoushaNumber(tantoushaNumber);
            makerTantoushaDTO.setUpdateDate(now);
            makerTantoushaDTOList.add(makerTantoushaDTO);
        }
        return makerTantoushaDTOList;
	}

    /**
     * 更新するためのdtoを作成する
     * @param makerTantoushaFormList
     * @param makerTantoushaIdList
     * @param tantoushaNumber
     * @return
     */
    public List<MakerTantoushaDTO> createUpdateDtoList(List<TantoushaForm> makerTantoushaFormList, List<Integer> makerTantoushaIdList, String tantoushaNumber){
        LocalDateTime now = LocalDateTime.now();
        List<MakerTantoushaDTO> makerTantoushaDTOList = this.findByIdList(makerTantoushaIdList);
        for(TantoushaForm tantoushaForm : makerTantoushaFormList) {
            Integer targetId = tantoushaForm.getId();
            for(MakerTantoushaDTO makerTantoushaDTO : makerTantoushaDTOList) {
                if(makerTantoushaDTO.getId().equals(targetId)){
                    makerTantoushaDTO.setTantoushaName(tantoushaForm.getName());
                    makerTantoushaDTO.setMailAddress(tantoushaForm.getMailAddress());
                    makerTantoushaDTO.setUpdateTantoushaNumber(tantoushaNumber);
                    makerTantoushaDTO.setUpdateDate(now);
                }
            }
        }
        return makerTantoushaDTOList;
	}
    
}
