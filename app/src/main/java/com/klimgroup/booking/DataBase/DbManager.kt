package com.klimgroup.booking.DataBase

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import android.util.Log

class DbManager(private val context: Context) {
    private val dbHelper = DbHelper(context)
    private var db : SQLiteDatabase? = null

    fun openDb(){ db = dbHelper.writableDatabase }
    fun closeDb(){ db?.close() }

    fun insertTable(table:TableItems){
        DbConstants.apply {
            val values = ContentValues().apply {
                put(COLUMN_NAME_TABLE_NUM,table.number)
                put(COLUMN_NAME_TABLE_CREATED_DATE,table.createdDate)
                put(COLUMN_NAME_TABLE_PRICE,table.price)
                put(COLUMN_NAME_DATE,table.date)
                put(COLUMN_NAME_FROM,table.from)
                put(COLUMN_NAME_TO,table.to)
                Log.d("table","insert function: $table")
            }
            val i = db?.insert(TABLE_NAME,null, values)
        }
    }

    fun updateTable(table:TableItems){
        val selection = "${BaseColumns._ID} = ${table.id}"
        DbConstants.apply {
            val values = ContentValues().apply {
                put(COLUMN_NAME_TABLE_CREATED_DATE,table.createdDate)
                put(COLUMN_NAME_TABLE_NUM,table.number)
                put(COLUMN_NAME_TABLE_PRICE,table.price)
                put(COLUMN_NAME_DATE,table.date)
                put(COLUMN_NAME_FROM,table.from)
                put(COLUMN_NAME_TO,table.to)
            }
            db?.update(TABLE_NAME,values, selection,null)
        }
    }

    fun getTables():ArrayList<TableItems>{
        val tablesList = ArrayList<TableItems>()
        val cursor = db?.query(DbConstants.TABLE_NAME,null, null, null, null,null,null)
        while (cursor?.moveToNext()!!){
            DbConstants.apply {
                val table = TableItems()
                var index = cursor.getColumnIndex(BaseColumns._ID)
                table.id = cursor.getInt(index)
                index = cursor.getColumnIndex(COLUMN_NAME_TABLE_CREATED_DATE)
                table.createdDate = cursor.getString(index)
                index = cursor.getColumnIndex(COLUMN_NAME_TABLE_NUM)
                table.number = cursor.getInt(index)
                index = cursor.getColumnIndex(COLUMN_NAME_TABLE_PRICE)
                table.price = cursor.getInt(index)
                index = cursor.getColumnIndex(COLUMN_NAME_DATE)
                table.date = cursor.getString(index)
                index = cursor.getColumnIndex(COLUMN_NAME_FROM)
                table.from = cursor.getString(index)
                index = cursor.getColumnIndex(COLUMN_NAME_TO)
                table.to = cursor.getString(index)
                Log.d("table","getTable function: $table")
                tablesList.add(table)
            }
        }
                cursor.close()
        return tablesList
    }

    fun deleteATable(id:Int){
        val selection = "${BaseColumns._ID} = $id"
        db?.delete(DbConstants.TABLE_NAME,selection,null)
    }

    @SuppressLint("SQLiteString")
    fun deleteAll(){
        DbConstants.apply {
            db?.execSQL(DELETE_TABLE)
            db?.execSQL(CREATE_TABLE)
        }
    }

}