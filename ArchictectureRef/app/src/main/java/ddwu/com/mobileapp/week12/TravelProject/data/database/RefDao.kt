package ddwu.com.mobileapp.week12.TravelProject.data.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RefDao {
    @Query ("SELECT * FROM ref_table")
    fun getAllRefs() : Flow<List<RefEntity>>

    @Query ("SELECT name FROM ref_table WHERE _id = :id")
    fun getNameById(id: Int) : String
}