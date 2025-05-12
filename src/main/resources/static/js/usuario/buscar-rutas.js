document.addEventListener("DOMContentLoaded", function () {
    const map = L.map('map', {
        center: [10.395431, -75.472807], // Coordenadas iniciales
        zoom: 18,                        // Nivel de zoom inicial
        minZoom: 14,                     // Zoom mínimo
        maxZoom: 18,                     // Zoom máximo
        maxBounds: [                     // Límites del mapa
            [10.2000, -75.5700],         // Coordenadas del suroeste
            [10.5000, -75.3700]          // Coordenadas del noreste
        ],
        maxBoundsViscosity: 1.0          // Restricción suave dentro de los límites
    });

    // Añadir la capa base de OpenStreetMap
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors' // Créditos del mapa
    }).addTo(map);
});