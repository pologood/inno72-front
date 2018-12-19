package com.inno72.service;

public interface Inno72QrCodeService {
    String createQrCode(String qrCodeContent, String localUrl);

    void qrCodeImgDeal(String localUrl, String objectName);
}
