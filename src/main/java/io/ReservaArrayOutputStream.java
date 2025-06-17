package src.main.java.io;

import java.io.IOException;
import java.io.OutputStream;

import src.main.java.model.Reserva;

import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;

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
    }

    public void writeAllReservas() throws IOException {
        for (int i = 0; i < quantidade; i++) {
            if (i < reservas.length) { 
                byte[] reservaBytes = serializeReserva(reservas[i]);
                dataOut.write(reservaBytes); 
            } else {
                System.err.println("Aviso: Quantidade excede o tamanho do array de reservas fornecido.");
                break;
            }
        }
        dataOut.flush();
    }

    private byte[] serializeReserva(Reserva r) throws IOException {
        try (ByteArrayOutputStream innerPayloadBaos = new ByteArrayOutputStream();
             DataOutputStream innerPayloadDataOut = new DataOutputStream(innerPayloadBaos)) {

            try (ByteArrayOutputStream baos3Attrs = new ByteArrayOutputStream();
                 DataOutputStream dataOut3Attrs = new DataOutputStream(baos3Attrs)) {
                dataOut3Attrs.writeUTF(r.getCPF());
                dataOut3Attrs.writeInt(r.getNumeroQuarto());
                dataOut3Attrs.writeUTF(r.getIdReserva());
                dataOut3Attrs.flush();
                byte[] threeAttrsBytes = baos3Attrs.toByteArray(); 
                
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

            try (ByteArrayOutputStream finalOuterBaos = new ByteArrayOutputStream(); 
                 DataOutputStream finalOuterDataOut = new DataOutputStream(finalOuterBaos)) { 
                finalOuterDataOut.writeInt(fullReservaContent.length); 
                finalOuterDataOut.write(fullReservaContent);         
                finalOuterDataOut.flush(); 
                return finalOuterBaos.toByteArray(); 
            } 
        } 
    } 

    @Override 
    public void write(int b) throws IOException { 
        dataOut.write(b);  
    } 

    @Override 
    public void close() throws IOException { 
        flush(); 
        dataOut.close(); 
        super.close(); 
    } 

    @Override 
    public void flush() throws IOException { 
        dataOut.flush(); 
    } 
}