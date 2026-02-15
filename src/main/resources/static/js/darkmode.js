// Função para alternar modo escuro
function toggleDarkMode() {
    const html = document.documentElement;
    const isDark = html.classList.contains('dark');
    
    if (isDark) {
        html.classList.remove('dark');
        localStorage.setItem('darkMode', 'false');
    } else {
        html.classList.add('dark');
        localStorage.setItem('darkMode', 'true');
    }
}

// Carregar preferência de modo escuro ao iniciar
document.addEventListener('DOMContentLoaded', function() {
    const darkMode = localStorage.getItem('darkMode');
    
    if (darkMode === 'true') {
        document.documentElement.classList.add('dark');
    }
});
