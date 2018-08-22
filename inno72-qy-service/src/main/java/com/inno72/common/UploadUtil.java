package com.inno72.common;

import com.inno72.oss.OSSUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class UploadUtil {

    /**
     * 上传图片
     * @param file
     * @param type
     * @return
     */
    public static Result<String> uploadImage(MultipartFile file, String type){
        String originalFilename = file.getOriginalFilename();
        String typeName = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        String name =StringUtil.getUUID()+ typeName;
        String path = CommonConstants.OSS_PATH+"/"+type+"/"+name;
        try {
            OSSUtil.uploadByStream(file.getInputStream(),path);
            path = ImageUtil.getLongImageUrl(path);
            return Results.success(path);
        } catch (IOException e) {
            e.printStackTrace();
            return Results.failure("图片处理失败！");
        } catch (Exception e) {
            e.printStackTrace();
            return Results.failure("图片处理失败！");
        }
    }
}
