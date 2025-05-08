function mostrarDetalleDesdeData(element) {
    // Remueve la clase 'selected' de todos los items
    document.querySelectorAll('.bus-item').forEach(item => {
        item.classList.remove('selected');
    });

    // Agrega la clase 'selected' al autobús actual
    const item = element.closest('.bus-item');
    item.classList.add('selected');

    // Mostrar los detalles
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
    document.getElementById('estado-autobus-edit').value = estado;

    const modal = new bootstrap.Modal(document.getElementById('modalEditarAutobus'));
    modal.show();
}

function eliminarAutobus(button) {
    const id = button.getAttribute("data-id");
    document.getElementById("autobusIdEliminar").value = id;

    const modal = new bootstrap.Modal(document.getElementById("modalConfirmarEliminarAutobus"));
    modal.show();
}
