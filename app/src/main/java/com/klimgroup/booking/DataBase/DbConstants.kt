package com.klimgroup.booking.DataBase

import android.provider.BaseColumns

object DbConstants {
    const val DATABASE_NAME = "Restaurant.db"
    const val DATABASE_VERSION = 1

    const val TABLE_NAME = "ebqinbtenitbn"

    const val COLUMN_NAME_TABLE_NUM = "number"
    const val COLUMN_NAME_TABLE_CREATED_DATE = "created_date"
    const val COLUMN_NAME_TABLE_PRICE = "price"
    const val COLUMN_NAME_DATE = "date"
    const val COLUMN_NAME_FROM = "from__"
    const val COLUMN_NAME_TO = "to__"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TABLE_CREATED_DATE STRING," +
            "$COLUMN_NAME_TABLE_NUM INTEGER, $COLUMN_NAME_TABLE_PRICE INTEGER, " +
            "$COLUMN_NAME_DATE STRING, $COLUMN_NAME_FROM STRING, " +
            "$COLUMN_NAME_TO STRING)"

    const val DELETE_TABLE = "DELETE TABLE IF EXISTS $TABLE_NAME"
}