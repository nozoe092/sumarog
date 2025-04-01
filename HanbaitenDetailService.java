package com.smalog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smalog.dto.HanbaitenDetailDTO;
import com.smalog.dto.HanbaitenTantoushaDTO;
import com.smalog.form.element.TantoushaForm;
import com.smalog.form.hanbaiten.DetailForm;
import com.smalog.mapper.HanbaitenDetailMapper;
import com.smalog.util.MessageUtils;



@Service
public class HanbaitenDetailService extends BaseService {
	
	@Autowired
	private HanbaitenDetailMapper hanbaitenDetailMapper;

    @Autowired
    private MessageUtils messageUtils;

    @Autowired
    private HanbaitenTantoushaService hanbaitenTantoushaService;

    /**
     * 販売店の情報を取得する
     * @param id
     * @return
     */
    public HanbaitenDetailDTO findById(Integer id) {
        return hanbaitenDetailMapper.findById(id);
    }

    /**
     * 販売店の情報を得意先コードで取得する
     * @param tokuisakiCode
     * @return
     */
    public HanbaitenDetailDTO findByTokuisakiCode(String tokuisakiCode) {
        return hanbaitenDetailMapper.findByTokuisakiCode(tokuisakiCode);
    }

    /**
     * 登録する
     * 登録されたIDを返す
     * @param hanbaitenDetailDTO
     * @return
     */
    public Integer insert(HanbaitenDetailDTO hanbaitenDetailDTO) {
        LocalDateTime now = LocalDateTime.now();
        if(hanbaitenDetailDTO.getCreateDate() == null) {
            hanbaitenDetailDTO.setCreateDate(now);
        }
        if(hanbaitenDetailDTO.getUpdateDate() == null) {
            hanbaitenDetailDTO.setUpdateDate(now);
        }

        hanbaitenDetailMapper.insert(hanbaitenDetailDTO);
        return hanbaitenDetailDTO.getId();
    }

    /**
     * 更新する
     * 更新行数を返す
     * @param hanbaitenDetailDTO
     * @return
     */
    public int update(HanbaitenDetailDTO hanbaitenDetailDTO) {
        if(hanbaitenDetailDTO.getUpdateDate() == null) {
            hanbaitenDetailDTO.setUpdateDate(LocalDateTime.now());
        }
        // 更新ログを出力する
        insertUpdateLog(hanbaitenDetailDTO);
        return hanbaitenDetailMapper.update(hanbaitenDetailDTO);
    }
    
