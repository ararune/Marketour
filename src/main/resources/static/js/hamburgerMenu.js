const btn = document.getElementById('menu-btn');
const nav = document.getElementById('menu');

btn.addEventListener('click', ()=> {
    btn.classList.toggle('open');
    nav.classList.toggle('flex');
    nav.classList.toggle('hidden');
});

// Close menu when clicking anywhere outside of it
document.addEventListener('click', (event) => {
    if (!event.target.matches('#menu-btn') && !event.target.matches('#menu *')) {
        btn.classList.remove('open');
        nav.classList.remove('flex');
        nav.classList.add('hidden');
    }
});