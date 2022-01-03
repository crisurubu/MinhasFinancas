package com.crisurubu.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crisurubu.minhasfinancas.model.entities.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	

}
