package com.project.sgra;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

public class EncriptadorContraseñas {

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Utilidad para encriptar contraseñas ===");
        System.out.print("Ingresa una contraseña a encriptar: ");
        String contraseña = scanner.nextLine();

        String contraseñaEncriptada = encriptarContraseña(contraseña);
        System.out.println("Contraseña encriptada: " + contraseñaEncriptada);

        System.out.print("\n¿Deseas verificar una contraseña? (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if (respuesta.equals("s")) {
            System.out.print("Ingresa la contraseña a verificar: ");
            String contraseñaIngresada = scanner.nextLine();

            if (verificarContraseña(contraseñaIngresada, contraseñaEncriptada)) {
                System.out.println("✅ Contraseña correcta.");
            } else {
                System.out.println("❌ Contraseña incorrecta.");
            }
        }

        scanner.close();
    }

    public static String encriptarContraseña(String contraseña) {
        return encoder.encode(contraseña);
    }

    public static boolean verificarContraseña(String contraseña, String contraseñaEncriptada) {
        return encoder.matches(contraseña, contraseñaEncriptada);
    }

}
