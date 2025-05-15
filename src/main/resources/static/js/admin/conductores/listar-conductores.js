function configurarPasosModal() {
    let currentStep = 1;
    const totalSteps = 3;

    const steps = Array.from({ length: totalSteps }, (_, i) => document.getElementById(`step${i + 1}`));
    const prevButton = document.getElementById('prevButton');
    const nextButton = document.getElementById('nextButton');
    const submitButton = document.getElementById('submitButton');
    const modal = document.getElementById('modalCrearConductor');

    function mostrarPaso(step) {
        steps.forEach((s, i) => s.classList.toggle('d-none', i !== step - 1));
        prevButton.classList.toggle('d-none', step === 1);
        nextButton.classList.toggle('d-none', step === totalSteps);
        submitButton.classList.toggle('d-none', step !== totalSteps);
    }

    nextButton.addEventListener('click', () => {
        if (currentStep < totalSteps) {
            currentStep++;
            mostrarPaso(currentStep);
        }
    });

    prevButton.addEventListener('click', () => {
        if (currentStep > 1) {
            currentStep--;
            mostrarPaso(currentStep);
        }
    });

    modal.addEventListener('shown.bs.modal', () => {
        currentStep = 1;
        mostrarPaso(currentStep);
    });

    modal.addEventListener('hidden.bs.modal', () => {
        currentStep = 1;
        mostrarPaso(currentStep);
    });
}

function configurarPasosModalEditar() {
    let currentStep = 1;
    const totalSteps = 3;

    const steps = Array.from({ length: totalSteps }, (_, i) => document.getElementById(`editStep${i + 1}`));
    const prevButton = document.getElementById('prevButtonEditar');
    const nextButton = document.getElementById('nextButtonEditar');
    const submitButton = document.getElementById('submitButtonEditar');
    const modal = document.getElementById('modalEditarConductor');

    function mostrarPaso(step) {
        steps.forEach((s, i) => s.classList.toggle('d-none', i !== step - 1));
        prevButton.classList.toggle('d-none', step === 1);
        nextButton.classList.toggle('d-none', step === totalSteps);
        submitButton.classList.toggle('d-none', step !== totalSteps);
    }

    nextButton.addEventListener('click', () => {
        if (currentStep < totalSteps) {
            currentStep++;
            mostrarPaso(currentStep);
        }
    });

    prevButton.addEventListener('click', () => {
        if (currentStep > 1) {
            currentStep--;
            mostrarPaso(currentStep);
        }
    });

    modal.addEventListener('shown.bs.modal', () => {
        currentStep = 1;
        mostrarPaso(currentStep);
    });

    modal.addEventListener('hidden.bs.modal', () => {
        currentStep = 1;
        mostrarPaso(currentStep);
    });
}

function configurarVisibilidadContraseñas() {
    const passwordInput = document.getElementById('contraseña');
    const confirmPasswordInput = document.getElementById('confirmarContraseña');
    const togglePasswordIcon = document.getElementById('togglePassword');
    const toggleConfirmPasswordIcon = document.getElementById('toggleConfirmPassword');

    passwordInput.setAttribute('type', 'password');
    confirmPasswordInput.setAttribute('type', 'password');

    function toggleVisibility(inputElement, iconElement) {
        const isPassword = inputElement.getAttribute('type') === 'password';
        inputElement.setAttribute('type', isPassword ? 'text' : 'password');
        iconElement.classList.toggle('fa-eye');
        iconElement.classList.toggle('fa-eye-slash');
    }

    togglePasswordIcon.addEventListener('click', () => {
        toggleVisibility(passwordInput, togglePasswordIcon);
    });

    toggleConfirmPasswordIcon.addEventListener('click', () => {
        toggleVisibility(confirmPasswordInput, toggleConfirmPasswordIcon);
    });
}

