package ProyectoRiegoAutomatico;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Aspersor {
    private final int idAspersor;
    private boolean estado = false;
    private Instant ultimoCambio;
    private final List<DatosAspersor> historial = new ArrayList<>();

    public Aspersor(int idAspersor) {
        this.idAspersor = idAspersor;
    }

    public void activar(long duracionMs) {
        this.estado = true;
        this.ultimoCambio = Instant.now();
        DatosAspersor d = new DatosAspersor(ultimoCambio, "ACTIVAR", duracionMs);
        agregarDato(d);
    }

    public void desactivar() {
        this.estado = false;
        this.ultimoCambio = Instant.now();
        DatosAspersor d = new DatosAspersor(ultimoCambio, "DESACTIVAR", 0);
        agregarDato(d);
    }

    public void agregarDato(DatosAspersor d) {
        historial.add(d);
    }

    public List<DatosAspersor> listarDatos() {
        return new ArrayList<>(historial);
    }

    // Getters
    public int getIdAspersor() { return idAspersor; }
    public boolean isEstado() { return estado; }
    public Instant getUltimoCambio() { return ultimoCambio; }

    public String detalleCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append("Aspersor id=").append(idAspersor).append("\n");
        sb.append("Estado: ").append(estado ? "ON" : "OFF").append("\n");
        sb.append("Historial (").append(historial.size()).append("):\n");
        for (DatosAspersor d : historial) sb.append("  - ").append(d).append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Aspersor " + idAspersor + " | " + (estado ? "ON" : "OFF");
    }
}

