package com.inno72.service.impl;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.util.QrCodeUtil;
import com.inno72.oss.OSSUtil;
import com.inno72.service.Inno72QrCodeService;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
public class Inno72QrCodeServiceImpl implements Inno72QrCodeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72QrCodeServiceImpl.class);
    @Resource
    private Inno72GameServiceProperties inno72GameServiceProperties;
    /**
     * 生成二维码
     * @return
     */
    @Override
    public String createQrCode(String qrCodeContent, String localUrl) {

        LOGGER.info("二维码访问 qrCodeContent is {} ", qrCodeContent);

        // 存储在阿里云上的文件名
        String objectName = "qrcode/" + localUrl;

        // 提供给前端用来调用二维码的地址
        String returnUrl = inno72GameServiceProperties.get("returnUrl") + objectName;

        boolean result = false;

        try {
            result = QrCodeUtil.createQrCode(localUrl, qrCodeContent, 1800, "png");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        if (result) {
            this.qrCodeImgDeal(localUrl, objectName);
        }

        return returnUrl;
    }

    /**
     * 二维图片码处理
     */
    @Override
    public void qrCodeImgDeal(String localUrl, String objectName) {
        try {
            File f = new File(localUrl);
            if (f.exists()) {
                // 压缩图片 1

                Thumbnails.of(localUrl).scale(0.5f).outputQuality(0f).toFile(localUrl);
                // 上传阿里云 2
                OSSUtil.uploadLocalFile(localUrl, objectName);
                // 删除本地文件
                f.delete();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
