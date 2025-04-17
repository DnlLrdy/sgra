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


const paradas = [], marcadores = [];

map.on('contextmenu', e => {
    // Limpiar el campo de texto del modal cada vez que se abra
    document.getElementById('nombre-parada').value = '';

    // Mostrar el modal para crear la parada
    const modal = new bootstrap.Modal(document.getElementById('modalCrearParada'));
    modal.show();

    // Guardar la ubicación de la parada al abrir el modal
    const ubicacion = { lat: e.latlng.lat, lng: e.latlng.lng };

    // Agregar la parada al hacer clic en el botón de "Crear Parada"
    document.getElementById('guardar-parada').onclick = function () {
        const nombre = document.getElementById('nombre-parada').value.trim();

        if (nombre) {
            // Crear la parada con la información de nombre y ubicación
            const parada = { nombre, latitud: ubicacion.lat, longitud: ubicacion.lng };
            paradas.push(parada);

            // Crear el marcador en el mapa
            const marcador = L.marker([parada.latitud, parada.longitud], { draggable: true })
                .addTo(map)
                .bindPopup(nombre)
                .on('dragend', event => {
                    const { lat, lng } = event.target.getLatLng();
                    parada.latitud = lat;
                    parada.longitud = lng;
                    marcador.setPopupContent(parada.nombre);
                    actualizarListaParadas();
                });

            marcadores.push(marcador);
            actualizarListaParadas();

            // Cerrar el modal después de agregar la parada
            modal.hide();
        } else {
            // Si no se ingresa un nombre, simplemente cerrar el modal sin alerta
            modal.hide();
        }
    };
});


function actualizarListaParadas() {
    const lista = document.getElementById('paradas-list');
    lista.innerHTML = '';

    const textoArrastrar = document.getElementById('texto-arrastrar');
    const alerta = document.getElementById('sin-paradas-alerta');

    if (paradas.length === 0) {
        // Mostrar mensaje de alerta si no hay paradas
        if (!alerta) {
            const div = document.createElement("div");
            div.id = "sin-paradas-alerta";
            div.className = "alert alert-dark text-center";
            div.innerHTML = `<strong>¡No hay paradas creadas!</strong><br>Haz clic en el mapa para agregar una.`;
            lista.appendChild(div);
        }
        if (textoArrastrar) textoArrastrar.style.display = "none";
    } else {
        if (alerta) alerta.remove();
        if (textoArrastrar) textoArrastrar.style.display = "block";
    }

    paradas.forEach((p, i) => {
        const isLongName = p.nombre.length > 30;
        const li = document.createElement('li');
        li.className = 'list-group-item';
        li.innerHTML = `
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
          <button type="button" class="btn btn-sm btn-outline-warning" onclick="editarParada(${i})">
            <i class="bi bi-pencil-square"></i>
          </button>
          <button type="button" class="btn btn-sm btn-outline-danger" onclick="eliminarParada(${i})">
            <i class="bi bi-trash"></i>
          </button>
        </div>
      `;
        li.addEventListener('click', () => {
            const marker = marcadores[i];
            const latLng = [p.latitud, p.longitud];
            map.setView(latLng, 18);
            marker.openPopup();
        });

        lista.appendChild(li);
    });

    // Inicializar tooltips para nombres largos
    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(el => new bootstrap.Tooltip(el));

    document.getElementById('paradas-input').value = JSON.stringify(paradas);
}


function editarParada(index) {
    // Guardar el índice de la parada seleccionada para usarlo después
    const paradaIndex = index;

    // Obtener el nombre de la parada a editar
    const nombreParada = paradas[paradaIndex].nombre;

    // Rellenar el campo de texto del modal con el nombre actual
    document.getElementById('nombre-parada-edit').value = nombreParada;

    // Mostrar el modal de edición
    const modal = new bootstrap.Modal(document.getElementById('modalEditarParada'));
    modal.show();

    // Añadir un listener para el botón "Guardar" del modal
    const guardarBtn = document.getElementById('guardar-edicion');
    guardarBtn.onclick = () => guardarEdicion(paradaIndex);
}

function guardarEdicion(paradaIndex) {
    // Obtener el nuevo nombre desde el campo de texto
    const nuevoNombre = document.getElementById('nombre-parada-edit').value.trim();

    // Si el nuevo nombre no está vacío y es diferente al anterior, actualizar
    if (nuevoNombre && nuevoNombre !== paradas[paradaIndex].nombre) {
        // Actualizar el nombre de la parada
        paradas[paradaIndex].nombre = nuevoNombre;

        // Actualizar el marcador en el mapa
        marcadores[paradaIndex].setPopupContent(nuevoNombre);

        // Actualizar la lista de paradas
        actualizarListaParadas();
    }

    // Cerrar el modal
    const modal = bootstrap.Modal.getInstance(document.getElementById('modalEditarParada'));
    modal.hide();
}

function eliminarParada(index) {
    // Guardar el índice de la parada seleccionada para usarlo después
    const paradaIndex = index;

    // Mostrar el modal de confirmación
    const modal = new bootstrap.Modal(document.getElementById('modalConfirmarEliminar'));
    modal.show();

    // Añadir un listener para el botón "Eliminar" del modal
    const eliminarBtn = document.querySelector('#modalConfirmarEliminar .btn-danger');
    eliminarBtn.onclick = () => confirmarEliminacion(paradaIndex);
}

function confirmarEliminacion(paradaIndex) {
    // Eliminar el marcador y la parada de las listas
    marcadores[paradaIndex].remove();
    paradas.splice(paradaIndex, 1);
    marcadores.splice(paradaIndex, 1);

    // Actualizar la lista de paradas en la interfaz
    actualizarListaParadas();

    // Cerrar el modal después de la eliminación
    const modal = bootstrap.Modal.getInstance(document.getElementById('modalConfirmarEliminar'));
    modal.hide();
}

new Sortable(document.getElementById('paradas-list'), {
    animation: 150,
    onEnd: ({ oldIndex, newIndex }) => {
        const [parada] = paradas.splice(oldIndex, 1);
        const [marcador] = marcadores.splice(oldIndex, 1);
        paradas.splice(newIndex, 0, parada);
        marcadores.splice(newIndex, 0, marcador);
        actualizarListaParadas();
    }
});


document.querySelector("form").addEventListener("submit", e => {
    if (paradas.length === 0) {
        e.preventDefault(); // Prevenir el envío del formulario
        const modal = new bootstrap.Modal(document.getElementById('modalConfirmarParadas'));
        modal.show(); // Mostrar el modal personalizado
    }
});

