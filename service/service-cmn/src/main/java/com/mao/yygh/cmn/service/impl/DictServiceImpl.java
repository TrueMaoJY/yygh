package com.mao.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mao.yygh.cmn.listener.DictListener;
import com.mao.yygh.cmn.mapper.DictMapper;
import com.mao.yygh.cmn.service.DictService;
import com.mao.yygh.model.cmn.Dict;
import com.mao.yygh.vo.cmn.DictEeVo;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
* Description:
* date: 2023/2/20 10:10
* @author: MaoJY
* @since JDK 1.8
*/
@Service("dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    /**
     * @Author MAOjy
     * @Description //TODO 查询所有parentId=id的记录
     * @Date 14:29 2023/2/11
     * @Param [id]
     * @return [java.lang.Long]
     */
    @Override
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> parentId = new QueryWrapper<Dict>().eq("parent_id", id);
        List<Dict> list = baseMapper.selectList(parentId);
        for (Dict dict : list) {
            if(hasChildren(dict.getId())){
                dict.setHasChildren(true);
            }
        }
        return list;
    }

    @Override
    public void exportData(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("数据字典", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
            List<Dict> list = baseMapper.selectList(null);
            ArrayList<DictEeVo> dictEeVos = new ArrayList<>();
            for (Dict dict : list) {
                DictEeVo dictEeVo = new DictEeVo();
                BeanUtils.copyProperties(dict,dictEeVo,DictEeVo.class);
                dictEeVos.add(dictEeVo);
            }
            //导出
            EasyExcel.write(response.getOutputStream(),DictEeVo.class).sheet("数据字典").doWrite(dictEeVos);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * @author MAOjy
     * @description 引入excel需要监听器
     * @date 10:08 2023/2/20
     * @param
     * @param file
     * @return void
     */
    @CacheEvict(value = "dict",allEntries = true)
    @Override
    public void importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(),DictEeVo.class,new DictListener(baseMapper))
                    .sheet()
                    .doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Cacheable(value = "dict",keyGenerator = "keyGenerator")
    @Override
    public String getNameByParentDictCodeAndValue(String parentDictCode, String value) {
        if (StringUtils.isEmpty(parentDictCode)) {
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("value", value));
            if(dict!=null) {
                return dict.getName();
            }
        }else {
            //根据dict_code查到parent_id
            Dict parentDict = this.getDictByDictCode(parentDictCode);
            if(parentDict==null) {
                return "";
            }
            Dict dict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("parent_id", parentDict.getId())
                    .eq("value", value));
            if (dict != null) {
                return dict.getName();
            }
        }
        return "";
    }
    /**
     * @author MaoJY
     * @description 根据dictcode查询id，查询所有parentId=id的记录
     * @date 15:56 2023/3/16
     * @param [dictcode]
     * @return java.util.List<com.mao.yygh.model.cmn.Dict>
     */
    @Override
    public List<Dict> findByDictCode(String dictcode) {
        Dict dict = this.getDictByDictCode(dictcode);
        if (dict == null) {
            return  null;
        }
        return  this.findChildData(dict.getId());
    }
    private Dict getDictByDictCode(String dictcode){
        Dict parentDict = baseMapper.selectOne(new QueryWrapper<Dict>().eq("dict_code", dictcode));
        return parentDict;
    }
    private boolean hasChildren(Long id){
        QueryWrapper<Dict> parentId = new QueryWrapper<Dict>().eq("parent_id", id);
        return baseMapper.selectCount(parentId)>0;
    }
}

