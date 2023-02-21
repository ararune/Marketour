function cancelBuy() {
    document.getElementById("buyPrompt").style.display = "none";
}

async function approveBuy() {
    let tourId = tourInCart.tourId;
    let response = await fetch("http://localhost:8080/transaction/buy/" + tourId, {
        method: "post"
    });
    cancelBuy();
    tourInCart = null;
    if (response.ok) {
        await moneyCheck();
        document.location.href = "http://localhost:8080/main";
    } else {
        showToast(await response.text());
    }
}

function buy(tour) {
    tourInCart = tour;
    document.getElementById("promptText").innerText = "Are you sure you want to buy " + tour.name + " tour for " + tour.price + " tokens?";
    document.getElementById("buyPrompt").style.display = "block";
}

async function withdrawTokens() {
    let value = document.getElementById("tokensToWithdraw").value;
    if (value > 0) {
        let response = await fetch("http://localhost:8080/bank/removeTokens/" + value, {
            method: "post"
        });
        if (response.ok) {
            await moneyCheck();
        }
        showToast(await response.text());

    } else {
        showToast("Wrong input for withdrawal");
    }
}

async function addTokens() {
    let value = document.getElementById("tokensToAdd").value;
    if (value > 0) {
        await fetch("http://localhost:8080/bank/addTokens/" + value, {
            method: "post"
        });
        await moneyCheck();
        showToast("Successfully added " + value + " tokens!");
    } else {
        showToast("Error");
    }
}

moneyCheck();
moneyCheckTimer();

// Display tokens in tokens tab and in profile
async function moneyCheck() {
    let response = await fetch("http://localhost:8080/bank/getTokens", {
        method: "get",
    });
    let tokens = await response.text();
    document.getElementById("tokens").innerText = tokens.toString();
    document.getElementById("tokens2").innerText = tokens.toString();
}

function moneyCheckTimer() {
    setInterval(moneyCheck, 10000);
}