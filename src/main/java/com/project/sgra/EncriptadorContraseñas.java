package com.project.sgra;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

public class EncriptadorContraseñas {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Utilidad para encriptar contraseñas ===");
        System.out.print("Ingresa una contraseña a hashear: ");
        String contrasena = scanner.nextLine();

        String contrasenaHasheada = hashearContrasena(contrasena);
        System.out.println("Contraseña hasheada: " + contrasenaHasheada);

        System.out.print("\n¿Deseas verificar una contraseña? (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if (respuesta.equals("s")) {
            System.out.print("Ingresa la contraseña a verificar: ");
            String contrasenaIngresada = scanner.nextLine();

            if (verificarContrasena(contrasenaIngresada, contrasenaHasheada)) {
                System.out.println("✅ Contraseña correcta.");
            } else {
                System.out.println("❌ Contraseña incorrecta.");
            }
        }

        scanner.close();
    }

    public static String hashearContrasena(String contrasena) {
        return encoder.encode(contrasena);
    }

    public static boolean verificarContrasena(String contrasena, String contrasenaHasheada) {
        return encoder.matches(contrasena, contrasenaHasheada);
    }

}
