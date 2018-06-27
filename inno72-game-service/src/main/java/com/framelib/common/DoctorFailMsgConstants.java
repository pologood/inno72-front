package com.framelib.common;

/**
 * Created by zhouzengbao on 29/03/2017.
 */
public class DoctorFailMsgConstants {
	
	/**
	 * 成功
	 */
	public static final String SUCCESS = "0000";

    /** 对象不能是空的 */
    public static final String OBJECT_ERROR_NOTNULL = "1000";

    /** 属性不能是空的 */
    public static final String PROPERTY_ERROR_NOTNULL = "2000";
    /** 属性不匹配 */
    public static final String PROPERTY_ERROR_NOTMATCH = "2001";
    /** 字段不符合，相同类型的字段多余制定数量 */
    public static final String PROPERTY_ERROR_TOOMATCH = "2002";

    /** 信息重复 */
    public static final String PROPERTY_ERROR_ISEXIST = "3000";

    /** 数据不存在 */
    public static final String OPTION_ERROR_UPDATENOTEXIST= "4000";
    
    /** 操作失败 */
    public static final String OPTION_ERROR_UPDATEFAIL= "4001";
    
    /** 系统错误 */
    public static final String SYSTEM_ERROR_EXCEPTION= "9999";

}
