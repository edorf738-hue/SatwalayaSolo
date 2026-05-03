package com.example.satwalaya.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookingDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "satwalaya_booking.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE bookings (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                service_name TEXT NOT NULL,
                pet_name TEXT NOT NULL,
                pet_type TEXT NOT NULL,
                room_type TEXT,
                start_date TEXT,
                end_date TEXT,
                nights INTEGER,
                total_price INTEGER,
                status TEXT
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS bookings")
        onCreate(db)
    }

    fun insertBooking(booking: Booking): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put("service_name", booking.serviceName)
            put("pet_name", booking.petName)
            put("pet_type", booking.petType)
            put("room_type", booking.roomType)
            put("start_date", booking.startDate)
            put("end_date", booking.endDate)
            put("nights", booking.nights)
            put("total_price", booking.totalPrice)
            put("status", booking.status)
        }

        return db.insert("bookings", null, values)
    }

    fun getActiveBookings(): List<Booking> {
        val bookings = mutableListOf<Booking>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM bookings WHERE status = ? ORDER BY id DESC",
            arrayOf("Active")
        )

        cursor.use {
            while (it.moveToNext()) {
                bookings.add(
                    Booking(
                        serviceName = it.getString(it.getColumnIndexOrThrow("service_name")),
                        petName = it.getString(it.getColumnIndexOrThrow("pet_name")),
                        petType = it.getString(it.getColumnIndexOrThrow("pet_type")),
                        roomType = it.getString(it.getColumnIndexOrThrow("room_type")),
                        startDate = it.getString(it.getColumnIndexOrThrow("start_date")),
                        endDate = it.getString(it.getColumnIndexOrThrow("end_date")),
                        nights = it.getInt(it.getColumnIndexOrThrow("nights")),
                        totalPrice = it.getInt(it.getColumnIndexOrThrow("total_price")),
                        status = it.getString(it.getColumnIndexOrThrow("status"))
                    )
                )
            }
        }

        return bookings
    }
}