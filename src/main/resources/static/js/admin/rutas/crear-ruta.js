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
let paradas = [];  // Array para almacenar las paradas
let marcadores = [];  // Array para almacenar los marcadores en el mapa

// Verificar si hay datos de paradas guardados y cargarlos
if (paradasJson?.trim()) {
    paradas = JSON.parse(paradasJson); // Convertir el JSON a objeto

    // Crear los marcadores en el mapa para cada parada
    paradas.forEach(parada => {
        const icono = L.icon({
            iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${parada.color}.png`,
            shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowSize: [41, 41]
        });

        // Crear marcador y añadirlo al mapa
        const marcador = L.marker([parada.latitud, parada.longitud], {
            draggable: true, icon: icono
        }).addTo(map).bindPopup(parada.nombre).on('dragend', e => {
            const { lat, lng } = e.target.getLatLng();
            parada.latitud = lat;
            parada.longitud = lng;
            marcador.setPopupContent(parada.nombre);
            actualizarListaParadas();  // Actualizar la lista de paradas
        });

        marcadores.push(marcador);  // Añadir marcador a la lista de marcadores
    });

    actualizarListaParadas(); // Actualizar la lista en la UI
}

// Manejar el clic derecho sobre el mapa para crear nuevas paradas
map.on('contextmenu', e => {
    const modal = new bootstrap.Modal(document.getElementById('modalCrearParada'));
    modal.show();
    const ubicacion = { lat: e.latlng.lat, lng: e.latlng.lng };

    // Acción al guardar una nueva parada
    document.getElementById('guardar-parada').onclick = function () {
        const nombre = document.getElementById('nombre-parada').value.trim();
        const color = document.getElementById('color-parada').value;

            const parada = { nombre, latitud: ubicacion.lat, longitud: ubicacion.lng, color };
            paradas.push(parada);  // Añadir nueva parada al array

            // Crear el icono del marcador para la nueva parada
            const icono = L.icon({
                iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${color}.png`,
                shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });

            // Crear el marcador y añadirlo al mapa
            const marcador = L.marker([parada.latitud, parada.longitud], {
                draggable: true, icon: icono
            }).addTo(map).bindPopup(nombre).on('dragend', e => {
                const { lat, lng } = e.target.getLatLng();
                parada.latitud = lat;
                parada.longitud = lng;
                marcador.setPopupContent(parada.nombre);
                actualizarListaParadas(); // Actualizar la lista de paradas
            });

            marcadores.push(marcador);  // Añadir marcador a la lista
            actualizarListaParadas(); // Actualizar la lista en la UI
            modal.hide(); // Cerrar el modal

    };
});

// Función para actualizar la lista de paradas en la UI
function actualizarListaParadas() {
    const lista = document.getElementById('paradas-list');
    lista.innerHTML = ''; // Limpiar la lista actual

    const textoArrastrar = document.getElementById('texto-arrastrar');
    const alerta = document.getElementById('sin-paradas-alerta');

    // Si no hay paradas, mostrar mensaje de alerta
    if (paradas.length === 0) {
        if (!alerta) {
            const div = document.createElement("div");
            div.id = "sin-paradas-alerta";
            div.className = "alert alert-dark text-center";
            div.innerHTML = `<strong>¡No hay paradas creadas!</strong><br>Haz clic derecho en el mapa para crear una.`;
            lista.appendChild(div);
        }
        if (textoArrastrar) textoArrastrar.style.display = "none"; // Ocultar mensaje de arrastre
    } else {
        if (alerta) alerta.remove();  // Eliminar mensaje de alerta si hay paradas
        if (textoArrastrar) textoArrastrar.style.display = "block";  // Mostrar mensaje de arrastre
    }

    // Mostrar cada parada en la lista con sus botones de edición y eliminación
    paradas.forEach((p, i) => {
        const isLongName = p.nombre.length > 30;
        const li = document.createElement('li');
        li.className = 'list-group-item';

        const nombreParada = `
            <div class="d-flex flex-column flex-grow-1 overflow-hidden me-3">
                <div class="d-flex align-items-center overflow-hidden">
                    <i class="bi bi-grip-vertical me-2 text-secondary flex-shrink-0"></i>
                    <strong class="nombre-parada flex-grow-1" ${isLongName ? `data-bs-toggle="tooltip" data-bs-placement="top" title="${p.nombre}"` : ''}>
                        ${i + 1}. ${p.nombre}
                    </strong>
                </div>
                <small class="text-muted">Latitud: ${p.latitud.toFixed(5)}, Longitud: ${p.longitud.toFixed(5)}</small>
            </div>
            <div class="d-flex align-items-center gap-1 flex-shrink-0">
                <button type="button" class="btn btn-sm btn-outline-warning" onclick="modificarParada(${i})">
                    <i class="bi bi-pencil-square"></i>
                </button>
                <button type="button" class="btn btn-sm btn-outline-danger" onclick="eliminarParada(${i})">
                    <i class="bi bi-trash"></i> 
                </button>
            </div>
        `;

        li.innerHTML = nombreParada;

        // Evento para centrar el mapa en la parada al hacer clic
        li.addEventListener('click', () => {
            const marker = marcadores[i];
            const latLng = [p.latitud, p.longitud];
            map.setView(latLng, 18);
            marker.openPopup();
        });

        lista.appendChild(li);  // Añadir la parada a la lista
    });

    // Inicializar tooltips para nombres largos de paradas
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(el => new bootstrap.Tooltip(el));

    // Actualizar el valor de las paradas en formato JSON en un campo oculto
    document.getElementById('paradas-input').value = JSON.stringify(paradas);
}

