const allUsersUri = "/api/users/all"
const userEndpoint = "/api/users/p"

async function fetchAwaitJson(uri){
    const response = await fetch(uri)
    return response.json() 
}

function fetchAllUsers() {
    return fetchAwaitJson(allUsersUri)
}

function sendUser(user, name) {
    const uri = userEndpoint + '/' + name
    return fetch(uri, {
        method: "POST",
        headers: {
            "Content-Type" : "application/json",
        },
        body: JSON.stringify(user)
    })
}

export {
    fetchAllUsers, sendUser
}