import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Doctor {
    private int id;
    private String nombre;
    private String especialidad;

    public Doctor(int id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}

class Paciente {
    private int id;
    private String nombre;

    public Paciente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}

class Cita {
    private int id;
    private LocalDateTime fechaHora;
    private String motivo;
    private Doctor doctor;
    private Paciente paciente;

    public Cita(int id, LocalDateTime fechaHora, String motivo, Doctor doctor, Paciente paciente) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.doctor = doctor;
        this.paciente = paciente;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}

class Administrador {
    private String identificador;
    private String contraseña;

    public Administrador(String identificador, String contraseña) {
        this.identificador = identificador;
        this.contraseña = contraseña;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getContraseña() {
        return contraseña;
    }
}

class GestorCitas {
    private static final String RUTA_DOCTORES = "doctores.csv"; 
    private static final String RUTA_PACIENTES = "pacientes.csv";
    private static final String RUTA_CITAS = "citas.csv";
    private static final String SEPARADOR = ",";

    public void altaDoctor(Doctor doctor) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_DOCTORES, true))) {
            writer.write(doctor.getId() + SEPARADOR + doctor.getNombre() + SEPARADOR + doctor.getEspecialidad());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void altaPaciente(Paciente paciente) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_PACIENTES, true))) {
            writer.write(paciente.getId() + SEPARADOR + paciente.getNombre());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void crearCita(Cita cita) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_CITAS, true))) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String fechaHora = cita.getFechaHora().format(formatter);
            writer.write(cita.getId() + SEPARADOR + fechaHora + SEPARADOR + cita.getMotivo()
                    + SEPARADOR + cita.getDoctor().getId() + SEPARADOR + cita.getPaciente().getId());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Doctor> cargarDoctores() {
        List<Doctor> doctores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_DOCTORES))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(SEPARADOR);
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                String especialidad = datos[2];
                Doctor doctor = new Doctor(id, nombre, especialidad);
                doctores.add(doctor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctores;
    }

    public List<Paciente> cargarPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_PACIENTES))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(SEPARADOR);
                int id = Integer.parseInt(datos[0]);
                String nombre = datos[1];
                Paciente paciente = new Paciente(id, nombre);
                pacientes.add(paciente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pacientes;
    }

    public List<Cita> cargarCitas() {
        List<Cita> citas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RUTA_CITAS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(SEPARADOR);
                int id = Integer.parseInt(datos[0]);
                LocalDateTime fechaHora = LocalDateTime.parse(datos[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String motivo = datos[2];
                int idDoctor = Integer.parseInt(datos[3]);
                int idPaciente = Integer.parseInt(datos[4]);
                Doctor doctor = buscarDoctor(idDoctor);
                Paciente paciente = buscarPaciente(idPaciente);
                Cita cita = new Cita(id, fechaHora, motivo, doctor, paciente);
                citas.add(cita);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return citas;
    }

    Doctor buscarDoctor(int idDoctor) {
        List<Doctor> doctores = cargarDoctores();
        for (Doctor doctor : doctores) {
            if (doctor.getId() == idDoctor) {
                return doctor;
            }
        }
        return null;
    }

    protected Paciente buscarPaciente(int idPaciente) {
        List<Paciente> pacientes = cargarPacientes();
        for (Paciente paciente : pacientes) {
            if (paciente.getId() == idPaciente) {
                return paciente;
            }
        }
        return null;
    }
}

public class Main {
    private static final String USUARIO_ADMIN = "admin";
    private static final String CONTRASEÑA_ADMIN = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GestorCitas gestorCitas = new GestorCitas();

        System.out.println("Bienvenido al sistema de administración de citas");
        System.out.println("Ingrese su identificador de administrador:");
        String identificador = scanner.nextLine();
        System.out.println("Ingrese su contraseña:");
        String contraseña = scanner.nextLine();

        if (identificador.equals(USUARIO_ADMIN) && contraseña.equals(CONTRASEÑA_ADMIN)) {
            System.out.println("Inicio de sesión exitoso");
            int opcion;
            do {
                mostrarMenu();
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion) {
                    case 1:
                        altaDoctor(scanner, gestorCitas);
                        break;
                    case 2:
                        altaPaciente(scanner, gestorCitas);
                        break;
                    case 3:
                        crearCita(scanner, gestorCitas);
                        break;
                    case 4:
                        mostrarCitas(gestorCitas);
                        break;
                    case 5:
                        System.out.println("Saliendo del sistema...");
                        break;
                    default:
                        System.out.println("Opción inválida, intente nuevamente");
                        break;
                }
            } while (opcion != 5);
        } else {
            System.out.println("Credenciales de administrador incorrectas. No se puede acceder al sistema.");
        }
    }

    private static void mostrarMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. Dar de alta doctor");
        System.out.println("2. Dar de alta paciente");
        System.out.println("3. Crear cita");
        System.out.println("4. Mostrar citas");
        System.out.println("5. Salir");
        System.out.println("Seleccione una opción:");
    }

    private static void altaDoctor(Scanner scanner, GestorCitas gestorCitas) {
        System.out.println("\nAlta de doctor:");
        System.out.println("Ingrese el identificador único del doctor:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar el salto de línea
        System.out.println("Ingrese el nombre completo del doctor:");
        String nombre = scanner.nextLine();
        System.out.println("Ingrese la especialidad del doctor:");
        String especialidad = scanner.nextLine();

        Doctor doctor = new Doctor(id, nombre, especialidad);
        gestorCitas.altaDoctor(doctor);
        System.out.println("Doctor registrado exitosamente.");
    }

    private static void altaPaciente(Scanner scanner, GestorCitas gestorCitas) {
        System.out.println("\nAlta de paciente:");
        System.out.println("Ingrese el identificador único del paciente:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ingrese el nombre completo del paciente:");
        String nombre = scanner.nextLine();

        Paciente paciente = new Paciente(id, nombre);
        gestorCitas.altaPaciente(paciente);
        System.out.println("Paciente registrado exitosamente.");
    }

    private static void crearCita(Scanner scanner, GestorCitas gestorCitas) {
        System.out.println("\nCrear cita:");
        System.out.println("Ingrese el identificador único de la cita:");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ingrese la fecha y hora de la cita (yyyy-MM-dd HH:mm:ss):");
        String fechaHoraString = scanner.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraString, formatter);
        System.out.println("Ingrese el motivo de la cita:");
        String motivo = scanner.nextLine();
        System.out.println("Ingrese el identificador único del doctor:");
        int idDoctor = scanner.nextInt();
        System.out.println("Ingrese el identificador único del paciente:");
        int idPaciente = scanner.nextInt();

        Doctor doctor = gestorCitas.buscarDoctor(idDoctor);
        Paciente paciente = gestorCitas.buscarPaciente(idPaciente);

        if (doctor != null && paciente != null) {
            Cita cita = new Cita(id, fechaHora, motivo, doctor, paciente);
            gestorCitas.crearCita(cita);
            System.out.println("Cita creada exitosamente.");
        } else {
            System.out.println("Doctor o paciente no encontrado. No se pudo crear la cita.");
        }
    }

    private static void mostrarCitas(GestorCitas gestorCitas) {
        List<Cita> citas = gestorCitas.cargarCitas();

        if (!citas.isEmpty()) {
            System.out.println("\nCitas registradas:");
            for (Cita cita : citas) {
                System.out.println("ID: " + cita.getId());
                System.out.println("Fecha y hora: " + cita.getFechaHora());
                System.out.println("Motivo: " + cita.getMotivo());
                System.out.println("Doctor: " + cita.getDoctor().getNombre());
                System.out.println("Paciente: " + cita.getPaciente().getNombre());
                System.out.println();
            }
        } else {
            System.out.println("\nNo hay ninguna citas registrada.");
        }
    }
}