// Función para modificar una parada
function modificarParada(index) {
    const parada = paradas[index];
    const modal = new bootstrap.Modal(document.getElementById('modalEditarParada'));
    const nombreInput = document.getElementById('nombre-parada-edit');
    const colorInput = document.getElementById('color-parada-edit');
    const guardarBtn = document.getElementById('guardar-edicion');

    nombreInput.value = parada.nombre;  // Prellenar los valores actuales
    colorInput.value = parada.color;

    modal.show();

    // Acción al guardar la edición
    guardarBtn.onclick = () => {
        const nuevoNombre = nombreInput.value.trim();
        const nuevoColor = colorInput.value;

        let hayCambios = false;

        // Si el nombre cambió, actualizarlo
        if (nuevoNombre && nuevoNombre !== parada.nombre) {
            parada.nombre = nuevoNombre;
            marcadores[index].setPopupContent(nuevoNombre);
            hayCambios = true;
        }

        // Si el color cambió, actualizarlo
        if (nuevoColor && nuevoColor !== parada.color) {
            parada.color = nuevoColor;

            // Crear el nuevo icono para el marcador
            const nuevoIcono = L.icon({
                iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${nuevoColor}.png`,
                shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });

            marcadores[index].setIcon(nuevoIcono);  // Cambiar el icono del marcador
            hayCambios = true;
        }

        if (hayCambios) {
            actualizarListaParadas();  // Actualizar la lista si hubo cambios
        }

        modal.hide();  // Cerrar el modal
    };
}

// Función para eliminar una parada
function eliminarParada(index) {
    const modal = new bootstrap.Modal(document.getElementById('modalConfirmarEliminar'));
    modal.show();

    const eliminarBtn = document.querySelector('#modalConfirmarEliminar .btn-danger');

    // Acción al confirmar la eliminación
    eliminarBtn.onclick = () => {
        marcadores[index].remove();  // Eliminar el marcador del mapa
        paradas.splice(index, 1);  // Eliminar la parada del array
        marcadores.splice(index, 1);  // Eliminar el marcador de la lista

        actualizarListaParadas();  // Actualizar la lista de paradas
        modal.hide();  // Cerrar el modal
    };
}

// Hacer la lista de paradas ordenable mediante drag-and-drop
new Sortable(document.getElementById('paradas-list'), {
    animation: 150,
    onEnd: ({ oldIndex, newIndex }) => {
        // Reorganizar las paradas y los marcadores al cambiar su orden
        const [parada] = paradas.splice(oldIndex, 1);
        const [marcador] = marcadores.splice(oldIndex, 1);
        paradas.splice(newIndex, 0, parada);
        marcadores.splice(newIndex, 0, marcador);
        actualizarListaParadas();  // Actualizar la lista
    }
});
