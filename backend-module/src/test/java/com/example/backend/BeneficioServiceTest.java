package com.example.backend;

import com.example.backend.domain.Beneficio;
import com.example.backend.service.BeneficioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeneficioServiceTest {

	@Autowired
	private BeneficioService service;

	@Test
	@Transactional
	void transferSuccess() {
		Beneficio a = new Beneficio();
		a.setNome("A");
		a.setDescricao("A");
		a.setValor(new BigDecimal("1000.00"));
		a.setAtivo(true);
		a = service.create(a);

		Beneficio b = new Beneficio();
		b.setNome("B");
		b.setDescricao("B");
		b.setValor(new BigDecimal("500.00"));
		b.setAtivo(true);
		b = service.create(b);

		service.transfer(a.getId(), b.getId(), new BigDecimal("200.00"));

		Beneficio aAfter = service.getById(a.getId());
		Beneficio bAfter = service.getById(b.getId());

		assertThat(aAfter.getValor()).isEqualByComparingTo("800.00");
		assertThat(bAfter.getValor()).isEqualByComparingTo("700.00");
	}

	@Test
	@Transactional
	void transferInsufficientFunds() {
		Beneficio a = new Beneficio();
		a.setNome("A");
		a.setDescricao("A");
		a.setValor(new BigDecimal("100.00"));
		a.setAtivo(true);
		a = service.create(a);

		Beneficio b = new Beneficio();
		b.setNome("B");
		b.setDescricao("B");
		b.setValor(new BigDecimal("500.00"));
		b.setAtivo(true);
		b = service.create(b);

		final Long aId = a.getId();
		final Long bId = b.getId();
		assertThrows(IllegalStateException.class, () -> service.transfer(aId, bId, new BigDecimal("200.00")));
	}
}


