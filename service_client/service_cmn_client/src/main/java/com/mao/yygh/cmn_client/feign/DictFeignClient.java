package com.mao.yygh.cmn_client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @InterfaceName DictFeignClient
 * @Description TODO 远程调用service_cmn的服务
 * @Author MAOjy
 * @DATE 2023/3/16 9:11
 * @Version 1.0
 */
@FeignClient("service-cmn")
public interface DictFeignClient {
    /**
     * @author MAOjy
     * @description 根据parentDictCode and value获取数据字典名称
     * @date 9:14 2023/3/16
     * @param
     * @param parentDictCode
     * @param value
     * @return java.lang.String
     */
    @GetMapping("/admin/cmn/dict/getName/{parentDictCode}/{value}")
    @ResponseBody
    String getName(@PathVariable("parentDictCode")String parentDictCode,
                   @PathVariable("value")String value);
  
    /**
     * @author MaoJY
     * @description
     * @date 9:37 2023/3/16
     * @param [value]
     * @return java.lang.String
     */
    @GetMapping("/admin/cmn/dict/getName/{value}")
    @ResponseBody
    String getName(@PathVariable("value")String value);
}
