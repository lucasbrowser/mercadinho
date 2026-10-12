package br.com.mercadinho.nuvem.controle;

import java.time.Instant;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/api/health")
    public Map<String, Object> consultarSaude() {
        return Map.of("estado", "ATIVO", "aplicacao", "api-nuvem", "instante", Instant.now().toString());
    }
}
