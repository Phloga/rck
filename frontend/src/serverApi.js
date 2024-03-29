
const currentUserUri = "/api/user/self"
const unitListUri = "/api/units/all"
const commonIngredientsListUri = "/api/items/common-ingredients"
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

function findByIngredients(itemIds){
    const params = new URLSearchParams()
    params.append("items",itemIds)
    const requestUri = recipesByIngredientsUri + "?" + params.toString()
    return fetchAwaitJson(requestUri)
}

function placeholderUserCard() {
    return {userName: "", roles: []}
}

export {
    fetchUser,
    fetchActiveUser, 
    fetchUnitList,
    fetchCommonIngredients,
    findByIngredients,
    placeholderUserCard
}