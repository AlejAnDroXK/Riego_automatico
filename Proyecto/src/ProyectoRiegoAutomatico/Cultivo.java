package ProyectoRiegoAutomatico;

public class Cultivo {
    private final String nombre;
    private final int humedadMin;      // % (ej. 30)
    private final int humedadMax;      // % (ej. 80)
    private final int frecuenciaHoras; // cada cuántas horas idealmente regar
    private final double coefConsumo;  // coeficiente 1..10 para consumo / prioridad

    public Cultivo(String nombre, int humedadMin, int humedadMax, int frecuenciaHoras, double coefConsumo) {
        this.nombre = nombre;
        this.humedadMin = humedadMin;
        this.humedadMax = humedadMax;
        this.frecuenciaHoras = frecuenciaHoras;
        this.coefConsumo = coefConsumo;
    }

    public int obtenerUmbralSugerido() {
        return (int) Math.round((humedadMin + humedadMax) / 2.0);
    }

    public String getNombre() { return nombre; }
    public int getHumedadMin() { return humedadMin; }
    public int getHumedadMax() { return humedadMax; }
    public int getFrecuenciaHoras() { return frecuenciaHoras; }
    public double getCoefConsumo() { return coefConsumo; }

    @Override
    public String toString() {
        return nombre + " (H:" + humedadMin + "-" + humedadMax + "%, freq:" + frecuenciaHoras + "h, coef:" + coefConsumo + ")";
    }
}
