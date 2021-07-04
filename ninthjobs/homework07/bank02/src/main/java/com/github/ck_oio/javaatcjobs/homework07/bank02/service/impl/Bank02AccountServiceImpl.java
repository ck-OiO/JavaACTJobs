package com.github.ck_oio.javaatcjobs.homework07.bank02.service.impl;

import com.github.ck_oio.javaatcjobs.homework07.bank02.mapper.AccountInfoMapper;
import com.github.ck_oio.javaatcjobs.homework07.common.domain.AccountInfo;
import com.github.ck_oio.javaatcjobs.homework07.common.service.Bank02AccountService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bank02AccountService")
@Slf4j
public class Bank02AccountServiceImpl implements Bank02AccountService {

    @Autowired
    private AccountInfoMapper accountInfoMapper;


    @Override
    public AccountInfo selectByName(String name) {
        return accountInfoMapper.selectByName(name);
    }

    @Override
    @Transactional
    @HmilyTCC(confirmMethod = "confirmMth", cancelMethod = "cancelMth")
    public Boolean addUSDBalance(String accountName, Double amount) {
        accountInfoMapper.addUSDBalance(accountName, amount);
        log.info("******** Bank02 Service Begin try ...");
        return Boolean.TRUE;

    }

    public void confirmMth(String accountName, Double amount) {
        accountInfoMapper.confirmAddUSD(accountName, amount);
        log.info("******** Bank02 Service commit...  ");
    }

    public void cancelMth(String accountName, Double amount) {
        accountInfoMapper.cancelAddUSD(accountName, amount);
        log.info("******** Bank02 Service  cancel...  ");

    }
}
