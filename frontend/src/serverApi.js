
const currentUserUri = "/api/users/self"
const unitListUri = "/api/units/all"
const commonIngredientsListUri = "/api/items/common-ingredients"
const userRoleListUri = "/api/users/roles/all"
const recipesByIngredientsUri = "/api/recipes/by-ingredients"

const userUriPrefix = "/api/users/p"

async function fetchAwaitJson(uri){
    const response = await fetch(uri)
    return response.json()
}

function fetchUser(name){
    return fetchAwaitJson(userUriPrefix + "/" + name)
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
    fetchUser,
    fetchActiveUser, 
    fetchUnitList,
    fetchCommonIngredients,
    fetchRoles,
    findByIngredients
}