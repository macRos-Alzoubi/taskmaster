package com.example.taskmaster;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM taskmodel")
    List<TaskModel> findAll();

    @Query("SELECT * FROM taskmodel WHERE id = :id")
    TaskModel findById(Long id);

    @Insert
    Long insertTask(TaskModel taskModel);

    @Delete
    void deleteTask(TaskModel taskModel);
}
