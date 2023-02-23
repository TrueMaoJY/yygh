package com.mao.yygh.cmn.controller;



import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mao.yygh.common.result.Result;
import com.mao.yygh.cmn.service.DictService;
import com.mao.yygh.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.List;

/**
 * 组织架构表(Dict)表控制层
 *
 * @author makejava
 * @since 2023-02-10 15:58:20
 */
@Api(tags = "数据字典")
@RestController
@RequestMapping("/admin/cmn/dict")
@CrossOrigin
public class DictController  {
    /**
     * 服务对象
     */
    @Resource
    private DictService dictService;

    @ApiOperation(value = "数据字典导入")
    @PostMapping("/importData")
    public void importData(MultipartFile file){
        dictService.importData(file);
    }
    @ApiOperation(value = "数据字典导出")
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        dictService.exportData(response);
    }
    /**
    * Description:根据 id查询 子数据列表
    * date: 2023/2/11 14:15
    * @author: MaoJY
    * @since JDK 1.8
    */
    @ApiOperation(value = "根据id查询子数据列表")
    @GetMapping("/findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
       List<Dict> list= dictService.findChildData(id);
        return Result.ok(list);
    }

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param dict 查询实体
     * @return 所有数据
     */
    @GetMapping
    public Result selectAll(Page<Dict> page, Dict dict) {
        return Result.ok(this.dictService.page(page, new QueryWrapper<>(dict)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public Result selectOne(@PathVariable Serializable id) {
        return Result.ok(this.dictService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param dict 实体对象
     * @return 新增结果
     */
    @PostMapping
    public Result insert(@RequestBody Dict dict) {
        return Result.ok(this.dictService.save(dict));
    }

    /**
     * 修改数据
     *
     * @param dict 实体对象
     * @return 修改结果
     */
    @PutMapping
    public Result update(@RequestBody Dict dict) {
        return Result.ok(this.dictService.updateById(dict));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public Result delete(@RequestParam("idList") List<Long> idList) {
        return Result.ok(this.dictService.removeByIds(idList));
    }
}

