package com.example.backend.service;

import java.math.BigDecimal;

public interface TransferPort {
	void transfer(Long fromId, Long toId, BigDecimal amount);
}


