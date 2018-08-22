package com.inno72.check.service;

import com.inno72.check.model.Inno72CheckUser;
import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.common.SessionData;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

public interface CheckUserService extends Service<Inno72CheckUser> {

    Result<String> smsCode(String phone);

    Result<SessionData> login(String phone, String smsCode);

    Result<String> upload(MultipartFile file);

    Result<Inno72CheckUser> updateUser(Inno72CheckUser inno72CheckUser);

    Result<String> logout();
}
