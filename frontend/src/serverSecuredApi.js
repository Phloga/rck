const allUsersUri = "/sec-api/user/all"
const userEndpoint = "/sec-api/user/p"
const allRolesEndpoint = "/sec-api/user/roles/all"

async function fetchAwaitJson(uri){
    const response = await fetch(uri)
    return response.json() 
}

function fetchAllUsers() {
    return fetchAwaitJson(allUsersUri)
}


function fetchAllRoles() {
    const response = fetchAwaitJson(allRolesEndpoint)
    return response;
}

function sendUser(user, name) {
    const uri = userEndpoint + '/' + name
    return fetch(uri, {
        method: "PUT",
        headers: {
            "Content-Type" : "application/json",
        },
        body: JSON.stringify(user)
    })
}




export {
    fetchAllUsers, sendUser, fetchAllRoles
}