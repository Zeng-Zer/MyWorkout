package com.zeng.myworkout.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.io.Serializable

@Entity(tableName = "workout_exercise")
open class WorkoutExercise(
//    @ColumnInfo
//    @TypeConverters(DataConverter::class)
//    var sets: List<Int> = listOf(),
    @ColumnInfo
    var sets: Int = 0,

    @ColumnInfo
    var reps: Int = 0,

    // TODO CONVERT TO "LOAD" TO HANDLE BODYWEIGHT WEIGHT STATIC ETC
    @ColumnInfo
    var weight: Float = 0f,

    @ColumnInfo
    var order: Int = 0,

    // TODO FOREIGN KEY - DELETE CASCADE ?
    @ColumnInfo
    var workoutId: Long? = null,

    // TODO FOREIGN KEY - DELETE DO NOTHING ?
    @ColumnInfo
    var exerciseId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

) : Serializable

class WorkoutExerciseDetail(
    ex: Exercise = Exercise(),
    sets: Int = 0,
    reps: Int = 0,
    weight: Float = 0f,
    order: Int = 0,
    workoutId: Long? = null,
    exerciseId: Long? = null,
    id: Long? = null
) : WorkoutExercise(sets, reps, weight, order, workoutId, exerciseId, id) {

    @Relation(parentColumn = "exerciseId", entityColumn = "id", entity = Exercise::class)
    var exercises: List<Exercise> = listOf(ex)

    var detail: Exercise
        get() = exercises.first()
        set(ex) { exercises = listOf(ex) }

}
