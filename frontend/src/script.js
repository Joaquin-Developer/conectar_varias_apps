
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

    fetch("http://127.0.0.1:5000/api/v1/insert_persons", request)
    .then(res => console.log(res))
    .catch(error => { console.error(error); alert("Se produjo un error") });

});

document.querySelector("#get_data").addEventListener("click", async (evt) => {
    evt.preventDefault();

    const response = await fetch("http://127.0.0.1:5000/api/v1/get_persons");
    console.log(response)
    const data = await response.json();
    console.log(data);

    // fetch("http://127.0.0.1:5000/api/v1/get_persons", {method: "GET", mode: 'no-cors'})
    // .then(res => res.json())
    // .then(data => console.log(data))
    // .catch(error => console.error(error));

});
