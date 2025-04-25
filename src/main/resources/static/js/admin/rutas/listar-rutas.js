// Inicialización del mapa con Leaflet
const map = L.map('map', {
    center: [10.395431, -75.472807], // Coordenadas iniciales
    zoom: 18,                        // Nivel de zoom inicial
    minZoom: 14,                     // Zoom mínimo permitido
    maxZoom: 18,                     // Zoom máximo permitido
    maxBounds: [                     // Límites de la vista del mapa
        [10.2000, -75.5700],         // Esquina inferior izquierda
        [10.5000, -75.3700]          // Esquina superior derecha
    ],
    maxBoundsViscosity: 1.0          // Evita que se salga del área definida
});

// Cargar capa de OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

// Variables globales
let marcadores = [];  // Array para almacenar los marcadores en el mapa

// Función para limpiar el mapa (eliminar los marcadores actuales)
function limpiarMapa() {
    marcadores.forEach(m => map.removeLayer(m)); // Elimina cada marcador del mapa
    marcadores = []; // Vacía el array de marcadores
}

// Función para mostrar las paradas de una ruta en el mapa
function mostrarParadas(paradas) {
    if (!paradas || paradas.length === 0) return; // Verificar si hay paradas disponibles

    const bounds = []; // Array para ajustar la vista del mapa a las paradas
    const lista = document.getElementById("paradas-list");
    lista.innerHTML = ''; // Limpiar la lista de paradas previa

    // Iterar sobre cada parada
    paradas.forEach((parada, index) => {
        const latLng = [parada.latitud, parada.longitud]; // Coordenadas de la parada

        // Crear un icono personalizado para cada parada según el color
        const iconoPersonalizado = L.icon({
            iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${parada.color}.png`,
            shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowSize: [41, 41]
        });

        // Crear el marcador en el mapa
        const marker = L.marker(latLng, { icon: iconoPersonalizado })
            .bindPopup(`<strong>${parada.nombre}</strong>`) // Vincular popup con el nombre de la parada
            .addTo(map);

        marcadores.push(marker); // Agregar el marcador al array
        bounds.push(latLng); // Agregar las coordenadas para ajustar la vista del mapa

        // Comprobar si el nombre de la parada es largo
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

        // Agregar evento de click para centrar el mapa en la parada y abrir el popup
        item.addEventListener('click', () => {
            map.setView(latLng, 18); // Centrar mapa en las coordenadas de la parada
            marker.openPopup(); // Abrir el popup del marcador
        });

        lista.appendChild(item); // Agregar el ítem de la parada a la lista
    });

    document.getElementById('paradas-container').classList.remove('d-none'); // Mostrar el contenedor de paradas
    map.fitBounds(bounds); // Ajustar el mapa para mostrar todas las paradas

    // Inicializar tooltips de Bootstrap para nombres largos de paradas
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(el => new bootstrap.Tooltip(el));
}

// Evento cuando se cambia la ruta en el selector
document.getElementById('selector-rutas').addEventListener('change', function () {
    const idSeleccionado = this.value; // Obtener el ID de la ruta seleccionada
    limpiarMapa(); // Limpiar el mapa de marcadores previos
    document.getElementById('paradas-list').innerHTML = ''; // Limpiar la lista de paradas
    document.getElementById('paradas-container').classList.add('d-none'); // Ocultar el contenedor de paradas

    const rutaSeleccionada = rutas.find(r => r.id === idSeleccionado); // Buscar la ruta seleccionada
    if (rutaSeleccionada) {
        mostrarParadas(rutaSeleccionada.paradas); // Mostrar las paradas de la ruta seleccionada
        // Mostrar los formularios de edición y eliminación para la ruta seleccionada
        document.getElementById("ruta-id-editar").value = idSeleccionado;
        document.getElementById("ruta-id-eliminar").value = idSeleccionado;
        document.getElementById("form-editar").style.display = "inline-block";
        document.getElementById("form-eliminar").style.display = "inline-block";
    } else {
        document.getElementById("form-editar").style.display = "none"; // Ocultar formulario de edición si no hay ruta
        document.getElementById("form-eliminar").style.display = "none"; // Ocultar formulario de eliminación si no hay ruta
    }
});

// Ejecutar al cargar la página
document.addEventListener("DOMContentLoaded", function () {
    if (rutas.length > 0) {
        const selector = document.getElementById("selector-rutas");
        selector.value = rutas[0].id; // Establecer la primera ruta como seleccionada
        selector.dispatchEvent(new Event('change')); // Activar evento para cargar las paradas de la ruta
    }
});

// Mostrar el modal de confirmación para eliminar la ruta
function mostrarModalEliminar() {
    const modal = new bootstrap.Modal(document.getElementById('modalConfirmarEliminar'));
    modal.show(); // Mostrar el modal
}

// Confirmar la eliminación de la ruta
function confirmarEliminacion() {
    document.getElementById('form-eliminar').submit(); // Enviar el formulario de eliminación
}
