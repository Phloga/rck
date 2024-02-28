
const currentUserUri = "/api/user/self"
const unitListUri = "/api/units/getAll"
const commonIngredientsListUri = "/api/items/commonIngredients";

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

export {
    fetchActiveUser, 
    fetchUnitList,
    fetchCommonIngredients
}