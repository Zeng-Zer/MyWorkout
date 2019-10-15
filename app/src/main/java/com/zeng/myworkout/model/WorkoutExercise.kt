package com.zeng.myworkout.model

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.serialization.Serializable

@Entity(
    tableName = "workout_exercise",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workoutId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE)
    ]
)
data class WorkoutExercise(
    @ColumnInfo
    var order: Int = 0,

    @ColumnInfo(index = true)
    var workoutId: Long? = null,

    @ColumnInfo
    var exerciseId: Long? = null,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

)

data class WorkoutExerciseDetail(
    @Embedded
    val exercise: WorkoutExercise,

    @Relation(parentColumn = "exerciseId", entityColumn = "id", entity = Exercise::class)
    private var exerciseDetail: List<Exercise>

) {
    var detail: Exercise
        get() = exerciseDetail.first()
        set(ex) { exerciseDetail = listOf(ex) }

    @Ignore
    lateinit var loads: List<Load>

    @Ignore
    lateinit var loadsLiveData: LiveData<List<Load>>
}

@Serializable
data class WorkoutExerciseSerialized(
    val name: String = "",
    val loads: List<LoadSerialized> = emptyList()
)
