
// let element = document.getElementById("new-line");

function choose(id, role) {
    document.getElementById("role").value = role;
    document.getElementById("id").value = id;
}

let element = document.getElementById("v-activated");

function changeActivatedHelperFunc(activated_id) {
    document.getElementById("updated").value = activated_id;
}


function changeRoleHelper(deleted_id) {
    document.getElementById("updated_user").value = deleted_id;
}

