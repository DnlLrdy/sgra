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

    document.getElementById('autobuses-list').innerHTML = ''; // limpiar lista de autobuses
    document.getElementById('autobuses-container').classList.add('d-none'); // ocultar contenedor

    const rutaSeleccionada = rutas.find(r => r.id === idSeleccionado); // Buscar la ruta seleccionada

    if (rutaSeleccionada) {
        mostrarParadas(rutaSeleccionada.paradas);
        const autobusesConRutaSeleccionada = autobuses.filter(bus => bus.rutaId === rutaSeleccionada.id);
        mostrarAutobuses(autobusesConRutaSeleccionada);
    
        document.getElementById("ruta-id-editar").value = idSeleccionado;
        document.getElementById("ruta-id-eliminar").value = idSeleccionado;
        document.getElementById("form-editar").style.display = "inline-block";
        document.getElementById("form-eliminar").style.display = "inline-block";
    
        // Mostrar botón de vincular autobús
        document.getElementById("btn-vincular-autobus").classList.remove("d-none");
    } else {
        document.getElementById("form-editar").style.display = "none";
        document.getElementById("form-eliminar").style.display = "none";
    
        // Ocultar botón de vincular autobús
        document.getElementById("btn-vincular-autobus").classList.add("d-none");
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

function mostrarAutobuses(autobuses) {
    const lista = document.getElementById("autobuses-list");
    const alerta = document.getElementById("alerta-sin-autobuses");
    const contenedor = document.getElementById("autobuses-container");

    lista.innerHTML = ''; // Limpiar la lista previa

    if (!autobuses || autobuses.length === 0) {
        alerta.classList.remove('d-none');
        contenedor.classList.remove('d-none');
        return;
    }

    alerta.classList.add('d-none'); // Ocultar la alerta si hay autobuses

    autobuses.forEach((autobus, index) => {
        const item = document.createElement('li');
        item.className = 'list-group-item';
        item.innerHTML = `
        <div class="text-truncate-container">
            <strong class="nombre-parada">${index + 1}. Matricula: ${autobus.matricula}</strong>
            <small class="text-muted">Estado: ${autobus.estado}</small>
        </div>
<button type="button" class="btn btn-outline-danger btn-sm ms-2" onclick="event.stopPropagation(); mostrarModalDesvincular('${autobus.id}')">
    <i class="bi-arrow-left-right"></i>
</button>

        `;
        item.addEventListener("click", () => mostrarDetallesAutobus(autobus));
        lista.appendChild(item);
    });


    contenedor.classList.remove('d-none'); // Mostrar el contenedor
}


function mostrarModalAutobuses() {
    cargarAutobusesEnModal(); // Cargar autobuses antes de mostrar el modal
    const modal = new bootstrap.Modal(document.getElementById('modalSeleccionarAutobuses'));
    modal.show();
}


function cargarAutobusesEnModal() {
    const contenedor = document.getElementById("modal-autobuses-container");
    contenedor.innerHTML = ''; // Limpiar el contenedor

    if (!autobuses || autobuses.length === 0) {
        contenedor.innerHTML = '<div class="alert alert-dark text-center">No hay autobuses disponibles.</div>';
        document.getElementById("btn-seleccionar-autobuses").classList.add("d-none");
        return;
    }

    // Filtrar autobuses sin ruta asignada
    const autobusesDisponibles = autobuses.filter(autobus => !autobus.rutaId);

    if (autobusesDisponibles.length === 0) {
        contenedor.innerHTML = '<div class="alert alert-dark text-center">Todos los autobuses están asignados a una ruta.</div>';
        document.getElementById("btn-seleccionar-autobuses").classList.add("d-none");
        return;
    }

    const selector = document.getElementById("selector-rutas");
    const rutaId = selector.value;

    const selectedOption = Array.from(selector.options).find(option => option.value === rutaId);
    const rutaNombre = selectedOption ? selectedOption.text : null;

    const inputId = document.getElementById("rutaId");
    inputId.value = rutaId;

    const inputNombre = document.getElementById("rutaNombre");
    inputNombre.value = rutaNombre;

    autobusesDisponibles.forEach((autobus) => {
        const div = document.createElement('div');
        div.className = 'form-check';
        div.innerHTML = `
<div class="d-flex align-items-center justify-content-between mb-2 p-2 border rounded-3 shadow-sm bg-light">
  <div class="d-flex align-items-center">
    <input class="form-check-input custom-checkbox me-2" type="checkbox" name="autobusesId" value="${autobus.id}" id="autobus-${autobus.id}">
    <div class="text-truncate-container">
      <strong class="nombre-parada">
        Matrícula: ${autobus.matricula} - Modelo: ${autobus.modelo} - Capacidad: ${autobus.capacidad}
      </strong>
      <div>
        <small class="text-muted">Estado: ${autobus.estado}</small>
      </div>
    </div>
  </div>
</div>
`;

        const container = document.getElementById('modal-autobuses-container');
        container.style.maxHeight = '500px'; // Limitar altura
        container.style.overflowY = 'auto';  // Habilitar desplazamiento vertical


        contenedor.appendChild(div);
    });
}

function mostrarModalDesvincular(autobusId) {
    const inputId = document.getElementById("autobusIdDesvincular");
    inputId.value = autobusId;

    const modal = new bootstrap.Modal(document.getElementById('modalConfirmarDesvinculacion'));
    modal.show();
}


function mostrarDetallesAutobus(autobus) {
    document.getElementById("detalle-autobus-modelo").textContent = autobus.modelo;
    document.getElementById("detalle-autobus-capacidad").textContent = autobus.capacidad;
    document.getElementById("detalle-conductor-nombres").textContent =
        `${autobus.conductor.primerNombre} ${autobus.conductor.segundoNombre} ${autobus.conductor.primerApellido} ${autobus.conductor.segundoApellido}`.trim();
    document.getElementById("detalle-conductor-tipoDocumento").textContent = autobus.conductor.tipoDocumento;
    document.getElementById("detalle-conductor-numeroDocumento").textContent = autobus.conductor.numeroDocumento;
    document.getElementById("detalle-conductor-estado").textContent = formatearEstado(autobus.conductor.estado);
    
    const modal = new bootstrap.Modal(document.getElementById("modalDetallesAutobus"));
    modal.show();
}

function formatearEstado(estadoEnum) {
    const texto = estadoEnum.toLowerCase().replace(/_/g, ' ');
    return texto.charAt(0).toUpperCase() + texto.slice(1);
}