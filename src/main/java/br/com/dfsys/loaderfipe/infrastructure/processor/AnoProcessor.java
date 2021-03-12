package br.com.dfsys.loaderfipe.infrastructure.processor;

import br.com.dfsys.loaderfipe.domain.entity.fipe.Veiculo;
import br.com.dfsys.loaderfipe.domain.entity.maonaroda.Ano;
import br.com.dfsys.loaderfipe.domain.repository.MarcaRepository;
import br.com.dfsys.loaderfipe.domain.repository.ModeloRepository;
import br.com.dfsys.loaderfipe.domain.repository.TipoVeiculoRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class AnoProcessor implements ItemProcessor<Veiculo, Ano> {

    @Autowired
    private TipoVeiculoRepository tipoVeiculoRepository;
    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private ModeloRepository modeloRepository;

    private Set<String> anos = new HashSet<>();

    @Override
    public Ano process(Veiculo veiculo) {
        String chaveAno = String.format("%s-%s", veiculo.getModelo(), veiculo.getAno());
        AtomicReference<Ano> ano = new AtomicReference<>();
        if(!anos.contains(chaveAno)) {
            tipoVeiculoRepository.findByNome(veiculo.getTipo())
                    .ifPresent(tipoVeiculo -> {
                        marcaRepository.findByNomeAndIdTipoVeiculo(veiculo.getMarca(), tipoVeiculo.getId())
                                .ifPresent(marca -> {
                                    modeloRepository.findByNomeAndIdMarca(veiculo.getModelo(), marca.getId())
                                            .ifPresent(modelo -> {
                                                anos.add(chaveAno);
                                                ano.set(Ano.builder()
                                                            .idModelo(modelo.getId())
                                                            .nome(veiculo.getAno())
                                                            .build());
                                            });
                                });
                    });


        }
        return ano.get();
    }
}
