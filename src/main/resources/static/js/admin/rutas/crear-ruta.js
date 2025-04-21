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
    document.getElementById('nombre-parada').value = '';

    const modal = new bootstrap.Modal(document.getElementById('modalCrearParada'));
    modal.show();

    const ubicacion = { lat: e.latlng.lat, lng: e.latlng.lng };

    document.getElementById('guardar-parada').onclick = function () {
        const nombre = document.getElementById('nombre-parada').value.trim();
        const color = document.getElementById('color-parada').value;

        if (nombre) {
            const parada = { nombre, latitud: ubicacion.lat, longitud: ubicacion.lng, color };
            paradas.push(parada);

            const iconoPersonalizado = L.icon({
                iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${color}.png`,
                shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });

            const marcador = L.marker([parada.latitud, parada.longitud], {
                draggable: true,
                icon: iconoPersonalizado
            })
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
            modal.hide();
        } else {
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
        if (!alerta) {
            const div = document.createElement("div");
            div.id = "sin-paradas-alerta";
            div.className = "alert alert-primary text-center";
            div.innerHTML = `<strong>¡No hay paradas creadas!</strong><br>Haz clic derecho en el mapa para crear una.`;
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

    document.querySelectorAll('[data-bs-toggle="tooltip"]').forEach(el => new bootstrap.Tooltip(el));

    document.getElementById('paradas-input').value = JSON.stringify(paradas);
}


function editarParada(index) {
    const paradaIndex = index;

    const nombreParada = paradas[paradaIndex].nombre;

    document.getElementById('nombre-parada-edit').value = nombreParada;
    document.getElementById('color-parada-edit').value = paradas[paradaIndex].color;

    const modal = new bootstrap.Modal(document.getElementById('modalEditarParada'));
    modal.show();

    const guardarBtn = document.getElementById('guardar-edicion');
    guardarBtn.onclick = () => guardarEdicion(paradaIndex);
}

function guardarEdicion(paradaIndex) {
    const nuevoNombre = document.getElementById('nombre-parada-edit').value.trim();
    const nuevoColor = document.getElementById('color-parada-edit').value;

    const parada = paradas[paradaIndex];
    let hayCambios = false;

    if (nuevoNombre && nuevoNombre !== parada.nombre) {
        parada.nombre = nuevoNombre;
        marcadores[paradaIndex].setPopupContent(nuevoNombre);
        hayCambios = true;
    }

    if (nuevoColor && nuevoColor !== parada.color) {
        parada.color = nuevoColor;

        const nuevoIcono = L.icon({
            iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${nuevoColor}.png`,
            shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
            iconSize: [25, 41],
            iconAnchor: [12, 41],
            popupAnchor: [1, -34],
            shadowSize: [41, 41]
        });

        marcadores[paradaIndex].setIcon(nuevoIcono);
        hayCambios = true;
    }

    if (hayCambios) {
        actualizarListaParadas();
    }

    const modal = bootstrap.Modal.getInstance(document.getElementById('modalEditarParada'));
    modal.hide();
}

function eliminarParada(index) {
    const paradaIndex = index;

    const modal = new bootstrap.Modal(document.getElementById('modalConfirmarEliminar'));
    modal.show();

    const eliminarBtn = document.querySelector('#modalConfirmarEliminar .btn-danger');
    eliminarBtn.onclick = () => confirmarEliminacion(paradaIndex);
}

function confirmarEliminacion(paradaIndex) {
    marcadores[paradaIndex].remove();
    paradas.splice(paradaIndex, 1);
    marcadores.splice(paradaIndex, 1);

    actualizarListaParadas();

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
        e.preventDefault();
        const modal = new bootstrap.Modal(document.getElementById('modalConfirmarParadas'));
        modal.show();
    }
});
