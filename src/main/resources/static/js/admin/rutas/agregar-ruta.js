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
    const nombre = prompt("Ingrese el nombre de la parada");
    if (!nombre) return;

    const parada = { nombre, latitud: e.latlng.lat, longitud: e.latlng.lng };
    paradas.push(parada);

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
});

function actualizarListaParadas() {
    const lista = document.getElementById('paradas-list');
    lista.innerHTML = '';
    paradas.forEach((p, i) => {
        const li = document.createElement('li');
        li.innerHTML = `
            <div class="d-flex justify-content-between align-items-center w-100">
                <div>
                    <i class="bi bi-grip-vertical me-2 text-secondary"></i>
                    <strong>${i + 1}. ${p.nombre}</strong><br>
                    <small class="text-muted">Latitud: ${p.latitud.toFixed(5)}, Longitud: ${p.longitud.toFixed(5)}</small>
                </div>
                <div>
                    <button type="button" class="btn btn-sm btn-outline-warning" onclick="editarParada(${i})">
                        <i class="bi bi-pencil-square"></i>
                    </button>
                    <button type="button" class="btn btn-sm btn-outline-danger" onclick="eliminarParada(${i})">
                        <i class="bi bi-trash"></i>
                    </button>
                </div>
            </div>
        `;
        lista.appendChild(li);
    });

    document.getElementById('paradas-input').value = JSON.stringify(paradas);
}

function editarParada(index) {
    const nuevoNombre = prompt("Ingrese el nuevo nombre de la parada", paradas[index].nombre);
    if (nuevoNombre && nuevoNombre !== paradas[index].nombre) {
        paradas[index].nombre = nuevoNombre;
        marcadores[index].setPopupContent(nuevoNombre);
        actualizarListaParadas();
    }
}

function eliminarParada(index) {
    marcadores[index].remove();
    paradas.splice(index, 1);
    marcadores.splice(index, 1);
    actualizarListaParadas();
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
        alert("Debes agregar al menos una parada antes de guardar la ruta.");
    }
});
