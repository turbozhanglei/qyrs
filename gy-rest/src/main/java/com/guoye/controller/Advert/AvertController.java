package com.guoye.controller.Advert;

import com.google.common.collect.Lists;
import com.guoye.base.BizAction;
import com.guoye.util.BaseResult;
import com.guoye.util.StatusConstant;
import org.g4studio.core.metatype.Dto;
import org.g4studio.core.metatype.impl.BaseDto;
import org.g4studio.core.resource.util.StringUtils;
import org.g4studio.core.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Autor:zhaowei
* @Date: 2020/2/11 16:12
* @Description:
*/
@RestController
@RequestMapping("/advert")
public class AvertController extends BizAction {

    @ResponseBody
    @RequestMapping(value = "/saveInfo")
    public BaseResult saveInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }
            dto.put("tableName", "adCode");
            //id 不为空则修改
            if (StringUtils.isNotEmpty(dto.getAsString("id"))){
                String type = dto.getAsString("type");
                if (StringUtils.isNotEmpty(type) && type.indexOf("1") < 0){
                    dto.put("width",null);
                    dto.put("height",null);
                }
                dto.put("update_time",new Date());
                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.updateInfo(dto);
            }else {
                //插入
                dto.put("creator", member == null ? "" : member.get("id"));
                dto.put("updator", member == null ? "" : member.get("id"));
                bizService.saveInfo(dto);
            }
        }catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    /**
    * @Autor:zhaowei
    * @Date: 2020/2/11 17:32
    * @Description: 查询广告位详情
    */
    @ResponseBody
    @RequestMapping(value = "/getInfo")
    public BaseResult getInfo(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Dto member = redisService.getObject(dto.getAsString("token"), BaseDto.class);

            if (null == member) {
                result.setCode(StatusConstant.CODE_4000);
                result.setMsg("请登录");
                return result;
            }

            Dto adCode = (BaseDto) bizService.queryForObject("adCode.getInfo", dto);
            if (adCode != null && adCode.getAsLong("id") != null) {
                result.setData(adCode);
            }
        }catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getAdSource")
    public BaseResult getAdSource(HttpServletRequest request, HttpServletResponse response) {
        BaseResult result = new BaseResult();
        Dto dto = WebUtils.getParamAsDto(request);
        try {
            Map<String,List> resultMap = new HashMap();

            String adCodeStr = dto.getAsString("adCode");
            if (StringUtils.isNotEmpty(adCodeStr)){
                List<String> adCodes = Lists.newArrayList();
                if (adCodeStr.indexOf(",") > 0){
                    adCodes = Arrays.asList(adCodeStr.split(","));
                }else {
                    adCodes.add(adCodeStr);
                }
                dto.put("adCodes",adCodes);
                //先获取广告位列表
                List<Dto> adList = (List<Dto>) bizService.queryList("adCode.queryAdList", dto);
                if (!adList.isEmpty()){
                    for (Dto ad :adList){
                        Dto adSourceDto = new BaseDto();
                        adSourceDto.put("adCodeId",ad.getAsString("id"));
                        adSourceDto.put("limit",ad.getAsInteger("num") == null ? '0' :ad.getAsInteger("num"));
                        adSourceDto.put("status","0");
                        //根据具体广告位id与限制个数查询生效中的素材列表
                        List<Dto> adSourceList = (List<Dto>) bizService.queryList("adSource.queryAdSourceList", adSourceDto);
                        List<Dto> restSourceList = Lists.newArrayList();
                        if (!adSourceList.isEmpty()){
                            for (Dto adSource : adSourceList){
                                Dto source = new BaseDto();
                                source.put("id",adSource.getAsInteger("id"));
                                source.put("name",adSource.getAsString("name"));
                                source.put("type",adSource.getAsString("type"));
                                source.put("content",adSource.getAsString("content"));
                                source.put("imageUrl",adSource.getAsString("image_url"));
                                source.put("linkUrl",adSource.getAsString("link_url"));
                                source.put("startDate",dateToStamp(adSource.getAsString("start_date")));
                                source.put("endDate",dateToStamp(adSource.getAsString("end_date")));
                                source.put("sort",adSource.getAsInteger("sort"));
                                source.put("refType",adSource.getAsInteger("ref_type"));
                                restSourceList.add(source);
                            }
                        }
                        resultMap.put(ad.getAsString("code"),restSourceList);
                    }

                }
                for (String adCode : adCodes){
                    if (resultMap.isEmpty() || resultMap.get(adCode) == null){
                        resultMap.put(adCode,Lists.newArrayList());
                    }
                }
                result.setData(resultMap);
            }

        }catch (Exception e) {
            e.printStackTrace();
            result = reduceErr(e.getLocalizedMessage());
        }
        return result;
    }

    public static long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }
}
