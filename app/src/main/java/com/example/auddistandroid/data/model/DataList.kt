package com.example.auddistandroid.data.model

import com.example.auddistandroid.data.model.entities.Entity

data class DataList<T : Entity> (var data: List<T>)
