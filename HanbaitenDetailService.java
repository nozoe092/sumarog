package com.smalog.service;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smalog.constant.ApplicationConstants;
import com.smalog.dto.HanbaitenDetailDTO;
import com.smalog.form.element.TantoushaForm;
import com.smalog.form.hanbaiten.DetailForm;
import com.smalog.mapper.HanbaitenDetailMapper;
import com.smalog.util.ApplicationUtils;


@Service
public class HanbaitenDetailService implements ApplicationConstants {
	
	@Autowired
	private HanbaitenDetailMapper hanbaitenDetailMapper;

    @Autowired
    private MessageService messageService;

    /**
     * 販売店の情報を取得する
     * @param id
     * @return
     */
    public HanbaitenDetailDTO findById(int id) {
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
    public int insert(HanbaitenDetailDTO hanbaitenDetailDTO) {
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

        int hanbaitenDetailId = detailForm.getId();
		HanbaitenDetailDTO hanbaitenDetailDTO = null;
		boolean isNew = hanbaitenDetailId == 0;
		if(isNew) {
			// 新規登録
			hanbaitenDetailDTO = new HanbaitenDetailDTO();
			// 値を設定する
			setHanibaitenDetailFormToDto(detailForm, hanbaitenDetailDTO, tantoushaNumber);

			int insertId = insert(hanbaitenDetailDTO);
			if(insertId == 0) {
				// 登録失敗
                throw new RuntimeException(messageService.getMessage(MESSAGE_PROPERTY_NAME_ERROR_SAVE_FAILED));
			}
		} else {
			// 更新
			hanbaitenDetailDTO = findById(hanbaitenDetailId);
			if(hanbaitenDetailDTO != null && detailForm.getUpdateDate().equals(hanbaitenDetailDTO.getUpdateDate())){
				// 値を設定する
				setHanibaitenDetailFormToDto(detailForm, hanbaitenDetailDTO, tantoushaNumber);
				int updateCount = update(hanbaitenDetailDTO);
				if(updateCount == 0) {
					// 更新失敗
                    throw new RuntimeException(messageService.getMessage(MESSAGE_PROPERTY_NAME_ERROR_UPDATE_FAILED));
				}
			}else{
				// 排他エラー
				throw new RuntimeException(messageService.getMessage(MESSAGE_PROPERTY_NAME_ERROR_OPTIMISTICLOCK));
			}
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
        
        for(int index = DetailForm.START_TANTOUSHA_LIST_INDEX; index <= DetailForm.MAX_TANTOUSHA_LIST_INDEX; index++) {
            String tantoushaName = ApplicationUtils.callVoidGetterInvoke(hanbaitenDetailDTO, "getTantoushaName" + index, String.class);
            String mailAddress = ApplicationUtils.callVoidGetterInvoke(hanbaitenDetailDTO, "getMailAddress" + index, String.class);
            if(tantoushaName != null && mailAddress != null) {
                TantoushaForm tantoushaForm = new TantoushaForm();
                tantoushaForm.setName(tantoushaName);
                tantoushaForm.setMailAddress(mailAddress);
                detailForm.getTantoushaFormList().put(index, tantoushaForm);
            }
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
		hanbaitenDetailDTO.setShouhinjouhouNyuryokuRule(0); // TODO: 仕様に合わせて値を設定する
        hanbaitenDetailDTO.setMailJushinFlag(detailForm.getMailJushinFlag());

		hanbaitenDetailDTO.setCreateTantoushaNumber(tantoushaNumber);
        hanbaitenDetailDTO.setCreateDate(null);
		hanbaitenDetailDTO.setUpdateTantoushaNumber(tantoushaNumber);
        hanbaitenDetailDTO.setUpdateDate(null);

        // 初期化する
        for(int index = DetailForm.START_TANTOUSHA_LIST_INDEX; index <= DetailForm.MAX_TANTOUSHA_LIST_INDEX; index++) {
            ApplicationUtils.callVoidSetterInvoke(hanbaitenDetailDTO, "setTantoushaName" + index, null);
            ApplicationUtils.callVoidSetterInvoke(hanbaitenDetailDTO, "setMailAddress" + index, null);
        }
   
		Map<Integer, TantoushaForm> tantoushaFormList = detailForm.getTantoushaFormList();
		int index = DetailForm.START_TANTOUSHA_LIST_INDEX;
		for (Integer key : tantoushaFormList.keySet()) {
			TantoushaForm tantoushaForm = tantoushaFormList.get(key);
			ApplicationUtils.callVoidSetterInvoke(hanbaitenDetailDTO, "setTantoushaName" + (index), tantoushaForm.getName());
			ApplicationUtils.callVoidSetterInvoke(hanbaitenDetailDTO, "setMailAddress" + (index), tantoushaForm.getMailAddress());
			index++;
		}
	}
}
