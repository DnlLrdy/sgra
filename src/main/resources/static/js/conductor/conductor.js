if (paradas.length > 0) {
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

            const marcadores = [];
            const bounds = [];

            paradas.forEach((parada, index) => {
                const latLng = [parada.latitud, parada.longitud];
                const color = parada.color || 'blue';

                const iconoPersonalizado = L.icon({
                    iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${color}.png`,
                    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
                    iconSize: [25, 41],
                    iconAnchor: [12, 41],
                    popupAnchor: [1, -34],
                    shadowSize: [41, 41]
                });

                const marker = L.marker(latLng, { icon: iconoPersonalizado })
                    .bindPopup(`<strong>${parada.nombre}</strong>`)
                    .addTo(map);

                marcadores.push(marker);
                bounds.push(latLng);

                marker.on('click', () => {
                    marker.openPopup();
                });
            });

            if (bounds.length > 0) {
                map.fitBounds(bounds);
            }

            document.addEventListener("DOMContentLoaded", () => {
                document.querySelectorAll('.parada-item').forEach(item => {
                    item.addEventListener('click', () => {
                        const index = parseInt(item.getAttribute('data-index'));
                        const marker = marcadores[index];
                        if (marker) {
                            map.setView(marker.getLatLng(), 18);
                            marker.openPopup();
                        }
                    });
                });
            });
        }