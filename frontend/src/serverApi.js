
const currentUserUri = "/api/users/self"
const unitListUri = "/api/units/all"
const commonIngredientsListUri = "/api/items/common-ingredients"
const userRoleListUri = "/api/users/roles/all"
const recipesByIngredientsUri = "/api/recipes/by-ingredients"

async function fetchAwaitJson(uri){
    const response = await fetch(uri)
    return response.json()
}


function fetchActiveUser() {
    return fetchAwaitJson(currentUserUri)
}

function fetchUnitList() {
    return fetchAwaitJson(unitListUri)
}

function fetchCommonIngredients() {
    return fetchAwaitJson(commonIngredientsListUri)
}

function fetchRoles(){
    return fetchAwaitJson(userRoleListUri)
}

function findByIngredients(itemIds){
    const params = new URLSearchParams()
    params.append("items",itemIds)
    const requestUri = recipesByIngredientsUri + "?" + params.toString()
    return fetchAwaitJson(requestUri)
}

export {
    fetchActiveUser, 
    fetchUnitList,
    fetchCommonIngredients,
    fetchRoles,
    findByIngredients
}