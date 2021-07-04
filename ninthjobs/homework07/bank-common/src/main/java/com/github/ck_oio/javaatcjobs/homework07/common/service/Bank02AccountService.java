package com.github.ck_oio.javaatcjobs.homework07.common.service;

public interface Bank02AccountService extends BankAccountService{
    Boolean addUSDBalance(String name, Double amount);
}
