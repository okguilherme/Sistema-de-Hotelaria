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

    private byte[] serializeReserva(Reserva r) throws IOException {
        try (ByteArrayOutputStream innerPayloadBaos = new ByteArrayOutputStream();
             DataOutputStream innerPayloadDataOut = new DataOutputStream(innerPayloadBaos)) {

            // Temporariamente serializa os 3 atributos (CPF, numeroQuarto, idReserva) para CALCULAR seu tamanho e escrevê-lo.
            try (ByteArrayOutputStream baos3Attrs = new ByteArrayOutputStream();
                 DataOutputStream dataOut3Attrs = new DataOutputStream(baos3Attrs)) {
                dataOut3Attrs.writeUTF(r.getCPF());
                dataOut3Attrs.writeInt(r.getNumeroQuarto());
                dataOut3Attrs.writeUTF(r.getIdReserva());
                dataOut3Attrs.flush();
                byte[] threeAttrsBytes = baos3Attrs.toByteArray(); // Bytes dos 3 atributos
                
                innerPayloadDataOut.writeInt(threeAttrsBytes.length); 
            }

            innerPayloadDataOut.writeUTF(r.getCPF());
            innerPayloadDataOut.writeUTF(r.getIdHospede());
            innerPayloadDataOut.writeInt(r.getNumeroQuarto());
            innerPayloadDataOut.writeUTF(r.getDataCheckIn());
            innerPayloadDataOut.writeUTF(r.getDataCheckOut());
            innerPayloadDataOut.writeDouble(r.getValorTotal());
            innerPayloadDataOut.writeUTF(r.getIdReserva());
            innerPayloadDataOut.flush();

            byte[] fullReservaContent = innerPayloadBaos.toByteArray();

            // Encapsula 'fullReservaContent' com seu próprio tamanho TOTAL.
            try (ByteArrayOutputStream finalOuterBaos = new ByteArrayOutputStream();
                 DataOutputStream finalOuterDataOut = new DataOutputStream(finalOuterBaos)) {
                finalOuterDataOut.writeInt(fullReservaContent.length); // Escreve o TAMANHO TOTAL DESTE PAYLOAD
                finalOuterDataOut.write(fullReservaContent);          // Escreve o payload completo (incluindo o tamanho dos 3 attrs)
                finalOuterDataOut.flush();
                return finalOuterBaos.toByteArray(); // Retorna o array de bytes completo para uma ÚNICA Reserva
            }
        }
    }

    @Override
    public void write(int b) throws IOException {
        dataOut.write(b); 
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