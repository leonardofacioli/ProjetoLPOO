package aplication;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import entities.*;

public class SistemaCompleto extends JFrame {
    private List<Clube> todosClubes = new ArrayList<>();
    private List<Campeonato> campeonatos = new ArrayList<>();
    private List<Grupo> grupos = new ArrayList<>();
    private Administrador admin = new Administrador("Admin Central");

    private DefaultListModel<String> modelClubes = new DefaultListModel<>();
    private DefaultListModel<String> modelCampeonatos = new DefaultListModel<>();
    private JTextArea areaRanking = new JTextArea(10, 30);

    public SistemaCompleto() {
        setTitle("Sistema de Apostas Esportivas");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cadastrarClubesIniciais();

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Administração", criarPainelAdmin());
        abas.addTab("Grupos e Apostas", criarPainelParticipante());

        add(abas);
    }

    private void cadastrarClubesIniciais() {
        String[] nomes = {"Corinthians", "Palmeiras", "São Paulo", "Santos", "Flamengo", "Grêmio", "Internacional", "Atlético-MG"};
        for (String n : nomes) {
            Clube c = new Clube(n);
            todosClubes.add(c);
            modelClubes.addElement(n);
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

        // Listeners (mantêm a mesma lógica anterior)
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

        btnGrupo.addActionListener(e -> {
            if (grupos.size() < 5) {
                String n = JOptionPane.showInputDialog("Nome do Grupo:");
                if (n != null) grupos.add(new Grupo(n));
            } else JOptionPane.showMessageDialog(this, "Limite de 5 grupos!");
        });

        btnEntrar.addActionListener(e -> entrarEmGrupo());
        btnApostar.addActionListener(e -> realizarAposta());

        return painel;
    }

    // --- REQUISITO: CADASTRO OBRIGATÓRIO DE HORÁRIO ---
    private void registrarPartida() {
        if (campeonatos.isEmpty()) return;
        Campeonato camp = (Campeonato) JOptionPane.showInputDialog(this, "Selecione o Campeonato", "Partida",
                JOptionPane.QUESTION_MESSAGE, null, campeonatos.toArray(), campeonatos.get(0));

        if (camp == null) return;

        Clube c1 = (Clube) JOptionPane.showInputDialog(this, "Time A", "Partida",
                JOptionPane.QUESTION_MESSAGE, null, camp.getClubes().toArray(), camp.getClubes().get(0));
        Clube c2 = (Clube) JOptionPane.showInputDialog(this, "Time B", "Partida",
                JOptionPane.QUESTION_MESSAGE, null, camp.getClubes().toArray(), camp.getClubes().get(1));

        String dataString = JOptionPane.showInputDialog(this, "Horário da Partida (dd/MM/yyyy HH:mm):",
                LocalDateTime.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

        try {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime horario = LocalDateTime.parse(dataString, parser);

            Partida p = new Partida(c1, c2, horario);
            camp.adicionarPartida(p);
            JOptionPane.showMessageDialog(this, "Partida agendada com sucesso!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ERRO: O horário é obrigatório e deve seguir o formato dd/MM/yyyy HH:mm");
        }
    }

    // --- REQUISITO: APOSTA SOMENTE ATÉ 20 MINUTOS ANTES ---
    private void realizarAposta() {
        Grupo g = selecionarGrupo();
        if (g == null || g.getParticipantes().isEmpty()) return;

        Participante part = (Participante) JOptionPane.showInputDialog(this, "Quem está apostando?", "Aposta",
                JOptionPane.QUESTION_MESSAGE, null, g.getParticipantes().toArray(), g.getParticipantes().get(0));

        List<Partida> todas = new ArrayList<>();
        for (Campeonato c : campeonatos) todas.addAll(c.getPartidas());

        if (todas.isEmpty()) return;

        Partida pSel = (Partida) JOptionPane.showInputDialog(this, "Selecione a Partida", "Aposta",
                JOptionPane.QUESTION_MESSAGE, null, todas.toArray(), todas.get(0));

        if (pSel != null) {
            if (!pSel.podeApostar()) {
                JOptionPane.showMessageDialog(this, "BLOQUEADO: Aposta permitida apenas até 20 minutos antes do início!");
                return;
            }
            try {
                int gA = Integer.parseInt(JOptionPane.showInputDialog("Gols " + pSel.getTimeA().getNome()));
                int gB = Integer.parseInt(JOptionPane.showInputDialog("Gols " + pSel.getTimeB().getNome()));
                part.apostar(new Aposta(pSel, gA, gB));
                JOptionPane.showMessageDialog(this, "Aposta registrada!");
            } catch (Exception e) { JOptionPane.showMessageDialog(this, "Dados inválidos."); }
        }
    }

    private void criarCampeonato() {
        String nome = JOptionPane.showInputDialog("Nome do Campeonato:");
        if (nome == null || nome.trim().isEmpty()) return;

        Campeonato novo = new Campeonato(nome);
        // Adiciona automaticamente os 8 clubes principais
        for (int i = 0; i < Math.min(8, todosClubes.size()); i++) {
            novo.adicionarClube(todosClubes.get(i));
        }
        campeonatos.add(novo);
        modelCampeonatos.addElement(nome);
    }

    private void lancarResultado() {
        List<Partida> todas = new ArrayList<>();
        for (Campeonato c : campeonatos) todas.addAll(c.getPartidas());
        if (todas.isEmpty()) return;

        Partida p = (Partida) JOptionPane.showInputDialog(this, "Selecione a Partida", "Admin",
                JOptionPane.QUESTION_MESSAGE, null, todas.toArray(), todas.get(0));

        if (p != null) {
            // Nova Verificação: Se já tem resultado, avisa e cancela
            if (p.getGolsA() != null) {
                JOptionPane.showMessageDialog(this, "ERRO: O resultado desta partida já foi registrado e não pode ser alterado!");
                return;
            }

            try {
                int gA = Integer.parseInt(JOptionPane.showInputDialog("Gols " + p.getTimeA().getNome()));
                int gB = Integer.parseInt(JOptionPane.showInputDialog("Gols " + p.getTimeB().getNome()));
                admin.registrarResultado(p, gA, gB); // A trava na classe Partida garante a segurança
                atualizarRankingInterface();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Valores inválidos.");
            }
        }
    }

    // --- REQUISITO: MOSTRAR CLASSIFICAÇÃO NA INTERFACE ---
    private void atualizarRankingInterface() {
        StringBuilder sb = new StringBuilder();
        for (Grupo g : grupos) {
            sb.append("--- GRUPO: ").append(g.getNome()).append(" ---\n");
            List<Participante> parts = new ArrayList<>(g.getParticipantes());
            parts.sort((p1, p2) -> p2.calcularPontuacao() - p1.calcularPontuacao());
            for (Participante p : parts) {
                sb.append(p.getNome()).append(": ").append(p.calcularPontuacao()).append(" pts\n");
            }
            sb.append("\n");
        }
        areaRanking.setText(sb.toString());
    }

    private void entrarEmGrupo() {
        Grupo g = selecionarGrupo();
        if (g != null && g.getParticipantes().size() < 5) {
            String n = JOptionPane.showInputDialog("Seu nome:");
            if (n != null) {
                g.adicionarParticipante(new Participante(n));
                atualizarRankingInterface();
            }
        } else JOptionPane.showMessageDialog(this, "Grupo cheio!");
    }

    private Grupo selecionarGrupo() {
        if (grupos.isEmpty()) return null;
        return (Grupo) JOptionPane.showInputDialog(this, "Selecione o Grupo", "Grupos",
                JOptionPane.QUESTION_MESSAGE, null, grupos.toArray(), grupos.get(0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaCompleto().setVisible(true));
    }
}