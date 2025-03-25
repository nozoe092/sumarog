package com.smalog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smalog.constant.DBKeyLabelConstants;
import com.smalog.dto.MakerDTO;
import com.smalog.mapper.MakerMapper;

@Service
public class MakerService {

    @Autowired
    private MakerMapper makerMapper;

    /**
     * メーカーの情報を取得する
     * @param hanbaiten
     * @param makerCode
     * @param name
     * @param category
     * @return
     */
    public List<MakerDTO> getMakerList(String hanbaiten, String makerCode, String name, String category) {
        return makerMapper.getMakerList(DBKeyLabelConstants.KAISHA_KUBUN_CODE_MAKER.getStringValue(), hanbaiten, makerCode, name, category);
    }
}