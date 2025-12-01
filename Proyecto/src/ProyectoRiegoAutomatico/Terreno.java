package ProyectoRiegoAutomatico;

import java.util.ArrayList;
import java.util.List;

public class Terreno {
    private final int id;
    private double area;
    private final List<Parcela> parcelas = new ArrayList<>();

    public Terreno(int id, double area) {
        this.id = id;
        this.area = area;
    }

    public List<Parcela> generarParcelasAutomaticas() {
        List<Parcela> nuevas = new ArrayList<>();

        double areaRestante = this.area;

        // Si ya existen, restar su superficie del área
        for (Parcela p : parcelas) {
            areaRestante -= p.getSuperficie();
        }

        if (areaRestante <= 0) return nuevas;

        // Crear paquetes de 200 m2
        while (areaRestante >= 200) {
            int id = parcelas.size() + nuevas.size() + 1;
            nuevas.add(new Parcela(id, 200));
            areaRestante -= 200;
        }

        // Crear parcela del residuo (si > 0)
        if (areaRestante > 0) {
            int id = parcelas.size() + nuevas.size() + 1;
            nuevas.add(new Parcela(id, areaRestante));
        }

        parcelas.addAll(nuevas);
        return nuevas;
    }


    public List<Parcela> getParcelas() { return new ArrayList<>(parcelas); }
    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }
    public int getId() { return id; }
}
