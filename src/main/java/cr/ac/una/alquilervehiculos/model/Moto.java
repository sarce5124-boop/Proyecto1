package cr.ac.una.alquilervehiculos.model;

public class Moto extends Vehiculo {

    private int cilindrada;

    /**
     * Crea una moto con sus datos principales.
     *
     * @param placa placa de la moto.
     * @param marca marca de la moto.
     * @param tarifaDiaria precio del alquiler por día.
     * @param cilindrada cilindrada de la moto en cc.
     */
    public Moto(String placa, String marca, double tarifaDiaria, int cilindrada) {
        super(placa, marca, tarifaDiaria);
        this.cilindrada = cilindrada;
    }

    /**
     * Obtiene la cilindrada de la moto.
     *
     * @return cilindrada en cc.
     */
    public int getCilindrada() {
        return cilindrada;
    }

    /**
     * Modifica la cilindrada de la moto.
     *
     * @param cilindrada nueva cilindrada en cc.
     */
    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    /**
     * Obtiene una representación de la moto en texto.
     *
     * @return información principal de la moto.
     */
    @Override
    public String toString() {
        return "Moto - " + super.toString() + " - " + cilindrada + " cc";
    }
}