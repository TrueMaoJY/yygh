package com.mao.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mao.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 组织架构表(Dict)表服务接口
 *
 * @author makejava
 * @since 2023-02-10 15:58:20
 *
 */
public interface DictService extends IService<Dict> {


    /**
     * @author MAOjy
     * @description //TODO
     * @date 14:40 2023/2/14
     * @param
     * @param id
     * @return java.util.List<com.mao.yygh.model.cmn.Dict>
     */
    List<Dict> findChildData(Long id);
    /**
     * @author MAOjy
     * @description //TODO
     * @date 14:24 2023/2/14
     * @param
     * @param response
     * @return void
     */
    void exportData(HttpServletResponse response);
    /**
     * @author MAOjy
     * @description 导入
     * @date 15:22 2023/2/14
     * @param
     * @param file
     * @return void
     */
    void importData(MultipartFile file);
}

