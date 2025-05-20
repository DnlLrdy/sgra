<script>
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

    // Ícono personalizado
    const iconoPersonalizado = L.icon({
        iconUrl: 'https://cdn-icons-png.flaticon.com/512/684/684908.png',
        iconSize: [32, 32],
        iconAnchor: [16, 32],
        popupAnchor: [0, -32]
    });

    // Limpiar marcadores del mapa
    function limpiarMarcadores() {
        marcadores.forEach(marker => map.removeLayer(marker));
        marcadores = [];
    }

    // Mostrar paradas en el mapa + lista
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

            if (lista) {
                const item = document.createElement('li');
                item.className = 'list-group-item';
                item.textContent = `${index + 1}. ${parada.nombre}`;

                item.addEventListener('click', () => {
                    map.setView(latLng, 18);
                    marker.openPopup();
                });

                lista.appendChild(item);
            }
        });

        if (bounds.length > 0) {
            map.fitBounds(bounds, { padding: [50, 50] });
        }
    }

    // Mostrar autobuses en lista
    function mostrarAutobuses(autobuses) {
        const lista = document.getElementById("autobuses-list");
        if (!lista) return;

        lista.innerHTML = '';

        if (!autobuses || autobuses.length === 0) {
            lista.innerHTML = `<li class="list-group-item text-muted text-center">No hay autobuses asignados</li>`;
            return;
        }

        autobuses.forEach((bus, index) => {
            const item = document.createElement('li');
            item.className = 'list-group-item';
            item.innerHTML = `
                <strong>${index + 1}. Matrícula:</strong> ${bus.matricula}<br>
                <small class="text-muted">Modelo: ${bus.modelo}</small>
            `;
            lista.appendChild(item);
        });
    }

    // Escuchar clics en las rutas
    document.addEventListener("DOMContentLoaded", function () {
        const rutas = document.querySelectorAll('.ruta-item');

        rutas.forEach(ruta => {
            ruta.addEventListener('click', function (e) {
                e.preventDefault();
                const rutaId = this.dataset.id;

                // Cargar paradas
                fetch(`/sgra/usuario/ruta/${rutaId}/paradas`)
                    .then(response => response.json())
                    .then(paradas => {
                        mostrarParadas(paradas);
                    })
                    .catch(error => {
                        console.error('Error al cargar las paradas:', error);
                    });

                // Cargar autobuses
                fetch(`/sgra/usuario/ruta/${rutaId}/autobuses`)
                    .then(response => response.json())
                    .then(autobuses => {
                        mostrarAutobuses(autobuses);
                    })
                    .catch(error => {
                        console.error('Error al cargar los autobuses:', error);
                    });
            });
        });
    });
</script>
