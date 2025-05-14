// === Visibilidad de contraseñas ===
const passwordInput = document.getElementById('contraseña');
const confirmPasswordInput = document.getElementById('confirmarContraseña');
const togglePasswordIcon = document.getElementById('togglePassword');
const toggleConfirmPasswordIcon = document.getElementById('toggleConfirmPassword');

// Alternar visibilidad de un campo de contraseña
function togglePasswordVisibility(inputElement, iconElement) {
    const type = inputElement.getAttribute('type') === 'password' ? 'text' : 'password';
    inputElement.setAttribute('type', type);
    iconElement.classList.toggle('fa-eye');
    iconElement.classList.toggle('fa-eye-slash');
}

// Asegurar que los campos de contraseña estén ocultos al cargar
window.onload = function () {
    passwordInput.setAttribute('type', 'password');
    confirmPasswordInput.setAttribute('type', 'password');
};

// Listeners para mostrar/ocultar contraseñas
togglePasswordIcon.addEventListener('click', function () {
    togglePasswordVisibility(passwordInput, this);
});

toggleConfirmPasswordIcon.addEventListener('click', function () {
    togglePasswordVisibility(confirmPasswordInput, this);
});

function mostrarDetalleConductor(element) {
    // Quitar selección previa
    document.querySelectorAll('.conductor-item').forEach(item => {
        item.classList.remove('selected');
    });

    // Marcar como seleccionado
    const item = element.closest('.conductor-item');
    item.classList.add('selected');

    // Obtener datos del atributo data-*
    const datos = {
        primerNombre: element.getAttribute("data-primerNombre") || '---',
        segundoNombre: element.getAttribute("data-segundoNombre") || '---',
        primerApellido: element.getAttribute("data-primerApellido") || '---',
        segundoApellido: element.getAttribute("data-segundoApellido") || '---',
        fechaNacimiento: element.getAttribute("data-fechaNacimiento") || '---',
        tipoDocumento: element.getAttribute("data-tipoDocumento") || '---',
        numeroDocumento: element.getAttribute("data-numeroDocumento") || '---',
        numeroLicencia: element.getAttribute("data-numeroLicencia") || '---',
        fechaVencimientoLicencia: element.getAttribute("data-fechaVencimientoLicencia") || '---',
        correoElectronico: element.getAttribute("data-correoElectronico") || '---',
        telefono: element.getAttribute("data-telefono") || '---',
        nombreUsuario: element.getAttribute("data-nombreUsuario") || '---',
        estado: element.getAttribute("data-estado") || '---'
    };

    // Mostrar los datos en los detalles
    document.getElementById('detalle-primerNombre').textContent = datos.primerNombre;
    document.getElementById('detalle-segundoNombre').textContent = datos.segundoNombre;
    document.getElementById('detalle-primerApellido').textContent = datos.primerApellido;
    document.getElementById('detalle-segundoApellido').textContent = datos.segundoApellido;
    document.getElementById('detalle-fechaNacimiento').textContent = datos.fechaNacimiento;
    document.getElementById('detalle-tipoDocumento').textContent = datos.tipoDocumento;
    document.getElementById('detalle-numeroDocumento').textContent = datos.numeroDocumento;
    document.getElementById('detalle-numeroLicencia').textContent = datos.numeroLicencia;
    document.getElementById('detalle-fechaVencimiento').textContent = datos.fechaVencimientoLicencia;
    document.getElementById('detalle-correo').textContent = datos.correoElectronico;
    document.getElementById('detalle-telefono').textContent = datos.telefono;
    document.getElementById('detalle-usuario').textContent = datos.nombreUsuario;
    document.getElementById('detalle-estado').textContent = datos.estado;
}


document.addEventListener("DOMContentLoaded", function () {
    const primerConductor = document.querySelector(".conductor-info");
    if (primerConductor) {
        mostrarDetalleConductor(primerConductor);
    }
});

function eliminarConductor(button) {
    const id = button.getAttribute("data-id");
    document.getElementById("conductorIdEliminar").value = id;

    const modal = new bootstrap.Modal(document.getElementById("modalConfirmarEliminarConductor"));
    modal.show();
}
