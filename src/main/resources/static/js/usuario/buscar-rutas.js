document.addEventListener('DOMContentLoaded', function () {
    const rutasData = JSON.parse(document.getElementById('rutas-data').textContent);

    const map = L.map('map').setView([10.395431, -75.472807], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
    }).addTo(map);

    let rutaLayers = {};  // Guardar las capas por cada ruta

    // Preparamos cada ruta y la guardamos (pero aún no la mostramos)
    rutasData.forEach(ruta => {
        const latlngs = [];
        const markers = [];

        ruta.paradas.forEach(parada => {
            const markerIcon = L.icon({
                iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${parada.color}.png`,
                shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
                iconSize: [25, 41],
                iconAnchor: [12, 41],
                popupAnchor: [1, -34],
                shadowSize: [41, 41]
            });

            const marker = L.marker([parada.latitud, parada.longitud], { icon: markerIcon })
                .bindPopup(`<b>${parada.nombre}</b><br><i>${ruta.nombre}</i>`);

            latlngs.push([parada.latitud, parada.longitud]);
            markers.push(marker);
        });

        const polyline = L.polyline(latlngs, { color: getColorAleatorio(), weight: 4, opacity: 0.7 });

        rutaLayers[ruta.nombre] = { markers, polyline, latlngs };
    });

    // Manejo de clics en la lista de rutas
    document.querySelectorAll('.list-group-item').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const nombreRuta = this.querySelector('span').textContent.trim();

            mostrarSoloRuta(nombreRuta);
            resaltarLinkActivo(this);
        });
    });

    // Mostrar la primera ruta automáticamente al cargar
    if (rutasData.length > 0) {
        const primeraRuta = rutasData[0].nombre;
        mostrarSoloRuta(primeraRuta);
        resaltarLinkActivo(document.querySelector('.list-group-item'));
    }

    // Función para mostrar solo una ruta
    function mostrarSoloRuta(nombreRuta) {
        // Primero, eliminamos todas las capas actuales
        Object.values(rutaLayers).forEach(obj => {
            obj.markers.forEach(m => map.removeLayer(m));
            map.removeLayer(obj.polyline);
        });

        // Luego añadimos la ruta seleccionada
        const capa = rutaLayers[nombreRuta];
        capa.markers.forEach(m => m.addTo(map));
        capa.polyline.addTo(map);

        // Ajustar el mapa a esa ruta
        const bounds = L.latLngBounds(capa.latlngs);
        map.fitBounds(bounds, { padding: [50, 50] });
    }

    // Función para resaltar el link activo
    function resaltarLinkActivo(linkActivo) {
        document.querySelectorAll('.list-group-item').forEach(l => l.classList.remove('active'));
        linkActivo.classList.add('active');
    }

});

// Colores distintos por ruta
function getColorAleatorio() {
    const colores = ['blue', 'red', 'green', 'orange', 'purple'];
    return colores[Math.floor(Math.random() * colores.length)];
}
