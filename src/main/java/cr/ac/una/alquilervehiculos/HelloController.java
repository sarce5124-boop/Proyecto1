package cr.ac.una.alquilervehiculos;

import cr.ac.una.alquilervehiculos.model.Alquiler;
import cr.ac.una.alquilervehiculos.model.Bicimoto;
import cr.ac.una.alquilervehiculos.model.Cliente;
import cr.ac.una.alquilervehiculos.model.Cuadraciclo;
import cr.ac.una.alquilervehiculos.model.Moto;
import cr.ac.una.alquilervehiculos.model.Vehiculo;
import cr.ac.una.alquilervehiculos.service.AlquilerService;
import cr.ac.una.alquilervehiculos.service.ClienteService;
import cr.ac.una.alquilervehiculos.service.VehiculoService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class HelloController {

    // Servicios de aplicación: conservan los datos y aplican las reglas del negocio.
    private final VehiculoService vehiculoService = new VehiculoService();
    private final ClienteService clienteService = new ClienteService();
    private final AlquilerService alquilerService = new AlquilerService();

    // Controles de la sección de inventario.
    @FXML
    private ComboBox<String> tipoVehiculoCombo;
    @FXML
    private Label caracteristicaLabel;
    @FXML
    private TextField placaField;
    @FXML
    private TextField marcaField;
    @FXML
    private TextField tarifaField;
    @FXML
    private TextField caracteristicaField;
    @FXML
    private ListView<Vehiculo> vehiculosList;

    // Controles de la sección de clientes.
    @FXML
    private TextField identificacionField;
    @FXML
    private TextField nombreField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField correoField;
    @FXML
    private ListView<Cliente> clientesList;

    // Controles de la sección de alquileres.
    @FXML
    private ComboBox<Cliente> clienteAlquilerCombo;
    @FXML
    private ComboBox<Vehiculo> vehiculoAlquilerCombo;
    @FXML
    private DatePicker fechaInicioPicker;
    @FXML
    private Spinner<Integer> diasSpinner;
    @FXML
    private TextField depositoField;
    @FXML
    private Label costoAlquilerLabel;
    @FXML
    private Label totalAlquilerLabel;
    @FXML
    private ListView<Alquiler> alquileresActivosList;

    // Controles de la sección de devoluciones.
    @FXML
    private ComboBox<Alquiler> devolucionCombo;
    @FXML
    private DatePicker fechaDevolucionPicker;
    @FXML
    private Label multaLabel;
    @FXML
    private Label depositoDevolverLabel;
    @FXML
    private Label totalDevolucionLabel;

    /**
     * Configura los valores iniciales de los controles y conecta los cambios
     * de la interfaz con los cálculos que deben actualizarse automáticamente
     * durante el uso del sistema.
     */
    @FXML
    private void initialize() {
        tipoVehiculoCombo.getItems().addAll(
                "Moto", "Bicimoto", "Cuadraciclo"
        );
        tipoVehiculoCombo.getSelectionModel().selectFirst();
        actualizarCaracteristica();

        fechaInicioPicker.setValue(LocalDate.now());
        fechaDevolucionPicker.setValue(LocalDate.now());
        diasSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365, 1)
        );

        tipoVehiculoCombo.valueProperty().addListener(
                (observable, anterior, actual) -> actualizarCaracteristica()
        );
        vehiculoAlquilerCombo.valueProperty().addListener(
                (observable, anterior, actual) -> actualizarTotalAlquiler()
        );
        diasSpinner.valueProperty().addListener(
                (observable, anterior, actual) -> actualizarTotalAlquiler()
        );
        depositoField.textProperty().addListener(
                (observable, anterior, actual) -> actualizarTotalAlquiler()
        );
        devolucionCombo.valueProperty().addListener(
                (observable, anterior, actual) -> calcularDevolucion()
        );
        fechaDevolucionPicker.valueProperty().addListener(
                (observable, anterior, actual) -> calcularDevolucion()
        );

        actualizarListas();
    }

    /**
     * Valida los datos ingresados y registra un cliente nuevo.
     *
     * <p>La identificación y el nombre son obligatorios. El teléfono y el
     * correo se almacenan como datos complementarios del cliente. Después de
     * registrarlo, se limpian los campos y se actualizan las listas y combos
     * que utilizan clientes.</p>
     */
    @FXML
    private void registrarCliente() {
        try {
            validarTexto(identificacionField.getText(), "la identificación");
            validarTexto(nombreField.getText(), "el nombre");

            Cliente cliente = new Cliente(
                    identificacionField.getText().trim(),
                    nombreField.getText().trim(),
                    telefonoField.getText(),
                    correoField.getText()
            );
            clienteService.registrarCliente(cliente);
            limpiarCamposCliente();
            actualizarListas();
            mostrarInformacion("Cliente registrado correctamente.");
        } catch (RuntimeException exception) {
            mostrarError(exception.getMessage());
        }
    }

    /**
     * Construye y registra un vehículo según el tipo seleccionado.
     *
     * <p>Todos los vehículos requieren placa, marca y tarifa diaria. Además,
     * cada subtipo solicita su característica particular: cilindrada para
     * motos, tipo de motor para bicimotos y tipo de tracción para
     * cuadraciclos.</p>
     */
    @FXML
    private void registrarVehiculo() {
        try {
            String tipo = tipoVehiculoCombo.getValue();
            validarTexto(tipo, "el tipo de vehículo");
            validarTexto(placaField.getText(), "la placa");
            validarTexto(marcaField.getText(), "la marca");
            validarTexto(caracteristicaField.getText(), "la característica");

            String placa = placaField.getText().trim();
            String marca = marcaField.getText().trim();
            double tarifa = convertirDouble(tarifaField.getText(), "la tarifa diaria");
            String caracteristica = caracteristicaField.getText().trim();
            Vehiculo vehiculo;

            if ("Moto".equals(tipo)) {
                vehiculo = new Moto(
                        placa, marca, tarifa,
                        convertirEntero(caracteristica, "la cilindrada")
                );
            } else if ("Bicimoto".equals(tipo)) {
                vehiculo = new Bicimoto(placa, marca, tarifa, caracteristica);
            } else {
                vehiculo = new Cuadraciclo(placa, marca, tarifa, caracteristica);
            }

            vehiculoService.registrarVehiculo(vehiculo);
            limpiarCamposVehiculo();
            actualizarListas();
            mostrarInformacion("Vehículo registrado correctamente.");
        } catch (RuntimeException exception) {
            mostrarError(exception.getMessage());
        }
    }

    /**
     * Registra un alquiler para el cliente y el vehículo seleccionados.
     *
     * <p>El servicio valida que el vehículo esté disponible, calcula el costo
     * del alquiler más el depósito y cambia el estado del vehículo a
     * {@code ALQUILADO}. Al finalizar, las listas de la interfaz se
     * sincronizan con el nuevo estado.</p>
     */
    @FXML
    private void registrarAlquiler() {
        try {
            if (clienteAlquilerCombo.getValue() == null) {
                throw new IllegalArgumentException(
                        "Debe seleccionar un cliente."
                );
            }
            if (vehiculoAlquilerCombo.getValue() == null) {
                throw new IllegalArgumentException(
                        "Debe seleccionar un vehículo disponible."
                );
            }
            if (fechaInicioPicker.getValue() == null) {
                throw new IllegalArgumentException(
                        "Debe seleccionar la fecha de inicio."
                );
            }

            double deposito = convertirDouble(
                    depositoField.getText(), "el depósito de garantía"
            );
            Alquiler alquiler = alquilerService.registrarAlquiler(
                    clienteAlquilerCombo.getValue(),
                    vehiculoAlquilerCombo.getValue(),
                    fechaInicioPicker.getValue(),
                    diasSpinner.getValue(),
                    deposito
            );
            actualizarListas();
            mostrarInformacion(
                    "Alquiler #" + alquiler.getId()
                            + " registrado. Total inicial: "
                            + dinero(alquiler.calcularTotal())
            );
        } catch (RuntimeException exception) {
            mostrarError(exception.getMessage());
        }
    }

    /**
     * Calcula una vista previa de la devolución seleccionada.
     *
     * <p>El cálculo se ejecuta automáticamente cuando cambia el alquiler o la
     * fecha. La multa se cobra por separado y se compara con el depósito para
     * indicar claramente si se devuelve dinero o si el cliente debe pagar un
     * saldo adicional.</p>
     */
    @FXML
    private void calcularDevolucion() {
        Alquiler alquiler = devolucionCombo.getValue();
        LocalDate fecha = fechaDevolucionPicker.getValue();

        if (alquiler == null || fecha == null) {
            multaLabel.setText("₡0.00");
            depositoDevolverLabel.setText("₡0.00");
            totalDevolucionLabel.setText("₡0.00");
            return;
        }

        try {
            double multa = alquilerService.calcularMulta(alquiler, fecha);
            double deposito = alquilerService.calcularDepositoADevolver(
                    alquiler, fecha
            );
            multaLabel.setText(dinero(multa));
            depositoDevolverLabel.setText(dinero(deposito));
            double saldo = deposito - multa;
            if (saldo >= 0) {
                totalDevolucionLabel.setText(
                        "Se devuelve: " + dinero(saldo)
                );
            } else {
                totalDevolucionLabel.setText(
                        "Cliente debe pagar: " + dinero(Math.abs(saldo))
                );
            }
        } catch (RuntimeException exception) {
            multaLabel.setText("Fecha inválida");
            depositoDevolverLabel.setText("Fecha inválida");
            totalDevolucionLabel.setText("Fecha inválida");
        }
    }

    /**
     * Finaliza el alquiler y registra la devolución del vehículo.
     *
     * <p>El servicio calcula la multa, marca el alquiler como finalizado y
     * devuelve el vehículo al estado {@code DISPONIBLE}. Luego se actualizan
     * las listas para que el vehículo vuelva a aparecer como disponible.</p>
     */
    @FXML
    private void devolverVehiculo() {
        try {
            if (devolucionCombo.getValue() == null) {
                throw new IllegalArgumentException(
                        "Debe seleccionar un alquiler activo."
                );
            }
            if (fechaDevolucionPicker.getValue() == null) {
                throw new IllegalArgumentException(
                        "Debe seleccionar la fecha de devolución."
                );
            }

            Alquiler alquiler = devolucionCombo.getValue();
            double multa = alquilerService.devolverVehiculo(
                    alquiler, fechaDevolucionPicker.getValue()
            );
            actualizarListas();
            mostrarInformacion(
                    "Devolución registrada. Multa cobrada: "
                            + dinero(multa)
            );
        } catch (RuntimeException exception) {
            mostrarError(exception.getMessage());
        }
    }

    /**
     * Cambia el texto de la característica según el subtipo de vehículo.
     */
    private void actualizarCaracteristica() {
        String tipo = tipoVehiculoCombo.getValue();
        if ("Moto".equals(tipo)) {
            caracteristicaLabel.setText("Cilindrada (cc)");
        } else if ("Bicimoto".equals(tipo)) {
            caracteristicaLabel.setText("Tipo de motor");
        } else {
            caracteristicaLabel.setText("Tipo de tracción");
        }
    }

    /**
     * Actualiza el costo base y el total inicial del alquiler.
     *
     * <p>El total inicial incluye la tarifa diaria multiplicada por los días
     * seleccionados y el depósito ingresado. El depósito se valida nuevamente
     * al confirmar el alquiler.</p>
     */
    private void actualizarTotalAlquiler() {
        Vehiculo vehiculo = vehiculoAlquilerCombo.getValue();
        if (vehiculo == null || diasSpinner.getValue() == null) {
            costoAlquilerLabel.setText("₡0.00");
            totalAlquilerLabel.setText("₡0.00");
            return;
        }

        double costo = vehiculo.calcularCostoAlquiler(diasSpinner.getValue());
        double deposito = 0;
        try {
            deposito = convertirDouble(depositoField.getText(), "el depósito");
        } catch (IllegalArgumentException ignored) {
            // El campo se valida al confirmar el alquiler.
        }
        costoAlquilerLabel.setText(dinero(costo));
        totalAlquilerLabel.setText(dinero(costo + deposito));
    }

    /**
     * Recarga las listas y combos usando la información actual de los
     * servicios, incluyendo únicamente vehículos disponibles y alquileres
     * activos en las secciones correspondientes.
     */
    private void actualizarListas() {
        vehiculosList.getItems().setAll(vehiculoService.listarVehiculos());
        clientesList.getItems().setAll(clienteService.listarClientes());
        clienteAlquilerCombo.getItems().setAll(clienteService.listarClientes());
        vehiculoAlquilerCombo.getItems().setAll(vehiculoService.listarDisponibles());
        alquileresActivosList.getItems().setAll(
                alquilerService.listarAlquileresActivos()
        );
        devolucionCombo.getItems().setAll(
                alquilerService.listarAlquileresActivos()
        );
        actualizarTotalAlquiler();
        calcularDevolucion();
    }

    /**
     * Limpia los campos del formulario de clientes después de un registro
     * exitoso.
     */
    private void limpiarCamposCliente() {
        identificacionField.clear();
        nombreField.clear();
        telefonoField.clear();
        correoField.clear();
    }

    /**
     * Limpia los campos del formulario de inventario después de registrar un
     * vehículo correctamente.
     */
    private void limpiarCamposVehiculo() {
        placaField.clear();
        marcaField.clear();
        tarifaField.clear();
        caracteristicaField.clear();
    }

    /**
     * Verifica que un campo de texto contenga información útil.
     *
     * @param valor contenido ingresado en el campo.
     * @param campo nombre del dato que se mostrará en el mensaje de error.
     * @throws IllegalArgumentException si el valor es nulo o está vacío.
     */
    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe ingresar " + campo + "."
            );
        }
    }

    /**
     * Convierte un texto a un número entero positivo.
     *
     * @param valor texto que contiene el número.
     * @param campo nombre del campo que se está convirtiendo.
     * @return número entero ingresado.
     * @throws IllegalArgumentException si el texto no es numérico o no es
     *                                  mayor que cero.
     */
    private int convertirEntero(String valor, String campo) {
        try {
            int resultado = Integer.parseInt(valor.trim());
            if (resultado <= 0) {
                throw new IllegalArgumentException(
                        campo + " debe ser mayor que cero."
                );
            }
            return resultado;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Ingrese un valor numérico válido para " + campo + "."
            );
        }
    }

    /**
     * Convierte un texto a un número decimal positivo y finito.
     *
     * @param valor texto que contiene el número.
     * @param campo nombre del campo que se está convirtiendo.
     * @return número decimal ingresado.
     * @throws IllegalArgumentException si el texto no es numérico, no es
     *                                  finito o no es mayor que cero.
     */
    private double convertirDouble(String valor, String campo) {
        try {
            double resultado = Double.parseDouble(valor.trim());
            if (!Double.isFinite(resultado) || resultado <= 0) {
                throw new IllegalArgumentException(
                        campo + " debe ser un número mayor que cero."
                );
            }
            return resultado;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Ingrese un valor numérico válido para " + campo + "."
            );
        }
    }

    /**
     * Formatea un monto para mostrarlo como dinero en la interfaz.
     *
     * @param valor monto que se desea formatear.
     * @return monto con símbolo de moneda y dos decimales.
     */
    private String dinero(double valor) {
        return String.format("₡%,.2f", valor);
    }

    /**
     * Muestra un mensaje informativo después de completar una operación.
     *
     * @param mensaje texto que se presentará al usuario.
     */
    private void mostrarInformacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alquiler de vehículos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de error producido por una validación o regla del
     * negocio.
     *
     * @param mensaje descripción del problema encontrado.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje == null ? "Ocurrió un error." : mensaje);
        alert.showAndWait();
    }
}
