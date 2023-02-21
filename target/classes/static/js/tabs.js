const tabs = document.querySelectorAll('[data-tab]')
const tabContents = document.querySelectorAll('[id^="tab"]')

tabs.forEach(tab => {
    tab.addEventListener('click', e => {
        e.preventDefault()
        const tabId = tab.getAttribute('data-tab')
        tabs.forEach(tab => tab.classList.remove('bg-white', 'text-gray-800'))
        tabContents.forEach(tabContent => tabContent.classList.add('hidden'))
        tab.classList.add('bg-white', 'text-gray-800')
        document.getElementById(tabId).classList.remove('hidden')
    })
})
document.getElementById("tab1").classList.remove("hidden");
document.querySelector("[data-tab='tab1']").classList.add('bg-white', 'text-gray-800');


/* Modal for tokens */
const tokensTab = document.querySelector("#tokens-tab");
const tokensTab2 = document.querySelector("#tokens-tab2");
const modal = document.querySelector("#modal");
const closeModal = document.querySelector("#close-modal");

tokensTab.addEventListener("click", () => {
    modal.classList.remove("hidden");
});
tokensTab2.addEventListener('click', () => {
    modal.classList.remove('hidden');
})

closeModal.addEventListener("click", () => {
    modal.classList.add("hidden");
});

modal.addEventListener("click", (e) => {
    if (e.target === modal) {
        modal.classList.add("hidden");
    }
});

function logout(){
    window.location.href = "http://localhost:8080/logout";
}