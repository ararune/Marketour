function hideToast(li){
    li.classList.add("hidden");
    let toastNotifications = document.getElementsByClassName("toast-notification");
    let count = 0;
    for(let i = 0; i < toastNotifications.length; i++){
        if(!toastNotifications[i].classList.contains("hidden")){
            count++;
        }
        toastNotifications[i].style.top = (i * 70) + "px";
    }
    if(count === 0){
        document.getElementById("toast").classList.add("hidden");
    }
}
function showToast(textToDisplay) {
    let toast = document.getElementById("toastList");
    let li = document.createElement("li");
    li.classList.add("toast-notification", "flex", "items-center", "py-2", "px-4");
    let path = document.createElementNS("http://www.w3.org/2000/svg", "path");
    path.setAttribute("d", "M5 13l4 4L19 7");
    let text = document.createTextNode(textToDisplay);
    let div = document.createElement("div");
    div.appendChild(text);
    li.appendChild(div);
    li.addEventListener("click", () => {
        hideToast(li);
    });
    setTimeout(() => {
        hideToast(li);
    }, 8000);
    toast.appendChild(li);
    document.getElementById("toast").classList.remove("hidden");
}
// Hide all toasts on X
function hideAllToasts() {
    let toastNotifications = document.getElementsByClassName("toast-notification");
    for(let i = 0; i < toastNotifications.length; i++){
        hideToast(toastNotifications[i]);
    }
}
document.getElementById("hideAllToasts").addEventListener("click", hideAllToasts);