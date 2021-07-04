package com.github.ck_oio.javaatcjobs.homework07.bank01.service.impl;

import com.github.ck_oio.javaatcjobs.homework07.bank01.mapper.AccountInfoMapper;
import com.github.ck_oio.javaatcjobs.homework07.common.Constant;
import com.github.ck_oio.javaatcjobs.homework07.common.domain.AccountInfo;
import com.github.ck_oio.javaatcjobs.homework07.common.service.Bank01AccountService;
import com.github.ck_oio.javaatcjobs.homework07.common.service.Bank02AccountService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bank01AccountService")
@Slf4j
public class Bank01AccountServiceImpl implements Bank01AccountService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;

    @Autowired
    private Bank02AccountService bank02AccountService;

    @Override
    @Transactional
    @HmilyTCC(confirmMethod = "confirmMth", cancelMethod = "cancelMth")
    public Boolean subCNYBalance(String name, Double amount) {

        //从账户扣减
        if (accountInfoMapper.subCNYBalance(name, amount) <= 0) {
            //扣减失败
            throw new HmilyRuntimeException("bank01 exception，扣减失败");
        }
        amount %= Constant.CNY2USD;
        //远程调用bank02
        if (!bank02AccountService.addUSDBalance("ls", amount)) {
            throw new HmilyRuntimeException("bank02增加存款失败");
        }

        log.info("******** Bank01 Service  end try...  ");

        return Boolean.TRUE;
    }

    @Override
    public AccountInfo selectByName(String accountName) {
        return accountInfoMapper.selectByName(accountName);
    }


    public boolean confirmMth(String name, Double amount) {
        int result = accountInfoMapper.confirmSubCNY(name, amount);
        log.info("******** Bank01 Service begin commit...");
        return result > 0;
    }

    public boolean cancelMth(String name, Double amount) {
        int result = accountInfoMapper.cancelSubCNY(name, amount);
        log.info("******** Bank01 Service end rollback...  ");
        return result > 0;
    }

}
