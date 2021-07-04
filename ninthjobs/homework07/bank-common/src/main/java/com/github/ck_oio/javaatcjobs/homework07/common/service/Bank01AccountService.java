package com.github.ck_oio.javaatcjobs.homework07.common.service;

public interface Bank01AccountService extends BankAccountService{
    Boolean subCNYBalance(String name, Double amount);
}