function mostrarDetalleConductor(element) {
    document.querySelectorAll('.conductor-item').forEach(item => {
        item.classList.remove('selected');
    });

    const item = element.closest('.conductor-item');
    item.classList.add('selected');

    const datos = {
        id: element.getAttribute("data-id") || '---',
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

    document.getElementById('detalle-id').value = datos.id;
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
    document.getElementById('detalle-estado').value = datos.estado.toUpperCase().replace(/\s+/g, '_');
}

function agregarConductor() {
    const modal = new bootstrap.Modal(document.getElementById('modalCrearConductor'));
    modal.show();
}

function editarConductor(button) {
    const datos = {
        id: button.getAttribute("data-id"),
        primerNombre: button.getAttribute("data-primerNombre"),
        primerNombre: button.getAttribute("data-primerNombre"),
        segundoNombre: button.getAttribute("data-segundoNombre"),
        primerApellido: button.getAttribute("data-primerApellido"),
        segundoApellido: button.getAttribute("data-segundoApellido"),
        fechaNacimiento: button.getAttribute("data-fechaNacimiento"),
        tipoDocumento: button.getAttribute("data-tipoDocumento"),
        numeroDocumento: button.getAttribute("data-numeroDocumento"),
        numeroLicencia: button.getAttribute("data-numeroLicencia"),
        fechaVencimientoLicencia: button.getAttribute("data-fechaVencimientoLicencia"),
        correoElectronico: button.getAttribute("data-correoElectronico"),
        telefono: button.getAttribute("data-telefono"),
        nombreUsuario: button.getAttribute("data-nombreUsuario")
    };

    document.getElementById('conductor-id-edit').value = datos.id;
    document.getElementById('conductor-primerNombre-edit').value = datos.primerNombre;
    document.getElementById('conductor-segundoNombre-edit').value = datos.segundoNombre;
    document.getElementById('conductor-primerApellido-edit').value = datos.primerApellido;
    document.getElementById('conductor-segundoApellido-edit').value = datos.segundoApellido;
    document.getElementById('conductor-fechaNacimiento-edit').value = datos.fechaNacimiento;
    document.getElementById('conductor-tipoDocumento-edit').value = datos.tipoDocumento;
    document.getElementById('conductor-numeroDocumento-edit').value = datos.numeroDocumento;
    document.getElementById('conductor-numeroLicencia-edit').value = datos.numeroLicencia;
    document.getElementById('conductor-fechaVencimiento-edit').value = datos.fechaVencimientoLicencia;
    document.getElementById('conductor-correoElectronico-edit').value = datos.correoElectronico;
    document.getElementById('conductor-telefono-edit').value = datos.telefono;
    document.getElementById('conductor-nombreUsuario-edit').value = datos.nombreUsuario;

    const modal = new bootstrap.Modal(document.getElementById('modalEditarConductor'));
    modal.show();
}

function eliminarConductor(button) {
    const id = button.getAttribute("data-id");
    document.getElementById("conductorIdEliminar").value = id;

    const modal = new bootstrap.Modal(document.getElementById("modalConfirmarEliminarConductor"));
    modal.show();
}

document.addEventListener("DOMContentLoaded", function () {
    configurarVisibilidadContraseñas();
    configurarPasosModal();
    configurarPasosModalEditar();

    const primerConductor = document.querySelector(".conductor-info");
    if (primerConductor) {
        mostrarDetalleConductor(primerConductor);
    }
});

function mostrarToast(icon, mensaje, colorIcono) {
    Swal.mixin({
        toast: true,
        position: 'top',
        showConfirmButton: false,
        timer: 3500,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer);
            toast.addEventListener('mouseleave', Swal.resumeTimer);
        },
        customClass: { popup: 'swal-popup' },
    }).fire({
        icon: icon,
        title: mensaje,
        iconColor: colorIcono,
    });
}

function mostrarToast(icon, mensaje, colorIcono) {
    const Toast = Swal.mixin({
        toast: true,
        position: 'top',
        showConfirmButton: false,
        timer: 3500,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.addEventListener('mouseenter', Swal.stopTimer);
            toast.addEventListener('mouseleave', Swal.resumeTimer);
        },
        customClass: { popup: 'swal-popup' },
    });

    const cerrarAlClickFuera = (e) => {
        const popup = document.querySelector('.swal-popup');
        if (popup && !popup.contains(e.target)) {
            Swal.close();
            document.removeEventListener('click', cerrarAlClickFuera);
        }
    };

    document.addEventListener('click', cerrarAlClickFuera);

    Toast.fire({
        icon: icon,
        title: mensaje,
        iconColor: colorIcono,
    });
}

function cambiarEstadoConductor() {
    const select = document.getElementById("detalle-estado");
    const nuevoEstado = select.value;
    const id = document.getElementById('detalle-id').value;

    fetch('/sgra/admin/conductores/actualizar-estado', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: new URLSearchParams({ id: id, estado: nuevoEstado })
    })
        .then(async response => {
            const mensaje = await response.text();
            if (response.ok) {
                mostrarToast('success', mensaje || 'Estado actualizado correctamente', '#27ae60');
            } else {
                mostrarToast('error', mensaje || 'Error al actualizar estado', '#e74c3c');
            }
        })
        .catch(() => {
            mostrarToast('error', 'Error en la comunicación con el servidor', '#e74c3c');
        });
}
