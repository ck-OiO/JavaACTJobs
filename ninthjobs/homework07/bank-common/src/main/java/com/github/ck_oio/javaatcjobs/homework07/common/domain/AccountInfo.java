package com.github.ck_oio.javaatcjobs.homework07.common.domain;

import lombok.Data;

@Data
public class AccountInfo {
	private Long id;
	private String acountName;
	private Double balanceCny;
	private Double balanceUsd;
	private Double frozenCny;
	private Double frozenUsd;

}