    /**
     * 保存処理
     * トランザクションを行う
     * @param detailForm
     * @param tantoushaNumber
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTransaction(DetailForm detailForm, String tantoushaNumber) {
        boolean result = false; 

        Integer hanbaitenDetailId = detailForm.getId();
		HanbaitenDetailDTO hanbaitenDetailDTO = null;
		boolean isNew = hanbaitenDetailId == null;
		if(isNew) {
			// 新規登録
			hanbaitenDetailDTO = new HanbaitenDetailDTO();
			// 値を設定する
			setHanibaitenDetailFormToDto(detailForm, hanbaitenDetailDTO, tantoushaNumber);

			this.insert(hanbaitenDetailDTO);
			if(hanbaitenDetailDTO.getId() == 0) {
				// 登録失敗
                throw new RuntimeException(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_SAVE_FAILED));
			}
		} else {
			// 更新
			hanbaitenDetailDTO = findById(hanbaitenDetailId);
            // 排他チェック
			if(hanbaitenDetailDTO != null && detailForm.getUpdateDate().isEqual(hanbaitenDetailDTO.getUpdateDate())){
				// 値を設定する
				setHanibaitenDetailFormToDto(detailForm, hanbaitenDetailDTO, tantoushaNumber);
				int updateCount = this.update(hanbaitenDetailDTO);
				if(updateCount != 1) {
					// 更新失敗
                    throw new RuntimeException(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_UPDATE_FAILED));
				}
			}else{
				// 排他エラー
				throw new RuntimeException(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_OPTIMISTICLOCK));
			}
		}


        // 担当者情報の保存
        Map<Integer, TantoushaForm> tantoushaFormList = detailForm.getTantoushaFormList();
        List<TantoushaForm> hanbaitenTantoushaFormInsertList = new ArrayList<>();
        List<TantoushaForm> hanbaitenTantoushaFormUpdateList = new ArrayList<>();
        List<Integer> hanbaitenTantoushaIdList = new ArrayList<>();
		for (Integer key : tantoushaFormList.keySet()) {
			TantoushaForm tantoushaForm = tantoushaFormList.get(key);
            Integer hanbaitenTantoushaId = tantoushaForm.getId();
            if(hanbaitenTantoushaId != null) {
                // 更新
                hanbaitenTantoushaFormUpdateList.add(tantoushaForm);
                hanbaitenTantoushaIdList.add(hanbaitenTantoushaId);
            }else{
                hanbaitenTantoushaFormInsertList.add(tantoushaForm);
            }
		}

        // 削除
        List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTODeleteList = hanbaitenTantoushaService.getNotIdList(hanbaitenDetailDTO.getId(), hanbaitenTantoushaIdList);
        if(hanbaitenTantoushaDTODeleteList.size() > 0) {
            hanbaitenTantoushaService.deleteList(hanbaitenTantoushaDTODeleteList);
        }

        // 更新
        List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTOUpdateList = hanbaitenTantoushaService.createUpdateDtoList(hanbaitenTantoushaFormUpdateList, hanbaitenTantoushaIdList, tantoushaNumber);
        int count = 0;
        for(HanbaitenTantoushaDTO hanbaitenTantoushaDTO : hanbaitenTantoushaDTOUpdateList) {
            count += hanbaitenTantoushaService.update(hanbaitenTantoushaDTO);
        }
        if(hanbaitenTantoushaFormUpdateList.size() != count){
            // 更新件数が違うため排他エラー
            throw new RuntimeException(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_OPTIMISTICLOCK));
        }
        // 登録
        List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTOInsertList = hanbaitenTantoushaService.createInsertDtoList(hanbaitenTantoushaFormInsertList, hanbaitenDetailDTO.getId(), hanbaitenDetailDTO.getTokuisakiCode(), tantoushaNumber);
        if(hanbaitenTantoushaDTOInsertList.size() > 0) {
            hanbaitenTantoushaService.insertList(hanbaitenTantoushaDTOInsertList);
        }

        result = true;
        return result;
    }

    /**
     * 表示するためのDTOの値をFormにセットする
     * @param detailForm
     * @param hanbaitenDetailDTO
     */
	public void setDtoToHanibaitenDetailForm(DetailForm detailForm, HanbaitenDetailDTO hanbaitenDetailDTO){
        detailForm.setId(hanbaitenDetailDTO.getId());
        detailForm.setShouhinjouhouNyuryokuRule(hanbaitenDetailDTO.getShouhinjouhouNyuryokuRule());
        detailForm.setMailJushinFlag(hanbaitenDetailDTO.getMailJushinFlag());
        detailForm.setUpdateDate(hanbaitenDetailDTO.getUpdateDate());

        List<HanbaitenTantoushaDTO> hanbaitenTantoushaDTOList = hanbaitenTantoushaService.fidByHanbaitenDetailId(hanbaitenDetailDTO.getId());
        
        for(int index = 0; index < hanbaitenTantoushaDTOList.size(); index++) {
            HanbaitenTantoushaDTO hanbaitenTantoushaDTO = hanbaitenTantoushaDTOList.get(index);
            TantoushaForm tantoushaForm = new TantoushaForm();
            tantoushaForm.setId(hanbaitenTantoushaDTO.getId());
            tantoushaForm.setName(hanbaitenTantoushaDTO.getTantoushaName());
            tantoushaForm.setMailAddress(hanbaitenTantoushaDTO.getMailAddress());
            tantoushaForm.setBeforeMailAddress(hanbaitenTantoushaDTO.getMailAddress());
            detailForm.getTantoushaFormList().put(index + DetailForm.START_TANTOUSHA_LIST_INDEX, tantoushaForm);
        }
	}

    /**
     * 保存するための値をDTOにセットする
     * @param detailForm
     * @param hanbaitenDetailDTO
     * @param tantoushaNumber
     */
	private void setHanibaitenDetailFormToDto(DetailForm detailForm, HanbaitenDetailDTO hanbaitenDetailDTO, String tantoushaNumber){
		hanbaitenDetailDTO.setTokuisakiCode(detailForm.getTokuisakiCode());
		hanbaitenDetailDTO.setShouhinjouhouNyuryokuRule(detailForm.getShouhinjouhouNyuryokuRule());
        hanbaitenDetailDTO.setMailJushinFlag(detailForm.getMailJushinFlag());

		hanbaitenDetailDTO.setCreateTantoushaNumber(tantoushaNumber);
        hanbaitenDetailDTO.setCreateDate(null);
		hanbaitenDetailDTO.setUpdateTantoushaNumber(tantoushaNumber);
        hanbaitenDetailDTO.setUpdateDate(null);

	}
}
