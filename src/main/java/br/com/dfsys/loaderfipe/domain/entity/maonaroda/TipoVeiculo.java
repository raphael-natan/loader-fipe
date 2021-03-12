package br.com.dfsys.loaderfipe.domain.entity.maonaroda;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "filtro_categoria1")
public class TipoVeiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
}