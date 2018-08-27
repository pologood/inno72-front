package com.inno72.service;


import com.inno72.vo.Result;

public interface Inno72FileService {
    /**
     * 肌肤检测
     * @param sessionUUid
     * @param base64Pic
     * @return
     */
	public Result<Object> skindetect(String sessionUUid, String base64Pic);

    /**
     * 获取肌肤检测结果
     * @param sessionUUid
     * @return
     */
    Result<Object> getSkinScore(String sessionUUid);

    /**
     * 上传检测结果图片
     * @param sessionUUid
     * @param base64Pic
     * @return
     */
    Result<Object> upSckinChectPic(String sessionUUid, String base64Pic);

    /**
     * 根据sessionid获取图片
     *
     * @param sessionUUid
     * @return
     */
    Result<Object> getSckinChectPic(String sessionUUid);
}
