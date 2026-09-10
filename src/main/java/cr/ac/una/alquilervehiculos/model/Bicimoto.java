package cr.ac.una.alquilervehiculos.model;

public class Bicimoto extends Vehiculo {

    private String tipoMotor;

    /**
     * Crea una bicimoto con sus datos principales.
     *
     * @param placa placa de la bicimoto.
     * @param marca marca de la bicimoto.
     * @param tarifaDiaria precio del alquiler por día.
     * @param tipoMotor tipo de motor de la bicimoto.
     */
    public Bicimoto(String placa, String marca, double tarifaDiaria, String tipoMotor) {
        super(placa, marca, tarifaDiaria);

        if (tipoMotor == null || tipoMotor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El tipo de motor es obligatorio."
            );
        }

        this.tipoMotor = tipoMotor;
    }

    /**
     * Obtiene el tipo de motor de la bicimoto.
     *
     * @return tipo de motor.
     */
    public String getTipoMotor() {
        return tipoMotor;
    }

    /**
     * Modifica el tipo de motor de la bicimoto.
     *
     * @param tipoMotor nuevo tipo de motor.
     */
    public void setTipoMotor(String tipoMotor) {
        if (tipoMotor == null || tipoMotor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El tipo de motor es obligatorio."
            );
        }

        this.tipoMotor = tipoMotor;
    }

    /**
     * Obtiene una representación de la bicimoto en texto.
     *
     * @return información principal de la bicimoto.
     */
    @Override
    public String toString() {
        return "Bicimoto - " + super.toString() + " - " + tipoMotor;
    }
}