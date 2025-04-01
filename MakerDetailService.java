package com.smalog.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smalog.dto.MakerDetailDTO;
import com.smalog.dto.MakerTantoushaDTO;
import com.smalog.form.element.TantoushaForm;
import com.smalog.form.maker.DetailForm;
import com.smalog.mapper.MakerDetailMapper;
import com.smalog.util.MessageUtils;



@Service
public class MakerDetailService extends BaseService {
	
	@Autowired
	private MakerDetailMapper makerDetailMapper;

    @Autowired
    private MessageUtils messageUtils;

    @Autowired
    private MakerTantoushaService makerTantoushaService;

    /**
     * 販売店の情報を取得する
     * @param id
     * @return
     */
    public MakerDetailDTO findById(Integer id) {
        return makerDetailMapper.findById(id);
    }

    /**
     * 販売店の情報を得意先コードで取得する
     * @param tokuisakiCode
     * @return
     */
    public MakerDetailDTO findByTokuisakiCode(String tokuisakiCode) {
        return makerDetailMapper.findByTokuisakiCode(tokuisakiCode);
    }

    /**
     * 登録する
     * 登録されたIDを返す
     * @param makerDetailDTO
     * @return
     */
    public Integer insert(MakerDetailDTO makerDetailDTO) {
        LocalDateTime now = LocalDateTime.now();
        if(makerDetailDTO.getCreateDate() == null) {
            makerDetailDTO.setCreateDate(now);
        }
        if(makerDetailDTO.getUpdateDate() == null) {
            makerDetailDTO.setUpdateDate(now);
        }

        makerDetailMapper.insert(makerDetailDTO);
        return makerDetailDTO.getId();
    }

    /**
     * 更新する
     * 更新行数を返す
     * @param makerDetailDTO
     * @return
     */
    public int update(MakerDetailDTO makerDetailDTO) {
        if(makerDetailDTO.getUpdateDate() == null) {
            makerDetailDTO.setUpdateDate(LocalDateTime.now());
        }
        // 更新ログを出力する
        insertUpdateLog(makerDetailDTO);
        return makerDetailMapper.update(makerDetailDTO);
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

        Integer makerDetailId = detailForm.getId();
		MakerDetailDTO makerDetailDTO = null;
		boolean isNew = makerDetailId == null;
		if(isNew) {
			// 新規登録
			makerDetailDTO = new MakerDetailDTO();
			// 値を設定する
			setHanibaitenDetailFormToDto(detailForm, makerDetailDTO, tantoushaNumber);

			this.insert(makerDetailDTO);
			if(makerDetailDTO.getId() == 0) {
				// 登録失敗
                throw new RuntimeException(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_SAVE_FAILED));
			}
		} else {
			// 更新
			makerDetailDTO = findById(makerDetailId);
            // 排他チェック
			if(makerDetailDTO != null && detailForm.getUpdateDate().isEqual(makerDetailDTO.getUpdateDate())){
				// 値を設定する
				setHanibaitenDetailFormToDto(detailForm, makerDetailDTO, tantoushaNumber);
				int updateCount = this.update(makerDetailDTO);
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
        List<TantoushaForm> makerTantoushaFormInsertList = new ArrayList<>();
        List<TantoushaForm> makerTantoushaFormUpdateList = new ArrayList<>();
        List<Integer> makerTantoushaIdList = new ArrayList<>();
		for (Integer key : tantoushaFormList.keySet()) {
			TantoushaForm tantoushaForm = tantoushaFormList.get(key);
            Integer makerTantoushaId = tantoushaForm.getId();
            if(makerTantoushaId != null) {
                // 更新
                makerTantoushaFormUpdateList.add(tantoushaForm);
                makerTantoushaIdList.add(makerTantoushaId);
            }else{
                makerTantoushaFormInsertList.add(tantoushaForm);
            }
		}

        // 削除
        List<MakerTantoushaDTO> makerTantoushaDTODeleteList = makerTantoushaService.getNotIdList(makerDetailDTO.getId(), makerTantoushaIdList);
        if(makerTantoushaDTODeleteList.size() > 0) {
            makerTantoushaService.deleteList(makerTantoushaDTODeleteList);
        }

        // 更新
        List<MakerTantoushaDTO> makerTantoushaDTOUpdateList = makerTantoushaService.createUpdateDtoList(makerTantoushaFormUpdateList, makerTantoushaIdList, tantoushaNumber);
        int count = 0;
        for(MakerTantoushaDTO makerTantoushaDTO : makerTantoushaDTOUpdateList) {
            count += makerTantoushaService.update(makerTantoushaDTO);
        }
        if(makerTantoushaFormUpdateList.size() != count){
            // 更新件数が違うため排他エラー
            throw new RuntimeException(messageUtils.getMessage(MESSAGE_PROPERTY_NAME_ERROR_OPTIMISTICLOCK));
        }
        // 登録
        List<MakerTantoushaDTO> makerTantoushaDTOInsertList = makerTantoushaService.createInsertDtoList(makerTantoushaFormInsertList, makerDetailDTO.getId(), makerDetailDTO.getTokuisakiCode(), tantoushaNumber);
        if(makerTantoushaDTOInsertList.size() > 0) {
            makerTantoushaService.insertList(makerTantoushaDTOInsertList);
        }

        result = true;
        return result;
    }

    /**
     * 表示するためのDTOの値をFormにセットする
     * @param detailForm
     * @param makerDetailDTO
     */
	public void setDtoToHanibaitenDetailForm(DetailForm detailForm, MakerDetailDTO makerDetailDTO){
        detailForm.setId(makerDetailDTO.getId());
        detailForm.setShouhinjouhouNyuryokuRule(makerDetailDTO.getShouhinjouhouNyuryokuRule());
        detailForm.setMailJushinFlag(makerDetailDTO.getMailJushinFlag());
        detailForm.setUpdateDate(makerDetailDTO.getUpdateDate());

        List<MakerTantoushaDTO> makerTantoushaDTOList = makerTantoushaService.fidByMakerDetailId(makerDetailDTO.getId());
        
        for(int index = 0; index < makerTantoushaDTOList.size(); index++) {
            MakerTantoushaDTO makerTantoushaDTO = makerTantoushaDTOList.get(index);
            TantoushaForm tantoushaForm = new TantoushaForm();
            tantoushaForm.setId(makerTantoushaDTO.getId());
            tantoushaForm.setName(makerTantoushaDTO.getTantoushaName());
            tantoushaForm.setMailAddress(makerTantoushaDTO.getMailAddress());
            tantoushaForm.setBeforeMailAddress(makerTantoushaDTO.getMailAddress());
            detailForm.getTantoushaFormList().put(index + DetailForm.START_TANTOUSHA_LIST_INDEX, tantoushaForm);
        }
	}

    /**
     * 保存するための値をDTOにセットする
     * @param detailForm
     * @param makerDetailDTO
     * @param tantoushaNumber
     */
	private void setHanibaitenDetailFormToDto(DetailForm detailForm, MakerDetailDTO makerDetailDTO, String tantoushaNumber){
		makerDetailDTO.setTokuisakiCode(detailForm.getTokuisakiCode());
		makerDetailDTO.setShouhinjouhouNyuryokuRule(detailForm.getShouhinjouhouNyuryokuRule());
        makerDetailDTO.setMailJushinFlag(detailForm.getMailJushinFlag());

		makerDetailDTO.setCreateTantoushaNumber(tantoushaNumber);
        makerDetailDTO.setCreateDate(null);
		makerDetailDTO.setUpdateTantoushaNumber(tantoushaNumber);
        makerDetailDTO.setUpdateDate(null);

	}
}
