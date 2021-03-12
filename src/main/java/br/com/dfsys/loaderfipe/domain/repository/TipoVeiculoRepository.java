package br.com.dfsys.loaderfipe.domain.repository;

import br.com.dfsys.loaderfipe.domain.entity.maonaroda.TipoVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoVeiculoRepository extends JpaRepository<TipoVeiculo, Long> {
    Optional<TipoVeiculo> findByNome(String nome);
}