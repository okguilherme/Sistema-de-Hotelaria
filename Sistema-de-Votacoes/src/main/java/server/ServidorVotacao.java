package main.java.server;

import main.java.model.Candidato;
import main.java.model.Eleitor;
import main.java.model.Voto;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorVotacao {

    private static final int PORTA = 12345;
    private static final int DURACAO_VOTACAO_SEG = 120; 

    private static final Map<String, Eleitor> eleitores = new HashMap<>();
    private static final Map<String, Candidato> candidatos = new HashMap<>();
    private static final List<Voto> votos = Collections.synchronizedList(new ArrayList<>());
    private static final AtomicBoolean votacaoAberta = new AtomicBoolean(true);

    public static void main(String[] args) {
        inicializarDados();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                votacaoAberta.set(false);
                System.out.println("\nVotação encerrada!");
                mostrarResultados();
            }
        }, DURACAO_VOTACAO_SEG * 1000);

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor de votação iniciado na porta " + PORTA);
            System.out.println("Votação estará aberta por " + DURACAO_VOTACAO_SEG + " segundos.");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> tratarCliente(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void inicializarDados() {
        carregarEleitoresDeArquivo("data/eleitores.json");
        carregarCandidatosDeArquivo("data/candidatos.json");

        System.out.println("Eleitores carregados: " + eleitores.size());
        System.out.println("Candidatos carregados: " + candidatos.size());
    }

    private static void tratarCliente(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Bem-vindo ao sistema de votação.");

            Eleitor eleitorLogado = null;
            boolean isAdmin = false;
            String linha;

            while ((linha = in.readLine()) != null) {
                if (linha.startsWith("LOGIN_ADMIN;")) {
                    String[] partes = linha.split(";");
                    if (partes.length == 3 && "admin".equals(partes[1]) && "adminpass".equals(partes[2])) {
                        out.println("SUCCESS_ADMIN_LOGIN");
                        isAdmin = true;
                        break;
                    } else {
                        out.println("FAILURE_ADMIN_LOGIN");
                    }
                } else if (linha.startsWith("LOGIN;")) {
                    String[] partes = linha.split(";");
                    if (partes.length == 3) {
                        String login = partes[1];
                        String senha = partes[2];
                        Eleitor e = eleitores.get(login);
                        if (e != null && e.getSenha().equals(senha)) {
                            eleitorLogado = e;
                            out.println("LOGIN_OK");
                            break;
                        } else {
                            out.println("LOGIN_FALHOU");
                        }
                    } else {
                        out.println("ERRO: Formato inválido.");
                    }
                } else {
                    out.println("Faça login primeiro.");
                }
            }

            while ((linha = in.readLine()) != null) {
                if (linha.equals("LISTAR")) {
                    out.println("CANDIDATOS_JSON");
                    out.println(gerarJsonCandidatos());

                } else if (linha.startsWith("VOTAR_JSON;")) {
                    if (eleitorLogado == null) {
                        out.println("ERRO: Você não está autenticado como eleitor.");
                        continue;
                    }
                    String json = linha.substring("VOTAR_JSON;".length());
                    processarVoto(json, eleitorLogado, out);

                } else if (linha.equals("STATUS")) {
                    out.println(votacaoAberta.get() ? "VOTACAO_ABERTA" : "VOTACAO_ENCERRADA");

                } else if (linha.equals("ADD_CANDIDATO") && isAdmin) {
                    String json = in.readLine();
                    adicionarCandidato(json, out);

                } else if (linha.equals("REMOVE_CANDIDATO") && isAdmin) {
                    String id = in.readLine();
                    removerCandidato(id, out);

                } else {
                    out.println("Comando desconhecido ou não autorizado.");
                }
            }

        } catch (IOException e) {
            System.out.println("Cliente desconectado.");
        }
    }

    private static void adicionarCandidato(String json, PrintWriter out) {
        try {
            Map<String, String> campos = parseJson(json);
            String id = campos.get("id");
            String nome = campos.get("nome");
            int numero = Integer.parseInt(campos.get("numero"));
            String partido = campos.get("partido");

            if (candidatos.containsKey(id)) {
                out.println("ERROR_ADD_CANDIDATO");
                out.println("Candidato com esse ID já existe.");
            } else {
                Candidato c = new Candidato(id, nome, numero, partido);
                candidatos.put(id, c);
                salvarCandidatosEmArquivo();
                out.println("SUCCESS_ADD_CANDIDATO");
                out.println("Candidato adicionado com sucesso.");
            }
        } catch (Exception e) {
            out.println("ERROR_ADD_CANDIDATO");
            out.println("Erro ao adicionar candidato: " + e.getMessage());
        }
    }

    private static void removerCandidato(String id, PrintWriter out) {
        if (candidatos.containsKey(id)) {
            candidatos.remove(id);
            salvarCandidatosEmArquivo();
            out.println("SUCCESS_REMOVE_CANDIDATO");
            out.println("Candidato removido com sucesso.");
        } else {
            out.println("ERROR_REMOVE_CANDIDATO");
            out.println("Candidato com ID informado não existe.");
        }
    }

    private static void salvarCandidatosEmArquivo() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/candidatos.json"))) {
            writer.println("[");
            boolean primeiro = true;
            for (Candidato c : candidatos.values()) {
                if (!primeiro) writer.println(",");
                writer.print("  {\"id\":\"" + c.getId() + "\",\"nome\":\"" + c.getNome() + "\",\"numero\":" + c.getNumeroVotacao() + ",\"partido\":\"" + c.getPartido() + "\"}");
                primeiro = false;
            }
            writer.println("\n]");
        } catch (IOException e) {
            System.err.println("Erro ao salvar candidatos no arquivo: " + e.getMessage());
        }
    }

    private static String gerarJsonCandidatos() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean primeiro = true;
        for (Candidato c : candidatos.values()) {
            if (!primeiro) sb.append(",");
            sb.append("{")
              .append("\"id\":\"").append(c.getId()).append("\",")
              .append("\"nome\":\"").append(c.getNome()).append("\",")
              .append("\"numero\":").append(c.getNumeroVotacao()).append(",")
              .append("\"partido\":\"").append(c.getPartido()).append("\"")
              .append("}");
            primeiro = false;
        }
        sb.append("]");
        return sb.toString();
    }

    private static void processarVoto(String json, Eleitor eleitor, PrintWriter out) {
        if (!votacaoAberta.get()) {
            out.println("ERRO: Votação encerrada.");
            return;
        }

        synchronized (eleitor) {
            if (eleitor.getJaVotou()) {
                out.println("ERRO: Você já votou.");
                return;
            }

            try {
                Map<String, String> mapa = parseJson(json);
                String idEleitor = mapa.get("idEleitor");
                String idCandidato = mapa.get("idCandidato");

                if (!eleitor.getId().equals(idEleitor)) {
                    out.println("ERRO: ID de eleitor inválido.");
                    return;
                }

                Candidato c = candidatos.get(idCandidato);
                if (c == null) {
                    out.println("ERRO: Candidato não encontrado.");
                    return;
                }

                Voto voto = new Voto(idEleitor, idCandidato);
                votos.add(voto);
                c.adicionarVoto();
                eleitor.setJaVotou(true);
                out.println("VOTO_OK");

                System.out.println("Voto computado: " + eleitor.getLogin() + " → " + c.getNome());

            } catch (Exception e) {
                out.println("ERRO: JSON de voto inválido.");
            }
        }
    }

    private static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.trim().replaceAll("[{}\"]", "");
        String[] pares = json.split(",");
        for (String par : pares) {
            String[] kv = par.split(":");
            if (kv.length == 2) {
                map.put(kv[0].trim(), kv[1].trim());
            }
        }
        return map;
    }

    private static void mostrarResultados() {
        System.out.println("\nResultados da Votação:");
        if (votos.isEmpty()) {
            System.out.println("Nenhum voto registrado.");
            return;
        }

        Map<String, Integer> contagem = new HashMap<>();
        for (Voto v : votos) {
            contagem.merge(v.getIdCandidato(), 1, Integer::sum);
        }

        int total = votos.size();
        int max = -1;
        List<String> vencedores = new ArrayList<>();

        for (Candidato c : candidatos.values()) {
            int v = contagem.getOrDefault(c.getId(), 0);
            double pct = (v * 100.0) / total;
            System.out.printf("- %s (%s): %d votos (%.2f%%)\n", c.getNome(), c.getPartido(), v, pct);

            if (v > max) {
                max = v;
                vencedores.clear();
                vencedores.add(c.getNome());
            } else if (v == max) {
                vencedores.add(c.getNome());
            }
        }

        System.out.println("\nResultado Final:");
        if (vencedores.size() == 1) {
            System.out.println("Vencedor: " + vencedores.get(0));
        } else {
            System.out.println("Empate entre: " + String.join(", ", vencedores));
        }
    }

    private static void carregarEleitoresDeArquivo(String caminho) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha.trim());
            }
            String json = sb.toString().replace("[", "").replace("]", "");
            if (json.isEmpty()) return;

            String[] blocos = json.split("\\},\\s*\\{");
            for (String bloco : blocos) {
                String dados = bloco.replace("{", "").replace("}", "");
                Map<String, String> campos = new HashMap<>();
                for (String campo : dados.split(",")) {
                    String[] partes = campo.split(":");
                    if (partes.length == 2) {
                        campos.put(partes[0].replaceAll("\"", "").trim(), partes[1].replaceAll("\"", "").trim());
                    }
                }
                String id = campos.get("id");
                String login = campos.get("login");
                String senha = campos.get("senha");
                boolean jaVotou = Boolean.parseBoolean(campos.get("jaVotou"));
                Eleitor e = new Eleitor(id, login, senha);
                e.setJaVotou(jaVotou);
                eleitores.put(login, e);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar eleitores: " + e.getMessage());
        }
    }


    private static void carregarCandidatosDeArquivo(String caminho) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            StringBuilder sb = new StringBuilder();
            String linha;
            while ((linha = br.readLine()) != null) {
                sb.append(linha.trim());
            }
            String json = sb.toString().replace("[", "").replace("]", "");
            if (json.isEmpty()) return;

            String[] blocos = json.split("\\},\\s*\\{");
            for (String bloco : blocos) {
                String dados = bloco.replace("{", "").replace("}", "");
                Map<String, String> campos = new HashMap<>();
                for (String campo : dados.split(",")) {
                    String[] partes = campo.split(":");
                    if (partes.length == 2) {
                        campos.put(partes[0].replaceAll("\"", "").trim(), partes[1].replaceAll("\"", "").trim());
                    }
                }
                String id = campos.get("id");
                String nome = campos.get("nome");
                int numero = Integer.parseInt(campos.get("numero"));
                String partido = campos.get("partido");
                Candidato c = new Candidato(id, nome, numero, partido);
                candidatos.put(id, c);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar candidatos: " + e.getMessage());
        }
    }

}
