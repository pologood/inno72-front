package com.inno72.common;

public class ImageUtil {
    public static String getLongImageUrl(String image){
        String longImage = "";
        if(StringUtil.isNotEmpty(image)){
            image = image.replace(CommonConstants.ALI_OSS,"");
            longImage = CommonConstants.ALI_OSS+image;
        }
        return longImage;
    }

    public static String getLackImageUrl(String image){
        String lackImageUrl = "";
        if(StringUtil.isNotEmpty(image)){
            lackImageUrl = image.replace(CommonConstants.ALI_OSS,"");
        }
        return lackImageUrl;
    }
}
