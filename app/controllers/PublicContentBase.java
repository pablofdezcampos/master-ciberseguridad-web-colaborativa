package controllers;

import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register() {
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type) {
        // Verificar unicidad de la entidad
        User existingUser = User.findByUsername(username);

        User existingUser = User.findByUsername(username);
        if (existingUser != null) {
            flash().error("El nombre de usuario ya está en uso. Por favor, elija otro.");
            return redirect(routes.PublicContentBase.register()); // Volver al formulario de registro
        }

        // Verificar si las contraseñas coinciden
        if (!password.equals(passwordCheck)) {
            flash().error("Las contraseñas no coinciden. Por favor, inténtelo de nuevo.");
            return redirect(routes.PublicContentBase.register()); // Volver al formulario de registro
        }

        // Verificar si la contraseña cumple con las políticas de seguridad
        if (!isValidPassword(password)) {
            flash().error(
                    "La contraseña no cumple con los requisitos de seguridad. Por favor, elija una contraseña más segura.");
            return redirect(routes.PublicContentBase.register()); // Volver al formulario de registro
        }

        // Crear usuario y guardar en la base de datos
        String hashedPassword = HashUtils.getBcrypt(password); // Utilizar bcrypt para cifrar la contraseña
        User u = new User(username, hashedPassword, type, -1);
        u.save();
        return redirect(routes.PublicContentBase.registerComplete());

        User u = new User(username, HashUtils.getMd5(password), type, -1);
        u.save();
        registerComplete();
    }
    
    private static boolean isValidPassword(String password) {
        // Aplicar políticas de contraseñas seguras
        // Por ejemplo, verificar longitud mínima, uso de caracteres mixtos, etc.
        // Devuelve true si la contraseña es válida, false de lo contrario
        return password.length() >= 8 // Longitud mínima de 8 caracteres
                && password.matches(".*\\d.*") // Contiene al menos un dígito
                && password.matches(".*[a-z].*") // Contiene al menos una letra minúscula
                && password.matches(".*[A-Z].*") // Contiene al menos una letra mayúscula
                && password.matches(".*[!@#$%^&*()-+=\\[\\]{};:',.<>?/~_].*"); // Contiene al menos un carácter especial
    }

    public static void registerComplete() {
        render();
    }

}
