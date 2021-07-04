package com.github.ck_oio.javaatcjobs.homework07.common.service;

import com.github.ck_oio.javaatcjobs.homework07.common.domain.AccountInfo;

public interface BankAccountService {
    AccountInfo selectByName(String name);
}
