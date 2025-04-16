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

document.getElementById('selector-rutas').addEventListener('change', function () {
    const idSeleccionado = this.value;

    limpiarMapa();

    document.getElementById('paradas-list').innerHTML = '';
    document.getElementById('paradas-container').classList.add('d-none');

    const rutaSeleccionada = rutas.find(r => r.id === idSeleccionado);
    if (rutaSeleccionada) {
        mostrarParadas(rutaSeleccionada.paradas);
    }
});

function mostrarParadas(paradas) {
    if (!paradas || paradas.length === 0) return;

    const bounds = [];
    const lista = document.getElementById("paradas-list");
    lista.innerHTML = '';

    paradas.forEach((parada, index) => {

        const marker = L.marker([parada.latitud, parada.longitud])
            .bindPopup(`<strong>${parada.nombre}</strong>`)
            .addTo(map);
        marcadores.push(marker);
        bounds.push([parada.latitud, parada.longitud]);

        const item = document.createElement('li');
        item.className = 'list-group-item';
        item.innerHTML = `
            <strong>${index + 1}. ${parada.nombre}</strong><br>
            <small class="text-muted">Latitud: ${parada.latitud.toFixed(5)}, Longitud: ${parada.longitud.toFixed(5)}</small>
        `;
        lista.appendChild(item);
    });

    document.getElementById('paradas-container').classList.remove('d-none');
    map.fitBounds(bounds);
}
