
document.querySelector("#save_person").addEventListener("click", (evt) => {
    evt.preventDefault();
    const name = document.getElementById("person_name").value;
    if (!name) {
        alert("Debe ingresar un nombre");
        return;
    }
    const request = {
        method: "POST", headers: { 'Content-Type': 'application/json' },
        mode: 'no-cors', body: JSON.stringify({ "name": name })
    };

    fetch("/api/v1/insert_persons", request)
    .then(res => res.json())
    .then(data => {
        console.log(data);
        showAlert(data.data);
    })
    .catch(error => { console.error(error); alert("Se produjo un error") });

});

function showAlert(text) {
    const alertElem = document.getElementById("alerts");
    alertElem.appendChild(document.createTextNode(text));
    setTimeout(() => {
        removeChilds(alertElem);
    }, 5000);
}

function removeChilds(elem) {
    while (elem.firstChild) { 
        elem.removeChild(elem.firstChild);
    }
}

document.querySelector("#get_data").addEventListener("click", async (evt) => {
    evt.preventDefault();
    const data = await getPersonsData();
    const registrosElem = document.querySelector("#registros");
    removeChilds(registrosElem);
    console.log(data);

    for (const elem of data) {
        registrosElem.appendChild(document.createTextNode(`${elem.id} - ${elem.name}`));
        registrosElem.appendChild(document.createElement("br"));
    }
    
});

async function getPersonsData() {
    return await (await fetch("/api/v1/get_persons")).json();
}
