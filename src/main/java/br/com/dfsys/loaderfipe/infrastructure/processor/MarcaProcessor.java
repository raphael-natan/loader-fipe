package br.com.dfsys.loaderfipe.infrastructure.processor;

import br.com.dfsys.loaderfipe.domain.entity.fipe.Veiculo;
import br.com.dfsys.loaderfipe.domain.entity.maonaroda.Marca;
import br.com.dfsys.loaderfipe.domain.repository.MarcaRepository;
import br.com.dfsys.loaderfipe.domain.repository.TipoVeiculoRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class MarcaProcessor implements ItemProcessor<Veiculo, Marca> {

    @Autowired
    private TipoVeiculoRepository tipoVeiculoRepository;
    @Autowired
    private MarcaRepository marcaRepository;

    private Set<String> marcas = new HashSet<>();

    @Override
    public Marca process(Veiculo veiculo) {
        AtomicReference<Marca> marca = new AtomicReference<>();
        if(!marcas.contains(veiculo.getMarca())){
            tipoVeiculoRepository.findByNome(veiculo.getTipo())
                    .ifPresent(tipoVeiculo -> {
                        if(!marcaRepository.existsByNomeAndIdTipoVeiculo(veiculo.getMarca(), tipoVeiculo.getId())){
                            marcas.add(veiculo.getMarca());
                            marca.set(Marca.builder()
                                            .nome(veiculo.getMarca())
                                            .idTipoVeiculo(tipoVeiculo.getId())
                                            .build());
                        }
            });
        }
        return marca.get();
    }
}
