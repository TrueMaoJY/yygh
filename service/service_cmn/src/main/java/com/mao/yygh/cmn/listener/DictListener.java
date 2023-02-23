package com.mao.yygh.cmn.listener;
/**
 * com.mao.yygh.cmn.listener
 *
 * @author MaoJY
 * @create 2023-02-14-15-13
 */

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mao.yygh.cmn.mapper.DictMapper;
import com.mao.yygh.model.cmn.Dict;
import com.mao.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;

/**
 *@ClassName DictListener
 *@Description TODO
 *@Author MAOjy
 *@DATE 2023/2/14 15:13
 *@Version 1.0
 */
public class DictListener extends AnalysisEventListener<DictEeVo> {
    private DictMapper dictMapper;

    public DictListener(DictMapper dictMapper) {
        this.dictMapper = dictMapper;
    }

    @Override
    public void invoke(DictEeVo dictEeVo, AnalysisContext analysisContext) {
        Dict dict = new Dict();
        BeanUtils.copyProperties(dictEeVo,dict);
        dictMapper.insert(dict);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}