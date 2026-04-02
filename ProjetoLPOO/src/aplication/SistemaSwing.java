package aplication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;
import entities.*;

public class SistemaSwing {

    private Campeonato campeonato;
    private Grupo grupo;
    private JFrame frame;
    private DefaultListModel<String> listaParticipantesModel;

    public SistemaSwing() {
        campeonato = new Campeonato("Brasileirão");
        grupo = new Grupo("Amigos");
        inicializarSwing();
    }

    private void inicializarSwing() {
        frame = new JFrame("Sistema de Apostas");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        
        JPanel painelCadastro = new JPanel();
        painelCadastro.setLayout(new FlowLayout());
        JTextField campoNome = new JTextField(10);
        JButton btnCadastrar = new JButton("Cadastrar Participante");
        painelCadastro.add(new JLabel("Nome:"));
        painelCadastro.add(campoNome);
        painelCadastro.add(btnCadastrar);
        frame.add(painelCadastro, BorderLayout.NORTH);

      
        listaParticipantesModel = new DefaultListModel<>();
        JList<String> listaParticipantes = new JList<>(listaParticipantesModel);
        JScrollPane scroll = new JScrollPane(listaParticipantes);
        frame.add(scroll, BorderLayout.CENTER);

        
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new FlowLayout());
        JButton btnApostar = new JButton("Registrar Aposta");
        JButton btnResultado = new JButton("Registrar Resultado");
        JButton btnRanking = new JButton("Mostrar Ranking");
        painelBotoes.add(btnApostar);
        painelBotoes.add(btnResultado);
        painelBotoes.add(btnRanking);
        frame.add(painelBotoes, BorderLayout.SOUTH);

       
        btnCadastrar.addActionListener(e -> {
            String nome = campoNome.getText().trim();
            if (!nome.isEmpty() && grupo.getParticipantes().size() < 5) {
                Participante p = new Participante(nome);
                grupo.adicionarParticipante(p);
                listaParticipantesModel.addElement(nome);
                campoNome.setText("");
                JOptionPane.showMessageDialog(frame, "Participante cadastrado!");
            } else {
                JOptionPane.showMessageDialog(frame, "Nome vazio ou limite de participantes atingido!");
            }
        });

        btnApostar.addActionListener(e -> registrarApostaSwing());
        btnResultado.addActionListener(e -> registrarResultadoSwing());
        btnRanking.addActionListener(e -> mostrarRankingSwing());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        
        criarPartidasExemplo();
    }

    private void criarPartidasExemplo() {
        Clube a = new Clube("Time A");
        Clube b = new Clube("Time B");
        Clube c = new Clube("Time C");
        Clube d = new Clube("Time D");
        campeonato.adicionarClube(a);
        campeonato.adicionarClube(b);
        campeonato.adicionarClube(c);
        campeonato.adicionarClube(d);

      
        campeonato.adicionarPartida(new Partida(a, b, LocalDateTime.now().plusHours(1)));
        campeonato.adicionarPartida(new Partida(c, d, LocalDateTime.now().plusHours(2)));
    }

    private void registrarApostaSwing() {
        List<Partida> partidas = campeonato.getPartidas();
        Participante participante = selecionarParticipante();
        if (participante == null) return;

        String[] opcoesPartida = new String[partidas.size()];
        for (int i = 0; i < partidas.size(); i++) {
            Partida p = partidas.get(i);
            opcoesPartida[i] = p.getTimeA().getNome() + " x " + p.getTimeB().getNome();
        }

        String escolha = (String) JOptionPane.showInputDialog(frame,
                "Selecione a partida:", "Aposta",
                JOptionPane.PLAIN_MESSAGE, null, opcoesPartida, opcoesPartida[0]);

        if (escolha != null) {
            int index = -1;
            for (int i = 0; i < opcoesPartida.length; i++) {
                if (opcoesPartida[i].equals(escolha)) {
                    index = i;
                    break;
                }
            }
            Partida partida = partidas.get(index);
            if (!partida.podeApostar()) {
                JOptionPane.showMessageDialog(frame, "Aposta não permitida. Falta menos de 20 minutos!");
                return;
            }

            try {
                int golsA = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gols " + partida.getTimeA().getNome() + ":"));
                int golsB = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gols " + partida.getTimeB().getNome() + ":"));
                Aposta aposta = new Aposta(partida, golsA, golsB);
                participante.apostar(aposta);
                JOptionPane.showMessageDialog(frame, "Aposta registrada!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valores inválidos!");
            }
        }
    }

    private void registrarResultadoSwing() {
        List<Partida> partidas = campeonato.getPartidas();
        String[] opcoesPartida = new String[partidas.size()];
        for (int i = 0; i < partidas.size(); i++) {
            Partida p = partidas.get(i);
            opcoesPartida[i] = p.getTimeA().getNome() + " x " + p.getTimeB().getNome();
        }

        String escolha = (String) JOptionPane.showInputDialog(frame,
                "Selecione a partida para registrar resultado:", "Resultado",
                JOptionPane.PLAIN_MESSAGE, null, opcoesPartida, opcoesPartida[0]);

        if (escolha != null) {
            int index = -1;
            for (int i = 0; i < opcoesPartida.length; i++) {
                if (opcoesPartida[i].equals(escolha)) {
                    index = i;
                    break;
                }
            }
            Partida partida = partidas.get(index);

            try {
                int golsA = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gols " + partida.getTimeA().getNome() + ":"));
                int golsB = Integer.parseInt(JOptionPane.showInputDialog(frame, "Gols " + partida.getTimeB().getNome() + ":"));
                Administrador admin = new Administrador("Admin");
                admin.registrarResultado(partida, golsA, golsB);
                JOptionPane.showMessageDialog(frame, "Resultado registrado!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valores inválidos!");
            }
        }
    }

    private void mostrarRankingSwing() {
        List<Participante> participantes = grupo.getParticipantes();
        participantes.sort((p1, p2) -> p2.calcularPontuacao() - p1.calcularPontuacao());

        StringBuilder sb = new StringBuilder();
        for (Participante p : participantes) {
            sb.append(p.getNome()).append(" - ").append(p.calcularPontuacao()).append(" pontos\n");
        }

        JOptionPane.showMessageDialog(frame, sb.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE);
    }

    private Participante selecionarParticipante() {
        List<Participante> participantes = grupo.getParticipantes();
        if (participantes.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Nenhum participante cadastrado!");
            return null;
        }

        String[] nomes = new String[participantes.size()];
        for (int i = 0; i < participantes.size(); i++) {
            nomes[i] = participantes.get(i).getNome();
        }

        String escolhido = (String) JOptionPane.showInputDialog(frame,
                "Selecione o participante:", "Participante",
                JOptionPane.PLAIN_MESSAGE, null, nomes, nomes[0]);

        if (escolhido != null) {
            for (Participante p : participantes) {
                if (p.getNome().equals(escolhido)) return p;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaSwing::new);
    }
}
