// Función para limpiar campos (redirecciona para quitar parámetros)
function limpiarCampos() {
  window.location.href = "/usuario/buscar";
}

// Variables para instancias de Choices.js
let choicesInstances = {};

// Mueve el icono dentro del contenedor .choices generado por Choices.js para mejor alineación
function attachIconToChoices(selectId) {
  const select = document.getElementById(selectId);
  if (!select) return;
  const container = select.closest(".input-group-modern");
  if (!container) return;
  const icon = container.querySelector(".input-icon");
  // La librería Choices reemplaza el select por un nuevo DOM; buscamos el elemento .choices que crea
  const choicesEl = container.querySelector(".choices");
  if (choicesEl && icon) {
    // Insertar el icono dentro del contenedor generado por Choices para que quede alineado
    choicesEl.insertBefore(icon, choicesEl.firstChild);
  }
}

// Inicializar Choices.js después de cargar paradas
document.addEventListener("DOMContentLoaded", async () => {
  try {
    const response = await fetch("/usuario/paradas/todas");
    const paradas = await response.json();

    const ubicacionSelect = document.getElementById("ubicacion");
    const destinoSelect = document.getElementById("destino");

    // Agregar opciones a los selects
    paradas.forEach((parada) => {
      const option1 = document.createElement("option");
      option1.value = parada;
      option1.textContent = parada;
      ubicacionSelect.appendChild(option1);

      const option2 = document.createElement("option");
      option2.value = parada;
      option2.textContent = parada;
      destinoSelect.appendChild(option2);
    });

    // Inicializar Choices.js para búsqueda
    choicesInstances.ubicacion = new Choices(ubicacionSelect, {
      searchEnabled: true,
      searchPlaceholderValue: "Buscar ubicación...",
      noResultsText: "No se encontraron resultados",
      noChoicesText: "No hay opciones disponibles",
      itemSelectText: "Seleccionar",
      placeholder: true,
      placeholderValue: "Selecciona ubicación de origen",
      shouldSort: false,
      position: "bottom",
    });

    choicesInstances.destino = new Choices(destinoSelect, {
      searchEnabled: true,
      searchPlaceholderValue: "Buscar destino...",
      noResultsText: "No se encontraron resultados",
      noChoicesText: "No hay opciones disponibles",
      itemSelectText: "Seleccionar",
      placeholder: true,
      placeholderValue: "Selecciona destino",
      shouldSort: false,
      position: "bottom",
    });

    // Mover iconos dentro del DOM generado por Choices para alinear correctamente
    attachIconToChoices("ubicacion");
    attachIconToChoices("destino");

    // Mantener valores seleccionados si existen (de th:value)
    const ubicacionValue = "[[${ubicacion}]]";
    const destinoValue = "[[${destino}]]";
    if (ubicacionValue) {
      choicesInstances.ubicacion.setChoiceByValue(ubicacionValue);
    }
    if (destinoValue) {
      choicesInstances.destino.setChoiceByValue(destinoValue);
    }
  } catch (error) {
    console.error("Error cargando paradas:", error);
  }
});

// Inicializar el mapa
const map = L.map("map", {
  center: [10.395431, -75.472807],
  zoom: 18,
  minZoom: 10,
  maxZoom: 18,
  maxBounds: [
    [10.2, -75.57],
    [10.5, -75.37],
  ],
  maxBoundsViscosity: 1.0,
});

// Capa base
L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png").addTo(map);

// Variables globales
let marcadores = [];

// Limpiar marcadores del mapa
function limpiarMarcadores() {
  marcadores.forEach((marker) => map.removeLayer(marker));
  marcadores = [];
}

