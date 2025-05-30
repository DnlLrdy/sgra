function mostrarDetalleDesdeData(element) {
    // Remueve la clase 'selected' de todos los items
    document.querySelectorAll('.bus-item').forEach(item => {
        item.classList.remove('selected');
    });

    // Agrega la clase 'selected' al autobús actual
    const item = element.closest('.bus-item');
    item.classList.add('selected');

    // Mostrar los detalles del autobús
    const id = element.getAttribute('data-id');
    const placa = element.getAttribute('data-matricula');
    const modelo = element.getAttribute('data-modelo');
    const capacidad = element.getAttribute('data-capacidad');
    const estado = element.getAttribute('data-estado');
    const ruta = element.getAttribute('data-ruta');

    document.getElementById('detalle-placa').textContent = placa;
    document.getElementById('detalle-modelo').textContent = modelo;
    document.getElementById('detalle-capacidad').textContent = capacidad;
    document.getElementById('detalle-estado').textContent = estado;
    document.getElementById('detalle-ruta').textContent = ruta && ruta.trim() !== ""
        ? ruta
        : "No se ha vinculado con ninguna ruta";

    // Mostrar los detalles del conductor

    const primerNombre = element.getAttribute('data-conductor-primerNombre');
    const segundoNombre = element.getAttribute('data-conductor-segundoNombre');
    const primerApellido = element.getAttribute('data-conductor-primerApellido');
    const segundoApellido = element.getAttribute('data-conductor-segundoApellido');
    const tipoDocumento = element.getAttribute('data-conductor-tipoDocumento');
    const numeroDocumento = element.getAttribute('data-conductor-numeroDocumento');
    const conductorEstado = element.getAttribute('data-conductor-estado');

    // Verificamos si se asignó algún dato al conductor.
    const nombresConductor = `${primerNombre || ""} ${segundoNombre || ""} ${primerApellido || ""} ${segundoApellido || ""}`.trim();
    const documentoConductor = `${tipoDocumento || ""} ${numeroDocumento || ""}`.trim();

    document.getElementById('detalle-conductor-nombres').textContent =
        nombresConductor !== "" ? nombresConductor : "";

    document.getElementById('detalle-conductor-documento').textContent =
        documentoConductor !== "" ? documentoConductor : "";

    document.getElementById('detalle-conductor-estado').textContent = conductorEstado;

    document.getElementById('btn-desvincular-conductor').value = id;
}


document.addEventListener("DOMContentLoaded", function () {
    const primerBusInfo = document.querySelector('.bus-item .bus-info');
    if (primerBusInfo) {
        mostrarDetalleDesdeData(primerBusInfo);
    }
});

function agregarAutobus() {
    const modal = new bootstrap.Modal(document.getElementById('modalCrearAutobus'));
    modal.show();
}

function modificarAutobus(button) {
    const id = button.getAttribute("data-id");
    const matricula = button.getAttribute("data-matricula");
    const modelo = button.getAttribute("data-modelo");
    const capacidad = button.getAttribute("data-capacidad");
    const estado = button.getAttribute("data-estado");

    document.getElementById('autobus-id-edit').value = id;
    document.getElementById('matricula-autobus-edit').value = matricula;
    document.getElementById('modelo-autobus-edit').value = modelo;
    document.getElementById('capacidad-autobus-edit').value = capacidad;
    document.getElementById('estado-autobus-edit').value = estado.toUpperCase().replace(/\s+/g, '_');

    const modal = new bootstrap.Modal(document.getElementById('modalEditarAutobus'));
    modal.show();
}

function eliminarAutobus(button) {
    const id = button.getAttribute("data-id");
    document.getElementById("autobusIdEliminar").value = id;

    const modal = new bootstrap.Modal(document.getElementById("modalConfirmarEliminarAutobus"));
    modal.show();
}

function abrirModalVincularConductor() {
    const selectedBus = document.querySelector('.bus-item.selected button[data-id]');

    if (!selectedBus) {
        mostrarToast('warning', 'No hay ningún autobús seleccionado.', '#f39c12');
        return;
    }

    const busId = selectedBus.getAttribute('data-id');
    if (!busId) {
        mostrarToast('warning', 'El autobús seleccionado no tiene un ID válido.', '#f39c12');
        return;
    }

    document.getElementById('vincular-autobus-id').value = busId;

    const modal = new bootstrap.Modal(document.getElementById('modalVincularConductor'));
    modal.show();
}

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


function desvincularConductor(button) {
    const id = button.value;

    if (!id) {
        mostrarToast('warning', 'Seleccione un autobús primero.', '#f39c12');
        return;
    }

    Swal.fire({
        title: '¿Estás seguro?',
        text: "Esto desvinculará al conductor del autobús.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#aaa',
        confirmButtonText: 'Sí, desvincular'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/sgra/admin/autobuses/desvincular-conductor/${id}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            }).then(async response => {
                const mensaje = await response.text();
                if (response.ok) {
                    mostrarToast('success', mensaje || 'Conductor desvinculado correctamente', '#27ae60');
                    setTimeout(() => {
                        window.location.reload();
                    }, 1500); 
                } else {
                    mostrarToast('error', mensaje || 'Error al desvincular conductor', '#e74c3c');
                }

            }).catch(() => {
                mostrarToast('error', 'Error en la comunicación con el servidor', '#e74c3c');
            });
        }
    });
}

