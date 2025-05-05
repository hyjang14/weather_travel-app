package ddwu.com.mobileapp.week12.TravelProject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ReviewDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "reviews.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "reviews"
        private const val COLUMN_ID = "id"
        private const val COLUMN_PLACE = "place"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_DIARY = "diary"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_PLACE TEXT, "
                + "$COLUMN_RATING REAL, "
                + "$COLUMN_DATE TEXT, "
                + "$COLUMN_DIARY TEXT)")
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertReview(review: Review): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("place", review.place)
            put("rating", review.rating)
            put("date", review.date)
            put("diary", review.diary)
        }
        val result = db.insert("reviews", null, values)
        db.close()
        return result
    }


    fun getAllReviews(): List<Review> {
        val reviews = mutableListOf<Review>()
        // ID 기준으로 내림차순 정렬
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_ID DESC"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val place = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLACE))
                val rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val diary = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIARY))
                val review = Review(id, place, rating, date, diary)
                reviews.add(review)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return reviews
    }

    fun deleteReview(id: Long): Boolean {
        val db = this.writableDatabase
        val result = db.delete("reviews", "id = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

}
