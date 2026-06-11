package aplication;

import dao.JPAUtil;
import entities.*;
import service.*;

import java.time.LocalDateTime;
import java.util.List;

public class TesteBanco {
    public static void main(String[] args) {
        ClubeService clubeService = new ClubeService();
        CampeonatoService campeonatoService = new CampeonatoService();
        PartidaService partidaService = new PartidaService();
        GrupoService grupoService = new GrupoService();
        ApostaService apostaService = new ApostaService();
        UsuarioService usuarioService = new UsuarioService();

        clubeService.inicializarClubesPadrao();
        List<Clube> clubes = clubeService.listarTodos();
        System.out.println("[OK] Clubes no banco: " + clubes.size());

        Administrador admin = usuarioService.obterAdministrador("Admin Central");
        System.out.println("[OK] Admin id=" + admin.getId());

        Campeonato camp = campeonatoService.criar("Brasileirao Teste", clubes.subList(0, 4));
        System.out.println("[OK] Campeonato id=" + camp.getId());

        Partida partida = partidaService.agendar(camp, clubes.get(0), clubes.get(1),
                LocalDateTime.now().plusHours(2));
        System.out.println("[OK] Partida id=" + partida.getId());

        Grupo grupo = grupoService.criar("Grupo Teste");
        Participante p1 = grupoService.adicionarParticipante(grupo, "Gabriel");
        Participante p2 = grupoService.adicionarParticipante(grupo, "Maria");
        System.out.println("[OK] Grupo id=" + grupo.getId() + " com " + grupo.getParticipantes().size() + " participantes");

        apostaService.apostar(p1, partida, 2, 1);
        apostaService.apostar(p2, partida, 0, 0);
        System.out.println("[OK] Apostas registradas");

        try {
            apostaService.apostar(p1, partida, 3, 3);
            System.out.println("[ERRO] aposta duplicada foi aceita!");
        } catch (RegraDeNegocioException e) {
            System.out.println("[OK] Regra aposta duplicada: " + e.getMessage());
        }

        try {
            partidaService.agendar(camp, clubes.get(0), clubes.get(0), LocalDateTime.now().plusHours(3));
            System.out.println("[ERRO] partida com times iguais foi aceita!");
        } catch (RegraDeNegocioException e) {
            System.out.println("[OK] Regra times iguais: " + e.getMessage());
        }

        partidaService.registrarResultado(admin, partida, 2, 1);
        System.out.println("[OK] Resultado registrado: " + partida.resultado());

        try {
            partidaService.registrarResultado(admin, partida, 5, 5);
            System.out.println("[ERRO] resultado duplo foi aceito!");
        } catch (RegraDeNegocioException e) {
            System.out.println("[OK] Regra resultado unico: " + e.getMessage());
        }

        for (Participante p : grupoService.classificacao(grupo)) {
            System.out.println("[RANKING] " + p.getNome() + " = " + p.calcularPontuacao() + " pts");
        }

        JPAUtil.fechar();
        System.out.println("[FIM] Teste concluido com sucesso.");
    }
}
