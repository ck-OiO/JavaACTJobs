package com.github.ck_oio.javaatcjobs.homework07.bank01.mapper;

import com.github.ck_oio.javaatcjobs.homework07.common.domain.AccountInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AccountInfoMapper {

    @Update("update account_info set frozen_cny = frozen_cny - #{amount} where balance_cny> #{amount} and account_name = #{name}")
    int subCNYBalance(@Param("name") String name, @Param("amount") Double amount);

    @Update("update account_info set balance_cny = balance_cny - #{amount}, frozen_cny = frozen_cny + #{amount} where account_name = #{name}")
    int confirmSubCNY(@Param("name")String name, @Param("amount") Double amount);

    @Update("update account_info set frozen_cny = frozen_cny + #{amount} where account_name = #{name}")
    int cancelSubCNY(@Param("name")String name, @Param("amount") Double amount);


    @Select("select id as 'id',account_name as 'accountName',balance_cny as 'balanceCny'," +
            " balance_usd as 'balanceUsd', balance_cny as 'frozenCny', " +
            "balance_usd as 'balanceUsd' from account_info where account_name = #{name}")
    AccountInfo selectByName(@Param("name") String name);
}