// Mostrar paradas en el mapa + lista
function mostrarParadas(paradas) {
  if (!paradas || paradas.length === 0) return;

  limpiarMarcadores();

  const bounds = [];
  const lista = document.getElementById("paradas-list");
  if (lista) lista.innerHTML = "";

  paradas.forEach((parada, index) => {
    const latLng = [parada.latitud, parada.longitud];

    // Crear un ícono personalizado según el color de la parada
    const iconoColor = L.icon({
      iconUrl: `https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-${parada.color}.png`,
      shadowUrl:
        "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    const marker = L.marker(latLng, { icon: iconoColor })
      .bindPopup(`<strong>${parada.nombre}</strong>`)
      .addTo(map);

    marcadores.push(marker);
    bounds.push(latLng);

    if (lista) {
      const isLongName = parada.nombre.length > 30;
      const item = document.createElement("li");
      item.className = "list-group-item";
      item.innerHTML = `
                <div class="text-truncate-container">
                    <strong class="nombre-parada" ${
                      isLongName
                        ? 'data-bs-toggle="tooltip" data-bs-placement="top" title="' +
                          parada.nombre +
                          '"'
                        : ""
                    }>
                        ${index + 1}. ${parada.nombre}
                    </strong>
                    <small class="text-muted">Lat: ${parada.latitud.toFixed(
                      5
                    )}, Lon: ${parada.longitud.toFixed(5)}</small>
                </div>
            `;

      item.addEventListener("click", () => {
        map.setView(latLng, 18);
        marker.openPopup();
      });

      lista.appendChild(item);
    }
  });

  if (bounds.length > 0) {
    map.fitBounds(bounds, { padding: [50, 50] });
  }

  // Inicializar tooltips de Bootstrap para nombres largos
  document
    .querySelectorAll('[data-bs-toggle="tooltip"]')
    .forEach((el) => new bootstrap.Tooltip(el));
}

// Crear un pane para que el marcador del usuario esté siempre encima
map.createPane("usuarioPane");
map.getPane("usuarioPane").style.zIndex = 650;

let marcadorUbicacion;

// Icono animado con DivIcon
const iconoUsuario = L.divIcon({
  className: "", // vacío, todo el estilo está en HTML/CSS
  html: '<div class="ubicacion-usuario"></div>',
  iconSize: [30, 30],
  iconAnchor: [15, 15],
  popupAnchor: [0, -15],
});

// Función para ubicación en tiempo real
function mostrarUbicacion() {
  if (!navigator.geolocation) {
    alert("Tu navegador no soporta geolocalización.");
    return;
  }

  navigator.geolocation.watchPosition(
    (position) => {
      const latlng = [position.coords.latitude, position.coords.longitude];

      if (marcadorUbicacion) {
        marcadorUbicacion.setLatLng(latlng);
      } else {
        marcadorUbicacion = L.marker(latlng, {
          icon: iconoUsuario,
          pane: "usuarioPane",
        })
          .addTo(map)
          .bindPopup("¡Estás aquí!")
          .openPopup();

        map.setView(latlng, 18);
      }
    },
    (err) => {
      console.warn("Error de geolocalización: " + err.message);
    },
    {
      enableHighAccuracy: true,
      maximumAge: 0,
      timeout: 5000,
    }
  );
}

// Ejecutar la función
mostrarUbicacion();

// Mostrar autobuses en lista
function mostrarAutobuses(autobuses) {
  const lista = document.getElementById("autobuses-list");
  if (!lista) return;

  lista.innerHTML = "";

  if (!autobuses || autobuses.length === 0) {
    lista.innerHTML = `<li class="list-group-item text-muted text-center">No hay autobuses asignados</li>`;
    return;
  }

  autobuses.forEach((bus, index) => {
    const item = document.createElement("li");
    item.className = "list-group-item";
    item.innerHTML = `
            <strong>${index + 1}. Matrícula:</strong> ${bus.matricula}<br>
            <small class="text-muted">Modelo: ${bus.modelo}</small>
        `;
    lista.appendChild(item);
  });
}

// Escuchar clics en las rutas
document.addEventListener("DOMContentLoaded", function () {
  const rutas = document.querySelectorAll(".ruta-item");

  rutas.forEach((ruta) => {
    ruta.addEventListener("click", function (e) {
      e.preventDefault();
      const rutaId = this.dataset.id;

      rutas.forEach((r) => r.classList.remove("active"));
      this.classList.add("active");

      // Cargar paradas
      fetch(`/usuario/ruta/${rutaId}/paradas`)
        .then((response) => response.json())
        .then((paradas) => {
          mostrarParadas(paradas);
        })
        .catch((error) => {
          console.error("Error al cargar las paradas:", error);
        });

      // Cargar autobuses
      fetch(`/usuario/ruta/${rutaId}/autobuses`)
        .then((response) => response.json())
        .then((autobuses) => {
          mostrarAutobuses(autobuses);
        })
        .catch((error) => {
          console.error("Error al cargar los autobuses:", error);
        });
    });
  });
});
