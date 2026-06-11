package aplication;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dao.JPAUtil;
import entities.*;
import service.*;

public class SistemaCompleto extends JFrame {

    private final ClubeService clubeService = new ClubeService();
    private final CampeonatoService campeonatoService = new CampeonatoService();
    private final PartidaService partidaService = new PartidaService();
    private final GrupoService grupoService = new GrupoService();
    private final ApostaService apostaService = new ApostaService();
    private final UsuarioService usuarioService = new UsuarioService();

    private final Administrador admin;

    private final DefaultListModel<String> modelClubes = new DefaultListModel<>();
    private final DefaultListModel<String> modelCampeonatos = new DefaultListModel<>();
    private final JTextArea areaRanking = new JTextArea(10, 30);

    public SistemaCompleto() {
        setTitle("Sistema de Apostas Esportivas");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Carrega/cria os dados persistidos no H2
        clubeService.inicializarClubesPadrao();
        admin = usuarioService.obterAdministrador("Admin Central");
        carregarDadosDoBanco();

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Administração", criarPainelAdmin());
        abas.addTab("Grupos e Apostas", criarPainelParticipante());

        add(abas);
        atualizarRankingInterface();
    }

    private void carregarDadosDoBanco() {
        modelClubes.clear();
        for (Clube c : clubeService.listarTodos()) {
            modelClubes.addElement(c.getNome());
        }
        modelCampeonatos.clear();
        for (Campeonato c : campeonatoService.listarTodos()) {
            modelCampeonatos.addElement(c.getNome());
        }
    }

