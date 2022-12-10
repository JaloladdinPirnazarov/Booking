package com.klimgroup.booking.DataBase

interface Actions {
    fun delete(id:Int)
    fun book(table: TableItems)
    fun click(table: TableItems)
}