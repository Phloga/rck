const allUsersUri = "/api/user/all"

async function fetchAwaitJson(uri){
    const response = await fetch(uri)
    return response.json() 
}

function fetchAllUsers() {
    return fetchAwaitJson(allUsersUri)
}

export {
    fetchAllUsers
}