    // Trecho atualizado do Painel Administrativo
    private JPanel criarPainelAdmin() {
        // Usamos um layout de 3 colunas, mas com margens maiores
        JPanel painel = new JPanel(new GridLayout(1, 3, 20, 20));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Coluna 1: Clubes
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(BorderFactory.createTitledBorder("1. Clubes"));
        p1.add(new JScrollPane(new JList<>(modelClubes)), BorderLayout.CENTER);

        // Coluna 2: Campeonatos
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(BorderFactory.createTitledBorder("2. Campeonatos"));
        p2.add(new JScrollPane(new JList<>(modelCampeonatos)), BorderLayout.CENTER);
        JButton btnNovoCamp = new JButton("Criar Campeonato");
        p2.add(btnNovoCamp, BorderLayout.SOUTH);

        // Coluna 3: Partidas (Aqui diminuímos os botões)
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER)); // FlowLayout não estica o botão
        p3.setBorder(BorderFactory.createTitledBorder("3. Ações de Partida"));

        JButton btnPartida = new JButton("Nova Partida");
        JButton btnResult = new JButton("Lançar Placar");

        // Definindo um tamanho menor e fixo para os botões
        Dimension btnSize = new Dimension(150, 30);
        btnPartida.setPreferredSize(btnSize);
        btnResult.setPreferredSize(btnSize);

        p3.add(btnPartida);
        p3.add(btnResult);

        painel.add(p1); painel.add(p2); painel.add(p3);

        btnPartida.addActionListener(e -> registrarPartida());
        btnResult.addActionListener(e -> lancarResultado());
        btnNovoCamp.addActionListener(e -> criarCampeonato());

        return painel;
    }

    private JPanel criarPainelParticipante() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));

        JPanel pBotoes = new JPanel(new FlowLayout());
        JButton btnGrupo = new JButton("Criar Grupo");
        JButton btnEntrar = new JButton("Entrar em Grupo");
        JButton btnApostar = new JButton("FAZER APOSTA");

        pBotoes.add(btnGrupo); pBotoes.add(btnEntrar); pBotoes.add(btnApostar);

        areaRanking.setEditable(false);
        areaRanking.setBorder(BorderFactory.createTitledBorder("Classificação Geral dos Grupos"));

        painel.add(pBotoes, BorderLayout.NORTH);
        painel.add(new JScrollPane(areaRanking), BorderLayout.CENTER);

        btnGrupo.addActionListener(e -> criarGrupo());
        btnEntrar.addActionListener(e -> entrarEmGrupo());
        btnApostar.addActionListener(e -> realizarAposta());

        return painel;
    }

    private void criarGrupo() {
        String nome = JOptionPane.showInputDialog("Nome do Grupo:");
        if (nome == null) return;
        try {
            grupoService.criar(nome);
            atualizarRankingInterface();
            JOptionPane.showMessageDialog(this, "Grupo criado!");
        } catch (RegraDeNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // --- REQUISITO: CADASTRO OBRIGATÓRIO DE HORÁRIO ---
    private void registrarPartida() {
        List<Campeonato> campeonatos = campeonatoService.listarTodos();
        if (campeonatos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Crie um campeonato primeiro.");
            return;
        }
        Campeonato camp = (Campeonato) JOptionPane.showInputDialog(this, "Selecione o Campeonato", "Partida",
                JOptionPane.QUESTION_MESSAGE, null, campeonatos.toArray(), campeonatos.get(0));

        if (camp == null) return;

        Clube c1 = (Clube) JOptionPane.showInputDialog(this, "Time A", "Partida",
                JOptionPane.QUESTION_MESSAGE, null, camp.getClubes().toArray(), camp.getClubes().get(0));
        Clube c2 = (Clube) JOptionPane.showInputDialog(this, "Time B", "Partida",
                JOptionPane.QUESTION_MESSAGE, null, camp.getClubes().toArray(), camp.getClubes().get(1));

        String dataString = JOptionPane.showInputDialog(this, "Horário da Partida (dd/MM/yyyy HH:mm):",
                LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        if (dataString == null) return;

        LocalDateTime horario;
        try {
            horario = LocalDateTime.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERRO: O horário é obrigatório e deve seguir o formato dd/MM/yyyy HH:mm");
            return;
        }

        try {
            partidaService.agendar(camp, c1, c2, horario);
            JOptionPane.showMessageDialog(this, "Partida agendada com sucesso!");
        } catch (RegraDeNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    // --- REQUISITO: APOSTA SOMENTE ATÉ 20 MINUTOS ANTES ---
    private void realizarAposta() {
        Grupo g = selecionarGrupo();
        if (g == null) return;
        if (g.getParticipantes().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Este grupo ainda não tem participantes.");
            return;
        }

        Participante part = (Participante) JOptionPane.showInputDialog(this, "Quem está apostando?", "Aposta",
                JOptionPane.QUESTION_MESSAGE, null, g.getParticipantes().toArray(), g.getParticipantes().get(0));

        if (part == null) return;

        List<Partida> pendentes = partidaService.listarPendentes();
        if (pendentes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há partidas pendentes disponíveis para aposta.");
            return;
        }

        Partida pSel = (Partida) JOptionPane.showInputDialog(this, "Selecione a Partida", "Aposta",
                JOptionPane.QUESTION_MESSAGE, null, pendentes.toArray(), pendentes.get(0));
        if (pSel == null) return;

        try {
            int gA = Integer.parseInt(JOptionPane.showInputDialog("Gols " + pSel.getTimeA().getNome()));
            int gB = Integer.parseInt(JOptionPane.showInputDialog("Gols " + pSel.getTimeB().getNome()));
            apostaService.apostar(part, pSel, gA, gB);
            JOptionPane.showMessageDialog(this, "Aposta registrada!");
        } catch (RegraDeNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Dados inválidos.");
        }
    }

    private void criarCampeonato() {
        String nome = JOptionPane.showInputDialog("Nome do Campeonato:");
        if (nome == null) return;

        List<Clube> todosClubes = clubeService.listarTodos();
        JList<Clube> list = new JList<>(todosClubes.toArray(new Clube[0]));
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JOptionPane.showMessageDialog(this, new JScrollPane(list),
                "Selecione os times (Segure CTRL para múltiplos)", JOptionPane.QUESTION_MESSAGE);

        try {
            Campeonato novo = campeonatoService.criar(nome, list.getSelectedValuesList());
            modelCampeonatos.addElement(novo.getNome());
            JOptionPane.showMessageDialog(this, "Campeonato criado!");
        } catch (RegraDeNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void lancarResultado() {
        List<Partida> pendentes = partidaService.listarPendentes();
        if (pendentes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há partidas pendentes para registrar resultado.");
            return;
        }

        Partida p = (Partida) JOptionPane.showInputDialog(this, "Selecione a Partida", "Admin",
                JOptionPane.QUESTION_MESSAGE, null, pendentes.toArray(), pendentes.get(0));
        if (p == null) return;

        try {
            int gA = Integer.parseInt(JOptionPane.showInputDialog("Gols " + p.getTimeA().getNome()));
            int gB = Integer.parseInt(JOptionPane.showInputDialog("Gols " + p.getTimeB().getNome()));
            partidaService.registrarResultado(admin, p, gA, gB);
            atualizarRankingInterface();
            JOptionPane.showMessageDialog(this, "Resultado registrado!");
        } catch (RegraDeNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Valores inválidos.");
        }
    }

    // --- REQUISITO: MOSTRAR CLASSIFICAÇÃO NA INTERFACE ---
    private void atualizarRankingInterface() {
        StringBuilder sb = new StringBuilder();
        for (Grupo g : grupoService.listarTodos()) {
            sb.append("--- GRUPO: ").append(g.getNome()).append(" ---\n");
            for (Participante p : grupoService.classificacao(g)) {
                sb.append(p.getNome()).append(": ").append(p.calcularPontuacao()).append(" pts\n");
            }
            sb.append("\n");
        }
        areaRanking.setText(sb.toString());
    }

    private void entrarEmGrupo() {
        Grupo g = selecionarGrupo();
        if (g == null) return;

        String n = JOptionPane.showInputDialog("Seu nome:");
        if (n == null) return;

        try {
            grupoService.adicionarParticipante(g, n);
            atualizarRankingInterface();
            JOptionPane.showMessageDialog(this, "Participante adicionado ao grupo!");
        } catch (RegraDeNegocioException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private Grupo selecionarGrupo() {
        List<Grupo> grupos = grupoService.listarTodos();
        if (grupos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum grupo criado.");
            return null;
        }
        return (Grupo) JOptionPane.showInputDialog(this, "Selecione o Grupo", "Grupos",
                JOptionPane.QUESTION_MESSAGE, null, grupos.toArray(), grupos.get(0));
    }

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(JPAUtil::fechar));
        SwingUtilities.invokeLater(() -> new SistemaCompleto().setVisible(true));
    }
}
