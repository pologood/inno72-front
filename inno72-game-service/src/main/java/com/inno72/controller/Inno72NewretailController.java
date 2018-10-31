package com.inno72.controller;

import com.inno72.common.Inno72BizException;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.common.util.excel.ExportExcel;
import com.inno72.mapper.*;
import com.inno72.model.Inno72AdminArea;
import com.inno72.model.Inno72Locale;
import com.inno72.model.Inno72Machine;
import com.inno72.model.Inno72MachineDeviceErrorlog;
import com.inno72.service.Inno72NewretailService;
import com.inno72.vo.DeviceVo;
import com.inno72.vo.MachineSellerVo;
import com.inno72.vo.MachineVo;
import com.inno72.vo.UserSessionVo;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.util.StringUtils;
import com.taobao.api.request.SmartstoreDeviceAddRequest;
import com.taobao.api.request.SmartstoreDeviceQueryRequest;
import com.taobao.api.request.SmartstoreStoresQueryRequest;
import com.taobao.api.response.SmartstoreDeviceAddResponse;
import com.taobao.api.response.SmartstoreDeviceQueryResponse;
import com.taobao.api.response.SmartstoreStoresQueryResponse;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/newretail")
public class Inno72NewretailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72NewretailController.class);

	public static final String sessionKey = "6100816bd6f85638abd2fdae18beee05e32809cebf39e224008390433";

	public static final String url = "https://eco.taobao.com/router/rest";

	public static final String appkey = "25101422";

	public static final String secret = "8ac43496a419501705a3dfb20b12dafe";

	private static final Integer ACTIVITYTYPE_PAIYANG = 1;

	@Resource
	private Inno72InteractMachineMapper inno72InteractMachineMapper;


	@Autowired
	private Inno72NewretailService service;

	@Resource
	private Inno72MachineMapper inno72MachineMapper;

	@Resource
	private Inno72LocaleMapper inno72LocaleMapper;

	@Resource
	private Inno72AdminAreaMapper inno72AdminAreaMapper;

    @Resource
    private Inno72ActivityMapper inno72ActivityMapper;

    @Resource
    private Inno72MachineDeviceErrorlogMapper inno72MachineDeviceErrorlogMapper;

	@Value("${sell_session_key}")
	private String sellSessionKey;

    /**
     * 查找门店id
     */
    @RequestMapping(value = "/findStores")
    public Result<Object> findStoreId(String sessionKey, String storeName) {
        try{
            Long storeId = service.findStores(sessionKey,storeName);
            return Results.success(storeId);
        }catch(Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch(Exception e){
            LOGGER.error("findStoreId",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/saveMachine",method = RequestMethod.POST)
    public Result<Object> saveMachine(@RequestBody List<DeviceVo> vo) {
        try{
            service.saveMachine(vo);
            return Results.success();
        }catch(Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch(Exception e){
            LOGGER.error("saveMachine",e);
            return Results.failure("系统异常");
        }
    }
    /**
     * 新增机器
     * @param sessionKey
     * @param deviceName 设备名称
     * @param storeId 门店id
     * @param osType 操作系统类型：
     *               WINDOWS("WINDOWS", "WINDOWS"),
     *               ANDROID("ANDROID", "ANDROID"),
     *               IOS("IOS", "IOS"), LINUX("LINUX", "LINUX"), OTHER("OTHER", "OTHER");
     * @param deviceType 设备类型：
     *                   CAMERA("CAMERA", "客流摄像头"), SHELF("SHELF", "云货架"),
     *                   MAKEUP_MIRROR("MAKEUP_MIRROR", "试妆镜"), FITTING_MIRROR("FITTING_MIRROR", "试衣镜"),
     *                   VENDOR("VENDOR", "售货机"), WIFI("WIFI","WIFI探针"), SAMPLE_MACHINE("SAMPLE_MACHINE","派样机"),
     *                   DOLL_MACHINE("DOLL_MACHINE", "娃娃机"), INTERACTIVE_PHOTO("INTERACTIVE_PHOTO", "互动拍照"),
     *                   INTERACTIVE_GAME("INTERACTIVE_GAME", "互动游戏"), USHER_SCREEN("USHER_SCREEN", "智慧迎宾屏"),
     *                   DRESSING("DRESSING", "闪电换装"), MAGIC_MIRROR("MAGIC_MIRROR", "百搭魔镜"),
     *                   SHOES_FITTING_MIRROR("SHOES_FITTING_MIRROR", "试鞋镜"), SKIN_DETECTION("SKIN_DETECTION", "肌肤测试仪"),
     *                   FOOT_DETECTION("FOOT_DETECTION", "测脚仪"),
     *                   RFID_SENSOR("RFID_SENSOR", "RFID"),touch_machine("touch_machine","导购一体屏")
     * @param outerCode  商家自定义设备编码
     */
    @RequestMapping(value = "/saveDevice",method = RequestMethod.POST)
    public Result<Object> saveDevice(String sessionKey, String deviceName, Long storeId, String osType, String deviceType, String outerCode) {
        try{
            String deviceCode = service.saveDevice(sessionKey,deviceName,storeId,osType,outerCode);
            return Results.success(deviceCode);
        }catch(Inno72BizException e){
            return Results.failure(e.getMessage());
        }catch(Exception e){
            LOGGER.error("saveDevice",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/getMemberIdentity")
    public Result<Object> getMemberIdentity(String sessionKey, String mixNick) {
        try{
            boolean flag =  service.getMemberIdentity(sessionKey,mixNick);
            return Results.success(flag);
        }catch(Exception e){
            LOGGER.error("getMemberIdentity",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/getStoreMemberurl")
    public Result<Object> getStoreMemberurl(String sessionKey, String deviceCode,String callbackUrl) {
        try{
            String url =  service.getStoreMemberurl(sessionKey,deviceCode,callbackUrl);
            return Results.success(url);
        }catch(Exception e){
            LOGGER.error("getStoreMemberurl",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/deviceVendorFeedback")
    public Result<Object> deviceVendorFeedback(String sessionKey, String tradeNo,
                                               String tradeType, String deviceCode, String action
            , String itemId, String couponId, String userNick, String outerBizId, String opTime, String outerUser)  {
        try{
            service.deviceVendorFeedback(sessionKey,tradeNo,tradeType,deviceCode,action,
                    itemId,couponId,userNick,outerBizId,opTime,outerUser);
            return Results.success();
        }catch(Exception e){
            LOGGER.error("deviceVendorFeedback",e);
            return Results.failure("系统异常");
        }
    }

    @RequestMapping(value = "/errorlog")
    public Result<Object> exportShop(String bizid) throws Exception {
        Inno72MachineDeviceErrorlog log = new Inno72MachineDeviceErrorlog();
        log.setBizid(bizid);
        List<Inno72MachineDeviceErrorlog> list = inno72MachineDeviceErrorlogMapper.select(log);
        return Results.success(list);
    }

	@RequestMapping(value = "/exportShop")
	public void exportShop(String activityId,Integer activityType, HttpServletResponse response) throws Exception {
        List<MachineSellerVo> list = null;
		if(ACTIVITYTYPE_PAIYANG == activityType){
			//派样
			list = inno72InteractMachineMapper.findMachineIdAndSellerId(activityId);
		}else{
			//互动
            List<String> machinelist = inno72ActivityMapper.selectMachineCodeByActivityId(activityId);
            List<String> sellerlist = inno72ActivityMapper.selectSellerIdByActivityId(activityId);
            list = buildMachineSellerVoList(machinelist,sellerlist);
		}
        exportShop(list,response);
	}

    private List<MachineSellerVo> buildMachineSellerVoList(List<String> machinelist, List<String> sellerlist) {
        if(machinelist!=null&&machinelist.size()>0&&sellerlist!=null&&sellerlist.size()>0){
            List<MachineSellerVo> list = new ArrayList<MachineSellerVo>();
            for(String machineCode:machinelist){
                for(String sellerId:sellerlist){
                    if(!StringUtils.isEmpty(machineCode)&&!StringUtils.isEmpty(sellerId)){
                        MachineSellerVo vo = new MachineSellerVo();
                        vo.setMachineCode(machineCode);
                        vo.setSellerId(sellerId);
                        list.add(vo);
                    }else{
                        LOGGER.error("machineCode={},sellerId={}",machineCode,sellerId);
                    }
                }
            }
            return list;
        }
        return null;
    }

    private void exportShop(List<MachineSellerVo> list, HttpServletResponse response) throws Exception {
    	if(list!=null && list.size()>0){
			List<String> headerList = buildHeaderList();
			ExportExcel excelShop = new ExportExcel("", headerList);
            Set<String> cacheSet = new HashSet();
    		for(MachineSellerVo machineSellerVo:list){
    		    if(!StringUtils.isEmpty(machineSellerVo.getMachineCode())&& !StringUtils.isEmpty(machineSellerVo.getSellerId())){
                    String shopName = machineSellerVo.getSellerId() + "-" + machineSellerVo.getMachineCode();
                    if(!cacheSet.contains(shopName)){
                        Inno72Machine inno72Machine = inno72MachineMapper.findMachineByCode(machineSellerVo.getMachineCode());
                        String localeId = inno72Machine.getLocaleId();
                        Inno72Locale inno72Locale = inno72LocaleMapper.selectByPrimaryKey(localeId);
                        String areaCode = inno72Locale.getAreaCode();
                        Inno72AdminArea inno72AdminArea = inno72AdminAreaMapper.selectByPrimaryKey(areaCode);

                        String province = inno72AdminArea.getProvince();
                        String city = inno72AdminArea.getCity();
                        String locale = inno72Locale.getName();

                        Long storeid =null;
                        try{
                            storeid = service.findStores(sellSessionKey,shopName);
                        }catch (Inno72BizException e){
                            LOGGER.error("查找门店异常{}",e.getMessage());
                        }catch (Exception e){
                            throw e;
                        }
                        if(storeid == null){
                            Row row = excelShop.addRow();
                            excelShop.addCell(row, 0, province); // 省
                            excelShop.addCell(row, 1, city); // 市
                            excelShop.addCell(row, 6, shopName); // 店名
                            excelShop.addCell(row, 9, locale); // 地址
                            excelShop.addCell(row, 10, getTel()); // 联系电话
                            excelShop.addCell(row, 12, "https://img.alicdn.com/top/i1/TB1Xb0QXjfguuRjy1zewu20KFXa.png"); // 图片地址
                            excelShop.addCell(row, 15, "09:00-22:00"); // 营业时间
                        }
                        cacheSet.add(shopName);
                    }
                }else{
    		        LOGGER.error("数据异常machineCode = {},sellerId= {}",machineSellerVo.getMachineCode(),machineSellerVo.getSellerId());
                }

			}
			try {
				excelShop.write(response, "门店.xlsx").dispose();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 构建sheet表头
	 */
	private List<String> buildHeaderList() {
		List<String> headerList = new ArrayList<String>();
		headerList.add("省份名(prov_name)");
		headerList.add("市名称(city_name)");
		headerList.add("区域名称(area_name)");
		headerList.add("街道名称(street_name)");
		headerList.add("品牌名称(brand_name)");
		headerList.add("商家编码(out_id)");
		headerList.add("店名(store_name)");
		headerList.add("分店名(sub_store_name)");
		headerList.add("展示名称(display)");
		headerList.add("地址(address)");
		headerList.add("联系电话(contact)");
		headerList.add("咨询电话(hotline)");
		headerList.add("图片地址(pic_addr)");
		headerList.add("支付宝账号(alipay_account)");
		headerList.add("支付宝实名(alipay_real_name)");
		headerList.add("营业时间(bustime)");
		headerList.add("营业时间描述(bustime_desc)");
		headerList.add("门店旺旺(wangwang)");
		headerList.add("门店邮箱(email)");
		headerList.add("核销账号(write_off_account)");
		headerList.add("法人姓名(legal_person_name)");
		headerList.add("法人身份证号(legal_cert_no)");
		headerList.add("营业主体名称(license_name)");
		headerList.add("营业主体类型(license_type)");
		headerList.add("营业执照编号(license_code)");
		return headerList;
	}

	/**
	 * 返回手机号码
	 */
	private static String[] telFirst = "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153"
			.split(",");

	private static String getTel() {
		int index = getNum(0, telFirst.length - 1);
		String first = telFirst[index];
		String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
		String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
		return first + second + third;
	}

	public static int getNum(int start, int end) {
		return (int) (Math.random() * (end - start + 1) + start);
	}


}
