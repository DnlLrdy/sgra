const map = L.map('map', {
    center: [10.395431, -75.472807],
    zoom: 18,
    minZoom: 14,
    maxZoom: 18,
    maxBounds: [
        [10.2000, -75.5700],
        [10.5000, -75.3700]
    ],
    maxBoundsViscosity: 1.0
});

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

let marcadores = [];

function limpiarMapa() {
    marcadores.forEach(m => map.removeLayer(m));
    marcadores = [];
}

function mostrarParadas(paradas) {
    if (!paradas || paradas.length === 0) return;

    const bounds = [];
    const lista = document.getElementById("paradas-list");
    lista.innerHTML = '';

    paradas.forEach((parada, index) => {
        const latLng = [parada.latitud, parada.longitud];
        const marker = L.marker(latLng).bindPopup(`<strong>${parada.nombre}</strong>`).addTo(map);
        marcadores.push(marker);
        bounds.push(latLng);

        const isLongName = parada.nombre.length > 30;
        const item = document.createElement('li');
        item.className = 'list-group-item';
        item.innerHTML = `
            <div class="text-truncate-container">
                <strong class="nombre-parada" ${isLongName ? 'data-bs-toggle="tooltip" data-bs-placement="top" title="' + parada.nombre + '"' : ''}>
                    ${index + 1}. ${parada.nombre}
                </strong>
                <small class="text-muted">Latitud: ${parada.latitud.toFixed(5)}, Longitud: ${parada.longitud.toFixed(5)}</small>
            </div>
        `;
        item.addEventListener('click', () => {
            map.setView(latLng, 18);
            marker.openPopup();
        });

        lista.appendChild(item);
    });

    document.getElementById('paradas-container').classList.remove('d-none');
    map.fitBounds(bounds);

    // Inicializar tooltips para nombres largos
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(el => new bootstrap.Tooltip(el));
}

document.getElementById('selector-rutas').addEventListener('change', function () {
    const idSeleccionado = this.value;
    limpiarMapa();
    document.getElementById('paradas-list').innerHTML = '';
    document.getElementById('paradas-container').classList.add('d-none');

    const rutaSeleccionada = rutas.find(r => r.id === idSeleccionado);
    if (rutaSeleccionada) {
        mostrarParadas(rutaSeleccionada.paradas);
        document.getElementById("ruta-id-editar").value = idSeleccionado;
        document.getElementById("ruta-id-eliminar").value = idSeleccionado;
        document.getElementById("form-editar").style.display = "inline-block";
        document.getElementById("form-eliminar").style.display = "inline-block";
    } else {
        document.getElementById("form-editar").style.display = "none";
        document.getElementById("form-eliminar").style.display = "none";
    }
});

// Inicializar paradas al cargar la página
document.addEventListener("DOMContentLoaded", function () {
    if (rutas.length > 0) {
        const selector = document.getElementById("selector-rutas");
        selector.value = rutas[0].id;
        selector.dispatchEvent(new Event('change'));
    }
});

function mostrarModalEliminar() {
    const modal = new bootstrap.Modal(document.getElementById('modalConfirmarEliminar'));
    modal.show();
}

function confirmarEliminacion() {
    document.getElementById('form-eliminar').submit();
}