package com.ecnu.rai.counsel.controller;

import com.ecnu.rai.counsel.common.Result;
import com.ecnu.rai.counsel.mapper.VisitorMapper;
import com.ecnu.rai.counsel.util.VerifyCodeGenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class SMSController {
    @Autowired
    VisitorMapper visitorMapper;

    @PostMapping("/verify")
    public Result verifyCode(@RequestBody List<String> phones){
        String sdkAppId = "1400826946";
        String signName = "个人Java开发技术分享";
        String templateId = "1820500";
        List<String> validPhones = new ArrayList<>();
        List<String> invalidPhones = new ArrayList<>();
        List<String> verifyCodes = new ArrayList<>();
        HashMap<String,String> phoneCode = new HashMap<>();
        for(String phone:phones)
        {

            if(phone.startsWith("+")&&(!phone.startsWith("+86")))
                invalidPhones.add(phone);
            else if(visitorMapper.ifPhoneExist(phone)==0){
                invalidPhones.add(phone);
            }
            else{
                phone = formatPhone(phone);
                validPhones.add(phone);
            }
        }
        if(!invalidPhones.isEmpty())
        return Result.fail("Some phones are invalid.",invalidPhones);

        {

            try {
                String secretId = "AKIDc3GDvObaVBcnjRsDKJ3LOhkY6OQEpAH9";
                String secretKey = "e2kDQN4XkL3zg7IEvPdbOzKPGuhPuCI9";
                Credential cred = new Credential(secretId, secretKey);
                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setSignMethod("HmacSHA256");
                SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
                SendSmsRequest req = new SendSmsRequest();
                req.setSmsSdkAppId(sdkAppId);
                req.setSignName(signName);
                req.setTemplateId(templateId);
                String[] phoneNumberSet = validPhones.toArray(new String[0]);
                for (String s : phoneNumberSet) {
                    String verifyCode = VerifyCodeGenUtil.generateCode(6);
                    verifyCodes.add(verifyCode);
                    phoneCode.put(s, verifyCode);

                }
                String[] templateParamSet = verifyCodes.toArray(new String[0]);
                req.setTemplateParamSet(templateParamSet);
                req.setPhoneNumberSet(phoneNumberSet);
                SendSmsResponse res = client.SendSms(req);
                System.out.println(SendSmsResponse.toJsonString(res));
            }
            catch(TencentCloudSDKException e){
                    e.printStackTrace();
                }
            }



            return Result.success("Already send message to all phones.",phoneCode);


    }
    private String formatPhone(String phone){
        if(phone.startsWith("+86")){
            return phone;
        }
        return "+86"+phone;
    }

}
