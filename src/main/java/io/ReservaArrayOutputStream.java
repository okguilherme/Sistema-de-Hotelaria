package src.main.java.io;

import java.io.IOException;
import java.io.OutputStream;

import src.main.java.model.Reserva;

import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

/**
 * OutputStream para enviar um array de objetos Reserva,
 * com cabeçalhos de tamanho por objeto.
 */
public class ReservaArrayOutputStream extends OutputStream {
    private DataOutputStream dataOut;
    private Reserva[] reservas;
    private int quantidade;

    // As variáveis currentReservaIndex, currentReservaBytes e currentByteIndex
    // não são mais usadas pela lógica principal de envio via writeAllReservas().
    // Mantemos o write(int b) apenas para cumprir a interface.

    /**
     * Construtor.
     *
     * @param destinationOut Stream de saída.
     * @param reservas Array de objetos Reserva.
     * @param quantidade Quantidade de objetos a escrever.
     */
    public ReservaArrayOutputStream(OutputStream destinationOut, Reserva[] reservas, int quantidade) throws IOException {
        this.dataOut = new DataOutputStream(destinationOut);
        this.reservas = reservas;
        this.quantidade = quantidade;

        // CRÍTICO: Escreve a quantidade total de reservas no início do stream principal.
        // O Servidor irá ler este 'int' primeiro.
        dataOut.writeInt(this.quantidade);
        dataOut.flush(); // Garante que a quantidade seja enviada imediatamente
    }

    /**
     * NOVO MÉTODO: Para escrever todas as reservas de uma vez.
     * Este é o método que o Cliente deve chamar.
     */
    public void writeAllReservas() throws IOException {
        for (int i = 0; i < quantidade; i++) {
            if (i < reservas.length) { // Garante que não excedemos o tamanho do array
                // Cada chamada a serializeReserva() agora retorna o payload completo
                // de UMA Reserva, já incluindo o cabeçalho de seu tamanho total.
                byte[] reservaBytes = serializeReserva(reservas[i]);
                dataOut.write(reservaBytes); // Escreve o array de bytes da reserva serializada
            } else {
                System.err.println("Aviso: Quantidade excede o tamanho do array de reservas fornecido.");
                break;
            }
        }
        dataOut.flush(); // Garante que todos os dados sejam enviados após o loop
    }


    /**
     * Serializa uma única Reserva em um array de bytes.
     *
     * O formato do array de bytes retornado para CADA RESERVA será:
     * 1. [INT: Tamanho Total deste Payload da Reserva]
     * 2. [INT: Tamanho dos 3 Atributos (CPF, numeroQuarto, idReserva)]
     * 3. [Bytes dos 3 Atributos] (como parte dos dados completos da Reserva)
     * 4. [Todos os Bytes dos Atributos da Reserva] (na ordem correta)
     *
     * @param r O objeto Reserva a ser serializado.
     * @return Um array de bytes contendo a representação serializada da Reserva.
     * @throws IOException Se ocorrer um erro de I/O durante a serialização.
     */
    private byte[] serializeReserva(Reserva r) throws IOException {
        // Usa um ByteArrayOutputStream temporário para construir o payload INTERNO da Reserva
        // (que inclui o tamanho dos 3 atributos e todos os atributos da Reserva).
        try (ByteArrayOutputStream innerPayloadBaos = new ByteArrayOutputStream();
             DataOutputStream innerPayloadDataOut = new DataOutputStream(innerPayloadBaos)) {

            // PASSO 1: Temporariamente serializa os 3 atributos (CPF, numeroQuarto, idReserva)
            // para CALCULAR seu tamanho e escrevê-lo.
            try (ByteArrayOutputStream baos3Attrs = new ByteArrayOutputStream();
                 DataOutputStream dataOut3Attrs = new DataOutputStream(baos3Attrs)) {
                dataOut3Attrs.writeUTF(r.getCPF());
                dataOut3Attrs.writeInt(r.getNumeroQuarto());
                dataOut3Attrs.writeUTF(r.getIdReserva());
                dataOut3Attrs.flush();
                byte[] threeAttrsBytes = baos3Attrs.toByteArray(); // Bytes dos 3 atributos
                
                // Escreve o TAMANHO EM BYTES DOS 3 ATRIBUTOS no stream do payload INTERNO.
                innerPayloadDataOut.writeInt(threeAttrsBytes.length); 
                // NÃO escreve threeAttrsBytes aqui, pois eles serão incluídos abaixo como parte dos atributos completos.
            }

            // PASSO 2: Escreve TODOS os atributos da Reserva em sequência.
            // A ordem aqui DEVE ser a mesma que o InputStream espera ler.
            innerPayloadDataOut.writeUTF(r.getCPF());
            innerPayloadDataOut.writeUTF(r.getIdHospede());
            innerPayloadDataOut.writeInt(r.getNumeroQuarto());
            innerPayloadDataOut.writeUTF(r.getDataCheckIn());
            innerPayloadDataOut.writeUTF(r.getDataCheckOut());
            innerPayloadDataOut.writeDouble(r.getValorTotal());
            innerPayloadDataOut.writeUTF(r.getIdReserva());
            innerPayloadDataOut.flush();

            // 'fullReservaContent' agora contém:
            // [INT: tamanho dos 3 atributos] + [TODOS os dados serializados da Reserva]
            byte[] fullReservaContent = innerPayloadBaos.toByteArray();

            // PASSO 3: Agora, encapsula 'fullReservaContent' com seu próprio tamanho TOTAL.
            // Este é o cabeçalho MAIS EXTERNO que o ReservaArrayInputStream.readReserva()
            // espera ler primeiro para cada Reserva.
            try (ByteArrayOutputStream finalOuterBaos = new ByteArrayOutputStream();
                 DataOutputStream finalOuterDataOut = new DataOutputStream(finalOuterBaos)) {
                finalOuterDataOut.writeInt(fullReservaContent.length); // Escreve o TAMANHO TOTAL DESTE PAYLOAD
                finalOuterDataOut.write(fullReservaContent);          // Escreve o payload completo (incluindo o tamanho dos 3 attrs)
                finalOuterDataOut.flush();
                return finalOuterBaos.toByteArray(); // Retorna o array de bytes completo para uma ÚNICA Reserva
            }
        }
    }

    /**
     * O método write(int b) da superclasse OutputStream.
     * Este é um método de baixo nível. Para serializar objetos, prefira writeAllReservas().
     * Esta implementação simplesmente passa o byte para o stream de destino.
     */
    @Override
    public void write(int b) throws IOException {
        dataOut.write(b); // Apenas passa o byte para o stream de destino subjacente.
    }


    /** Garante que todos os dados são enviados e fecha os streams. */
    @Override
    public void close() throws IOException {
        flush();
        dataOut.close(); // Fecha o DataOutputStream interno, o que também fecha o OutputStream de destino
        super.close();
    }

    /** Força o envio dos dados pendentes. */
    @Override
    public void flush() throws IOException {
        dataOut.flush();
    }
}