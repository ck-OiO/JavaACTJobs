package com.github.ck_oio.javaatcjobs.homework07.bank02.mapper;

import com.github.ck_oio.javaatcjobs.homework07.common.domain.AccountInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AccountInfoMapper {
    @Update("update account_info set  frozen_balance = #{frozenBalance} " +
            "where  account_name = #{name}")
    int increaseAccountBalance(@Param("name") String accountName,@Param("frozenBalance") Double frozenBalance);

    @Update("update account_info set  frozen_usd = frozen_usd + #{amount} " +
            "where  account_name = #{name}")
    int addUSDBalance(@Param("name") String name, @Param("amount") Double amount);

    @Update("update account_info set account_balance = account_balance + #{amount} , frozen_usd = frozen_usd - #{amount} ")
    int confirmAddUSD(@Param("name") String name, @Param("amount") Double amount);
    @Update("update account_info set  frozen_usd= frozen_usd-#{amount} " +
            "where  account_name = #{name}")
    int cancelAddUSD(@Param("name") String name, @Param("amount") Double amount);

    @Select("select id as 'id',account_name as 'accountName',balance_cny as 'balanceCny'," +
            " balance_usd as 'balanceUsd', balance_cny as 'frozenCny', " +
            "balance_usd as 'balanceUsd' from account_info where account_name = #{name}")
    AccountInfo selectByName(@Param("name") String name);
}
