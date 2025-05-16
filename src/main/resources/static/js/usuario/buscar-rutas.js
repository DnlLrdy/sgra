// Inicializar el mapa
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

// Capa base
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

// Variables globales
let marcadores = [];

// Ícono personalizado para las paradas
const iconoPersonalizado = L.icon({
    iconUrl: 'https://cdn-icons-png.flaticon.com/512/684/684908.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32],
    popupAnchor: [0, -32]
});

// Función para limpiar los marcadores del mapa
function limpiarMarcadores() {
    marcadores.forEach(marker => map.removeLayer(marker));
    marcadores = [];
}

// Función para mostrar paradas en el mapa
function mostrarParadas(paradas) {
    if (!paradas || paradas.length === 0) return;

    limpiarMarcadores();

    const bounds = [];

    const lista = document.getElementById("paradas-list");
    if (lista) lista.innerHTML = '';

    paradas.forEach((parada, index) => {
        const latLng = [parada.latitud, parada.longitud];

        const marker = L.marker(latLng, { icon: iconoPersonalizado })
            .bindPopup(`<strong>${parada.nombre}</strong>`)
            .addTo(map);

        marcadores.push(marker);
        bounds.push(latLng);

        // Crear lista visual si existe el contenedor
        if (lista) {
            const isLongName = parada.nombre.length > 30;
            const item = document.createElement('li');
            item.className = 'list-group-item';
            item.innerHTML = `
                <div class="text-truncate-container">
                    <strong class="nombre-parada" ${isLongName ? 'data-bs-toggle="tooltip" data-bs-placement="top" title="' + parada.nombre + '"' : ''}>
                        ${index + 1}. ${parada.nombre}
                    </strong>
                    <br>
                    <small class="text-muted">Lat: ${parada.latitud.toFixed(5)}, Lng: ${parada.longitud.toFixed(5)}</small>
                </div>
            `;
            lista.appendChild(item);
        }
    });

    if (bounds.length > 0) {
        map.fitBounds(bounds, { padding: [50, 50] });
    }
}

// Escuchar clics en la lista de rutas
document.querySelectorAll('.ruta-item').forEach(item => {
    item.addEventListener('click', function (e) {
        e.preventDefault();

        const rutaId = this.getAttribute('data-id');
        if (!rutaId) return;

        fetch(`/sgra/usuario/ruta/${rutaId}/paradas`)
            .then(response => {
                if (!response.ok) throw new Error('Error al obtener las paradas');
                return response.json();
            })
            .then(data => {
                mostrarParadas(data);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('No se pudieron cargar las paradas.');
            });
    });
});
