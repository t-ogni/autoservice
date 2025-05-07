package com.ktproject.autoservice.component.carList

data class CarMakeResponse(val Results: List<CarMake>)
data class CarMake(val Make_ID: Int, val Make_Name: String)

data class CarModelResponse(val Results: List<CarModel>)
data class CarModel(val Make_ID: Int, val Make_Name: String, val Model_Name: String)
