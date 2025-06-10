package src.main.java.io;

import java.io.IOException;
import java.io.InputStream;

import src.main.java.model.Reserva;

import java.io.DataInputStream;
import java.io.ByteArrayInputStream;

/** InputStream para ler objetos Reserva */
public class ReservaArrayInputStream extends InputStream {
    private DataInputStream dataIn;
    private byte[] currentReservaPayloadBuffer; // Isso é usado apenas pelo método read(), não pelo readReserva()
    private int currentPayloadByteIndex = 0;    // Isso é usado apenas pelo método read(), não pelo readReserva()
    private boolean endOfUnderlyingStream = false;

    public ReservaArrayInputStream(InputStream sourceIn) {
        this.dataIn = new DataInputStream(sourceIn);
    }

    // O método read() da superclasse InputStream, não diretamente usado pelo Servidor para ler Reservas
    @Override
    public int read() throws IOException {
        // ... (manter como está, esta parte não é o problema atual)
        if (endOfUnderlyingStream) return -1;

        if (currentReservaPayloadBuffer == null || currentPayloadByteIndex >= currentReservaPayloadBuffer.length) {
            try {
                int totalSize = dataIn.readInt(); // lê tamanho do payload
                currentReservaPayloadBuffer = new byte[totalSize];
                dataIn.readFully(currentReservaPayloadBuffer); // lê bytes do payload
                currentPayloadByteIndex = 0;
            } catch (java.io.EOFException e) {
                endOfUnderlyingStream = true;
                return -1;
            }
        }

        return currentReservaPayloadBuffer[currentPayloadByteIndex++] & 0xFF;
    }

    // MÉTODO readReserva() - ONDE ESTÁ A CORREÇÃO CRÍTICA
    public Reserva readReserva() throws IOException {
        try {
            int totalSize = dataIn.readInt(); // OK: lê tamanho total da Reserva (cabeçalho externo)
            byte[] rawPayload = new byte[totalSize];
            dataIn.readFully(rawPayload); // OK: lê todo o payload da Reserva

            try (ByteArrayInputStream bais = new ByteArrayInputStream(rawPayload);
                 DataInputStream payloadIn = new DataInputStream(bais)) {

                // OK: lê o tamanho dos 3 atributos (como meta-informação)
                int threeAttrsSize = payloadIn.readInt(); 
                
                // **** REMOVA OU COMENTE A PRÓXIMA LINHA ****
                // A linha abaixo é o problema! Ela tenta ler um bloco de bytes que não foi enviado.
                // byte[] temp = new byte[threeAttrsSize];
                // payloadIn.readFully(temp); // <-- ESTA LINHA CAUSA O ERRO!

                // Agora, lê os atributos reais da Reserva (na ordem em que foram escritos pelo OutputStream)
                String cpf = payloadIn.readUTF();
                String idHospede = payloadIn.readUTF();
                int numeroQuarto = payloadIn.readInt();
                String dataCheckIn = payloadIn.readUTF();
                String dataCheckOut = payloadIn.readUTF();
                double valorTotal = payloadIn.readDouble();
                String idReserva = payloadIn.readUTF();

                return new Reserva(cpf, idHospede, numeroQuarto, dataCheckIn, dataCheckOut, valorTotal, idReserva);
            }

        } catch (java.io.EOFException e) {
            // Se um EOFException ocorrer aqui, significa que não há mais Reservas para ler.
            // É um sinal de que o Cliente pode ter fechado a conexão ou não há mais dados.
            endOfUnderlyingStream = true; // Sinaliza que o stream subjacente terminou
            return null;
        }
        // Não é necessário um catch para outras IOExceptions aqui, pois o chamador (ClientHandler) já lida.
    }

    @Override
    public void close() throws IOException {
        dataIn.close();
        super.close();
    }
}