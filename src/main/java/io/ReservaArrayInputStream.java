package src.main.java.io;

import java.io.IOException;
import java.io.InputStream;
import src.main.java.model.Reserva;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;

// InputStream para ler objetos Reserva 
public class ReservaArrayInputStream extends InputStream {
    private DataInputStream dataIn;
    private byte[] currentReservaPayloadBuffer;
    private int currentPayloadByteIndex = 0;
    private boolean endOfUnderlyingStream = false;

    public ReservaArrayInputStream(InputStream sourceIn) {
        this.dataIn = new DataInputStream(sourceIn);
    }

    @Override
    public int read() throws IOException {
        if (endOfUnderlyingStream)
            return -1;

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

    public Reserva readReserva() throws IOException {
        try {
            int totalSize = dataIn.readInt();
            byte[] rawPayload = new byte[totalSize];
            dataIn.readFully(rawPayload);

            try (ByteArrayInputStream bais = new ByteArrayInputStream(rawPayload);
                    DataInputStream payloadIn = new DataInputStream(bais)) {

                payloadIn.readInt(); // Lê e descarta o marcador de tamanho de atributos

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
            endOfUnderlyingStream = true;
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        dataIn.close();
        super.close();
    }
}