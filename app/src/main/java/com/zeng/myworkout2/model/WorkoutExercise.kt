package com.zeng.myworkout2.model

import androidx.room.*
import com.zeng.myworkout2.util.DataConverter
import java.io.Serializable

@Entity(tableName = "workout_exercise")
open class WorkoutExerciseSql(
    @ColumnInfo
    @TypeConverters(DataConverter::class)
    var sets: List<Int> = listOf(),

    @ColumnInfo
    var weight: Float = 0f,

    @ColumnInfo
    var order: Int = 0,

    @ColumnInfo
    var workoutId: Long? = null,

    @ColumnInfo
    var exerciseId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable

class WorkoutExercise(
    ex: Exercise = Exercise(),
    sets: List<Int> = listOf(),
    weight: Float = 0f,
    order: Int = 0,
    workoutId: Long? = null,
    exerciseId: Long? = null,
    id: Long? = null
) : WorkoutExerciseSql(sets, weight, order, workoutId, exerciseId, id) {

    @Relation(parentColumn = "exerciseId", entityColumn = "id", entity = Exercise::class)
    var exercises: List<Exercise> = listOf(ex)

    var exercise : Exercise
        get() = exercises.first()
        set(ex) {
            exercises = listOf(ex)
        }

}
