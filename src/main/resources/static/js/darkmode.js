// Módulo de Modo Escuro - Sisgetrim
(function () {
    const doc = document.documentElement;

    // Função global para alternar modo escuro
    window.toggleDarkMode = function () {
        const isDark = doc.classList.toggle('dark');
        localStorage.setItem('darkMode', isDark);
    };

    // Inicialização rápida para evitar flicker
    const darkMode = localStorage.getItem('darkMode');
    if (darkMode === 'true' || (darkMode === null && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        doc.classList.add('dark');
    }
})();
