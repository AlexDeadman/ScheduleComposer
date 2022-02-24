package com.example.auddistandroid.data.model

import com.example.auddistandroid.data.model.entity.Entity

data class DataList<T : Entity> (var data: List<T>)
