let lastAddedProductId = null;
let toastTimeout = null;
const TOAST_DURATION = 3000; // Duración de 3 segundos



// Función para iniciar o reiniciar la cuenta atrás de ocultado
function startToastTimer() {
    clearTimeout(toastTimeout);
    toastTimeout = setTimeout(() => {
        const toastEl = document.getElementById('cartSnackbar');
        const toast = bootstrap.Toast.getInstance(toastEl);
        if (toast) {
            toast.hide();
        }
    }, TOAST_DURATION);
}

// Función para pausar el temporizador
function stopToastTimer() {
    clearTimeout(toastTimeout);
}

// Configurar los detectores de eventos (Hover) al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    const toastEl = document.getElementById('cartSnackbar');

    // Pausar el cierre automático al pasar el ratón por encima
    toastEl.addEventListener('mouseenter', () => {
        stopToastTimer();
    });

    // Reanudar el temporizador de 3 segundos al quitar el ratón
    toastEl.addEventListener('mouseleave', () => {
        startToastTimer();
    });
});

function addToCartAjax(event, formElement) {
    event.preventDefault(); // Evita la redirección o recarga de la página

    const url = formElement.action;
    const formData = new FormData(formElement);
    lastAddedProductId = formElement.getAttribute('data-product-id');
    const productName = formElement.getAttribute('data-product-name') || 'Producto';

    fetch(url, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            // Actualizar mensaje en el snackbar
            const snackbarMsg = document.querySelector('#cartSnackbar .toast-body span');
            if (snackbarMsg) {
                snackbarMsg.textContent = `"${productName}" añadido al carrito`;
            }

            // Instanciar y mostrar el Toast sin autohide nativo
            const toastEl = document.getElementById('cartSnackbar');
            const toast = bootstrap.Toast.getOrCreateInstance(toastEl, { autohide: false });
            toast.show();

            // Iniciar nuestro propio temporizador de 3 segundos
            startToastTimer();
        } else {
            alert('No se pudo añadir el producto al carrito.');
        }
    })
    .catch(error => console.error('Error al añadir al carrito:', error));
}

// Configurar acción del botón "Deshacer"
document.getElementById('undoCartBtn').addEventListener('click', function() {
    if (!lastAddedProductId) return;

    stopToastTimer(); // Cancelar temporizadores activos

    // Obtener token CSRF
    const csrfInput = document.querySelector('input[name="_csrf"]');
    const csrfParam = csrfInput ? csrfInput.name : '';
    const csrfToken = csrfInput ? csrfInput.value : '';

    const formData = new FormData();
    if (csrfParam && csrfToken) {
        formData.append(csrfParam, csrfToken);
    }

    // Petición al endpoint de eliminar del carrito
    fetch(`/cart/remove/${lastAddedProductId}`, {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            // Ocultar el toast inmediatamente tras deshacer
            const toastEl = document.getElementById('cartSnackbar');
            const toast = bootstrap.Toast.getInstance(toastEl);
            if (toast) toast.hide();
        }
    })
    .catch(error => console.error('Error al deshacer:', error));
});