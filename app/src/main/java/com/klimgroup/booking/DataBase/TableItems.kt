package com.klimgroup.booking.DataBase

data class TableItems (
    var id: Int = -1,
    var createdDate: String = "",
    var number: Int = -1,
    var price: Int = -1,
    var date:String = "",
    var from: String = "free",
    var to: String = "free"
    )